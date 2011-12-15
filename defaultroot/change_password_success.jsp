<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Change GNomEx Password</title>
</head>

<%
String parm1 = request.getParameter("parm1");
String parm2 = request.getParameter("parm2");
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");

//Set Cache-Control to no-cache.
response.setHeader("Cache-Control", "max-age=0, must-revalidate");

session.removeAttribute("j_username");
session.removeAttribute("j_password");
session.removeAttribute("User");
session.removeAttribute("user");
session.removeAttribute("username");
session.removeAttribute("gnomexSecurityAdvisor");
session.removeAttribute("logined"); 
session.removeAttribute("context"); 

session.invalidate();
   
%>

<body>

<div class="header-bar" >
  <div class="left"><h1>GNomEx</h1></div>
  <div class="rightMenu" >
      <a href="gnomexFlex.jsp">Login</a> |    
      <a href="change_password.jsp">Change password</a> |    
      <a href="reset_password.jsp">Reset password</a> |    
      <a href="register_user.jsp">Create a new account</a> 
  </div>
</div>

<div id="content" align="center" bgcolor="white">
 <div class="containerMessage">
    <h3>Your password has been changed.</h3>
 </div> 
</div>
</body>
</html>