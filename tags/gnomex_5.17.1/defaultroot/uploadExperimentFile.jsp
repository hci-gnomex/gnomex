<HTML>
<HEAD>
<link rel="stylesheet" type="text/css" href="css/message.css" />
<TITLE>Upload experiment files</TITLE>
</HEAD>
<BODY bgcolor="#FFFFF0">
<img src="images/navbar.png"/>
<br>
<h3>Upload experiment files for <%=request.getParameter("requestNumber")%></h3>
<form enctype="multipart/form-data"  method="post" action="UploadExperimentFileServlet.gx">

<p>
<input type="hidden" name="requestNumber" value="<%=request.getParameter("requestNumber")%>">
<br>
File:&nbsp;<input type="file" name="file"  size="45">
<p>
File:&nbsp;<input type="file" name="file"  size="45">
<p>
File:&nbsp;<input type="file" name="file"  size="45">
<p>
File:&nbsp;<input type="file" name="file"  size="45">
<p>
File:&nbsp;<input type="file" name="file"  size="45">
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
