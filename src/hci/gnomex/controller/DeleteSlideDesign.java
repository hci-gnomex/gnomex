package hci.gnomex.controller;

import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.ExperimentFactor;
import hci.gnomex.model.ExperimentFactorEntry;
import hci.gnomex.model.Project;
import hci.gnomex.model.QualityControlStep;
import hci.gnomex.model.QualityControlStepEntry;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;




public class DeleteSlideDesign extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteSlideDesign.class);
  
  
  private Integer      idSlideDesign = null;
  private Integer      idSlideProductOld = null;
  private Integer      idSlideDesignOld = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idSlideDesign") != null && !request.getParameter("idSlideDesign").equals("")) {
     idSlideDesign = new Integer(request.getParameter("idSlideDesign"));
   }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        SlideDesign sd = (SlideDesign)sess.load(SlideDesign.class, idSlideDesign);
        idSlideProductOld = sd.getIdSlideProduct();
        idSlideDesignOld = sd.getIdSlideDesign();
        
        
        //
        // Delete array coordinates
        //
        List arrayCoords = sess.createQuery("SELECT ac from ArrayCoordinate ac where ac.idSlideDesign = " + idSlideDesignOld).list();
        for(Iterator i = arrayCoords.iterator(); i.hasNext();) {
          ArrayCoordinate ac  = (ArrayCoordinate)i.next();
          sess.delete(ac);
        }
        
        //
        // Delete slide design
        //
        sess.delete(sd);
        
        sess.flush();
        

        //
        // Get rid of unused slide products or update slide count
        //
        if (this.idSlideProductOld != null) {
          SlideProduct oldSlideProduct = (SlideProduct)sess.load(SlideProduct.class, idSlideProductOld);
         
          if (oldSlideProduct.getSlideDesigns().size() == 0) {
            oldSlideProduct.setMicroarrayCategories(null);
            sess.delete(oldSlideProduct);
          } else {
            oldSlideProduct.setSlidesInSet(new Integer(oldSlideProduct.getSlideDesigns().size()));
          }
          
        }


        

        sess.flush();    
        
       

        this.xmlResult = "<SUCCESS/>";
      
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete slide design.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in DeleteSlideDesign ", e);
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