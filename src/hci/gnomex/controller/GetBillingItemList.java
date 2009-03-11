package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
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
    Element requestNode = null;
    
    NumberFormat nf = NumberFormat.getCurrencyInstance();
    boolean firstTime = true;
    for(Iterator i = billingItems.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      String codeBillingStatus   = (String)row[0]  == null ? ""  : (String)row[0];
      Integer idRequest          = (Integer)row[1];
      String requestNumber       = (String)row[2]  == null ? ""  : (String)row[2];
      String codeRequestCategory = (String)row[3]  == null ? ""  : (String)row[3];
      String labName             = (String)row[4]  == null ? ""  : (String)row[4];
      AppUser submitter          = (AppUser)row[5];
      BillingItem billingItem    = (BillingItem)row[6];
      
      
      if (!requestNumber.equals(prevRequestNumber)) {
        requestNode = new Element("Request");
        requestNode.setAttribute("idRequest", idRequest.toString());
        requestNode.setAttribute("requestNumber", requestNumber);
        requestNode.setAttribute("label", requestNumber);
        requestNode.setAttribute("codeRequestCategory", codeRequestCategory);        
        requestNode.setAttribute("requestLabName", labName);        
        requestNode.setAttribute("submitter", submitter != null ? submitter.getDisplayName() : "");
        requestNode.setAttribute("billingAccountName", billingItem.getBillingAccount().getAccountName());       
        requestNode.setAttribute("idBillingAccount", billingItem.getBillingAccount().getIdBillingAccount().toString() );       
        requestNode.setAttribute("isDirty", "N");
        
        
        doc.getRootElement().addContent(requestNode);
      }
      
      Element billingItemNode = billingItem.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
      if (billingItem.getTotalPrice() != null) {
        billingItemNode.setAttribute("totalPrice", nf.format(billingItem.getTotalPrice().doubleValue()));        
      }
      requestNode.addContent(billingItemNode);
      
      prevRequestNumber = requestNumber;
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