package pro.boyu.dongxin.utils.logger;

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
        sb.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">");
        sb.append("<tr>").append("<td align=\"center\" class=\"biaoti\" height=\"60\">").append(title).append("</td>").append("</tr>");
        sb.append("</td>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<table width=\"75%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\" bgcolor=\"#cccccc\" class=\"tabtop13\" align=\"center\">");
        sb.append("<tr>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("TestClass").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("TestMethod").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Time(ms)").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("State").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Message").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Included Groups").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Excluded Groups").append("</td>");
        sb.append("</table>");
    }
    public void insert(){
        for(String className: map.keySet()) {
            List<OutputInfoBean> outputInfoBeans = map.get(className);
            sb.append("<table width=\"75%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\" bgcolor=\"#cccccc\" class=\"tabtop13\" align=\"center\">");
            sb.append("<tr>");
            sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\"rowspan=\"2\">").append(String.valueOf(outputInfoBeans.get(0).getClassName())).append("</td>");
            sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\"rowspan=\"2\">").append(String.valueOf(outputInfoBeans.get(0).getMethodName())).append("</td>");
            for (ExecutionInfo executionInfo : outputInfoBeans.get(0).getExecutionInfos()) {
                long time = System.currentTimeMillis() - executionInfo.getTime();
                sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\"rowspan=\"2\">").append(String.valueOf(time)).append("</td>");
                sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\"rowspan=\"2\">").append(String.valueOf(executionInfo.getState())).append("</td>");
                sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\"rowspan=\"2\">").append(executionInfo.getMessage()).append("</td>");
            }
            sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\"rowspan=\"2\">").append(String.valueOf(outputInfoBeans.get(0).getIncludeGroups())).append("</td>");
            sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\"rowspan=\"2\">").append(String.valueOf(outputInfoBeans.get(0).getExcludeGroups())).append("</td>");
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

    public static void main(String[] args) {
        Map<String, List<OutputInfoBean>> beans = new HashMap<>();
        List<OutputInfoBean> list1 = new ArrayList<>();
        List<ExecutionInfo> execlist = new ArrayList<>();
        execlist.add(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.START));
        list1.add(new OutputInfoBean("TestClass", "TestMethod", execlist));
        beans.put("Test",list1);
        HtmlReporterWriter hm = new HtmlReporterWriter("C:\\b.html",beans,"测试报告");
        hm.createHtml();
    }
}
