<HTML>
<HEAD>
<link rel="stylesheet" type="text/css" href="css/message.css" />
<TITLE>Quote Info</TITLE>
</HEAD>
<BODY bgcolor="#FFFFF0">
<img src="images/navbar.png"/>
<br>
<h3>Enter quote information for request <%=request.getParameter("requestNumber")%></h3>
<form enctype="multipart/form-data"  method="post" action="UploadQuoteInfoServlet.gx">

<p>
<input type="hidden" name="requestNumber" value="<%=request.getParameter("requestNumber")%>">
<br>
Quote #:&nbsp;<input type="text" name="quoteNumber" size="35">
<p>
File:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="file" name="file" size="35">

<p>
<p>
<input type="submit" name="Show" value="Submit">
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
