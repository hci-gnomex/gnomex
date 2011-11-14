<html>

<head>
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Login to GNomEx</title>
	
	<script type="text/javascript">
		function setFocus()
		{
     		theform.username.focus();
		}
	</script>
</head>

<body onload="setFocus()">


<div class="header-bar" >
  <div class="left"><h1>GNomEx</h1></div>
  <div class="rightMenu" >
      <a href="change_password.jsp">Change password</a> |    
      <a href="reset_password.jsp">Reset password</a> |    
      <a href="register_user.jsp">Create a new account</a> 
  </div>
</div>

<div id="content" align="center" bgcolor="white">

    <form id="theform" method="POST" action="j_security_check" >

  <div class="box">
    <h3>Sign In</h3>

      <div class="col1"><div class="right">User name</div></div>
      <div class="col2"><input id="username" type="text" class="text" value="${param.j_username}"  name="j_username"></div>

   
      <div class="col1"><div class="right">Password</div></div>
      <div class="col2"><input type="password" class="text" name="j_password"></div>



      <div class="leftButton"><a href="gnomexGuestFlex.jsp" class="login">Login as guest</a></div>
      <div class="buttonPanel"><input type="submit" class="submit" value="Login" /></div>


  </div>

</div>

    </form>

</body>
</html>