<%@ include file="requireSecureRemote.jsp" %>
<HTML>
<HEAD>
<TITLE>ExGen</TITLE>
<%
  String host = request.getRequestURL().substring(0, request.getRequestURL().toString().indexOf(request.getRequestURI()));
%>
<script language="JavaScript">
<!--
var link='<%= host + "/altio44/init_reconnect.jsp?name=" + ((request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "") + "&app=gnomexaltio&webtitle=GNomEx 2.0&conf=views/view.xml&container=AppletHolder4.jsp" %>';
// -->
</script>
</HEAD>
<BODY bgcolor="#ffffff" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0"  onload="window.location=link">
<h3>Reconnecting to GNomEx Application</h3>
</BODY>

</HTML>
