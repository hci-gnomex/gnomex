<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
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


<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

boolean showCampusInfoLink = false;
Session sess = null;
try {
  sess = HibernateGuestSession.currentGuestSession("guest");
  PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
  if (propUniversityUserAuth != null && propUniversityUserAuth.getPropertyValue() != null && propUniversityUserAuth.getPropertyValue().equals("Y")) {
    showCampusInfoLink = true;
  }  
} catch (Exception e){
  message = "Cannot obtain property " + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + " " + e.toString() + " sess=" + sess;
} finally {
  try {
	  HibernateGuestSession.closeGuestSession();
  } catch (Exception e) {
  }  
}

%>


<div id="content" align="center" bgcolor="white">

   <div class="header-bar" >
       <div class="rightMenu" >
         <a href="change_password.jsp">Change password</a> |    
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

<% if (showCampusInfoLink) { %>
<div class="bottomPanel">
If you have an University ID (u00000000), use it and the password from University Campus Information Systems to login.
</div>
<% }  %>



</div>

    </form>

</body>
</html>