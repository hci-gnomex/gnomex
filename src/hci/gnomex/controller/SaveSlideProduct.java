package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.MicroarrayCategory;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MicroarrayCategoryParser;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class SaveSlideProduct extends GNomExCommand implements Serializable {

  private static Logger log = Logger.getLogger(SaveSlideProduct.class);
  
  private boolean isNewSlideProduct;
  
  private SlideProduct slideProductScreen;
  
  private String microarrayCategoryXMLString = null;
  private Document mcDoc;
  private MicroarrayCategoryParser mcParser;
  
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      SlideProduct load = null;
      Integer successId = null;
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        
        // Parse microarray category xml
        if (mcParser != null) {
          mcParser.parse(sess);
        }
        
        // update current slide product 
        if (slideProductScreen.getIdSlideProduct() != null) { 
          load = (SlideProduct) sess.load(SlideProduct.class, this.slideProductScreen.getIdSlideProduct());
          load.copyEditableDataFrom(slideProductScreen);
          sess.update(load);
          // save microarray categories
          saveMicroarrayCategories(load, mcParser);
          successId = load.getIdSlideProduct();
       // make a new one
        } else { 
          // make a new slide product and add a new slide design to it
          this.slideProductScreen.setIsSlideSet("Y");
          this.slideProductScreen.setSlidesInSet(new Integer(1));
          this.slideProductScreen.setArraysPerSlide(new Integer(1));
          sess.save(slideProductScreen);
          sess.flush();
          // save microarray categories
          saveMicroarrayCategories(this.slideProductScreen, mcParser);
          this.slideProductScreen.getIdSlideProduct();
          // now make the new slide design
          SlideDesign newSlide = new SlideDesign();
          newSlide.setIdSlideProduct(this.slideProductScreen.getIdSlideProduct());
          newSlide.setName(this.slideProductScreen.getName()+" Slide 1");
          newSlide.setIsActive("Y");
          sess.save(newSlide);
        }
        successId = this.slideProductScreen.getIdSlideProduct();
        
        sess.flush();
        
        this.xmlResult = "<SUCCESS idSlideProduct=\"" + successId + "\"/>";
        
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save slide design.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (HibernateException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (NamingException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (SQLException e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    } catch (Exception e) {
      log.error(e.getClass().toString() + ": " + e);
      throw new RollBackCommandException();
    }
    finally {
      try {
        HibernateSession.closeSession();
      } catch (HibernateException e) {
        log.error(e.getClass().toString() + ": " + e);
        throw new RollBackCommandException();
      } catch (SQLException e) {
        log.error(e.getClass().toString() + ": " + e);
        throw new RollBackCommandException();
      }
    }
    return this;
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    slideProductScreen = new SlideProduct();
    HashMap errors = this.loadDetailObject(request, slideProductScreen);
    this.addInvalidFields(errors);
    
    if (request.getParameter("microarrayCategoryXMLString") != null && !request.getParameter("microarrayCategoryXMLString").equals("")) {
      microarrayCategoryXMLString = request.getParameter("microarrayCategoryXMLString");
      
      StringReader reader = new StringReader(microarrayCategoryXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        mcDoc = sax.build(reader);
        mcParser = new MicroarrayCategoryParser(mcDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse microarrayCategoryXMLString", je );
        this.addInvalidField( "microarrayCategoryXMLString", "Invalid microarrayCategoryXMLString");
      }
    }
    
  }

  public void validate() {
  }

  private void saveMicroarrayCategories(SlideProduct slideProduct, MicroarrayCategoryParser mcParser) {
    if (mcParser != null) {
      //
      // Save microarrayCategories
      //
      Set microarrayCategories = new TreeSet();
      for(Iterator i = mcParser.getCodeMicroarrayCategoryMap().keySet().iterator(); i.hasNext();) {
        String codeMicroarrayCategory = (String)i.next();
        MicroarrayCategory microarrayCategory = (MicroarrayCategory)mcParser.getCodeMicroarrayCategoryMap().get(codeMicroarrayCategory);
        microarrayCategories.add(microarrayCategory);
      }
      slideProduct.setMicroarrayCategories(microarrayCategories);
    }
    
  }
  
}
