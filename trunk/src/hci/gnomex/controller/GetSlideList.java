package hci.gnomex.controller;

import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.SlideProductFilter;
import hci.gnomex.security.SecurityAdvisor;
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

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetSlideList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetSlideList.class);
  
  private SlideProductFilter filter;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new SlideProductFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    Document doc = new Document(new Element("SlideList"));
    Element slideNode = null;
    // Only return a list of slide products if the user is a GNomEx user that participates
    // in a group
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      StringBuffer buf = filter.getQuery(this.getSecAdvisor());
      log.info("Query for GetSlideProductList: " + buf.toString());
      List slideDesigns = (List)sess.createQuery(buf.toString()).list();
      
      for(Iterator i = slideDesigns.iterator(); i.hasNext();) {
        SlideProduct sp = (SlideProduct)i.next();
        
        // Don't show any slides that user doesn't have read permission
        // on.
        if (!getSecAdvisor().canRead(sp)) {
          continue;
        }

        
        Hibernate.initialize(sp.getSlideDesigns());
        Iterator sdIter = sp.getSlideDesigns().iterator();
        if (sp.getIsSlideSet() != null && sp.getIsSlideSet().equals("Y")) {
          slideNode = new Element("SlideProduct");
          slideNode.setAttribute("name",sp.getName());
          slideNode.setAttribute("id", sp.getIdSlideProduct().toString());
          slideNode.setAttribute("isSlideSet",sp.getIsSlideSet());
          slideNode.setAttribute("isActive", sp.getIsActive());
          while (sdIter.hasNext()) {
            SlideDesign sd = (SlideDesign) sdIter.next();
            Element sdNode = new Element("SlideDesign");
            sdNode.setAttribute("name", sd.getName());
            sdNode.setAttribute("id", sd.getIdSlideDesign().toString());
            sdNode.setAttribute("isActive", sd.getIsActive());
            sdNode.setAttribute("isInSlideSet", sp.getIsSlideSet());
            sdNode.setAttribute("slideSetName", sp.getName());
            sdNode.setAttribute("idSlideProduct", sp.getIdSlideProduct().toString());
            slideNode.addContent(sdNode);
          }
        } else {
          while (sdIter.hasNext()) {
            SlideDesign sd = (SlideDesign) sdIter.next();
            slideNode = new Element("SlideDesign");
            slideNode.setAttribute("name", sd.getName());
            slideNode.setAttribute("id", sd.getIdSlideDesign().toString());
            slideNode.setAttribute("isActive", sd.getIsActive());
            slideNode.setAttribute("isInSlideSet", "N");
          }
        }
        doc.getRootElement().addContent(slideNode);
      }
    } 
    
    XMLOutputter out = new org.jdom.output.XMLOutputter();
    this.xmlResult = out.outputString(doc);
    
    setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetSlideList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetSlideList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetSlideList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetSlideList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }

}