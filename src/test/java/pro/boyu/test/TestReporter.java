package pro.boyu.test;

import com.itextpdf.text.DocumentException;
import pro.boyu.dongxin.utils.logger.HtmlReporterWriter;
import pro.boyu.dongxin.utils.logger.LoggerFactory;
import pro.boyu.dongxin.utils.logger.OutputInfoBean;
import pro.boyu.dongxin.utils.logger.PdfReportWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestReporter {
    public static void outputHtml() {
        Map<String, List<OutputInfoBean>> map = LoggerFactory.getMap();
        HtmlReporterWriter hm = new HtmlReporterWriter("C:\\b.html",map,"测试报告");
        hm.createHtml();
    }

    public static void outputPdf() throws DocumentException, IOException {
        Map<String, List<OutputInfoBean>> map = LoggerFactory.getMap();
        PdfReportWriter hm = new PdfReportWriter("C:/a.pdf","title", map );
        hm.createPdf();
    }
}
