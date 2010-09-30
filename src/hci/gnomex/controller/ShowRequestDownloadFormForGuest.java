package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class ShowRequestDownloadFormForGuest extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowRequestDownloadFormForGuest.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";
  
  private Integer          idRequest;
  private String           serverName;
  private String           baseURL;

  private boolean         createdSecurityAdvisor = false;
  private SecurityAdvisor  secAdvisor = null;
  
  

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }
    
    serverName = request.getServerName();
    
    baseURL =  (request.isSecure() ? "https://" : "http://") + serverName + request.getContextPath();
    
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    try {
      
      sess = HibernateGuestSession.currentGuestSession(getUsername());

      // Get security advisor, create one hasn't already been created for this session.
      secAdvisor = this.getSecAdvisor();
      if (secAdvisor == null) {
        secAdvisor = SecurityAdvisor.createGuest();          
        createdSecurityAdvisor = true;
      }

      // Get the experiment
      Request experiment = (Request)sess.get(Request.class, idRequest);
      if (experiment == null) {
        this.addInvalidField("no experiment", "Request not found");
      }
      

      if (this.isValid()) {
        // Make sure the user can read the experiment
        if (secAdvisor.canRead(experiment)) { 

          // Format an HTML page with links to download the files
          String baseDir = PropertyHelper.getInstance(sess).getMicroarrayDirectoryForReading(serverName);
          String baseDirFlowCell = PropertyHelper.getInstance(sess).getFlowCellDirectory(serverName);
          Document doc = ShowRequestDownloadForm.formatDownloadHTML(sess, experiment, baseDir, baseDirFlowCell, baseURL);
          
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding(true);
          this.xmlResult = out.outputString(doc);
          this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
          this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");

        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to show experiment download form.");
        }

      }

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowRequestDownloadFormForGuest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowRequestDownloadFormForGuest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowRequestDownloadFormForGuest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowRequestDownloadFormForGuest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        if (sess != null) {
          HibernateGuestSession.closeGuestSession();
        }
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

  /**
   *  The callback method called after the loadCommand, and execute methods,
   *  this method allows you to manipulate the HttpServletResponse object prior
   *  to forwarding to the result JSP (add a cookie, etc.)
   *
   *@param  request  The HttpServletResponse for the command
   *@return          The processed response
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    return response;
  } 
  
  /**
   *  The callback method called after the loadCommand and execute methods
   *  allowing you to do any post-execute processing of the HttpSession. Should
   *  be used to add/remove session data resulting from the execution of this
   *  command
   *
   *@param  session  The HttpSession
   *@return          The processed HttpSession
   */
  public HttpSession setSessionState(HttpSession session) {
    if (createdSecurityAdvisor) {
      session.setAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY, secAdvisor);      
    }
    return session;
  }


  
}