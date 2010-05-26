package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.billing.BillingPlugin;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingPeriod;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
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
          
          List billingItems = new ArrayList<BillingItem>();
 
          DictionaryHelper dh = DictionaryHelper.getInstance(sess);

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
          
          // Find the appropriate price sheet
          PriceSheet priceSheet = null;
          List priceSheets = sess.createQuery("SELECT ps from PriceSheet as ps").list();
          for(Iterator i = priceSheets.iterator(); i.hasNext();) {
            PriceSheet ps = (PriceSheet)i.next();
            for(Iterator i1 = ps.getRequestCategories().iterator(); i1.hasNext();) {
              RequestCategory requestCategory = (RequestCategory)i1.next();
              if(requestCategory.getCodeRequestCategory().equals(request.getCodeRequestCategory())) {
                priceSheet = ps;
                break;
              }
              
            }
          }
          
          if (priceSheet != null) {
            
            
            for(Iterator i1 = priceSheet.getPriceCategories().iterator(); i1.hasNext();) {
              PriceSheetPriceCategory priceCategoryX = (PriceSheetPriceCategory)i1.next();
              PriceCategory priceCategory = priceCategoryX.getPriceCategory();

              
              // Instantiate plugin for billing category
              BillingPlugin plugin = null;
              if (priceCategory.getPluginClassName() != null) {
                try {
                  plugin = (BillingPlugin)Class.forName(priceCategory.getPluginClassName()).newInstance();
                } catch(Exception e) {
                  log.error("Unable to instantiate billing plugin " + priceCategory.getPluginClassName());
                }
                
              }
              
              // Get the billing items
              if (plugin != null) {
                List billingItemsForCategory = plugin.constructBillingItems(sess, null, billingPeriod, priceCategory, request, request.getSamples(), request.getLabeledSamples(), request.getHybridizations(), request.getSequenceLanes());
                billingItems.addAll(billingItemsForCategory);                
              }
            }
            
          }
          
            
          Document doc = new Document(new Element("NewBilling"));
          
          
          Element requestNode = new Element("Request");
          requestNode.setAttribute("idRequest", request.getIdRequest().toString());
          requestNode.setAttribute("idBillingAccount", request.getIdBillingAccount().toString());
          requestNode.setAttribute("requestNumber", request.getNumber());
          requestNode.setAttribute("idLab", request.getIdLab().toString());
          requestNode.setAttribute("label", request.getNumber());
          requestNode.setAttribute("submitter", request.getAppUser() != null ? request.getAppUser().getDisplayName() : "");
          requestNode.setAttribute("codeRequestCategory", request.getCodeRequestCategory());        
          requestNode.setAttribute("billingLabName", request.getLabName());        
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