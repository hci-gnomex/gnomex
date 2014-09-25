<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.CoreFacility" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="hci.gnomex.utility.JspHelper" %>
<%@ page import="hci.gnomex.utility.PropertyDictionaryHelper" %>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="css/login.css" type="text/css" />
	<title>Create an account - select core facility </title>
		
</head>


<%

Integer idCoreFacility = JspHelper.getIdCoreFacility(request);
String idCoreParm = idCoreFacility == null?"":("?idCore=" + idCoreFacility.toString());
String href = "";
CoreFacility facility = null;

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
  siteLogo = PropertyDictionaryHelper.getSiteLogo(sess, idCoreFacility);
 
  facilities = CoreFacility.getActiveCoreFacilities(sess);
  if ( facilities.size() == 1 ){
    facility = (CoreFacility) facilities.get(0);
  } else if (idCoreFacility != null) {
    for (CoreFacility f : (List<CoreFacility>)facilities) {
      if (f.getIdCoreFacility().equals(idCoreFacility)) {
        facility = f;
        break;
      }
    }
  }
  
  if (facility != null) {
    int idFacility = facility.getIdCoreFacility();
    %>
    <script>
      href = "register_user.jsp?idFacility=<%=idFacility%><%=idCoreParm.replace("?","&")%>";
      window.location.href = href;
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
        <a href="gnomexFlex.jsp<%=idCoreParm%>">Sign in</a> |         
        <a href="reset_password.jsp<%=idCoreParm%>">Reset password</a>
    </div>
  </div>

  
  <div class="boxSuperWide">
    <h3>Select Core Facility</h3>
    
    <div id="coreFacilityDiv"><div class="col1"><div class="left">
      <table border="0" width="800" class="facilities" rules="rows">
        
        <%
          Iterator facilityIter = facilities.iterator();
          while (facilityIter.hasNext()) {
            CoreFacility f = (CoreFacility) facilityIter.next();
        %>
        <tr height="35">
          <td width="250" style="padding-bottom:5; padding-top:5">
            <a class="button" href="register_user.jsp?idFacility=<%=f.getIdCoreFacility()%><%=idCoreParm.replace("?","&")%>"><%=f.getDisplay()%></a>
          </td>
          
          <td style="padding-bottom:5; padding-top:5">
            <%
              if (f.getDescription() != null) {%>
                  <%=f.getDescription()%>
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