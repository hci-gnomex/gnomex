package hci.gnomex.controller;

import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
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
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;


import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.ExperimentFactor;
import hci.gnomex.model.ExperimentFactorEntry;
import hci.gnomex.model.Project;


public class GetProject extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetProject.class);
  
  private Integer idProject;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idProject") != null) {
      idProject = new Integer(request.getParameter("idProject"));
    } else {
      this.addInvalidField("idProject", "idProject is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      Project project = null;
      if (idProject.intValue() == 0) {
        project = new Project();
        project.setIdProject(new Integer(0));
      } else {
        project = (Project)sess.get(Project.class, idProject);
        
      }
   
    
      
      StringBuffer queryBuf = new StringBuffer();
      queryBuf.append("SELECT ed from ExperimentDesign as ed ");
      List experimentDesigns = sess.createQuery(queryBuf.toString()).list();

      queryBuf = new StringBuffer();
      queryBuf.append("SELECT ef from ExperimentFactor as ef ");
      List experimentFactors = sess.createQuery(queryBuf.toString()).list();

      
      if (this.getSecAdvisor().canRead(project)) {
      
        this.getSecAdvisor().flagPermissions(project);

      
        Document doc = new Document(new Element("OpenProjectList"));
        Element projectNode = project.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(projectNode);

        // Show list of experiment design entries
        Element edParentNode = new Element("ExperimentDesignEntries");
        projectNode.addContent(edParentNode);
        for(Iterator i = experimentDesigns.iterator(); i.hasNext();) {
          ExperimentDesign ed = (ExperimentDesign)i.next();

          Element edNode = new Element("ExperimentDesignEntry");
          ExperimentDesignEntry entry = null;
          for(Iterator i1 = project.getExperimentDesignEntries().iterator(); i1.hasNext();) {
            ExperimentDesignEntry edEntry = (ExperimentDesignEntry)i1.next();
            if (edEntry.getCodeExperimentDesign().equals(ed.getCodeExperimentDesign())) {
              entry = edEntry;
              break;
            }
          }
          edNode.setAttribute("codeExperimentDesign", ed.getCodeExperimentDesign());
          edNode.setAttribute("experimentDesign", ed.getExperimentDesign());
          edNode.setAttribute("otherLabel", entry != null && entry.getOtherLabel() != null ? entry.getOtherLabel() : "");
          edNode.setAttribute("isSelected", entry != null ? "true" : "false");
                  
          edParentNode.addContent(edNode);
        }
        
        // Show list of experiment Factor entries
        Element efParentNode = new Element("ExperimentFactorEntries");
        projectNode.addContent(efParentNode);
        for(Iterator i = experimentFactors.iterator(); i.hasNext();) {
          ExperimentFactor ef = (ExperimentFactor)i.next();

          Element efNode = new Element("ExperimentFactorEntry");
          ExperimentFactorEntry entry = null;
          for(Iterator i1 = project.getExperimentFactorEntries().iterator(); i1.hasNext();) {
            ExperimentFactorEntry efEntry = (ExperimentFactorEntry)i1.next();
            if (efEntry.getCodeExperimentFactor().equals(ef.getCodeExperimentFactor())) {
              entry = efEntry;
              break;
            }
          }
          efNode.setAttribute("codeExperimentFactor", ef.getCodeExperimentFactor());
          efNode.setAttribute("experimentFactor", ef.getExperimentFactor());
          efNode.setAttribute("otherLabel", entry != null && entry.getOtherLabel() != null ? entry.getOtherLabel() : "");
          efNode.setAttribute("isSelected", entry != null ? "true" : "false");
                  
          efParentNode.addContent(efNode);
        }

      
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);
      } else {
        this.addInvalidField("Insufficient Permission", "Insufficient permission to access this project");      
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetProject ", e);
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