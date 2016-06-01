package hci.gnomex.controller;

import hci.framework.model.DetailObject;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ServletUtil;
import hci.gnomex.utility.HibernateSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.jdom.Element;

public class DownloadPlateSampleSheetFileServlet extends HttpServlet { 

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadPlateSampleSheetFileServlet.class);
  
  private Integer                        idPlate;
  private Plate                          plate;
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {


    // Restrict commands to local host if request is not secure
    if (!ServletUtil.checkSecureRequest(req, log)) {
      ServletUtil.reportServletError(response, "Secure connection is required. Prefix your request with 'https'",
              log, "Accessing secure command over non-secure line from remote host is not allowed.");
      return;
    }

    // Get the fileName parameter
    if (req.getParameter("idPlate") != null && !req.getParameter("idPlate").equals("")) {
      idPlate = Integer.valueOf(req.getParameter("idPlate"));
    }
    
    if (idPlate == null) {
      ServletUtil.reportServletError(response, "Missing parameter:  idPlate required", log);
    }

    InputStream in = null;
    SecurityAdvisor secAdvisor = null;
    
    try {
      

      // Get security advisor
     secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

     
     if (secAdvisor != null) {

        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
        plate = (Plate)sess.load(Plate.class, idPlate);

        String plateName = plate.getLabel() != null && !plate.getLabel().equals("") ? plate.getLabel() : plate.getIdPlate().toString();
        plateName = plateName.replaceAll("\\s", "_");
        String plateFileName = plateName + ".txt";

        // Check permissions 
        if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
          response.setContentType("text/html");
          response.getOutputStream().println(
              "<html><head><title>Error</title></head>");
          response.getOutputStream().println("<body><b>");
          response.getOutputStream().println(
              "DownloadPlateSampleSheetFileServlet: Insufficient permission to export sample names for plate."
                  + "<br>");
          response.getOutputStream().println("</body>");
          response.getOutputStream().println("</html>");
          System.out.println( "DownloadPlateSampleSheetFileServlet: Insufficient  permission to export sample names for plate.");
          return;
        }
        
        
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + plateFileName);          
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");

        
        OutputStream out = response.getOutputStream();

        
        // Header
        response.getOutputStream().print( "Sample Name\t\n" );
        
        
        
        Element runNode = getPlateWells( sess );

        if( runNode != null ) {
          Iterator i = runNode.getChildren("PlateWell").iterator();

          while( i.hasNext() ) {
            Element well = (Element) i.next();

            String sampleName = well.getAttributeValue( "sampleName" ) != null ? well.getAttributeValue("sampleName") : "";
            if ( well.getAttributeValue( "isControl" ) != null && well.getAttributeValue( "isControl" ).equals("Y") ) {
              sampleName = "";
            }

            response.getOutputStream().print( sampleName + "\t\n" );

          }

        }

        out.close();
        out.flush();


      } else {
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "DownloadPlateSampleSheetFileServlet: You must have a SecurityAdvisor in order to run this command."
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        System.out.println( "DownloadPlateSampleSheetFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      HibernateSession.rollback();
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "DownloadPlateSampleSheetFileServlet: An exception occurred " + e.toString()
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      System.out.println( "DownloadPlateSampleSheetFileServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      try {
        secAdvisor.closeHibernateSession();        
      } catch (Exception e) {
        
      }
     
    }

  }    
  
  
  private Element getPlateWells(Session sess) {

    try { 
      Element plateNode = plate.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

      for ( int col = 1; col <= 12; col++ ) {
        for( char row = 'A'; row <= 'H'; row ++ ){
          
            Element wellNode = getWellNode( sess, row, col );
            plateNode.addContent(wellNode);
          
        }
        
      }
      
      return plateNode;

    } catch( Exception e ) {
      log.error( "An exception has occurred in CreateRunFile ", e );
      e.printStackTrace();
      return null;
    }
  }
  
  private Element getWellNode( Session sess, char row, int col) {
    try {
      
      Element wellNode = new Element("PlateWell");

        String wellQuery = "SELECT pw from PlateWell as pw where pw.idPlate=" + plate.getIdPlate() + "        AND pw.row='" + row + "'       AND pw.col=" + col;
        PlateWell plateWell = (PlateWell) sess.createQuery( wellQuery ).uniqueResult();

        if ( plateWell != null ) {
          plateWell.excludeMethodFromXML("getPlate");
          plateWell.excludeMethodFromXML("getSample");
          plateWell.excludeMethodFromXML("getAssay");
          plateWell.excludeMethodFromXML("getPrimer");
          wellNode = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
//          wellNode.setAttribute("sampleName", plateWell.getSampleName() != null ? plateWell.getSampleName() : "" );

        } else {
          wellNode.setAttribute( "idPlateWell", "0" );
        }

      return wellNode;

    } catch( Exception e ) {
      log.error( "An exception has occurred in DownloadPlateSampleSheet ", e );
      e.printStackTrace();
      return null;
    }
  }

  
 
}