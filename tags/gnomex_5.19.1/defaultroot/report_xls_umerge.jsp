<%@ page language="java" import="java.util.Iterator,hci.report.model.ReportRow,hci.report.model.Column" %>
<jsp:useBean id="tray" class="hci.report.model.ReportTray" scope="request"/>
<%
response.setHeader("Content-type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=\""+tray.getFileName()+".xls\"");
// Some vars for the row colors
String bgcolor;
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

<style type="text/css" media="all">
body {
	margin: 11px;
	font-family: Arial;
}
table.reportTable {
	font-family: Arial;
	font-size: 10pt;
}
</style>
<style type="text/css" media="print">
tr.rowColor {
	background-color: #ffffff;
}
tr.rowColorWht {
	background-color: #ffffff;
}
</style>
</head>
<body>
<table border="0" width="<%=width%>" cellspacing="0" cellpadding="0" class="reportTable">

<%
 if (tray.getRows().size() > 0) {
	 Iterator iter = tray.getRows().iterator();
	 bgcolor = " bgcolor=\"#ffffff\"";
	 count = 0;
	 while (iter.hasNext()) {
	   ReportRow row = (ReportRow) iter.next();
	   if (count % 2 > 0) {
	     bgcolor = " bgcolor=\"#ffffff\"";
	   }
	%>
	<tr <%=bgcolor%>>
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
	    bgcolor = " bgcolor=\"#ffffff\"";
	  }
} 
 else {
   %>
   <tr><td colsoan="<%=tray.getColumns().size()%>"> No results </td></tr>
   <%
 }
%>

</table>

</body>
</html>