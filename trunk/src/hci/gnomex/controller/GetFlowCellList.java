package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.FlowCell;
import hci.gnomex.security.SecurityAdvisor;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetFlowCellList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetFlowCellList.class);
  
  private String   codeSequencingPlatform;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("codeSequencingPlatform") != null && !request.getParameter("codeSequencingPlatform").equals("")) {
      codeSequencingPlatform = request.getParameter("codeSequencingPlatform");
    } else {
      this.addInvalidField("codeSequencingPlatform", "Sequencing platform is required.");
    }

  }

  public Command execute() throws RollBackCommandException {
    
    if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
      try {
        
        
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
        StringBuffer buf =  new StringBuffer("SELECT fc from FlowCell as fc ");
        buf.append(" WHERE codeSequencingPlatform ='" + codeSequencingPlatform + "'");
        buf.append(" ORDER by fc.createDate desc");
        List flowCells = (List)sess.createQuery(buf.toString()).list();
        
        Document doc = new Document(new Element("FlowCellList"));
        for(Iterator i = flowCells.iterator(); i.hasNext();) {
          FlowCell fc = (FlowCell)i.next();
          
          
          Element fcNode = fc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
          
          doc.getRootElement().addContent(fcNode);
          
        }
        
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        
        setResponsePage(this.SUCCESS_JSP);
        }catch (NamingException e){
          log.error("An exception has occurred in GetFlowCellList ", e);
          e.printStackTrace();
          throw new RollBackCommandException(e.getMessage());
        }catch (SQLException e) {
          log.error("An exception has occurred in GetFlowCellList ", e);
          e.printStackTrace();
          throw new RollBackCommandException(e.getMessage());
        } catch (XMLReflectException e){
          log.error("An exception has occurred in GetFlowCellList ", e);
          e.printStackTrace();
          throw new RollBackCommandException(e.getMessage());
        } catch (Exception e){
          log.error("An exception has occurred in GetFlowCellList ", e);
          e.printStackTrace();
          throw new RollBackCommandException(e.getMessage());
        } finally {
          try {
            this.getSecAdvisor().closeReadOnlyHibernateSession();        
          } catch(Exception e) {
            
          }
        }
        setResponsePage(this.SUCCESS_JSP);
      
    } else {
      this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow.");
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }

}