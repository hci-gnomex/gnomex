package hci.gnomex.controller;

import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.ExperimentFactor;
import hci.gnomex.model.ExperimentFactorEntry;
import hci.gnomex.model.Project;
import hci.gnomex.model.QualityControlStep;
import hci.gnomex.model.QualityControlStepEntry;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import sun.tools.tree.ThisExpression;




public class SaveProject extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveProject.class);
  
  private String     projectXMLString;
  private Document   projectDoc;
  
  private Project    project;
  private boolean   isNewProject = false;
  private String     parseEntries = "N";
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    
    if (request.getParameter("projectXMLString") != null && !request.getParameter("projectXMLString").equals("")) {
      projectXMLString = request.getParameter("projectXMLString");
    }
    
    if (request.getParameter("parseEntries") != null && !request.getParameter("parseEntries").equals("")) {
      parseEntries = request.getParameter("parseEntries");
    }
    
    StringReader reader = new StringReader(projectXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      projectDoc = sax.build(reader);
    } catch (JDOMException je ) {
      log.error( "Cannot parse projectXMLString", je );
      this.addInvalidField( "RequestXMLString", "Invalid request xml");
    }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());

      Element projectNode = projectDoc.getRootElement();
      initializeProject(projectNode, sess);      
      
      if (project.getName() == null || project.getName().equals("")) {
        this.addInvalidField("projectName", "Project name is required.");
        this.setResponsePage(this.ERROR_JSP);
      }
      
      if (this.isValid() && this.getSecAdvisor().canUpdate(project)) {

        sess.save(project);
        sess.flush();

        
        if (parseEntries.equals("Y")) {
          initializeExperimentFactorEntries(projectNode.getChild("ExperimentFactorEntries"),  sess);
          initializeExperimentDesignEntries(projectNode.getChild("ExperimentDesignEntries"),  sess);
          
        }
        else {
          initializeExperimentFactors(projectNode.getChild("ExperimentFactor"),  sess);
          initializeExperimentDesigns(projectNode.getChild("ExperimentDesign"),  sess);
          initializeExperimentQuality(projectNode.getChild("ExperimentQuality"), sess);
          
        }
        
        sess.flush();

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(projectDoc);
      

        this.xmlResult = "<SUCCESS idProject=\"" + project.getIdProject() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save project.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveRequest ", e);
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
  
  
  private void initializeProject(Element n, Session sess) throws Exception {
    
    Integer idProject = new Integer(n.getAttributeValue("idProject"));
    if (idProject.intValue() == 0) {
      project = new Project();
      isNewProject = true;
      
    } else {
      project = (Project)sess.load(Project.class, idProject);
    }
    
    project.setName(RequestParser.unEscape(n.getAttributeValue("name")));
    project.setDescription(RequestParser.unEscapeBasic(n.getAttributeValue("description")));
    project.setIdAppUser(new Integer(n.getAttributeValue("idAppUser")));
    project.setIdLab(new Integer(n.getAttributeValue("idLab")));
    
    
  }
  
  
  private void initializeExperimentFactors(Element n, Session sess) throws Exception {
    // Delete the existing experiment design entries
    if (!isNewProject) {
      for(Iterator i = project.getExperimentFactorEntries().iterator(); i.hasNext();) {
        ExperimentFactorEntry entry = (ExperimentFactorEntry)i.next();
        sess.delete(entry);
      }
    }
    
    String otherLabel = n.getAttributeValue(ExperimentFactorEntry.OTHER_LABEL);

    for(Iterator i = n.getAttributes().iterator(); i.hasNext();) {
      
      Attribute a = (Attribute)i.next();
      String code = a.getName();
      String value = a.getValue();
      
      if (code.equals(ExperimentFactorEntry.OTHER_LABEL)) {
        continue;
      }
      
      if (code.equals(ExperimentFactor.OTHER)) {
        if (otherLabel != null && !otherLabel.equals("")) {
          ExperimentFactorEntry entry = new ExperimentFactorEntry();
          
          entry.setIdProject(project.getIdProject());
          entry.setCodeExperimentFactor(code);
          entry.setValue("Y");
          entry.setOtherLabel(RequestParser.unEscape(otherLabel));
          
          sess.save(entry);
        }
      } else {
        if (value != null && value.equalsIgnoreCase("Y")) {
          ExperimentFactorEntry entry = new ExperimentFactorEntry();
          
          entry.setIdProject(project.getIdProject());
          entry.setCodeExperimentFactor(code);
          entry.setValue("Y");
          
          sess.save(entry);
        }
      }
    }
    
    sess.flush();
  }

  private void initializeExperimentDesigns(Element n, Session sess) throws Exception {
    // Delete the existing experiment design entries
    if (!isNewProject) {
      for(Iterator i = project.getExperimentDesignEntries().iterator(); i.hasNext();) {
        ExperimentDesignEntry entry = (ExperimentDesignEntry)i.next();
        sess.delete(entry);
      }
    }

    
    String otherLabel = n.getAttributeValue(ExperimentDesignEntry.OTHER_LABEL);

    for(Iterator i = n.getAttributes().iterator(); i.hasNext();) {
      
      Attribute a = (Attribute)i.next();
      String code = a.getName();
      String value = a.getValue();
      
      if (code.equals(ExperimentDesignEntry.OTHER_LABEL)) {
        continue;
      }
      if (code.equals(ExperimentDesign.OTHER)) {
        if (otherLabel != null && !otherLabel.equals("")) {
          ExperimentDesignEntry entry = new ExperimentDesignEntry();
          entry.setIdProject(project.getIdProject());
          entry.setCodeExperimentDesign(code);
          entry.setValue("Y");
          entry.setOtherLabel(RequestParser.unEscape(otherLabel));
          sess.save(entry);
        }
        
      } else {
        if (value != null && value.equalsIgnoreCase("Y")) {
          ExperimentDesignEntry entry = new ExperimentDesignEntry();
          entry.setIdProject(project.getIdProject());          
          entry.setCodeExperimentDesign(code);
          entry.setValue("Y");
          sess.save(entry);
        }
        
      }
      
    }
    
    sess.flush();
  }

   private void initializeExperimentQuality(Element n, Session sess) throws Exception {
     // Delete existing quality control step entries
     if (!isNewProject) {
       for(Iterator i = project.getQualityControlStepEntries().iterator(); i.hasNext();) {
         QualityControlStepEntry entry = (QualityControlStepEntry)i.next();
         sess.delete(entry);
       }
     }
     
     String otherLabel = n.getAttributeValue(QualityControlStepEntry.OTHER_LABEL);
     String otherValidationLabel = n.getAttributeValue(QualityControlStepEntry.OTHER_VALIDATION_LABEL);

    
    for(Iterator i = n.getAttributes().iterator(); i.hasNext();) {
      
      Attribute a = (Attribute)i.next();
      String code = a.getName();
      String value = a.getValue();
      
      if (code.equals(QualityControlStepEntry.OTHER_LABEL) || 
          code.equals(QualityControlStepEntry.OTHER_VALIDATION_LABEL)) {
        continue;
      }
      
      if (code.equals(ExperimentDesign.OTHER)) {
        if (otherLabel != null && !otherLabel.equals("")) {
          QualityControlStepEntry entry = new QualityControlStepEntry();
          
          entry.setIdProject(project.getIdProject());
          entry.setCodeQualityControlStep(code);
          entry.setValue("Y");
          entry.setOtherLabel(RequestParser.unEscape(otherLabel));
          sess.save(entry);
        }
        
      } else if (code.equals(ExperimentDesign.OTHER_VALIDATION)) {
        if (otherValidationLabel != null && !otherValidationLabel.equals("")) {
          QualityControlStepEntry entry = new QualityControlStepEntry();
          
          entry.setIdProject(project.getIdProject());
          entry.setCodeQualityControlStep(code);
          entry.setValue("Y");
          entry.setOtherLabel(RequestParser.unEscape(otherValidationLabel));
          sess.save(entry);
        }
        
      } else {
        if (value != null && value.equalsIgnoreCase("Y")) {
          QualityControlStepEntry entry = new QualityControlStepEntry();
          
          entry.setIdProject(project.getIdProject());
          entry.setCodeQualityControlStep(code);
          entry.setValue("Y");
          sess.save(entry);
        }
        
      }
    }
    
    sess.flush();
  }
   
   private void initializeExperimentFactorEntries(Element n, Session sess) throws Exception {
     // Delete the existing experiment factor entries
     if (!isNewProject) {
       for(Iterator i = project.getExperimentFactorEntries().iterator(); i.hasNext();) {
         ExperimentFactorEntry entry = (ExperimentFactorEntry)i.next();
         sess.delete(entry);
       }
     }
     

     // Add experiment factor entry for each one marked as 'isSelected'.
     for(Iterator i = n.getChildren().iterator(); i.hasNext();) {
       
       Element node = (Element)i.next();
       
       String code = node.getAttributeValue("codeExperimentFactor");
       String isSelected = node.getAttributeValue("isSelected");
       String otherLabel = node.getAttributeValue("otherLabel");
       
       if (isSelected.equals("true")) {
         ExperimentFactorEntry entry = new ExperimentFactorEntry();
         entry.setIdProject(project.getIdProject());
         entry.setCodeExperimentFactor(code);
         entry.setValue("Y");
         if (otherLabel != null && !otherLabel.equals("")) {
           entry.setOtherLabel(otherLabel);             
         }
         
         sess.save(entry);         
       }       
     }
     
     sess.flush();
   }

   private void initializeExperimentDesignEntries(Element n, Session sess) throws Exception {
     // Delete the existing experiment design entries
     if (!isNewProject) {
       for(Iterator i = project.getExperimentDesignEntries().iterator(); i.hasNext();) {
         ExperimentDesignEntry entry = (ExperimentDesignEntry)i.next();
         sess.delete(entry);
       }
     }
     

     // Add experiment design entry for each one marked as 'isSelected'.
     for(Iterator i = n.getChildren().iterator(); i.hasNext();) {
       
       Element node = (Element)i.next();
       
       String code = node.getAttributeValue("codeExperimentDesign");
       String isSelected = node.getAttributeValue("isSelected");
       String otherLabel = node.getAttributeValue("otherLabel");
       
       if (isSelected.equals("true")) {
         ExperimentDesignEntry entry = new ExperimentDesignEntry();
         entry.setIdProject(project.getIdProject());
         entry.setCodeExperimentDesign(code);
         entry.setValue("Y");
         if (otherLabel != null && !otherLabel.equals("")) {
           entry.setOtherLabel(otherLabel);             
         }
         
         sess.save(entry);         
       }       
     }
     
     sess.flush();
   }





  
  

}