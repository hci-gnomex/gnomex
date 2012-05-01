<%@ page import="hci.gnomex.utility.HibernateGuestSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.CoreFacility" %>
<%@ page import="hci.gnomex.model.Lab" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
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

    if (document.theform.uofuAffiliate[0].checked)
    {
      document.getElementById("UofUDiv").style.display = "block";
      document.getElementById("externalDiv").style.display = "none";
    }
    else
    {
      document.getElementById("UofUDiv").style.display = "none";
      document.getElementById("externalDiv").style.display = "block";
    }
  }
  
  function showHideLab()
  {
    if (document.theform.existingLab[0].checked)
    {
      document.getElementById("existingLabDiv").style.display = "block";
      document.getElementById("newLabDiv").style.display = "none";      
    }
    else
    {
      document.getElementById("existingLabDiv").style.display = "none";
      document.getElementById("newLabDiv").style.display = "block";      
    }
  }
  
  function checkAlphaNumeric(e)
  {
     var KeyID = e.keyCode;
     if(KeyID<32||(KeyID>=33 && KeyID<=47 )||(KeyID>=58 && KeyID<=64 )||(KeyID>=91 && KeyID<=96)||( KeyID>122))
        return false;
  }

<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
if (message == null) {
  message = "";
}

List labs = null;
List facilities = null;

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
  
  labs = sess.createQuery("from Lab l where l.isActive = 'Y' order by l.lastName, l.firstName").list();
  facilities = CoreFacility.getActiveCoreFacilities(sess);
  
} catch (Exception e){
  message = "Cannot obtain property " + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + " " + e.toString() + " sess=" + sess;
} finally {
  try {
    HibernateGuestSession.closeGuestSession();
  } catch (Exception e) {
  }  
}

%>

</script>
</head>

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
      
      <div style="width:100%;"><div style="float:left;">
      <br>
       Lab Group?

       <INPUT TYPE="radio" NAME="existingLab" VALUE="y" onClick="showHideLab();">Use existing
       <INPUT TYPE="radio" NAME="existingLab" VALUE="n" onClick="showHideLab();">Request new
      </div></div>

      <div id="existingLabDiv" style="display:none;">
        <div class="col1"><div class="right">Choose Lab</div></div>
        <div class="col2"><select name="labDropdown" id="labDropdown" class="textWide">
          <%
          Iterator i = labs.iterator();
          while (i.hasNext()) {
            Lab l = (Lab) i.next();
          %>
            <option value="<%=l.getIdLab()%>"><%=l.getName()%></option>
          <%}%>
        </select></div>
      </div>
      <div id="newLabDiv" style="display:none;">
        <div style="width:100%;text-align:left" id="coreFacilities">
          <div style="width:100%;clear:both;"><br/></div>
          Choose Core Facility<br>
          <%
          Iterator facilityIter = facilities.iterator();
          while (facilityIter.hasNext()) {
            CoreFacility facility = (CoreFacility) facilityIter.next();
          %>
          <input type="radio" name="facilityRadio" id="facilityRadio" value="<%=facility.getIdCoreFacility()%>"/> <%=facility.getFacilityName()%> 
          <br>
          <%}%>
        </div>
        <div class="col1"><div class="right">Enter Lab</div></div>
        <div class="col2"><input type="text" class="textWide"  name="newLab" onkeypress="return checkAlphaNumeric(event)"/></div>

        <div class="col1"><div class="right">Department</div></div>
        <div class="col2"><input type="text" class="textWide"  name="department" /></div>
      </div>

<% if (showUserNameChoice) { %>
    <div style="width:100%;"><div style="float:left;">
    <br>
     Are you affiliated with the University of Utah?
     <INPUT TYPE="radio" NAME="uofuAffiliate" VALUE="y" onClick="showHideExternal();">Yes
     <INPUT TYPE="radio" NAME="uofuAffiliate" VALUE="n" onClick="showHideExternal();">No
    </div></div>
<% }  %>
    
      <div id="UofUDiv" style="display:none;width:100%;">
        <div id="univUserNameArea1" class="col1"><div class="right">University ID</div></div>
        <div id="univUserNameArea2" class="col2"><input type="text" class="textWide" name="uNID"  ></div>
        <div>Format should be a "u" followed by 7 digits (u0000000)</div>
      </div>

      <div id="externalDiv" style="display:none">
        <div class="col1"><div class="right">Institute</div></div>
        <div class="col2"><input type="text" class="textWide"  name="institute" /></div>
        
        <div id="externalUserNameArea1" class="col1"><div class="right">User name</div></div>
        <div id="externalUserNameArea2" class="col2"><input type="text" class="textWide" name="userNameExternal"  ></div>

    
        <div id="externalPasswordArea1" class="col1"><div class="right">Password</div></div>
        <div id="externalPasswordArea2" class="col2"><input type="password" name="passwordExternal" class="textWide"></div>
      </div>

      <div style="float:left;"><div class="message"> <strong><%= message %></strong></div></div>
      <div>   
          <div class="buttonPanel"><input type="submit" class="submit" value="Submit" /></div>
      </div>

  </div>

</div>
    <input type="hidden" name="responsePageSuccess" value="/register_user_success.jsp"/>
    <input type="hidden" name="responsePageError" value="/register_user.jsp"/>

<script  type="text/javascript" language="JavaScript">
<%
if (facilities != null && facilities.size() > 1) {
%>
  document.getElementById("coreFacilities").style.display = "block";
<%
} else {
%>
  document.getElementById("coreFacilities").style.display = "none";
<%
}
%>
</script>
    </form>


</body>
</html>