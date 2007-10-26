package hci.gnomex.controller;

import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.MicroarrayCategory;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideDesignFilter;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetSlideDesignList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetSlideDesignList.class);
  
  private SlideDesignFilter filter;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new SlideDesignFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = HibernateSession.currentSession(this.getUsername());
    
    StringBuffer buf = filter.getQuery(this.getSecAdvisor());
    log.info("Query for GetSlideDesignList: " + buf.toString());
    List slideDesigns = (List)sess.createQuery(buf.toString()).list();
    
    Document doc = new Document(new Element("SlideDesignList"));
    for(Iterator i = slideDesigns.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      SlideDesign  sd = (SlideDesign)row[0];
      SlideProduct sp = (SlideProduct)row[1];
      
      Element sdNode = sd.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      sdNode.setAttribute("arraysPerSlide",         sp.getArraysPerSlide() != null ? sp.getArraysPerSlide().toString() : "");
      sdNode.setAttribute("idVendor",               sp.getIdVendor() != null ? sp.getIdVendor().toString() : "");
      sdNode.setAttribute("idOrganism",             sp.getIdOrganism() != null ? sp.getIdOrganism().toString() : "");
      sdNode.setAttribute("idLab",                  sp.getIdLab() != null ? sp.getIdLab().toString() : "");
      sdNode.setAttribute("isCustom",               sp.getIsCustom() != null ? sp.getIsCustom() : "N");
      sdNode.setAttribute("isActive",               sp.getIsActive() != null ? sp.getIsActive() : "N");
      if (sp.getSlidesInSet() != null && sp.getSlidesInSet().intValue() > 1) {
        sdNode.setAttribute("isInSlideSet", "Y");
        sdNode.setAttribute("idSlideProductSlideSet", sp.getIdSlideProduct().toString());
      } else {
        sdNode.setAttribute("isInSlideSet", "N");        
        sdNode.setAttribute("idSlideProductSlideSet", "");
      }
      
      if (sp.getMicroarrayCategories() != null) {
        Element mcRootNode = new Element("microarrayCategories");
        sdNode.addContent(mcRootNode);
        StringBuffer concatMicroarrayCategories = new StringBuffer();
        for(Iterator i1 = sp.getMicroarrayCategories().iterator(); i1.hasNext();) {
          MicroarrayCategory mc = (MicroarrayCategory)i1.next();
          mcRootNode.addContent(mc.toXMLDocument(null).getRootElement());
          concatMicroarrayCategories.append(mc.getMicroarrayCategory());
          if (i1.hasNext()) {
            concatMicroarrayCategories.append(", ");
          }
        }
        sdNode.setAttribute("microarrayCategories", concatMicroarrayCategories.toString());
      }
      
      if (sp.getArraysPerSlide() != null && sp.getArraysPerSlide().intValue() > 1) {
        Element acRootNode = new Element("arrayCoordinates");
        sdNode.addContent(acRootNode);
         
        List arrayCoordinates = sess.createQuery("SELECT ac from ArrayCoordinate ac where ac.idSlideDesign = " + sd.getIdSlideDesign() + " order by ac.x, ac.y").list();
        for(Iterator i1 = arrayCoordinates.iterator(); i1.hasNext();) {
          ArrayCoordinate ac = (ArrayCoordinate)i1.next();
          acRootNode.addContent(ac.toXMLDocument(null).getRootElement());
        }
      }
      
      doc.getRootElement().addContent(sdNode);
      
    }
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetSlideDesignList ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetSlideDesignList ", e);
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetSlideDesignList ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

}