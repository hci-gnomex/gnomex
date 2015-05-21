<%
String message = (String) ((request.getAttribute("message") != null)?request.getAttribute("message"):"none");
%>

<DATA>
<ERROR message="<%= message %>"/>
<AL_ACTIONS>
<ACTMESSAGE CAPTION="GNomEx Error" TEXT="<%= message %>" TYPE="ERROR" />
</AL_ACTIONS>
</DATA>