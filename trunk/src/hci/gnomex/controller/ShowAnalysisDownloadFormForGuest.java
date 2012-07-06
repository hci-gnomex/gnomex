package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Analysis;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;


public class ShowAnalysisDownloadFormForGuest extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowAnalysisDownloadFormForGuest.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";
  
  private Integer          idAnalysis;
  private String           serverName;
  private String           baseURL;
  
  private DictionaryHelper dictionaryHelper;
  
  private boolean         createdSecurityAdvisor = false;
  private SecurityAdvisor  secAdvisor = null;
  
  

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idAnalysis") != null) {
      idAnalysis = new Integer(request.getParameter("idAnalysis"));
    } else {
      this.addInvalidField("idAnalysis", "idAnalysis is required");
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

      // Get the analysis
      Analysis analysis = (Analysis)sess.get(Analysis.class, idAnalysis);
      if (analysis == null) {
        this.addInvalidField("no analysis", "Analysis not found");
      }
      

      if (this.isValid()) {
        // Make sure user is authorized to read analysis
        if (secAdvisor.canRead(analysis)) { 
          
          // Format an HTML page with the download links for this analysis
          String baseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(serverName);
          Document doc = ShowAnalysisDownloadForm.formatDownloadHTML(analysis,secAdvisor, baseDir, baseURL);
          
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding(true);
          this.xmlResult = out.outputString(doc);
          this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
          this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");

        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to show analysis download form.");
        }

      }

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowAnalysisDownloadFormForGuest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowAnalysisDownloadFormForGuest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowAnalysisDownloadFormForGuest ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowAnalysisDownloadFormForGuest ", e);
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