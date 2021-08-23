package pro.boyu.dongxin.utils.logger;

import com.itextpdf.text.DocumentException;
import pro.boyu.dongxin.framework.infobean.TestExecuteInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface IReporter {
//    public void addRecord(Class<?> clazz, Method method, boolean success, String message, long time, TimeUnit unit);
//    public void addRecord(TestExecuteInfo info);
    public void exportHtml(String path);
    public void initPdf(String path,int cells) throws IOException, DocumentException;
    //pdf文件加内容
    public void exportPdf(String path, int count, Map<String, List<String>> map) throws IOException, DocumentException;
}
