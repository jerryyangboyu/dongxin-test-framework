package pro.boyu.dongxin.utils.logger;

import pro.boyu.dongxin.framework.TestApplication;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlReporterWriter {
    private final int columns = 7;
    private  String path;
    private  Map<String, List<OutputInfoBean>> map;
    private String title = "测试报告";
    private StringBuilder sb;

    public HtmlReporterWriter(String path, Map<String, List<OutputInfoBean>> map) {
        this.path = path;
        this.map = map;
    }

    public HtmlReporterWriter(String path, Map<String, List<OutputInfoBean>> map, String title) {
        this.path = path;
        this.map = map;
        this.title = title;
    }

    private PrintStream printStream;

    public HtmlReporterWriter() {
    }

    public void init(){
        //用于存储html字符串
        sb = new StringBuilder();
        try{
            //打开文件
            printStream = new PrintStream(new FileOutputStream(path));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
//输入HTML文件内容
        sb.append("<html><head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        sb.append("<style type=\"text/css\">");
        sb.append("html {\n" +
                "    font-family: sans-serif;\n" +
                "    -ms-text-size-adjust: 100%;\n" +
                "    -webkit-text-size-adjust: 100%;\n" +
                "}");
        sb.append("body {\n" +
                "    margin: 10px;\n" +
                "}");
        sb.append("table {\n" +
                "    border-collapse: collapse;\n" +
                "    border-spacing: 0;\n" +
                "}");
        sb.append("td,th {\n" +
                "    padding: 0;\n" +
                "}");
        sb.append(".pure-table {\n" +
                "    border-collapse: collapse;\n" +
                "    border-spacing: 0;\n" +
                "    empty-cells: show;\n" +
                "    border: 1px solid #cbcbcb;\n" +
                "}");
        sb.append(".pure-table caption {\n" +
                "    color: #000;\n" +
                "    font: italic 85%/1 arial,sans-serif;\n" +
                "    padding: 1em 0;\n" +
                "    text-align: center;\n" +
                "}");
        sb.append(".pure-table td,.pure-table th {\n" +
                "    border-left: 1px solid #cbcbcb;\n" +
                "    border-width: 0 0 0 1px;\n" +
                "    font-size: inherit;\n" +
                "    margin: 0;\n" +
                "    overflow: visible;\n" +
                "    padding: .5em 1em;\n" +
                "}");
        sb.append(".pure-table thead {\n" +
                "    background-color: #e0e0e0;\n" +
                "    color: #000;\n" +
                "    text-align: left;\n" +
                "    vertical-align: bottom;\n" +
                "}");
        sb.append(".pure-table td {\n" +
                "    background-color: transparent;\n" +
                "}");
        sb.append(".pure-table-bordered td {\n" +
                "    border-bottom: 1px solid #cbcbcb;\n" +
                "}");
        sb.append(".pure-table-bordered tbody>tr:last-child>td {\n" +
                "    border-bottom-width: 0;\n" +
                "}");
        sb.append("</style>");
        sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">");
        sb.append("<tr>").append("<td align=\"center\" class=\"biaoti\" height=\"60\">").append(title).append("</td>").append("</tr>");
        sb.append("</td>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table class=\"pure-table pure-table-bordered\">");
        sb.append("<tr>");
        sb.append("<td>").append("TestClass").append("</td>");
        sb.append("<td>").append("TestMethod").append("</td>");
        sb.append("<td>").append("Time(ms)").append("</td>");
        sb.append("<td>").append("State").append("</td>");
        sb.append("<td>").append("Message").append("</td>");
        sb.append("<td>").append("Included Groups").append("</td>");
        sb.append("<td>").append("Excluded Groups").append("</td>");
        sb.append("</table>");
    }
    public void insert(){
        for(String className: map.keySet()) {
            List<OutputInfoBean> outputInfoBeans = map.get(className);
            sb.append("<table >");
            sb.append("<tr>");
            sb.append("<td>").append(String.valueOf(outputInfoBeans.get(0).getClassName())).append("</td>");
            sb.append("<td>").append(String.valueOf(outputInfoBeans.get(0).getMethodName())).append("</td>");
            for (ExecutionInfo executionInfo : outputInfoBeans.get(0).getExecutionInfos()) {
                long time = System.currentTimeMillis() - executionInfo.getTime();
                sb.append("<td>").append(String.valueOf(time)).append("</td>");
                sb.append("<td>").append(String.valueOf(executionInfo.getState())).append("</td>");
                sb.append("<td>").append(executionInfo.getMessage()).append("</td>");
            }
            sb.append("<td>").append(String.valueOf(outputInfoBeans.get(0).getIncludeGroups())).append("</td>");
            sb.append("<td>").append(String.valueOf(outputInfoBeans.get(0).getExcludeGroups())).append("</td>");
            sb.append("</table>");
        }
    }
    public void end(){
        sb.append("</table>");
        sb.append("</body></html>");
        try{
            //将HTML文件内容写入文件中
            printStream.println(sb.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createHtml(){
        init();
        insert();
        end();
    }

}
