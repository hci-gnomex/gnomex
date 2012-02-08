<html>

<head>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">

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




<div id="content" align="center" bgcolor="white">

   <div class="header-bar" >
       <div class="rightMenu" >
         <a href="change_password.jsp">Change password</a> |    
         <a href="reset_password.jsp">Reset password</a> |    
         <a href="register_user.jsp">Create a new account</a> 
      </div>
    </div>
    <form id="theform" method="POST"  >

  <div class="box">
    <h3>Log In</h3>

      <div class="col1"><div class="right">User name</div></div>
      <div class="col2"><input id="username" type="text" class="text" name="j_username"  ></div>

   
      <div class="col1"><div class="right">Password</div></div>
      <div class="col2"><input type="password" class="text" name="j_password"></div>



      <div class="leftButton"><a href="gnomexGuestFlex.jsp" class="login">Login as guest</a></div>
      <div class="buttonPanel"><input type="submit" class="submit" value="Login" /></div>


  </div>

</div>

    </form>

</body>
</html>