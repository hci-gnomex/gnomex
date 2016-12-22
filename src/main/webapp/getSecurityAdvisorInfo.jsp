<%@ include file="requireSecureRemote.jsp" %>
<%@ page import="org.jdom.*" %>
<%@ page import="org.jdom.output.*" %>
<%@ page import="hci.gnomex.security.SecurityAdvisor" %>
<%@ page import="hci.framework.model.DetailObject" %>
<%
SecurityAdvisor sa = (SecurityAdvisor)session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
if (sa != null) {
    XMLOutputter xmlOutputter = new org.jdom.output.XMLOutputter();
    Document doc = sa.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL);
    out.println(xmlOutputter.outputString(doc));
}
%>
