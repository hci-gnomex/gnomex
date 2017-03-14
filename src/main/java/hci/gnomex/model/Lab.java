package hci.gnomex.model;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.*;

import hci.hibernate5utils.HibernateDetailObject;


public class Lab extends HibernateDetailObject implements java.lang.Comparable {
  private Integer idLab;
  private String  firstName;
  private String  lastName;
  private String  isExternalPricing;
  private String  isExternalPricingCommercial;
  private String  department;
  private String  contactName;
  private String  contactAddress2;
  private String  contactAddress;
  private String  contactCodeState;
  private String  contactCity;
  private String  contactZip;
  private String  contactCountry;
  private String  contactEmail;
  private String  contactPhone;
  private String  isCcsgMember;
  private String  isActive;
  private String  excludeUsage;
  private String  billingContactPhone;
  private String  billingContactEmail;  private Long    version;
  private Set     billingAccounts;
  private Set     members;
  private Set     collaborators;
  private Set     managers;
  private Set     projects;
  private Set     institutions;
  private Set     coreFacilities;

  // Permission flag
  private boolean canSubmitRequests = false;
  private boolean canManage = false;
  private boolean isMyLab = false;
  private boolean canGuestSubmit = false;


  public String getContactAddress() {
    return contactAddress;
  }

  public void setContactAddress(String contactAddress) {
    this.contactAddress = contactAddress;
  }

  public String getContactCity() {
    return contactCity;
  }

  public void setContactCity(String contactCity) {
    this.contactCity = contactCity;
  }

  public String getContactCodeState() {
    return contactCodeState;
  }

  public void setContactCodeState(String contactCodeState) {
    this.contactCodeState = contactCodeState;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getContactAddress2() {
    return contactAddress2;
  }

  public void setContactAddress2(String contactAddress2) {
    this.contactAddress2 = contactAddress2;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
  }

  public String getContactZip() {
    return contactZip;
  }

  public void setContactZip(String contactZip) {
    this.contactZip = contactZip;
  }

  public String getContactCountry() {
    return contactCountry;
  }

  public void setContactCountry(String contactCountry) {
    this.contactCountry = contactCountry;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public Integer getIdLab() {
    return idLab;
  }

  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }


  public String getBillingContactEmail() {
    return billingContactEmail;
  }

  public void setBillingContactEmail(String billingContactEmail) {
    this.billingContactEmail = billingContactEmail;
  }


  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public String getName() {
    return getName(true,true,true);
  }

  public String getNameFirstLast() {
    return getName(false, true, true);
  }

  public String getName( Boolean lastNameFirst, Boolean includeLab ) {
    return getName(lastNameFirst, includeLab, false);
  }


  public String getName( Boolean lastNameFirst, Boolean includeLab, Boolean includeActiveFlag) {

    String name = "";

    if ( lastNameFirst ) {
      if (lastName != null && !lastName.equals("")) {
        name = lastName;
      }
      if (firstName != null && !firstName.equals("")) {
        if (name.length() > 0) {
          name += ", ";
        }
        name += firstName;
      }
    } else {
      if (firstName != null && !firstName.equals("")) {
        name = firstName;
      }
      if (lastName != null && !lastName.equals("")) {
        name += " " + lastName;
      }
    }
    if ( includeLab ) {
      if (name.length() > 0) {
        name += " Lab";
      }
    }
    if ( includeActiveFlag ) {
      if (this.getIsActive() != null && this.getIsActive().equalsIgnoreCase("N")) {
        name += " (inactive)";
      }
    }

    return name;
  }


  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getAppUsers");
  }


  public Set getBillingAccounts() {
    return billingAccounts;
  }


  public void setBillingAccounts(Set billingAccounts) {
    this.billingAccounts = billingAccounts;
  }


  public Set getMembers() {
    return members;
  }


  public void setMembers(Set members) {
    this.members = members;
  }


  public Set getCollaborators() {
    return collaborators;
  }


  public void setCollaborators(Set collaborators) {
    this.collaborators = collaborators;
  }


  public Set getManagers() {
    return managers;
  }


  public void setManagers(Set managers) {
    this.managers = managers;
  }

  public String getCanSubmitRequests() {
    if (this.canSubmitRequests) {
      return "Y";
    } else {
      return "N";
    }
  }

  public String getCanGuestSubmit() {
    if (this.canGuestSubmit) {
      return "Y";
    } else {
      return "N";
    }
  }

  public void canSubmitRequests(boolean canDo) {
    canSubmitRequests = canDo;
  }

  public void canGuestSubmit(boolean canDo) {
    canGuestSubmit = canDo;
  }

  public String getCanManage() {
    if (this.canManage) {
      return "Y";
    } else {
      return "N";
    }
  }
  public void canManage(boolean canDo) {
    canManage = canDo;
  }


  public String getIsMyLab() {
    if (isMyLab) {
      return "Y";
    } else {
      return "N";
    }
  }


  public void isMyLab(boolean isMyLab) {
    this.isMyLab = isMyLab;
  }



  public String getIsCcsgMember() {
    return isCcsgMember;
  }


  public void setIsCcsgMember(String isCcsgMember) {
    this.isCcsgMember = isCcsgMember;
  }


  public String getFirstName() {
    return firstName;
  }


  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }


  public String getLastName() {
    return lastName;
  }


  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFormattedLabName(boolean firstLast) {
    if(firstLast) {
      return formatLabNameFirstLast(getFirstName(), getLastName());
    } else {
      return formatLabName(getLastName(), getFirstName());

    }
  }

  public static String formatLabName(String lastName, String firstName, boolean includeLab) {

    String labName = "";
    if (lastName != null && !lastName.equals("")) {
      labName = lastName;
    }
    if (firstName != null && !firstName.equals("")) {
      if (labName.length() > 0) {
        labName += ", ";
      }
      labName += firstName;
    }
    if (labName.length() > 0 && includeLab) {
      labName += " Lab";
    }
    return labName;
  }

  public static String formatLabName(String lastName, String firstName ) {

    return formatLabName(lastName, firstName, true);
  }

  public static String formatLabNameFirstLast(String firstName, String lastName, boolean includeLab) {

    String labName = "";
    if (firstName != null && !firstName.equals("")) {
      labName = firstName;
    }

    if (lastName != null && !lastName.equals("")) {
      labName += " " + lastName;
    }

    if (labName.length() > 0 && includeLab) {
      labName += " Lab";
    }
    return labName;
  }

  public static String formatLabNameFirstLast(String firstName, String lastName) {

    return formatLabNameFirstLast(firstName, lastName, true);
  }


  public Set getProjects() {
    return projects;
  }


  public void setProjects(Set projects) {
    this.projects = projects;
  }


  public String getIsActive() {
    return isActive;
  }


  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  public String getIsExternalPricing() {
    return isExternalPricing;
  }


  public void setIsExternalPricing(String isExternalPricing) {
    this.isExternalPricing = isExternalPricing;
  }


  public String getIsExternalPricingCommercial() {
    return isExternalPricingCommercial;
  }


  public void setIsExternalPricingCommercial(String isExternalPricingCommercial) {
    this.isExternalPricingCommercial = isExternalPricingCommercial;
  }

  public Set getInstitutions() {
    return institutions;
  }

  public void setInstitutions(Set institutions) {
    this.institutions = institutions;
  }

  public Set getCoreFacilities() {
    return coreFacilities;
  }

  public void setCoreFacilities(Set coreFacilities) {
    this.coreFacilities = coreFacilities;
  }

  public Boolean isExternalLab() {
    return (getIsExternalPricing() != null && getIsExternalPricing().equals("Y"))
        || (getIsExternalPricingCommercial() != null && getIsExternalPricingCommercial().equals("Y"));
  }

  public List getApprovedBillingAccounts() {
    ArrayList approvedBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if (ba.getIsApproved() != null && ba.getIsApproved().equalsIgnoreCase("Y")) {
        approvedBillingAccounts.add(ba);
      }
    }
    return approvedBillingAccounts;
  }
  public List getInternalBillingAccounts() {
    ArrayList internalBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if ((ba.getIsPO() == null || ba.getIsPO().equals("N")) &&
          (ba.getIsCreditCard() == null || ba.getIsCreditCard().equals("N"))) {
        internalBillingAccounts.add(ba);
      }
    }
    return internalBillingAccounts;
  }

  public List getPOBillingAccounts() {
    ArrayList poBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if (ba.getIsPO() != null &&
          ba.getIsPO().equals("Y") ) {
        poBillingAccounts.add(ba);
      }
    }
    return poBillingAccounts;
  }
  public List getCreditCardBillingAccounts() {
    ArrayList creditCardBillingAccounts = new ArrayList();
    for(Iterator i = getBillingAccounts().iterator(); i.hasNext();) {
      BillingAccount ba = (BillingAccount)i.next();
      if (ba.getIsCreditCard() != null &&
          ba.getIsCreditCard().equals("Y") ) {
        creditCardBillingAccounts.add(ba);
      }
    }
    return creditCardBillingAccounts;
  }
  public String getExcludeUsage() {
    return excludeUsage;
  }

  public void setExcludeUsage(String excludeUsage) {
    this.excludeUsage = excludeUsage;
  }

  // Includes PI email if requested.
  public String getBillingNotificationEmail() {
    return formatBillingNotificationEmail(getContactEmail(), getBillingContactEmail());
  }

  public String getBillingContactPhone() {
    return billingContactPhone;
  }

  public void setBillingContactPhone(String billingContactPhone) {
    this.billingContactPhone = billingContactPhone;
  }

  // Always inclused PI
  public String getWorkAuthSubmitEmail() {
    return formatBillingNotificationEmail(getContactEmail(), getBillingContactEmail());
  }

  public static String formatBillingNotificationEmail(String contactEmail, String billingContactEmail) {
    String email = "";
    if (billingContactEmail != null) {
      email = billingContactEmail;
    }
    String piEmail = "";
    if (contactEmail != null) {
      piEmail = contactEmail;
    }
    if (piEmail.length() > 0) {
      if (email.length() > 0) {
        email += ",";
      }
      email += piEmail;
    }

    return email;
  }

  public Boolean validateVisibilityInLab(VisibilityInterface object) {
    Boolean valid = true;
    if (object != null && object.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
      if (object.getIdInstitution() == null) {
        valid = false;
        Integer inst = getDefaultIdInstitutionForLab();
        if (inst != null) {
          valid = true;
          object.setIdInstitution(inst);
        }
      }
    }

    return valid;
  }

  public Integer getDefaultIdInstitutionForLab() {
    return getDefaultIdInstitutionForLab(getInstitutions());
  }

  public Integer getDefaultIdInstitutionForLab(Collection<Institution> institutions) {
    Integer defaultInst = null;
    Integer onlyInst = null;
    Integer numActiveInstitutions = 0;
    for(Institution inst : institutions) {
      if (inst.getIsActive() != null && inst.getIsActive().equals("Y")) {
        numActiveInstitutions++;
        if (numActiveInstitutions.equals(1)) {
          onlyInst = inst.getIdInstitution();
        } else {
          onlyInst = null;
        }
        if (inst.getIsDefault() != null && inst.getIsDefault().equals("Y")) {
          defaultInst = inst.getIdInstitution();
          break;
        }
      }
    }

    if (defaultInst == null && onlyInst != null) {
      defaultInst = onlyInst;
    }

    return defaultInst;
  }

  public static boolean hasDataTracks(Session sess,Integer idLab){
      Long count = null;
      Long zero = new Long(0);
      boolean dataTracks = false;

      String queryStr = "SELECT COUNT(idLab) FROM DataTrack as d WHERE d.idLab = :idLab";
      Query query = sess.createQuery(queryStr)
      .setParameter("idLab",idLab);
      List numOfDataTracks  =  query.list();

      count = (Long)numOfDataTracks.get(0);
      if(count.compareTo(zero) > 0){
              dataTracks = true;
      }

    return dataTracks;
  }

  public static boolean hasExperiments(Session sess, Integer idLab){
      Long count = null;
      Long zero = new Long(0);
      boolean experiments = false;

      String queryStr = "SELECT COUNT(idLab) FROM Request as r WHERE r.idLab = :idLab";
      Query query = sess.createQuery(queryStr)
              .setParameter("idLab",idLab);
      List numOfDataTracks  =  query.list();

      count = (Long)numOfDataTracks.get(0);
      if(count.compareTo(zero) > 0){
          experiments = true;
      }

      return experiments;
  }

  public static boolean hasAnalysis(Session sess, Integer idLab){


      String queryStr ="SELECT ba.expirationDate FROM BillingAccount as ba WHERE DATE_ADD(CURDATE(),INTERVAL 17 DAY) = ba.expirationDate";



              /* "SELECT cf.facilityName, l.contactEmail, l.department,l.billingContactEmail" +
              " FROM Lab as l" +
              " JOIN l.coreFacilities as cf" +
              " WHERE l.idLab = 1328";*/
      Query query = sess.createQuery(queryStr);
      List something = query.list();
      return true;

      /*Long count = null;
      Long zero = new Long(0);
      boolean analyses = false;

      String queryStr = "SELECT COUNT(idLab) FROM Analysis as a WHERE a.idLab = :idLab";
      Query query = sess.createQuery(queryStr)
              .setParameter("idLab",idLab);
      List numOfDataTracks  =  query.list();

      count = (Long)numOfDataTracks.get(0);
      if(count.compareTo(zero) > 0){
          analyses = true;
      }

      return analyses;*/
  }

  public static Set<AppUser> getHistoricalOwnersAndSubmitters(Session sess, Integer idLab) {
    Set<AppUser> combined = new HashSet<>();
    combined.addAll(getExperimentOwners(sess, idLab));
    combined.addAll(getExperimentSubmitters(sess, idLab));
    return combined;
  }

  public static Set<AppUser> getExperimentOwners(Session sess, Integer idLab) {
    String queryStr = "SELECT DISTINCT reqOwner FROM Request AS req LEFT JOIN req.appUser as reqOwner WHERE req.idLab = :idLab";
    Query query = sess.createQuery(queryStr)
            .setParameter("idLab",idLab);
    return new HashSet(query.list());
  }

  public static Set<AppUser> getExperimentSubmitters(Session sess, Integer idLab) {
    String queryStr = "SELECT DISTINCT reqSubmitter FROM Request AS req LEFT JOIN req.submitter as reqSubmitter WHERE req.idLab = :idLab";
    Query query = sess.createQuery(queryStr)
            .setParameter("idLab",idLab);
    return new HashSet(query.list());
  }

    public static boolean hasTopics(Session sess, Integer idLab) {
        Long count = null;
        Long zero = new Long(0);
        boolean topics = false;

        String queryStr = "SELECT COUNT(idLab) FROM Topic as t WHERE t.idLab = :idLab";
        Query query = sess.createQuery(queryStr)
                .setParameter("idLab",idLab);
        List numOfTopics  =  query.list();

        count = (Long)numOfTopics.get(0);
        if(count.compareTo(zero) > 0){
            topics = true;
        }

        return topics;

    }

  @Override
  public int compareTo(Object o) {
    if (o instanceof Lab) {
      Lab ol = (Lab)o;
      if (ol.getIdLab() == null && this.getIdLab() == null) {
        return 0;
      } else if (ol.getIdLab() == null) {
        return 1;
      } else if (this.getIdLab() == null) {
        return -1;
      } else {
        return this.getIdLab().compareTo(ol.getIdLab());
      }
    } else {
      return 1;
    }
  }


}
