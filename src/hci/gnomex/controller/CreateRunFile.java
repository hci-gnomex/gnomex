
package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.SealType;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PlateReportHTMLFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
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
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
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

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(
          this.getUsername() );

      dictionaryHelper = DictionaryHelper.getInstance( sess );

      // Get plate
      ir = ( InstrumentRun ) sess.get( InstrumentRun.class, idInstrumentRun );
      if( ir == null ) {
        this.addInvalidField( "no run", "Run not found" );
      }

      if( this.isValid() ) {
        if( 1==1 ) { //this.getSecAdvisor().canRead( plate ) ) {

          String runName = ir.getLabel();
          String dirName = "C:\\temp\\";
          String fileExt = ".plt";
          
          File runfile = new File( dirName + runName + fileExt);
          FileWriter fwrite = new FileWriter(runfile);
          // Run headers 
          fwrite.write( "Container Name\tPlate ID\tDescription\tApplication\tContainerType\tOwner\tOperator\tPlateSealing\tSchedulingPref\t" );
          // Run information
          SealType sealType = ( SealType ) sess.get( SealType.class, ir.getCodeSealType() );
          String sealTypeText = sealType.getSealType();
          
          fwrite.write("\r\n" + runName + "\t" + runName + "\tSequencingAnalysis\t384-Well\tCore\t3730-1\t" + sealTypeText + "\t1234" );
          // Well headers
          fwrite.write( "\r\nWell\tSample Name\tComment\tResults\tGroup" +
          		"\tInstrument Protocol 1\tAnalysis Protocol 1" +
          		"\tInstrument Protocol 2\tAnalysis Protocol 2" +
          		"\tInstrument Protocol 3\tAnalysis Protocol 3" +
          		"\tInstrument Protocol 4\tAnalysis Protocol 4" +
          		"\tInstrument Protocol 5\tAnalysis Protocol 5" );
          
          char plateRow = 'A';
          int plateInd = 0;
          
          for ( char row = 'A'; row <= 'P'; row++ ) {
            for ( int col = 1; col <= 23; col=col+2 ) {
              
              // This inner loop will actually go through all cols, not just the odd ones.
              // For the odd ones, it will use plates 0 or 1, for the even cols, it will
              // use plates 2 or 3.
              
              fwrite.write( "\r\n" + row + String.format( "%02d", col ) + "\t" );
            }

            // Every other time a row is filled up => plateRow++;
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

    } catch( UnknownPermissionException e ) {
      log.error( "An exception has occurred in ShowRequestForm ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( NamingException e ) {
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
