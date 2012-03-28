package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SaveInstrumentRun extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveInstrumentRun.class);
  
  private int                   idInstrumentRun;
  private boolean               isNew = true;
  private String                runDateStr = null;
  
  //Variable to indicate which run from the plate editor page (up to 4 runs can be submitted at a time)
  private int                   runNumber = 0;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idInstrumentRun") != null && !request.getParameter("idInstrumentRun").equals("")) {
      idInstrumentRun = Integer.parseInt(request.getParameter("idInstrumentRun"));
      isNew = false;
    }
    if (request.getParameter("runDate") != null && !request.getParameter("runDate").equals("")) {
      runDateStr = request.getParameter("runDate");
    }
    
    if (request.getParameter("runNumber") != null && !request.getParameter("runNumber").equals("")) {
      runNumber = Integer.parseInt(request.getParameter("runNumber"));
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      InstrumentRun ir;
      
      if(isNew) {
        ir = new InstrumentRun();
        sess.save(ir);
      } else {
        ir = (InstrumentRun) sess.get(InstrumentRun.class, idInstrumentRun);
      }
      
      java.util.Date runDate = null;
      if (runDateStr != null) {
        runDate = this.parseDate(runDateStr);
      }
      ir.setRunDate(runDate != null ? runDate : new java.util.Date(System.currentTimeMillis()));
      
        
      sess.flush();
        
      this.xmlResult = "<SUCCESS idInstrumentRun=\"" + ir.getIdInstrumentRun() + "\" runNumber=\"" + runNumber + "\"/>";
      
      setResponsePage(this.SUCCESS_JSP);
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveInstrumentRun ", e);
      e.printStackTrace();
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