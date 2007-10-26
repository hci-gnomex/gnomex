<%@ include file="requireSecureRemote.jsp" %>
<HTML>
<HEAD>
<TITLE>GNomEx Application</TITLE>
<%
  String host = request.getRequestURL().substring(0, request.getRequestURL().toString().indexOf(request.getRequestURI()));
  String launchParm1 = request.getParameter("launchParm1");
  String launchParm2 = request.getParameter("launchParm2");

%>
<script language="JavaScript">
<!--
// Set applet width to a minimum of 1260x944, but allow to be larger for large screens
var appletWidth = 1260;
var appletHeight = 944;
if (screen.width > 1280 || screen.height > 1024) {
  appletWidth = screen.width - 20;
  appletHeight = screen.height - 80;
}
var link='<%= host + "/altio44/init.jsp?name=" + ((request.getUserPrincipal() != null) ? request.getUserPrincipal().getName() : "") + "&app=gnomexaltio&webtitle=GNomEx&conf=views/view.xml&container=AppletHolder4.jsp" %>&appWidth=' + appletWidth + '&appHeight=' + appletHeight;
link = link + '<%= (launchParm1 != null && ! launchParm1.equals(""))?("&hci.logon.gnomexLaunchParm1=" + launchParm1):"" %>';
link = link + '<%= (launchParm2 != null && ! launchParm2.equals(""))?("&hci.logon.gnomexLaunchParm2=" + launchParm2):"" %>';
link = link + '&hci.logon.gnomexLaunchParm3=N';
var appWindow;
function openApp(theURL,winName,features) { //v2.0
  if (!appWindow || appWindow.closed) {
    appWindow = window.open(theURL,winName,features);
    window.opener=null;
    window.setTimeout('window.close()', 2000);
  } else {
    appWindow.focus();
  }
}
// -->
</script>
</HEAD>
<BODY bgcolor="#ffffff" topmargin="0" leftmargin="0" marginwidth="0" marginheight="0"  onload="openApp(link,'gnomexhome','width=' + (screen.width - 20) + ',height=' + (screen.height - 80) + 'hotkeys=no,top=0,left=0,resizable=yes,scrollbars=yes');">
<h3>Starting GNomEx application, please enable pop up windows for this site.</h3>
</BODY>

</HTML>
