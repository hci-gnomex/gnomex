package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.billing.BillingPlugin;
import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class CreateBillingItems extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CreateBillingItems.class);
  
  private Integer idRequest;
  private Integer idBillingPeriod;
  

  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    }
    
    
    if (request.getParameter("idBillingPeriod") != null && !request.getParameter("idBillingPeriod").equals("")) {
      idBillingPeriod = new Integer(request.getParameter("idBillingPeriod"));
    }
    
    if (idRequest == null) {
      this.addInvalidField("idRequest", "idRequest is required.");
    }
    
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
        if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
          
          List billingItems = new ArrayList();
 
          // Read (and cache) the billing templates, categories, and prices
          DictionaryHelper dh = DictionaryHelper.getInstance(sess);
          dh.loadBillingTemplates(sess);

          // Get the current billing period
          BillingPeriod billingPeriod = null;
          if (idBillingPeriod == null) {
            billingPeriod = dh.getCurrentBillingPeriod();
          } else {
            billingPeriod = dh.getBillingPeriod(idBillingPeriod);
          }
          if (billingPeriod == null) {
            throw new RollBackCommandException("Cannot find current billing period in dictionary");
          }

          // Read the experiment
          Request request = (Request)sess.get(Request.class, idRequest);
          NumberFormat nf = NumberFormat.getCurrencyInstance();
          
          // Find the appropriate billing template
          BillingTemplate billingTemplate = null;
          for(Iterator i = dh.getBillingTemplates().iterator(); i.hasNext();) {
            BillingTemplate bt = (BillingTemplate)i.next();
            
            if(bt.getCodeRequestCategory().equals(request.getCodeRequestCategory())) {
              billingTemplate = bt;
              break;
            }
          }
          
          if (billingTemplate != null) {
            
            List billingCategories = dh.getBillingCategories(billingTemplate.getIdBillingTemplate());
            
            for(Iterator i1 = billingCategories.iterator(); i1.hasNext();) {
              BillingCategory billingCategory = (BillingCategory)i1.next();

              List billingPrices = dh.getBillingPrices(billingCategory.getIdBillingCategory());
              
              // Instantiate plugin for billing category
              BillingPlugin plugin = null;
              try {
                plugin = (BillingPlugin)Class.forName(billingCategory.getPluginClassname()).newInstance();
              } catch(Exception e) {
                log.error("Unable to instantiate billing plugin " + billingCategory.getPluginClassname());
              }
              
              // Get the billing items
              if (plugin != null) {
                List billingItemsForCategory = plugin.createBillingItems(sess, billingPeriod, billingCategory, billingPrices, request);
                billingItems.addAll(billingItemsForCategory);                
              }
            }
            
          }
          
            
          Document doc = new Document(new Element("NewBilling"));
          
          
          Element requestNode = new Element("Request");
          requestNode.setAttribute("idRequest", request.getIdRequest().toString());
          requestNode.setAttribute("requestNumber", request.getNumber());
          requestNode.setAttribute("label", request.getNumber());
          requestNode.setAttribute("codeRequestCategory", request.getCodeRequestCategory());        
          requestNode.setAttribute("labName", request.getLabName());        
          requestNode.setAttribute("billingAccountName", request.getBillingAccount() != null ? request.getBillingAccount().getAccountName() : "");        
          requestNode.setAttribute("status", BillingStatus.NEW);       
          requestNode.setAttribute("isDirty", "Y");
          doc.getRootElement().addContent(requestNode);          

          
          for(Iterator i = billingItems.iterator(); i.hasNext();) {
            BillingItem bi = (BillingItem)i.next();
            Element billingItemNode = bi.toXMLDocument(null, this.DATE_OUTPUT_SQL).getRootElement();
            if (bi.getTotalPrice() != null) {
              billingItemNode.setAttribute("totalPrice", nf.format(bi.getTotalPrice().doubleValue()));        
            }            
            requestNode.addContent(billingItemNode);
          }
      
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(doc);
      
          setResponsePage(this.SUCCESS_JSP);
      
        } else {
        this.addInvalidField("insufficient permission", "Insufficient permission to create billing items");
      }
    }catch (NamingException e){
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in CreateBillingItems ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }

}