package pro.boyu.test;

import com.itextpdf.text.DocumentException;
import pro.boyu.dongxin.framework.TestApplication;

import java.io.IOException;

public class GeneralTestApp {
    public static void main(String[] args) throws DocumentException, IOException {
        TestApplication.run(GeneralTestApp.class);
        TestReporter.outputPdf();
    }
}
