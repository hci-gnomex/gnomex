package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.InstrumentRun;
import hci.gnomex.model.InstrumentRunStatus;
import hci.gnomex.model.Plate;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.ProductException;
import hci.gnomex.utility.ProductUtil;

import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
public class SaveInstrumentRun extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveInstrumentRun.class);

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

  private String                plateXMLString;

  private String                disassociatePlates = "N";

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
    if (request.getParameter("plateXMLString") != null
        && !request.getParameter("plateXMLString").equals("")) {
      plateXMLString = request.getParameter("plateXMLString");
    }
  }

  public Command execute() throws RollBackCommandException {

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      InstrumentRun ir;

      if (this.getSecurityAdvisor().hasPermission( SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE )) {

        if ( isNew && label != null ) {
          StringBuffer queryBuf = new StringBuffer();
          queryBuf.append("SELECT ir from InstrumentRun as ir WHERE ir.label = '");
          queryBuf.append(label);
          queryBuf.append("'");
          List plates = sess.createQuery(queryBuf.toString()).list();
          if ( plates.size() > 0 ) {
            this.addInvalidField( "Duplicate Run Name", "An instrument run with this name already exists." );
          }
        }

        if ( isValid() ) {
          
          if(isNew) {

            ir = new InstrumentRun();
            sess.save(ir);
            creator = this.getSecAdvisor().getIdAppUser().toString();
            ir.setCreateDate(new java.util.Date(System.currentTimeMillis()));
            ir.setCodeInstrumentRunStatus( InstrumentRunStatus.PENDING );
          } else {
            ir = (InstrumentRun) sess.get(InstrumentRun.class, idInstrumentRun);
          }

          idInstrumentRun = ir.getIdInstrumentRun();

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
            if ( codeInstrumentRunStatus.equals( InstrumentRunStatus.RUNNING ) ||
                codeInstrumentRunStatus.equals( InstrumentRunStatus.PENDING )) {
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

          if (plateXMLString != null) {
            StringReader reader = new StringReader(plateXMLString);
            SAXBuilder sax = new SAXBuilder();
            Document platesDoc = sax.build(reader);
            for (Iterator i = platesDoc.getRootElement().getChildren("Plate").iterator(); i.hasNext();) {
              Element node = (Element) i.next();

              String idPlate = node.getAttributeValue("idPlate");
              String quadrant = node.getAttributeValue( "quadrant" );

              Plate plate = (Plate) sess.get(Plate.class, Integer.valueOf( idPlate ));
              if ( plate != null) {
                plate.setIdInstrumentRun( idInstrumentRun );
                plate.setQuadrant( Integer.valueOf( quadrant ) );
                plate.setCodeSealType( ir.getCodeSealType() );
              }
            }      
          }
          sess.flush();

          List plates = sess.createQuery( "SELECT p from Plate as p where p.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();
          if ( plates.size() == 0 ) {
            sess.delete( ir );
            sess.flush();
            this.xmlResult = "<SUCCESS idInstrumentRun=\"-1\"/>";
          } else {
            this.xmlResult = "<SUCCESS idInstrumentRun=\"" + ir.getIdInstrumentRun() + "\"/>";
          }

          setResponsePage(this.SUCCESS_JSP);
        }
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save run.");
        setResponsePage(this.ERROR_JSP);
      }

    }catch (Exception e){
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in SaveInstrumentRun ", e);
      throw new RollBackCommandException(e.getMessage());  
    }

    return this;
  }

  private void changeRequestStatus( Session sess, InstrumentRun ir, String status ) throws ProductException {

    // Get any requests on that run
    Map<Integer, Request> requests = new HashMap<Integer, Request>();
    List wells = sess.createQuery( "SELECT pw from PlateWell as pw " +
        " join pw.plate as plate where plate.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();
    for(Iterator i1 = wells.iterator(); i1.hasNext();) {
      PlateWell well = (PlateWell)i1.next();
      if ( well.getIdRequest() == null ) {
        continue;
      }
      if ( !well.getIdRequest().equals( "" ) && !requests.containsKey( well.getIdRequest() ) ) {
        Request req = (Request) sess.get(Request.class, well.getIdRequest());
        requests.put( req.getIdRequest(), req );
      }
    }

    HashMap<Integer, Integer> inCompleteRequests = this.getInCompleteRequests(sess, status, requests);

    // Change request Status 
    for ( Iterator i = requests.keySet().iterator(); i.hasNext();) {
      Integer idReq = (Integer) i.next();
      if (!inCompleteRequests.containsKey(idReq)) {
        Request req = (Request) sess.get(Request.class, idReq );
        ProductUtil.updateLedgerOnRequestStatusChange(sess, req, req.getCodeRequestStatus(), status);
        req.setCodeRequestStatus( status );
        if ( status.equals( RequestStatus.COMPLETED ) ) {
          if ( req.getCompletedDate() == null ) {
            req.setCompletedDate( new java.sql.Date(System.currentTimeMillis()) );
          }
          // Now change the billing items for the request from PENDING to COMPLETE
          for (BillingItem billingItem : (Set<BillingItem>)req.getBillingItemList(sess)) {
            if(billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING)){
              billingItem.setCodeBillingStatus(BillingStatus.COMPLETED);
            }
          }
        }
      }
    }
    sess.flush();
  }

  private HashMap<Integer, Integer> getInCompleteRequests(Session sess, String status, Map<Integer, Request> requests) {
    HashMap<Integer, Integer> inCompleteRequests = new HashMap<Integer, Integer>();
    if (status.equals( RequestStatus.COMPLETED)) {
      // Get well information for all requests that are on this run.
      String wellString = "Select plate.codePlateType, well.idRequest, well.idSample, well.redoFlag, well.idAssay, well.idPrimer, run.codeInstrumentRunStatus "
        + " from PlateWell well left join well.plate plate left join plate.instrumentRun run "
        + " where well.idRequest in (:ids) and (well.isControl is null or well.isControl = 'N') ";
      Query wellQuery = sess.createQuery(wellString);
      wellQuery.setParameterList("ids", requests.keySet());
      List wells = wellQuery.list();
      HashMap<Integer, HashMap<String, String>> requestStatusMap = new HashMap<Integer, HashMap<String, String>>();
      HashMap<Integer, Integer> requestRedoMap = new HashMap<Integer, Integer>();
      for(Iterator i = wells.iterator(); i.hasNext(); ) {
        Object[] row = (Object[])i.next();
        String codePlateType = (String)row[0];
        Integer idRequest = (Integer)row[1];
        Integer idSample = (Integer)row[2];
        String redoFlag = (String)row[3];
        Integer idAssay = (Integer)row[4];
        Integer idPrimer = (Integer)row[5];
        String runStatus = (String)row[6];
        Boolean isSourceWell = false;
        if (codePlateType == null || codePlateType.equals(PlateType.SOURCE_PLATE_TYPE)) {
          isSourceWell = true;
        }
        if (redoFlag != null && redoFlag.equals("Y") && isSourceWell) {
          // Right now will not happen, cannot set non-chromo wells to redo.
          requestRedoMap.put(idRequest, idRequest);
        }
        if (!requestRedoMap.containsKey(idRequest)) {
          HashMap<String, String> wellStatusMap = requestStatusMap.get(idRequest);
          if (wellStatusMap == null) {
            wellStatusMap = new HashMap<String, String>();
            requestStatusMap.put(idRequest, wellStatusMap);
          }
          String wellKey = this.getWellStatusKey(idSample, idAssay, idPrimer);
          if (isSourceWell) {
            // This is a source plate.  Add it as incomplete if not there yet.
            if (!wellStatusMap.containsKey(wellKey)) {
              wellStatusMap.put(wellKey, "N");
            }
          } else {
            // Reaction plate.  If on a complete run then complete the well.
            if (runStatus != null && runStatus.equals(InstrumentRunStatus.COMPLETE)) {
              wellStatusMap.put(wellKey, "Y");
            }
          }
        }
      }

      for(Integer idRequest:requests.keySet()) {
        if (requestRedoMap.containsKey(idRequest)) {
          // Redos are always incomplete
          inCompleteRequests.put(idRequest, idRequest);
        } else {
          HashMap<String, String> wellStatusMap = requestStatusMap.get(idRequest);
          if (wellStatusMap == null || wellStatusMap.size() == 0) {
            // Should never happen.
            inCompleteRequests.put(idRequest, idRequest);
          } else {
            for(String value:wellStatusMap.values()) {
              if (value.equals("N")) {
                inCompleteRequests.put(idRequest, idRequest);
                break;
              }
            }
          }
        }
      }
    }

    return inCompleteRequests;
  }

  private String getWellStatusKey(Integer idSample, Integer idAssay, Integer idPrimer) {
    // Cases:
    // Fragment Analysis -- idSample + idAssay defines the well
    // Mitochondrial Sequencing -- idSample + idPrimer defines the well (note this will probably not happen -- done through chromos)
    // Otherwise -- idSample alone defines the well.
    String key = idSample.toString();
    if (idAssay != null) {
      key += "\t" + idAssay.toString();
    }
    if (idPrimer != null) {
      key += "\t" + idPrimer.toString();
    }

    return key;
  }

  private void disassociatePlates( Session sess, InstrumentRun ir ) {

    List plates = sess.createQuery( "SELECT p from Plate as p where p.idInstrumentRun =" + ir.getIdInstrumentRun() ).list();

    for(Iterator i1 = plates.iterator(); i1.hasNext();) {
      Plate plate = (Plate)i1.next();
      plate.setIdInstrumentRun( null );
      plate.setQuadrant( -1 );
      sess.flush();
    }


  }

}
