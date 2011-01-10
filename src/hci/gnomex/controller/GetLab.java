package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;


public class GetLab extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetLab.class);
  
  
  private Lab        lab;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    lab = new Lab();
    HashMap errors = this.loadDetailObject(request, lab);
    this.addInvalidFields(errors);
    
    if (lab.getIdLab() == null) {
      this.addInvalidField("idLab required", "idLab required");
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
    Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
    
    Lab theLab = (Lab)sess.get(Lab.class, lab.getIdLab());
    
    // We want the billing accounts to show up if the user is authorized to submit
    // requests for this lab
    if (this.getSecAdvisor().isGroupIAmMemberOrManagerOf(theLab.getIdLab())) {
      Hibernate.initialize(theLab.getBillingAccounts());
    }
    
    
    theLab.excludeMethodFromXML("getIsMyLab");
    theLab.excludeMethodFromXML("getCanSubmitRequests");
    theLab.excludeMethodFromXML("getCanManage");
    theLab.excludeMethodFromXML("getHasPublicData");
    
    Hibernate.initialize(theLab.getProjects());
   
    
    
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS) ||
        this.getSecAdvisor().canUpdate(lab, SecurityAdvisor.PROFILE_GROUP_MEMBERSHIP)) {
      
      Hibernate.initialize(theLab.getMembers());
      Hibernate.initialize(theLab.getCollaborators());
      Hibernate.initialize(theLab.getManagers());
      Hibernate.initialize(theLab.getInstitutions());
      
      blockAppUserContent(theLab.getMembers());
      blockAppUserContent(theLab.getCollaborators());
      blockAppUserContent(theLab.getManagers());
      
      // Get the total charges to date on all billing accounts
      StringBuffer buf = new StringBuffer("SELECT bi.idBillingAccount, sum(bi.totalPrice) ");
      buf.append(" FROM  BillingItem bi");
      buf.append(" WHERE bi.idLab = " + lab.getIdLab());
      buf.append(" GROUP BY bi.idBillingAccount ");
      List rows = sess.createQuery(buf.toString()).list();
      HashMap totalChargesMap = new HashMap();
      for(Iterator i = rows.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        totalChargesMap.put(row[0], row[1]);
      }
      for(Iterator i = theLab.getBillingAccounts().iterator(); i.hasNext();) {
        BillingAccount ba = (BillingAccount)i.next();
        ba.setTotalChargesToDate((BigDecimal)totalChargesMap.get(ba.getIdBillingAccount()));
      }

      
      
      Document doc = new Document(new Element("OpenLabList"));
      Element labNode = theLab.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      this.appendPossibleCollaborators(labNode, theLab);
      doc.getRootElement().addContent(labNode);
      
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
    } else if (this.getSecAdvisor().isGroupIAmMemberOf(theLab.getIdLab()) || 
                this.getSecAdvisor().isGroupICollaborateWith(theLab.getIdLab())) {
      
      // For adding services to lab, lab member needs to be able to select
      // from list of other lab members.
      if (this.getSecAdvisor().isGroupIAmMemberOf(theLab.getIdLab())) {
        Hibernate.initialize(theLab.getMembers());
        blockAppUserContent(theLab.getMembers());
      }  else {
        theLab.excludeMethodFromXML("getProjects");
      }
      
      theLab.excludeMethodFromXML("getApprovedBillingAccounts");
      theLab.excludeMethodFromXML("getPendingBillingAccounts");

      // Block details about total dollar amount on billing accounts
      for(Iterator i = theLab.getBillingAccounts().iterator(); i.hasNext();) {
        BillingAccount ba = (BillingAccount)i.next();
        ba.excludeMethodFromXML("getTotalDollarAmount");
        ba.excludeMethodFromXML("getTotalDollarAmountDisplay");
        ba.excludeMethodFromXML("getTotalDollarAmountRemaining");
        ba.excludeMethodFromXML("getTotalDollarAmountRemainingDisplay");
        ba.excludeMethodFromXML("getTotalChargesToDateDisplay");
      }

      
      Document doc = new Document(new Element("OpenLabList"));
      Element labNode = theLab.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
      if (this.getSecAdvisor().isGroupIAmMemberOrManagerOf(theLab.getIdLab())) {
        this.appendPossibleCollaborators(labNode, theLab);
      }
      doc.getRootElement().addContent(labNode);        
          
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
    } else {
      this.xmlResult = "<OpenLabList/>";
    }
      
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GetLab ", e);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
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
  
  private void appendPossibleCollaborators(Element labNode, Lab theLab) throws Exception {
    // Show all the members, collaborators, and mgr under this lab
    // if the user can submit requests
    Element possibleCollaboratorsNode = new Element("possibleCollaborators");
    labNode.addContent(possibleCollaboratorsNode);
    
    TreeMap appUsers = new TreeMap();
    for(Iterator i2 = theLab.getMembers().iterator(); i2.hasNext();) {
      AppUser u = (AppUser)i2.next();
      appUsers.put(u.getDisplayName(), u);
    }
    for(Iterator i2 = theLab.getCollaborators().iterator(); i2.hasNext();) {
      AppUser u = (AppUser)i2.next();
      appUsers.put(u.getDisplayName(), u);
    }
    for(Iterator i2 = theLab.getManagers().iterator(); i2.hasNext();) {
      AppUser u = (AppUser)i2.next();
      appUsers.put(u.getDisplayName(), u);
    }
    for (Iterator i2 = appUsers.keySet().iterator(); i2.hasNext();) {
      String key = (String)i2.next();
      AppUser user = (AppUser)appUsers.get(key);
      this.blockAppUserContent(user);
      
      possibleCollaboratorsNode.addContent(user.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement());            
    }
  }    
  
  private void blockAppUserContent(Set appUsers) {
    
    for(Iterator i1 = appUsers.iterator(); i1.hasNext();) {
      AppUser user = (AppUser)i1.next();
      blockAppUserContent(user);
    }
    
  }
  
  private void blockAppUserContent(AppUser user) {
    user.excludeMethodFromXML("getCodeUserPermissionKind");
    user.excludeMethodFromXML("getuNID");
    user.excludeMethodFromXML("getEmail");
    user.excludeMethodFromXML("getDepartment");
    user.excludeMethodFromXML("getInstitute");
    user.excludeMethodFromXML("getJobTitle");
    user.excludeMethodFromXML("getCodeUserPermissionKind");
    user.excludeMethodFromXML("getUserNameExternal");
    user.excludeMethodFromXML("getPasswordExternal");
    user.excludeMethodFromXML("getPhone");
    user.excludeMethodFromXML("getIsAdminPermissionLevel");
    user.excludeMethodFromXML("getIsLabPermissionLevel");
    user.excludeMethodFromXML("getLabs");
    user.excludeMethodFromXML("getCollaboratingLabs");
    user.excludeMethodFromXML("getManagingLabs");  
    user.excludeMethodFromXML("getPasswordExternalEntered");
    user.excludeMethodFromXML("getIsExternalUser");
    user.excludeMethodFromXML("getPasswordExternal");   

  }

}