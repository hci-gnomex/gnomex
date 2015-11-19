package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingItemAutoComplete;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.Util;
import hci.gnomex.utility.comparators.SequenceLaneNumberComparator;
import hci.gnomex.utility.formatters.RequestEmailBodyFormatter;
import hci.gnomex.utility.mail.MailUtil;
import hci.gnomex.utility.mail.MailUtilHelper;
import hci.gnomex.utility.parsers.WorkItemSolexaPipelineParser;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;




public class SaveWorkItemSolexaPipeline extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveWorkItemSolexaPipeline.class);
  
  private String                       workItemXMLString;
  private Document                     workItemDoc;
  private WorkItemSolexaPipelineParser parser;
  
  private DictionaryHelper             dictionaryHelper;
  
  private String                       launchAppURL;
  private String                       appURL;
  
  private String                       serverName;
  
  private HashMap                      requestNotifyMap = new HashMap();
  private HashMap                      requestNotifyLaneMap = new HashMap();
  
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("workItemXMLString") != null && !request.getParameter("workItemXMLString").equals("")) {
      workItemXMLString = "<WorkItemList>" + request.getParameter("workItemXMLString") + "</WorkItemList>";
      
      StringReader reader = new StringReader(workItemXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        workItemDoc = sax.build(reader);
        parser = new WorkItemSolexaPipelineParser(workItemDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse workItemXMLString", je );
        this.addInvalidField( "WorkItemXMLString", "Invalid work item xml");
      }
    }
    
    try {
      launchAppURL = this.getLaunchAppURL(request);      
      appURL = this.getAppURL(request);      
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveRequest", e);
    }
    
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    if (workItemXMLString != null) {
      try {
        Session sess = HibernateSession.currentSession(this.getUsername());
        Map<Integer, BillingItemAutoComplete> autoCompleteMap = new HashMap<Integer, BillingItemAutoComplete>();
        Map<Integer, Set<SequenceLane>> sequenceLanesCompletedMap = new HashMap<Integer, Set<SequenceLane>>();
        
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
          parser.parse(sess);
          
          for (Iterator i = parser.getWorkItems().iterator(); i.hasNext();) {
            WorkItem workItem = (WorkItem) i.next();
            FlowCellChannel channel = (FlowCellChannel) parser.getFlowCellChannel(workItem.getIdWorkItem());

            // No further processing required for On Hold or In Progress work items
            if (workItem.getStatus() == null || workItem.getStatus().equals(Constants.STATUS_ON_HOLD) || workItem.getStatus().equals(Constants.STATUS_IN_PROGRESS)) {
              continue;
            } 
            
            // If pipeline is done or has failed, delete the work item.
            if (channel.getPipelineDate() != null ||
                (channel.getPipelineFailed() != null && channel.getPipelineFailed().equals("Y"))) {

              // Delete work item
              sess.delete(workItem);
              
              
             
            }
            
            // Check to see if all of the sequence lanes for each request have been completed.
            if (channel.getSequenceLanes() != null) {
              for(Iterator i1 = channel.getSequenceLanes().iterator(); i1.hasNext();) {
                SequenceLane lane = (SequenceLane)i1.next();
                
                Request request = (Request)sess.load(Request.class, lane.getIdRequest());
                if (autoCompleteMap.containsKey(request.getIdRequest())) {
                  BillingItemAutoComplete auto = autoCompleteMap.get(request.getIdRequest());
                  // If same request has workitems of different steps then they have to manually complete billing items.
                  if (!auto.getCodeStep().equals(workItem.getCodeStepNext())) {
                    auto.setSkip();
                  }
                } else {
                  BillingItemAutoComplete auto = new BillingItemAutoComplete(sess, workItem.getCodeStepNext(), request);
                  autoCompleteMap.put(request.getIdRequest(), auto);
                }
                
                // Keep track of lanes by request to create billing items.
                // Note that currently channel status can only be "On Hold", "Complete" or "Terminate".  We'll only
                // get here if status is "Complete" or "Terminate" so we want to create billing items in either case.
                Set<SequenceLane> laneSet = sequenceLanesCompletedMap.get(request.getIdRequest());
                if (laneSet == null) {
                  laneSet = new TreeSet<SequenceLane>(new SequenceLaneNumberComparator());
                }
                laneSet.add(lane);
                sequenceLanesCompletedMap.put(request.getIdRequest(), laneSet);
                
                // Keep track of requests to send out a confirmation email (just once per request)
                // (Send email if at least one sequence lane has been completed pipeline.)
                if (channel.getPipelineDate() != null) {  
                  requestNotifyMap.put(request.getNumber(), request);

                  Collection lanes = (Collection)requestNotifyLaneMap.get(request.getNumber());
                  if (lanes == null) {
                    lanes = new ArrayList();
                  }
                  lanes.add(lane);
                  requestNotifyLaneMap.put(request.getNumber(), lanes);
                }

                // Set the completed date on the request
                request.completeRequestIfFinished(sess);
                
              }
            }
            
          }

          processBilling(sess, autoCompleteMap, sequenceLanesCompletedMap);

          sess.flush();
          
          parser.resetIsDirty();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(workItemDoc);

          // Now send out confirmation emails
          for(Iterator i = requestNotifyMap.keySet().iterator(); i.hasNext();) {
            String requestNumber = (String)i.next();
            Request request = (Request)requestNotifyMap.get(requestNumber);
            try {
              this.sendConfirmationEmail(sess, request, (Collection)requestNotifyLaneMap.get(request.getNumber()));
            } catch (Exception e) {
              this.xmlResult = "<InvalidSubmitterEmail notice=\"Unable to notify " + request.getAppUser().getFirstLastDisplayName() +" that their sequence lanes have been completed because either they have no email address listed in gnomex or their email address is malformed." + "\"" + "/>";
              e.printStackTrace();
            }
          }
          setResponsePage(this.SUCCESS_JSP);
        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to manage workflow");
          setResponsePage(this.ERROR_JSP);
        }
        
        
      }catch (Exception e){
        log.error("An exception has occurred in SaveWorkflowSolexaPipeline ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
          
      }finally {
        try {
          HibernateSession.closeSession();        
        } catch(Exception e) {
          
        }
      }
      
    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }
    
    return this;
  }
  
  private void processBilling(Session sess, Map<Integer, BillingItemAutoComplete> autoCompleteMap, Map<Integer, Set<SequenceLane>> sequenceLanesCompletedMap) throws Exception {
    PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    // Get the current billing period
    BillingPeriod billingPeriod = dictionaryHelper.getCurrentBillingPeriod();
    if (billingPeriod == null) {
      throw new Exception("Cannot find current billing period to create billing items");
    }

    for(Integer key : autoCompleteMap.keySet()) {
      BillingItemAutoComplete auto = autoCompleteMap.get(key);
      String prop = propertyHelper.getCoreFacilityRequestCategoryProperty(auto.getRequest().getIdCoreFacility(), auto.getRequest().getCodeRequestCategory(), PropertyDictionary.BILLING_DURING_WORKFLOW);
      if (prop == null || !prop.equals("Y")) {
        // Billing items created at submit.  Just complete items that can be completed.
         if (auto.getSkip()) {
          continue;
        }
  
        Integer completedQty = 0;
        Map<Integer, FlowCellChannel> channels = auto.getRequest().getFlowCellChannels();
        for(Integer chKey : channels.keySet()) {
          FlowCellChannel channel = channels.get(chKey);
          if (channel != null && channel.getPipelineDate() != null) {
            completedQty++;
          }
        }
        
        auto.completeItems(channels.size(), completedQty);
      } else {
        // Need to create billing items at this point.
        Set<SequenceLane> laneSet = sequenceLanesCompletedMap.get(auto.getRequest().getIdRequest());
        if (laneSet != null) {
            SaveRequest.createBillingItems(sess, auto.getRequest(), null, billingPeriod, dictionaryHelper, null, null, null, laneSet, null, auto.getCodeStep(), BillingStatus.COMPLETED);
        }
      }
    }
  }
  
  private void sendBioinformaticsAssistanceEmail(Session sess, Request r) throws NamingException, MessagingException, IOException {
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    AppUser user = r.getAppUser();
    String organismName = "";
    String genomeBuild = "";
    String seqRunType = "";
    String seqLibProtocol = "";
    String application = "";
    String subject = "Bioinformatics analysis for " + user.getFirstLastDisplayName() + ", sequencing request number " + r.getNumber();
    String fromAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    if (MailUtil.isValidEmail(user.getEmail())) {
      fromAddress = user.getEmail();
    }
    String toAddress = dictionaryHelper.getPropertyDictionary(PropertyDictionary.CONTACT_EMAIL_BIOINFORMATICS_ANALYSIS_REQUESTS);
    String ccAddress = "";
    
    if(r.getIdOrganismSampleDefault() != null) {
      organismName = dictionaryHelper.getOrganism(r.getIdOrganismSampleDefault());
    }
    if(r.getApplication() != null && r.getApplication().getApplication() != null) {
      application = r.getApplication().getApplication();
    }
    
    for(Iterator i = r.getSamples().iterator(); i.hasNext();) {
      Sample s = (Sample)i.next();
      if(s.getIdSeqLibProtocol() != null) {
        seqLibProtocol = dictionaryHelper.getSeqLibProtocol(s.getIdSeqLibProtocol());
        break;
      }
    }
    for(Iterator j = r.getSequenceLanes().iterator(); j.hasNext();) {
      SequenceLane sl = (SequenceLane)j.next();

      if(sl.getIdSeqRunType() != null && seqRunType.equals("")) {
        seqRunType = dictionaryHelper.getSeqRunType(sl.getIdSeqRunType());
      }

      if(sl.getIdGenomeBuildAlignTo() != null && genomeBuild.equals("")) {
        genomeBuild = dictionaryHelper.getGenomeBuildName(sl.getIdGenomeBuildAlignTo());
      }

      if(!genomeBuild.equals("") && !seqRunType.equals("")) {
        break;
      }
    }

    
    StringBuffer body = new StringBuffer();
    
    body.append(user.getFirstLastDisplayName() + " of the " + r.getLabName() + " has requested bioinformatic analysis assistance for sequencing request number " + r.getNumber() + ". <br>");
    body.append("The data is now available in GNomEx.<br><br>");
    body.append("<b>Contact Information:</b><br> " + user.getFirstLastDisplayName() + "<br>");
    if(user.getEmail() != null) {
      body.append(user.getEmail() + "<br>");
      ccAddress = user.getEmail();
    }
    if(user.getPhone() != null) {
      body.append(user.getPhone() + "<br> <br>");
    } else {
      body.append("<br>");
    }
    
    if(r.getAnalysisInstructions() != null) {
      body.append("<b>Analysis Notes:</b><br> " + r.getAnalysisInstructions() + "<br> <br>");
    }
    
    body.append("<b>Experiment Information:</b><br>" + r.getNumberOfSamples() + " sample(s). <br>");
    body.append("<u>Sequencing Application:</u> " + application + ". <br>");
    body.append("<u>Organism:</u> " + organismName + ". <br>");
    body.append("<u>Genome Build:</u> " + genomeBuild + ". <br>");
    body.append("<u>Run Type:</u> " + seqRunType + ". <br>");
    body.append("<u>Library Protocol:</u> " + seqLibProtocol);
    
    MailUtilHelper helper = new MailUtilHelper(	
    		toAddress,
    		ccAddress,
    		null,
    		fromAddress,
    		subject,
    		body.toString(),
    		null,
			true, 
			dictionaryHelper,
			serverName 			);
    MailUtil.validateAndSendEmail(helper);
    
  }

  private void sendConfirmationEmail(Session sess, Request request, Collection lanes) throws NamingException, MessagingException, IOException {
    
    dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    String downloadRequestURL = Util.addURLParameter(launchAppURL, "?requestNumber=" + request.getNumber() + "&launchWindow=" + Constants.WINDOW_FETCH_RESULTS);
    CoreFacility cf = (CoreFacility)sess.load(CoreFacility.class, request.getIdCoreFacility());

    String analysisInstruction = null;
    String genomeAlignTo = null;
    
    String finishedLaneText = "";
    int finishedLaneCount = 0;
    
    for(Iterator i = lanes.iterator(); i.hasNext();) {
      SequenceLane lane = (SequenceLane)i.next();

      if (lane.getAnalysisInstructions() != null && !lane.getAnalysisInstructions().equals("")) {
        analysisInstruction = lane.getAnalysisInstructions();
      }
      if (lane.getIdGenomeBuildAlignTo() != null) {
        GenomeBuild genomeBuild = (GenomeBuild)sess.load(GenomeBuild.class, lane.getIdGenomeBuildAlignTo());
        genomeAlignTo = genomeBuild.getDisplay();
      }
      
      if (lane.getFlowCellChannel() != null && lane.getFlowCellChannel().getPipelineDate() != null) {
        finishedLaneText += lane.getNumber();
        finishedLaneCount++;
        if (i.hasNext()) {
          finishedLaneText += ", ";
        }
      }
    }
    String laneText = finishedLaneCount > 1 ? "Lanes" : "Lane";
    String haveText = finishedLaneCount > 1 ? "have"  : "has";
    
    if (finishedLaneCount == 0) {
      return;
    }
    
    // Send email to submitter
    StringBuffer introNote = new StringBuffer();
    
    introNote.append("Sequence " + laneText + " " + finishedLaneText + " for ");
    introNote.append("Request " + request.getNumber() + " " + haveText + " been completed by the " + cf.getFacilityName() + " core.");
    if(request.getIsExternal().equals("N") && request.getIdCoreFacility().equals(CoreFacility.CORE_FACILITY_GENOMICS_ID)){
      introNote.append("<br><br>" + PropertyDictionaryHelper.getInstance(sess).getCoreFacilityProperty(CoreFacility.CORE_FACILITY_GENOMICS_ID, PropertyDictionary.ANALYSIS_ASSISTANCE_NOTE));
    }
    
    introNote.append("<br><br>To fetch the results, click <a href=\"" + downloadRequestURL + "\">" + Constants.APP_NAME + " - " + Constants.WINDOW_NAME_FETCH_RESULTS + "</a>.");
    
    if (request.getBioinformaticsAssist() != null && request.getBioinformaticsAssist().equals("Y")) {
      introNote.append("<br><br>Bioinformatics core has been informed that the experiment is complete.");
    }
    RequestEmailBodyFormatter emailFormatter = new RequestEmailBodyFormatter(sess, this.getSecAdvisor(), appURL, dictionaryHelper, request, null, request.getSamples(), request.getHybridizations(), request.getSequenceLanes(), introNote.toString());
    emailFormatter.setIncludeMicroarrayCoreNotes(false);
    
    String subject = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber() + " completed";
    
    String emailRecipients = request.getAppUser().getEmail();
    String fromAddress = cf.getContactEmail();
    
    if(!MailUtil.isValidEmail(emailRecipients)){
      log.error("Invalid email: " + emailRecipients + " for submitter " + request.getAppUser().getFirstLastDisplayName());
    }
    
    if(!MailUtil.isValidEmail(fromAddress)){
        fromAddress = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    }
    
    MailUtilHelper helper = new MailUtilHelper(	
    		emailRecipients,
    		fromAddress,
    		subject,
    		emailFormatter.format(),
    		null,
			true, 
			dictionaryHelper,
			serverName 				);
    helper.setRecipientAppUser(request.getAppUser());
    MailUtil.validateAndSendEmail(helper);
    
    // Send email to bioinformatics core
    if (request.getBioinformaticsAssist() != null && request.getBioinformaticsAssist().equals("Y")) {
      sendBioinformaticsAssistanceEmail(sess, request);
    }
  }
}