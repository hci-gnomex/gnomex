package hci.gnomex.controller;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ContextSensitiveHelp;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

// will need to change this to extend hci framework command if need to use in other projects.
public class UpdateContextSensitiveHelp  extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UpdateContextSensitiveHelp.class);
  
  private String idContextSensitiveHelpString;
  private String context1;
  private String context2;
  private String context3;
  private String helpText;
  private String toolTipText;
  
  public void validate() {
    if(idContextSensitiveHelpString != null && idContextSensitiveHelpString.length() > 0) {
      try {
        Integer id = Integer.parseInt(idContextSensitiveHelpString);
      } catch(NumberFormatException ex) {
        addInvalidField("idContextSensitiveHelp", "Invalid number: " + idContextSensitiveHelpString);
      }
      if (context1 == null || context1.length() == 0) {
        addInvalidField("context1", "context1 required");
      }
    }
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
    idContextSensitiveHelpString = request.getParameter("idContextSensitiveHelp");
    context1 = request.getParameter("context1");
    context2 = request.getParameter("context2");
    context3 = request.getParameter("context3");
    helpText = request.getParameter("helpText");
    toolTipText = request.getParameter("toolTipText");
    validate();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_DICTIONARIES)) {
        ContextSensitiveHelp helpModel = null;
        if (idContextSensitiveHelpString != null && idContextSensitiveHelpString.length() > 0) {
          Integer idContextSensitiveHelp = Integer.parseInt(idContextSensitiveHelpString);
          helpModel = (ContextSensitiveHelp)sess.load(ContextSensitiveHelp.class, idContextSensitiveHelp);
        }
        if (helpText.trim().length() == 0 && toolTipText.trim().length() == 0) {
          // If no help text and no tool tip text and no id, then this is attempt to add empty help text and can be ignored
          // Otherwise delete the existing one.
          if (helpModel != null ) {
            sess.delete(helpModel);
            sess.flush();
          }
        } else {
          if (helpModel == null) {
            helpModel = new ContextSensitiveHelp();
          }
          helpModel.setContext1(context1);
          helpModel.setContext2(context2);
          helpModel.setContext3(context3);
          helpModel.setHelpText(helpText);
          helpModel.setToolTipText(toolTipText);
          
          sess.save(helpModel);
          sess.flush();
        }
        this.xmlResult = "<SUCCESS idContextSensitiveHelp=\"" + helpModel.getIdContextSensitiveHelp() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        addInvalidField("Security", "User must have security to write dictionaries.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in UpdateContextSensitiveHelp ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }


}
