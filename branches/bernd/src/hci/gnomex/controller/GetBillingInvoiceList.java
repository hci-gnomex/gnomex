package hci.gnomex.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingItemFilter;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.Invoice;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetBillingInvoiceList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetBillingInvoiceList.class);
  
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
    
    StringBuffer buf = billingItemFilter.getBillingInvoiceQuery();
    log.info("Query: " + buf.toString());
    List invoices = sess.createQuery(buf.toString()).list();

    Document doc = new Document(new Element("BillingInvoiceList"));
    for(Iterator i = invoices.iterator(); i.hasNext();) {
      Invoice invoice = (Invoice)i.next();
      Element invoiceNode = invoice.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
      doc.getRootElement().addContent(invoiceNode);
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