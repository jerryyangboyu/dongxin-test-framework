package pro.boyu.dongxin.utils.logger;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfReportWriter {
    private final int columns = 7;
    private final String path;
    private final Map<String, List<OutputInfoBean>> map;
    private Document document;
    private PdfWriter writer;
    private String title = "测试报告";
    private PdfPTable reportTable;

    public PdfReportWriter(String path, Map<String, List<OutputInfoBean>> map) {
        this.path = path;
        this.map = map;
    }

    public PdfReportWriter(String path, String title, Map<String, List<OutputInfoBean>> map) {
        this.path = path;
        this.map = map;
        this.title = title;
    }

    private void initDocument() throws DocumentException, FileNotFoundException {
        //创建文件
        document = new Document();
        //建立一个书写器
        writer = PdfWriter.getInstance(document, new FileOutputStream(path));
        //打开文件
        document.open();
        //添加内容
        document.add(new Paragraph(title));
        // 7列的表.
        reportTable = new PdfPTable(columns);
        reportTable.setWidthPercentage(100); // 宽度100%填充
        reportTable.setSpacingBefore(10f); // 前间距
        reportTable.setSpacingAfter(10f); // 后间距
        reportTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        List<PdfPRow> listRow = reportTable.getRows();
        //设置列宽
        float[] widths = {3f, 2f, 2f, 2f, 2f, 4f, 4f};
        reportTable.setWidths(widths);
        //单元格
        PdfPCell[] cells1 = new PdfPCell[columns];
        cells1[0] = new PdfPCell(new Paragraph("Test Class"));//单元格内容
        cells1[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[0].setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells1[1] = new PdfPCell(new Paragraph("Test Method"));
        cells1[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[1].setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells1[2] = new PdfPCell(new Paragraph("Time"));
        cells1[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[2].setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells1[3] = new PdfPCell(new Paragraph("State"));//单元格内容
        cells1[3].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[3].setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells1[4] = new PdfPCell(new Paragraph("Message"));//单元格内容
        cells1[4].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[4].setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells1[5] = new PdfPCell(new Paragraph("Include Groups"));//单元格内容
        cells1[5].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[5].setVerticalAlignment(Element.ALIGN_MIDDLE);
        cells1[6] = new PdfPCell(new Paragraph("Excluded Groups"));//单元格内容
        cells1[6].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        cells1[6].setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPRow titleRow = new PdfPRow(cells1);
        listRow.add(titleRow);
    }

    private void insertRows() throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font blueFont = new Font(bfChinese);
        blueFont.setColor(BaseColor.GREEN);
        ArrayList<PdfPRow> rows = reportTable.getRows();
        List<PdfPCell> cells = new ArrayList<>();
        for (String className : map.keySet()) {
            List<OutputInfoBean> outputInfoBeans = map.get(className);
            Object[] outPut = outputInfoBeans.toArray();
            for (int i = 0; i < 7; i++) {
                cells.add(new PdfPCell(new Paragraph()));
            }
            PdfPRow newRow = new PdfPRow(cells.toArray(new PdfPCell[0]));
            rows.add(newRow);
        }
        PdfPCell[] cells1 = new PdfPCell[columns];
        for (String className : map.keySet()) {
            List<OutputInfoBean> outputInfoBeans = map.get(className);
            Object[] outPut = outputInfoBeans.toArray();
            cells1[0] = (new PdfPCell(new Paragraph(outputInfoBeans.get(0).getClassName())));
            cells1[0].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居
            cells1[0].setVerticalAlignment(Element.ALIGN_MIDDLE);
            cells1[1] = (new PdfPCell(new Paragraph(outputInfoBeans.get(0).getMethodName())));
            cells1[1].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
            cells1[1].setVerticalAlignment(Element.ALIGN_MIDDLE);
            for (ExecutionInfo executionInfo : outputInfoBeans.get(0).getExecutionInfos()) {
                cells1[2] = new PdfPCell(new Paragraph(String.valueOf(System.currentTimeMillis() - executionInfo.getTime())));
                cells1[2].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[2].setVerticalAlignment(Element.ALIGN_MIDDLE);
                cells1[3] = (new PdfPCell(new Paragraph(String.valueOf(executionInfo.getState()))));
                cells1[3].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[3].setVerticalAlignment(Element.ALIGN_MIDDLE);
                cells1[4] = (new PdfPCell(new Paragraph(String.valueOf(executionInfo.getMessage()),blueFont)));
                cells1[4].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[4].setVerticalAlignment(Element.ALIGN_MIDDLE);
            }
            if (outputInfoBeans.get(0).getIncludeGroups()==null){
                cells1[5] = (new PdfPCell(new Paragraph("")));
                cells1[5].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[5].setVerticalAlignment(Element.ALIGN_MIDDLE);
                cells1[6] = (new PdfPCell(new Paragraph("")));
                cells1[6].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[6].setVerticalAlignment(Element.ALIGN_MIDDLE);
            }
            else {
                cells1[5] = (new PdfPCell(new Paragraph(String.valueOf(outputInfoBeans.get(0).getIncludeGroups()))));
                cells1[5].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[5].setVerticalAlignment(Element.ALIGN_MIDDLE);
                cells1[6] = (new PdfPCell(new Paragraph(String.valueOf(outputInfoBeans.get(0).getExcludeGroups()))));
                cells1[6].setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
                cells1[6].setVerticalAlignment(Element.ALIGN_MIDDLE);
            }

            PdfPRow newRow = new PdfPRow(cells1);
            rows.add(newRow);
        }
    }

    private void end() throws DocumentException {
        reportTable.setHorizontalAlignment(Element.ALIGN_CENTER);
        document.add(reportTable);
        //关闭文档
        document.close();
        //关闭书写器
        writer.close();
    }

    public void createPdf() throws DocumentException, IOException {
        initDocument();
        insertRows();
        end();
    }

    public static void main(String[] args) throws DocumentException, IOException {
        Map<String, List<OutputInfoBean>> beans = new HashMap<>();
        List<OutputInfoBean> list1 = new ArrayList<>();
        List<ExecutionInfo> execlist = new ArrayList<>();
        execlist.add(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.START));
        list1.add(new OutputInfoBean("TestClass", "TestMethod", execlist));
        beans.put("TestClass", list1);
        PdfReportWriter writer = new PdfReportWriter("C:/a.pdf", "Test", beans);
        writer.createPdf();
    }
}
