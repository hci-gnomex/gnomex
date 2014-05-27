<%@ page language="java" import="hci.report.model.*" %>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFSheet" %>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCreationHelper"%>
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
HSSFCreationHelper createHelper = workbook.getCreationHelper();


// Create workbook styles
// Header style
HSSFFont font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle headerStyle = workbook.createCellStyle();
headerStyle.setFont(font);
headerStyle.setWrapText(true);

// Date + time style
HSSFCellStyle dateTimeCellStyle = workbook.createCellStyle();
dateTimeCellStyle.setDataFormat( createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
dateTimeCellStyle.setFont(font);
dateTimeCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);


// Create a worksheet for each report tray
for (ReportTray tray : (java.util.ArrayList<ReportTray>)reportTrayList.getTrays()) {
	HSSFSheet sheet = workbook.createSheet(tray.getReportTitle());

	String bgcolor;
	int rowNumber = 0;
	int colNumber = 0;
	
	// Report description
	HSSFRow titleRow = sheet.createRow(rowNumber++);
	HSSFCell cell = titleRow.createCell(0);
	cell.setCellValue(tray.getReportDescription());
	cell.setCellStyle(headerStyle);

	// Report date
	titleRow = sheet.createRow(rowNumber++);
	cell = titleRow.createCell(0);
	cell.setCellValue(tray.getReportDate());
	cell.setCellStyle(dateTimeCellStyle);
	
	// Blank line
	titleRow = sheet.createRow(rowNumber++);


	// Print the column headings
	HSSFRow headerRow = sheet.createRow(rowNumber++);
	Iterator cIter = tray.getColumns().iterator();
	while (cIter.hasNext()) {
		Column col = (Column) cIter.next(); 
		cell = headerRow.createCell(colNumber++);
		cell.setCellValue(col.getName());
		cell.setCellStyle(headerStyle);
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
			  cell = excelRow.createCell(colNumber++);
			  cell.setCellValue(val);
			}
			
		 }
	}
	
	// Auto size the column widths
	for(int columnIndex = 0; columnIndex < 5; columnIndex++) {
	     sheet.autoSizeColumn(columnIndex);
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
