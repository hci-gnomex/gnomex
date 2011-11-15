<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Reset your Password</title>
</head>

<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
%>

<body>

<div class="header-bar" >
  <div class="left"><h1>GNomEx</h1></div>
  <div class="rightMenu" >
      <a href="login.jsp">Login</a> |    
      <a href="change_password.jsp">Change password</a> |    
      <a href="reset_password.jsp">Reset password</a> |    
      <a href="register_user.jsp">Create a new account</a> 
  </div>
</div>

<div id="content" align="center" bgcolor="white">
 <div class="containerMessage">
    <h3>Password reset.</h3>
    Your new password has been emailed to you.
 </div> 
</div>
</body>
</html>