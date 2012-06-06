
package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.SealType;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class CreateRunFile extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log         = org.apache.log4j.Logger.getLogger( CreateRunFile.class );

  public String                          SUCCESS_JSP = "/getHTML.jsp";

  private Integer                        idInstrumentRun;
  private InstrumentRun                  ir;
  

  private DictionaryHelper               dictionaryHelper;


  public void validate() {
  }


  public void loadCommand( HttpServletRequest request, HttpSession session ) {
    
    if (request.getParameter("idInstrumentRun") != null && !request.getParameter("idInstrumentRun").equals("")) {
      idInstrumentRun = Integer.valueOf( request.getParameter("idInstrumentRun") );
    }
    
  }


  public Command execute() throws RollBackCommandException {

    try {

      Session sess = HibernateSession.currentSession(this.getUsername());

      dictionaryHelper = DictionaryHelper.getInstance( sess );

      // Get plate
      ir = ( InstrumentRun ) sess.get( InstrumentRun.class, idInstrumentRun );
      if( ir == null ) {
        this.addInvalidField( "no run", "Run not found" );
      }

      if( this.isValid() ) {
        if( 1==1 ) { //this.getSecAdvisor().canRead( plate ) ) {

          // Change run status
          if ( ir.getCodeInstrumentRunStatus() == null || 
               ir.getCodeInstrumentRunStatus().equals( InstrumentRunStatus.PENDING ) ) {
            ir.setCodeInstrumentRunStatus( InstrumentRunStatus.RUNNING );
          }
          
          changeRequestsToProcessing( sess, ir );
          sess.flush();
          
          
          String runName = ir.getLabel();
          String dirName = "C:\\temp\\";
          String fileExt = ".plt";
          
          File runfile = new File( dirName + runName + fileExt);
          FileWriter fwrite = new FileWriter(runfile);
          // Run headers 
          fwrite.write( "Container Name\tPlate ID\tDescription\tApplication\tContainerType\tOwner\tOperator\tPlateSealing\tSchedulingPref\t\r\n" );
          // Run information
          SealType sealType = ( SealType ) sess.get( SealType.class, ir.getCodeSealType() );
          String sealTypeText = sealType.getSealType();
          
          fwrite.write( runName + "\t" + runName + "\tSequencingAnalysis\t384-Well\tCore\t3730-1\t" + sealTypeText + "\t1234\t\r\n" );
          // Well headers
          fwrite.write( "Well\tSample Name\tComment\tResults\tGroup" +
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
                    
                    fwrite.write( row + String.format( "%02d", col ) + "\t" );
                    
                    String fileName = idSample + "?" + sampleName + "?" + idPlate;
                    fwrite.write( fileName + "\t" );
                    
                    String comments = "<ID:" + idPlateWellString + "><WELL:" + wellRow + String.format( "%02d", wellCol ) + ">";
                    fwrite.write( comments + "\t" );
                    
                    fwrite.write( "Finch\tLongSeq50\tSeq_A\t\r\n");
                  }
                  
                  
                  
                }
              }
            }
          }
          
          
          fwrite.flush();
          fwrite.close();
          
          
          
          
          Document doc = new Document(new Element("SUCCESS"));

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(doc);
          
          setResponsePage(this.SUCCESS_JSP);
        } else {
          this.addInvalidField( "Insufficient permissions",
              "Insufficient permission to read request." );
        }

      }

      if( isValid() ) {
        setResponsePage( this.SUCCESS_JSP );
      } else {
        setResponsePage( this.ERROR_JSP );
      }

    }  catch( NamingException e ) {
      log.error( "An exception has occurred in ShowRequestForm ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( SQLException e ) {
      log.error( "An exception has occurred in ShowRequestForm ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch (IOException e) {
      e.printStackTrace();
    } catch( Exception e ) {
      log.error( "An exception has occurred in ShowRequestForm ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch( Exception e ) {

      }
    }

    return this;
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
      log.error( "An exception has occurred in ShowRequestForm ", e );
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

  /**
   * The callback method called after the loadCommand, and execute methods, this
   * method allows you to manipulate the HttpServletResponse object prior to
   * forwarding to the result JSP (add a cookie, etc.)
   * 
   * @param request
   *          The HttpServletResponse for the command
   * @return The processed response
   */
  public HttpServletResponse setResponseState( HttpServletResponse response ) {
    return response;
  }


}
