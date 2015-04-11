package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Application;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.ApplicationParser;

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
  
  private SlideProduct slideProductScreen;
  
  private String microarrayCategoryXMLString = null;
  private Document mcDoc;
  private ApplicationParser applicationParser;
  
  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      SlideProduct load = null;
      Integer successId = null;
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_WORKFLOW)) {
        
        // Parse application xml
        if (applicationParser != null) {
          applicationParser.parse(sess);
        }
        
        // update current slide product 
        if (slideProductScreen.getIdSlideProduct() != null) { 
          load = (SlideProduct) sess.load(SlideProduct.class, this.slideProductScreen.getIdSlideProduct());
          load.copyEditableDataFrom(slideProductScreen);
          sess.update(load);
          // save applications 
          saveApplications(load, applicationParser);
          successId = load.getIdSlideProduct();
       // make a new one
        } else { 
          // make a new slide product and add a new slide design to it
          this.slideProductScreen.setIsSlideSet("Y");
          this.slideProductScreen.setSlidesInSet(new Integer(1));
          this.slideProductScreen.setArraysPerSlide(new Integer(1));
          sess.save(slideProductScreen);
          sess.flush();
          // save applications
          saveApplications(this.slideProductScreen, applicationParser);
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
        applicationParser = new ApplicationParser(mcDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse microarrayCategoryXMLString", je );
        this.addInvalidField( "microarrayCategoryXMLString", "Invalid microarrayCategoryXMLString");
      }
    }
    
  }

  public void validate() {
  }

  private void saveApplications(SlideProduct slideProduct, ApplicationParser applicationParser) {
    if (applicationParser != null) {
      //
      // Save applications
      //
      Set applications = new TreeSet();
      for(Iterator i = applicationParser.getCodeApplicationMap().keySet().iterator(); i.hasNext();) {
        String codeApplication = (String)i.next();
        Application application = (Application)applicationParser.getCodeApplicationMap().get(codeApplication);
        applications.add(application);
      }
      slideProduct.setApplications(applications);
    }
    
  }
  
}
