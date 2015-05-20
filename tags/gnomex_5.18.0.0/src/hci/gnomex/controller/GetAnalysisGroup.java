package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AnalysisGroup;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetAnalysisGroup extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysisGroup.class);
  
  private Integer idAnalysisGroup;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idAnalysisGroup") != null) {
      idAnalysisGroup = new Integer(request.getParameter("idAnalysisGroup"));
    } else {
      this.addInvalidField("idAnalysisGroup", "idAnalysisGroup is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      AnalysisGroup ag = null;
      if (idAnalysisGroup.intValue() == 0) {
        ag = new AnalysisGroup();
        ag.setIdAnalysisGroup(new Integer(0));
      } else {
        ag = (AnalysisGroup)sess.get(AnalysisGroup.class, idAnalysisGroup);
        if (!this.getSecAdvisor().canRead(ag)) {
          this.addInvalidField("permissionerror", "Insufficient permissions to access this analysis Group.");
        } else {
          this.getSecAdvisor().flagPermissions(ag);
          
        }
      }
   
    
      if (isValid())  {
      
        Document doc = new Document(new Element("OpenAnalysisGroupList"));
        Element agNode = ag.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(agNode);
      
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetAnalysisGroup ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetAnalysisGroup ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetAnalysisGroup ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetAnalysisGroup ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetAnalysisGroup ", e);
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

}