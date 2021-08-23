package pro.boyu.dongxin.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import pro.boyu.dongxin.framework.annotations.*;
import pro.boyu.dongxin.framework.executor.SyncTestCaseExecutor;

import pro.boyu.dongxin.framework.exception.*;
import pro.boyu.dongxin.framework.executor.manager.SyncTestMethodExecutorManager;
import pro.boyu.dongxin.framework.infobean.TestMethodInvokeInfo;
import pro.boyu.dongxin.utils.Subject;

/**
 *
 */

public class OrderedPackageLoader extends AbstractPackageLoader {
	static final Logger logger = LoggerFactory.getLogger(OrderedPackageLoader.class);
	@Deprecated
	static Object lock = new Object();
	
	public static Object loggerLockObject=new Object();
	private Map<Integer, List<SyncTestCaseExecutor>> syncExecutorMap = new HashMap<Integer, List<SyncTestCaseExecutor>>();
	private Map<Class<?>, Object> testClassInstanceMap = new HashMap<>();
	private Map<Class<?>, Object> serviceClassInstanceMap = new HashMap<>();
	private SyncTestMethodExecutorManager syncManager = new SyncTestMethodExecutorManager();

	protected OrderedPackageLoader(Class<?> c) {
		super(c);
	}

	@Override
	protected void processIocContainer() {
		Map<String, Class<?>> iocContainer = this.getIocContainer();
		// 会优先初始化Service注解的类，在完全
		for (String key : iocContainer.keySet()) {
			Class<?> clazz = iocContainer.get(key);
			try {
				clazz = Class.forName(key);
				if (clazz.isAnnotationPresent(Service.class)) {
					processService(clazz);
				} else if (clazz.isAnnotationPresent(TestClass.class)) {
					Constructor<?> constructor = clazz.getConstructor();
					constructor.setAccessible(true);
					Object target = constructor.newInstance();
					// this.processClazz(clazz,target);
					this.testClassInstanceMap.put(clazz, target);
				}

			} catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException
					| InstantiationException e) {
				logger.error("program exit because of " + e.getMessage());
				System.exit(0);
			} catch (Exception e1) {
				logger.error(e1.getMessage());
			}
		}

		// executing test class
		for (Class<?> clazz: testClassInstanceMap.keySet()) {
			try {
				this.processTestClazz(clazz, testClassInstanceMap.get(clazz));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * description：初始化Service类，当前Service类不能自动注入任何依赖 author：zhangying date:2021/08/13
	 */
	private void processService(Class<?> clazz) throws ClassNotFoundException, NoSuchMethodException,
			InvocationTargetException, IllegalAccessException, InstantiationException {
		Class.forName(clazz.getName());
		Object instance = null;
		// 如果注解了初始化方法则调用初始化方法（必须为静态方法，不自动注入任何参数），如果没有检测到初始化方法，则默认调用无参的构造方法
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.isAnnotationPresent(InitMethod.class)) {
				m.setAccessible(true);
				instance = m.invoke(null);
				break;
			}
		}
		if (instance == null) {
			Constructor<?> constructor = clazz.getConstructor();
			constructor.setAccessible(true);
			instance = constructor.newInstance();
		}
		this.serviceClassInstanceMap.put(clazz, instance);
	}

	private void processTestInit(Method m, Object target, String className) {
		logger.info("Test Class {} is initting", className);
		try {
			this.injectArguments(m, target);
		} catch (Exception e) {
			logger.error("Test Class {} init failed because of {} ", className, e.getMessage());
		}
	}

	protected void processTestClazz(Class<?> clazz, Object target)
			throws InvocationTargetException, IllegalAccessException {
		SyncTestMethodExecutorManager syncManager = new SyncTestMethodExecutorManager();
		Map<Integer, List<Method>> testMethodsMap = new HashMap<>();
		TestClassManager testClazzExecutor = new TestClassManager(target);
		// 扫描TestClazz下所有方法，首先处理带有TestInit注解的初始化工作，支持注入注册过的Service
		for (Method method : target.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(TestInit.class)) {
				method.setAccessible(true);
				method.invoke(target, dependencyDetection(method, target));
			}
			if (method.isAnnotationPresent(TestMethod.class)) {

				processSyncTestMethod(target, method, clazz);
			}
		}
	}

	protected void processSyncTestMethod(Object target, Method m, Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
		for (Annotation annotation : m.getAnnotations()) {
			if (annotation instanceof TestMethod) {
				// retrieve info
				TestMethod testMethod = (TestMethod) annotation;
				TestMethodInvokeInfo info = new TestMethodInvokeInfo(target, m, testMethod, dependencyDetection(m, target));
				syncManager.addMethod(info);
				syncManager.exec();
			}
		}
	}

	protected Object[] dependencyDetection(Method m, Object target)
			throws InvocationTargetException, IllegalAccessException {
		List<Object> objects = new LinkedList<>();
		for (Parameter parameter : m.getParameters()) {
			Class<?> clazz = parameter.getClass();
			objects.add(
					this.serviceClassInstanceMap.containsKey(clazz) ? this.serviceClassInstanceMap.get(clazz) : null);
		}
		return objects.toArray();
	}

	@Override
	protected void processMethod(Class<?> c, Method m, Object target) {
		publisher.register(new ExecutionHandlerSubscriber(c, m, false));
		try {
			if (m.getName().equals("main")) {
				m.setAccessible(true);
				m.invoke(target, (Object) null);
				if (!publisher.isSubscriberPending(m.getName())) {
					// no other process called subscriber, like ParallelExecutor
					publisher.invoke(m.getName(), true, null);
				}
			}
			if (m.getName().equals("init") || m.isAnnotationPresent(TestMethod.class)) {
				m.setAccessible(true);
				boolean flag = false;
				long maxWait = Long.MAX_VALUE;
				for (Annotation a : m.getAnnotations()) {
					if (a instanceof TestMethod) {
						if (((TestMethod) a).await()) {
							for (Annotation[] annotations : m.getParameterAnnotations()) {
								for (Annotation annotation : annotations) {
									if (annotation instanceof TestLock) {
										logger.info("true");

										flag = true;
										maxWait = ((TestLock) annotation).maxWait();
										break;
									}
								}
								if (flag)
									break;
							}
						}
						if (flag) {
							synchronized (lock) {
								logger.info("invoke");
								m.invoke(target, lock);
								try {
									long start = System.currentTimeMillis();
									lock.wait(maxWait);
									long end = System.currentTimeMillis();
									if (end - start >= maxWait) {
										throw new OutTimeLimitException(maxWait);
									} else {
										if (!publisher.isSubscriberPending(m.getName())) {
											publisher.invoke(m.getName(), true, null);
										}
									}
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else {
							// logger.info("plain invoke");
							plainInvoke(m, target);
						}
					}
				}
			}
		} catch (InvocationTargetException | IllegalAccessException | OutTimeLimitException e) {
			publisher.invoke(m.getName(), false, e.getMessage() + e.getCause());
		}
	}

	private void plainInvoke(Method m, Object target) throws InvocationTargetException, IllegalAccessException {
		m.invoke(target);
		if (!publisher.isSubscriberPending(m.getName())) {
			// no other process called subscriber, like ParallelExecutor
			publisher.invoke(m.getName(), true, null);
		}
	}

	public static void run(Class<?> c) {
		if (c.isAnnotationPresent(Exclude.class)) {
			PackageUtility.setExcludeClasses(c.getAnnotation(Exclude.class).classes());
		}
		AbstractPackageLoader frameworkApplication = new OrderedPackageLoader(c);
		Set<Class<?>> classes = frameworkApplication.scanPackage();
		frameworkApplication.loadIntoIoc(classes);
		frameworkApplication.processIocContainer();
	}
}

class Test {
	private Object o;

	@TestInit
	void init(Object o) {
		o = o;
	}

	@TestMethod
	void test1(Object o, Subject<String> a) {
		a.updateData("xx");


	}
}
