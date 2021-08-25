package pro.boyu.dongxin.utils.logger;

import pro.boyu.dongxin.framework.infobean.ExecutionInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class OutputInfoBean {
    String className;
    String methodName;
    List<ExecutionInfo> executionInfos;
    String IncludeGroups;
    String ExcludeGroups;
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

    public String getIncludeGroups() {
        return IncludeGroups;
    }

    public void setIncludeGroups(String includeGroups) {
        IncludeGroups = includeGroups;
    }

    public String getExcludeGroups() {
        return ExcludeGroups;
    }

    public void setExcludeGroups(String excludeGroups) {
        ExcludeGroups = excludeGroups;
    }

    public OutputInfoBean(String className, String methodName, List<ExecutionInfo> executionInfos) {
        this.className = className;
        this.methodName = methodName;
        this.executionInfos = executionInfos;
    }
}
