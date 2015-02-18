package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PlateFilter;
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


public class GetPlateList extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetPlateList.class);

  private PlateFilter          plateFilter;
  private String               listKind = "PlateList";
  private Element              rootNode = null;
  private String               message = "";
  
  private static final int	   DEFAULT_MAX_PLATE_COUNT = 200;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    plateFilter = new PlateFilter();
    HashMap errors = this.loadDetailObject(request, plateFilter);
    this.addInvalidFields(errors);

    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");

    }

  }

  public Command execute() throws RollBackCommandException {

    try {
      if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

        Document doc = new Document(new Element(listKind));

        if (!plateFilter.hasSufficientCriteria(this.getSecAdvisor())) {
          message = "Please select a filter";
        } else {
          Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

          StringBuffer buf = plateFilter.getQuery(this.getSecAdvisor());
          log.info("Query for GetPlateList: " + buf.toString());
          List plates = sess.createQuery(buf.toString()).list();
          
          Integer maxPlates = getMaxPlates(sess);
          int          plateCount = 0;
          for(Iterator i = plates.iterator(); i.hasNext();) {

            Object[] row = (Object[])i.next();

            Integer idPlate          = row[0] == null ? new Integer(0) : (Integer)row[0];
            Integer idInstrumentRun  = row[1] == null ? new Integer(0) : (Integer)row[1];
            Integer quadrant         = row[2] == null ? new Integer(0) : (Integer)row[2];
            String  createDate       = this.formatDate((java.sql.Timestamp)row[3]);
            String  comments         = row[4] == null ? "" : row[4].toString();
            String  label            = row[5] == null ? "" : row[5].toString();
            String  codeReactionType = row[6] == null ? "" : row[6].toString();
            String  creator          = row[7] == null ? "" : row[7].toString();
            String  codeSealType     = row[8] == null ? "" : row[8].toString();
            String  codePlateType    = row[9] == null ? "" : row[9].toString();

            Element pNode = new Element("Plate");
            pNode.setAttribute("idPlate", idPlate.toString());
            pNode.setAttribute("idInstrumentRun", idInstrumentRun.toString());
            pNode.setAttribute("quadrant", quadrant.toString());
            pNode.setAttribute("createDate", createDate);
            pNode.setAttribute("comments", comments);
            pNode.setAttribute("label", label);
            pNode.setAttribute("codeReactionType", codeReactionType);
            if ( creator != null && !creator.equals( "" ) ) {
              AppUser user = (AppUser)sess.get(AppUser.class, Integer.valueOf(creator));
              pNode.setAttribute( "creator", user != null ? user.getDisplayName() : creator);
            } else {
              pNode.setAttribute( "creator", creator);
            }

            pNode.setAttribute("codeSealType", codeSealType);
            pNode.setAttribute("codePlateType", codePlateType);
            pNode.setAttribute( "icon", ReactionType.getIcon(codeReactionType));

            doc.getRootElement().addContent(pNode);

            plateCount++;
            if (plateCount >= maxPlates) {
                break;
            }
          }
          
          doc.getRootElement().setAttribute("plateCount", Integer.valueOf(plateCount).toString());
          message = plateCount == maxPlates ? "First " + maxPlates + " displayed of " + plates.size() : "";
          doc.getRootElement().setAttribute("message", message);
          
        }


        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);

      } else {
        this.addInvalidField( "Insufficient permissions",
        "Insufficient permission to view plate list." );
      }
    }catch (NamingException e){
      log.error("An exception has occurred in GetPlateList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in GetPlateList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetPlateList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetPlateList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {

      }
    }

    return this;
  }
  
  private Integer getMaxPlates(Session sess) {
	  Integer maxPlates = DEFAULT_MAX_PLATE_COUNT;
	  String prop = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.PLATE_AND_RUN_VIEW_LIMIT);
	  if (prop != null && prop.length() > 0) {
		  try {
			  maxPlates = Integer.parseInt(prop);
	      }
	      catch(NumberFormatException e) {
	      }    
	    }
	    return maxPlates;
  }

}