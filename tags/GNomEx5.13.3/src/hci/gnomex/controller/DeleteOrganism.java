package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Organism;
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




public class DeleteOrganism extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteOrganism.class);
  
  
  private Integer      idOrganism = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idOrganism") != null && !request.getParameter("idOrganism").equals("")) {
     idOrganism = new Integer(request.getParameter("idOrganism"));
   } else {
     this.addInvalidField("idOrganism", "idOrganism is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    Organism organism = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      organism = (Organism)sess.load(Organism.class, idOrganism);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(organism)) {
        
        
        
        //
        // First delete associated genome builds
        //        
        StringBuffer query = new StringBuffer("SELECT gb from GenomeBuild gb");
        query.append(" where gb.idOrganism=" + organism.getIdOrganism());
        query.append(" order by gb.genomeBuildName");
        List genomeBuilds = sess.createQuery(query.toString()).list();
        
        if (!genomeBuilds.isEmpty()) {          
          Element gbEle = new Element("genomeBuilds");
          for(Iterator j = genomeBuilds.iterator(); j.hasNext();) {
            GenomeBuild gb = (GenomeBuild)j.next();
            sess.delete(gb);
          }
        }
        
        sess.flush();        
        
       
        //
        // Delete organism
        //
        sess.delete(organism);
      
        
        sess.flush();
        
       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete organism.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (ConstraintViolationException ce) {
      this.addInvalidField("constraint", "Organism set to inactive.  Unable to delete because organism is referenced on existing db objects.");
      
      try {
        sess.clear();
        organism = (Organism)sess.load(Organism.class, idOrganism);
        organism.setIsActive("N");
        sess.flush();
      } catch(Exception e) {
        log.error("An exception has occurred in DeleteOrganism when trying to inactivate organism ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());
        
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in DeleteOrganism ", e);
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