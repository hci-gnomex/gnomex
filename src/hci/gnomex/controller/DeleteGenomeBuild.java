package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.jdom.Element;




public class DeleteGenomeBuild extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteGenomeBuild.class);
  
  
  private Integer      idGenomeBuild = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idGenomeBuild") != null && !request.getParameter("idGenomeBuild").equals("")) {
     idGenomeBuild = new Integer(request.getParameter("idGenomeBuild"));
   } else {
     this.addInvalidField("idGenomeBuild", "idGenomeBuild is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    GenomeBuild genomeBuild = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      genomeBuild = (GenomeBuild)sess.load(GenomeBuild.class, idGenomeBuild);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(genomeBuild)) {
       
        //
        // Delete genomeBuild
        //
        sess.delete(genomeBuild);
      
        
        sess.flush();
        
       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete genome build.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (ConstraintViolationException ce) {
      this.addInvalidField("constraint", "Unable to delete because genome build is associated with other objects in the datatabase.");
      
      try {
        sess.clear();
        genomeBuild = (GenomeBuild)sess.load(GenomeBuild.class, idGenomeBuild);
        genomeBuild.setIsActive("N");
        sess.flush();
      } catch(Exception e) {
        log.error("An exception has occurred in DeleteGenomeBuild when trying to inactivate genome build ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
        
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in DeleteGenomeBuild ", e);
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
  
 
  
  
  

}