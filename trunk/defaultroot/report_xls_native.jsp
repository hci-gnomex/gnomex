<%@ page language="java" import="hci.report.model.*" %>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFSheet" %>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page language="java" import="java.io.FileNotFoundException"%>
<%@ page language="java" import="java.io.IOException"%>
<%@ page language="java" import="java.util.Iterator"%>
<%@ page language="java" import="hci.report.model.ReportTray"%>
<%@ page language="java" import="hci.gnomex.model.ReportTrayList"%>

<jsp:useBean id="reportTrayList" class="hci.gnomex.model.ReportTrayList" scope="request"/>
<%
response.setHeader("Content-type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=\""+reportTrayList.getFileName()+".xls\"");


// Create a workbook
HSSFWorkbook workbook = new HSSFWorkbook();

// Create workbook style
HSSFFont font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle boldStyle = workbook.createCellStyle();
boldStyle.setFont(font);

// Create a worksheet for each report tray
for (ReportTray tray : (java.util.ArrayList<ReportTray>)reportTrayList.getTrays()) {
	HSSFSheet sheet = workbook.createSheet(tray.getReportTitle());

	String bgcolor;
	int rowNumber = 0;
	int colNumber = 0;

	// Print the column headings
	HSSFRow headerRow = sheet.createRow(rowNumber++);
	Iterator cIter = tray.getColumns().iterator();
	while (cIter.hasNext()) {
		Column col = (Column) cIter.next(); 
		HSSFCell cell = headerRow.createCell(colNumber++);
		cell.setCellValue(col.getName());
		cell.setCellStyle(boldStyle);
	}

	// For each report tray row, generate an excel spreadsheet row.
	if (tray.getRows().size() > 0) {
		 Iterator iter = tray.getRows().iterator();
		 bgcolor = " bgcolor=\"#ffffff\"";
		 // For each row in report tray, create a row in the spreadsheet
		 while (iter.hasNext()) {
			ReportRow row = (ReportRow) iter.next();
			HSSFRow excelRow = sheet.createRow(rowNumber++);
			
			if (rowNumber % 2 > 0) {
			  bgcolor = " bgcolor=\"#ffffff\"";
			}
			
			// For each value in row, create a cell in the spreadsheet row
			Iterator vIter = row.getValues().iterator();
			colNumber = 0;
			while (vIter.hasNext()) {
			  String val = (String) vIter.next();
			  if (val == null) {
			    val = "";
			  }
			  HSSFCell cell = excelRow.createCell(colNumber++);
			  cell.setCellValue(val);
			}
			
		 }
	}
}
try {
    workbook.write(response.getOutputStream());
    response.getOutputStream().close();
     
} catch (FileNotFoundException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
%>
