<%@ page import="java.io.*" contentType="application/xml" %>
<jsp:useBean id="xmlResult" type="String" scope="request" />
<%
  if (response != null) {
    java.io.OutputStream os = response.getOutputStream();
    OutputStreamWriter osWriter = new OutputStreamWriter(os, "UTF-8");
    osWriter.write((xmlResult != null)?xmlResult:"<NO_RETURN />");
    osWriter.flush();
    osWriter.close();
  }
%>
