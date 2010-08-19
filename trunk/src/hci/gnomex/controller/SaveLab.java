package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Property;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingAccountParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabMemberParser;
import hci.gnomex.utility.MailUtil;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
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
  
  private String                         serverName;
  private String                         launchAppURL;
  
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
    
    try {
      launchAppURL = this.getLaunchAppURL(request);  
    } catch (Exception e) {
      log.warn("Cannot get launch app URL in SaveLab", e);
    }

    serverName = request.getServerName();

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
          
          // Need to initialize billing accounts; otherwise new accounts
          // get in the list and get deleted.
          Hibernate.initialize(lab.getBillingAccounts());
          
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
          
          // If billing account has just been approved, send out a notification
          // email to submitter of work auth form as well as lab managers
          if (ba.isJustApproved()) {
            this.sendApprovedBillingAccountEmail(sess, ba, lab);
          }
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
    lab.setIsExternalPricing(labScreen.getIsExternalPricing());
    lab.setIsActive(labScreen.getIsActive());
    
  }
  
  private void sendApprovedBillingAccountEmail(Session sess, BillingAccount billingAccount, Lab lab) throws NamingException, MessagingException {
    
    DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
    
    
    StringBuffer submitterNote = new StringBuffer();
    StringBuffer body = new StringBuffer();
    String submitterSubject = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " approved";    
    
    boolean send = false;
    String submitterEmail = billingAccount.getSubmitterEmail();
    String coreEmail = dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY);
    boolean isTestEmail = false;
    if (serverName.equals(dictionaryHelper.getProperty(Property.PRODUCTION_SERVER))) {
      send = true;
    } else {
      if (submitterEmail.equals(dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        isTestEmail = true;
        submitterSubject = "TEST - " + submitterSubject;
        coreEmail = dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER);
      }
    }
    
    submitterNote.append("The following work authorization " +
        "has been approved by the " + dictionaryHelper.getProperty(Property.CORE_FACILITY_NAME) +  
        ".  Lab members can now submit experiment " +
        "requests against this account in GNomEx " + launchAppURL + ".");


    body.append("\n");
    body.append("\n");
    body.append("Lab:               " + lab.getName() + "\n");
    body.append("Account:           " + billingAccount.getAccountName() + "\n");
    body.append("Chartfield:        " + billingAccount.getAccountNumber() + "\n");
    body.append("Funding Agency:    " + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "\n");
    body.append("Effective until:   " + billingAccount.getExpirationDateOther() + "\n");
    body.append("Submitter UID:     " + billingAccount.getSubmitterUID() + "\n");
    body.append("Submitter Email:   " + billingAccount.getSubmitterEmail() + "\n");
    

    if (send) {
      // Email submitter
      MailUtil.send(submitterEmail, 
            null,
            dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY), 
            submitterSubject, 
            submitterNote.toString() + body.toString(),
            false); 
      
      // Email lab managers
      for (Iterator i = lab.getManagers().iterator(); i.hasNext();) {
        AppUser manager = (AppUser)i.next();        
        if (manager.getEmail() != null && !manager.getEmail().equals("")) {
          String managerEmail = manager.getEmail();
          if (isTestEmail) {
            managerEmail = dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER);
          }
          // Email lab manager(s) for lab
          MailUtil.send(managerEmail, 
              null,
              dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY), 
              isTestEmail ? submitterSubject + " (for lab manager" + manager.getEmail()+ ")": submitterSubject, 
              submitterNote.toString() + body.toString(),
              false); 
        }
      }
      
      // Email lab contact email address(es)
      if (lab.getContactEmail() != null && !lab.getContactEmail().equals("")) {
        String contactEmail = lab.getContactEmail();
        if (isTestEmail) {
          contactEmail = dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER);
        }
        MailUtil.send(contactEmail, 
            null,
            dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY), 
            isTestEmail ? submitterSubject + " (for lab contact " + lab.getContactEmail() + ")" : submitterSubject, 
            submitterNote.toString() + body.toString(),
            false); 
                
      }

    }
    
  }
  
  private class AppUserComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AppUser u1 = (AppUser)o1;
      AppUser u2 = (AppUser)o2;
      
      return u1.getIdAppUser().compareTo(u2.getIdAppUser());
      
    }
  }

}