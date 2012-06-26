package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.Sample;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.output.XMLOutputter;




public class ChangeRequestStatus extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeRequestStatus.class);
  
  private String           codeRequestStatus = null;
  private Integer          idRequest = 0;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("codeRequestStatus") != null && !request.getParameter("codeRequestStatus").equals("")) {
      codeRequestStatus = request.getParameter("codeRequestStatus");
    }
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    }
   
  }

  public Command execute() throws RollBackCommandException {
    
    Session sess = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
            
      if ( codeRequestStatus.equals(null) || idRequest.equals("0") ) {
        this.addInvalidField( "Missing information", "id and code request status needed" );
      }
      
      if (this.isValid()) {
        
        
        Request req = (Request) sess.get( Request.class,idRequest );
        req.setCodeRequestStatus( codeRequestStatus );
        
        // If this is a DNA Seq core request, we need to create the billing items when the status changes to
        // submitted
        if (codeRequestStatus.equals(RequestStatus.SUBMITTED) && RequestCategory.isDNASeqCoreRequestCategory(req.getCodeRequestCategory())) {
          if (req.getBillingItems() == null || req.getBillingItems().isEmpty()) {
            createBillingItems(sess, req);
            sess.flush();
          }
        }
        // Set the complete date
        if ( codeRequestStatus.equals(RequestStatus.COMPLETED) ) {
          if ( req.getCompletedDate() == null ) {
            req.setCompletedDate( new java.sql.Date( System.currentTimeMillis() ) );
          }
        }
        sess.flush();
        
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        
        this.xmlResult = "<SUCCESS idRequest=\"" + idRequest + 
             "\" codeRequestStatus=\"" + codeRequestStatus  +
             "\"/>";
      
      }
        
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
      
    } catch (Exception e){
      log.error("An exception has occurred while emailing in ChangeRequestStatus ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.toString());      
    } finally {
      try {
       
        if (sess != null) {
          HibernateSession.closeSession();
        }
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
    
  

  private void createBillingItems(Session sess, Request req) throws Exception {
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    

    // Get the current billing period
    BillingPeriod billingPeriod = dictionaryHelper.getCurrentBillingPeriod();
    if (billingPeriod == null) {
      throw new Exception("Cannot find current billing period to create billing items");
    }

    
    // Hash map of assays chosen.  Build up the map
    Map<String, ArrayList<String>> sampleAssays = new HashMap<String, ArrayList<String>>();
    for (Sample sample : (Set<Sample>)req.getSamples()) {
      Integer idAssay = null;
      for (PlateWell well : (Set<PlateWell>)sample.getWells()) {
        if (well.getIdPrimer() == null && well.getIdAssay() == null) {
          continue;
        }
        if (well.getPlate() == null || !well.getPlate().getCodePlateType().equals(PlateType.SOURCE_PLATE_TYPE)) {
          continue;
        }
        
        if (well.getIdPrimer() != null) {
          idAssay = well.getIdPrimer();
          break;
        } else if (well.getIdAssay() != null) {
          idAssay = well.getIdAssay();
          break;
        }
      }
      if (idAssay == null) {
        continue;
      }
      ArrayList<String> assays = sampleAssays.get(sample.getIdSample());
      if (assays == null) {
        assays = new ArrayList<String>();
        sampleAssays.put(sample.getIdSample().toString(), assays);

      }
      assays.add(idAssay.toString());
    }
    
    
    SaveRequest.createBillingItems(sess, req, null, billingPeriod, dictionaryHelper, req.getSamples(), null, null, null, sampleAssays);
    sess.flush();

  }
  
  
  

}