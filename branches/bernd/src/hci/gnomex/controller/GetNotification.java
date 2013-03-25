package hci.gnomex.controller;

import hci.gnomex.model.NotificationFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

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


public class GetNotification extends GNomExCommand implements Serializable {
  
  
private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetNotification.class);
  
  private NotificationFilter filter;
  private Integer workflowCoreFacility = null;
  
  public void validate() {
	  
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
	
    filter = new NotificationFilter();
    filter.setIdUserTarget(this.getSecAdvisor().getIdAppUser());
    HashMap errors = this.loadDetailObject(request, filter);
    if(request.getParameter("workflowCoreFacility") != null){
    	workflowCoreFacility = Integer.parseInt(request.getParameter("workflowCoreFacility"));	
    }
    
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
   
	try {
    	
    	Boolean adminAuth = this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_RECEIVE_ADMIN_NOTIFICATION);
    	Boolean billingAuth = this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_RECEIVE_BILLING_NOTIFICATION);
    	Boolean workflowAuth = this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_RECEIVE_WORKFLOW_NOTIFICATION);
    	
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
        
        // Process filter calling here 
        Document doc = new Document(new Element("NotificationCollection"));
        
        StringBuffer queryBuf = filter.getQuery(this.getSecAdvisor());
        List rows = (List) sess.createQuery(queryBuf.toString()).list();
        
        // Foreach step in permission level (user, admin, billing, workflow) create subnodes.
        // Get sourceType and determine parsing type.
        Element x = null;
        for (Iterator<Object[]> i1 = rows.iterator(); i1.hasNext();) {
            Object[] row = (Object[]) i1.next();
            x = null;
            String sourceType = (String)row[1].toString();
            if(sourceType.equals("ADMIN")){
            	if(adminAuth){
            		// Set admin level notification node
            		x = new Element("admin");
            	}
            }else if(sourceType.equals("USER")){
            	// Set user level notification node
            	x = new Element("user");
            }else if(sourceType.equals("BILLING")){
            	if(billingAuth){
            		// Set billing level notification node
            		if(billingAuth){
            			x = new Element("billing");
            		}
            	}	
            }else{ // Unknown
            	x = new Element("Unknown");
            }
            
    		x.setAttribute("notificationId",	row[0] == null ? "0" : ((Integer)row[0]).toString());
    		x.setAttribute("sourceType",		row[1] == null ? "" : sourceType);						// Should not be undef.
    		x.setAttribute("state", 			row[2] == null ? "" : (String)row[2]);
    		x.setAttribute("date",				row[3] == null ? "" : this.formatDate((java.util.Date)row[3]));
    		x.setAttribute("idTargetUser", 		row[4] == null ? "" : ((Integer)row[4]).toString());
    		x.setAttribute("idTargetLab",		row[5] == null ? "" : ((Integer)row[5]).toString());
    		x.setAttribute("expId", 			row[6] == null ? "" : ((Integer)row[6]).toString());
    		x.setAttribute("type", 				row[7] == null ? "" : (String)row[7]);
    		x.setAttribute("fullNameUser", 		row[8] == null ? "" : (String)row[8]);
            
            // Add node content to rootElement XML output.
            if(row[0] != null){
            	doc.getRootElement().addContent(x);	
            }
        }
  	  
        // If authorized for workflow management 
        	if(workflowAuth){
                StringBuffer buf = new StringBuffer();
                // Build the workflow samples
                buf.append("SELECT wi.codeStepNext, count(wi.codeStepNext)");
                buf.append(" FROM  Sample s ");
          	  	buf.append(" JOIN    s.request as req ");
          	  	buf.append(" LEFT JOIN	 s.workItems wi ");
          	  	
          	  	
          	  	if(workflowCoreFacility != null){
          	  		buf.append(" WHERE req.idCoreFacility='");
          	  		buf.append(workflowCoreFacility);
          	  		buf.append("'");
          	  	}
          	  	
          	  	buf.append(" GROUP BY wi.codeStepNext ");
          	  	
                rows = sess.createQuery(buf.toString()).list();
                if (rows.size() > 0) {
                  for(Iterator i = rows.iterator(); i.hasNext();) {
                    Object[] row = (Object[])i.next();
                    x = new Element("workflow");
                    
                    x.setAttribute("codeStep", 	row[0] == null ? "" : (String)row[0]);
                    x.setAttribute("count", 	row[1] == null ? "" : ((Integer)row[1]).toString());
                    x.setAttribute("sourceType", "WORKFLOW");
                    x.setAttribute("codeStepName", dictionaryHelper.getCodeStepName(x.getAttributeValue("codeStep")));
                    
                    if((String)row[0] != null){
                    	doc.getRootElement().addContent(x);
                    }
                  }
                }
        	}
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
        
        // Send redirect with response SUCCESS.
        setResponsePage(this.SUCCESS_JSP);
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetNotification ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetNotification ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetNotification ", e);
      e.printStackTrace(System.out);
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