<%@ page language="java" import="hci.report.model.*" %>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFSheet" %>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFCreationHelper"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page language="java" import="org.apache.poi.hssf.usermodel.HSSFPalette"%>
<%@ page language="java" import="org.apache.poi.hssf.util.HSSFColor"%>
<%@ page language="java" import="org.apache.poi.hssf.util.CellRangeAddress"%>
<%@ page language="java" import="java.io.FileNotFoundException"%>
<%@ page language="java" import="java.io.IOException"%>
<%@ page language="java" import="java.util.Iterator"%>
<%@ page language="java" import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="hci.report.model.ReportTray"%>
<%@ page language="java" import="hci.gnomex.model.ReportTrayList"%>

<jsp:useBean id="tray" class="hci.report.model.ReportTray" scope="request"/>
<%
response.setHeader("Content-type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=\"" + tray.getFileName() + ".xls\"");

// Create a workbook and creation helper
HSSFWorkbook workbook = new HSSFWorkbook();
HSSFCreationHelper createHelper = workbook.getCreationHelper();

// Create sheet in workbook
HSSFSheet sheet = workbook.createSheet("Annotation Report");

// Repetitive temporary variables
HSSFFont font;
HSSFRow row;
HSSFCell cell;

// Styles

// Title style
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle titleStyle = workbook.createCellStyle();
titleStyle.setFont(font);
titleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

// Header style
HSSFCellStyle headerStyle = workbook.createCellStyle();
headerStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

// Caption style
short CAPTION_ROW_HEIGHT = 380;
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle captionStyle = workbook.createCellStyle();
captionStyle.setFont(font);
captionStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
captionStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
captionStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);

// Data style
HSSFCellStyle dataStyle = workbook.createCellStyle();
dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

// Trailing blank style
HSSFCellStyle trailingBlankStyle = workbook.createCellStyle();
trailingBlankStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

int rowNumber = 0;
int colNumber = 0;
int totalColCount = tray.getColumns().size();

// Create rows (with merged cells) that hold title and subheader plus blank row
while (rowNumber < 3) {
	row = sheet.createRow(rowNumber++);
	for (int i = 0; i < totalColCount; i++) {
		row.createCell(i);
	}
}
for (int i = 0; i < 3; i++) {
	sheet.addMergedRegion(new CellRangeAddress(i, i, 0, totalColCount - 1));
}

// Write table title
cell = sheet.getRow(0).getCell(0);
cell.setCellStyle(titleStyle);
cell.setCellValue(tray.getReportDescription());

// Write table subheader
cell = sheet.getRow(1).getCell(0);
cell.setCellStyle(headerStyle);
cell.setCellValue(new SimpleDateFormat("EEE, MMM d yyyy h:mm a").format(tray.getReportDate()));

// Write column captions
row = sheet.createRow(rowNumber++);
row.setHeight(CAPTION_ROW_HEIGHT);
Iterator colIter = tray.getColumns().iterator();
while (colIter.hasNext()) {
	Column col = (Column) colIter.next();
	cell = row.createCell(colNumber++);
	cell.setCellStyle(captionStyle);
	cell.setCellValue(col.getCaption());
}

// Write table

// For each row in report tray, create a row in the spreadsheet
Iterator rowIter = tray.getRows().iterator();
while (rowIter.hasNext()) {
	ReportRow reportRow = (ReportRow) rowIter.next();
	row = sheet.createRow(rowNumber++);
	
	// For each value in a row, create a cell in the spreadsheet row
	Iterator valIter = reportRow.getValues().iterator();
	colNumber = 0;
	String value;
	while (valIter.hasNext()) {
		value = (String) valIter.next();
		cell = row.createCell(colNumber++);
		cell.setCellValue((value != null) ? value : "");
		cell.setCellStyle(dataStyle);
	}
}

// Create a trailing row of blank cells
row = sheet.createRow(rowNumber++);
colNumber = 0;
for (int i = 0; i < totalColCount; i++) {
	cell = row.createCell(colNumber++);
	cell.setCellStyle(trailingBlankStyle);
}

// Size column widths and add padding
int COLUMN_WIDTH_PADDING = 20;
for (int i = 0; i < totalColCount; i++) {
	sheet.autoSizeColumn(i, false);
	sheet.setColumnWidth(i, sheet.getColumnWidth(i) + COLUMN_WIDTH_PADDING);
}

// Create freeze pane
sheet.createFreezePane(3, 4);

// Write out the spreadsheet
try {
	workbook.write(response.getOutputStream());
	response.getOutputStream().close();
} catch (FileNotFoundException e) {
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
}
%>
