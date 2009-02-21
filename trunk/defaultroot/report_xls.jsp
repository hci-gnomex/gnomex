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
<title>HCI Report - <%=tray.getReportTitle()%></title>

<style type="text/css" media="all">
body {
	margin: 10px;
	font-family: Arial;
}
div.header {
	border-bottom: 1px solid #666666; 
	font-size: 17px;
	font-weight: bold;
	font-family: Arial;
	width: <%=width%>px;
	margin-bottom: 4px;
}
.subheader {
	font-size: 12px;
	font-weight: normal;
	font-family: Arial;
	color: #333333;
}
.tableTitle {
	font-family: Arial;
	font-size: 12px;
	font-weight: bold;
	color: #000000;
}
table.reportTable {
	font-family: Arial;
	font-size: 12px;
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

<div class="tableTitle"><%=tray.getReportDescription()%></div>
<div class="subheader"><%=tray.getReportDate()%></div>
</table>
<p>
<table border="0" width="<%=width%>" cellspacing="0" cellpadding="4" class="reportTable">
<tr>
<%
      Iterator cIter = tray.getColumns().iterator();
      while (cIter.hasNext()) {
      Column col = (Column) cIter.next();
%>
<td align="left" style="border-bottom: 2px solid #000000"><strong><%=col.getCaption()%></strong></td>
<%
}
%>
</tr>
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
<tr>
<td align="left" style="border-top: 1px solid #000000"  colspan="<%=tray.getColumns().size()-1%>">&nbsp;</td>
<td align="left" style="border-top: 1px solid #000000">&nbsp</td>
</tr>
</table>
</p>
</body>
</html>