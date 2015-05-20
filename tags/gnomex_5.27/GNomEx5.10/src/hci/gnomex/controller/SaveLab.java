package hci.gnomex.controller;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.model.NullDictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.CoreFacility;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Project;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingAccountComparator;
import hci.gnomex.utility.BillingAccountParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabCoreFacilityParser;
import hci.gnomex.utility.LabInstitutionParser;
import hci.gnomex.utility.LabMemberParser;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveLab extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveLab.class);

  private String                         institutionsXMLString;
  private Document                       institutionsDoc;
  private LabInstitutionParser           labInstitutionParser;

  private String                         membersXMLString;
  private Document                       membersDoc;
  private LabMemberParser                labMemberParser;

  private String                         collaboratorsXMLString;
  private Document                       collaboratorsDoc;
  private LabMemberParser                collaboratorParser;

  private String                         managersXMLString;
  private Document                       managersDoc;
  private LabMemberParser                managerParser;

  private String                         accountsXMLString;
  private Document                       accountsDoc;
  private BillingAccountParser           accountParser;
  
  private LabCoreFacilityParser             coreFacilityParser;

  private Lab                            labScreen;
  private boolean                        isNewLab = false;

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
    
    // It is invalid for both the first and last name of the lab to be blank.
    if (labScreen.getFirstName() == null || labScreen.getFirstName().trim().equals("")) {
      if (labScreen.getLastName() == null || labScreen.getLastName().trim().equals("")) {
        this.addInvalidField("reqdname", "Lab first or last name must be filled in");
      }
    }


    if (request.getParameter("institutionsXMLString") != null && !request.getParameter("institutionsXMLString").equals("")) {
      institutionsXMLString = request.getParameter("institutionsXMLString");
    }

    StringReader reader = new StringReader(institutionsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      institutionsDoc = sax.build(reader);
      labInstitutionParser = new LabInstitutionParser(institutionsDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse institutionsXMLString", je );
      this.addInvalidField( "institutionsXMLString", "Invalid institutionsXMLString");
    }


    if (request.getParameter("membersXMLString") != null && !request.getParameter("membersXMLString").equals("")) {
      membersXMLString = request.getParameter("membersXMLString");
    }

    reader = new StringReader(membersXMLString);
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

    String coreFacilitiesXMLString = "";
    if (request.getParameter("coreFacilitiesXMLString") != null && !request.getParameter("coreFacilitiesXMLString").equals("")) {
      coreFacilitiesXMLString = request.getParameter("coreFacilitiesXMLString");

      reader = new StringReader(coreFacilitiesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        Document coreFacilitiesDoc = sax.build(reader);
        coreFacilityParser = new LabCoreFacilityParser(coreFacilitiesDoc);
  
      } catch (JDOMException je ) {
        log.error( "Cannot parse coreFacilitiesXMLString", je );
        this.addInvalidField( "coreFacilitiesXMLString", "Invalid coreFacilitiesXMLString");
      }
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
      
      StringBuffer buf = new StringBuffer();
      boolean whereAdded = false;
      buf.append("from Lab ");
      if (labScreen.getLastName() != null && !labScreen.getLastName().trim().equals("")) {
        buf.append(whereAdded ? " AND " : " WHERE" );
        buf.append(" upper(lastName) = :lastName"); //'" +  labScreen.getLastName().toUpperCase()+ "'");
        whereAdded = true;
      }
      if (labScreen.getFirstName() != null && !labScreen.getFirstName().trim().equals("")) {
        buf.append(whereAdded ? " AND " : " WHERE" );
        buf.append(" upper(firstName) = :firstName"); //'" +  labScreen.getFirstName().toUpperCase()+ "'");
        whereAdded = true;
      }
      // If this is an existing lab, check for duplicate lab name, excluding this lab.
      if (!isNewLab) {
        buf.append(" AND idLab != :idLab"); // + labScreen.getIdLab().toString());
      }
      Query labsQuery = sess.createQuery(buf.toString());
      if (labScreen.getLastName() != null && !labScreen.getLastName().trim().equals("")) {
        labsQuery.setParameter("lastName", labScreen.getLastName().toUpperCase());
      }
      if (labScreen.getFirstName() != null && !labScreen.getFirstName().trim().equals("")) {
        labsQuery.setParameter("firstName", labScreen.getFirstName().toUpperCase());
      }
      if (!isNewLab) {
        labsQuery.setParameter("idLab", labScreen.getIdLab());
      }
      List labs = labsQuery.list();
      
      // If there are duplicate labs, throw an error.
      if (labs.size() > 0) {
        String labFirstName = labScreen.getFirstName() == null || labScreen.getFirstName().trim().equals("") ? "" : labScreen.getFirstName() + " ";
        this.addInvalidField("Duplicate Lab Name", "The lab name " + labFirstName + labScreen.getLastName() + " is already in use.");
        setResponsePage(this.ERROR_JSP);
      }
      
      accountParser.parse(sess);
     
      // For some reason, this code is necessary for the billing account saves.  Without this here, the
      // billing accounts are deleted, perhaps because the lab isn't iniatialized with all of the
      // billing accounts?
      for(Iterator i = accountParser.getBillingAccountMap().keySet().iterator(); i.hasNext();) {
        String idBillingAccountString = (String)i.next();
        BillingAccount ba = (BillingAccount)accountParser.getBillingAccountMap().get(idBillingAccountString);    
      }

      if (isValid()) {
        if (!this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to save lab.");
          setResponsePage(this.ERROR_JSP);
        }
      }
      
      Lab lab = null;
      
      if (isValid()) {
          accountParser.parse(sess);
          labInstitutionParser.parse(sess);
          labMemberParser.parse(sess);
          collaboratorParser.parse(sess);
          managerParser.parse(sess);
          if (coreFacilityParser != null) {
            coreFacilityParser.parse(sess);
          }
  
          if (isNewLab) {
            lab = labScreen;
            lab.setIncludePiInBillingEmails("Y"); // Default to include pi in billing emails on new labs.
            sess.save(lab);
            
          } else {
  
            lab = (Lab)sess.load(Lab.class, labScreen.getIdLab());
  
            if (!lab.getVersion().equals(labScreen.getVersion())) {
              this.addInvalidField("Locking Violation", "Lab modified by another user.  Please refresh and enter your changes again.");
              setResponsePage(this.ERROR_JSP);
            } else {
              // Need to initialize billing accounts; otherwise new accounts
              // get in the list and get deleted.
              Hibernate.initialize(lab.getBillingAccounts());
    
              initializeLab(lab);
            }
          }
      }
      
      if (isValid()) {
          //
          // Save billing accounts
          //
          Set<BillingAccount> billingAccountsToSave = new TreeSet<BillingAccount>(new BillingAccountComparator());
          for(Iterator i = accountParser.getBillingAccountMap().keySet().iterator(); i.hasNext();) {
            String idBillingAccountString = (String)i.next();
            BillingAccount ba = (BillingAccount)accountParser.getBillingAccountMap().get(idBillingAccountString);
            ba.setIdLab(lab.getIdLab());
            sess.save(ba);
            billingAccountsToSave.add(ba);
            
            //If the billing account isPO then change billing status of all billingItems in the account 
            List billingItems = sess.createQuery("select bi from BillingItem bi where idBillingAccount = " + ba.getIdBillingAccount()).list();
            if(ba.getIsPO().equals("Y")){
              for(Iterator bIterator = billingItems.iterator(); bIterator.hasNext();){
                BillingItem bi = (BillingItem)bIterator.next();
                if(bi.getCodeBillingStatus().equals(BillingStatus.APPROVED)){
                  bi.setCodeBillingStatus(BillingStatus.APPROVED_PO);
                  sess.save(bi);
                }
              }
            }
            else if(ba.getIsPO().equals("N")){
              for(Iterator bIterator = billingItems.iterator(); bIterator.hasNext();){
                BillingItem bi = (BillingItem)bIterator.next();
                if(bi.getCodeBillingStatus().equals(BillingStatus.APPROVED_PO)){
                  bi.setCodeBillingStatus(BillingStatus.APPROVED);
                  sess.save(bi);
                }
              }  
            }
            sess.flush();
  
            // If billing account has just been approved, send out a notification
            // email to submitter of work auth form as well as lab managers
            if (ba.isJustApproved()) {
              this.sendApprovedBillingAccountEmail(sess, ba, lab);
            }
          }
         // lab.setBillingAccounts(billingAccountsToSave);
          //sess.flush();
  
          // Remove billing accounts no longer in the billing account list
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
              ba.setUsers( null );
              lab.getBillingAccounts().remove(ba);
              sess.delete(ba);
            }
          }
  
          //
          // Save lab institutions
          //
          TreeSet institutions = new TreeSet(new InstitutionComparator());
          for(Iterator i = labInstitutionParser.getInstititionMap().keySet().iterator(); i.hasNext();) {
            Integer idInstitution = (Integer)i.next();
            Institution institution = (Institution)labInstitutionParser.getInstititionMap().get(idInstitution);     
            institutions.add(institution);
          }
          lab.setInstitutions(institutions);
  
          sess.flush();
  
          //
          // Save lab members
          //
          // Lab members to keep
          TreeSet members = new TreeSet(new AppUserComparator());
          for(Iterator i = labMemberParser.getAppUserMap().keySet().iterator(); i.hasNext();) {
            Integer idAppUser = (Integer)i.next();
            AppUser appUser = (AppUser)labMemberParser.getAppUserMap().get(idAppUser);     
            members.add(appUser);
          }
          
          // Lab members to remove
          List membersToRemove = new ArrayList();
          if (lab.getMembers() != null) {
            for(Iterator i = lab.getMembers().iterator(); i.hasNext();) {
              AppUser user = (AppUser)i.next();
              if (!labMemberParser.getAppUserMap().containsKey(user.getIdAppUser())) {
                membersToRemove.add(user);
              }
            }
            // Remove the lab member from any accounts they are users on
            if (lab.getBillingAccounts() != null) {
              for(Iterator i = lab.getBillingAccounts().iterator(); i.hasNext();) {
                BillingAccount ba = (BillingAccount)i.next();
                for(Iterator i2 = membersToRemove.iterator(); i2.hasNext();) {
                  AppUser user = (AppUser)i2.next(); 
                    ba.getUsers().remove( user );
                }
              }
            }
          }
          // Save the members who are still part of the lab
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
          
          //
          // delete any empty projects where user is no longer associated with this lab
          //
          ArrayList<Integer> labAssociatedIds = new ArrayList<Integer>();
          for(Iterator i=lab.getManagers().iterator();i.hasNext();) {
            AppUser a = (AppUser)i.next();
            labAssociatedIds.add(a.getIdAppUser());
          }
          for(Iterator i=lab.getCollaborators().iterator();i.hasNext();) {
            AppUser a = (AppUser)i.next();
            labAssociatedIds.add(a.getIdAppUser());
          }
          for(Iterator i=lab.getMembers().iterator();i.hasNext();) {
            AppUser a = (AppUser)i.next();
            labAssociatedIds.add(a.getIdAppUser());
          }
          if (labAssociatedIds.size() == 0) {
            labAssociatedIds.add(-1);
          }
          String deleteProjectString = "select p from Project p " +
            "where p.idLab is not null " +
            "and p.idAppUser not in (:ids) " +
            "and p.idProject not in (select d.idProject from ExperimentDesignEntry d) " +
            "and p.idProject not in (select f.idProject from ExperimentFactorEntry f) " +
            "and p.idProject not in (select q.idProject from QualityControlStepEntry q) " +
            "and p.idProject not in (select r.idProject from Request r) " + 
            "and p.idLab=:idLab ";
          Query query = sess.createQuery(deleteProjectString);
          query.setParameterList("ids", labAssociatedIds);
          query.setParameter("idLab", lab.getIdLab());
          List projectsToDelete = query.list();
          for(Iterator i=projectsToDelete.iterator();i.hasNext();) {
            sess.delete(i.next());
          }
          sess.flush();
          
          //
          // Create default user projects
          //
          HashMap<Integer, List<Project>> userProjectMap = new HashMap<Integer, List<Project>>();
          HashMap<Integer, AppUser> userMap = new HashMap<Integer, AppUser>(); 
          initializeUserProjectMap(userProjectMap, userMap, (Set<AppUser>)lab.getMembers());
          initializeUserProjectMap(userProjectMap, userMap, (Set<AppUser>)lab.getManagers());
          hashProjects(userProjectMap, lab);
          // Now iterate through the hash and create a default project for each user
          // where one is not present
          for (Integer idAppUser : userProjectMap.keySet()) {
            List<Project> defaultProjects = userProjectMap.get(idAppUser);
            if (defaultProjects == null || defaultProjects.isEmpty()) {
              Project project = new Project();
              project.setIdAppUser(idAppUser);
              project.setIdLab(lab.getIdLab());
              AppUser theUser = userMap.get(idAppUser);
              project.setName("Experiments for " + theUser.getFirstLastDisplayName());
              sess.save(project);
            }
          }
          sess.flush();
  
          //
          // Save core facilities
          //
          if (this.isNewLab) {
            // If a core admin (not a super admin) is adding the lab,
            // assign lab to same core facilty that the admin manages;            
            if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) &&
                this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS) &&
                this.getSecAdvisor().getCoreFacilitiesIManage().size() > 0) {
              TreeSet facilities = new TreeSet(new CoreFacilityComparator());
              for(Iterator i = this.getSecAdvisor().getCoreFacilitiesIManage().iterator(); i.hasNext();) {
                CoreFacility facility = (CoreFacility)i.next();
                facilities.add(facility);
              }
              lab.setCoreFacilities(facilities);
              sess.flush();
            } else {
              // If a super admin is adding the lab (or an admin that isn't managing a core facility),
              // see if there is just one core facility.  in this case, assign the lab to that
              // core facility.
              int coreFacilityCount = 0;
              CoreFacility coreFacilityDefault = null;
              for (Iterator i = DictionaryManager.getDictionaryEntries("hci.gnomex.model.CoreFacility").iterator(); i.hasNext();) {
                DictionaryEntry de = (DictionaryEntry)i.next();
                if (de instanceof NullDictionaryEntry) {
                  continue;
                }
                CoreFacility cf = (CoreFacility)de;
                if (cf.getIsActive() != null && cf.getIsActive().equals("Y")) {
                  coreFacilityCount++;
                  if (coreFacilityDefault == null) {
                    coreFacilityDefault = cf;
                  }
                }
              }
              if (coreFacilityCount ==  1) {
                TreeSet coreFacilities = new TreeSet(new CoreFacilityComparator());
                coreFacilities.add(coreFacilityDefault);
                lab.setCoreFacilities(coreFacilities);
                sess.flush();
              }
            }

          } else {
            if ((this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) || this.getSecAdvisor().getCoreFacilitiesIManage().size()>0) 
                && coreFacilityParser != null) {
              TreeSet facilities = new TreeSet(new CoreFacilityComparator());
              for(Iterator i = coreFacilityParser.getCoreFacilityMap().keySet().iterator(); i.hasNext();) {
                Integer idCoreFacility = (Integer)i.next();
                CoreFacility facility = (CoreFacility)coreFacilityParser.getCoreFacilityMap().get(idCoreFacility);
                facilities.add(facility);
              }
              lab.setCoreFacilities(facilities);
      
              sess.flush();
            }
          }
          
          this.xmlResult = "<SUCCESS idLab=\"" + lab.getIdLab() + "\"/>";
  
          setResponsePage(this.SUCCESS_JSP);
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
  
  private void initializeUserProjectMap(HashMap<Integer, List<Project>> userProjectMap, HashMap<Integer, AppUser> userMap, Set<AppUser> theUsers) {
    for (AppUser theUser : theUsers) {
      List<Project>userProjects = userProjectMap.get(theUser.getIdAppUser());
      if (userProjects == null) {
        userProjects = new ArrayList<Project>();
        userProjectMap.put(theUser.getIdAppUser(), userProjects);
        userMap.put(theUser.getIdAppUser(), theUser);
      }
    }
  }
  private void hashProjects(HashMap<Integer, List<Project>> userProjectMap, Lab lab) {
    //  for each project with idAppUser, associate it with the active members and managers of the lab
    if (lab.getProjects() != null) {
      for (Project proj : (Set<Project>)lab.getProjects()) {
        if (proj.getIdAppUser() != null) {
          List<Project> theProjects = userProjectMap.get(proj.getIdAppUser());
          if (theProjects != null) {
            theProjects.add(proj);
          }
        }
      }
    }    
  }

  private void initializeLab(Lab lab) {
    lab.setFirstName(labScreen.getFirstName() != null ? labScreen.getFirstName().trim() : labScreen.getFirstName());
    lab.setLastName(labScreen.getLastName() != null ? labScreen.getLastName().trim() : labScreen.getLastName());
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
    lab.setIsExternalPricingCommercial(labScreen.getIsExternalPricingCommercial());
    lab.setIsActive(labScreen.getIsActive());
    lab.setExcludeUsage(labScreen.getExcludeUsage());
    lab.setBillingContactEmail(labScreen.getBillingContactEmail());
    lab.setIncludePiInBillingEmails(labScreen.getIncludePiInBillingEmails());

  }

  private void sendApprovedBillingAccountEmail(Session sess, BillingAccount billingAccount, Lab lab) throws NamingException, MessagingException {

    PropertyDictionaryHelper dictionaryHelper = PropertyDictionaryHelper.getInstance(sess);


    StringBuffer submitterNote = new StringBuffer();
    StringBuffer body = new StringBuffer();
    String submitterSubject = "GNomEx Work authorization '" + billingAccount.getAccountName() + "' for " + lab.getName() + " approved";    

    boolean send = false;
    String submitterEmail = billingAccount.getSubmitterEmail();

    CoreFacility facility = (CoreFacility)sess.load(CoreFacility.class, billingAccount.getIdCoreFacility());

    String facilityEmail = dictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);


    boolean isTestEmail = false;
    if (dictionaryHelper.isProductionServer(serverName)) {
      send = true;
    } else {
      if (submitterEmail.equals(dictionaryHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        isTestEmail = true;
        submitterSubject = "TEST - " + submitterSubject;
        facilityEmail = dictionaryHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
      }
    }

    submitterNote.append("The following work authorization " +
        "has been approved by the " + facility.getDisplay() + " Core" +  
        ".  Lab members can now submit experiment " +
        "requests against this account in GNomEx " + launchAppURL + ".");


    body.append("\n");
    body.append("\n");
    body.append("Lab:               " + lab.getName() + "\n");
    body.append("Core Facility      " + facility.getDisplay() + "\n");
    body.append("Account:           " + billingAccount.getAccountName() + "\n");
    body.append("Chartfield:        " + billingAccount.getAccountNumber() + "\n");
    if (billingAccount.getIdFundingAgency() != null) {
      body.append("Funding Agency:    " + DictionaryManager.getDisplay("hci.gnomex.model.FundingAgency", billingAccount.getIdFundingAgency().toString()) + "\n");
    }
    if (billingAccount.getExpirationDateOther() != null && billingAccount.getExpirationDateOther().length() > 0) {
      body.append("Effective until:   " + billingAccount.getExpirationDateOther() + "\n");
    }
    body.append("Submitter UID:     " + billingAccount.getSubmitterUID() + "\n");
    body.append("Submitter Email:   " + billingAccount.getSubmitterEmail() + "\n");

    String from = dictionaryHelper.getCoreFacilityProperty(facility.getIdCoreFacility(), PropertyDictionary.CONTACT_EMAIL_CORE_FACILITY);
    

    if (send) {
      // Email submitter
      MailUtil.send(submitterEmail, 
          null,
          from, 
          submitterSubject, 
          submitterNote.toString() + body.toString(),
          false); 


      // Email lab contact email address(es)
      if (lab.getBillingNotificationEmail() != null && !lab.getBillingNotificationEmail().equals("")) {
        String contactEmail = lab.getBillingNotificationEmail();
        if (isTestEmail) {
          contactEmail = dictionaryHelper.getProperty(PropertyDictionary.CONTACT_EMAIL_SOFTWARE_TESTER);
        }
        MailUtil.send(contactEmail, 
            null,
            from, 
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

  private class InstitutionComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      Institution u1 = (Institution)o1;
      Institution u2 = (Institution)o2;

      return u1.getIdInstitution().compareTo(u2.getIdInstitution());

    }
  }

  private class CoreFacilityComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      CoreFacility u1 = (CoreFacility)o1;
      CoreFacility u2 = (CoreFacility)o2;

      return u1.getIdCoreFacility().compareTo(u2.getIdCoreFacility());

    }
  }

}