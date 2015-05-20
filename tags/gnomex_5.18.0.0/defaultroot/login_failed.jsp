<html>

<head>
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Sign in to GNomEx</title>
	
	<script type="text/javascript">
		function setFocus()
		{
     		theform.username.focus();
		}
	</script>
</head>

<body onload="setFocus()">




<div id="content" align="center" bgcolor="white">

<div class="header-bar" >
    <div class="rightMenu" >
      <a href="change_password.jsp">Change password</a> |    
      <a href="reset_password.jsp">Reset password</a>  |    
      <a href="select_core.jsp">Sign up for an account</a> 
  </div>
</div>

    <form id="theform" method="POST"  >

  <div class="box">
    <h3>Sign In</h3>

      <div class="col1"><div class="right">User name</div></div>
      <div class="col2"><input id="username" type="text" class="text"  ></div>

   
      <div class="col1"><div class="right">Password</div></div>
      <div class="col2"><input type="password" class="text" name="j_password"></div>



      <div class="leftButton"><a href="gnomexGuestFlex.jsp" class="login">Sign in as guest</a></div>
      <div class="buttonPanel"><input type="submit" class="submit" value="Sign in" /></div>


  </div>
  <div class="message"> <strong>User name or password you entered is incorrect.  Please try again.</strong></div>

</div>

    </form>

</body>
</html>


