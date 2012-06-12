package hci.gnomex.controller;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Chromatogram;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.SealType;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class DownloadABIRunFileServlet extends HttpServlet { 

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadABIRunFileServlet.class);
  
  private Integer                        idInstrumentRun;
  private InstrumentRun                  ir;
  private DictionaryHelper               dictionaryHelper;
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    

    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
      }
      else {
        log.error("Accessing secure command over non-secure line from remote host is not allowed");
        
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "Secure connection is required. Prefix your request with 'https: "
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        return;
      }
    }
    
    
    // Get the fileName parameter
    if (req.getParameter("idInstrumentRun") != null && !req.getParameter("idInstrumentRun").equals("")) {
      idInstrumentRun = Integer.valueOf(req.getParameter("idInstrumentRun"));
    }
    
    if (idInstrumentRun == null) {
      log.error("idInstrumentRun required");
      
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "Missing parameter:  idInstrumentRun required"
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      return;
      
    }

    InputStream in = null;
    SecurityAdvisor secAdvisor = null;
    
    try {
      

      // Get security advisor
     secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

     
     if (secAdvisor != null) {

        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
        ir = (InstrumentRun)sess.load(InstrumentRun.class, idInstrumentRun);

        String runName = ir.getLabel() != null && !ir.getLabel().equals("") ? ir.getLabel() : ir.getIdInstrumentRun().toString();
        String runFileName = "abirun-" + runName.replaceAll("\\s", "-") + ".plt";

        // Check permissions 
        if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
          response.setContentType("text/html");
          response.getOutputStream().println(
              "<html><head><title>Error</title></head>");
          response.getOutputStream().println("<body><b>");
          response.getOutputStream().println(
              "DownloadABIRunFileServlet: Insufficient permission to generate ABI Run (.plt) file."
                  + "<br>");
          response.getOutputStream().println("</body>");
          response.getOutputStream().println("</html>");
          System.out.println( "DownloadABIRunFileServlet: Insufficient  permission to generate ABI Run (.plt) file.");
          return;
        }
        
        
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + runFileName);          
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");

        
        OutputStream out = response.getOutputStream();

        
        
        
        
        // Change run status
        if ( ir.getCodeInstrumentRunStatus() == null || 
             ir.getCodeInstrumentRunStatus().equals( InstrumentRunStatus.PENDING ) ) {
          ir.setCodeInstrumentRunStatus( InstrumentRunStatus.RUNNING );
        }
        
        changeRequestsToProcessing( sess, ir );
        sess.flush();
        
        // Run headers 
        response.getOutputStream().print( "Container Name\tPlate ID\tDescription\tApplication\tContainerType\tOwner\tOperator\tPlateSealing\tSchedulingPref\t\r\n" );
        // Run information
        SealType sealType = ( SealType ) sess.get( SealType.class, ir.getCodeSealType() );
        String sealTypeText = sealType.getSealType();
        
        response.getOutputStream().print( runName + "\t" + runName + "\tSequencingAnalysis\t384-Well\tCore\t3730-1\t" + sealTypeText + "\t1234\t\r\n" );
        // Well headers
        response.getOutputStream().print( "Well\tSample Name\tComment\tResults\tGroup" +
            "\tInstrument Protocol 1\tAnalysis Protocol 1" +
            "\tInstrument Protocol 2\tAnalysis Protocol 2" +
            "\tInstrument Protocol 3\tAnalysis Protocol 3" +
            "\tInstrument Protocol 4\tAnalysis Protocol 4" +
            "\tInstrument Protocol 5\tAnalysis Protocol 5\t\r\n" );
        
        Element runNode = getRunWells( sess );
        
        if( runNode != null ) {
          Iterator i = runNode.getChildren("PlateWell").iterator();
        
          for ( char row = 'A'; row <= 'P'; row++ ) {
            for ( int col = 1; col <= 23; col++ ) {

              if( i.hasNext() ) {
                Element well = (Element) i.next();
                
                String idPlateWellString = well.getAttributeValue("idPlateWell") != null ? well.getAttributeValue("idPlateWell") : "0";
                String sampleName = well.getAttributeValue( "sampleName" ) != null ? well.getAttributeValue("sampleName") : "";
                String idSample = well.getAttributeValue( "idSample" ) != null ? well.getAttributeValue("idSample") : "";
                String idPlate = well.getAttributeValue( "idPlate" ) != null ? well.getAttributeValue("idPlate") : "";
                String wellRow = well.getAttributeValue( "row" ) != null ? well.getAttributeValue("row") : "";
                int wellCol = well.getAttributeValue( "col" ) != null ? Integer.valueOf( well.getAttributeValue("col") ) : 0;

                // Add request number to list of request numbers
                if ( idPlateWellString != null && !idPlateWellString.equals( "0" ) ) {
                  
                  response.getOutputStream().print( row + String.format( "%02d", col ) + "\t" );
                  
                  String fileName = idSample + "?" + sampleName + "?" + idPlate;
                  response.getOutputStream().print( fileName + "\t" );
                  
                  String comments = "<ID:" + idPlateWellString + "><WELL:" + wellRow + String.format( "%02d", wellCol ) + ">";
                  response.getOutputStream().print( comments + "\t" );
                  
                  response.getOutputStream().print( "Finch\tLongSeq50\tSeq_A\t\r\n");
                }
                
                
                
              }
            }
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
            "DownloadABIRunFileServlet: You must have a SecurityAdvisor in order to run this command."
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        System.out.println( "DownloadABIRunFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "DownloadABIRunFileServlet: An exception occurred " + e.toString()
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      System.out.println( "DownloadABIRunFileServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      try {
        secAdvisor.closeHibernateSession();        
      } catch (Exception e) {
        
      }
      
     
    }

  }    
  
  
  private Element getRunWells(Session sess) {

    try { 
      Element irNode = ir.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

      for( char row = 'A'; row <= 'H'; row ++ ) {
        for ( int i = 0; i < 4; i++) {
          for ( int col = 1; col <= 12; col++ ) {

            int quadrant = i; 
            if ( i == 1 ) {
              quadrant = 2;
            } else if ( i==2 ) {
              quadrant = 1; 
            } 

            String plateQuery = "SELECT p from Plate as p where p.idInstrumentRun=" + ir.getIdInstrumentRun() + "       AND p.quadrant=" + quadrant;
            Plate plate = (Plate) sess.createQuery( plateQuery ).uniqueResult();

            Element wellNode = new Element("PlateWell");

            if ( plate != null ) {

              String wellQuery = "SELECT pw from PlateWell as pw where pw.idPlate=" + plate.getIdPlate() + "        AND pw.row='" + row + "'       AND pw.col=" + col;
              PlateWell plateWell = (PlateWell) sess.createQuery( wellQuery ).uniqueResult();

              if ( plateWell != null ) {
                plateWell.excludeMethodFromXML("getPlate");

                wellNode = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

              } else {
                wellNode.setAttribute( "idPlateWell", "0" );
              }

            } else {
              wellNode.setAttribute( "idPlateWell", "0" );
            }
            irNode.addContent(wellNode);

          }
        }
      }
      
      return irNode;

    } catch( Exception e ) {
      log.error( "An exception has occurred in CreateRunFile ", e );
      e.printStackTrace();
      return null;
    }
  }

  private void changeRequestsToProcessing(Session sess, InstrumentRun ir) {
    
    // Get any requests on that run
    Map requests = new HashMap();
    List wells = sess.createQuery( "SELECT pw from PlateWell as pw " +
        " join pw.plate as plate where plate.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();
    for(Iterator i1 = wells.iterator(); i1.hasNext();) {
      PlateWell well = (PlateWell)i1.next();
      if ( well.getIdRequest() == null ) {
        break;
      }
      if ( !well.getIdRequest().equals( "" ) && !requests.containsKey( well.getIdRequest() ) ) {
        Request req = (Request) sess.get(Request.class, well.getIdRequest());
        requests.put( req.getIdRequest(), req );
      }
    }
    
    // Change request Status 
    for ( Iterator i = requests.keySet().iterator(); i.hasNext();) {
      int idReq = (Integer) i.next();
      Request req = (Request) sess.get(Request.class, idReq );
      if ( req.getCodeRequestStatus() == null ) {
        req.setCodeRequestStatus( RequestStatus.PROCESSING );
      } else if ( req.getCodeRequestStatus().equals( RequestStatus.NEW ) || 
                  req.getCodeRequestStatus().equals( RequestStatus.SUBMITTED ) ) {
        req.setCodeRequestStatus( RequestStatus.PROCESSING );
      }
    }
    sess.flush();
  }

 
}