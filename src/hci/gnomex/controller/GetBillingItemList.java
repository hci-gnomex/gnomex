package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.model.Lab;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetBillingItemList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetBillingItemList.class);
  
  private BillingItemFilter billingItemFilter;
  private String            showOtherBillingItems = "";
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    billingItemFilter = new BillingItemFilter();
    HashMap errors = this.loadDetailObject(request, billingItemFilter);
    this.addInvalidFields(errors);
    
    if (request.getParameter("showOtherBillingItems") != null && !request.getParameter("showOtherBillingItems").equals("")) {
      showOtherBillingItems = request.getParameter("showOtherBillingItems");
    }
    
    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
      this.addInvalidField("permission", "Insufficient permission to manage billing items");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    DictionaryHelper dh = DictionaryHelper.getInstance(sess);
    
    Document doc = new Document(new Element("BillingItemList"));
    
    StringBuffer buf = billingItemFilter.getBillingItemQuery();
    log.info("Query: " + buf.toString());
    List billingItems = (List)sess.createQuery(buf.toString()).list();
    
    // When filtering by billing period,
    // get the list of ids for the requests of these billing items
    // We will use this id list to augment the billing item list with
    // billing items from other billing periods
    HashMap otherBillingItemMap = new HashMap();
    if (billingItemFilter.getIdBillingPeriod() != null && this.showOtherBillingItems.equals("Y")) {
      HashMap idRequestMap = new HashMap();
      for(Iterator i = billingItems.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        Integer idRequest           = (Integer)row[1];
        idRequestMap.put(idRequest, null);
      }
      buf = billingItemFilter.getRelatedBillingItemQuery(idRequestMap.keySet());
      List relatedBillingItems = (List)sess.createQuery(buf.toString()).list();
      // Has "other" billing items by idRequest
      for(Iterator i = relatedBillingItems.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        Integer idRequest           = (Integer)row[1];
        List theRows = (List)otherBillingItemMap.get(idRequest);
        if (theRows == null) {
          theRows = new ArrayList();
          otherBillingItemMap.put(idRequest, theRows);
        }
        theRows.add(row);
      }      
    }

    

    Integer prevIdRequest = new Integer(-1);
    Integer prevIdLab = new Integer(-1);
    Integer prevIdBillingAccount = new Integer(-1);
    
    Element requestNode = null;
    
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    boolean firstTime = true;
    BigDecimal totalPrice = new BigDecimal("0");
    totalPrice.setScale(2);
    for(Iterator i = billingItems.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      String codeBillingStatus    = (String)row[0]  == null ? ""  : (String)row[0];
      Integer idRequest           = (Integer)row[1];
      String requestNumber        = (String)row[2]  == null ? ""  : (String)row[2];
      Integer idLab               = (Integer)row[3];
      String codeRequestCategory  = (String)row[4]  == null ? ""  : (String)row[4];
      String labLastName          = (String)row[5]  == null ? ""  : (String)row[5];
      String labFirstName         = (String)row[6]  == null ? ""  : (String)row[6];
      AppUser submitter           = (AppUser)row[7];
      BillingItem billingItem     = (BillingItem)row[8];
      String labIsExternalPricing = (String)row[9];

      String labName = Lab.formatLabName(labLastName, labFirstName);
      
      if (!prevIdRequest.equals(idRequest) || 
          !prevIdLab.equals(billingItem.getIdLab()) ||
          !prevIdBillingAccount.equals(billingItem.getIdBillingAccount())) {

        // Before creating a new request node, attach all of the "other" billing
        // items from another billing period
        if (requestNode != null) {
          List otherBillingItemRows = (List)otherBillingItemMap.get(prevIdRequest);
          if (otherBillingItemRows != null) {
            for(Iterator i1 = otherBillingItemRows.iterator(); i1.hasNext();) {
              Object[] otherRow = (Object[])i1.next();
              BillingItem otherBillingItem = (BillingItem)otherRow[8];
              Element otherBillingItemNode = otherBillingItem.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
              otherBillingItemNode.setAttribute("other", "Y");
              otherBillingItemNode.setAttribute("isDirty","N");
              requestNode.addContent(otherBillingItemNode);
            }
          }
        }
        
        RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
        
        // Create the new request node
        requestNode = new Element("Request");
        requestNode.setAttribute("idRequest", idRequest.toString());
        requestNode.setAttribute("idLab", billingItem.getIdLab().toString());
        requestNode.setAttribute("requestNumber", requestNumber);
        requestNode.setAttribute("label", requestNumber);
        requestNode.setAttribute("codeRequestCategory", codeRequestCategory);        
        requestNode.setAttribute("icon", requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
        requestNode.setAttribute("type", requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
        requestNode.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
        requestNode.setAttribute("billingLabName", labName);        
        requestNode.setAttribute("billingAccountName", billingItem.getBillingAccount().getAccountNameAndNumber());       
        requestNode.setAttribute("idBillingAccount", billingItem.getBillingAccount().getIdBillingAccount().toString() );       
        requestNode.setAttribute("isExternalPricing", labIsExternalPricing != null ? labIsExternalPricing : "N");
        requestNode.setAttribute("isDirty", "N");

        
        doc.getRootElement().addContent(requestNode);
        
        totalPrice = new BigDecimal(0);
        totalPrice.setScale(2);
      }
      
      // Attach the billing item
      Element billingItemNode = billingItem.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
      billingItemNode.setAttribute("other", "N");
      billingItemNode.setAttribute("isDirty","N");
      billingItemNode.setAttribute("currentCodeBillingStatus", billingItem.getCodeBillingStatus());
      if (billingItem.getTotalPrice() != null) {
        billingItemNode.setAttribute("totalPrice", nf.format(billingItem.getTotalPrice().doubleValue()));        
      }
      requestNode.addContent(billingItemNode);
      
      // Sum the price
      totalPrice = totalPrice.add(billingItem.getTotalPrice() != null ? billingItem.getTotalPrice() : new BigDecimal(0));
      requestNode.setAttribute("totalPrice", nf.format(totalPrice.doubleValue()));
      
      
      
      prevIdRequest = billingItem.getIdRequest();
      prevIdLab = billingItem.getIdLab();
      prevIdBillingAccount = billingItem.getIdBillingAccount();
    }

    // Before creating a new request node, attach all of the "other" billing
    // items from another billing period
    if (requestNode != null) {
      List otherBillingItemRows = (List)otherBillingItemMap.get(prevIdRequest);
      if (otherBillingItemRows != null) {
        for(Iterator i1 = otherBillingItemRows.iterator(); i1.hasNext();) {
          Object[] otherRow = (Object[])i1.next();
          BillingItem otherBillingItem = (BillingItem)otherRow[8];
          Element otherBillingItemNode = otherBillingItem.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
          otherBillingItemNode.setAttribute("other", "Y");
          otherBillingItemNode.setAttribute("isDirty", "N");
          requestNode.addContent(otherBillingItemNode);
        }
      }
    }

    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetBillingItemList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetBillingItemList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetBillingItemList ", e);
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

  
  public BillingItemFilter getBillingItemFilter() {
    return billingItemFilter;
  }

  
  public void setBillingItemFilter(BillingItemFilter billingItemFilter) {
    this.billingItemFilter = billingItemFilter;
  }

}