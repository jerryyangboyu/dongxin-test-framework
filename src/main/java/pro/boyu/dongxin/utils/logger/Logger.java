package pro.boyu.dongxin.utils.logger;

import java.text.MessageFormat;

public class Logger {
    public enum FormatType {C, PYTHON, SLF4J};
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_DEFAULT = "\u001B[39m";

    public void debug(String message) {
        info(message);
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
                pattern = pattern.replaceFirst("\\{}", arg.toString());
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

}
