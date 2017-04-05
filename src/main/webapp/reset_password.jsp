<%@ page import="hci.gnomex.utility.HibernateSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="hci.gnomex.utility.JspHelper" %>
<%@ page import="hci.gnomex.utility.PropertyDictionaryHelper" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="hci.gnomex.controller.ChangePassword" %>

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
		
		function showEmail(){
		
		  document.getElementById("emailDiv").style.display = "block";
      document.getElementById("emailDiv1").style.display = "block";
      
      document.getElementById("nameDiv").style.display = "none";
      document.getElementById("nameDiv1").style.display = "none";
      
      document.getElementById("forgotUserName").innerHTML = "Lookup by user name";
      document.getElementById("forgotUserName").onclick = hideEmail;
		  
		}
		
		function hideEmail(){
		  document.getElementById("emailDiv").style.display = "none";
		  document.getElementById("emailDiv1").style.display = "none";
		  
		  document.getElementById("nameDiv").style.display = "block";
      document.getElementById("nameDiv1").style.display = "block";
      
      document.getElementById("forgotUserName").innerHTML = "Lookup by email";
      document.getElementById("forgotUserName").onclick = showEmail;
      
    }
	</script>	
</head>


<%
    Logger LOG = Logger.getLogger("reset_password.jsp");
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
Integer coreToPassThru = JspHelper.getIdCoreFacility(request);
String idCoreParm = coreToPassThru == null?"":("?idCore=" + coreToPassThru.toString());
boolean showUserSignup = true;

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

boolean isUniversityUserAuthentication = false;
String siteLogo = "";
Session sess = null;
try {
  sess = HibernateSession.currentSession("guest");
  isUniversityUserAuthentication = PropertyDictionaryHelper.getInstance(sess).isUniversityUserAuthentication();
    
  // Determine if user sign up screen is enabled
  PropertyDictionary disableUserSignup = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.DISABLE_USER_SIGNUP + "'").uniqueResult();
  if (disableUserSignup != null && disableUserSignup.getPropertyValue().equals("Y")) {
    showUserSignup = false;
  } 
  
  // Get site specific log
  siteLogo = PropertyDictionaryHelper.getSiteLogo(sess, coreToPassThru);
  
} catch (Exception e){
    LOG.error("Error in reset_password.jsp", e);
  message = "Cannot obtain properties " + e.toString() + " sess=" + sess;
} finally {
  try {
	  HibernateSession.closeSession();
  } catch (Exception e) {
      LOG.error("Error in reset_password.jsp", e);
  }  
}

%>


<body onload="setFocus(); hideEmail()">





<div id="content" align="center" bgcolor="white">

  <div class="header-bar" >
    <div class="leftMenu">
        <img src="<%=siteLogo%>"/>
    </div>
    <div class="rightMenu" >
        <a href="gnomexFlex.jsp<%=idCoreParm%>">Sign in</a>      
        <%if(showUserSignup) {%>
            |   <a href="select_core.jsp<%=idCoreParm%>">Sign up for an account</a>
        <%}%> 
    </div>
  </div>

  <form id="theform" method="POST" action="ChangePassword.gx" >

  <div class="box">
    <h3>Reset Password</h3>

      <div class="col1" id="nameDiv" ><div class="right">User name</div></div>
      <div class="col2" id="nameDiv1"><input id="username" name="userName" type="text" class="text"/></div>
      
      <div class="col1" id="emailDiv"><div class="right">Email</div></div>
      <div class="col2" id="emailDiv1"><input id="email" name="email" type="text" class="text"/></div>



      <div class="buttonPanel"><input type="submit" class="submit" value="Submit" /></div>
      <a href="#" onclick="showEmail()" class="lookup" id="forgotUserName">Lookup by email</a>
      
<% if (isUniversityUserAuthentication) { %>
<div class="bottomPanel">

If you have registered using your uNID (u00000000), your password is tied to the University Campus Information System. Please use the <a href='https://gate.acs.utah.edu/' class="other" target='_blank'>Campus Information System</a> to change or reset your password.
</div>

<% }  %>
      
  </div>

<div class="message"><strong><%= message %></strong></div>
    <input type="hidden" name="responsePageSuccess" value="/reset_password_success.jsp<%=idCoreParm%>"/>
    <input type="hidden" name="responsePageError" value="/reset_password.jsp<%=idCoreParm%>"/>
    <input type="hidden" name="action" value="<%=ChangePassword.ACTION_REQUEST_PASSWORD_RESET%>"/>
    <input type="hidden" name="idCoreParm" value="<%=idCoreParm%>"/>
  </form>

</div>

</div>

</body>
</html>