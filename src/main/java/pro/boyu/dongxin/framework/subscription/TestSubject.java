package pro.boyu.dongxin.framework.subscription;

import pro.boyu.dongxin.framework.infobean.ExecutionInfo;

public class TestSubject extends Subject<ExecutionInfo> {
    private final String className;
    private final String methodName;

    public TestSubject(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }
}
