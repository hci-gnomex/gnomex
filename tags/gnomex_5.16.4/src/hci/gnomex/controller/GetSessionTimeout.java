package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class GetSessionTimeout extends GNomExCommand implements Serializable {
  
 
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  int maxInactive;
  
  
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    maxInactive = session.getMaxInactiveInterval();
  }

  public Command execute() throws RollBackCommandException {
    this.xmlResult = "<SUCCESS maxInactiveTime=\"" + maxInactive + "\"/>";
    setResponsePage(this.SUCCESS_JSP);
    return this;

  }

}