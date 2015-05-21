<%@ page import="java.io.*" contentType="application/xml" %>
<jsp:useBean id="xmlResult" type="String" scope="request" />
<%
  if (response != null) {
    response.getWriter().write((xmlResult != null)?xmlResult:"<NO_RETURN />");
    response.getWriter().flush();
    response.getWriter().close();
  }
%>
