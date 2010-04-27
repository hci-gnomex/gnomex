package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingAccountParser;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabMemberParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveLab extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveLab.class);
  
  private String                membersXMLString;
  private Document              membersDoc;
  private LabMemberParser       labMemberParser;
  
  private String                collaboratorsXMLString;
  private Document              collaboratorsDoc;
  private LabMemberParser       collaboratorParser;

  private String                managersXMLString;
  private Document              managersDoc;
  private LabMemberParser       managerParser;

  private String                accountsXMLString;
  private Document              accountsDoc;
  private BillingAccountParser  accountParser;

  private Lab                   labScreen;
  private boolean              isNewLab = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    labScreen = new Lab();
    HashMap errors = this.loadDetailObject(request, labScreen);
    this.addInvalidFields(errors);
    if (labScreen.getIdLab() == null || labScreen.getIdLab().intValue() == 0) {
      isNewLab = true;
    }
    
    
    if (request.getParameter("membersXMLString") != null && !request.getParameter("membersXMLString").equals("")) {
      membersXMLString = request.getParameter("membersXMLString");
    }
    
    StringReader reader = new StringReader(membersXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      membersDoc = sax.build(reader);
      labMemberParser = new LabMemberParser(membersDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse membersXMLString", je );
      this.addInvalidField( "membersXMLString", "Invalid membersXMLString");
    }
    
    if (request.getParameter("collaboratorsXMLString") != null && !request.getParameter("collaboratorsXMLString").equals("")) {
      collaboratorsXMLString = request.getParameter("collaboratorsXMLString");
    }
    
    reader = new StringReader(collaboratorsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      collaboratorsDoc = sax.build(reader);
      collaboratorParser = new LabMemberParser(collaboratorsDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse collaboratorsXMLString", je );
      this.addInvalidField( "collaboratorsXMLString", "Invalid collaboratorsXMLString");
    }
    
    if (request.getParameter("accountsXMLString") != null && !request.getParameter("accountsXMLString").equals("")) {
      accountsXMLString = request.getParameter("accountsXMLString");
    }
    
    

    if (request.getParameter("managersXMLString") != null && !request.getParameter("managersXMLString").equals("")) {
      managersXMLString = request.getParameter("managersXMLString");
    }
    
    reader = new StringReader(managersXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      managersDoc = sax.build(reader);
      managerParser = new LabMemberParser(managersDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse managersXMLString", je );
      this.addInvalidField( "managersXMLString", "Invalid managersXMLString");
    }
    
    
    if (request.getParameter("accountsXMLString") != null && !request.getParameter("accountsXMLString").equals("")) {
      accountsXMLString = request.getParameter("accountsXMLString");
    }
    
    reader = new StringReader(accountsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      accountsDoc = sax.build(reader);
      accountParser = new BillingAccountParser(accountsDoc);
      
    } catch (JDOMException je ) {
      log.error( "Cannot parse accountsXMLString", je );
      this.addInvalidField( "accountsXMLString", "Invalid accountsXMLString");
    }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
        accountParser.parse(sess);
        labMemberParser.parse(sess);
        collaboratorParser.parse(sess);
        managerParser.parse(sess);
        
        Lab lab = null;
              
        if (isNewLab) {
          lab = labScreen;
          sess.save(lab);
        } else {
          
          lab = (Lab)sess.load(Lab.class, labScreen.getIdLab());
          
          initializeLab(lab);
        }


        //
        // Save billing accounts
        //
        for(Iterator i = accountParser.getBillingAccountMap().keySet().iterator(); i.hasNext();) {
          String idBillingAccountString = (String)i.next();
          BillingAccount ba = (BillingAccount)accountParser.getBillingAccountMap().get(idBillingAccountString);
          ba.setIdLab(lab.getIdLab());
          sess.save(ba);
        }
        
        // Remove billing accounts no longer in the billing acount list
        List billingAccountsToRemove = new ArrayList();
        if (lab.getBillingAccounts() != null) {
          for(Iterator i = lab.getBillingAccounts().iterator(); i.hasNext();) {
            BillingAccount ba = (BillingAccount)i.next();
            if (!accountParser.getBillingAccountMap().containsKey(ba.getIdBillingAccount().toString())) {
              billingAccountsToRemove.add(ba);
            }
          }
          for(Iterator i = billingAccountsToRemove.iterator(); i.hasNext();) {
            BillingAccount ba = (BillingAccount)i.next();
            lab.getBillingAccounts().remove(ba);
            sess.delete(ba);
          }
        }
        
        //
        // Save lab members
        //
        TreeSet members = new TreeSet(new AppUserComparator());
        for(Iterator i = labMemberParser.getAppUserMap().keySet().iterator(); i.hasNext();) {
          Integer idAppUser = (Integer)i.next();
          AppUser appUser = (AppUser)labMemberParser.getAppUserMap().get(idAppUser);     
          members.add(appUser);
        }
        lab.setMembers(members);
        
        sess.flush();
        
        
        //
        // Save lab collaborators
        //
        TreeSet collaborators = new TreeSet(new AppUserComparator());
        for(Iterator i = collaboratorParser.getAppUserMap().keySet().iterator(); i.hasNext();) {
          Integer idAppUser = (Integer)i.next();
          AppUser appUser = (AppUser)collaboratorParser.getAppUserMap().get(idAppUser);     
          collaborators.add(appUser);
        }
        lab.setCollaborators(collaborators);
        
        sess.flush();
        
        
        
        //
        // Save lab managers
        //
        TreeSet managers = new TreeSet(new AppUserComparator());
        for(Iterator i = managerParser.getAppUserMap().keySet().iterator(); i.hasNext();) {
          Integer idAppUser = (Integer)i.next();
          AppUser appUser = (AppUser)managerParser.getAppUserMap().get(idAppUser);     
          managers.add(appUser);
        }
        lab.setManagers(managers);
        
        sess.flush();

        
        this.xmlResult = "<SUCCESS idLab=\"" + lab.getIdLab() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save lab.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveLab ", e);
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
  
  private void initializeLab(Lab lab) {
    lab.setFirstName(labScreen.getFirstName());
    lab.setLastName(labScreen.getLastName());
    lab.setDepartment(labScreen.getDepartment());
    
    lab.setContactAddress(labScreen.getContactAddress());
    lab.setContactCity(labScreen.getContactCity());
    lab.setContactCodeState(labScreen.getContactCodeState());
    lab.setContactEmail(labScreen.getContactEmail());
    lab.setContactName(labScreen.getContactName());
    lab.setContactPhone(labScreen.getContactPhone());
    lab.setContactZip(labScreen.getContactZip());
    lab.setIsCcsgMember(labScreen.getIsCcsgMember());
    lab.setIsExternal(labScreen.getIsExternal());
    lab.setIsActive(labScreen.getIsActive());
    
  }
  
  private class AppUserComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AppUser u1 = (AppUser)o1;
      AppUser u2 = (AppUser)o2;
      
      return u1.getIdAppUser().compareTo(u2.getIdAppUser());
      
    }
  }

}