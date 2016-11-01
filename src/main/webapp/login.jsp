<%@ page import="hci.gnomex.utility.HibernateSession" %>
<%@ page import="org.hibernate.Session" %>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.controller.GNomExFrontController" %>
<%@ page import="hci.gnomex.controller.GetRequest" %>
<%@ page import="hci.gnomex.model.Request" %>
<%@ page import="hci.gnomex.controller.GetAnalysis" %>
<%@ page import="hci.gnomex.model.Analysis" %>
<%@ page import="hci.gnomex.controller.GetDataTrack" %>
<%@ page import="hci.gnomex.model.DataTrack" %>
<%@ page import="hci.gnomex.model.Topic" %>
<%@ page import="hci.gnomex.utility.TopicQuery" %>
<%@ page import="hci.gnomex.utility.JspHelper" %>
<%@ page import="java.util.List" %>
<%@ page import="hci.gnomex.model.Visibility" %>
<%@ page import="hci.gnomex.utility.PropertyDictionaryHelper" %>
<%@ page import="org.apache.log4j.Logger" %>;

<html>



<body onload="setFocus()">


<%
    Logger LOG = Logger.getLogger("login.jsp");
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
String errFlag = (String)((request.getParameter("err") != null)?request.getParameter("err"):"N");
Integer idCoreFacility = JspHelper.getIdCoreFacility(request);
String idCoreParm = idCoreFacility == null?"":("?idCore=" + idCoreFacility.toString());

// We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
String webContextPath = getServletConfig().getServletContext().getRealPath("/");
GNomExFrontController.setWebContextPath(webContextPath);

boolean showCampusInfoLink = false;
boolean itemNotPublic = false;
boolean showUserSignup = true;
boolean allowGuest = false;
String itemType="";
String siteLogo = "";
Session sess = null;
try {
  sess = HibernateSession.currentReadOnlySession("guest");
  PropertyDictionary propUniversityUserAuth = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + "'").uniqueResult();
  if (propUniversityUserAuth != null && propUniversityUserAuth.getPropertyValue() != null && propUniversityUserAuth.getPropertyValue().equals("Y")) {
    showCampusInfoLink = true;
  }  

  // Get site specific log
  siteLogo = PropertyDictionaryHelper.getSiteLogo(sess, idCoreFacility);
  
  // Determine if user sign up screen is enabled
  PropertyDictionary disableUserSignup = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.DISABLE_USER_SIGNUP + "'").uniqueResult();
  if (disableUserSignup != null && disableUserSignup.getPropertyValue().equals("Y")) {
    showUserSignup = false;
  } 
 
   // Determine if guest access is allowed
  PropertyDictionary noGuestAccess = (PropertyDictionary)sess.createQuery("from PropertyDictionary p where p.propertyName='" + PropertyDictionary.NO_GUEST_ACCESS + "'").uniqueResult();
  if (noGuestAccess == null) {
    allowGuest = true;
  } 

  // If launching experiment, analysis, data track, or topic then check for public
  // If public then launch directly as guest user without requiring login
  String requestNumber = (String) ((request.getParameter("requestNumber") != null)?request.getParameter("requestNumber"):"");
  if(requestNumber.length() > 0) {
    Request experiment = GetRequest.getRequestFromRequestNumber(sess, requestNumber);
    if(experiment != null && experiment.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
        // If public experiment then skip login screen and launch directly as guest user
%>
  <script type="text/javascript">
    window.location = "gnomexGuestFlex.jsp?requestNumber=<%=requestNumber%>";
  </script>
<% 
    } else {
      itemNotPublic = true;
      itemType = "Experiment";
    }
  } else {
    String analysisNumber = (String) ((request.getParameter("analysisNumber") != null)?request.getParameter("analysisNumber"):"");
    if(analysisNumber.length() > 0) {
      Analysis analysis = GetAnalysis.getAnalysisFromAnalysisNumber(sess, analysisNumber);
      if(analysis != null && analysis.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
        // If public analysis then skip login screen and launch directly as guest user
%>
      <script type="text/javascript">
      window.location = "gnomexGuestFlex.jsp?analysisNumber=<%=analysisNumber%>";
      </script>
<% 
      }  else {
        itemNotPublic = true;
        itemType = "Analysis";
      }           
    } else {
      String dataTrackNumber = (String) ((request.getParameter("dataTrackNumber") != null)?request.getParameter("dataTrackNumber"):"");
      if(dataTrackNumber.length() > 0) {
        DataTrack dt = GetDataTrack.getDataTrackFromDataTrackNumber(sess, dataTrackNumber);
        if(dt != null && dt.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          // If public data track then skip login screen and launch directly as guest user
%>
        <script type="text/javascript">
        window.location = "gnomexGuestFlex.jsp?dataTrackNumber=<%=dataTrackNumber%>";
        </script>
<% 
        } else {
          itemNotPublic = true;
          itemType = "Data Track";
        }      
      } else {
        String topicNumber = (String) ((request.getParameter("topicNumber") != null)?request.getParameter("topicNumber"):"");
        if(topicNumber.length() > 0) {
          Topic t = TopicQuery.getTopicFromTopicNumber(sess, topicNumber);
          if(t != null && t.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
            // If public topic then skip login screen and launch directly as guest user
%>
          <script type="text/javascript">
          window.location = "gnomexGuestFlex.jsp?topicNumber=<%=topicNumber%>";
          </script>
<% 
          } else {
            itemNotPublic = true;
            itemType = "Topic";
          }  
        }          
      }        
    }      
  }  
 } catch (Exception e){
              LOG.error("Error in login.jsp", e);
  message = "Cannot obtain property " + PropertyDictionary.UNIVERSITY_USER_AUTHENTICATION + " " + e.toString() + " sess=" + sess;
} finally {
  try {
    HibernateSession.closeSession();
  } catch (Exception e) {
      LOG.error("Error in login.jsp", e);
  }  
}

%>

<head>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">

  <link rel="stylesheet" href="css/login.css" type="text/css" />

<style type="text/css"> 

.header-bar {
  float: top;
  width: 800px;
  height: 50px;
  padding-bottom: 10px;
  margin-bottom: 10px;
  background: transparent url(<%=siteLogo%> no-repeat;}

</style> 

  <title>Sign in to GNomEx</title>
  
  <script type="text/javascript">
    function setFocus()
    {
        theform.username.focus();
    }
  </script>
</head>




<div id="content" align="center" bgcolor="white">

   <div class="header-bar" >
       <div class="leftMenu">
            <img src="<%=siteLogo%>"/>
       </div>
    </div>
    <form id="theform" method="POST" action="j_security_check<%=idCoreParm%>"  >

  <div class="box">
  
<% if (itemNotPublic) { %>
<div class="topPanel">
The <%= itemType %> you are linking to does not have public visibility. Please sign in to proceed:
</div>
<% }  %>  
  
  
  
    <h3>Sign In</h3>

      <div class="col1"><div class="right">User name</div></div>
      <div class="col2"><input id="username" type="text" class="text" name="j_username"  ></div>

   
      <div class="col1"><div class="right">Password</div></div>
      <div class="col2"><input type="password" class="text" name="j_password"></div>
 
      <br>
      
       <%
        if( showCampusInfoLink ) {
      %>
        <div class="boxCenter"><note class="centered"><i>University of Utah investigators should 
          sign in with their UNID and CIS password.</i></note></div>      
        <%
        }
      %>
      
      <div class="buttonPanelShort"><input type="submit" class="submit" value="Sign in" /></div>
      
      <%
       if ( allowGuest ) {
       %>
      <div class="bottomPanel">
<!--         <div class="col1Wide"><note class="inline"><i>For guest access to public data</i></note></div> -->
        <!-- Note that guest ignores idCore parameter -- guest just sees all public objects. -->
        <div class="buttonPanelShort"><a href="gnomexGuestFlex.jsp" class="button">Guest Login</a></div>
        <div class="buttonPanelShort"><a href="reset_password.jsp<%=idCoreParm%>" class="button">Reset Password</a></div>
        <%if(showUserSignup) {%>
           <div class="buttonPanelShort"><a href="select_core.jsp<%=idCoreParm%>" class="button">New Account</a></div>
        <%}%> 
      </div>
      <%
      } else{
      %>
      <div class="bottomPanel" >          
          <div class="buttonPanelShort"><a href="reset_password.jsp<%=idCoreParm%>">Reset password</a></div>
        <%if(showUserSignup) {%>
           <div class="buttonPanelShort"><a href="select_core.jsp<%=idCoreParm%>">New Account</a></div>
        <%}%> 
      </div>
      <%} %>

    </div>
       
    </form>
<% if (errFlag.equals("Y")) { %>
  <div id="error" class="message"> <strong>User name or password you entered is incorrect.  Please try again.</strong></div>
<% } %>
</body>
</html>