package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class ShowRedoReport extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log         = org.apache.log4j.Logger.getLogger( ShowRedoReport.class );

  public String                          SUCCESS_JSP = "/getHTML.jsp";

  private Integer                        idPlate;
  private Plate                          plate;
  private Element                        wellsNode; 


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

          // Get plate wells xml
          wellsNode = this.getPlateWellXML( plate, sess );

          // HTML
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
          title.addContent( "Redo Report" );
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
          Element h2 = new Element( "H2" );
          h2.addContent( plate.getLabel() ); 
          maindiv.addContent( h2 );
          Element h5 = new Element( "h4" );
          h5.addContent( "Redo Report" );
          maindiv.addContent( h5 );

          // Redo wells table
          maindiv.addContent( new Element( "BR" ) );
          maindiv.addContent( makeTable() );

          
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
          "Insufficient permission to view plate report." );
        }

      }

      if( isValid() ) {
        setResponsePage( this.SUCCESS_JSP );
      } else {
        setResponsePage( this.ERROR_JSP );
      }

    } catch( UnknownPermissionException e ) {
      log.error( "An exception has occurred in ShowRedoReport ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( NamingException e ) {
      log.error( "An exception has occurred in ShowRedoReport ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( SQLException e ) {
      log.error( "An exception has occurred in ShowRedoReport ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( Exception e ) {
      log.error( "An exception has occurred in ShowRedoReport ", e );
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

  private Element makeTable() {

    Element table = new Element("TABLE");
    table.setAttribute("CLASS",       "grid");
    table.setAttribute("CELLPADDING", "5");
    table.setAttribute("CELLSPACING", "5");

    Element rowh = new Element("TR");
    table.addContent(rowh);
    this.addHeaderCell(rowh, "Dest. Well" );
    this.addHeaderCell(rowh, "Source Plate" );
    this.addHeaderCell(rowh, "Source Well"    );
    this.addHeaderCell(rowh, "Sample Name");

    if( wellsNode != null ) {
      for ( Iterator i = wellsNode.getChildren("PlateWell").iterator(); i.hasNext(); ) {
        
        Element well = (Element) i.next();

        String destWellName = well.getAttributeValue( "wellName" );
        String sourcePlateName = well.getAttributeValue( "sourcePlateName" );
        String sourceWellName = well.getAttributeValue( "sourceWellName" );
        String sampleName = well.getAttributeValue( "sampleName" );
                       
        Element row = new Element("TR");
        table.addContent(row);
        
        this.addCell( row, destWellName );
        this.addCell( row, sourcePlateName );
        this.addCell( row, sourceWellName );
        this.addCell( row, sampleName );
        
      }  
    }
    return table;
  }
  
  private void addHeaderCell(Element row, String header) {
    Element cell = new Element("TH");    
    cell.setAttribute("CLASS", "normal");
    cell.addContent(header);
    row.addContent(cell);
  }
  
  private void addCell(Element row, String value) {
    Element cell = new Element("TD");
    cell.setAttribute("CLASS", "grid");      
    cell.addContent(value);
    row.addContent(cell);
  }
  
  public static StringBuffer getRedoSourceWellQuery( PlateWell reactionWell ) {
    StringBuffer    queryBuf = new StringBuffer();
    queryBuf.append(" SELECT     well FROM PlateWell as well ");
    queryBuf.append(" LEFT JOIN  well.plate plate ");
    // Find the original reaction plate containing the samples
    queryBuf.append(" WHERE   (plate.codePlateType = '" + PlateType.REACTION_PLATE_TYPE + "') "); 
    queryBuf.append(" AND well.redoFlag = 'N' ");

    if ( reactionWell.getIdSample() != null ) {
      queryBuf.append(" AND   (well.idSample = '" + reactionWell.getIdSample() + "') ");
    }
    return queryBuf;
  }

  public static StringBuffer getRedoWellsQuery( Plate plate ) {
    StringBuffer    queryBuf = new StringBuffer();
    queryBuf.append(" SELECT     well FROM PlateWell as well ");
    queryBuf.append(" WHERE   well.idPlate = " + plate.getIdPlate() ); 
    queryBuf.append(" AND     well.redoFlag = 'Y' ");

    return queryBuf;
  }

  private Element getPlateWellXML( Plate plate, Session sess ) throws XMLReflectException {

    plate.excludeMethodFromXML("getPlateWells");
    plate.excludeMethodFromXML( "getInstrumentRun" );
    Element pNode = plate.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

    StringBuffer buf = getRedoWellsQuery( plate );
    Query query = sess.createQuery(buf.toString());
    List redoWells = query.list();

    for ( Iterator i2 = redoWells.iterator(); i2.hasNext();) {
      PlateWell redoWell = (PlateWell) i2.next();

      StringBuffer buf2 = getRedoSourceWellQuery( redoWell );
      PlateWell sourceWell = (PlateWell) sess.createQuery( buf2.toString() ).uniqueResult();

      Element node = new Element("PlateWell");

      redoWell.excludeMethodFromXML("getPlate");

      node = redoWell.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();

      if ( sourceWell != null ) {

        Plate sourcePlate = sourceWell.getPlate();
        node.setAttribute("sourceIdPlateWell", sourceWell.getIdPlateWell() != null ? sourceWell.getIdPlateWell().toString() : "");
        node.setAttribute("sourceIdPlate", sourcePlate.getIdPlate() != null ? sourcePlate.getIdPlate().toString() : "");
        node.setAttribute("sourceWellName", sourceWell.getWellName() != null ? sourceWell.getWellName() : "");
        node.setAttribute("sourcePlateName", sourcePlate.getLabel() != null ? sourcePlate.getLabel() : ""); 
      }

      pNode.addContent( node );
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
