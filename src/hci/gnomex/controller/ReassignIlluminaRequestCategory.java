package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.billing.IlluminaSeqPlugin;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.Step;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingAccountSplitParser;
import hci.gnomex.utility.BillingInvoiceEmailFormatter;
import hci.gnomex.utility.BillingItemParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.SequenceLaneNumberComparator;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class ReassignIlluminaRequestCategory extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReassignIlluminaRequestCategory.class);
  
  private Integer                      idRequest;
  private String                       codeRequestCategory;
  private Integer                      idNumberSequencingCycles;
  private Integer                      idSeqRunType;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }
    
    if (request.getParameter("codeRequestCategory") != null && !request.getParameter("codeRequestCategory").equals("")) {
      codeRequestCategory = request.getParameter("codeRequestCategory");
    } else {
      this.addInvalidField("codeRequestCategory", "codeRequestCategory is required");
    }
    
    if (request.getParameter("idNumberSequencingCycles") != null && !request.getParameter("idNumberSequencingCycles").equals("")) {
      idNumberSequencingCycles = new Integer(request.getParameter("idNumberSequencingCycles"));
    } else {
      this.addInvalidField("idNumberSequencingCycles", "idNumberSequencingCycles is required");
    }
    
    if (request.getParameter("idSeqRunType") != null && !request.getParameter("idSeqRunType").equals("")) {
      idSeqRunType = new Integer(request.getParameter("idSeqRunType"));
    } else {
      this.addInvalidField("idSeqRunType", "v is required");
    }

    
  }

  public Command execute() throws RollBackCommandException {
    
    String message = "";

    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {

        Request request = (Request)sess.load(Request.class, idRequest);


        if (RequestCategory.isIlluminaRequestCategory(request.getCodeRequestCategory()) && 
            RequestCategory.isIlluminaRequestCategory(codeRequestCategory) &&
            !request.getCodeRequestCategory().equals(codeRequestCategory)) {

          // Change the codeRequestCategory
          request.setCodeRequestCategory(codeRequestCategory);

          // Change the seq run type and number of sequencing cycles
          TreeSet seqLanesAdded = new TreeSet(new SequenceLaneNumberComparator());
          for (Iterator i = request.getSequenceLanes().iterator(); i.hasNext();) {
            SequenceLane lane = (SequenceLane)i.next();

            if (lane.getFlowCellChannel() != null) {
              throw new Exception("Cannot convert Illumina request " + request.getNumber() + " because sequence lane " + lane.getNumber() + " is already on a flow cell");
            }
            lane.setIdNumberSequencingCycles(idNumberSequencingCycles);
            lane.setIdSeqRunType(idSeqRunType);      

            seqLanesAdded.add(lane);
          }

          // Change the workflow step
          for(Iterator i = request.getWorkItems().iterator(); i.hasNext();) {
            WorkItem wi = (WorkItem)i.next();
            if (codeRequestCategory.equals(RequestCategory.ILLUMINA_HISEQ_REQUEST_CATEGORY)) {
              if (wi.getCodeStepNext().equals(Step.SEQ_QC)) {
                wi.setCodeStepNext(Step.HISEQ_QC);
              } else if (wi.getCodeStepNext().equals(Step.SEQ_PREP)) {
                wi.setCodeStepNext(Step.HISEQ_PREP);
              } else if (wi.getCodeStepNext().equals(Step.SEQ_CLUSTER_GEN)) {
                wi.setCodeStepNext(Step.HISEQ_CLUSTER_GEN);
              } 
            } else if (codeRequestCategory.equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
              if (wi.getCodeStepNext().equals(Step.HISEQ_QC)) {
                wi.setCodeStepNext(Step.SEQ_QC);
              } else if (wi.getCodeStepNext().equals(Step.HISEQ_PREP)) {
                wi.setCodeStepNext(Step.SEQ_PREP);
              } else if (wi.getCodeStepNext().equals(Step.HISEQ_CLUSTER_GEN)) {
                wi.setCodeStepNext(Step.SEQ_CLUSTER_GEN);
              }
            } 
          }
          
          // Find price category for sequencing
          List priceCategories = sess.createQuery("SELECT pc from PriceCategory pc WHERE pc.pluginClassName = '" + IlluminaSeqPlugin.class.getName() + "'").list();
          
          // Find all of the existing billing items that will be replaced
          int undeletedCount = 0;
          int deletedCount = 0;
          for(Iterator i = priceCategories.iterator(); i.hasNext();) {
            PriceCategory priceCategory = (PriceCategory)i.next();
            
            for(Iterator i1 = request.getBillingItems().iterator(); i1.hasNext();) {
              BillingItem bi = (BillingItem)i1.next();
              if (bi.getIdPriceCategory().equals(priceCategory.getIdPriceCategory())) {
                if (bi.getCodeBillingStatus().equals(BillingStatus.PENDING) || bi.getCodeBillingStatus().equals(BillingStatus.COMPLETED)) {
                  sess.delete(bi);
                  deletedCount++;
                } else {
                  undeletedCount++;
                }                
              }
            }
          }
          


          // Get the billingPeriod
          BillingPeriod billingPeriod = DictionaryHelper.getInstance(sess).getCurrentBillingPeriod();

          // Create the new billing items
          SaveRequest.createBillingItems(sess, request, Constants.AMEND_ADD_SEQ_LANES, billingPeriod, DictionaryHelper.getInstance(sess), null, null, null, seqLanesAdded, null);
          message = "Experiment " + request.getNumber() + " experiment category has been changed.  Sequence lanes have been modified, work items have been moved to the appropriate workflow, and new billing items for sequencing have been created.";
          if (undeletedCount > 0) {
            message += "\n\nWarning: Unable to remove " + undeletedCount + " billing item(s) due to approved status.  Please correct billing accordingly.";
          } 

        }


        sess.flush();


        this.xmlResult = "<SUCCESS message='" + message + "'/>";

        setResponsePage(this.SUCCESS_JSP);          
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to reassign request category");
        setResponsePage(this.ERROR_JSP);
      }


    } catch (Exception e){
      log.error("An exception has occurred in ReassignIlluminaRequestCategory ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {

      }
    }


    return this;
  }
  
    

}