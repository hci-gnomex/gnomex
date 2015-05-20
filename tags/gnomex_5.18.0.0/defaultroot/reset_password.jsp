<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="hci.gnomex.utility.JspHelper" %>
<%@ page import="hci.gnomex.utility.PropertyDictionaryHelper" %>
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
Integer coreToPassThru = JspHelper.getIdCoreFacility(request);
String idCoreParm = coreToPassThru == null?"":("?idCore=" + coreToPassThru.toString());

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

boolean showCampusInfoLink = false;
String siteLogo = "";
Session sess = null;
try {
  sess = HibernateGuestSession.currentGuestSession("guest");
  PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
  if (propUniversityUserAuth != null && propUniversityUserAuth.getPropertyValue() != null && propUniversityUserAuth.getPropertyValue().equals("Y")) {
    showCampusInfoLink = true;
  }  
  
  // Get site specific log
  siteLogo = PropertyDictionaryHelper.getSiteLogo(sess, coreToPassThru);
  
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
        <a href="gnomexFlex.jsp<%=idCoreParm%>">Sign in</a> |    
        <a href="change_password.jsp<%=idCoreParm%>">Change password</a> |    
        <a href="select_core.jsp<%=idCoreParm%>">Sign up for an account</a> 
    </div>
  </div>

  <form id="theform" method="POST" action="ChangePassword.gx" >

  <div class="box">
    <h3>Reset Password</h3>

      <div class="col1"><div class="right">User name</div></div>
      <div class="col2"><input id="username" name="userName" type="text" class="text"/></div>



      <div class="buttonPanel"><input type="submit" class="submit" value="Submit" /></div>
      
<% if (showCampusInfoLink) { %>
<div class="bottomPanel">

If you have registered using your uNID (u00000000), your password is tied to the University Campus Information System. Please use the <a href='https://gate.acs.utah.edu/' class="other" target='_blank'>Campus Information System</a> to change or reset your password.
</div>

<% }  %>
      
  </div>

<div class="message"><strong><%= message %></strong></div>
    <input type="hidden" name="responsePageSuccess" value="/reset_password_success.jsp<%=idCoreParm%>"/>
    <input type="hidden" name="responsePageError" value="/reset_password.jsp<%=idCoreParm%>"/>
    <input type="hidden" name="idCoreParm" value="<%=idCoreParm%>"/>
  </form>

</div>

</div>

</body>
</html>