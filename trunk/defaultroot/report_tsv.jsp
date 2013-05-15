<%@ page language="java" import="java.util.Iterator,hci.report.model.ReportRow,hci.report.model.Column"  contentType="text/csv"
%><jsp:useBean id="tray" class="hci.report.model.ReportTray" scope="request"
/><%response.setHeader("Content-type","text/text/tab-separated-values");
response.setHeader("Content-Disposition","attachment; filename=\""+tray.getFileName()+".txt\"");
Iterator cIter = tray.getColumns().iterator();
while (cIter.hasNext()) {
	Column col = (Column) cIter.next();
	out.print(col.getCaption());
	if (cIter.hasNext()) {
	  out.print(",");
	} else {
	  out.println();
	}
}
 if (tray.getRows().size() > 0) {
	 Iterator iter = tray.getRows().iterator();
	 while (iter.hasNext()) {
	 ReportRow row = (ReportRow) iter.next();
	 Iterator vIter = row.getValues().iterator();
	 while (vIter.hasNext()) {
	      String val = (String) vIter.next();
	    	out.print(val);
	    	if (vIter.hasNext()) {
	    	  out.print("\t");
	    	} else {
	    	  out.println();
	    	}
		}
  }
} 
 else {
   out.println("No results");
 }%>