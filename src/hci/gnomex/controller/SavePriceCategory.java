package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingChargeKind;
import hci.gnomex.model.PriceCategory;
import hci.gnomex.model.PriceSheet;
import hci.gnomex.model.PriceSheetPriceCategory;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Step;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PriceCategoryStepParser;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SavePriceCategory extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SavePriceCategory.class);
  

  private PriceCategory           priceCategoryScreen;
  private Integer                 idPriceSheet;
  private boolean                 isNewPriceCategory = false;
  private PriceCategoryStepParser stepParser;
  private boolean				  isNewProductPriceCategory = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    priceCategoryScreen = new PriceCategory();
    HashMap errors = this.loadDetailObject(request, priceCategoryScreen);
    this.addInvalidFields(errors);

    if (request.getParameter("idPriceSheet") != null && !request.getParameter("idPriceSheet").equals("")) {
      idPriceSheet = Integer.valueOf(request.getParameter("idPriceSheet"));
    }

    if (priceCategoryScreen.getIdPriceCategory() == null || priceCategoryScreen.getIdPriceCategory().intValue() == 0) {
      isNewPriceCategory = true;
    }
    
    if (request.getParameter("isNewProductPriceCategory") != null && request.getParameter("isNewProductPriceCategory").equalsIgnoreCase("Y")) {
      isNewProductPriceCategory = true;
    }

    String stepsXMLString = "";
    if (request.getParameter("stepsXMLString") != null && !request.getParameter("stepsXMLString").equals("")) {
      stepsXMLString = request.getParameter("stepsXMLString");
    }
    StringReader reader = new StringReader(stepsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      Document stepsDoc = sax.build(reader);
      stepParser = new PriceCategoryStepParser(stepsDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse stepsXMLString", je );
      this.addInvalidField( "stepsXMLString", "Invalid stepsXMLString");
    }

  }

  @SuppressWarnings("unchecked")
public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
        
        
        PriceCategory priceCategory = null;
              
        if (isNewProductPriceCategory) {
          priceCategory = this.createNewProductPriceCategory();
          sess.save(priceCategory);
        } else if (isNewPriceCategory) {
          priceCategory = priceCategoryScreen;
          sess.save(priceCategory);
        } else {
          
          priceCategory = (PriceCategory)sess.load(PriceCategory.class, priceCategoryScreen.getIdPriceCategory());
          
          initializePrice(priceCategory);
        }

        sess.flush();
        
        //
        // Attach price category to price sheet
        //
        if (idPriceSheet != null && !isNewProductPriceCategory) {
          PriceSheet priceSheet = (PriceSheet)sess.load(PriceSheet.class, idPriceSheet);
          boolean found = false;
          Integer maxSortOrder = Integer.valueOf(0);
          for(Iterator i = priceSheet.getPriceCategories().iterator(); i.hasNext();) {
            PriceSheetPriceCategory x  = (PriceSheetPriceCategory)i.next();
            PriceCategory cat = x.getPriceCategory();
            if (x.getSortOrder().compareTo(maxSortOrder) > 0) {
              maxSortOrder = x.getSortOrder();
            }
            if (cat.getIdPriceCategory().equals(priceCategory.getIdPriceCategory())) {
              found = true;
            }
          }
          if (!found) {
            Set priceCategories = priceSheet.getPriceCategories();
            PriceSheetPriceCategory x = new PriceSheetPriceCategory();
            x.setIdPriceCategory(priceCategory.getIdPriceCategory());
            x.setIdPriceSheet(priceSheet.getIdPriceSheet());
            x.setPriceCategory(priceCategory);
            x.setSortOrder(Integer.valueOf(maxSortOrder.intValue() + 1));
            sess.save(x);          
            sess.flush();
          }
          
        } else if (isNewProductPriceCategory) {
        	PriceSheet productPriceSheet;
        	String productSheetName = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.PRODUCT_SHEET_NAME);
        	Query query = sess.createQuery(" SELECT DISTINCT ps FROM PriceSheet AS ps WHERE ps.name =:priceSheetName ");
        	query.setParameter("priceSheetName", productSheetName);
        	List<PriceSheet> queryResult = (List<PriceSheet>) query.list();
        	if (queryResult.size() > 0) {
        		productPriceSheet = queryResult.get(0);
        	} else {
        		productPriceSheet = new PriceSheet();
        		productPriceSheet.setName(productSheetName);
        		productPriceSheet.setDescription(null);
        		productPriceSheet.setIsActive("Y");
        		sess.save(productPriceSheet);
        	}
        	
        	Integer maxSortOrder = Integer.valueOf(0);
        	if (productPriceSheet.getPriceCategories() != null) {
        		for (Iterator<PriceSheetPriceCategory> i = productPriceSheet.getPriceCategories().iterator(); i.hasNext();) {
            		PriceSheetPriceCategory pspc  = i.next();
                    if (pspc.getSortOrder().compareTo(maxSortOrder) > 0) {
                    	maxSortOrder = pspc.getSortOrder();
                    }
            	}
        	}
        	
        	PriceSheetPriceCategory newPSPC = new PriceSheetPriceCategory();
        	newPSPC.setIdPriceCategory(priceCategory.getIdPriceCategory());
        	newPSPC.setIdPriceSheet(productPriceSheet.getIdPriceSheet());
        	newPSPC.setPriceCategory(priceCategory);
        	newPSPC.setSortOrder(Integer.valueOf(maxSortOrder.intValue() + 1));
            sess.save(newPSPC);          
            sess.flush();
        }
 
        // Modify associated completion steps
        stepParser.parse(sess);
        TreeSet steps = new TreeSet(new StepComparator());
        for(Iterator i = stepParser.getStepMap().keySet().iterator(); i.hasNext();) {
          String codeStep = (String)i.next();
          Step step = (Step)stepParser.getStepMap().get(codeStep);     
          steps.add(step);
        }
        priceCategory.setSteps(steps);
        sess.flush();
        
        this.xmlResult = "<SUCCESS idPriceCategory=\"" + priceCategory.getIdPriceCategory() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save priceCategory.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SavePriceCategory ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private PriceCategory createNewProductPriceCategory() {
	  PriceCategory pc = new PriceCategory();
	  pc.setName(priceCategoryScreen.getName());
	  pc.setDescription(null);
	  pc.setIsActive(priceCategoryScreen.getIsActive());
	  pc.setCodeBillingChargeKind(BillingChargeKind.PRODUCT);
	  pc.setDictionaryClassNameFilter1("hci.gnomex.model.Product");
	  pc.setDictionaryClassNameFilter2(null);
	  pc.setPluginClassName(null);
	  return pc;
  }
  
  private void initializePrice(PriceCategory priceCategory) {
    priceCategory.setName(priceCategoryScreen.getName());
    priceCategory.setDescription(priceCategoryScreen.getDescription());
    priceCategory.setIsActive(priceCategoryScreen.getIsActive());
    priceCategory.setCodeBillingChargeKind(priceCategoryScreen.getCodeBillingChargeKind());
    priceCategory.setDictionaryClassNameFilter1(priceCategoryScreen.getDictionaryClassNameFilter1());
    priceCategory.setDictionaryClassNameFilter2(priceCategoryScreen.getDictionaryClassNameFilter2());
    priceCategory.setPluginClassName(priceCategoryScreen.getPluginClassName());
    
  }

  private class StepComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Step u1 = (Step)o1;
      Step u2 = (Step)o2;

      return u1.getCodeStep().compareTo(u2.getCodeStep());

    }
  }
}