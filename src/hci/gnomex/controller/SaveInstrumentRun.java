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
  private String                createDateStr = null;
  
  private String                runDateStr = null;
  private String                comments = null;
  private String                label = null;
  private String                codeReactionType = null;
  private String                creator = null;
  private String                codeSealType = null;
  
  //Variable to indicate which run from the plate editor page (up to 4 runs can be submitted at a time)
  private int                   runNumber = 0;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idInstrumentRun") != null && !request.getParameter("idInstrumentRun").equals("")) {
      idInstrumentRun = Integer.parseInt(request.getParameter("idInstrumentRun"));
      isNew = false;
    }
    if (request.getParameter("createDate") != null && !request.getParameter("createDate").equals("")) {
      createDateStr = request.getParameter("createDate");
    }
    
    if (request.getParameter("runNumber") != null && !request.getParameter("runNumber").equals("")) {
      runNumber = Integer.parseInt(request.getParameter("runNumber"));
    }
    
    if (request.getParameter("comments") != null && !request.getParameter("comments").equals("")) {
      comments = request.getParameter("comments");
    } 
    if (request.getParameter("label") != null && !request.getParameter("label").equals("")) {
      label = request.getParameter("label");
    } 
    if (request.getParameter("codeReactionType") != null && !request.getParameter("codeReactionType").equals("")) {
      codeReactionType = request.getParameter("codeReactionType");
    }
    if (request.getParameter("creator") != null && !request.getParameter("creator").equals("")) {
      creator = request.getParameter("creator");
    } 
    if (request.getParameter("codeSealType") != null && !request.getParameter("codeSealType").equals("")) {
      codeSealType = request.getParameter("codeSealType");
    }
    if (request.getParameter("runDate") != null && !request.getParameter("runDate").equals("")) {
      runDateStr = request.getParameter("runDate");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      InstrumentRun ir;
      
      if(isNew) {
        // Should set creator to the current user
        ir = new InstrumentRun();
        sess.save(ir);
      } else {
        ir = (InstrumentRun) sess.get(InstrumentRun.class, idInstrumentRun);
      }
      
      java.util.Date createDate = null;
      if (createDateStr != null) {
        createDate = this.parseDate(createDateStr);
      }
      ir.setCreateDate(createDate != null ? createDate : new java.util.Date(System.currentTimeMillis()));
      
      if ( runDateStr != null ) {ir.setRunDate(this.parseDate(runDateStr));}
      if ( comments != null ) {ir.setComments(comments);}
      if ( label != null ) {ir.setLabel(label);}
      if ( codeReactionType != null ) {ir.setCodeReactionType(codeReactionType);}
//      if ( creator != null ) {ir.setCreator(creator);}
      if ( codeSealType != null )  {ir.setCodeSealType(codeSealType);}
        
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