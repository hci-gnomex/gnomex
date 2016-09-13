package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.*;
import hci.gnomex.utility.*;
import org.hibernate.Session;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import org.apache.log4j.Logger;
public class ChangeRequestStatus extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(ChangeRequestStatus.class);

  private String                         codeRequestStatus = null;
  private Integer                        idRequest         = 0;
  private String                         launchAppURL;
  private String                         appURL;
  private String                         serverName;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    try {
      launchAppURL = this.getLaunchAppURL(request);
      appURL = this.getAppURL(request);
    } catch (Exception e) {
      LOG.warn("Cannot get launch app URL in SaveRequest", e);
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
      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);

      if (codeRequestStatus == null || idRequest.equals("0")) {
        this.addInvalidField("Missing information", "id and code request status needed");
      }

      if (this.isValid()) {

        Request req = (Request) sess.get(Request.class, idRequest);
        String oldRequestStatus = req.getCodeRequestStatus();

        // If this request uses products, create ledger entries
        // If there is a problem, don't proceed with changing the status
        boolean errorEncounteredWithProducts = false;
        String productErrorMessage = "";

        if (ProductUtil.determineIfRequestUsesProducts(req)) {
          String statusToUseProducts = ProductUtil.determineStatusToUseProducts(sess, req);
          if (statusToUseProducts != null && !statusToUseProducts.trim().equals("")) {
            try {
              if (ProductUtil.updateLedgerOnRequestStatusChange(sess, req, oldRequestStatus, codeRequestStatus)) {
                sess.flush();
              }
            } catch (ProductException e) {
              errorEncounteredWithProducts = true;
              productErrorMessage = e.getMessage();
              LOG.error("Unable to create ProductLedger for request. " + e.getMessage(), e);
            }
          }
        }

        if (!errorEncounteredWithProducts) {
          req.setCodeRequestStatus(codeRequestStatus);

          // SUBMITTED
          if (oldRequestStatus != null) {
            if (oldRequestStatus.equals(RequestStatus.NEW) && codeRequestStatus.equals(RequestStatus.SUBMITTED)) {
              req.setCreateDate(new java.util.Date());

              String otherRecipients = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.REQUEST_SUBMIT_CONFIRMATION_EMAIL);
              if ((req.getAppUser() != null && req.getAppUser().getEmail() != null && !req.getAppUser().getEmail().equals("")) || (otherRecipients != null && otherRecipients.length() > 0)) {
                try {
                  // confirmation email for dna seq requests is sent at submit
                  // time.
                  sendConfirmationEmail(sess, req, otherRecipients);
                } catch (Exception e) {
                  String msg = "Unable to send confirmation email notifying submitter that request " + req.getNumber() + " has been submitted.  " + e.toString();
                  LOG.error(msg, e);
                }
              } else {
                String msg = ("Unable to send confirmation email notifying submitter that request " + req.getNumber() + " has been submitted.  Request submitter or request submitter email is blank.");
                LOG.error(msg);
              }
            }
          }

          // If this was a save before submit request and now we are submitting then create billing
          // items and send confirmation email
          // when the status changes to submitted
          if ((codeRequestStatus.equals(RequestStatus.SUBMITTED) || codeRequestStatus.equals(RequestStatus.PROCESSING)) && pdh.getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.NEW_REQUEST_SAVE_BEFORE_SUBMIT).equals("Y")
                  && (pdh.getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.BILLING_DURING_WORKFLOW) == null ||
                  pdh.getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.BILLING_DURING_WORKFLOW).equals("N"))) {
            if (req.getBillingItemList(sess) == null || req.getBillingItemList(sess).isEmpty()) {
              // if we don't bill during workflow create all billing items including request property entry items
              createBillingItems(sess, req, false, false);
              sess.flush();
            }
          } else if ((codeRequestStatus.equals(RequestStatus.SUBMITTED) || codeRequestStatus.equals(RequestStatus.PROCESSING)) && pdh.getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.NEW_REQUEST_SAVE_BEFORE_SUBMIT).equals("Y")
                  && (pdh.getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.BILLING_DURING_WORKFLOW) != null &&
                  pdh.getCoreFacilityRequestCategoryProperty(req.getIdCoreFacility(), req.getCodeRequestCategory(), PropertyDictionary.BILLING_DURING_WORKFLOW).equals("Y"))) {
            // if we do bill during workflow then only bill for request property entry items
            createBillingItems(sess, req, true, false);

          }

          // COMPLETE
          // Set the complete date
          if (codeRequestStatus.equals(RequestStatus.COMPLETED)) {
            if (req.getCompletedDate() == null) {
              req.setCompletedDate(new java.sql.Date(System.currentTimeMillis()));
            }
            // Now change the billing items for the request from PENDING to
            // COMPLETE
            for (BillingItem billingItem : (Set<BillingItem>) req.getBillingItemList(sess)) {
              if (billingItem.getCodeBillingStatus().equals(BillingStatus.PENDING)) {
                billingItem.setCodeBillingStatus(BillingStatus.COMPLETED);
              }
            }
            // Delete any pending work items associated with the request since
            // we are marking it as complete
            for (WorkItem wi : (Set<WorkItem>) req.getWorkItems()) {
              sess.delete(wi);
            }

            // Send a confirmation email

            try {
              EmailHelper.sendConfirmationEmail(sess, req, this.getSecAdvisor(), launchAppURL, appURL, serverName);
            } catch (Exception e) {
              LOG.error("Unable to send confirmation email notifying submitter that request " + req.getNumber() + " is complete. " + e.toString(), e);
            }

          }

          // PROCCESSING
          if (codeRequestStatus.equals(RequestStatus.PROCESSING)) {
            if (req.getProcessingDate() == null) {
              req.setProcessingDate(new java.sql.Date(System.currentTimeMillis()));
            }
          }

          sess.flush();

          this.xmlResult = "<SUCCESS idRequest=\"" + idRequest + "\" codeRequestStatus=\"" + codeRequestStatus + "\"/>";
        } else {
          this.xmlResult = "<FAILURE message=\"PRODUCT ERROR: " + productErrorMessage + "\"/>";
        }

      }

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }

    } catch (Exception e) {
      LOG.error("An exception has occurred in ChangeRequestStatus ", e);
      throw new RollBackCommandException(e.toString());
    } finally {
      try {

        if (sess != null) {
          HibernateSession.closeSession();
        }
      } catch (Exception e) {
        LOG.error("Error in ChangeRequestStatus: ", e);

      }
    }

    return this;
  }

  private void createBillingItems(Session sess, Request req, Boolean requestPropertiesOnly, Boolean comingFromWorkflow) throws Exception {

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

    // Get the current billing period
    BillingPeriod billingPeriod = dictionaryHelper.getCurrentBillingPeriod();
    if (billingPeriod == null) {
      throw new Exception("Cannot find current billing period to create billing items");
    }

    // Hash map of assays chosen. Build up the map
    Map<String, ArrayList<String>> sampleAssays = new HashMap<String, ArrayList<String>>();
    for (Sample sample : (Set<Sample>) req.getSamples()) {
      sample.setIdSampleString(sample.getIdSample().toString());
      Integer idAssay = null;
      for (PlateWell well : (Set<PlateWell>) sample.getWells()) {
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

    SaveRequest.createBillingItems(sess, req, null, billingPeriod, dictionaryHelper, req.getSamples(), null, null, null, sampleAssays, null, BillingStatus.PENDING, req.getPropertyEntries(), req.getBillingTemplate(sess), requestPropertiesOnly, comingFromWorkflow);
    sess.flush();

  }

  private void sendConfirmationEmail(Session sess, Request req, String otherRecipients) throws NamingException, MessagingException, IOException {

    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);

    StringBuffer introNote = new StringBuffer();
    String trackRequestURL = Util.addURLParameter(launchAppURL, "?requestNumber=" + req.getNumber() + "&launchWindow=" + Constants.WINDOW_TRACK_REQUESTS);
    CoreFacility cf = (CoreFacility) sess.load(CoreFacility.class, req.getIdCoreFacility());

    introNote.append("Experiment request " + req.getNumber() + " has been submitted to the " + cf.getFacilityName() + " core.  You will receive email notification when the experiment is complete.");

    introNote.append("<br><br>To track progress on the experiment request, click <a href=\"" + trackRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_TRACK_REQUESTS + "</a>.");

    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, this.getSecAdvisor(), appURL, dictionaryHelper, req, "", req.getSamples(), new HashSet(), new HashSet(), introNote.toString());
    String subject = dictionaryHelper.getRequestCategory(req.getCodeRequestCategory()) + (req.getIsExternal().equals("Y") ? " Experiment " : " Experiment Request ") + req.getNumber() + (req.getIsExternal().equals("Y") ? " registered" : " submitted");

    String contactEmailCoreFacility = cf.getContactEmail();
    String contactEmailSoftwareBugs = PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(req.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_SOFTWARE_BUGS);
    String emailRecipients = "";
    if (req.getAppUser() != null && req.getAppUser().getEmail() != null) {
      emailRecipients = req.getAppUser().getEmail();
    }

    if (!MailUtil.isValidEmail(emailRecipients)) {
      LOG.error("Invalid email address " + emailRecipients);
    }

    if (otherRecipients != null && otherRecipients.length() > 0) {
      if (emailRecipients.length() > 0) {
        emailRecipients += ",";
      }
      emailRecipients += otherRecipients;
    }

    for (String e : emailRecipients.split(",")) {
      if (!MailUtil.isValidEmail(e.trim())) {
        LOG.error("Invalid email address " + e);
      }
    }

    String fromAddress = req.getIsExternal().equals("Y") ? contactEmailSoftwareBugs : contactEmailCoreFacility;
    if (!MailUtil.isValidEmail(fromAddress)) {
      fromAddress = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    }

    MailUtilHelper helper = new MailUtilHelper(emailRecipients, fromAddress, subject, emailFormatter.format(), null, true, dictionaryHelper, serverName);
    helper.setRecipientAppUser(req.getAppUser());
    MailUtil.validateAndSendEmail(helper);

  }
}