package pro.boyu.dongxin.utils.logger;

import pro.boyu.dongxin.framework.infobean.ExecutionInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class OutputInfoBean {
    String className;
    String methodName;
    List<ExecutionInfo> executionInfos;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ExecutionInfo> getExecutionInfos() {
        return executionInfos;
    }

    public OutputInfoBean(String className, String methodName, List<ExecutionInfo> executionInfos) {
        this.className = className;
        this.methodName = methodName;
        this.executionInfos = executionInfos;
    }
}
