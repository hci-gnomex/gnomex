<%@ page import="javax.servlet.*, javax.servlet.http.*, java.net.* hci.gnomex.constants.*" %>
<%

if (Constants.REQUIRE_SECURE_REMOTE && !request.isSecure()) {
  if (request.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
      || request.getRemoteAddr().equals("127.0.0.1")) {
  }
  else {
     response.sendRedirect("/messageNotSecure.jsp");
  }
}
%>
