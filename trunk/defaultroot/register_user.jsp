<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.Property" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Create a new GNomEx Account</title>
	
<script  type="text/javascript" language="JavaScript">
  function setFocus()
  {
    theform.firstName.focus();
  }
  
  function showHideExternal()
  {

    if (document.theform.q1[0].checked)
    {
      document.getElementById("univUserNameArea1").style.display = "block";
      document.getElementById("univUserNameArea2").style.display = "block";
      document.getElementById("externalUserNameArea1").style.display  = "none";
      document.getElementById("externalUserNameArea2").style.display  = "none";
      document.getElementById("externalPasswordArea1").style.display = "none";
      document.getElementById("externalPasswordArea2").style.display = "none";
    }
    else
    {
      document.getElementById("univUserNameArea1").style.display = "none";
      document.getElementById("univUserNameArea2").style.display = "none";
      document.getElementById("externalUserNameArea1").style.display  = "block";
      document.getElementById("externalUserNameArea2").style.display  = "block";
      document.getElementById("externalPasswordArea1").style.display = "block";
      document.getElementById("externalPasswordArea2").style.display = "block";
    }
  }
</script>

	
</head>

<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

boolean showUserNameChoice = false;
String externalUserDisplay = "display:block;";
Session sess = null;
try {
  sess = HibernateGuestSession.currentGuestSession("guest");
  Property propUniversityUserAuth = (Property)sess.createQuery("from Property p where p.propertyName='" + Property.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
  if (propUniversityUserAuth != null && propUniversityUserAuth.getPropertyValue() != null && propUniversityUserAuth.getPropertyValue().equals("Y")) {
    showUserNameChoice = true;
    externalUserDisplay = "display:none;";
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
      <a href="reset_password.jsp">Reset password</a>   
  </div>
</div>

<div id="content" align="center" bgcolor="white">

    <form name="theform" method="POST" action="PublicSaveSelfRegisteredAppUser.gx" >

  <div class="boxWide">
    <h3>Create a new Account</h3>

      <div class="col1"><div class="right">First name</div></div>
      <div class="col2"><input id="firstName" type="text" class="textWide" value="${param.firstName}"  name="firstName"></div>

      <div class="col1"><div class="right">Last name</div></div>
      <div class="col2"><input type="text" class="textWide" value="${param.lastName}" name="lastName"></div>

      <div class="col1"><div class="right">Email</div></div>
      <div class="col2"><input type="text" class="textWide"  value="${param.email}" name="email"></div>

      <div class="col1"><div class="right">Phone</div></div>
      <div class="col2"><input type="text" class="textWide" value="${param.phone}" name="phone"></div>

      <div class="col1"><div class="right">Lab</div></div>
      <div class="col2"><input type="text" class="textWide" value="${param.lab}" name="lab"></div>

      <div class="col1"><div class="right">Institute</div></div>
      <div class="col2"><input type="text" class="textWide" value="${param.institute}" name="institute"></div>

      <div class="col1"><div class="right">Department</div></div>
      <div class="col2"><input type="text" class="textWide" value="${param.department}" name="department"></div>

<% if (showUserNameChoice) { %>
    <div class="left">
    <br>
     Do you have a University user id?
     <INPUT TYPE="radio" NAME="q1" VALUE="y"  ${param.q1} == 'y' ? 'checked' : '' onClick="showHideExternal();">Yes
     <INPUT TYPE="radio" NAME="q1" VALUE="n"  ${param.q1} == 'n' ? 'checked' : '' onClick="showHideExternal();">No
    </div>    
<% }  %>
    

      <div id="univUserNameArea1" style="display:none;" class="col1"><div class="right">University ID</div></div>
      <div id="univUserNameArea2" style="display:none;" class="col2"><input type="text" class="text" value="${param.uNID}" name="uNID"></div>

      <div id="externalUserNameArea1" style="<%= externalUserDisplay%>" class="col1"><div class="right">User name</div></div>
      <div id="externalUserNameArea2" style="<%= externalUserDisplay%>" class="col2"><input type="text" class="text" value="${param.userNameExternal}" name="userNameExternal"></div>

    
      <div id="externalPasswordArea1" style="<%= externalUserDisplay%>" class="col1"><div class="right">Password</div></div>
      <div id="externalPasswordArea2" style="<%= externalUserDisplay%>" class="col2"><input type="password" class="text" name="passwordExternal"></div>
    
      <div class="bottomPanel">   
          <div class="buttonPanel"><input type="submit" class="submit" value="Submit" /></div>
      </div>

  </div>
 <div class="message"> <strong><%= message %></strong></div>

</div>
    <input type="hidden" name="responsePageSuccess" value="/register_user_success.jsp"/>
    <input type="hidden" name="responsePageError" value="/register_user.jsp"/>
    </form>

</body>
</html>