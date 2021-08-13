package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.framework.annotations.TestClass;
import pro.boyu.dongxin.framework.annotations.TestMethod;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Method;

public class ExecutionHandlerSubscriber {
    private final Class<?> currentExecClass;
    private final Method currentExecMethod;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    private boolean pending = false;
    private final Long startTime;

    public ExecutionHandlerSubscriber(Class<?> c, Method m, boolean isPending) {
        this.currentExecClass = c;
        this.currentExecMethod = m;
        this.pending = isPending;
        startTime = System.currentTimeMillis();
    }

    public void process(boolean isPending, boolean isSuccess, String message) {
        Long endTime = System.currentTimeMillis() - startTime;
        this.pending = isPending;
        String message1 = "";
        if (message != null) message1 = message;
        if (pending) throw new RuntimeException("Cannot process pending threads");
        String result = String.format("测试类 %s 当中的方法 %s 测试完成 耗时 %sms 状态 %s%s", currentExecClass.getName(),
                currentExecMethod.getName(), endTime,
                isSuccess ? "PASS" : "ERROR ", message1);
        if (isSuccess) {
            System.out.println(ANSI_GREEN + result);
        } else {
            System.out.println(ANSI_RED + result);
        }

        result = "";
        TestClass classAnnotation = currentExecClass.getAnnotation(TestClass.class);
        if (!classAnnotation.description().equals("")) result += "类描述 " + classAnnotation.description() + " ";
        if (currentExecMethod.isAnnotationPresent(TestMethod.class)) {
            TestMethod methodAnnotation = currentExecMethod.getAnnotation(TestMethod.class);
            if (!methodAnnotation.description().equals("")) result += "方法描述 " + methodAnnotation.description() + " ";
        }
        if (!result.equals("")) System.out.println(result);
    }

    public boolean isFinished() {
        return this.pending;
    }

    public void setFinished(boolean state) {
        this.pending = state;
    }

    public String getName() {
        return this.currentExecMethod.getName();
    }
}
