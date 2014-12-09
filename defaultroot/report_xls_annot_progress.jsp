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

<jsp:useBean id="reportTrayList" class="hci.gnomex.model.ReportTrayList" scope="request"/>
<%
response.setHeader("Content-type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=\""+reportTrayList.getFileName()+".xls\"");

int  MISSING_ANNOT_SHEET = 0;
int  FULLY_ANNOT_SHEET = 1;
int  SUMMARY_SHEET = 2;


//Create a workbook
HSSFWorkbook workbook = new HSSFWorkbook();
HSSFCreationHelper createHelper = workbook.getCreationHelper();

// Palette for custom color
int     PALLETE_RED_INDEX = 41;
int     PALLETE_LIGHT_RED_INDEX = 42;
int     PALLETE_GREY_INDEX = 43;
int     PALLETE_LIGHT_GREY_INDEX = 44;
int     PALLETE_DARK_GREY_INDEX = 45;
int     PALLETE_WHITE_INDEX = 46;
int     PALLETE_CELL_BORDER_INDEX = 47;
int     PALLETE_GREEN_INDEX = 48;


// Column numbers of significance (requires special logic)
int     FIRST_COL_FOR_ANNOTS = 7;
int     SAMPLE_COUNT_COL = 2;
int     LAB_COL = 0;

int     LAB_COL_WIDTH = 6000;
short   ROW_HEIGHT = 400;

// Colors
HSSFPalette palette = workbook.getCustomPalette();
palette.setColorAtIndex(new Byte((byte) PALLETE_RED_INDEX),       new Byte((byte) 232), new Byte((byte) 118), new Byte((byte) 130));
palette.setColorAtIndex(new Byte((byte) PALLETE_LIGHT_RED_INDEX), new Byte((byte) 245), new Byte((byte) 206), new Byte((byte) 210));
palette.setColorAtIndex(new Byte((byte) PALLETE_GREY_INDEX),      new Byte((byte) 207), new Byte((byte) 207), new Byte((byte) 207));
palette.setColorAtIndex(new Byte((byte) PALLETE_LIGHT_GREY_INDEX),new Byte((byte) 240), new Byte((byte) 240), new Byte((byte) 240));
palette.setColorAtIndex(new Byte((byte) PALLETE_DARK_GREY_INDEX), new Byte((byte) 115),  new Byte((byte) 110), new Byte((byte) 111));
palette.setColorAtIndex(new Byte((byte) PALLETE_WHITE_INDEX),     new Byte((byte) 255),   new Byte((byte) 255),   new Byte((byte) 255));
palette.setColorAtIndex(new Byte((byte) PALLETE_CELL_BORDER_INDEX),  new Byte((byte) 115),  new Byte((byte) 110), new Byte((byte) 111));
palette.setColorAtIndex(new Byte((byte) PALLETE_GREEN_INDEX),     new Byte((byte) 138),   new Byte((byte) 230),   new Byte((byte) 138));

// Fonts		
short fontHeightTitle = 16;
short fontHeightHeading = 11;
short fontHeightBody = 11;

// Body style
HSSFFont defaultFont = workbook.createFont();
defaultFont.setFontHeightInPoints(fontHeightBody);
HSSFCellStyle defaultStyle = workbook.createCellStyle();
defaultStyle.setFont(defaultFont);

//Blank annot style
HSSFCellStyle defaultAnnotStyle = workbook.createCellStyle();
defaultAnnotStyle.setFont(defaultFont);
defaultAnnotStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
defaultAnnotStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
defaultAnnotStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
defaultAnnotStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
defaultAnnotStyle.setTopBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
defaultAnnotStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
defaultAnnotStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
defaultAnnotStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());


// Title style
HSSFFont font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
font.setFontHeightInPoints(fontHeightTitle);
HSSFCellStyle titleStyle = workbook.createCellStyle();
titleStyle.setFont(font);
titleStyle.setFillForegroundColor(palette.getColor(PALLETE_WHITE_INDEX).getIndex());
titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);



// Header style
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle headerStyle = workbook.createCellStyle();
headerStyle.setFont(font);
font.setFontHeightInPoints(fontHeightHeading);
headerStyle.setWrapText(true);
headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
headerStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_GREY_INDEX).getIndex());
headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

//Annotation Header style
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle annotHeaderStyle = workbook.createCellStyle();
annotHeaderStyle.setFont(font);
annotHeaderStyle.setWrapText(true);
annotHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
annotHeaderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
annotHeaderStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_GREY_INDEX).getIndex());
annotHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
annotHeaderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
annotHeaderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
annotHeaderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
annotHeaderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);




// Error cell styles
HSSFCellStyle errorStyle = workbook.createCellStyle();
errorStyle.setFillForegroundColor(palette.getColor(PALLETE_RED_INDEX).getIndex());
errorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
errorStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
errorStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
errorStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
errorStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
errorStyle.setTopBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
errorStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
errorStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
errorStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());


HSSFCellStyle errorLegendStyle = workbook.createCellStyle();
errorLegendStyle.setFillForegroundColor(palette.getColor(PALLETE_RED_INDEX).getIndex());
errorLegendStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
errorLegendStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle errorTopBorderStyle = workbook.createCellStyle();
errorTopBorderStyle.setFillForegroundColor(palette.getColor(PALLETE_RED_INDEX).getIndex());
errorTopBorderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
errorTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
errorTopBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
errorTopBorderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
errorTopBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
errorTopBorderStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
errorTopBorderStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
errorTopBorderStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());

   
// Partial Error cell 
HSSFCellStyle partialErrorStyle = workbook.createCellStyle();
partialErrorStyle.setFont(defaultFont);
partialErrorStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_RED_INDEX).getIndex());
partialErrorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
partialErrorStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
partialErrorStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
partialErrorStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
partialErrorStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
partialErrorStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
partialErrorStyle.setTopBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
partialErrorStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
partialErrorStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
partialErrorStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());



HSSFCellStyle partialErrorLegendStyle = workbook.createCellStyle();
partialErrorLegendStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_RED_INDEX).getIndex());
partialErrorLegendStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
partialErrorLegendStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

HSSFCellStyle partialErrorTopBorderStyle = workbook.createCellStyle();
partialErrorTopBorderStyle.setFont(defaultFont);
partialErrorTopBorderStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_RED_INDEX).getIndex());
partialErrorTopBorderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
partialErrorTopBorderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
partialErrorTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
partialErrorTopBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
partialErrorTopBorderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
partialErrorTopBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
partialErrorTopBorderStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
partialErrorTopBorderStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
partialErrorTopBorderStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());



// Warning cell  (grey)
HSSFCellStyle warningStyle = workbook.createCellStyle();
warningStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
warningStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
warningStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
warningStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
warningStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
warningStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
warningStyle.setTopBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
warningStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
warningStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
warningStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());


HSSFCellStyle warningLegendStyle = workbook.createCellStyle();
warningLegendStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
warningLegendStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

HSSFCellStyle warningTopBorderStyle = workbook.createCellStyle();
warningTopBorderStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
warningTopBorderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
warningTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
warningTopBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
warningTopBorderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
warningTopBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
warningTopBorderStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
warningTopBorderStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
warningTopBorderStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());


// Success cell  (green)
HSSFCellStyle successStyle = workbook.createCellStyle();
successStyle.setFillForegroundColor(palette.getColor(PALLETE_GREEN_INDEX).getIndex());
successStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
successStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
successStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
successStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
successStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
successStyle.setTopBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
successStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
successStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
successStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());

HSSFCellStyle successLegendStyle = workbook.createCellStyle();
successLegendStyle.setFillForegroundColor(palette.getColor(PALLETE_GREEN_INDEX).getIndex());
successLegendStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
successLegendStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle successTopBorderStyle = workbook.createCellStyle();
successTopBorderStyle.setFillForegroundColor(palette.getColor(PALLETE_GREEN_INDEX).getIndex());
successTopBorderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
successTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
successTopBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
successTopBorderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
successTopBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
successTopBorderStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
successTopBorderStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
successTopBorderStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());


// Blank row style
HSSFCellStyle blankStyle = workbook.createCellStyle();
blankStyle.setFillForegroundColor(palette.getColor(PALLETE_WHITE_INDEX).getIndex());
blankStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


// Border styles
HSSFCellStyle  leftTopBorderStyle=workbook.createCellStyle();
leftTopBorderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
leftTopBorderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
leftTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
leftTopBorderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle  leftBorderStyle=workbook.createCellStyle();
leftBorderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle  leftBottomBorderStyle=workbook.createCellStyle();
leftBottomBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
leftBottomBorderStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle  rightBorderStyle=workbook.createCellStyle();
rightBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle  rightBottomBorderStyle=workbook.createCellStyle();
rightBottomBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
rightBottomBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle  rightTopBorderStyle=workbook.createCellStyle();
rightTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
rightTopBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle  bottomBorderStyle=workbook.createCellStyle();
bottomBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle  topBorderStyle=workbook.createCellStyle();
topBorderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);


// Lab break border style
HSSFCellStyle  labBreakDefaultStyle=workbook.createCellStyle();
labBreakDefaultStyle.setFont(defaultFont);
labBreakDefaultStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

HSSFCellStyle  labBreakAnnotStyle=workbook.createCellStyle();
labBreakAnnotStyle.setFont(defaultFont);
labBreakAnnotStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
labBreakAnnotStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
labBreakAnnotStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
labBreakAnnotStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
labBreakAnnotStyle.setBottomBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
labBreakAnnotStyle.setLeftBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());
labBreakAnnotStyle.setRightBorderColor(palette.getColor(PALLETE_CELL_BORDER_INDEX).getIndex());



// Create a worksheet for each report tray
int sheetIndex = 0;
for (ReportTray tray : (java.util.ArrayList<ReportTray>)reportTrayList.getTrays()) {
	HSSFSheet sheet = workbook.createSheet(tray.getReportTitle());

	String bgcolor;
	int rowNumber = 0;
	int colNumber = 0;
	
	// Report description
	HSSFRow titleRow1 = sheet.createRow(rowNumber++);
	HSSFCell cell = titleRow1.createCell(0);
	cell.setCellValue(tray.getReportDescription());
	cell.setCellStyle(titleStyle);
	sheet.addMergedRegion(new CellRangeAddress( 0, 0, 0, tray.getColumns().size() - 1));

	HSSFRow titleRowDate = sheet.createRow(rowNumber++);
	cell = titleRowDate.createCell(0);
	cell.setCellValue(new SimpleDateFormat("EEE, MMM d yyyy h:mm a").format(tray.getReportDate()));
	cell.setCellStyle(titleStyle);
	sheet.addMergedRegion(new CellRangeAddress( 1, 1, 0, tray.getColumns().size() - 1));


	// Blank row
	HSSFRow blankRow = sheet.createRow(rowNumber++);
	for ( int idx = 0; idx < tray.getColumns().size(); idx++) {
		cell = blankRow.createCell(idx);
		cell.setCellStyle(blankStyle);
	}
	
	
	// Show the Legend for the Missing Annotation worksheet
	if (sheetIndex == MISSING_ANNOT_SHEET) {
		// Legend
		HSSFRow titleRow2 = sheet.createRow(rowNumber++);
		cell = titleRow2.createCell(FIRST_COL_FOR_ANNOTS );
		cell.setCellValue("LEGEND");
		cell.setCellStyle(leftTopBorderStyle);
		
		cell = titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 1);
		cell.setCellStyle(errorLegendStyle);
		cell = titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 2);
		cell.setCellValue("Missing Annotation");
		cell.setCellStyle(topBorderStyle);
		titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 3).setCellStyle(topBorderStyle);
		titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 4).setCellStyle(topBorderStyle);
		titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 5).setCellStyle(topBorderStyle);
		titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 6).setCellStyle(topBorderStyle);
		titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 7).setCellStyle(topBorderStyle);
		titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 8).setCellStyle(rightTopBorderStyle);
		for (int x = 0; x < FIRST_COL_FOR_ANNOTS; x++) {
			titleRow2.createCell(x).setCellStyle(blankStyle);
		}
		for (int x = FIRST_COL_FOR_ANNOTS + 9; x < tray.getColumns().size(); x++) {
			titleRow2.createCell(x).setCellStyle(blankStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress( 3, 3, FIRST_COL_FOR_ANNOTS + 2, FIRST_COL_FOR_ANNOTS + 8));


		HSSFRow titleRow3 = sheet.createRow(rowNumber++);
		cell = titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 1);
		cell.setCellValue("n");
		cell.setCellStyle(partialErrorLegendStyle);
		cell = titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 2);
		cell.setCellValue("Incomplete Annotation (n samples on experiment are missing annotation)");
		titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 3).setCellStyle(blankStyle);
		titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 4).setCellStyle(blankStyle);
		titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 5).setCellStyle(blankStyle);
		titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 6).setCellStyle(blankStyle);
		titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 7).setCellStyle(blankStyle);
		titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 8).setCellStyle(rightBorderStyle);
		for (int x = 0; x < FIRST_COL_FOR_ANNOTS; x++) {
			titleRow3.createCell(x).setCellStyle(blankStyle);
		}
		for (int x = FIRST_COL_FOR_ANNOTS + 9; x < tray.getColumns().size(); x++) {
			titleRow3.createCell(x).setCellStyle(blankStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress( 4, 4, FIRST_COL_FOR_ANNOTS + 2, FIRST_COL_FOR_ANNOTS + 8));


		HSSFRow titleRow4 = sheet.createRow(rowNumber++);
		cell = titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 1);
		cell.setCellValue("");
		cell.setCellStyle(warningLegendStyle);
		cell = titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 2);
		cell.setCellValue("Blank Annotation (no files are present for this experiment)");
		titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 3).setCellStyle(blankStyle);
		titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 4).setCellStyle(blankStyle);
		titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 5).setCellStyle(blankStyle);
		titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 6).setCellStyle(blankStyle);
		titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 7).setCellStyle(blankStyle);
		titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 8).setCellStyle(rightBorderStyle);
		for (int x = 0; x < FIRST_COL_FOR_ANNOTS; x++) {
			titleRow4.createCell(x).setCellStyle(blankStyle);
		}
		for (int x = FIRST_COL_FOR_ANNOTS + 9; x < tray.getColumns().size(); x++) {
			titleRow4.createCell(x).setCellStyle(blankStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress( 5, 5, FIRST_COL_FOR_ANNOTS + 2, FIRST_COL_FOR_ANNOTS + 8));




		HSSFRow titleRow5 = sheet.createRow(rowNumber++);
		cell = titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 1);
		cell.setCellValue("");
		cell.setCellStyle(successLegendStyle);
		cell = titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 2);
		cell.setCellValue("Complete Annotation (annotation is filled in for every sample)");
		cell.setCellStyle(bottomBorderStyle);
		titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 3).setCellStyle(bottomBorderStyle);
		titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 4).setCellStyle(bottomBorderStyle);
		titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 5).setCellStyle(bottomBorderStyle);
		titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 6).setCellStyle(bottomBorderStyle);
		titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 7).setCellStyle(bottomBorderStyle);
		titleRow5.createCell(FIRST_COL_FOR_ANNOTS + 8).setCellStyle(rightBottomBorderStyle);
		for (int x = 0; x < FIRST_COL_FOR_ANNOTS; x++) {
			titleRow5.createCell(x).setCellStyle(blankStyle);
		}
		for (int x = FIRST_COL_FOR_ANNOTS + 9; x < tray.getColumns().size(); x++) {
			titleRow5.createCell(x).setCellStyle(blankStyle);
		}
		sheet.addMergedRegion(new CellRangeAddress( 6, 6, FIRST_COL_FOR_ANNOTS + 2, FIRST_COL_FOR_ANNOTS + 8));


		// Rest of the Legend, need border before merging cells
		titleRow3.createCell(FIRST_COL_FOR_ANNOTS).setCellStyle(leftBorderStyle);
		titleRow4.createCell(FIRST_COL_FOR_ANNOTS).setCellStyle(leftBottomBorderStyle);
		titleRow5.createCell(FIRST_COL_FOR_ANNOTS).setCellStyle(leftBottomBorderStyle);
		sheet.addMergedRegion(new CellRangeAddress( 3, 6, FIRST_COL_FOR_ANNOTS, FIRST_COL_FOR_ANNOTS));

		
		// Blank Row
		blankRow = sheet.createRow(rowNumber++);
		for ( int idx = 0; idx < tray.getColumns().size(); idx++) {
			cell = blankRow.createCell(idx);
			cell.setCellStyle(blankStyle);
		}
		// Set the rows to repeat from row 1 through 8 on the first sheet.
	    sheet.setRepeatingRows(CellRangeAddress.valueOf("1:8"));
	    
	}

	// Print the column headings
	HSSFRow headerRow = sheet.createRow(rowNumber++);
	Iterator cIter = tray.getColumns().iterator();
	while (cIter.hasNext()) {
		Column col = (Column) cIter.next(); 
		cell = headerRow.createCell(colNumber++);
		cell.setCellValue(col.getName());
		cell.setCellStyle(headerStyle);
	}

		
	// For each row in report tray, create a row in the spreadsheet
	Iterator iter = tray.getRows().iterator();
	while (iter.hasNext()) {
		ReportRow row = (ReportRow) iter.next();
		
		
		// Put a top border before every lab break
		boolean isLabBreak = false;
		if (sheetIndex == MISSING_ANNOT_SHEET && row.getValues().get(0) != null && ((String)row.getValues().get(0)).trim().length() >= 1) {
			isLabBreak = true;			
		}

		
		HSSFRow excelRow = sheet.createRow(rowNumber++);
		excelRow.setHeight(ROW_HEIGHT);
		
		
		// For each value in row, create a cell in the spreadsheet row
		Iterator vIter = row.getValues().iterator();
		colNumber = 0;
		while (vIter.hasNext()) {
			String val = (String) vIter.next();
			
			
			// Is this an annotation cell?
			boolean isStandardAnnotCell = (sheetIndex == MISSING_ANNOT_SHEET && colNumber > FIRST_COL_FOR_ANNOTS) ? true : false;
			boolean isSampleSourceAnnotCell =  (sheetIndex == MISSING_ANNOT_SHEET && colNumber == FIRST_COL_FOR_ANNOTS) ? true : false;
			
			
			// Is this cell numeric?
			boolean isNumeric = false;
			if (isStandardAnnotCell && !val.equals("missing") && !val.equals("missingNew") && !val.equals("complete") && !val.equals("n/a")) {
			 	// If this is a non-blank annot cell that doesn't have an "X" or "n/a", it has a sample count
			 	// and is therefore numeric
				 isNumeric = true;
			} else if (sheetIndex == MISSING_ANNOT_SHEET || sheetIndex == FULLY_ANNOT_SHEET) {
			 	if (colNumber == SAMPLE_COUNT_COL) {
			  		isNumeric = true;
			 	}
			} else if (sheetIndex == SUMMARY_SHEET) {
			 	if (colNumber > LAB_COL && val != null && !val.contains("%")) {
			  		isNumeric = true;
			 	}
			}
			
				
		  
			// Create the cell and set the value and type
			cell = excelRow.createCell(colNumber++);
			if (val == null) {
				val = "";
			  	cell.setCellValue("");
			} else if (isNumeric) {
			 	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			 	cell.setCellValue(Integer.valueOf(val));
			} else {
			 	cell.setCellValue(val);
			}
			if (isLabBreak) {
				cell.setCellStyle(labBreakDefaultStyle);
			} else {
				cell.setCellStyle(defaultStyle);
			}
		 

			// Style the annot cell with a fill color if annotation is missing
			if (isStandardAnnotCell) {
				
				// Standard annot cell - "missing" is filled red, "missingNew" is filled grey, sample count is filled light red
				if (val.equals("missing")) {
					// Blank out the "missing", show as error cell (red) 
 					cell.setCellValue("");
			  		cell.setCellStyle(isLabBreak ? errorTopBorderStyle : errorStyle);
				} else if (val.equals("missingNew")) {
			  		// Grey out "missingNew" for missing annotations on experiments w/o files 
			  		cell.setCellValue("");
			  		cell.setCellStyle(isLabBreak ? warningTopBorderStyle : warningStyle);		  
			 	} else if (val.equals("n/a")) {
			  		// Blank out "n/a".  This annotation is not applicable for experiment. 
			  		// Show as blank cell. 
			  		cell.setCellValue("");
			 		cell.setCellStyle(isLabBreak ? labBreakAnnotStyle : defaultAnnotStyle);
			 	} else if (val.equals("complete")) {
			  		// Blank out "complete" annotations and show as green cell 
			  		cell.setCellValue("");
			  		cell.setCellStyle(isLabBreak ? successTopBorderStyle : successStyle);		  
			 	} else if (isNumeric && !val.equals("")) {
			  		// Show sample count as partial error (light red)
			  		cell.setCellStyle(isLabBreak? partialErrorTopBorderStyle : partialErrorStyle);
			 	} else {
			 		// We should never get to this condition.  If we do, just show
			 		// it as a cell with the value to help troubleshoot the problem.
			 		cell.setCellValue("?");
			 		cell.setCellStyle(isLabBreak ? labBreakAnnotStyle : defaultAnnotStyle);
			 	}
			
			} else if (isSampleSourceAnnotCell) {
			 	// Sample source annot cell - Blank is filled red, "n/a" is filled grey
			 	if (val.equals("missing")) {
			 		cell.setCellValue("");
					cell.setCellStyle(isLabBreak ? errorTopBorderStyle : errorStyle);
			 	}  else if (val.equals("missingNew")) {
			 		cell.setCellValue("");
					cell.setCellStyle(isLabBreak ? warningTopBorderStyle : warningStyle);
			 	}  else {
			 		cell.setCellStyle(isLabBreak ? successTopBorderStyle : successStyle);
			 	}
			}
		  
	 	} // end of loop through cells 
		
		
	} // end of loop through rows
	
	if (sheetIndex == MISSING_ANNOT_SHEET) {
		// Auto size the column widths (skip lab and annotation columns)
		for(int columnIndex = 1; columnIndex <= FIRST_COL_FOR_ANNOTS; columnIndex++) {
		     sheet.autoSizeColumn(columnIndex);
		}
		sheet.setColumnWidth(LAB_COL, LAB_COL_WIDTH);
	} else {
		// For other worksheets, autofit every column
		for(int columnIndex = 0; columnIndex < tray.getColumns().size(); columnIndex++) {
		     sheet.autoSizeColumn(columnIndex);
		}
	}
	sheetIndex++;
	
} // end of loop through trays

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
