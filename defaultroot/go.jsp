<%@ include file="requireSecureRemote.jsp" %>
<HTML>
<HEAD>
<TITLE>GNomEx</TITLE>

<%
String parm1 = request.getParameter("parm1");
String parm2 = request.getParameter("parm2");
%>

</HEAD>
<BODY bgcolor="#ffffff" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0"  onload="window.location='CreateSecurityAdvisor.gx?launchAction=/launcher.jsp&launchParm1=<%= parm1 %>&launchParm2=<%= parm2 %>'">
<h3>Starting GNomEx application, please enable pop up windows for this site.</h3>
</BODY>

</HTML>