package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.IScanChip;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class SaveIScanChip extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveIScanChip.class);
  
  private IScanChip                      chipScreen;
  private boolean                        isNewIScanChip = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    chipScreen = new IScanChip();
    HashMap errors = this.loadDetailObject(request, chipScreen);
        
    this.addInvalidFields(errors);
    
    if (request.getParameter("costPerSampleDisplay") != null && !request.getParameter("costPerSampleDisplay").equals("")) {
      String chipCost = request.getParameter("costPerSampleDisplay");
      chipCost = chipCost.replaceAll("\\$", "");
      chipCost = chipCost.replaceAll(",", "");
      chipScreen.setCostPerSample( new BigDecimal(chipCost) );
    }
    
    if (chipScreen.getIdIScanChip() == null || chipScreen.getIdIScanChip().intValue() == 0) {
      isNewIScanChip = true;
    }
    
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {

        
        IScanChip chip = null;
        
        if (isNewIScanChip) {
          chip = chipScreen;
          
          sess.save(chip);
          sess.flush();

        } else {
          
          chip = (IScanChip)sess.load(IScanChip.class, chipScreen.getIdIScanChip());
          
          initializeIScanChip(chip);
          sess.save(chip);
          sess.flush();
        }
        
        sess.flush();
               
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idIScanChip=\"" + chip.getIdIScanChip() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save iScan Chip.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveIScanChip ", e);
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
  
  private void initializeIScanChip(IScanChip chip) {
    
    chip.setName(chipScreen.getName());
    chip.setCostPerSample(chipScreen.getCostPerSample());
    chip.setSamplesPerChip(chipScreen.getSamplesPerChip());
    chip.setMarkersPerSample(chipScreen.getMarkersPerSample());
    chip.setIsActive(chipScreen.getIsActive());
    chip.setCatalogNumber(chipScreen.getCatalogNumber());

  }
  
  
  private class IScanChipComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      IScanChip org1 = (IScanChip)o1;
      IScanChip org2 = (IScanChip)o2;
      
      return org1.getIdIScanChip().compareTo(org2.getIdIScanChip());
      
    }
  }
}