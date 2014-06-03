<%
//Set Cache-Control to no-cache.
response.setHeader("Cache-Control", "max-age=0, must-revalidate");

session.removeAttribute("j_username");
session.removeAttribute("j_password");
session.removeAttribute("User");
session.removeAttribute("user");
session.removeAttribute("username");
session.removeAttribute("gnomexSecurityAdvisor");
session.removeAttribute("logined"); 
session.removeAttribute("context"); 

session.invalidate();
%>
<DATA>
<LOGOUT/>
</DATA>