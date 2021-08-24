package pro.boyu.dongxin.utils.logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
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
            printStream = new PrintStream(new FileOutputStream("C:\\b.html"));
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
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Test").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("#Passed").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("#Skipped").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("#Failed").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Time(ms)").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Included Groups").append("</td>");
        sb.append("<td colspan=\"1\" class=\"btbg font-center titfont\" rowspan=\"2\">").append("Excluded Groups").append("</td>");
        sb.append("</table>");
    }
    public void insert(){
//        for(String className: map.keySet()){
//
//        }
    }
    public void end(){

        sb.append("</body></html>");
        try{
            //将HTML文件内容写入文件中
            printStream.println(sb.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HtmlReporterWriter htmlReporterWriter = new HtmlReporterWriter();
        htmlReporterWriter.init();
    }
}
