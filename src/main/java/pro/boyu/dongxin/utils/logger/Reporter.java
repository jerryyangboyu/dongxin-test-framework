package pro.boyu.dongxin.utils.logger;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Reporter {
    private final String path;
    private final Map<String,List<OutputInfoBean>> map;

    public Reporter(String path, Map<String,List<OutputInfoBean>> map) {
        this.path = path;
        this.map = map;
    }

    public void exportHtml(String path) {

    }

    //path 输出的地址 count 返回的测试样例的总数  返回一个hashMap 根据方法名找到对应的信息
    public void exportPdf() throws IOException, DocumentException {
        PdfReportWriter writer = new PdfReportWriter(path, map);
        writer.createPdf();
    }
}
