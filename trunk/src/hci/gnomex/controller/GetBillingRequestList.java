package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetBillingRequestList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetBillingRequestList.class);
  
  private BillingItemFilter billingItemFilter;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    billingItemFilter = new BillingItemFilter();
    HashMap errors = this.loadDetailObject(request, billingItemFilter);
    this.addInvalidFields(errors);
    
    if (!this.billingItemFilter.hasCriteria()) {
      this.addInvalidField("criteria", "Please select a billing period, group, or billing account; or enter a request number");
    }
    
    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
      this.addInvalidField("permission", "Insufficient permission to manage billing items");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
    HashMap statusNodeMap = new HashMap();
    
    if (billingItemFilter.getIdBillingPeriod() != null) {
      BillingPeriod bp = dh.getBillingPeriod(billingItemFilter.getIdBillingPeriod());
      if (bp == null) {
        throw new RollBackCommandException("Unable to find billing period " + billingItemFilter.getIdBillingPeriod());
      }
      billingItemFilter.setBillingPeriod(bp);
    }
    
    StringBuffer buf = billingItemFilter.getBillingNewRequestQuery();
    log.info("Query: " + buf.toString());
    List newRequests = (List)sess.createQuery(buf.toString()).list();
    
    
    Element statusNode = new Element("Status");

    TreeMap requestToStatusMap = new TreeMap();
    HashMap requestNodeMap = new HashMap();
    HashMap statusToLabNodeMap = new HashMap();
    
    // Query all requests that don't have any billing items.
    // Assign these requests a 'Pending' status.
    if (newRequests.size() > 0) {
      statusNode.setAttribute("label", "Pending");
      statusNode.setAttribute("status", BillingStatus.PENDING);
      statusNodeMap.put(BillingStatus.PENDING, statusNode);

      for(Iterator i = newRequests.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        Integer idRequest          = (Integer)row[0];
        String requestNumber       = (String)row[1]  == null ? ""  : (String)row[1];
        String codeRequestCategory = (String)row[2]  == null ? ""  : (String)row[2];
        String idBillingAccount    = (Integer)row[3] == null ? ""  : ((Integer)row[3]).toString();
        String labName             = (String)row[4]  == null ? ""  : (String)row[4];
        AppUser submitter          = (AppUser)row[5];
        Date createDate            = (Date)row[6];
        Date completedDate         = (Date)row[7];
        String billingAcctName     = (String)row[8]  == null ? ""  : (String)row[8];
        
        String toolTip = requestNumber + " " + labName;
        if (createDate != null) {
          toolTip += "\nsubmitted " + this.formatDate(createDate, this.DATE_OUTPUT_DASH_SHORT);
        }
        if (completedDate != null) {
          toolTip += ", completed  " + this.formatDate(completedDate, this.DATE_OUTPUT_DASH_SHORT);
        }
        
        Element node = new Element("Request");
        node.setAttribute("idRequest", idRequest.toString());
        node.setAttribute("requestNumber", requestNumber);
        node.setAttribute("label", requestNumber);
        node.setAttribute("codeRequestCategory", codeRequestCategory);
        node.setAttribute("idBillingAccount", idBillingAccount);
        node.setAttribute("labName", labName != null ? labName : "");
        node.setAttribute("toolTip", toolTip);
        node.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
        node.setAttribute("codeBillingStatus", BillingStatus.NEW);
        node.setAttribute("createDate", createDate != null ? this.formatDate(createDate, this.DATE_OUTPUT_DASH) :  "");
        node.setAttribute("completedDate", completedDate != null ? this.formatDate(completedDate, this.DATE_OUTPUT_DASH) : "");
      
        String labBillingName = labName + " (" + billingAcctName + ")";

        // Hash the status node.
        List statusList = (List)requestToStatusMap.get(requestNumber);
        if (statusList == null) {
          statusList = new ArrayList();
          requestToStatusMap.put(requestNumber, statusList);
        }
        statusList.add(BillingStatus.PENDING);
        
        // There can be multiple requests nodes for a given request number when
        // the request's billing items are split among multiple billing 
        // accounts.  Keep a hash map of these different request nodes.
        
        TreeMap requestNodes = (TreeMap)requestNodeMap.get(requestNumber);
        if (requestNodes == null) {
          requestNodes = new TreeMap();
          requestNodeMap.put(requestNumber, requestNodes);
        }
        requestNodes.put(requestNumber + labBillingName, node);
        
        
      }
      
    }
    
    
    buf = billingItemFilter.getBillingRequestQuery();
    log.info("Query: " + buf.toString());
    List billingItemRequests = (List)sess.createQuery(buf.toString()).list();
    String prevCodeBillingStatus = "NEW";
    // 
    // Query all requests with billing items.  Determine status by looking
    // at all of billing items for request.  
    // Hash nodes for lab/billing account, status.
    //
    for(Iterator i = billingItemRequests.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      String codeBillingStatus   = (String)row[0]  == null ? ""  : (String)row[0];
      Integer idRequest          = (Integer)row[1];
      String requestNumber       = (String)row[2]  == null ? ""  : (String)row[2];
      String codeRequestCategory = (String)row[3]  == null ? ""  : (String)row[3];
      Integer idLab              = (Integer)row[4];
      String labName             = (String)row[5]  == null ? ""  : (String)row[5];
      AppUser submitter          = (AppUser)row[6];
      Date createDate            = (Date)row[7];
      Date completedDate         = (Date)row[8];
      Integer idBillingAcct      = (Integer)row[9];
      String billingAcctName     = (String)row[10]  == null ? ""  : (String)row[10];
      
      String toolTip = requestNumber + " " + labName;
      if (createDate != null) {
        toolTip += "\nsubmitted " + this.formatDate(createDate, this.DATE_OUTPUT_DASH_SHORT);
      }
      if (completedDate != null) {
        toolTip += ", completed  " + this.formatDate(completedDate, this.DATE_OUTPUT_DASH_SHORT);
      }
      
      
      List statusList = (List)requestToStatusMap.get(requestNumber);
      if (statusList == null) {
        statusList = new ArrayList();
        requestToStatusMap.put(requestNumber, statusList);
      }
      statusList.add(codeBillingStatus);
      
      Map labNodeMap = (Map)statusToLabNodeMap.get(codeBillingStatus);
      if (labNodeMap == null) {
        labNodeMap = new TreeMap();
        statusToLabNodeMap.put(codeBillingStatus, labNodeMap);
      }
      String labBillingName = labName + " (" + billingAcctName + ")";
      Element labNode = (Element)labNodeMap.get(labBillingName);
      if (labNode == null) {
        labNode = new Element("Lab");
        labNode.setAttribute("label", labBillingName);
        labNode.setAttribute("idLab", idLab != null ? idLab.toString() : "");
        labNode.setAttribute("name", labName);
        labNode.setAttribute("idBillingAccount", idBillingAcct != null ? idBillingAcct.toString() : "");
        labNode.setAttribute("billingAccountName", billingAcctName);
        labNodeMap.put(labBillingName, labNode);
      }
      
      if (!prevCodeBillingStatus.equals(codeBillingStatus)) {
        statusNode = new Element("Status");
        statusNode.setAttribute("label",  dh.getBillingStatus(codeBillingStatus));
        statusNode.setAttribute("status", codeBillingStatus);
        statusNodeMap.put(codeBillingStatus, statusNode);
      }
      
      
      Element node = new Element("Request");
      node.setAttribute("idRequest", idRequest.toString());
      node.setAttribute("label", requestNumber);        
      node.setAttribute("requestNumber", requestNumber);        
      node.setAttribute("toolTip", toolTip);
      node.setAttribute("codeRequestCategory", codeRequestCategory);
      node.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
      node.setAttribute("labBillingName", labBillingName);
      node.setAttribute("idLab", idLab != null ? idLab.toString() : "");
      node.setAttribute("idBillingAccount", idBillingAcct != null ? idBillingAcct.toString() : "");
      
      // There can be multiple requests nodes for a given request number when
      // the request's billing items are split among multiple billing 
      // accounts.  Keep a hash map of these different request nodes.
      TreeMap requestNodes = (TreeMap)requestNodeMap.get(requestNumber);
      if (requestNodes == null) {
        requestNodes = new TreeMap();
        requestNodeMap.put(requestNumber, requestNodes);
      }
      requestNodes.put(requestNumber + labBillingName, node);
      
      
      
      prevCodeBillingStatus = codeBillingStatus;
    }
    
    //
    //  Determine request status.
    //   - If any billing items are Pending, status is Pending.
    //   - If any billing items are Completed, status is Completed.
    //   - If all billing items are approved, status is Approved.
    //
    // Organize requests under lab/billing account node if request is Completed or Approved; and
    // then place lab/billing account nodes under appropriate status node).
    // Organize requests directly under status node if request is Pending.
    //    
    for(Iterator i = requestToStatusMap.keySet().iterator(); i.hasNext();) {
      String requestNumber = (String)i.next();
      
      List statusList = (List)requestToStatusMap.get(requestNumber);
      Map requestNodes = (Map)requestNodeMap.get(requestNumber);
      

      // For this request, figure out which of the billing item
      // status takes precedence.
      String codeBillingStatus = null;
      if (statusList.size() == 1) {
        codeBillingStatus = (String)statusList.iterator().next();
      } else {
        // Pending takes precedence over completed, approved
        // Then completed takes precedence over approved
        for(Iterator i1 = statusList.iterator(); i1.hasNext();) {
          String code = (String)i1.next();
          if (code.equals(BillingStatus.PENDING)) {
            codeBillingStatus = code;
            break;
          }
        }
        if (codeBillingStatus == null) {
          for(Iterator i1 = statusList.iterator(); i1.hasNext();) {
            String code = (String)i1.next();
            if (code.equals(BillingStatus.COMPLETED)) {
              codeBillingStatus = code;
              break;
            }
          }

        }
        if (codeBillingStatus == null) {
          for(Iterator i1 = statusList.iterator(); i1.hasNext();) {
            String code = (String)i1.next();
            if (code.equals(BillingStatus.APPROVED)) {
              codeBillingStatus = code;
              break;
            }
          }

        }
      }

      // Grab the appropriate status node
      statusNode = (Element)statusNodeMap.get(codeBillingStatus);
      
      // If the status is PENDING, only stick one request node
      // under pending (don't consider distinct billing accounts for
      // billing items under a request.)
      if (codeBillingStatus.equals(BillingStatus.PENDING)) {
        String key = (String)requestNodes.keySet().iterator().next();
        Element requestNode = (Element)requestNodes.get(key);
        statusNode.addContent(requestNode);        
      } else {
        // If the status is COMPLETED or APPROVED,
        // For each request/billing account combination, attach
        // the request node to the appropriate parent lab/billing
        // account node
        for(Iterator i0 = requestNodes.keySet().iterator(); i0.hasNext();) {
          String key = (String)i0.next();
          Element requestNode = (Element)requestNodes.get(key);
          Map labNodeMap = (Map)statusToLabNodeMap.get(codeBillingStatus);
          Element labNode = (Element)labNodeMap.get(requestNode.getAttributeValue("labBillingName"));
          labNode.addContent(requestNode);
        }
        
      }
    }
      


    // Now create XML document organizing each request
    // under <STATUS> node.  Show <STATUS> in order
    // of New, Pending, Completed, Approved.
    Document doc = new Document(new Element("BillingRequestList"));
    Element status = (Element)statusNodeMap.get(BillingStatus.NEW);
    if (status != null) {
      doc.getRootElement().addContent(status);      
    }
    status = (Element)statusNodeMap.get(BillingStatus.PENDING);
    if (status != null) {
      doc.getRootElement().addContent(status);
    }
    status = (Element)statusNodeMap.get(BillingStatus.COMPLETED);
    if (status != null) {
      doc.getRootElement().addContent(status);      

      // Add non-empty labNodes onto status
      Map labNodeMap = (Map)statusToLabNodeMap.get(BillingStatus.COMPLETED);
      for(Iterator i1 = labNodeMap.keySet().iterator(); i1.hasNext();) {
        String key = (String)i1.next();
        Element labNode = (Element)labNodeMap.get(key);
        if (labNode.hasChildren()) {
          status.addContent(labNode);
        }
      }
    }
    status = (Element)statusNodeMap.get(BillingStatus.APPROVED);
    if (status != null) {
      doc.getRootElement().addContent(status);      

      // Add non-empty labNodes onto status
      Map labNodeMap = (Map)statusToLabNodeMap.get(BillingStatus.APPROVED);
      for(Iterator i1 = labNodeMap.keySet().iterator(); i1.hasNext();) {
        String key = (String)i1.next();
        Element labNode = (Element)labNodeMap.get(key);
        if (labNode.hasChildren()) {
          status.addContent(labNode);
        }
      }      
    }


    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);

    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetBillingRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetBillingRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetBillingRequestList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

}