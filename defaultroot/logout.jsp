<%
//Set Cache-Control to no-cache.
response.setHeader("Cache-Control", "max-age=0, must-revalidate");

session.removeAttribute("User");
session.removeAttribute("username");
session.invalidate();
%>
<DATA>
<LOGOUT/>
</DATA>