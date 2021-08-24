package pro.boyu.dongxin.framework.infobean;

import pro.boyu.dongxin.framework.annotations.TestMethod;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class MethodExecutionInfo {
    private final String className;
    private final String methodName;
    private final Class<?> clazz;
    private final Method method;
    private final TestMethod testMethod;
    private final Object[] args;
    private final Object target;
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public MethodExecutionInfo(Object target, Method method, TestMethod testMethod, Object... args) {
        this.className = "Anonymous class";
        this.methodName = method.getName();
        this.clazz = target.getClass();
        this.method = method;
        this.testMethod = testMethod;
        this.args = args;
        this.target = target;
    }

    public MethodExecutionInfo(Class<?> clazz, Object target, Method method, TestMethod testMethod, Object... args) {
        this.className = clazz.getName();
        this.methodName = method.getName();
        this.clazz = clazz;
        this.method = method;
        this.testMethod = testMethod;
        this.args = args;
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public TestMethod getTestMethod() {
        return testMethod;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit unit) {
        this.timeUnit = unit;
    }
}
