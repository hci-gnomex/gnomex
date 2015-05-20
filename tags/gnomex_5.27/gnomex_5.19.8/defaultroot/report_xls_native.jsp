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
<%@ page language="java" import="hci.report.model.ReportTray"%>
<%@ page language="java" import="hci.gnomex.model.ReportTrayList"%>

<jsp:useBean id="reportTrayList" class="hci.gnomex.model.ReportTrayList" scope="request"/>
<%
response.setHeader("Content-type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=\""+reportTrayList.getFileName()+".xls\"");



// Create a workbook
HSSFWorkbook workbook = new HSSFWorkbook();
HSSFCreationHelper createHelper = workbook.getCreationHelper();

// Palette for custom color
int     PALLETE_RED_INDEX = 41;
int     PALLETE_LIGHT_RED_INDEX = 42;
int     PALLETE_GREY_INDEX = 43;
		
HSSFPalette palette = workbook.getCustomPalette();
palette.setColorAtIndex(new Byte((byte) PALLETE_RED_INDEX),       new Byte((byte) 247), new Byte((byte) 139), new Byte((byte) 139));
palette.setColorAtIndex(new Byte((byte) PALLETE_LIGHT_RED_INDEX), new Byte((byte) 245), new Byte((byte) 176), new Byte((byte) 176));
palette.setColorAtIndex(new Byte((byte) PALLETE_GREY_INDEX),      new Byte((byte) 217), new Byte((byte) 217), new Byte((byte) 217));

		
short fontHeightTitle = 14;
short fontHeightHeading = 12;

// Create workbook styles

// Title style
HSSFFont font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
font.setFontHeightInPoints(fontHeightTitle);
HSSFCellStyle titleStyle = workbook.createCellStyle();
titleStyle.setFont(font);


// Header style
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle headerStyle = workbook.createCellStyle();
headerStyle.setFont(font);
font.setFontHeightInPoints(fontHeightHeading);
headerStyle.setWrapText(true);
headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

//Annotation Header style
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
HSSFCellStyle annotHeaderStyle = workbook.createCellStyle();
annotHeaderStyle.setFont(font);
annotHeaderStyle.setWrapText(true);
annotHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
annotHeaderStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
annotHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

//Annotation Header style
font = workbook.createFont();
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
font.setFontHeightInPoints(fontHeightHeading);
HSSFCellStyle annotParentHeaderStyle = workbook.createCellStyle();
annotParentHeaderStyle.setFont(font);
annotParentHeaderStyle.setWrapText(true);
annotParentHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
annotParentHeaderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
annotParentHeaderStyle.setFillForegroundColor(palette.getColor(PALLETE_GREY_INDEX).getIndex());
annotParentHeaderStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);


// Date + time style
HSSFCellStyle dateTimeCellStyle = workbook.createCellStyle();
dateTimeCellStyle.setDataFormat( createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
font = workbook.createFont();
font.setFontHeightInPoints(fontHeightTitle);
font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
dateTimeCellStyle.setFont(font);
dateTimeCellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

// Error cell 
HSSFCellStyle errorStyle = workbook.createCellStyle();
errorStyle.setFillForegroundColor(palette.getColor(PALLETE_RED_INDEX).getIndex());
errorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
errorStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
   
// Partial Error cell 
HSSFCellStyle partialErrorStyle = workbook.createCellStyle();
partialErrorStyle.setFillForegroundColor(palette.getColor(PALLETE_LIGHT_RED_INDEX).getIndex());
partialErrorStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
partialErrorStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);


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
	cell.setCellStyle(titleStyle);

	// Report date
	titleRow = sheet.createRow(rowNumber++);
	cell = titleRow.createCell(0);
	cell.setCellValue(tray.getReportDate());
	cell.setCellStyle(dateTimeCellStyle);
	
	// Blank line
	titleRow = sheet.createRow(rowNumber++);
	
	// If this is the missing annotations report (first worksheet),
	// show the header "Missing Annotations" that spans
	// column 6 through 21
	if (tray.getReportTitle().contains("Missing")) {
		titleRow = sheet.createRow(rowNumber++);
		cell = titleRow.createCell(5);
		cell.setCellValue("Missing Annotations");
		cell.setCellStyle(annotParentHeaderStyle);
		sheet.addMergedRegion(new CellRangeAddress(
	            3, //first row (0-based)
	            4, //last row  (0-based)
	            5, //first column (0-based)
	            21  //last column  (0-based)
	    ));
		rowNumber++;
		
	}


	// Print the column headings
	HSSFRow headerRow = sheet.createRow(rowNumber++);
	Iterator cIter = tray.getColumns().iterator();
	while (cIter.hasNext()) {
		Column col = (Column) cIter.next(); 
		cell = headerRow.createCell(colNumber++);
		cell.setCellValue(col.getName());
		
		if (tray.getReportTitle().contains("Missing") && colNumber >= 6) {
			cell.setCellStyle(annotHeaderStyle);

		} else {
			cell.setCellStyle(headerStyle);
		}
		
		
	}

	// For each report tray row, generate an excel spreadsheet row.
	if (tray.getRows().size() > 0) {
		 Iterator iter = tray.getRows().iterator();
		 // For each row in report tray, create a row in the spreadsheet
		 while (iter.hasNext()) {
			ReportRow row = (ReportRow) iter.next();
			HSSFRow excelRow = sheet.createRow(rowNumber++);
			
			
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
			  
			  // If this is a missing annotation, color the background of the cell as red
			  if (tray.getReportTitle().contains("Missing")) {
	
				  if (colNumber > 6) {
					  if (!val.equals("") && !val.equals(" ")) {
						  
						  // Blank out the "X", show as error cell (red) 
						  if (val.equals("X")) {
							  cell.setCellValue("");
							  cell.setCellStyle(errorStyle);
						  } else {
							  // Show sample count as partial error (light red)
							  cell.setCellValue(Integer.valueOf(val));
							  cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							  cell.setCellStyle(partialErrorStyle);
						  }
					  }
				  }
				  // Sample source is just the opposite.  If there is a blank value, make the
				  // cell style error
				  if (colNumber == 6) {
					  if (val.equals("") || val.equals(" ")) {
						  cell.setCellValue(val);
						  cell.setCellStyle(errorStyle);
					  }
					  
				  }
			  }
			}
			
			
		 }
	}
	
	// Auto size the column widths
	for(int columnIndex = 0; columnIndex < 6; columnIndex++) {
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
