package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.IScanChip;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PlateType;
import hci.gnomex.model.PlateWell;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.Sample;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.EmailHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequestEmailBodyFormatter;
import hci.gnomex.utility.RequisitionFormUtil;
import hci.gnomex.utility.Util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class ChangeRequestStatus extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ChangeRequestStatus.class);
  
  private String           codeRequestStatus = null;
  private Integer          idRequest = 0;
  private String           launchAppURL;
  private String           appURL;
  private String           serverName;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    try {
      launchAppURL = this.getLaunchAppURL(request);  
      appURL = this.getAppURL(request);
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveRequest", e);
    }
    serverName = request.getServerName();

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
            
      if ( codeRequestStatus == null || idRequest.equals("0") ) {
        this.addInvalidField( "Missing information", "id and code request status needed" );
      }
      
      if (this.isValid()) {
        
        Request req = (Request) sess.get( Request.class,idRequest );
        String oldRequestStatus = req.getCodeRequestStatus();

        req.setCodeRequestStatus( codeRequestStatus );

        // SUBMITTED
        if ( oldRequestStatus!=null ) {
          if (oldRequestStatus.equals(RequestStatus.NEW) && codeRequestStatus.equals(RequestStatus.SUBMITTED)) {
            req.setCreateDate(new java.util.Date());
            
            String otherRecipients = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.REQUEST_SUBMIT_CONFIRMATION_EMAIL);
            if ((req.getAppUser() != null
                  && req.getAppUser().getEmail() != null
                  && !req.getAppUser().getEmail().equals(""))
                || (otherRecipients != null && otherRecipients.length() > 0)) {
              try {
                // confirmation email for dna seq requests is sent at submit time.
                sendConfirmationEmail(sess, req, otherRecipients);
              } catch (Exception e) {
                String msg = "Unable to send confirmation email notifying submitter that request "
                  + req.getNumber()
                  + " has been submitted.  " + e.toString();
                log.error(msg);
              }
            } else {
              String msg = ( "Unable to send confirmation email notifying submitter that request "
                  + req.getNumber()
                  + " has been submitted.  Request submitter or request submitter email is blank.");
              log.error(msg);
            }
            
            // For INTERNAL ISCAN Requests
            if (req.getCodeRequestCategory().equals(RequestCategory.ISCAN_REQUEST_CATEGORY) && !req.getLab().isExternalLab() ) {
              
              // Bypass requisition form and illumina email for CUSTOM orders and for internal HCI labs.
              IScanChip chip = (IScanChip) sess.get( IScanChip.class, req.getIdIScanChip() );
              boolean isHCI = req.getLab().getContactEmail().indexOf( "@hci.utah.edu" ) > 0;
              
              if ( chip != null && req.getNumberIScanChips()!=0 && !isHCI ) {

                // REQUISITION FORM
                try {
                  // Download and fill out requisition form
                  File reqFile = RequisitionFormUtil.saveReqFileFromURL(req, sess, serverName);
                  reqFile = RequisitionFormUtil.populateRequisitionForm( req, reqFile, sess );
                  if ( reqFile == null ) {
                    String msg = "Unable to download requisition form for request " + req.getNumber() + "." ;
                    log.error(msg);
                  } else {
                    // If we already have the quote number, send requisition form and quote number to purchasing contact
                    if ( req.getMaterialQuoteNumber() != null && !req.getMaterialQuoteNumber().equals( "" ) ) {
                      UploadQuoteInfoServlet.sendPurchasingEmail(sess, req, serverName);
                    }
                  }

                } catch (Exception e) {
                  String msg = "Unable to download requisition form OR unable to send purchasing email for Request "
                    + req.getNumber()
                    + ".  " + e.toString();
                  log.error(msg);
                  e.printStackTrace();
                }
                // ILLUMINA EMAIL
                // Send email to illumina contact requesting quote #
                if ( req.getMaterialQuoteNumber() == null || req.getMaterialQuoteNumber().equals( "" ) ) {
                  try {
                    sendIlluminaEmail(sess, req);
                  } catch (Exception e) {
                    String msg = "Unable to send Illumina IScan Chip email requesting a quote number for request "
                      + req.getNumber()
                      + ".  " + e.toString();
                    log.error(msg);
                    e.printStackTrace();
                  }
                }
              }
            } 
          }
        }
        
        // If this is a DNA Seq core request, we need to create the billing items and send confirmation email 
        // when the status changes to submitted
        if ( (codeRequestStatus.equals(RequestStatus.SUBMITTED)||codeRequestStatus.equals(RequestStatus.PROCESSING)) && RequestCategory.isDNASeqCoreRequestCategory(req.getCodeRequestCategory())) {
          if (req.getBillingItems() == null || req.getBillingItems().isEmpty()) {
            createBillingItems(sess, req);
            sess.flush();
          }
        }
        
        // COMPLETE
        // Set the complete date
        if ( codeRequestStatus.equals(RequestStatus.COMPLETED) ) {
          if ( req.getCompletedDate() == null ) {
            req.setCompletedDate( new java.sql.Date( System.currentTimeMillis() ) );
          }
          // Now change the billing items for the request from PENDING to COMPLETE
          for (BillingItem billingItem : (Set<BillingItem>)req.getBillingItems()) {
            if(billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING)){
              billingItem.setCodeBillingStatus(BillingStatus.COMPLETED);
            }
          }
          
          // Send a confirmation email

          try {
            EmailHelper.sendConfirmationEmail(sess, req, this.getSecAdvisor(), launchAppURL, appURL, serverName);                  
          } catch (Exception e) {
            log.error("Unable to send confirmation email notifying submitter that request " + req.getNumber() + 
                " is complete. " + e.toString());
          }
          
        }
        
        // PROCCESSING
        if (codeRequestStatus.equals(RequestStatus.PROCESSING)) {
          if (req.getProcessingDate() == null) {
            req.setProcessingDate( new java.sql.Date( System.currentTimeMillis() ) );
          }
        }
        
        sess.flush();

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
      log.error("An exception has occurred in ChangeRequestStatus ", e);
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
      sample.setIdSampleString(sample.getIdSample().toString());
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
  
  
  private void sendConfirmationEmail(Session sess, Request req, String otherRecipients) throws NamingException, MessagingException {
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    
    StringBuffer introNote = new StringBuffer();
    String trackRequestURL = Util.addURLParameter(launchAppURL, "?requestNumber=" + req.getNumber() + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS);
    CoreFacility cf = (CoreFacility)sess.load(CoreFacility.class, req.getIdCoreFacility());
    
    introNote.append("Experiment request " + req.getNumber() + " has been submitted to the " + cf.getFacilityName() + 
      ".  You will receive email notification when the experiment is complete.");   
    
    // Special notes for iScan requests
    if (req.getCodeRequestCategory().equals(RequestCategory.ISCAN_REQUEST_CATEGORY)){
      if ( req.getNumberIScanChips()==0 ) {
        introNote.append("<br><br><b><FONT COLOR=\"#ff0000\">Please note that this is an order with a custom number of samples and/or iScan chips.  An email has not been sent to the Illumina rep requesting a quote number for purchasing chips and a requisition form has not been downloaded.</FONT></b>");   
      } else {
        Lab lab = dictionaryHelper.getLabObject( req.getIdLab() );
        String email = lab.getContactEmail();
        if ( email.indexOf( "@hci.utah.edu" ) > 0 ) {
          introNote.append("<br><br><b><FONT COLOR=\"#ff0000\">Please note that this is an order from an internal HCI lab.  An email has not been sent to the Illumina rep requesting a quote number for purchasing chips and a requisition form has not been downloaded.</FONT></b>");   
        }
      }
    }
  
    introNote.append("<br><br>To track progress on the experiment request, click <a href=\"" + trackRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");
       
    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, this.getSecAdvisor(), appURL, dictionaryHelper, req, "", req.getSamples(), new HashSet(), new HashSet(), introNote.toString());
    String subject = dictionaryHelper.getRequestCategory(req.getCodeRequestCategory()) + 
                  (req.getIsExternal().equals("Y") ? " Experiment " : " Experiment Request ") + 
                  req.getNumber() + (req.getIsExternal().equals("Y") ? " registered" : " submitted");
    
    
    String contactEmailCoreFacility = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(req.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    String contactEmailSoftwareBugs = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(req.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS);
    String emailRecipients = "";
    if (req.getAppUser() != null && req.getAppUser().getEmail() != null) {
      emailRecipients = req.getAppUser().getEmail();
    }
    
    if(!MailUtil.isValidEmail(emailRecipients)){
      log.error("Invalid email address " + emailRecipients);
    }
    
    if (otherRecipients != null && otherRecipients.length() > 0) {
      if (emailRecipients.length() > 0) {
        emailRecipients += ",";
      }
      emailRecipients += otherRecipients;
    }
    
    for(String e : emailRecipients.split(",")){
      if(!MailUtil.isValidEmail(e.trim())){
        log.error("Invalid email address " + e);  
      }
    }
    
    boolean send = false;
    String emailInfo = "";
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      send = true;
      subject = subject + "  (TEST)";
      emailInfo = "[If this were a production environment then this email would have been sent to: " + emailRecipients + "]<br><br>";
      emailRecipients = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
    }
    
    
    if (send) {
      String fromAddress = req.getIsExternal().equals("Y") ? contactEmailSoftwareBugs : contactEmailCoreFacility;
      if(!MailUtil.isValidEmail(fromAddress)){
        fromAddress = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      MailUtil.send(emailRecipients, 
          null,
          fromAddress, 
          subject, 
          emailInfo + emailFormatter.format(),
          true);      
    }
    
  }
  
  private boolean sendIlluminaEmail(Session sess, Request req) throws NamingException, MessagingException {

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    String requestNumber = req.getNumber();
    IScanChip chip = (IScanChip) sess.get( IScanChip.class, req.getIdIScanChip() );
    CoreFacility cf = (CoreFacility)sess.load(CoreFacility.class, req.getIdCoreFacility());

    if ( chip == null || req.getNumberIScanChips()==0 ) {
      return false;
    }
    
    String chipName = chip.getName() != null ? chip.getName() : "";
    String catNumber = chip.getCatalogNumber() != null ? chip.getCatalogNumber() : "";
    
    StringBuffer emailBody = new StringBuffer();
    
    UUID uuid = UUID.randomUUID(); 
    String uuidStr = uuid.toString();
    req.setUuid(uuidStr);
    String uploadQuoteURL = appURL + "/" + Constants.UPLOAD_QUOTE_JSP + "?requestUuid=" + uuidStr ;

    emailBody.append("A request for iScan chips has been submitted from the " + 
                      cf.getFacilityName() +
                      ".");
    emailBody.append("<br><br><table border='0' width = '600'><tr><td>Experiment:</td><td>" + requestNumber );
    emailBody.append("</td></tr><tr><td>Chip Type:</td><td>" + chipName );
    emailBody.append("</td></tr><tr><td>Catalog Number:</td><td>" + catNumber );
    emailBody.append("</td></tr><tr><td>Number of Samples:</td><td>" + req.getNumberOfSamples() );

    emailBody.append("</td></tr></table><br><br>To enter a quote number and upload a file, click <a href=\"" + uploadQuoteURL + "\">" + Constants.APP_NAME + " - Upload Quote Info</a>.");

    String subject = "Request for Quote Number for iScan Chips";

    String contactEmailCoreFacility = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(req.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    String contactEmailIlluminaRep  = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(req.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_ILLUMINA_REP);
    
    String senderEmail = contactEmailCoreFacility;

    String contactEmail = contactEmailIlluminaRep;
    String ccEmail = null;
        

    String emailInfo = "";
    boolean send = false;

    if(contactEmail.contains(",")){
      for(String e: contactEmail.split(",")){
        if(!MailUtil.isValidEmail(e.trim())){
          log.error("Invalid email address: " + e);
        }
      }
    } else{
      if(!MailUtil.isValidEmail(contactEmail)){
        log.error("Invalid email address: " + contactEmail);
      }
    }
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      send = true;
      subject = subject + " (TEST)";
      contactEmail = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
      emailInfo = "[If this were a production environment then this email would have been sent to: " + contactEmailIlluminaRep + "]<br><br>";
      ccEmail = null;
    }    

    if (send) {
      if(!MailUtil.isValidEmail(senderEmail)){
        senderEmail = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      }
      MailUtil.send(contactEmail, 
          ccEmail,
          senderEmail, 
          subject, 
          emailInfo + emailBody.toString(),
          true);      
    }
    return send;
  }

 
}