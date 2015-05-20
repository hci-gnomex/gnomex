<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"none");
%>

<HTML>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="message.css" type="text/css" />
	<title>GNomEx GL Interface Error</title>
</head>
<H3>
<%= message %>
</H3>
</HTML>