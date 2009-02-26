<%@ page language="java" import="java.util.Iterator,hci.report.model.ReportRow,hci.report.model.Column" %>
<jsp:useBean id="tray" class="hci.report.model.ReportTray" scope="request"/>
<%
response.setHeader("Content-type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=\""+tray.getFileName()+".xls\"");
// Some vars for the row colors
int count;

int width = 640;
if (tray.getColumns().size() > 6) {
	width = tray.getColumns().size()*100; 
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
</head>
<body>
<table border="0" width="<%=width%>" cellspacing="0" cellpadding="4" class="reportTable">
<%
 if (tray.getRows().size() > 0) {
	 Iterator iter = tray.getRows().iterator();
	 count = 0;
	 while (iter.hasNext()) {
	   ReportRow row = (ReportRow) iter.next();
	%>
	<tr>
	<%
	    Iterator vIter = row.getValues().iterator();
	    while (vIter.hasNext()) {
	      String val = (String) vIter.next();
	      if (val == null) {
	        val = "&nbsp;";
	      } 
	%>
	<td align="left"><%=val%></td>
	<%
	}
	%>
	</tr>
	<%
	    count++;
	  }
} 
 else {
   %>
   <tr><td colspan="<%=tray.getColumns().size()%>"> No results </td></tr>
   <%
 }
%>

</table>
</body>
</html>