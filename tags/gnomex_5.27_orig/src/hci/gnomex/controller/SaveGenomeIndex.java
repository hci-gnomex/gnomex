package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.GenomeIndex;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class SaveGenomeIndex extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveGenomeIndex.class);


  private GenomeIndex                    genomeIndexScreen;
  private boolean                        isNewGenomeIndex = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    genomeIndexScreen = new GenomeIndex();
    HashMap errors = this.loadDetailObject(request, genomeIndexScreen);
    this.addInvalidFields(errors);
    if (genomeIndexScreen.getIdGenomeIndex() == null || genomeIndexScreen.getIdGenomeIndex().intValue() == 0) {
      isNewGenomeIndex = true;
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        GenomeIndex gnIndx = null;
              
        if (isNewGenomeIndex) {
          gnIndx = genomeIndexScreen;
          
          sess.save(gnIndx);
        } else {
          
          gnIndx = (GenomeIndex)sess.load(GenomeIndex.class, genomeIndexScreen.getIdGenomeIndex());

          
          initializeGenomeIndex(gnIndx);
        }
        
        sess.flush();
        
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idGenomeIndex=\"" + gnIndx.getIdGenomeIndex() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to genomeIndex.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveGenomeIndex ", e);
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
  
  private void initializeGenomeIndex(GenomeIndex gdIndx) {
    gdIndx.setGenomeIndexName(genomeIndexScreen.getGenomeIndexName());
    gdIndx.setWebServiceName(genomeIndexScreen.getWebServiceName());
    gdIndx.setIsActive(gdIndx.getIsActive());
    gdIndx.setIdOrganism(gdIndx.getIdOrganism());
  }
}