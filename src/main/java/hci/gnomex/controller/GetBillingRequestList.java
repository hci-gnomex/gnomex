package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.DiskUsageByMonth;
import hci.gnomex.model.Lab;
import hci.gnomex.model.ProductOrder;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.Util;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
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
import org.apache.log4j.Logger;

public class GetBillingRequestList extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(GetBillingRequestList.class);

  private static final String DELIM = "\t";
  private static final String DISK_USAGE_PREFIX = "ZZDiskUsage";
  private static final String PRODUCT_ORDER_PREFIX = "ZZProductOrder";

  private BillingItemFilter billingItemFilter;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    billingItemFilter = new BillingItemFilter(this.getSecAdvisor());
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
      LOG.info("Query: " + buf.toString());
      List newRequests = sess.createQuery(buf.toString()).list();


      Element statusNode = new Element("Status");

      TreeMap requestToStatusMap = new TreeMap(new RequestNumberBilledComparator());
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

          Integer idRequest           = (Integer)row[0];
          String requestNumber        = (String)row[1]  == null ? ""  : (String)row[1];
          String codeRequestCategory  = (String)row[2]  == null ? ""  : (String)row[2];
          String idBillingAccount     = (Integer)row[3] == null ? ""  : ((Integer)row[3]).toString();
          String labLastName          = (String)row[4]  == null ? ""  : (String)row[4];
          String labFirstName         = (String)row[5]  == null ? ""  : (String)row[5];
          AppUser submitter           = (AppUser)row[6];
          java.util.Date createDate   = (java.util.Date)row[7];
          Date completedDate          = (Date)row[8];
          BillingAccount billingAcct  = (BillingAccount)row[9];
          String labIsExternalPricing = (String)row[10];
          String labIsExternalPricingCommercial = (String)row[11];

          String labName = Lab.formatLabName(labLastName, labFirstName);


          String toolTip = requestNumber + " " + labName;
          if (createDate != null) {
            toolTip += "\nsubmitted " + this.formatDate(createDate, this.DATE_OUTPUT_DASH_SHORT);
          }
          if (completedDate != null) {
            toolTip += ", completed  " + this.formatDate(completedDate, this.DATE_OUTPUT_DASH_SHORT);
          }

          RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);

          Element node = new Element("Request");
          node.setAttribute("idRequest", idRequest.toString());
          node.setAttribute("idProductOrder", "");
          node.setAttribute("requestNumber", requestNumber);
          node.setAttribute("label", requestNumber);
          node.setAttribute("codeRequestCategory", codeRequestCategory);
          node.setAttribute("icon", requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
          node.setAttribute("type", requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
          node.setAttribute("idBillingAccount", idBillingAccount);
          node.setAttribute("labName", labName != null ? labName : "");
          node.setAttribute("toolTip", toolTip);
          node.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
          node.setAttribute("codeBillingStatus", BillingStatus.NEW);
          node.setAttribute("createDate", createDate != null ? this.formatDate(createDate, this.DATE_OUTPUT_DASH) :  "");
          node.setAttribute("completedDate", completedDate != null ? this.formatDate(completedDate, this.DATE_OUTPUT_DASH) : "");
          node.setAttribute("isExternalPricing", labIsExternalPricing != null ? labIsExternalPricing : "N");
          node.setAttribute("isExternalPricingCommercial", labIsExternalPricingCommercial != null ? labIsExternalPricingCommercial : "N");
          node.setAttribute("hasBillingItems", "N");

          String labBillingName = labName;
          if ( billingAcct != null ) {
            labBillingName +=  " (" + billingAcct.getAccountNameAndNumber() + ")";
          }

          String requestNumberBilled = requestNumber + DELIM + labBillingName;

          // Hash the status node.
          List statusList = (List)requestToStatusMap.get(requestNumberBilled);
          if (statusList == null) {
            statusList = new ArrayList<String>();
            requestToStatusMap.put(requestNumberBilled, statusList);
          }
          statusList.add(BillingStatus.PENDING);

          // There can be multiple requests nodes for a given request number when
          // the request's billing items are split among multiple billing 
          // accounts.  Keep a hash map of these different request nodes.

          TreeMap requestNodes = (TreeMap)requestNodeMap.get(requestNumberBilled);
          if (requestNodes == null) {
            requestNodes = new TreeMap(new RequestNumberBilledComparator());
            requestNodeMap.put(requestNumberBilled, requestNodes);
          }
          requestNodes.put(requestNumberBilled, node);


        }

      }


      buf = billingItemFilter.getBillingRequestQuery();
      LOG.info("Query: " + buf.toString());
      List billingItemRequests = sess.createQuery(buf.toString()).list();
      String prevCodeBillingStatus = "NEW";
      // 
      // Query all requests with billing items.  Determine status by looking
      // at all of billing items for request.  
      // Hash nodes for lab/billing account, status.
      //
      for(Iterator i = billingItemRequests.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();

        String codeBillingStatus    = (String)row[0]  == null ? ""  : (String)row[0];
        Integer idRequest           = (Integer)row[1];
        String requestNumber        = (String)row[2]  == null ? ""  : (String)row[2];
        String codeRequestCategory  = (String)row[3]  == null ? ""  : (String)row[3];
        Integer idLab               = (Integer)row[4];
        String labLastName          = (String)row[5]  == null ? ""  : (String)row[5];
        String labFirstName         = (String)row[6]  == null ? ""  : (String)row[6];
        AppUser submitter           = (AppUser)row[7];
        java.util.Date createDate   = (java.util.Date)row[8];
        Date completedDate          = (Date)row[9];
        BillingAccount billingAcct  = (BillingAccount)row[10];
        String labIsExternalPricing = (String)row[11];
        String labIsExternalPricingCommercial = (String)row[12];
        Integer idInvoice           = (Integer)row[13];
        String contactEmail         = (String)row[14];
        String billingContactEmail  = (String)row[15];

        String labName = Lab.formatLabName(labLastName, labFirstName);
        String billingEmail = Lab.formatBillingNotificationEmail(contactEmail, billingContactEmail);


        String toolTip = requestNumber + " " + labName;

        String labBillingName = labName + " (" + billingAcct.getAccountNameAndNumber() + ")";
        String requestNumberBilled = requestNumber + DELIM + labBillingName;

        if (createDate != null) {
          toolTip += "\nsubmitted " + this.formatDate(createDate, this.DATE_OUTPUT_DASH_SHORT);
        }
        if (completedDate != null) {
          toolTip += ", completed  " + this.formatDate(completedDate, this.DATE_OUTPUT_DASH_SHORT);
        }


        List statusList = (List)requestToStatusMap.get(requestNumberBilled);
        if (statusList == null) {
          statusList = new ArrayList<String>();
          requestToStatusMap.put(requestNumberBilled, statusList);
        }
        statusList.add(codeBillingStatus);

        Map labNodeMap = (Map)statusToLabNodeMap.get(codeBillingStatus);
        if (labNodeMap == null) {
          labNodeMap = new TreeMap();
          statusToLabNodeMap.put(codeBillingStatus, labNodeMap);
        }
        Element labNode = (Element)labNodeMap.get(labBillingName);
        if (labNode == null) {
          labNode = new Element("Lab");
          labNode.setAttribute("label", labBillingName);
          labNode.setAttribute("dataTip", labBillingName);
          labNode.setAttribute("idLab", idLab != null ? idLab.toString() : "");
          labNode.setAttribute("name", labName);
          labNode.setAttribute("idBillingAccount", billingAcct.getIdBillingAccount().toString());
          labNode.setAttribute("billingAccountName", billingAcct.getAccountNameAndNumber());
          labNode.setAttribute("contactEmail", contactEmail != null ? contactEmail : "");
          labNode.setAttribute("billingNotificationEmail", billingEmail != null ? billingEmail : "");
          if (idInvoice != null) {
            labNode.setAttribute("idInvoice", idInvoice.toString());
          }
          labNodeMap.put(labBillingName, labNode);
        }

        if (!prevCodeBillingStatus.equals(codeBillingStatus)) {
          statusNode = new Element("Status");
          statusNode.setAttribute("label",  dh.getBillingStatus(codeBillingStatus));
          statusNode.setAttribute("status", codeBillingStatus);
          statusNodeMap.put(codeBillingStatus, statusNode);
        }



        RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);

        Element node = new Element("Request");
        node.setAttribute("idRequest", idRequest.toString());
        node.setAttribute("idProductOrder", "");
        node.setAttribute("label", requestNumber);        
        node.setAttribute("requestNumber", requestNumber);        
        node.setAttribute("toolTip", toolTip);
        node.setAttribute("codeRequestCategory", codeRequestCategory);
        node.setAttribute("icon", requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
        node.setAttribute("type", requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
        node.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
        node.setAttribute("labBillingName", labBillingName);
        node.setAttribute("idLab", idLab != null ? idLab.toString() : "");
        node.setAttribute("labName", labName != null ? labName : "");
        node.setAttribute("idBillingAccount", billingAcct.getIdBillingAccount().toString());
        node.setAttribute("billingAccountName", billingAcct.getAccountNameAndNumber());
        node.setAttribute("isExternalPricing", labIsExternalPricing != null ? labIsExternalPricing : "N");
        node.setAttribute("isExternalPricingCommercial", labIsExternalPricingCommercial != null ? labIsExternalPricingCommercial : "N");
        node.setAttribute("hasBillingItems", "Y");
        if (idInvoice != null) {
          node.setAttribute("idInvoice", idInvoice.toString());
        }

        // There can be multiple requests nodes for a given request number when
        // the request's billing items are split among multiple billing 
        // accounts.  Keep a hash map of these different request nodes.
        TreeMap requestNodes = (TreeMap)requestNodeMap.get(requestNumberBilled);
        if (requestNodes == null) {
          requestNodes = new TreeMap(new RequestNumberBilledComparator());
          requestNodeMap.put(requestNumberBilled, requestNodes);
        }
        requestNodes.put(requestNumberBilled, node);



        prevCodeBillingStatus = codeBillingStatus;
      }




      buf = billingItemFilter.getBillingDiskUsageQuery();
      LOG.info("Query: " + buf.toString());
      List billingItemDiskUsage = sess.createQuery(buf.toString()).list();
      prevCodeBillingStatus = "NEW";
      // 
      // Query all disk usage rows with billing items.  Determine status by looking
      // at all of billing items for disk usage (should only be one but...)  
      // Hash nodes for lab/billing account, status.
      //
      for(Iterator i = billingItemDiskUsage.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();

        String codeBillingStatus    = (String)row[0]  == null ? ""  : (String)row[0];
        Integer idDiskUsage         = (Integer)row[1];
        Integer idLab               = (Integer)row[2];
        String labLastName          = (String)row[3]  == null ? ""  : (String)row[3];
        String labFirstName         = (String)row[4]  == null ? ""  : (String)row[4];
        java.util.Date asOfDate     = (java.util.Date)row[5];
        BillingAccount billingAcct  = (BillingAccount)row[6];
        String labIsExternalPricing = (String)row[7];
        String labIsExternalPricingCommercial = (String)row[8];
        Integer idInvoice           = (Integer)row[9];
        String contactEmail         = (String)row[10];

        String labName = Lab.formatLabName(labLastName, labFirstName);


        String toolTip = "Disk Usage Charge for " + labName;

        String labBillingName = labName + " (" + billingAcct.getAccountNameAndNumber() + ")";
        String requestNumberBilled = DISK_USAGE_PREFIX + idDiskUsage.toString() + DELIM + labBillingName;

        if (asOfDate != null) {
          toolTip += "\ncomputed as of " + this.formatDate(asOfDate, this.DATE_OUTPUT_DASH_SHORT);
        }

        List statusList = (List)requestToStatusMap.get(requestNumberBilled);
        if (statusList == null) {
          statusList = new ArrayList<String>();
          requestToStatusMap.put(requestNumberBilled, statusList);
        }
        statusList.add(codeBillingStatus);

        Map labNodeMap = (Map)statusToLabNodeMap.get(codeBillingStatus);
        if (labNodeMap == null) {
          labNodeMap = new TreeMap();
          statusToLabNodeMap.put(codeBillingStatus, labNodeMap);
        }
        Element labNode = (Element)labNodeMap.get(labBillingName);
        if (labNode == null) {
          labNode = new Element("Lab");
          labNode.setAttribute("label", labBillingName);
          labNode.setAttribute("dataTip", labBillingName);
          labNode.setAttribute("idLab", idLab != null ? idLab.toString() : "");
          labNode.setAttribute("name", labName);
          labNode.setAttribute("idBillingAccount", billingAcct.getIdBillingAccount().toString());
          labNode.setAttribute("billingAccountName", billingAcct.getAccountNameAndNumber());
          labNode.setAttribute("contactEmail", contactEmail != null ? contactEmail : "");
          if (idInvoice != null) {
            labNode.setAttribute("idInvoice", idInvoice.toString());
          }
          labNodeMap.put(labBillingName, labNode);
        }

        if (!prevCodeBillingStatus.equals(codeBillingStatus)) {
          if (!statusNodeMap.containsKey(codeBillingStatus)) {
            statusNode = new Element("Status");
            statusNode.setAttribute("label",  dh.getBillingStatus(codeBillingStatus));
            statusNode.setAttribute("status", codeBillingStatus);
            statusNodeMap.put(codeBillingStatus, statusNode);
          } else {
            statusNode = (Element)statusNodeMap.get(codeBillingStatus);
          }
        }

        Element node = new Element("Request");
        node.setAttribute("idRequest", idDiskUsage.toString());
        node.setAttribute("idProductOrder", "");
        node.setAttribute("label", "Disk Usage");        
        node.setAttribute("requestNumber", "Disk Usage");        
        node.setAttribute("toolTip", toolTip);
        node.setAttribute("codeRequestCategory", DiskUsageByMonth.DISK_USAGE_REQUEST_CATEGORY);
        node.setAttribute("icon", DiskUsageByMonth.DISK_USAGE_ICON);
        node.setAttribute("type", "Disk Usage");
        node.setAttribute("submitter", "");
        node.setAttribute("labBillingName", labBillingName);
        node.setAttribute("idLab", idLab != null ? idLab.toString() : "");
        node.setAttribute("labName", labName != null ? labName : "");
        node.setAttribute("idBillingAccount", billingAcct.getIdBillingAccount().toString());
        node.setAttribute("billingAccountName", billingAcct.getAccountNameAndNumber());
        node.setAttribute("isExternalPricing", labIsExternalPricing != null ? labIsExternalPricing : "N");
        node.setAttribute("isExternalPricingCommercial", labIsExternalPricingCommercial != null ? labIsExternalPricingCommercial : "N");
        node.setAttribute("hasBillingItems", "Y");
        if (idInvoice != null) {
          node.setAttribute("idInvoice", idInvoice.toString());
        }

        // There can be multiple requests nodes for a given request number when
        // the request's billing items are split among multiple billing 
        // accounts.  This should never happen for disk usage, but keeping the code anyway.
        TreeMap requestNodes = (TreeMap)requestNodeMap.get(requestNumberBilled);
        if (requestNodes == null) {
          requestNodes = new TreeMap(new RequestNumberBilledComparator());
          requestNodeMap.put(requestNumberBilled, requestNodes);
        }
        requestNodes.put(requestNumberBilled, node);



        prevCodeBillingStatus = codeBillingStatus;
      }

      buf = billingItemFilter.getBillingProductOrderQuery();
      LOG.info("Query: " + buf.toString());
      List billingItemProductOrder = sess.createQuery(buf.toString()).list();
      prevCodeBillingStatus = "NEW";
      List labNames = new ArrayList();
      // 
      // Query all product order rows with billing items.  Determine status by looking
      // at all of billing items for product order (should only be one but...)  
      // Hash nodes for lab/billing account, status.
      //
      for(Iterator i = billingItemProductOrder.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();

        String codeBillingStatus    = (String)row[0]  == null ? ""  : (String)row[0];
        String  productOrderNumber  = (String)row[1] == null ? "" : (String)row[1];
        Integer idProductOrder      = (Integer)row[2];
        Integer idLab               = (Integer)row[3];
        String labLastName          = (String)row[4]  == null ? ""  : (String)row[4];
        String labFirstName         = (String)row[5]  == null ? ""  : (String)row[5];
        java.util.Date submitDate   = (java.util.Date)row[6];
        BillingAccount billingAcct  = (BillingAccount)row[7];
        String labIsExternalPricing = (String)row[8];
        String labIsExternalPricingCommercial = (String)row[9];
        Integer idInvoice           = (Integer)row[10];
        String contactEmail         = (String)row[11];
        Integer idCoreFacility      = (Integer)row[12];

        String labName = Lab.formatLabName(labLastName, labFirstName);


        String toolTip = "Product Order Charge for " + labName;

        String labBillingName = labName + " (" + billingAcct.getAccountNameAndNumber() + ")";
        String requestNumberBilled = PRODUCT_ORDER_PREFIX + idProductOrder.toString() + DELIM + labBillingName;

        if (submitDate != null) {
          toolTip += "\nsubmitted on " + this.formatDate(submitDate, this.DATE_OUTPUT_DASH_SHORT);
        }

        List statusList = (List)requestToStatusMap.get(requestNumberBilled);
        if (statusList == null) {
          statusList = new ArrayList<String>();
          requestToStatusMap.put(requestNumberBilled, statusList);
        }
        statusList.add(codeBillingStatus);

        Map labNodeMap = (Map)statusToLabNodeMap.get(codeBillingStatus);
        if (labNodeMap == null) {
          labNodeMap = new TreeMap();
          statusToLabNodeMap.put(codeBillingStatus, labNodeMap);
        }
        Element labNode = (Element)labNodeMap.get(labBillingName);
        if (labNode == null) {
          labNode = new Element("Lab");
          labNode.setAttribute("label", labBillingName);
          labNode.setAttribute("dataTip", labBillingName);
          labNode.setAttribute("idLab", idLab != null ? idLab.toString() : "");
          labNode.setAttribute("name", labName);
          labNode.setAttribute("idBillingAccount", billingAcct.getIdBillingAccount().toString());
          labNode.setAttribute("billingAccountName", billingAcct.getAccountNameAndNumber());
          labNode.setAttribute("contactEmail", contactEmail != null ? contactEmail : "");
          if (idInvoice != null) {
            labNode.setAttribute("idInvoice", idInvoice.toString());
          }
          labNodeMap.put(labBillingName, labNode);
        }

        if (!prevCodeBillingStatus.equals(codeBillingStatus)) {
          if (!statusNodeMap.containsKey(codeBillingStatus)) {
            statusNode = new Element("Status");
            statusNode.setAttribute("label",  dh.getBillingStatus(codeBillingStatus));
            statusNode.setAttribute("status", codeBillingStatus);
            statusNodeMap.put(codeBillingStatus, statusNode);
          } else {
            statusNode = (Element)statusNodeMap.get(codeBillingStatus);
          }
        }

        Element node = new Element("Request");
        node.setAttribute("idRequest", idProductOrder.toString());
        node.setAttribute("idProductOrder", idProductOrder.toString());
        node.setAttribute("idCoreFacility", idCoreFacility.toString());
        node.setAttribute("label", "Product Order " + (productOrderNumber.equals("") ? idProductOrder.toString() : productOrderNumber));       
        node.setAttribute("requestNumber", productOrderNumber.equals("") ? idProductOrder.toString() : productOrderNumber);
        node.setAttribute("toolTip", toolTip);
        node.setAttribute("codeRequestCategory", ProductOrder.PRODUCT_ORDER_REQUEST_CATEGORY);
        node.setAttribute("icon", ProductOrder.PRODUCT_ORDER_ICON);
        node.setAttribute("type", "Product Order");
        node.setAttribute("submitter", "");
        node.setAttribute("labBillingName", labBillingName);
        node.setAttribute("idLab", idLab != null ? idLab.toString() : "");
        node.setAttribute("labName", labName != null ? labName : "");
        node.setAttribute("idBillingAccount", billingAcct.getIdBillingAccount().toString());
        node.setAttribute("billingAccountName", billingAcct.getAccountNameAndNumber());
        node.setAttribute("isExternalPricing", labIsExternalPricing != null ? labIsExternalPricing : "N");
        node.setAttribute("isExternalPricingCommercial", labIsExternalPricingCommercial != null ? labIsExternalPricingCommercial : "N");
        node.setAttribute("hasBillingItems", "Y");
        if (idInvoice != null) {
          node.setAttribute("idInvoice", idInvoice.toString());
        }

        // There can be multiple requests nodes for a given request number when
        // the request's billing items are split among multiple billing 
        // accounts.  This should never happen for disk usage, but keeping the code anyway.
        TreeMap requestNodes = (TreeMap)requestNodeMap.get(requestNumberBilled);
        if (requestNodes == null) {
          requestNodes = new TreeMap(new RequestNumberBilledComparator());
          requestNodeMap.put(requestNumberBilled, requestNodes);
        }
        requestNodes.put(requestNumberBilled, node);

        labNames.add(labBillingName);

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
        String requestNumberBilled = (String)i.next();

        List statusList = (List)requestToStatusMap.get(requestNumberBilled);
        Map requestNodes = (Map)requestNodeMap.get(requestNumberBilled);

        if (requestNodes == null) {
          System.out.println("Cannot find request nodes for " + requestNumberBilled);
          continue;
        }


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
          if (codeBillingStatus == null) {
            for(Iterator i1 = statusList.iterator(); i1.hasNext();) {
              String code = (String)i1.next();
              if (code.equals(BillingStatus.APPROVED_PO)) {
                codeBillingStatus = code;
                break;
              }
            }

          }
          if (codeBillingStatus == null) {
            for(Iterator i1 = statusList.iterator(); i1.hasNext();) {
              String code = (String)i1.next();
              if (code.equals(BillingStatus.APPROVED_CC)) {
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
            if (labNode == null) {
              labNode = new Element("Lab");
              labNode.setAttribute("label", requestNode.getAttributeValue("labBillingName"));
              labNode.setAttribute("dataTip", requestNode.getAttributeValue("labBillingName"));
              labNode.setAttribute("idLab", requestNode.getAttributeValue("idLab"));
              labNode.setAttribute("name", requestNode.getAttributeValue("labBillingName"));
              labNode.setAttribute("idBillingAccount", requestNode.getAttributeValue("idBillingAccount"));
              labNode.setAttribute("billingAccountName", requestNode.getAttributeValue("billingAccountName"));
              labNodeMap.put(requestNode.getAttributeValue("labBillingName"), labNode);
              labNode.addContent(requestNode);
            } else {
              labNode.addContent(requestNode);
            }
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
          if (labNode.getChildren().size() > 0) {
            status.addContent(labNode);
          }
        }
      }
      status = (Element)statusNodeMap.get(BillingStatus.APPROVED_CC);
      if (status != null) {
        doc.getRootElement().addContent(status);      

        // Add non-empty labNodes onto status
        Map labNodeMap = (Map)statusToLabNodeMap.get(BillingStatus.APPROVED_CC);
        for(Iterator i1 = labNodeMap.keySet().iterator(); i1.hasNext();) {
          String key = (String)i1.next();
          Element labNode = (Element)labNodeMap.get(key);
          if (labNode.getChildren().size() > 0) {
            status.addContent(labNode);
          }
        }      
      }
      status = (Element)statusNodeMap.get(BillingStatus.APPROVED_PO);
      if (status != null) {
        doc.getRootElement().addContent(status);      

        // Add non-empty labNodes onto status
        Map labNodeMap = (Map)statusToLabNodeMap.get(BillingStatus.APPROVED_PO);
        for(Iterator i1 = labNodeMap.keySet().iterator(); i1.hasNext();) {
          String key = (String)i1.next();
          Element labNode = (Element)labNodeMap.get(key);
          if (labNode.getChildren().size() > 0) {
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
          if (labNode.getChildren().size() > 0) {
            status.addContent(labNode);
          }
        }      
      }


      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    } catch (Exception e) {
      LOG.error("An exception has occurred in GetBillingRequestList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        //closeReadOnlyHibernateSession;        
      } catch(Exception e) {
        LOG.error("An exception has occurred in GetBillingRequestList ", e);
      }
    }

    return this;
  }

  public static class  RequestNumberBilledComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;


      if (key1.startsWith(DISK_USAGE_PREFIX) || key2.startsWith(DISK_USAGE_PREFIX)) {
        return key1.compareTo(key2);
      }

      if (key1.startsWith(PRODUCT_ORDER_PREFIX) || key2.startsWith(PRODUCT_ORDER_PREFIX)) {
        return key1.compareTo(key2);
      }

      String[] tokens1 = key1.split(DELIM);
      String[] tokens2 = key2.split(DELIM);

      String reqNumber1    = tokens1[0];
      String remainder1    = tokens1[1];

      String reqNumber2    = tokens2[0];
      String remainder2    = tokens2[1];

      if (reqNumber1.equals(reqNumber2)) {
        return remainder1.compareTo(remainder2);        
      }
      return Util.compareRequestNumbers(reqNumber1, reqNumber2);      
    }
  }

}