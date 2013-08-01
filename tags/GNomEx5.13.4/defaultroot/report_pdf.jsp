<%@ page language="java" buffer="8kb" 
import="java.io.*,
		com.lowagie.text.*,
		com.lowagie.text.pdf.*,
		java.awt.Color,
		java.util.Iterator,
		hci.report.model.ReportRow,
		hci.report.model.Column"
%>
<jsp:useBean id="tray" class="hci.report.model.ReportTray" scope="request"/>
<%
response.setContentType("application/pdf");
response.setHeader("Content-Disposition","attachment; filename=\""+tray.getFileName()+".pdf\"");
Document doc = null;
if (tray.getColumns().size() > 6 && tray.getColumns().size() < 10) {
  doc = new Document(PageSize.LETTER.rotate());
} else if (tray.getColumns().size() >= 10) {
  Rectangle pagesize = new Rectangle(tray.getColumns().size()*100,792);
  doc = new Document(pagesize);
} else {
  doc = new Document(PageSize.LETTER);
}
ByteArrayOutputStream buffer = new ByteArrayOutputStream();
PdfWriter.getInstance(doc, buffer);
doc.open();

// Some vars for the row colors
Color bgcolor = null;
int count;
Paragraph p1 = new Paragraph(new Chunk("HCI Report - "+tray.getReportTitle(), new Font(Font.HELVETICA, 14,Font.BOLD)));
p1.setSpacingAfter(6f);
doc.add(p1);
Graphic g = new Graphic();
g.setHorizontalLine(1f,100f,new Color(0x66,0x66,0x66));
doc.add(g);
Paragraph p2 = new Paragraph(new Chunk(tray.getReportDescription(),new Font(Font.HELVETICA,11,Font.NORMAL)));
doc.add(p2);
Paragraph p3 = new Paragraph(new Chunk(tray.getReportDate().toString(),new Font(Font.HELVETICA,11,Font.NORMAL)));
p3.setAlignment(Element.ALIGN_RIGHT);
doc.add(p3);

// Build the table
if (tray.getColumns().size() > 0) {
  PdfPTable table = new PdfPTable(tray.getColumns().size());
  table.setSpacingBefore(10f);
  table.setSpacingAfter(10f);
  table.setWidthPercentage(100f);
  PdfPCell cell = null;
	Iterator cIter = tray.getColumns().iterator();
	while (cIter.hasNext()) {
	Column col = (Column) cIter.next();
		cell = new PdfPCell(new Paragraph(col.getCaption(),new Font(Font.HELVETICA,12,Font.BOLD,new Color(0,0,0))));
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setPadding(6f);
		cell.setBorderWidth(0f);
		cell.setBorderWidthBottom(2f);
		cell.setBorderColorBottom(new Color(0,0,0));
		cell.setBackgroundColor(new Color(255,255,255));
		table.addCell(cell);
	}

	if (tray.getRows().size() > 0) {
		 Iterator iter = tray.getRows().iterator();
		 bgcolor = new Color(0xEE,0xEE,0xE0);
		 count = 0;
		 while (iter.hasNext()) {
		   ReportRow row = (ReportRow) iter.next();
		   if (count % 2 > 0) {
		     bgcolor = new Color(255,255,255);
		   }
	   Iterator vIter = row.getValues().iterator();
	   while (vIter.hasNext()) {
			String val = (String) vIter.next();
			if (val == null) {
			  val = " ";
			}
				cell = new PdfPCell(new Paragraph(val,new Font(Font.HELVETICA,11,Font.NORMAL,new Color(0,0,0))));
				cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
				cell.setPadding(4f);
				cell.setBackgroundColor(bgcolor);
				cell.setBorderWidth(0f);
				if (!iter.hasNext()) {
					cell.setBorderWidthBottom(0.5f);
					cell.setBorderColorBottom(new Color(0,0,0));
					cell.setPaddingBottom(4f);
				}
				table.addCell(cell);
			}
		    count++;
		    bgcolor = new Color(0xEE,0xEE,0xE0);
		  }
	} 
	else {
	  cell = new PdfPCell(new Paragraph("No Results", new Font(Font.HELVETICA,12,Font.BOLD)));
	  cell.setColspan(tray.getColumns().size());
	  cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
	  cell.setPadding(4f);
	  cell.setBorderWidth(0f);
	  table.addCell(cell);
	}
	doc.add(table);
} else {
  doc.add(new Paragraph("No Results", new Font(Font.HELVETICA,12,Font.BOLD)));
}

doc.close();
DataOutput output = new DataOutputStream( response.getOutputStream() );
byte[] bytes = buffer.toByteArray();
response.setContentLength(bytes.length);
response.setBufferSize(bytes.length+1000);
for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }
%>