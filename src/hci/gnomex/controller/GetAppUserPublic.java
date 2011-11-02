package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AppUserPublic;
import hci.gnomex.model.BillingStatus;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetAppUserPublic extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAppUserPublic.class);
  
  
  private AppUserPublic        appUser;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    appUser = new AppUserPublic();
    HashMap errors = this.loadDetailObject(request, appUser);
    this.addInvalidFields(errors);
    
    if (appUser.getIdAppUser() == null) {
      this.addInvalidField("idAppUser required", "idAppUser required");
    }
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    Connection con = null;

    try {
      sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      AppUserPublic theAppUser = (AppUserPublic)sess.get(AppUserPublic.class, appUser.getIdAppUser());

      Document doc = new Document(theAppUser.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());

      Statement stmt = null;
      ResultSet rs = null;    
      con = sess.connection();      
      stmt = con.createStatement();

      StringBuffer buf = new StringBuffer("select lm.idLab, isnull(l.lastName, '') + isnull(', ' + l.firstName, '') + ' Lab' as labName, 'Manager' as role, isNull(sendUploadAlert, 'N') as doUploadAlert\n");
      buf.append(" from LabManager lm\n");
      buf.append("   join Lab l\n");
      buf.append("   on lm.idLab = l.idLab\n");
      buf.append(" where idAppUser = " + appUser.getIdAppUser().intValue() + "\n");
      buf.append(" union\n");
      buf.append(" select  lm.idLab, isnull(l.lastName, '') + isnull(', ' + l.firstName, '') + ' Lab' as labName, 'Collaborator' as role, isNull(sendUploadAlert, 'N') as doUploadAlert\n");
      buf.append(" from LabCollaborator lm\n");
      buf.append("   join Lab l\n");
      buf.append("   on lm.idLab = l.idLab\n");
      buf.append(" where idAppUser = " + appUser.getIdAppUser().intValue() + "\n");
      buf.append(" union\n");
      buf.append(" select  lm.idLab, isnull(l.lastName, '') + isnull(', ' + l.firstName, '') + ' Lab' as labName, 'User' as role, isNull(sendUploadAlert, 'N') as doUploadAlert\n");
      buf.append(" from LabUser lm\n");
      buf.append("   join Lab l\n");
      buf.append("   on lm.idLab = l.idLab\n");
      buf.append(" where idAppUser = " + appUser.getIdAppUser().intValue() + "\n");
      buf.append(" order by LabName, role\n");

      rs = stmt.executeQuery(buf.toString());
      Element notificationLabs = new Element("notificationLabs");
      while (rs.next()) {
        Element labNode = new Element("Lab");
        labNode.setAttribute("idLab", ""+rs.getInt("idLab"));
        labNode.setAttribute("labName", rs.getString("labName"));
        labNode.setAttribute("role", rs.getString("role"));
        labNode.setAttribute("doUploadAlert", rs.getString("doUploadAlert"));
        notificationLabs.addContent(labNode);   
      }
      doc.getRootElement().addContent(notificationLabs); 
      rs.close();
      stmt.close();      

      XMLOutputter out = new org.jdom.output.XMLOutputter();
      //this.xmlResult = out.outputString(theAppUser.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);  

    } catch (NamingException e){
      log.error("An exception has occurred in GetAppUserPublic ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());

    }catch (SQLException e) {
      log.error("An exception has occurred in GetAppUserPublic ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetAppUserPublic ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetAppUserPublic ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        if(sess != null) {
          if (con != null) {
            con.close();
          }
          this.getSecAdvisor().closeReadOnlyHibernateSession(); 
        }         
      } catch(Exception e) {

      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

    return this;
  }

}