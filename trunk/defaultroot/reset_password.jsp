<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.Property" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
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

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

boolean showCampusInfoLink = false;
Session sess = null;
try {
  sess = HibernateGuestSession.currentGuestSession("guest");
  Property propUniversityUserAuth = (Property)sess.createQuery("from Property p where p.propertyName='" + Property.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
  if (propUniversityUserAuth != null && propUniversityUserAuth.getPropertyValue() != null && propUniversityUserAuth.getPropertyValue().equals("Y")) {
    showCampusInfoLink = true;
  }  
} catch (Exception e){
  message = "Cannot obtain property " + Property.UNIVERSITY_USER_AUTHENTICATION + " " + e.toString() + " sess=" + sess;
} finally {
  try {
	  HibernateGuestSession.closeGuestSession();
  } catch (Exception e) {
  }  
}

%>


<body onload="setFocus()">



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

    <form id="theform" method="POST" action="ChangePassword.gx" >

  <div class="boxMedium">
    <h3>Reset Password</h3>

      <div class="col1Wide"><div class="right">User name</div></div>
      <div class="col2"><div class="right"><input id="username" type="text" class="text"  value="${param.userName}" name="userName"></div></div>


      <div class="buttonPanel"><input type="submit" class="submit" value="Submit" /></div>
      
<% if (showCampusInfoLink) { %>
<div class="bottomPanel">

  <div class="leftInfo">If you have registered using your uNID (u00000000), your</div>
  <div class="left">password is tied to the University Campus Information </div>
  <div class="left">System. Please use the <a href='https://gate.acs.utah.edu/' class="other" target='_blank'>Campus Information System</a></div>
  <div class="left">to change or reset your password.</div>
</div>
<% }  %>
      
  </div>

<div class="message"><strong><%= message %></strong></div>
    <input type="hidden" name="responsePageSuccess" value="/reset_password_success.jsp"/>
    <input type="hidden" name="responsePageError" value="/reset_password.jsp"/>
  </form>

</div>

</div>

</body>
</html>