<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="hci.gnomex.utility.JspHelper" %>
<%@ page import="hci.gnomex.utility.JspHelper" %>
<%@ page import="hci.gnomex.utility.PropertyDictionaryHelper" %>
<%@ page import="hci.gnomex.model.AppUser" %>
<%@ page import="java.sql.Timestamp" %>;
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Change GNomEx Password</title>
	<script type="text/javascript">
		function setFocus()
		{
     		theform.username.focus();
		}
	</script>	
</head>

<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
Integer coreToPassThru = JspHelper.getIdCoreFacility(request);
String idCoreParm = coreToPassThru == null?"":("?idCore=" + coreToPassThru.toString());
boolean showUserSignup = true;

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

boolean showCampusInfoLink = false;
String siteLogo = "";
Session sess = null;
String guid = "";
try {
  sess = HibernateGuestSession.currentGuestSession("guest");
  PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
  if (propUniversityUserAuth != null && propUniversityUserAuth.getPropertyValue() != null && propUniversityUserAuth.getPropertyValue().equals("Y")) {
    showCampusInfoLink = true;
  }  
    
  // Determine if user sign up screen is enabled
  PropertyDictionary disableUserSignup = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.DISABLE_USER_SIGNUP + "'").uniqueResult();
  if (disableUserSignup != null && disableUserSignup.getPropertyValue().equals("Y")) {
    showUserSignup = false;
  } 
  
  // Get site specific log
siteLogo = PropertyDictionaryHelper.getSiteLogo(sess, coreToPassThru);
//Dummy d = new Dummy(sess);
//d.getAppUser(request);

//Check if user is able to change password
guid = (String) ((request.getParameter("guid") != null)?request.getParameter("guid"):"");
String loginPage = request.getScheme() + "://" + request.getServerName() + "/gnomex";

if(guid == null || guid.equals("")){
	response.sendRedirect(loginPage);
}

AppUser au = (AppUser) sess.createQuery("Select au from AppUser au where guid = '" + guid + "'").uniqueResult();

if(au == null){
	response.sendRedirect(loginPage);
}

Timestamp ts = new Timestamp(System.currentTimeMillis());

if(ts.after(au.getGuidExpiration())){
	response.sendRedirect(loginPage);
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
    <div class="leftMenu">
        <img src="<%=siteLogo%>"/>
    </div>
   <div class="rightMenu" >
      <a href="gnomexFlex.jsp<%=idCoreParm%>">Sign in</a>
      |   <a href="reset_password.jsp<%=idCoreParm%>">Reset password</a>
      <%if(showUserSignup) {%>
          |   <a href="select_core.jsp<%=idCoreParm%>">Sign up for an account</a>
      <%}%> 
  </div>
</div>


    <form id="theform" method="POST" action="ChangePassword.gx" >

  <div class="boxWide">
    <h3>Change Password</h3>

      <div class="col1Wide"><div class="right">User name</div></div>
      <div class="col2"><input id="username" name="userName" type="text" class="text"/></div>

      <div class="col1Wide"><div class="right">New Password</div></div>
      <div class="col2"><input type="password" name="newPassword" class="text" /></div>

      <div class="col1Wide"><div class="right">New Password (confirm)</div></div>
      <div class="col2"><input type="password"  name="newPasswordConfirm" class="text"/></div>


      <div class="buttonPanel"><input type="submit" class="submit" value="Submit" /></div>

<% if (showCampusInfoLink) { %>
<div class="bottomPanel">
If you have registered using your uNID (u00000000), your password is tied to the University Campus Information System. Please use the <a href='https://gate.acs.utah.edu/' class="other" target='_blank'>Campus Information System</a> to change or reset your password.
</div>
<% }  %>


  </div>

<div class="message"> <strong><%= message %></strong></div>

</div>
    <input type="hidden" name="responsePageSuccess" value="/change_password_success.jsp<%=idCoreParm%>"/>
    <input type="hidden" name="responsePageError" value="/change_password.jsp<%=idCoreParm%>"/>
    <input type="hidden" name="idCoreParm" value="<%=idCoreParm%>"/>
    <input type="hidden" name="guid" value="<%=guid%>"/>
    </form>


</body>
</html>