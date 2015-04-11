package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Command to create data files for upload. It looks into the export tables and
 * apply the NCCN file format standards. Each data file for one table and the
 * file name should use the table name, the format is text and the file name
 * should end with .txt. Each record takes one line. Use tab to separate the
 * fields. What field data is null, use empty string. See <code>
 * NCCNExportFileGenerator</code>
 * for more detail
 * 
 * @see hci.nccn.util.NCCNExportFileGenerator
 * @author u0007037
 */
public class KeepHttpSession extends GNomExCommand implements Serializable {

  static Logger log = Logger.getLogger(KeepHttpSession.class);
  private String keepSession;
  
  /**
   * The callback method where your business logic should be placed. This method
   * is either called from the FrontController servlet or from the
   * RequestProcessor Session Bean (if EJB is used). Any data resulting from the
   * execution of this method should be put into instance variables in this
   * class.
   * 
   * @return Returns this command with the results of the execute method
   * @exception RollBackCommandException
   *              Description of the Exception
   */
  public Command execute() throws RollBackCommandException {
    // log.debug("Executing execute method in " + this.getClass().getName());
    
    return this;
  }

  /**
   * The callback method in which any pre-processing of the command takes place
   * before the execute method is called. This method is where you would want to
   * load objects from the HttpServletRequest (passed in), do form validation,
   * etc. The HttpSession is also available in this method in case any session
   * data is necessary.
   * 
   * @param request
   *          The HttpServletRequest object
   * @param session
   *          The HttpSession object
   */
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    this.keepSession = request.getParameter("keepSession");
    String whoAndWhy = request.getParameter("whoAndWhy");
    log.debug("keepSession: " + keepSession);
    log.debug("whoAndWhy: " + whoAndWhy);
    if (this.keepSession == null || !this.keepSession.equalsIgnoreCase("true")){
      session.invalidate();
    }else{
      //keep it alive.
    }
    setResponsePage(this.SUCCESS_JSP);
  }

  /**
   * The callback method allowing you to manipulate the HttpServletRequest prior
   * to forwarding to the response JSP. This can be used to put the results from
   * the execute method into the request object for display in the JSP.
   * 
   * @param request
   *          The new requestState value
   * @return Description of the Return Value
   */
  public HttpServletRequest setRequestState(HttpServletRequest request) {
    // log.debug("Executing setRequestState method in " +
    // this.getClass().getName());
    request.setAttribute("xmlResult", this.xmlResult);
    return request;
  }

 
  /**
   * The callback method called after the loadCommand and execute methods
   * allowing you to do any post-execute processing of the HttpSession. Should
   * be used to add/remove session data resulting from the execution of this
   * command
   * 
   * @param session
   *          The HttpSession
   * @return The processed HttpSession
   */
  public HttpSession setSessionState(HttpSession session) {
    // log.debug("Executing setSessionState method in " +
    // this.getClass().getName());

    return session;
  }

  /**
   * The method in which you can do any final validation and add any additional
   * validation entries into the invalidField hashmap, this should be called in
   * the loadCommand prior to setting the response jsp
   */
  public void validate() {
    // log.debug("Executing validate method in " + this.getClass().getName());
  }

}
