
package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.InstrumentRunFilter;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.ReactionType;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.PropertyDictionaryHelper;

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
import org.apache.log4j.Logger;
public class GetInstrumentRunList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG      = Logger.getLogger( GetInstrumentRunList.class );

  private InstrumentRunFilter            runFilter;
  private String                         listKind = "RunList";
  private String						 message = "";

  private static final int				 DEFAULT_MAX_RUN_COUNT = 200;

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
      if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

        Document doc = new Document( new Element( listKind ) );

        if( ! runFilter.hasSufficientCriteria( this.getSecAdvisor() ) ) {
          this.addInvalidField("missing filter", "Please select a filter" );
        } else {
          Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(
              this.getUsername() );

          StringBuffer buf = runFilter.getQuery( this.getSecAdvisor() );
          LOG.info( "Query for GetInstrumentRunList: " + buf.toString() );
          List runs = sess.createQuery( buf.toString() ).list();
          
          Integer maxRuns = getMaxRuns(sess);
          int              runCount = 0;

          for( Iterator i = runs.iterator(); i.hasNext(); ) {

            Object[] row = ( Object[] ) i.next();

            Integer idInstrumentRun = row[0] == null ? new Integer( - 2 ) : ( Integer ) row[0];
            String runDate = this.formatDate( ( java.sql.Timestamp ) row[1] );
            String createDate = this.formatDate( ( java.sql.Timestamp ) row[2] );
            String codeInstrumentRunStatus = row[3] == null ? "" : row[3].toString();
            String comments = row[4] == null ? "" : row[4].toString();
            String label = row[5] == null ? "" : row[5].toString();
            String codeReactionType = row[6] == null ? "" : row[6].toString();
            String creator = row[7] == null ? "" : row[7].toString();
            String codeSealType = row[8] == null ? "" : row[8].toString();

            Element irNode = new Element( "InstrumentRun" );
            irNode.setAttribute( "idInstrumentRun", idInstrumentRun.toString() );
            irNode.setAttribute( "runDate", runDate );
            irNode.setAttribute( "createDate", this.formatDate(createDate, this.DATE_OUTPUT_SQL));
            irNode.setAttribute( "codeInstrumentRunStatus", codeInstrumentRunStatus.toString() );
            irNode.setAttribute( "comments", comments );
            irNode.setAttribute( "label", label );
            irNode.setAttribute( "codeReactionType", codeReactionType );
            if ( creator != null && !creator.equals( "" ) ) {
              AppUser user = (AppUser)sess.get(AppUser.class, Integer.valueOf(creator));
              irNode.setAttribute( "creator", user != null ? user.getDisplayName() : creator);
            } else {
              irNode.setAttribute( "creator", creator);
            }

            irNode.setAttribute( "codeSealType", codeSealType );
            irNode.setAttribute( "icon", ReactionType.getIcon(codeReactionType));

            List plates = sess.createQuery(
                "SELECT p from Plate as p where p.idInstrumentRun="
                + idInstrumentRun + "  ORDER BY p.quadrant").list();

            for( Iterator i2 = plates.iterator(); i2.hasNext(); ) {
              Plate plate = ( Plate ) i2.next();
              plate.excludeMethodFromXML( "getPlateWells" );
              plate.excludeMethodFromXML( "getcreateDate" );
              plate.excludeMethodFromXML( "getInstrumentRun" );
              Element pNode = plate.toXMLDocument( null,
                  DetailObject.DATE_OUTPUT_SQL ).getRootElement();
              pNode.setAttribute( "createDate",
                  this.formatDate( plate.getCreateDate() ) );

              irNode.addContent( pNode );
            }

            doc.getRootElement().addContent( irNode );
            
            runCount++;
            if (runCount >= maxRuns) {
                break;
              }
          }
          
          doc.getRootElement().setAttribute("runCount", Integer.valueOf(runCount).toString());
          message = runCount == maxRuns ? "First " + maxRuns + " displayed of " + runs.size() : "";
          doc.getRootElement().setAttribute("message", message);
          
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString( doc );

        setResponsePage( this.SUCCESS_JSP );

      } else {
        this.addInvalidField( "Insufficient permissions",
        "Insufficient permission to view run list." );
      }
    } catch( NamingException e ) {
      LOG.error( "An exception has occurred in GetRunList ", e );

      throw new RollBackCommandException( e.getMessage() );

    } catch( SQLException e ) {
      LOG.error( "An exception has occurred in GetRunList ", e );

      throw new RollBackCommandException( e.getMessage() );
    } catch( XMLReflectException e ) {
      LOG.error( "An exception has occurred in GetRunList ", e );

      throw new RollBackCommandException( e.getMessage() );
    } catch( Exception e ) {
      LOG.error( "An exception has occurred in GetRunList ", e );

      throw new RollBackCommandException( e.getMessage() );
    } finally {
      try {
        //closeReadOnlyHibernateSession;
      } catch( Exception e ) {

      }
    }

    return this;
  }
  
  private Integer getMaxRuns(Session sess) {
	  Integer maxRuns = DEFAULT_MAX_RUN_COUNT;
	  String prop = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.PLATE_AND_RUN_VIEW_LIMIT);
	  if (prop != null && prop.length() > 0) {
		  try {
			  maxRuns = Integer.parseInt(prop);
	      }
	      catch(NumberFormatException e) {
	      }    
	    }
	    return maxRuns;
  }  

}
