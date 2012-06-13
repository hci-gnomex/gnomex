
package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.InstrumentRunFilter;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.ReactionType;
import hci.gnomex.model.Request;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetInstrumentRunList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log      = org.apache.log4j.Logger.getLogger( GetInstrumentRunList.class );

  private InstrumentRunFilter            runFilter;
  private String                         listKind = "RunList";
  private Element                        rootNode = null;
  private String                         message  = "";


  public void validate() {
  }


  public void loadCommand( HttpServletRequest request, HttpSession session ) {

    runFilter = new InstrumentRunFilter();
    HashMap errors = this.loadDetailObject( request, runFilter );
    this.addInvalidFields( errors );

    if( request.getParameter( "listKind" ) != null
        && ! request.getParameter( "listKind" ).equals( "" ) ) {
      listKind = request.getParameter( "listKind" );

    }

  }


  public Command execute() throws RollBackCommandException {

    try {
      Document doc = new Document( new Element( listKind ) );

      if( ! runFilter.hasSufficientCriteria( this.getSecAdvisor() ) ) {
        message = "Please select a filter";
//        rootNode.setAttribute( "message", message );
      } else {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(
            this.getUsername() );

        StringBuffer buf = runFilter.getQuery( this.getSecAdvisor() );
        log.info( "Query for GetInstrumentRunList: " + buf.toString() );
        List runs = sess.createQuery( buf.toString() ).list();

        for( Iterator i = runs.iterator(); i.hasNext(); ) {

          Object[] row = ( Object[] ) i.next();

          Integer idInstrumentRun = row[0] == null ? new Integer( - 2 )
              : ( Integer ) row[0];
          String runDate = this.formatDate( ( java.sql.Timestamp ) row[1] );
          String createDate = this.formatDate( ( java.sql.Timestamp ) row[2] );
          String codeInstrumentRunStatus = row[3] == null ? ""
              : row[3].toString();
          String comments = row[4] == null ? "" : row[4].toString();
          String label = row[5] == null ? "" : row[5].toString();
          String codeReactionType = row[6] == null ? "" : row[6].toString();
          String creator = row[7] == null ? "" : row[7].toString();
          String codeSealType = row[8] == null ? "" : row[8].toString();

          Element irNode = new Element( "InstrumentRun" );
          irNode.setAttribute( "idInstrumentRun", idInstrumentRun.toString() );
          irNode.setAttribute( "runDate", runDate );
          irNode.setAttribute( "createDate", this.formatDate(createDate, this.DATE_OUTPUT_SQL));
          irNode.setAttribute( "codeInstrumentRunStatus",
              codeInstrumentRunStatus.toString() );
          irNode.setAttribute( "comments", comments );
          irNode.setAttribute( "label", label );
          irNode.setAttribute( "codeReactionType", codeReactionType );
          AppUser user = (AppUser)sess.get(AppUser.class, Integer.valueOf( creator ) );
          irNode.setAttribute( "creator", user != null ? user.getDisplayName() : creator );
          
          irNode.setAttribute( "codeSealType", codeSealType );
          irNode.setAttribute( "icon", ReactionType.getIcon(codeReactionType));

          List plates = sess.createQuery(
              "SELECT p from Plate as p where p.idInstrumentRun="
                  + idInstrumentRun ).list();

          for( Iterator i2 = plates.iterator(); i2.hasNext(); ) {
            Plate plate = ( Plate ) i2.next();
            plate.excludeMethodFromXML( "getPlateWells" );
            plate.excludeMethodFromXML( "getcreateDate" );
            plate.excludeMethodFromXML( "getInstrumentRun" );
            Element pNode = plate.toXMLDocument( null,
                DetailObject.DATE_OUTPUT_SQL ).getRootElement();
            pNode.setAttribute( "createDate",
                this.formatDate( plate.getCreateDate() ) );

            List wells = sess.createQuery(
                "SELECT w from PlateWell as w where w.idPlate="
                    + plate.getIdPlate() ).list();

            for( Iterator i3 = wells.iterator(); i3.hasNext(); ) {
              PlateWell well = ( PlateWell ) i3.next();
              well.excludeMethodFromXML( "getSample" );
              Element pwNode = well.toXMLDocument( null,
                  DetailObject.DATE_OUTPUT_SQL ).getRootElement();

              pwNode.setAttribute("requestSubmitDate", "");
              pwNode.setAttribute("requestSubmitter", "");
              
              if ( well.getIdRequest() != null ) {
                String idRequestString = well.getIdRequest().toString();
                if ( idRequestString != null && !idRequestString.equals("")) {
                  Request request = (Request) sess.createQuery("SELECT r from Request as r where r.idRequest=" + idRequestString).uniqueResult();
                  if ( request != null ) {
                    pwNode.setAttribute("requestSubmitDate", request.getCreateDate().toString());
                    pwNode.setAttribute("requestSubmitter", request.getOwnerName());
                  }
                }
              }
              
              pNode.addContent( pwNode );
            }

            irNode.addContent( pNode );
          }

          doc.getRootElement().addContent( irNode );

        }
      }

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString( doc );

      setResponsePage( this.SUCCESS_JSP );
    } catch( NamingException e ) {
      log.error( "An exception has occurred in GetRunList ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );

    } catch( SQLException e ) {
      log.error( "An exception has occurred in GetRunList ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );
    } catch( XMLReflectException e ) {
      log.error( "An exception has occurred in GetRunList ", e );
      e.printStackTrace();
      throw new RollBackCommandException( e.getMessage() );
    } catch( Exception e ) {
      log.error( "An exception has occurred in GetRunList ", e );
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

}
