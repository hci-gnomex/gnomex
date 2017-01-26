package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.gnomex.model.*;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PriceSheetCategoryParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;



public class SavePriceSheet extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SavePriceSheet.class);
  
  private String                     requestCategoriesXMLString;
  private Document                   requestCategoryDoc;
  private PriceSheetCategoryParser   requestCategoryParser;
  

  private PriceSheet                 priceSheetScreen;
  private boolean                   isNewPriceSheet = false;
  private Integer                    idCoreFacility;

  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    priceSheetScreen = new PriceSheet();
    HashMap errors = this.loadDetailObject(request, priceSheetScreen);
    this.addInvalidFields(errors);
    if (priceSheetScreen.getIdPriceSheet() == null || priceSheetScreen.getIdPriceSheet().intValue() == 0) {
      isNewPriceSheet = true;
    }

    if (request.getParameter("requestCategoriesXMLString") != null && !request.getParameter("requestCategoriesXMLString").equals("")) {
      requestCategoriesXMLString = request.getParameter("requestCategoriesXMLString");
    }
    
    StringReader reader = new StringReader(requestCategoriesXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      requestCategoryDoc = sax.build(reader);
      requestCategoryParser = new PriceSheetCategoryParser(requestCategoryDoc);
    } catch (JDOMException je ) {
      LOG.error( "Cannot parse requestCategoriesXMLString", je );
      this.addInvalidField( "requestCategoriesXMLString", "Invalid requestCategoriesXMLString");
    }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_BILLING)) {
        requestCategoryParser.parse(sess);
        
        PriceSheet priceSheet = null;
              
        if (isNewPriceSheet) {
          priceSheet = priceSheetScreen;
          sess.save(priceSheet);
        } else {
          
          priceSheet = (PriceSheet)sess.load(PriceSheet.class, priceSheetScreen.getIdPriceSheet());
          
          initializePriceSheet(priceSheet);
        }


        //
        // Save priceSheet requestCategory
        //
        TreeSet requestCategories = new TreeSet();
        for(Iterator i = requestCategoryParser.getRequestCategoryMap().keySet().iterator(); i.hasNext();) {
          String codeRequestCategory = (String)i.next();
          RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);
          idCoreFacility = requestCategory.getIdCoreFacility();
          Map priceRequestCategories = getAllRequestCategories(sess);
          requestCategories.add(requestCategory);

          // Check if any of the request categories are already set for this core facility
          for (Object requestCategoryObject:requestCategories) {
            RequestCategory reqCategory = (RequestCategory) requestCategoryObject;
            if (priceRequestCategories.containsKey(requestCategory.getCodeRequestCategory())) {
              CoreFacility facility = (CoreFacility)sess.get(CoreFacility.class, idCoreFacility);
              this.addInvalidField("Invalid Request Category", "Request category " + reqCategory.getRequestCategory().toString() + " already set on price sheet "+priceRequestCategories.get(requestCategory.getCodeRequestCategory())+" in "+facility.getFacilityName());
              setResponsePage(this.ERROR_JSP);
              return this;
            }
          }
        }

      priceSheet.setRequestCategories(requestCategories);

      sess.flush();

      this.xmlResult = "<SUCCESS idPriceSheet=\"" + priceSheet.getIdPriceSheet() + "\"/>";

      setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save price sheet.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      LOG.error("An exception has occurred in SavePriceSheet ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        //closeHibernateSession;        
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }
    
    return this;
  }

  /**
   *
   * @param sess
   * @return priceRequestCategories All of the request categories for this core facility
   */
    private Map<String, String> getAllRequestCategories (Session sess) {
    Map<String, String> priceRequestCategories = new HashMap<> ();

    StringBuffer buf = new StringBuffer();

    buf.append("SELECT distinct p from PriceSheet p ");
    buf.append("JOIN p.requestCategories rc ");
    buf.append("WHERE ");
    if (this.idCoreFacility > 0) {
      buf.append("(rc.idCoreFacility = ").append(this.idCoreFacility).append(")");
    } else {
      this.getSecAdvisor().appendCoreFacilityCriteria(buf, "rc");
    }
    buf.append(" ");
    buf.append("order by p.name");
    List priceSheets = sess.createQuery(buf.toString()).list();
    for (Object obj:priceSheets) {
      PriceSheet sheet = (PriceSheet) obj;
      Set requestCategories = sheet.getRequestCategories();
      for (Object reqObject:requestCategories) {
        RequestCategory requestCategory = (RequestCategory) reqObject;
        String stringRequestCategory = requestCategory.getCodeRequestCategory();
        priceRequestCategories.put(stringRequestCategory, sheet.getName());
      }
    }
    return priceRequestCategories;

  }

  private void initializePriceSheet(PriceSheet priceSheet) {
    priceSheet.setName(priceSheetScreen.getName());
    priceSheet.setDescription(priceSheetScreen.getDescription());
    priceSheet.setIsActive(priceSheetScreen.getIsActive());
    
  }

}