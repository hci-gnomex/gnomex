<%
String requestNumber     = request.getParameter("launchParm1");
String windowName        = request.getParameter("launchParm2");
String isGuest           = request.getParameter("launchParm3");
%>
<LaunchInfo requestNumber="<%=requestNumber%>" windowName="<%=windowName%>" isGuest="<%=isGuest%>"/>