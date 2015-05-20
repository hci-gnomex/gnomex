package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.ArrayCoordinate;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideDesignFilter;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.SlideProductFilter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetSlideDesign extends GNomExCommand implements Serializable {

  private static Logger log = Logger.getLogger(GetSlideDesign.class);
  
  private SlideDesignFilter filter;
  
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      StringBuffer buf = filter.getQuery(this.getSecAdvisor());
      log.info("Query for GetSlideDesign: "+buf.toString());
      Object[] row = (Object[]) sess.createQuery(buf.toString()).uniqueResult();
      
      if (row[0] != null) {
        SlideDesign sd = (SlideDesign) row[0];
        Element design = sd.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        Document doc = new Document().setRootElement(design);
        // get the related slide product data
        if (row[1] != null) {
          SlideProduct sp = (SlideProduct) row[1];
          if (sp != null) {
            design.addContent(sp.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
            // now get the arraycoords
            Element acRootNode = new Element("arrayCoordinates");
            design.addContent(acRootNode);
            if (sp.getArraysPerSlide() != null && sp.getArraysPerSlide().intValue() > 0) {
              List arrayCoordinates = sess.createQuery("SELECT ac from ArrayCoordinate ac where ac.idSlideDesign = " + sd.getIdSlideDesign() + " order by ac.x, ac.y").list();
              for(Iterator i1 = arrayCoordinates.iterator(); i1.hasNext();) {
                ArrayCoordinate ac = (ArrayCoordinate)i1.next();
                acRootNode.addContent(ac.toXMLDocument(null).getRootElement());
              }
            }
          }
        }
        XMLOutputter out = new XMLOutputter();
        this.xmlResult = out.outputString(doc);
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Slide Product Not Found", "No slide product for the given ID.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (HibernateException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (XMLReflectException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (Exception e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    }
    finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {
        log.error(e.getClass().toString() + ": " + e);
        throw new RollBackCommandException();
      }
    }
    return this;
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new SlideDesignFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);

  }

  public void validate() {

  }

}
