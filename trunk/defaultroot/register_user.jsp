<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
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
      document.getElementById("userNameExternal").value = "";
      document.getElementById("passwordExternal").value = "";
    }
    else
    {
      document.getElementById("univUserNameArea1").style.display = "none";
      document.getElementById("univUserNameArea2").style.display = "none";
      document.getElementById("uNID").value = "";
      document.getElementById("externalUserNameArea1").style.display  = "block";
      document.getElementById("externalUserNameArea2").style.display  = "block";
      document.getElementById("externalPasswordArea1").style.display = "block";
      document.getElementById("externalPasswordArea2").style.display = "block";
    }
  }
  
  function checkAlphaNumeric(e)
  {
     var KeyID = e.keyCode;
     if(KeyID<32||(KeyID>=33 && KeyID<=47 )||(KeyID>=58 && KeyID<=64 )||(KeyID>=91 && KeyID<=96)||( KeyID>122))
        return false;
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
  PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
  if (propUniversityUserAuth != null && propUniversityUserAuth.getPropertyValue() != null && propUniversityUserAuth.getPropertyValue().equals("Y")) {
    showUserNameChoice = true;
    externalUserDisplay = "display:none;";
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

<body onload="setFocus()">




<div id="content" align="center" bgcolor="white">

    <div class="header-bar" >
      <div class="rightMenu" >
          <a href="gnomexFlex.jsp">Login</a> | 
          <a href="change_password.jsp">Change password</a> |    
          <a href="reset_password.jsp">Reset password</a> |    
          <a href="register_user.jsp">Create a new account</a> 
      </div>
    </div>

    <form name="theform" method="POST" action="PublicSaveSelfRegisteredAppUser.gx" >

  <div class="boxWide">
    <h3>Create a new Account</h3>

      <div class="col1"><div class="right">First name</div></div>
      <div class="col2"><input id="firstName" type="text" class="textWide" name="firstName"   ></div>

      <div class="col1"><div class="right">Last name</div></div>
      <div class="col2"><input type="text" class="textWide" name="lastName"  /></div>

      <div class="col1"><div class="right">Email</div></div>
      <div class="col2"><input type="text" class="textWide"   name="email"  /></div>

      <div class="col1"><div class="right">Phone</div></div>
      <div class="col2"><input type="text" class="textWide" name="phone"  /></div>

      <div class="col1"><div class="right">Lab Group</div></div>
      <div class="col2"><input type="text" class="textWide"  name="lab" onkeypress="return checkAlphaNumeric(event)"  /></div>

      <div class="col1"><div class="right">Institute</div></div>
      <div class="col2"><input type="text" class="textWide"  name="institute" /></div>

      <div class="col1"><div class="right">Department</div></div>
      <div class="col2"><input type="text" class="textWide"  name="department" /></div>

<% if (showUserNameChoice) { %>
    <div class="left">
    <br>
     Do you have a University user id?
     <INPUT TYPE="radio" NAME="q1" VALUE="y"  ${param.q1} == 'y' ? 'checked' : '' onClick="showHideExternal();">Yes
     <INPUT TYPE="radio" NAME="q1" VALUE="n"  ${param.q1} == 'n' ? 'checked' : '' onClick="showHideExternal();">No
    </div>    
<% }  %>
    

      <div id="univUserNameArea1" style="display:none;" class="col1"><div class="right">University ID</div></div>
      <div id="univUserNameArea2" style="display:none;" class="col2"><input type="text" class="text" name="uNID"  ></div>

      <div id="externalUserNameArea1" style="<%= externalUserDisplay%>" class="col1"><div class="right">User name</div></div>
      <div id="externalUserNameArea2" style="<%= externalUserDisplay%>" class="col2"><input type="text" class="text" name="userNameExternal"  ></div>

    
      <div id="externalPasswordArea1" style="<%= externalUserDisplay%>" class="col1"><div class="right">Password</div></div>
      <div id="externalPasswordArea2" style="<%= externalUserDisplay%>" class="col2"><input type="password" name="passwordExternal" class="text" ></div>
    
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