package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.NumberFormat;
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
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    billingItemFilter = new BillingItemFilter();
    HashMap errors = this.loadDetailObject(request, billingItemFilter);
    this.addInvalidFields(errors);
    
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
    
    String prevRequestNumber = "";
    Integer prevIdLab = new Integer(-1);
    Integer prevIdBillingAccount = new Integer(-1);
    
    Element requestNode = null;
    
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    boolean firstTime = true;
    BigDecimal totalPrice = new BigDecimal("0");
    totalPrice.setScale(2);
    for(Iterator i = billingItems.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      String codeBillingStatus   = (String)row[0]  == null ? ""  : (String)row[0];
      Integer idRequest          = (Integer)row[1];
      String requestNumber       = (String)row[2]  == null ? ""  : (String)row[2];
      Integer idLab              = (Integer)row[3];
      String codeRequestCategory = (String)row[4]  == null ? ""  : (String)row[4];
      String labLastName         = (String)row[5]  == null ? ""  : (String)row[5];
      String labFirstName        = (String)row[6]  == null ? ""  : (String)row[6];
      AppUser submitter          = (AppUser)row[7];
      BillingItem billingItem    = (BillingItem)row[8];
      
      String labName = Lab.formatLabName(labLastName, labFirstName);
      
      if (!requestNumber.equals(prevRequestNumber) || 
          !prevIdLab.equals(billingItem.getIdLab()) ||
          !prevIdBillingAccount.equals(billingItem.getIdBillingAccount())) {
        requestNode = new Element("Request");
        requestNode.setAttribute("idRequest", idRequest.toString());
        requestNode.setAttribute("idLab", billingItem.getIdLab().toString());
        requestNode.setAttribute("requestNumber", requestNumber);
        requestNode.setAttribute("label", requestNumber);
        requestNode.setAttribute("codeRequestCategory", codeRequestCategory);        
        requestNode.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
        requestNode.setAttribute("billingLabName", labName);        
        requestNode.setAttribute("billingAccountName", billingItem.getBillingAccount().getAccountName());       
        requestNode.setAttribute("idBillingAccount", billingItem.getBillingAccount().getIdBillingAccount().toString() );       
        requestNode.setAttribute("isDirty", "N");
        
        doc.getRootElement().addContent(requestNode);
        
        totalPrice = new BigDecimal(0);
        totalPrice.setScale(2);
      }
      
      Element billingItemNode = billingItem.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
      if (billingItem.getTotalPrice() != null) {
        billingItemNode.setAttribute("totalPrice", nf.format(billingItem.getTotalPrice().doubleValue()));        
      }
      requestNode.addContent(billingItemNode);
      
      totalPrice = totalPrice.add(billingItem.getTotalPrice() != null ? billingItem.getTotalPrice() : new BigDecimal(0));
      requestNode.setAttribute("totalPrice", nf.format(totalPrice.doubleValue()));
      
      
      prevRequestNumber = requestNumber;
      prevIdLab = billingItem.getIdLab();
      prevIdBillingAccount = billingItem.getIdBillingAccount();
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