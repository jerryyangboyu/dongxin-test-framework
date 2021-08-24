package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.framework.annotations.*;
import pro.boyu.dongxin.framework.exception.OutTimeLimitException;
import pro.boyu.dongxin.framework.executor.manager.SyncExecutorManager;
import pro.boyu.dongxin.framework.infobean.MethodExecutionInfo;
import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 *
 */

public class OrderedPackageLoader extends AbstractPackageLoader {
	static final Logger logger = LoggerFactory.getLogger(OrderedPackageLoader.class);

	/*
		Used when used lock inside a test method, you should use @TestMethod with sync=true instead
	 */
	@Deprecated
	final static Object lock = new Object();
	
	public static final Object loggerLockObject=new Object();
	private final Map<Class<?>, Object> testClassInstanceMap = new HashMap<>();
	private final Map<Class<?>, Object> serviceClassInstanceMap = new HashMap<>();

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

		// executing each test class
		for (Class<?> clazz: testClassInstanceMap.keySet()) {
			try {
				this.processTestClass(clazz, testClassInstanceMap.get(clazz));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * description：初始化Service类，当前Service类不能自动注入任何依赖 author：Zhang Ying date:2021/08/13
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
		logger.info("Test Class {} is initiating", className);
		try {
			this.injectArguments(m, target);
		} catch (Exception e) {
			logger.error("Test Class {} init failed because of {} ", className, e.getMessage());
		}
	}

	protected void processTestClass(Class<?> clazz, Object target)
			throws InvocationTargetException, IllegalAccessException {
		SyncExecutorManager syncManager = null;

		// 扫描TestClazz下所有方法，首先处理带有TestInit注解的初始化工作，支持注入注册过的Service
		for (Method method : target.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(TestInit.class)) {
				method.setAccessible(true);
				method.invoke(target, dependencyDetection(method));
			}
			if (method.isAnnotationPresent(TestMethod.class)) {
				TestMethod testMethod = method.getAnnotation(TestMethod.class);
				if (testMethod.sync()) {
					if (null == syncManager) {
						syncManager = new SyncExecutorManager();
					}
					processSyncTestMethod(clazz, target, method, testMethod, syncManager);
				} else {
					// TODO impl async manager
				}

			}
		}
	}

	protected void processSyncTestMethod(Class<?> clazz, Object target, Method m, TestMethod testMethod, SyncExecutorManager syncManager) {
		MethodExecutionInfo info = new MethodExecutionInfo(clazz, target, m, testMethod, dependencyDetection(m));
		syncManager.addMethod(info);
		syncManager.exec();
	}

	protected Object[] dependencyDetection(Method m) {
		List<Object> objects = new LinkedList<>();
		for (Parameter parameter : m.getParameters()) {
			Class<?> clazz = parameter.getClass();
			objects.add(
					this.serviceClassInstanceMap.getOrDefault(clazz, null));
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