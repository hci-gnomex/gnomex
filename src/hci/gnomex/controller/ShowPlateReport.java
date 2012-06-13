
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
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PlateReportHTMLFormatter;

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

public class ShowPlateReport extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log         = org.apache.log4j.Logger.getLogger( ShowPlateReport.class );

  public String                          SUCCESS_JSP = "/getHTML.jsp";

  private Integer                        idPlate;
  private Plate                          plate;
  private InstrumentRun                  ir;
  private String                         plateXMLString;
  private Map                            reqMap = new HashMap();
  
  private Element                        plateNode; 
  
  private String                         appURL      = "";

  private DictionaryHelper               dictionaryHelper;


  public void validate() {
  }


  public void loadCommand( HttpServletRequest request, HttpSession session ) {
    
    if (request.getParameter("idPlate") != null && !request.getParameter("idPlate").equals("")) {
      idPlate = Integer.valueOf( request.getParameter("idPlate") );
    }
    if (request.getParameter("plateXMLString") != null && !request.getParameter("plateXMLString").equals("")) {
      plateXMLString = request.getParameter("plateXMLString");
      
      StringReader reader = new StringReader(plateXMLString);
      
      try {
        SAXBuilder sax = new SAXBuilder();
        Document plateDoc = sax.build(reader);
        plateNode = plateDoc.getRootElement();
      
      } catch (JDOMException je ) {
        log.error( "Cannot parse plateXMLString", je );
        this.addInvalidField( "plateXMLString", "Invalid xml");
      }
    }
  }


  public Command execute() throws RollBackCommandException {

    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(
          this.getUsername() );

      dictionaryHelper = DictionaryHelper.getInstance( sess );

      // Get plate
      plate = ( Plate ) sess.get( Plate.class, idPlate );
      if( plate == null ) {
        this.addInvalidField( "no plate", "Plate not found" );
      }

      if( this.isValid() ) {
        if( 1==1 ) { //this.getSecAdvisor().canRead( plate ) ) {

          // Get instrument run
          if( plate.getIdInstrumentRun() != null ) {
            ir = ( InstrumentRun ) sess.get( InstrumentRun.class, plate.getIdInstrumentRun() );
          }

          plate.excludeMethodFromXML("getPlateWells");
          plate.excludeMethodFromXML( "getInstrumentRun" );
          Element pNode = plate.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          
          // Get the wells in row order
          for( char row = 'A'; row <= 'H'; row ++ ) {
            for ( int col = 1; col <= 12; col++ ) {
              String queryStr = "SELECT pw from PlateWell as pw where pw.idPlate=" + idPlate + "        AND pw.row='" + row + "'       AND pw.col=" + col;
              PlateWell plateWell = (PlateWell) sess.createQuery( queryStr ).uniqueResult();
              
              Element node = new Element("PlateWell");
              if ( plateWell != null ) {
                plateWell.excludeMethodFromXML("getPlate");
                
                node = plateWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
                
                node.setAttribute("requestSubmitDate", "");
                node.setAttribute("requestSubmitter", "");
                
                if ( plateWell.getIdRequest() != null ) {
                  String idRequestString = plateWell.getIdRequest().toString();
                  if ( idRequestString != null && !idRequestString.equals("")) {
                    Request request = (Request) sess.createQuery("SELECT r from Request as r where r.idRequest=" + idRequestString).uniqueResult();
                    if ( !reqMap.containsKey( idRequestString ) ) {
                      reqMap.put(idRequestString, request);
                    }
                    if ( request != null ) {
                      node.setAttribute("requestSubmitDate", request.getCreateDate().toString());
                      node.setAttribute("requestSubmitter", request.getOwnerName());
                    }
                  }
                }
              } else {
                node.setAttribute( "idPlateWell", "0" );
              }
              pNode.addContent(node);
            }
          }
          
          
          // HTML
          
          // HTML formatter
          PlateReportHTMLFormatter formatter = new PlateReportHTMLFormatter(
              this.getSecAdvisor(), pNode, ir, dictionaryHelper );

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
          title.addContent( "Plate " + plate != null ? plate.getIdPlate().toString() : plateNode.getAttributeValue("idPlate") );
          head.addContent( title );

          Element body = new Element("BODY");
          root.addContent(body);
          
          Element outerDiv = new Element("DIV");
          outerDiv.setAttribute("id", "container");
          body.addContent(outerDiv);
          
          Element maindiv = new Element("DIV");
          maindiv.setAttribute("id", "containerForm");
          outerDiv.addContent(maindiv);
          
          
          // Print link
          Element printColRight = new Element("DIV");
          printColRight.setAttribute("id", "printLinkColRight");
          maindiv.addContent(printColRight);

          Element printLink = new Element("A");
          printLink.setAttribute("HREF", "javascript:window.print()");
          printLink.addContent("Print");
          printColRight.addContent(printLink);

          Element ftr = new Element("DIV");
          ftr.setAttribute("id", "footer");            
          maindiv.addContent(ftr);

          
          // Plate header
          Element h2 = new Element( "H2" );
          h2.addContent( "Plate " + ( plate != null ? plate.getIdPlate().toString() : plateNode.getAttributeValue("idPlate") ) ); 
          maindiv.addContent( h2 );
          
          if ( plate != null & !plate.getLabel().equals( "" ) ) {
            Element h4 = new Element( "h4" );
            h4.addContent( plate.getLabel() );
            maindiv.addContent( h4 );
          }
          
          
          // Run information
          if ( ir != null ) {
            maindiv.addContent( new Element( "BR" ) );
            maindiv.addContent( formatter.makeRunTable() );
          }
          
          // Plate table
          maindiv.addContent( new Element( "BR" ) );
          formatter.addPlateTable( maindiv );

          maindiv.addContent( new Element( "BR" ) );

          // Request information table
          Element reqInf = new Element( "H5" );
          reqInf.addContent( "Request Information:" );
          maindiv.addContent( reqInf );

          // for ( all requests on plate ) {
          // make a set of requests
          // }
          maindiv.addContent( new Element( "BR" ) );
          formatter.addRequestTable(maindiv, reqMap);



          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding( true );
          this.xmlResult = out.outputString( doc );
          this.xmlResult = this.xmlResult.replaceAll( "&amp;", "&" );
          this.xmlResult = this.xmlResult.replaceAll( "�", "&micro" );

          // Injust the <script> for java script handling of alternate style
          // sheets
          this.xmlResult = this.xmlResult.replaceAll( "JAVASCRIPT_GOES_HERE",
              "<script type=\"text/javascript\" src=\"switchPrintStyleSheet.js\"></script>" );

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
      log.error( "An exception has occurred in ShowPlateReport ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( NamingException e ) {
      log.error( "An exception has occurred in ShowPlateReport ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( SQLException e ) {
      log.error( "An exception has occurred in ShowPlateReport ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( Exception e ) {
      log.error( "An exception has occurred in ShowPlateReport ", e );
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
