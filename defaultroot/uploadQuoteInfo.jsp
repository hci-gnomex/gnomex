<%@ page import="hci.gnomex.utility.HibernateGuestSession"%>
<%@ page import="org.hibernate.Session"%>
<%@ page import="hci.gnomex.controller.GNomExFrontController"%>
<%@ page import="hci.gnomex.model.CoreFacility"%>
<%@ page import="hci.gnomex.model.PropertyDictionary" %>
<%@ page import="hci.gnomex.utility.PropertyDictionaryHelper" %>
<%@ page import="hci.gnomex.model.ProductOrder" %>
<HTML>
<HEAD>
<link rel="stylesheet" type="text/css" href="css/message.css" />
<TITLE>Enter Quote Info</TITLE>

<%
      String uuid = ( String ) ( ( request.getParameter( "orderUuid" ) != null ) ? request.getParameter( "orderUuid" ) : "" );

      String message = ( String ) ( ( request.getAttribute( "message" ) != null ) ? request.getAttribute( "message" )  : "" );
      
      ProductOrder po = null;

      String coreFacilityName = "";
      
      // We can't obtain a hibernate session unless webcontextpath is initialized.  See HibernateSession.
      String webContextPath = getServletConfig().getServletContext().getRealPath( "/" );
      GNomExFrontController.setWebContextPath( webContextPath );

      Session sess = null;

      try {
        sess = HibernateGuestSession.currentGuestSession( "guest" );
        
        po = ( ProductOrder ) sess.createQuery( "from ProductOrder po where po.uuid = '" + uuid + "'" ).uniqueResult();

        if( po == null ) {
          message = "Quote information has already been submitted for the experiement.";
        } else {
          CoreFacility cf = ( CoreFacility ) sess.createQuery( "from CoreFacility where idCoreFacility = " + po.getIdCoreFacility().toString() ).uniqueResult();
          coreFacilityName = cf.getFacilityName();
        }

      } catch( Exception e ) {
        message = "Error retrieving form for experiment.";
      } finally {
        try {
          HibernateGuestSession.closeGuestSession();
        } catch( Exception e ) {
        }
      }%>

</HEAD>
<BODY bgcolor="#FFFFF0">
  <img src="images/navbar.png" />
  <br>
  <%
    if( po != null ) {
  %>
    <h3>Enter quote information for <%=coreFacilityName%> Product Order <%=po.getIdProductOrder() != null ? po.getIdProductOrder() : ""%></h3>
    <form enctype="multipart/form-data" method="post" action="UploadQuoteInfoServlet.gx">
      <p>
        <input type="hidden" name="orderUuid" value="<%=uuid%>">
        <br> Quote #:&nbsp;<input type="text" name="quoteNumber" size="35">
      <p>
        File:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="file" name="file" size="35">
      <p>
      <p>
      <p>
        <input type="submit" name="Show" value="Submit">
    </form>
  <%
    }
  %>

  <p class="message"><%=message%></p>

</BODY>

</HTML>
