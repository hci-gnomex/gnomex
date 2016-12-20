<%@ page import="java.io.*" contentType="application/json" %>
<jsp:useBean id="xmlResult" type="String" scope="request" />
<%
  if (response != null) {
      if (xmlResult != null) {
          out.println(xmlResult);
      } else {
          out.println("{NO_RETURN }");
      }
  }
%>