<HTML>
<HEAD>
<link rel="stylesheet" type="text/css" href="css/message.css" />
<TITLE>Upload analysis file</TITLE>
</HEAD>
<BODY bgcolor="#FFFFF0">
<h3>Upload analysis file</h3>
<form enctype="multipart/form-data"  method="post" action="UploadAnalysisFileServlet.gx">

<p>
<input type="hidden" name="analysisNumber" value="<%=request.getParameter("analysisNumber")%>">
<%=request.getParameter("analysisNumber")%>
<br>
<%=request.getParameter("analysisName")%>
<p>
File:&nbsp;<input type="file" name="file"  size="45">
<p>
<p>
<input type="submit" name="Show" value="Upload">
</form>

<p class="message">
<%
  if (request.getAttribute("message") != null) {
    %>
    <%=request.getAttribute("message")%>
    <%
  }
   %>
</p>

</BODY>

</HTML>