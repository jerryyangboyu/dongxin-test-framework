package pro.boyu.dongxin.utils.logger;

import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logger {
    public enum FormatType {C, PYTHON, SLF4J};
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_DEFAULT = "\u001B[39m";
    private final String id;

    public Logger(String name) {
        this.id = name;
    }

    public String getId() {return this.id;}

    public void debug(String message) {
        if (LoggerFactory.debug) {
            System.out.println(message);
        }
    }

    public void exportInfo(OutputInfoBean info) {
        LogCache.getCache().appendInfo(id, info);
        for (ExecutionInfo exec: info.getExecutionInfos()) {
            String message = TestCaseInfoUtil.executeExceptionInfoUtil(info.getClassName(), info.getMethodName(), (int) exec.getTime(), exec.getMessage());
            TestCaseState state = exec.getState();
            if (state == TestCaseState.SUCCESSFIN) {
                info(message);
            } else if (state == TestCaseState.ERROR || state == TestCaseState.ERRORFIN) {
                error(message);
            }
        }
    }


    public void info(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_DEFAULT);
    }

    public void info(String pattern, FormatType type, Object ...args) {
        String result = processMessage(pattern, type, args);
        info(result);
    }

    public void info(String pattern, Object ...args) {
        String result = processMessage(pattern, FormatType.SLF4J, args);
        info(result);
    }

    public void warn(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_DEFAULT);
    }

    public void warn(String pattern, FormatType type, Object ...args) {
        String result = processMessage(pattern, type, args);
        warn(result);
    }

    public void warn(String pattern, Object ...args) {
        String result = processMessage(pattern, FormatType.SLF4J, args);
        warn(result);
    }

    public void error(String message) {
        System.out.println(ANSI_RED + message + ANSI_DEFAULT);
    }

    public void error(String pattern, FormatType type, Object ...args) {
        String result = processMessage(pattern, type, args);
        error(result);
    }

    public void error(String pattern, Object ...args) {
        String result = processMessage(pattern, FormatType.SLF4J, args);
        error(result);
    }

    private String processMessage(String pattern, FormatType type, Object ...args) {
        if ( FormatType.SLF4J == type) {
            for (Object arg: args) {
                if (null != arg) {
                    pattern = pattern.replaceFirst("\\{}", arg.toString());
                }
            }
            return pattern;
        } else if (FormatType.C == type) {
            return String.format(pattern, args);
        } else if (FormatType.PYTHON == type) {
            return  MessageFormat.format(pattern, args);
        } else {
            return pattern;
        }
    }

    public List<OutputInfoBean> getInfo() {
        return LoggerFactory.getMap().get(id);
    }

}
