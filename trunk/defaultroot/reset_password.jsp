<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Reset GNomEx Password</title>
	<script type="text/javascript">
		function setFocus()
		{
     		theform.username.focus();
		}
	</script>	
</head>

<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
%>

<body onload="setFocus()">



<div class="header-bar" >
  <div class="left"><h1>GNomEx</h1></div>
  <div class="rightMenu" >
      <a href="login.jsp">Login</a> |    
      <a href="change_password.jsp">Change password</a> |       
      <a href="register_user.jsp">Create a new account</a> 
  </div>
</div>

<div id="content" align="center" bgcolor="white">

    <form id="theform" method="POST" action="ChangePassword.gx" >

  <div class="box">
    <h3>Reset Password</h3>

      <div class="col1"><div class="right">User name</div></div>
      <div class="col2"><div class="right"><input id="username" type="text" class="text"  value="${param.userName}" name="userName"></div></div>


      <div class="buttonPanel"><input type="submit" class="submit" value="Submit" /></div>
  </div>

<div class="message"><strong><%= message %></strong></div>
    <input type="hidden" name="responsePageSuccess" value="/reset_password_success.jsp"/>
    <input type="hidden" name="responsePageError" value="/reset_password.jsp"/>
  </form>

</div>

</div>

</body>
</html>