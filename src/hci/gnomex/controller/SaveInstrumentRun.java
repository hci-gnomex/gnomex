package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
  private String                codeInstrumentRunStatus = null;
  
  private String                disassociatePlates = null;
  
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
    if (request.getParameter("codeInstrumentRunStatus") != null && !request.getParameter("codeInstrumentRunStatus").equals("")) {
      codeInstrumentRunStatus = request.getParameter("codeInstrumentRunStatus");
    }
    if (request.getParameter("runDate") != null && !request.getParameter("runDate").equals("")) {
      runDateStr = request.getParameter("runDate");
    }
    if (request.getParameter("disassociatePlates") != null && !request.getParameter("disassociatePlates").equals("")) {
      disassociatePlates = request.getParameter("disassociatePlates");
    } 
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      InstrumentRun ir;
      
      if(isNew) {

        ir = new InstrumentRun();
        sess.save(ir);
        creator = this.getSecAdvisor().getIdAppUser().toString();
        ir.setCreateDate(new java.util.Date(System.currentTimeMillis()));

      } else {
        ir = (InstrumentRun) sess.get(InstrumentRun.class, idInstrumentRun);
      }
      
      java.util.Date createDate = null;
      if (createDateStr != null) {
        createDate = this.parseDate(createDateStr);
        ir.setCreateDate(createDate);
      }
      
      if ( runDateStr != null ) {ir.setRunDate(this.parseDate(runDateStr));}
      if ( comments != null ) {ir.setComments(comments);}
      if ( label != null ) {ir.setLabel(label);}
      if ( codeReactionType != null ) {ir.setCodeReactionType(codeReactionType);}
      if ( creator != null ) {
        ir.setCreator(creator);
      } else if ( ir.getCreator()==null || ir.getCreator().equals("") ) {
        ir.setCreator( this.getSecAdvisor().getIdAppUser() != null ? this.getSecAdvisor().getIdAppUser().toString() : "" ); 
      }
      if ( codeSealType != null )  {ir.setCodeSealType(codeSealType);}
      
      if ( codeInstrumentRunStatus != null )  {
        ir.setCodeInstrumentRunStatus(codeInstrumentRunStatus);
        if ( codeInstrumentRunStatus.equals( InstrumentRunStatus.RUNNING ) && ir.getRunDate() == null ) {
          ir.setRunDate( new java.util.Date(System.currentTimeMillis()) ); 
        }  
        // Change request status...
        if ( codeInstrumentRunStatus.equals( InstrumentRunStatus.RUNNING ) ) {
          changeRequestStatus( sess, ir, RequestStatus.PROCESSING );
        } else if ( codeInstrumentRunStatus.equals( InstrumentRunStatus.PENDING ) ) {
          changeRequestStatus( sess, ir, RequestStatus.PROCESSING );
        } else if ( codeInstrumentRunStatus.equals( InstrumentRunStatus.COMPLETE ) ){
          changeRequestStatus( sess, ir, RequestStatus.COMPLETED );
        } else if ( codeInstrumentRunStatus.equals( InstrumentRunStatus.FAILED ) ){
          changeRequestStatus( sess, ir, RequestStatus.FAILED );
        } 
        
      }
      
      // Disassociate any plates currently on run.
      if (  disassociatePlates != null && disassociatePlates.equals( "Y" ) ) {
        this.disassociatePlates( sess, ir );
      }
            
      sess.flush();
        
      this.xmlResult = "<SUCCESS idInstrumentRun=\"" + ir.getIdInstrumentRun() + "\"/>";
      
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
  
  private void changeRequestStatus( Session sess, InstrumentRun ir, String status ) {
    
    // Get any requests on that run
    Map requests = new HashMap();
    List wells = sess.createQuery( "SELECT pw from PlateWell as pw " +
        " join pw.plate as plate where plate.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();
    for(Iterator i1 = wells.iterator(); i1.hasNext();) {
      PlateWell well = (PlateWell)i1.next();
      if ( well.getIdRequest()==null ) {
        break;
      }
      if ( !well.getIdRequest().equals( "" ) && !requests.containsKey( well.getIdRequest() ) ) {
        Request req = (Request) sess.get(Request.class, well.getIdRequest());
        requests.put( req.getIdRequest(), req );
      }
    }
    
    // Change request Status 
    for ( Iterator i = requests.keySet().iterator(); i.hasNext();) {
      int idReq = (Integer) i.next();
      Request req = (Request) sess.get(Request.class, idReq );
      req.setCodeRequestStatus( status );
      if ( status.equals( RequestStatus.COMPLETED ) ) {
        if ( req.getCompletedDate() == null ) {
          req.setCompletedDate( new java.sql.Date(System.currentTimeMillis()) );
        }
      }
    }
    sess.flush();
  }
  
  private void disassociatePlates( Session sess, InstrumentRun ir ) {
    
    List plates = sess.createQuery( "SELECT p from Plate as p where p.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();
    
    for(Iterator i1 = plates.iterator(); i1.hasNext();) {
      Plate plate = (Plate)i1.next();
      plate.setIdInstrumentRun( null );
      sess.flush();
    }
   
    
  }
  
}