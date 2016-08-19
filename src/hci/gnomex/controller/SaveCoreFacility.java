package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;

public class SaveCoreFacility extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(SaveCoreFacility.class);
  
  private CoreFacility            coreFacilityScreen;
  private boolean                 isNewCoreFacility = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    coreFacilityScreen = new CoreFacility();
    HashMap errors = this.loadDetailObject(request, coreFacilityScreen);
        
    this.addInvalidFields(errors);
        
    if (coreFacilityScreen.getIdCoreFacility() == null || coreFacilityScreen.getIdCoreFacility().intValue() == 0) {
      isNewCoreFacility = true;
    }
    
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecAdvisor().hasPermission( SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) || this.getSecAdvisor().isCoreFacilityIManage(coreFacilityScreen.getIdCoreFacility())) {

        CoreFacility coreFacility = null;
        
        if (isNewCoreFacility) {
          coreFacility = coreFacilityScreen;
          
          sess.save(coreFacility);
          sess.flush();

        } else {
          
          coreFacility = (CoreFacility)sess.load(CoreFacility.class, coreFacilityScreen.getIdCoreFacility());
          
          initializeCoreFacility(coreFacility);
          sess.save(coreFacility);
          sess.flush();
        }
        
        sess.flush();
               
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idCoreFacility=\"" + coreFacility.getIdCoreFacility() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save Core Facility.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      LOG.error("An exception has occurred in SaveCoreFacility ", e);

      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }
    
    return this;
  }
  
  private void initializeCoreFacility(CoreFacility coreFacility) {
    
    coreFacility.setFacilityName( coreFacilityScreen.getFacilityName() );
    coreFacility.setIsActive( coreFacilityScreen.getIsActive() );
    coreFacility.setShowProjectAnnotations( coreFacilityScreen.getShowProjectAnnotations() );
    coreFacility.setDescription( coreFacilityScreen.getDescription() );
    coreFacility.setAcceptOnlineWorkAuth( coreFacilityScreen.getAcceptOnlineWorkAuth() );
    coreFacility.setShortDescription( coreFacilityScreen.getShortDescription() );
    coreFacility.setContactName( coreFacilityScreen.getContactName() );
    coreFacility.setContactEmail( coreFacilityScreen.getContactEmail() );
    coreFacility.setContactPhone( coreFacilityScreen.getContactPhone() );
    coreFacility.setSortOrder( coreFacilityScreen.getSortOrder() );
    coreFacility.setContactImage( coreFacilityScreen.getContactImage() );
    coreFacility.setLabRoom( coreFacilityScreen.getLabRoom() );
    coreFacility.setContactRoom( coreFacilityScreen.getContactRoom() );
    coreFacility.setLabPhone( coreFacilityScreen.getLabPhone() );
  }
  
  
  private class CoreFacilityComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      CoreFacility org1 = (CoreFacility)o1;
      CoreFacility org2 = (CoreFacility)o2;
      
      return org1.getIdCoreFacility().compareTo(org2.getIdCoreFacility());
      
    }
  }
}