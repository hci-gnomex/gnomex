package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.BillingCategory;
import hci.gnomex.model.BillingPrice;
import hci.gnomex.model.BillingTemplate;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
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


public class GetBillingTemplateList extends GNomExCommand implements Serializable {
  
  private String action = "";
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetBillingTemplateList.class);

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("action") != null) {
      action = request.getParameter("action");
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
 
          // Read (and cache) the billing templates, categories, and prices
          DictionaryHelper dh = DictionaryHelper.getInstance(sess);
          if (action.equals("refresh")) {
            dh.reloadBillingTemplates(sess);
          } else {
            dh.loadBillingTemplates(sess);            
          }
          
          Document doc = new Document(new Element("BillingTemplateList"));
          
          for(Iterator i = dh.getBillingTemplates().iterator(); i.hasNext();) {
            BillingTemplate bt = (BillingTemplate)i.next();
            Element templateNode = bt.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
            doc.getRootElement().addContent(templateNode);
            
            List categories = (List)dh.getBillingCategories(bt.getIdBillingTemplate());
            if (categories != null) {
              for(Iterator i1 = categories.iterator(); i1.hasNext();) {
                BillingCategory bc = (BillingCategory)i1.next();
                Element categoryNode = bc.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
                templateNode.addContent(categoryNode);
                
                List prices = (List)dh.getBillingPrices(bc.getIdBillingCategory());
                if (prices != null) {
                  for(Iterator i2 = prices.iterator(); i2.hasNext();) {
                    BillingPrice bp = (BillingPrice)i2.next();
                    
                    if (bp.getIdBillingTemplate() == null || bp.getIdBillingTemplate().equals(bt.getIdBillingTemplate())) {
                      Element priceNode = bp.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
                      priceNode.setAttribute("category", categoryNode.getAttributeValue("description"));
                      priceNode.setAttribute("codeBillingChargeKind", categoryNode.getAttributeValue("codeBillingChargeKind"));
                      categoryNode.addContent(priceNode);                      
                    }
                  }
                }
              }
            }
            
          }
          
          org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = out.outputString(doc);
      
          setResponsePage(this.SUCCESS_JSP);
      
        } else {
        this.addInvalidField("insufficient permission", "Insufficient permission to access billing template");
      }
    }catch (NamingException e){
      log.error("An exception has occurred in GetBillingTemplateList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetBillingTemplateList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetBillingTemplateList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetBillingTemplateList ", e);
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