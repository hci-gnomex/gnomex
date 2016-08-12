package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.ExperimentDesign;
import hci.gnomex.model.ExperimentDesignEntry;
import hci.gnomex.model.ExperimentFactor;
import hci.gnomex.model.ExperimentFactorEntry;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Project;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;

public class GetProject extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(GetProject.class);
  
  private Integer idProject;
  private Integer idLab;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idProject") != null) {
      idProject = new Integer(request.getParameter("idProject"));
    } else {
      this.addInvalidField("idProject", "idProject is required");
    }

    if (request.getParameter("idLab") != null) {
      idLab = new Integer(request.getParameter("idLab"));
    } else {
      this.addInvalidField("idLab", "idLab is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      Project project = null;
      if (idProject.intValue() == 0) {
        project = new Project();
        project.setIdProject(new Integer(0));
        Lab l = (Lab)sess.load(Lab.class, idLab);
        project.setIdLab(idLab);
        project.setLab(l);
      } else {
        project = (Project)sess.get(Project.class, idProject);
        if (!this.getSecAdvisor().canRead(project)) {
          this.addInvalidField("permissionerror", "Insufficient permissions to access this project.");
        } else {
          this.getSecAdvisor().flagPermissions(project);
          
        }
      }
   
    
      if (isValid())  {
        StringBuffer queryBuf = new StringBuffer();
        queryBuf.append("SELECT ed from ExperimentDesign as ed ");
        List experimentDesigns = sess.createQuery(queryBuf.toString()).list();

        queryBuf = new StringBuffer();
        queryBuf.append("SELECT ef from ExperimentFactor as ef ");
        List experimentFactors = sess.createQuery(queryBuf.toString()).list();

        String showProjectAnnotations = "N";
        for(Iterator i = project.getLab().getCoreFacilities().iterator(); i.hasNext();) {
          CoreFacility facility = (CoreFacility)i.next();
          if (facility.getShowProjectAnnotations().equals("Y")) {
            showProjectAnnotations = "Y";
            break;
          }
        }
      

      
        Document doc = new Document(new Element("OpenProjectList"));
        Element projectNode = project.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        projectNode.setAttribute("showProjectAnnotations", showProjectAnnotations);
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
      }
    
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      LOG.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      LOG.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      LOG.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      LOG.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      LOG.error("An exception has occurred in GetProject ", e);
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