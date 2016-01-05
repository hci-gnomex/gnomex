package hci.gnomex.controller;

import hci.gnomex.model.NotificationFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
  private Integer idCoreFacility = null;
  
  public void validate() {
	  
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
	
    filter = new NotificationFilter();
 //   filter.setIdUserTarget(this.getSecAdvisor().getIdAppUser());
    HashMap errors = this.loadDetailObject(request, filter);
    if(request.getParameter("workflowCoreFacility") != null){
    	workflowCoreFacility = Integer.parseInt(request.getParameter("workflowCoreFacility"));	
    }
    if(request.getParameter("idCoreFacility") != null){
      idCoreFacility = Integer.parseInt(request.getParameter("idCoreFacility"));  
    }
    
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
   
	try {
	//	if(isValid()){
	  
	    	Boolean adminAuth = this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_RECEIVE_ADMIN_NOTIFICATION);
	    	Boolean billingAuth = this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_RECEIVE_BILLING_NOTIFICATION);
	    	Boolean workflowAuth = this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_RECEIVE_WORKFLOW_NOTIFICATION);
	    	
	        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
	        DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
	        
	        // Process filter calling here 
	        Document doc = new Document(new Element("NotificationCollection"));
	        filter.setIdCoreFacility(idCoreFacility);
	        
	        StringBuffer queryBuf = filter.getQuery(this.getSecAdvisor());
	        List rows = (List) sess.createQuery(queryBuf.toString()).list();
	        
	        // Foreach step in permission level (user, admin, billing, workflow) create subnodes.
	        // Get sourceType and determine parsing type.
	        Element x = null;
	        
	        Integer idUser = this.getSecAdvisor().getIdAppUser();
	        
	      for (Iterator<Object[]> i1 = rows.iterator(); i1.hasNext();) {
	            Object[] row = (Object[]) i1.next();
	            x = null;
	            String sourceType = (String)row[1].toString();
	
	            if(sourceType.equals("ADMIN")){
	            	if(adminAuth){
	            		// Set admin level notification node
	            		x = new Element("admin");
	            	}else{
	            		continue;
	            	}
	            }else if(sourceType.equals("USER")){
	            	// Set user level notification node
	            	Integer uid = null;
	            	
	            	if((Integer) row[4] != null){
	            		uid = (Integer) row[4];
	            	}
	            	
	            	if(uid.equals(idUser)){
	            		x = new Element("user");
	            	}else{
	            		continue;
	            	}
	            }else if(sourceType.equals("BILLING")){
	            	if(billingAuth){
	            		// Set billing level notification node
	           			x = new Element("billing");
	            	}else{
	            		continue;
	            	}
	            }else{ // Unknown
	            	x = new Element("Unknown");
	            }
	            
          Date date = (Date)row[3];
          DateFormat df = new SimpleDateFormat("H:mm");
          String time = "";
          if(date != null){
            time = df.format(date);
          }
          
	    		x.setAttribute("notificationId",	row[0] == null ? "0" : ((Integer)row[0]).toString());
	    		x.setAttribute("sourceType",		row[1] == null ? "" : sourceType);						// Should not be undef.
	    		x.setAttribute("state", 			row[2] == null ? "" : (String)row[2]);
	    		x.setAttribute("date",				row[3] == null ? "" : this.formatDate((java.util.Date)row[3]));
	    		x.setAttribute("time",       time);
	    		x.setAttribute("idTargetUser", 		row[4] == null ? "" : ((Integer)row[4]).toString());
	    		x.setAttribute("idTargetLab",		row[5] == null ? "" : ((Integer)row[5]).toString());
	    		x.setAttribute("expId", 			row[6] == null ? "" : (String)row[6]);
	    		x.setAttribute("type", 				row[7] == null ? "" : (String)row[7]);
	    		x.setAttribute("fullNameUser", 		row[8] == null ? "" : (String)row[8]);
	    		x.setAttribute("imageSource",     row[9] == null ? "" : (String)row[9]);
	    		x.setAttribute("idCoreFacility",     row[10] == null ? "" : ((Integer)row[10]).toString());
	           
	            // Add node content to rootElement XML output.
	            if(row[0] != null){
	            	doc.getRootElement().addContent(x);	
	            }
	        }
	
	        // If authorized for workflow management show all work flow items in each of the steps.
	        	if(workflowAuth){
	                StringBuffer buf = new StringBuffer();
	                // Build the workflow samples
	                buf.append("SELECT wi.codeStepNext, max(wi.createDate), count(wi.codeStepNext)");
	                buf.append(" FROM  WorkItem wi ");
//	                buf.append(" FROM  Sample s ");
//	          	  	buf.append(" JOIN    s.request as req ");
//	          	  	buf.append(" LEFT JOIN	 s.workItems wi ");
	          	  	
	          	  	if(workflowCoreFacility != null){
	          	  		buf.append(" WHERE req.idCoreFacility='");
	          	  		buf.append(workflowCoreFacility);
	          	  		buf.append("'");
	          	  	}
	          	  	
	          	  	buf.append(" GROUP BY wi.codeStepNext, wi.createDate ");
	          	  	buf.append(" ORDER BY wi.createDate DESC");
	          	  	
	                rows = sess.createQuery(buf.toString()).list();
	                if (rows.size() > 0) {
	                  for(Iterator i = rows.iterator(); i.hasNext();) {
	                    Object[] row = (Object[])i.next();
	                    x = new Element("workflow");
	                    
	                    x.setAttribute("codeStep", 	row[0] == null ? "" : (String)row[0]);
	                    x.setAttribute("count", 	row[2] == null ? "" : ((Long)row[2]).toString());
	                    x.setAttribute("date",   row[1] == null ? "" : this.formatDate((java.util.Date)row[1]));
//	                    x.setAttribute("sourceType", "WORKFLOW");
	                    x.setAttribute("codeStepName", dictionaryHelper.getCodeStepName(x.getAttributeValue("codeStep")));
	                    
	                    if((String)row[0] != null){
	                    	doc.getRootElement().addContent(x);
	                    }
	                  }
	                }
	        	}
	        	
	        	//Get workflow items for specific user
	        	StringBuffer buf = new StringBuffer();
	        	buf.append("SELECT wi.codeStepNext, wi.createDate, s.number, r.number");
	        	buf.append(" FROM WorkItem wi ");
	        	buf.append(" JOIN wi.sample as s");
	        	buf.append(" JOIN s.request as r");
	        	buf.append(" Where r.idAppUser = " + idUser);
            buf.append(" ORDER BY wi.createDate DESC");
	        	
	        	rows = sess.createQuery(buf.toString()).list();
	        	if (rows.size() > 0) {
              for(Iterator i = rows.iterator(); i.hasNext();) {
                Object[] row = (Object[])i.next();
                x = new Element("userWorkflow");
                
                x.setAttribute("codeStep",  row[0] == null ? "" : (String)row[0]);
                x.setAttribute("date",   row[1] == null ? "" : this.formatDate((java.util.Date)row[1]));
                x.setAttribute("sampleNumber",   row[2] == null ? "" : (String)row[2]);
                x.setAttribute("requestNumber", row[3] == null ? "" : (String)row[3]);
                x.setAttribute("sourceType", "USER");
                x.setAttribute("codeStepName", dictionaryHelper.getCodeStepName(x.getAttributeValue("codeStep")));
                
                if((String)row[0] != null){
                  doc.getRootElement().addContent(x);
                }
              }
            }
	        	
	        XMLOutputter out = new org.jdom.output.XMLOutputter();
	        this.xmlResult = out.outputString(doc);
        
         // Send redirect with response SUCCESS or ERROR page.
         if (isValid()) {
            setResponsePage(this.SUCCESS_JSP);
          } else {
            setResponsePage(this.ERROR_JSP);
         }
	//	}
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