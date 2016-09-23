package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.LabeledSample;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.RequestEmailBodyFormatter;
import hci.gnomex.utility.Util;
import hci.gnomex.utility.WorkItemQualityControlParser;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;



public class SaveWorkItemQualityControl extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveWorkItemQualityControl.class);

  private String                       workItemXMLString;
  private Document                     workItemDoc;
  private WorkItemQualityControlParser parser;

  private String                       launchAppURL;
  private String                       appURL;

  private String                       serverName;

  private Map                          confirmedRequestMap = new HashMap();

  private DictionaryHelper             dictionaryHelper = null;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {


    if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
      workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";

      StringReader reader = new StringReader(workItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        workItemDoc = sax.build(reader);
        parser = new WorkItemQualityControlParser(workItemDoc);
      } catch (Exception je ) {
        LOG.error( "Cannot parse workItemXMLString", je );
        this.addInvalidField( "WorkItemXMLString", "Invalid work item xml");
      }
    }

    try {
      launchAppURL = this.getLaunchAppURL(request);     
      appURL = this.getAppURL(request);
    } catch (Exception e) {
      LOG.warn("Cannot get launch app URL in SaveRequest", e);
    }

    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {

    if (workItemXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());

        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          parser.parse(sess);

          for(Iterator i = parser.getWorkItems().iterator(); i.hasNext();) {
            WorkItem workItem = (WorkItem)i.next();
            Sample sample = (Sample)parser.getSample(workItem.getIdWorkItem());

            // No further processing required for On Hold or In Progress work items
            if (workItem.getStatus() == null || workItem.getStatus().equals(Constants.STATUS_ON_HOLD) || workItem.getStatus().equals(Constants.STATUS_IN_PROGRESS)) {
              continue;
            } 


            // If QC is done or bypassed for this sample, create work items for LABELING.
            if (sample.getQualDate() != null || 
                (sample.getQualBypassed() != null && sample.getQualBypassed().equalsIgnoreCase("Y"))) {

              StringBuffer buf = new StringBuffer();
              buf.append("SELECT  ls ");
              buf.append(" from LabeledSample ls ");
              buf.append(" WHERE  ls.idSample =  " + sample.getIdSample());


              List labeledSamples = sess.createQuery(buf.toString()).list();
              for(Iterator i1 = labeledSamples.iterator(); i1.hasNext();) {
                LabeledSample ls = (LabeledSample)i1.next();

                WorkItem wi = new WorkItem();
                wi.setIdRequest(sample.getIdRequest());
                wi.setIdCoreFacility(sample.getRequest().getIdCoreFacility());
                wi.setCodeStepNext(Step.LABELING_STEP);
                wi.setLabeledSample(ls);
                wi.setCreateDate(new java.sql.Date(System.currentTimeMillis()));

                sess.save(wi);
              }
            }

            // If QC is done or failed for this sample, delete the QC work item
            if (sample.getQualDate() != null || 
                (sample.getQualFailed() != null && sample.getQualFailed().equalsIgnoreCase("Y")) ||
                (sample.getQualBypassed() != null && sample.getQualBypassed().equalsIgnoreCase("Y"))) {

              // Delete qc work item
              sess.delete(workItem);
            }

            // If this is a QC request, check to see if all QC has been performed on
            // all samples.  If so, set complete date on request
            Request request = (Request)sess.load(Request.class, workItem.getIdRequest());

            // Set complete date on QC requests
            request.completeRequestIfFinished(sess);

            // Send confirmation email on QC requests; send progress email
            // on Hyb requests. (Send only once for entire request and don't
            // send if any of the QC work items were terminated.)
            if (request.isCompleteWithQC()  && !confirmedRequestMap.containsKey(request.getNumber())) {
              if (request.getAppUser() != null && 
                  request.getAppUser().getEmail() != null &&
                  !request.getAppUser().getEmail().equals("")) {

                try {
                  this.sendConfirmationEmail(sess, request);
                } catch (Exception e) {
                  LOG.error("Unable to send confirmation email notifying submitter that qc on request " + request.getNumber() +
                      " is complete. " + e.toString(), e);
                }
                confirmedRequestMap.put(request.getNumber(), request.getNumber());
              } else {
                LOG.error("Unable to send confirmation email notifying submitter that request " + request.getNumber() +
                    " sample quality is complete.  Request submitter or request submitter email is blank.");
              }
            }
          }

          sess.flush();

          parser.resetIsDirty();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(workItemDoc);

          setResponsePage(this.SUCCESS_JSP);          
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
          setResponsePage(this.ERROR_JSP);
        }


      }catch (Exception e){
        LOG.error("An exception has occurred in SaveWorkflowQualityControl ", e);

        throw new RollBackCommandException(e.getMessage());

      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e){
        LOG.error("Error", e);
      }
      }

    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }

    return this;
  }



  private void sendConfirmationEmail(Session sess, Request request) throws NamingException, MessagingException, IOException {

    dictionaryHelper = DictionaryHelper.getInstance(sess);

    CoreFacility cf = (CoreFacility)sess.load(CoreFacility.class, request.getIdCoreFacility());

    String emailSubject = null;
    StringBuffer introNote = new StringBuffer();
    String downloadRequestURL = Util.addURLParameter(launchAppURL, "requestNumber=" + request.getNumber() + "&launchWindow=" + Constants.WINDOW_FETCH_RESULTS);
    if (RequestCategory.isQCRequestCategory(request.getCodeRequestCategory())) {
      emailSubject = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory())+ " Request " + request.getNumber() + " completed";
      introNote.append("Request " + request.getNumber() + " has been completed by the " + cf.getFacilityName() + " core.");
      introNote.append("<br>To fetch the quality control reports, click <a href=\"" + downloadRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_FETCH_RESULTS + "</a>.");      
    } else {
      emailSubject = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory())+ " Request " + request.getNumber() + " in progress";
      introNote.append("Request " + request.getNumber() + " is in progress.  ");
      introNote.append("The " + cf.getFacilityName() + " core has finished Quality Control on all of the samples for Request " + request.getNumber() + ".  The report below summarizes the spectophotometer and bioanalyzer readings.");
      introNote.append("<br>To fetch the quality control reports, click <a href=\"" + downloadRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_FETCH_RESULTS + "</a>.");      
    } 

    String emailRecipients = request.getAppUser().getEmail();
    String fromAddress = cf.getContactEmail();

    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, this.getSecAdvisor(), appURL, dictionaryHelper, request, null, request.getSamples(), request.getHybridizations(), request.getSequenceLanes(),  introNote.toString());
    
    if(!MailUtil.isValidEmail(emailRecipients)){
      LOG.error("Invalid email address: " + emailRecipients);
    }
    if(!MailUtil.isValidEmail(fromAddress)){
      fromAddress = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    }
    
    MailUtilHelper helper = new MailUtilHelper(	
    		emailRecipients,
    		fromAddress,
    		emailSubject,
    		emailFormatter.formatQualityControl(),
    		null,
			true, 
			dictionaryHelper,
			serverName 								);
    MailUtil.validateAndSendEmail(helper);

  }


}