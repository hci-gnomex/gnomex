package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.gnomex.model.Application;
import hci.gnomex.model.FlowCellChannel;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.Institution;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.model.SlideProduct;
import hci.gnomex.model.WorkItem;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.ApplicationParser;
import hci.gnomex.utility.FlowCellChannelParser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SaveSimpleSlide extends GNomExCommand implements Serializable {

  private SlideProduct slideInfo;
  private SlideProduct slideProduct;
  private SlideDesign slideDesign;
  
  private String applicationXMLString = null;
  private Document mcDoc;
  private ApplicationParser applicationParser;


	// the static field for logging in Log4J
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveSimpleSlide.class);


	public void validate() {}

  public void loadCommand(HttpServletRequest request, HttpSession session)
  {
    slideInfo = new SlideProduct();
    HashMap errors = this.loadDetailObject(request, slideInfo);
    this.addInvalidFields(errors);
    
    if (request.getParameter("applicationXMLString") != null && !request.getParameter("applicationXMLString").equals("")) {
      applicationXMLString = request.getParameter("applicationXMLString");
      
      StringReader reader = new StringReader(applicationXMLString);
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
    

  public Command execute() throws RollBackCommandException {
   try{
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      slideProduct = new SlideProduct();
      slideDesign = new SlideDesign();
      
      applicationParser.parse(sess);
      
      slideProduct.setName(slideInfo.getName());
      slideProduct.setIdVendor(slideInfo.getIdVendor());
      slideProduct.setIdOrganism(slideInfo.getIdOrganism());
      slideProduct.setIsActive(slideInfo.getIsActive());
      slideProduct.setIsCustom("N");
      slideProduct.setIsSlideSet("N");
      slideProduct.setArraysPerSlide(1);
      slideProduct.setSlidesInSet(1);
      saveApplications(slideProduct, applicationParser);
      
      sess.save(slideProduct);
      sess.flush();
      
      StringBuffer query = new StringBuffer("SELECT sp from SlideProduct sp");
      //query.append(" where sp.name=" + slideInfo.getName());
      List slide = sess.createQuery(query.toString()).list();
      
      for(int i = slide.size()-2; i < slide.size(); i++)
      {
        SlideProduct temp = (SlideProduct) slide.get(i);
        
        if(temp.getName() == slideInfo.getName())
        {
          slideDesign.setName(slideInfo.getName());
          slideDesign.setIsActive(slideInfo.getIsActive());
          slideDesign.setIdSlideProduct(temp.getIdSlideProduct()); 
        }
      }
      sess.save(slideDesign);
      sess.flush();
   }

   catch (Exception e) {
     log.error("An exception has occurred in SaveSimpleSlide ", e);
     e.printStackTrace();
     throw new RollBackCommandException(e.getMessage());
   } finally {
     try {
       HibernateSession.closeSession();
     } catch (Exception e) {
     }
   }
   this.xmlResult = "<SUCCESS institution=\"" + slideProduct.getIdSlideProduct()
   + "\"/>";
   setResponsePage(this.SUCCESS_JSP);
    return this;
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


