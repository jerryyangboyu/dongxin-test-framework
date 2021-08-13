package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.framework.annotations.Exclude;
import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestMethod;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AbstractPackageLoader {
    private final Class<?> applicationClass;
    private final Map<String, Class<?>> iocContainer = new HashMap<>();
    Logger logger = Logger.getLogger(AbstractPackageLoader.class);
    public static ExecutionHandlerPublisher publisher = new ExecutionHandlerPublisher();

    protected AbstractPackageLoader(Class<?> c) {
        this.applicationClass = c;
    }

    protected Set<Class<?>> scanPackage() {
        PackageUtility.setRecursive(true);
        return PackageUtility.getClasses(applicationClass.getPackage().getName());
    }

    protected Map<String,Class<?>> getIocContainer(){
        return this.iocContainer;
    }

    protected void loadIntoIoc(Set<Class<?>> classes) {
        for (Class<?> clazz: classes) {
            logger.debug("Processing class: " + clazz.getName());
            if (clazz.isAnnotationPresent(TestClass.class)) {
                TestClass classAnnotation = clazz.getAnnotation(TestClass.class);
                String clazzName = "";
                if (classAnnotation.value().equals("")) {
                    clazzName = clazz.getName();
                } else {
                    clazzName = classAnnotation.value();
                }
                iocContainer.put(clazzName, clazz);
            }
        }
    }

    protected void processIocContainer() {

        for (String key: iocContainer.keySet()) {
            Class<?> clazz = iocContainer.get(key);
            try {
                clazz = Class.forName(key);
                Constructor<?> constructor = clazz.getConstructor();
                constructor.setAccessible(true);
                Object target = constructor.newInstance();

                for (Method m: clazz.getDeclaredMethods()) {
                    processMethod(clazz, m, target);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    protected void injectArguments(Method m, Object target) throws InvocationTargetException, IllegalAccessException {
        List<Object> parameters = new ArrayList<>();
        for (Parameter p: m.getParameters()) {
            // dump code
            parameters.add(null);
        }
        if (parameters.isEmpty()) {
            m.invoke(target);
        } else {
            m.invoke(target, parameters.toArray());
        }
    }

    protected void processMethod(Class<?> c, Method m, Object target) {
    	//logger.info("message");
        publisher.register(new ExecutionHandlerSubscriber(c, m, false));
        try {
            if (m.getName().equals("main")) {
                m.setAccessible(true);
                injectArguments(m, target);
                if (!publisher.isSubscriberPending(m.getName())) {
                    // no other process called subscriber, like ParallelExecutor
                    publisher.invoke(m.getName(), true, null);
                }
            }
            if (m.getName().equals("init") || m.isAnnotationPresent(TestMethod.class)) {
                m.setAccessible(true);
                injectArguments(m, target);
                if (!publisher.isSubscriberPending(m.getName())) {
                    // no other process called subscriber, like ParallelExecutor
                    publisher.invoke(m.getName(), true, null);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException e) {
            publisher.invoke(m.getName(), false, e.getMessage() + e.getCause());
        }
    }

    public static void run(Class<?> c) {
        if (c.isAnnotationPresent(Exclude.class)) {
            PackageUtility.setExcludeClasses(c.getAnnotation(Exclude.class).classes());
        }
        AbstractPackageLoader frameworkApplication = new AbstractPackageLoader(c);
        Set<Class<?>> classes = frameworkApplication.scanPackage();
        frameworkApplication.loadIntoIoc(classes);
        frameworkApplication.processIocContainer();
    }

}
