<%@ page language="java" import="java.util.Iterator,hci.report.model.ReportRow,hci.report.model.Column,java.util.HashMap" %>
<jsp:useBean id="tray" class="hci.report.model.ReportTray" scope="request"/>
<%
response.setHeader("Content-type","application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=\""+tray.getFileName()+".xls\"");
// Some vars for the row colors
int count;
int width = 1000;
HashMap widthMap = new HashMap();

widthMap.put(new Integer(1),   new Integer(12));
widthMap.put(new Integer(3),   new Integer(26));
widthMap.put(new Integer(4),   new Integer(33));
widthMap.put(new Integer(5),   new Integer(40));
widthMap.put(new Integer(6),   new Integer(47));
widthMap.put(new Integer(10),  new Integer(75));
widthMap.put(new Integer(15),  new Integer(110));
widthMap.put(new Integer(16),  new Integer(117));
widthMap.put(new Integer(30),  new Integer(215));
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns:o="urn:schemas-microsoft-com:office:office"
xmlns:x="urn:schemas-microsoft-com:office:excel"
xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<head>
<style id="umerge_microarray_test_2009-01-31_26985_Styles">
<!--table
	{mso-displayed-decimal-separator:"\.";
	mso-displayed-thousand-separator:"\,";}
.myStyle
	{mso-number-format:General;
	text-align:left;
	vertical-align:bottom;
	white-space:nowrap;
	mso-rotate:0;
	mso-background-source:auto;
	mso-pattern:auto;
	color:windowtext;
	font-size:8.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:"Courier New", monospace;
	mso-generic-font-family:auto;
	mso-font-charset:0;
	border:none;
	mso-protection:locked visible;
	mso-style-name:Normal;
	mso-style-id:0;}


-->
</style>
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
	      Object v = vIter.next();
	      String val = null;
	      String span = "";
	      Integer widthPixels = new Integer(40);
	      if (v instanceof String) {
		      val = (String)v;
		    	%>
		      <td class="myStyle" align="left" colspan=17><%=val%></td>
      		<%
       } else if (v instanceof Object[]) {
	        Object[] valInfo = (Object[])v;
	        val               = (String)valInfo[0];
	        String xVal       = (String)valInfo[1];
	        Integer textWidth = (Integer)valInfo[2];
	        String align      = (String)valInfo[3];
	        widthPixels = (Integer)widthMap.get(textWidth);
	        if (widthPixels != null) {
		    	  %>
		        <td class="myStyle" align="<%=align%>" width="<%=widthPixels.toString()%>" x:str='<%=xVal%>'><%=val%></td>
      		  <%
	        } else {
		    	  %>
		        <td class="myStyle" align="<%=align%>" colspan="17" x:str='<%=xVal%>'><%=val%></td>
      		  <%
	          
	        }
	      }
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