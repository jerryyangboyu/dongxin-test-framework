package pro.boyu.dongxin.framework;

import pro.boyu.dongxin.framework.annotations.ExportLogs;
import pro.boyu.dongxin.utils.logger.LoggerFactory;
import pro.boyu.dongxin.utils.logger.Reporter;

public class TestApplication {
    public static void run(Class<?> c) {
        LoggerFactory.debug = false;
        OrderedPackageLoader.run(c);
        if (c.isAnnotationPresent(ExportLogs.class)) {
            ExportLogs logAnnotation = c.getAnnotation(ExportLogs.class);
            Reporter reporter = new Reporter(logAnnotation.path(), LoggerFactory.getMap());
            try {
                if (logAnnotation.fileType().equals("pdf")) {
                    reporter.exportPdf();
                } else {
                    reporter.exportHtml(logAnnotation.path());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
