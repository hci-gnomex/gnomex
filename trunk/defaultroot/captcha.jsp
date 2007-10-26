<%@ include file="requireSecureRemote.jsp" %>
<BODY>
<HTML>
<HEAD>
<TITLE>ExGen</TITLE>

<%
String parm1 = request.getParameter("parm1");
String parm2 = request.getParameter("parm2");
%>


<h3>Guest Login to GNomEx</h3>


<table>
  <tr>
	  <td><img src="Captcha.jpg">
   </td>
  </tr>
  <tr>
   <td valign="top">
      <form action="/gnomex/CreateSecurityAdvisorForGuest.gx" method="post">
         <table>
           <tr>
             <td>Please enter the text displayed in the image</td> <td><input type="text" name="captchafield"></td>
           </tr>
         </table>
         <br>
         <br>
         <INPUT type="submit" name="submit" value="Login to GNomEx">
         <input type="hidden" name="launchAction" value="">
         <input type="hidden" name="launchParm1" value="<%= parm1 %>">
         <input type="hidden" name="launchParm2" value="<%= parm2 %>">
         <input type="hidden" name="launchParm3" value="Y">
      </form>
   </td>
  </tr>
</table>

</BODY>

</HTML>