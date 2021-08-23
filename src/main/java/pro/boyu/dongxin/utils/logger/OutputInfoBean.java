package pro.boyu.dongxin.utils.logger;

import java.util.concurrent.TimeUnit;

public class OutputInfoBean {
    String className;
    String methodName;
    boolean success;
    Long timeUsage;
    TimeUnit timeUnit = TimeUnit.MICROSECONDS;
    String message;
    String[] includeGroups;
    String[] excludeGroups;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getTimeUsage() {
        return timeUsage;
    }

    public void setTimeUsage(Long timeUsage) {
        this.timeUsage = timeUsage;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getIncludeGroups() {
        return includeGroups;
    }

    public void setIncludeGroups(String[] includeGroups) {
        this.includeGroups = includeGroups;
    }

    public String[] getExcludeGroups() {
        return excludeGroups;
    }

    public void setExcludeGroups(String[] excludeGroups) {
        this.excludeGroups = excludeGroups;
    }

    public OutputInfoBean(String className, String methodName, boolean success, Long timeUsage, String message) {
        this.className = className;
        this.methodName = methodName;
        this.success = success;
        this.timeUsage = timeUsage;
        this.message = message;
        this.includeGroups = null;
        this.excludeGroups = null;
    }
    public OutputInfoBean(String className, String methodName, boolean success, Long timeUsage, String message, String[] includeGroups, String[] excludeGroups) {
        this.className = className;
        this.methodName = methodName;
        this.success = success;
        this.timeUsage = timeUsage;
        this.message = message;
        this.includeGroups = includeGroups;
        this.excludeGroups = excludeGroups;
    }
}
