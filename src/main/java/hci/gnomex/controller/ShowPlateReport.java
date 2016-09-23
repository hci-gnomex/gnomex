package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Assay;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Primer;
import hci.gnomex.model.ReactionType;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.PlateReportHTMLFormatter;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;
public class ShowPlateReport extends GNomExCommand implements Serializable {

  private static Logger LOG         = Logger.getLogger( ShowPlateReport.class );

  public String                          SUCCESS_JSP = "/getHTML.jsp";

  private Integer                        idPlate;
  private Plate                          plate;
  private String                         codeReactionType;
  private InstrumentRun                  ir;
  private Map                            groupsMap = new HashMap();
  
  

  public void loadCommand( HttpServletRequest request, HttpSession session ) {
    
    if (request.getParameter("idPlate") != null && !request.getParameter("idPlate").equals("")) {
      idPlate = Integer.valueOf( request.getParameter("idPlate") );
    }
  }


  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(
          this.getUsername() );

      
      // Get plate
      plate = ( Plate ) sess.get( Plate.class, idPlate );
      if( plate == null ) {
        this.addInvalidField( "no plate", "Plate not found" );
      }
      
      if( this.isValid() ) {

        if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

          codeReactionType = plate.getCodeReactionType();
          
          // Get instrument run
          if( plate.getInstrumentRun() != null ) {
            ir = plate.getInstrumentRun();
          }
          
          // Get plate xml
          Element pNode = this.getPlateXML( plate, sess );

          
          // HTML
          
          // HTML formatter
          PlateReportHTMLFormatter formatter = new PlateReportHTMLFormatter(
              this.getSecAdvisor(), pNode, ir );

          Element root = new Element( "HTML" );
          Document doc = new Document( root );

          Element head = new Element( "HEAD" );
          root.addContent( head );

          Element link = new Element( "link" );
          link.setAttribute( "rel", "stylesheet" );
          link.setAttribute( "type", "text/css" );
          link.setAttribute( "href", Constants.PLATE_REPORT_CSS );
          link.setAttribute( "title", "standard" );
          head.addContent( link );
          
          Element title = new Element( "TITLE" );
          title.addContent( plate.getLabel() );
          head.addContent( title );

          Element body = new Element("BODY");
          root.addContent(body);
          
          Element maindiv = new Element("CENTER");
          maindiv.setAttribute("id", "containerForm");
          body.addContent(maindiv);
          
          
          // Print link
          Element printColRight = new Element("CENTER");
          printColRight.setAttribute("id", "printLinkColRight");
          maindiv.addContent(printColRight);

          Element printLink = new Element("A");
          printLink.setAttribute("HREF", "javascript:window.print()");
          printLink.addContent("Print");
          printColRight.addContent(printLink);

          Element ftr = new Element("CENTER");
          ftr.setAttribute("id", "footer");            
          maindiv.addContent(ftr);

          
          // Plate header
          // Plate Name
          Element h2 = new Element( "H2" );
          h2.addContent( plate.getLabel() ); 
          maindiv.addContent( h2 );
          
          // Run information table
          if ( ir != null ) {
            maindiv.addContent( new Element( "BR" ) );
            maindiv.addContent( formatter.makeRunTable() );
          }
          
          // Colored plate table
          maindiv.addContent( new Element( "BR" ) );
          maindiv.addContent( formatter.makePlateTable() );
          maindiv.addContent( new Element( "BR" ) );

         
          if ( codeReactionType.equals( ReactionType.SEQUENCING_REACTION_TYPE )) {
            // Request information table
            Element reqInf = new Element( "H5" );
            reqInf.addContent( "Request Information:" );
            maindiv.addContent( reqInf );
            maindiv.addContent( new Element( "BR" ) );
            maindiv.addContent( formatter.makeRequestTable( groupsMap ) );
          } else if ( codeReactionType.equals( ReactionType.MITO_DLOOP_REACTION_TYPE ) ) {
            // Primer information table
            Element primerInf = new Element( "H5" );
            primerInf.addContent( "Primer Information:" );
            maindiv.addContent( primerInf );
            maindiv.addContent( new Element( "BR" ) );
            maindiv.addContent( formatter.makePrimerTable( groupsMap ) );
          } else if ( codeReactionType.equals( ReactionType.FRAGMENT_ANALYSIS_REACTION_TYPE ) ) {
            // Assay information table
            Element assayInf = new Element( "H5" );
            assayInf.addContent( "Assay Information:" );
            maindiv.addContent( assayInf );
            maindiv.addContent( new Element( "BR" ) );
            maindiv.addContent( formatter.makeAssayTable( groupsMap ) );
          }
          

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.getFormat().setOmitEncoding( true );
          this.xmlResult = out.outputString( doc );
          this.xmlResult = this.xmlResult.replaceAll( "&amp;", "&" );
          this.xmlResult = this.xmlResult.replaceAll( "�", "&micro" );

          // Injust the <script> for java script handling of alternate style
          // sheets
          this.xmlResult = this.xmlResult.replaceAll( "JAVASCRIPT_GOES_HERE",
              "<script type=\"text/javascript\" src=\"switchPrintStyleSheet.js\"></script>" );

        } else {
          this.addInvalidField( "Insufficient permissions",
              "Insufficient permission to view plate report." );
        }

      }

      if( isValid() ) {
        setResponsePage( this.SUCCESS_JSP );
      } else {
        setResponsePage( this.ERROR_JSP );
      }

    } catch( UnknownPermissionException e ) {
      LOG.error( "An exception has occurred in ShowPlateReport ", e );

      throw new RollBackCommandException( e.getMessage() );

    } catch( NamingException e ) {
      LOG.error( "An exception has occurred in ShowPlateReport ", e );

      throw new RollBackCommandException( e.getMessage() );

    } catch( SQLException e ) {
      LOG.error( "An exception has occurred in ShowPlateReport ", e );

      throw new RollBackCommandException( e.getMessage() );

    } catch( Exception e ) {
      LOG.error( "An exception has occurred in ShowPlateReport ", e );

      throw new RollBackCommandException( e.getMessage() );
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch( Exception e ) {

      }
    }

    return this;
  }

  private Element getPlateXML( Plate plate, Session sess ) throws XMLReflectException {
    
    plate.excludeMethodFromXML("getPlateWells");
    plate.excludeMethodFromXML( "getInstrumentRun" );
    Element pNode = plate.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
    
    // Get the wells in row order
    for( char row = 'A'; row <= 'H'; row ++ ) {
      for ( int col = 1; col <= 12; col++ ) {
        String queryStr = "SELECT pw from PlateWell as pw where pw.idPlate=" + idPlate 
                       + "        AND pw.row='" + row + "'       AND pw.col=" + col;
        PlateWell plateWell = (PlateWell) sess.createQuery( queryStr ).uniqueResult();
        
        Element node = new Element("PlateWell");
        if ( plateWell != null ) {
          
          plateWell.excludeMethodFromXML("getPlate");

          node = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

          if ( plateWell.getIsControl().equals( "N" ) ) {

            Request request = (Request) sess.get(Request.class, plateWell.getIdRequest());
            Assay assay = plateWell.getAssay();
            Primer primer = plateWell.getPrimer();

            node.setAttribute("requestSubmitDate", request != null ? request.getCreateDate().toString() : "");
            node.setAttribute("requestSubmitter", request != null ? request.getOwnerName() : "");
            node.setAttribute("requestNumber", request != null ? request.getNumber() : "");
            node.setAttribute("assayName", assay != null ? assay.getDisplay() : "");
            node.setAttribute("primerName", primer != null ? primer.getDisplay() : "");

            if ( codeReactionType.equals( ReactionType.SEQUENCING_REACTION_TYPE )) {
              if ( !groupsMap.containsKey( plateWell.getIdRequest().toString() ) && request != null ) {
                groupsMap.put(plateWell.getIdRequest().toString(), request);
              }
            } else if ( codeReactionType.equals( ReactionType.MITO_DLOOP_REACTION_TYPE ) ) {
              if ( !groupsMap.containsKey( plateWell.getIdPrimer().toString() ) && primer != null ) {
                groupsMap.put(plateWell.getIdPrimer().toString(), primer);
              }
            } else if ( codeReactionType.equals( ReactionType.FRAGMENT_ANALYSIS_REACTION_TYPE ) ) {
              if ( !groupsMap.containsKey( plateWell.getIdAssay().toString() ) && assay != null ) {
                groupsMap.put(plateWell.getIdAssay().toString(), assay);
              }
            }

          }

        } else {
          node.setAttribute( "idPlateWell", "0" );
        }
        pNode.addContent(node);
      }
    }
    
    return pNode;
  }
  
  
  public void validate() { 
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
