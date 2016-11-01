<%@page import="com.itextpdf.text.Font.FontFamily"%>
<%@ page language="java" buffer="8kb" 
import="java.io.*,
		com.itextpdf.text.*,
		com.itextpdf.text.pdf.*,
		java.awt.Color,
		java.util.Iterator,
		hci.report.model.ReportRow,
		hci.report.model.Column"
%>
<%@ page trimDirectiveWhitespaces="true" %>
<jsp:useBean id="tray" class="hci.report.model.ReportTray" scope="request"/>
<%
response.setContentType("application/pdf");
response.setHeader("Content-Disposition","attachment; filename=\"" + tray.getFileName() + ".pdf\"");
Document doc = new Document(PageSize.LETTER);
ByteArrayOutputStream buffer = new ByteArrayOutputStream();
PdfWriter.getInstance(doc, buffer);
doc.open();

doc.addCreationDate();
doc.addTitle(tray.getReportTitle());

if (tray.getRows().size() > 0) {
	Iterator iterator = tray.getRows().iterator();
	while (iterator.hasNext()) {
		doc.add((Element) iterator.next());
	}
} else {
	doc.add(new Paragraph("No Results"));
}

doc.close();
DataOutput output = new DataOutputStream( response.getOutputStream() );
byte[] bytes = buffer.toByteArray();
response.setContentLength(bytes.length);
response.setBufferSize(bytes.length+1000);
for( int i = 0; i < bytes.length; i++ ) { output.writeByte( bytes[i] ); }
((DataOutputStream) output).flush();
((DataOutputStream) output).close();
%>