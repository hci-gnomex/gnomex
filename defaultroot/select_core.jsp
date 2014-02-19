<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.CoreFacility" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Create an account - select core facility </title>
		
</head>


<%

String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
List facilities = null;

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

String siteLogo = "";
Session sess = null;
try {
  sess = HibernateGuestSession.currentGuestSession("guest");
  PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
   
  
  // Get site specific log
  PropertyDictionary propSiteLogo = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.SITE_LOGO + "'").uniqueResult();
  if (propSiteLogo != null && !propSiteLogo.getPropertyValue().equals("")) {
    siteLogo = "./" + propSiteLogo.getPropertyValue();
  }  else {
    siteLogo = "./assets/gnomex_logo.png";
  } 
 
  facilities = CoreFacility.getActiveCoreFacilities(sess);
  if ( facilities.size() == 1 ){
    CoreFacility facility = (CoreFacility) facilities.get(0);
    int idFacility = facility.getIdCoreFacility();
    %>
    <script>
      window.location.href = "register_user.jsp?idFacility=" + <%=idFacility%>;
    </script>
    <%
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


<body>



<div id="content" align="center" bgcolor="white">

  <div class="header-bar" >
    <div class="leftMenu">
        <img src="<%=siteLogo%>"/>
    </div>
    <div class="rightMenu" >
        <a href="gnomexFlex.jsp">Sign in</a> |    
        <a href="change_password.jsp">Change password</a> |       
        <a href="reset_password.jsp">Reset password</a>
    </div>
  </div>

  
  <div class="boxSuperWide">
    <h3>Select Core Facility</h3>
    
    <div id="coreFacilityDiv"><div class="col1"><div class="left">
      <table border="0" width="800" class="facilities">
        
        <%
          Iterator facilityIter = facilities.iterator();
          while (facilityIter.hasNext()) {
            CoreFacility facility = (CoreFacility) facilityIter.next();
        %>
        <tr height="35">
          <td width="250">
            <a class="button" href="register_user.jsp?idFacility=<%=facility.getIdCoreFacility()%>"><%=facility.getDisplay()%></a>
          </td>
          
          <td >
            <%
              if (facility.getDescription() != null) {%>
                  <%=facility.getDescription()%>
            <%}%>
          </td>
        </tr> 
            <%}%>
      </table>
      
    </div></div></div>       
  
    <div class="empty"></div>
    
      
  </div>


</div>


</body>
</html>