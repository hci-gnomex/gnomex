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


// Column numbers of significance (requires special logic)
int     FIRST_COL_FOR_ANNOTS = 5;
int     SAMPLE_COUNT_COL = 2;
int     LAB_COL = 0;

int     LAB_COL_WIDTH = 6000;
short   SEPARATOR_ROW_HEIGHT = 50;

// Colors
HSSFPalette palette = workbook.getCustomPalette();
palette.setColorAtIndex(new Byte((byte) PALLETE_RED_INDEX),       new Byte((byte) 232), new Byte((byte) 118), new Byte((byte) 130));
palette.setColorAtIndex(new Byte((byte) PALLETE_LIGHT_RED_INDEX), new Byte((byte) 245), new Byte((byte) 206), new Byte((byte) 210));
palette.setColorAtIndex(new Byte((byte) PALLETE_GREY_INDEX),      new Byte((byte) 207), new Byte((byte) 207), new Byte((byte) 207));
palette.setColorAtIndex(new Byte((byte) PALLETE_LIGHT_GREY_INDEX),new Byte((byte) 240), new Byte((byte) 240), new Byte((byte) 240));
palette.setColorAtIndex(new Byte((byte) PALLETE_DARK_GREY_INDEX),new Byte((byte) 115),  new Byte((byte) 110), new Byte((byte) 111));
palette.setColorAtIndex(new Byte((byte) PALLETE_WHITE_INDEX),     new Byte((byte) 255),   new Byte((byte) 255),   new Byte((byte) 255));

// Fonts		
short fontHeightTitle = 14;
short fontHeightHeading = 11;
short fontHeightBody = 11;

// Body style
HSSFFont defaultFont = workbook.createFont();
defaultFont.setFontHeightInPoints(fontHeightBody);
HSSFCellStyle bodyStyle = workbook.createCellStyle();
bodyStyle.setFont(defaultFont);

// Title style
HSSFFont font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
font.setFontHeightInPoints(fontHeightTitle);
HSSFCellStyle titleStyle = workbook.createCellStyle();
titleStyle.setFont(font);
titleStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_GREY_INDEX).getIndex());
titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
titleStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_GREY_INDEX).getIndex());



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

HSSFCellStyle errorLegendStyle = workbook.createCellStyle();
errorLegendStyle.setFillForegroundColor(palette.getColor(PALLETE_RED_INDEX).getIndex());
errorLegendStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
errorLegendStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle errorTopBorderStyle = workbook.createCellStyle();
errorTopBorderStyle.setFillForegroundColor(palette.getColor(PALLETE_RED_INDEX).getIndex());
errorTopBorderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
errorTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
errorTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

   
// Partial Error cell 
HSSFCellStyle partialErrorStyle = workbook.createCellStyle();
partialErrorStyle.setFont(defaultFont);
partialErrorStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_RED_INDEX).getIndex());
partialErrorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
partialErrorStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

HSSFCellStyle partialErrorLegendStyle = workbook.createCellStyle();
partialErrorLegendStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_RED_INDEX).getIndex());
partialErrorLegendStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

HSSFCellStyle partialErrorTopBorderStyle = workbook.createCellStyle();
partialErrorTopBorderStyle.setFont(defaultFont);
partialErrorTopBorderStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_RED_INDEX).getIndex());
partialErrorTopBorderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
partialErrorTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);



// Warning cell  (grey)
HSSFCellStyle warningStyle = workbook.createCellStyle();
warningStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
warningStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

HSSFCellStyle warningLegendStyle = workbook.createCellStyle();
warningLegendStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
warningLegendStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
warningLegendStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);

HSSFCellStyle warningTopBorderStyle = workbook.createCellStyle();
warningTopBorderStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
warningTopBorderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
warningTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);

// Blank row style
HSSFCellStyle blankStyle = workbook.createCellStyle();
blankStyle.setFillForegroundColor(palette.getColor(PALLETE_WHITE_INDEX).getIndex());
blankStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


// Border styles
HSSFCellStyle  leftTopBorderStyle=workbook.createCellStyle();
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

HSSFCellStyle  rowTopBorderStyle=workbook.createCellStyle();
rowTopBorderStyle.setFont(defaultFont);
rowTopBorderStyle.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);




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
	
	
	// Legend
	HSSFRow titleRow2 = sheet.createRow(rowNumber++);
	cell = titleRow2.createCell(FIRST_COL_FOR_ANNOTS );
	cell.setCellValue("LEGEND");
	cell.setCellStyle(leftTopBorderStyle);
	
	cell = titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 1);
	cell.setCellStyle(errorLegendStyle);
	cell = titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 2);
	cell.setCellValue("= Missing Annotation");
	cell.setCellStyle(topBorderStyle);
	titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 3).setCellStyle(topBorderStyle);
	titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 4).setCellStyle(topBorderStyle);
	titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 5).setCellStyle(topBorderStyle);
	titleRow2.createCell(FIRST_COL_FOR_ANNOTS + 6).setCellStyle(rightTopBorderStyle);

	HSSFRow titleRow3 = sheet.createRow(rowNumber++);
	cell = titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 1);
	cell.setCellValue("");
	cell.setCellStyle(partialErrorLegendStyle);
	cell = titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 2);
	cell.setCellValue("= Missing Annotation (for some samples on experiment)");
	titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 3);
	titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 4);
	titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 5);
	titleRow3.createCell(FIRST_COL_FOR_ANNOTS + 6).setCellStyle(rightBorderStyle);

	HSSFRow titleRow4 = sheet.createRow(rowNumber++);
	cell = titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 1);
	cell.setCellValue("");
	cell.setCellStyle(warningLegendStyle);
	cell = titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 2);
	cell.setCellValue("= Blank Annotation (on new experiment, no files)");
	cell.setCellStyle(bottomBorderStyle);
	titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 3).setCellStyle(bottomBorderStyle);
	titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 4).setCellStyle(bottomBorderStyle);
	titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 5).setCellStyle(bottomBorderStyle);
	titleRow4.createCell(FIRST_COL_FOR_ANNOTS + 6).setCellStyle(rightBottomBorderStyle);

	// Rest of the Legend, need border before merging cells
	titleRow3.createCell(FIRST_COL_FOR_ANNOTS).setCellStyle(leftBorderStyle);
	titleRow4.createCell(FIRST_COL_FOR_ANNOTS).setCellStyle(leftBottomBorderStyle);
	sheet.addMergedRegion(new CellRangeAddress( 2, 4, FIRST_COL_FOR_ANNOTS - 1, FIRST_COL_FOR_ANNOTS - 1));

	
	// Blank Row
	blankRow = sheet.createRow(rowNumber++);
	for ( int idx = 0; idx < tray.getColumns().size(); idx++) {
		cell = blankRow.createCell(idx);
		cell.setCellStyle(blankStyle);
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
			if (isStandardAnnotCell && !val.equals("") && !val.equals(" ") && !val.equals("X") && !val.equals("n/a")) {
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
				cell.setCellStyle(rowTopBorderStyle);
			} else {
				cell.setCellStyle(bodyStyle);
			}
		 

			// Style the annot cell with a fill color if annotation is missing
			if (isStandardAnnotCell) {
				
				// Standard annot cell - "X" is filled red, "n/a" is filled grey, sample count is filled light red
				if (val.equals("X")) {
					// Blank out the "X", show as error cell (red) 
 					cell.setCellValue("");
			  		cell.setCellStyle(isLabBreak ? errorTopBorderStyle : errorStyle);
				} else if (val.equals("n/a")) {
			  		// Grey out "n/a" for missing annotations on experiments w/o files 
			  		cell.setCellValue("");
			  		cell.setCellStyle(isLabBreak ? warningTopBorderStyle : warningStyle);		  
			 	} else if (isNumeric && !val.equals("")) {
			  		// Show sample count as partial error (light red)
			  		cell.setCellStyle(isLabBreak? partialErrorTopBorderStyle : partialErrorStyle);
			 	}
			
			} else if (isSampleSourceAnnotCell) {
			 	// Sample source annot cell - Blank is filled red, "n/a" is filled grey
			 	if (val.equals("") || val.equals(" ")) {
					cell.setCellStyle(isLabBreak ? errorTopBorderStyle : errorStyle);
			 	} else if (val.equals("n/a")) {
			  		// Grey out "n/a" for missing annotations on experiments w/o files 
			  		cell.setCellValue("");
			  		cell.setCellStyle(isLabBreak ? warningTopBorderStyle : warningStyle);		  
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
