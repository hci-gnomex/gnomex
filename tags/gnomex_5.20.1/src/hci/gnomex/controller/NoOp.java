package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 *
 *@author
 *@created
 *@version    1.0
 * Generated by the CommandBuilder tool - Kirt Henrie
 */

public class NoOp extends GNomExCommand implements Serializable {

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NoOp.class);
  
  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

  }

  public Command execute() throws RollBackCommandException {
    log.debug("executing NoOp.execute");
    this.xmlResult = "<NoOp/>";
    return this;
  }
}
