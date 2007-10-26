<%@ include file="requireSecureRemote.jsp" %>
<BODY>
<HTML>
<HEAD>
<TITLE>GNomEx - Change Password</TITLE>

<%
String parm1 = request.getParameter("parm1");
String parm2 = request.getParameter("parm2");
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"");
%>


<h3>Change your GNomEx Password</h3>


<table>
  
  <tr>
   <td valign="top">
      <form action="/gnomex/ChangePasswordExternalUser.gx" method="post">
         <table>
           <tr>
             <td>User name:</td> <td><input type="text" name="userName"></td>
           </tr>
           <tr>
             <td>Enter your old password:</td> <td><input type="password" name="password"></td>
           </tr>
           <tr>
             <td>&nbsp;</td>
           </tr>
           <tr>
             <td>Enter your new password:</td> <td><input type="password" name="passwordNew1"></td>
           </tr>
           <tr>
             <td>Enter your new password again:</td> <td><input type="password" name="passwordNew2"></td>
           </tr>
         </table>
         <br>
         <br>
         <INPUT type="submit" name="submit" value="Change password">
         <br>
         <br>
         <%= message %>
      </form>
   </td>
  </tr>
</table>

</BODY>

</HTML>