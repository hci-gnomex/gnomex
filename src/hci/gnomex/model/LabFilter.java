package hci.gnomex.model;


import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Iterator;
import java.util.Set;

public class LabFilter extends DetailObject {
  
  
  // Criteria
  private String          firstName;
  private String          lastName;
  private String          userLastName;
  private String          userFirstName;
  private Integer         idLab;
  private Integer         idInstitution;
  private Integer         idCoreFacility;
  private String          isExternal;
  private SecurityAdvisor secAdvisor;
  private boolean         isUnbounded = false;

  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT distinct lab");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public StringBuffer getQueryWithInstitutionAndCore(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT distinct lab, inst, coreFacility");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Lab as lab ");
  
    queryBuf.append(" LEFT JOIN lab.institutions as inst ");

    if (hasUserCriteria()) {
      queryBuf.append(" JOIN lab.appUsers as user ");
    }
    
    queryBuf.append(" LEFT JOIN lab.coreFacilities as coreFacility ");
    
    
    
    addLabCriteria();
    addInstitutionCriteria();
    addCoreFacilityCriteria();
    addUserCriteria();
    if (!isUnbounded) {
      addSecurityCriteria();      
    } else {
      addUnboundedSecurityCriteria();
    }
    
    queryBuf.append(" order by lab.lastName, lab.firstName, lab.idLab");
  }
  
  
  private boolean hasUserCriteria() {
    if ((userLastName != null && !userLastName.equals("")) ||
        (userFirstName != null && !userFirstName.equals(""))) {
      return true;
    } else {
      return false;
    }
  }
  
  private boolean hasInstitutionCriteria() {
    if (idInstitution != null) {
      return true;
    } else {
      return false;
    }
  }
  

  private void addLabCriteria() {
    // Search by lab last name 
    if (lastName != null && !lastName.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" lab.lastName like '%");
      queryBuf.append(lastName);
      queryBuf.append("%'");
    } 

    // Search by lab first name 
    if (firstName != null && !firstName.equals("")){
      this.addWhereOrAnd();
      queryBuf.append(" lab.firstName like '%");
      queryBuf.append(firstName);
      queryBuf.append("%'");
    } 
    //  Search by idLab
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" lab.idLab =");
      queryBuf.append(idLab);
    } 
    // Search by pricing (external academic or external commercial)
    if (this.isExternal != null && this.isExternal.equals("Y")) {
      this.addWhereOrAnd();
      queryBuf.append(" (lab.isExternalPricing = 'Y' OR lab.isExternalPricingCommercial = 'Y') ");
    }
  }
  
  private void addInstitutionCriteria() {
    //  Search by idInstitution
    if (idInstitution != null){
      this.addWhereOrAnd();
      queryBuf.append(" inst.idInstitution =");
      queryBuf.append(idInstitution);
    } 
    
  }
  
  private void addCoreFacilityCriteria() {
    //  Search by idCoreFacility
    if (idCoreFacility != null && !idCoreFacility.equals( "" )){
      this.addWhereOrAnd();
      queryBuf.append(" coreFacility.idCoreFacility =");
      queryBuf.append(idCoreFacility);
    } 
    
  }
  
  private void addUserCriteria() {
    if (userLastName != null && !userLastName.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" user.lastName like '%");
      queryBuf.append(userLastName);
      queryBuf.append("%'");
    }
    if (userFirstName != null && !userFirstName.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" user.firstName like '%");
      queryBuf.append(userFirstName);
      queryBuf.append("%'");
    }
  }
  
  private void addUnboundedSecurityCriteria() {
    if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      // No criteria needed if this is a super user
    } else if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !secAdvisor.hasPermission(SecurityAdvisor.CAN_SUBMIT_FOR_OTHER_CORES)) { 
      // No criteria.  Admins can see labs from all core facilities.
    } else {      
      if (secAdvisor.getCoreFacilitiesForMyLab().isEmpty()) {
        // User is not a member of any lab, let's just show all labs.
        // The only place this lab list is used is for searching and
        // submitting work auth forms.

      } else {
        // Filter to show only labs associated with core facilities this user is 
        // associated with
        this.addWhereOrAnd();
        Set coreFacilities = secAdvisor.getCoreFacilitiesForMyLab();
        coreFacilities.addAll(secAdvisor.getCoreFacilitiesICanSubmitTo());
        queryBuf.append(" coreFacility.idCoreFacility in ( ");
        for(Iterator i = coreFacilities.iterator(); i.hasNext();) {
          CoreFacility cf = (CoreFacility)i.next();
          queryBuf.append(cf.getIdCoreFacility());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }
        queryBuf.append(" )");        
        
      }
    }
  }
  
  
  private void addSecurityCriteria() {
    if (secAdvisor.hasPermission(secAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES)) {
      // No criteria needed if this is a super users
      
    } else if (secAdvisor.hasPermission(secAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      
      // Filter to show only labs associated with core facilities this admin manages
      if (secAdvisor.getCoreFacilitiesIManage().isEmpty()) {
        throw new RuntimeException("Admin is not assigned to any core facilities.  Cannot apply appropriate filter to lab query.");
      }     
      
      
    } else if (secAdvisor.hasPermission(secAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      if (this.secAdvisor.getAllMyGroups().size() > 0) {
        // Limit to requests for labs that the user is a member of
        this.addWhereOrAnd();
        queryBuf.append(" lab.idLab in ( ");
        for(Iterator i = secAdvisor.getAllMyGroups().iterator(); i.hasNext();) {
          Lab theLab = (Lab)i.next();
          queryBuf.append(theLab.getIdLab());
          if (i.hasNext()) {
            queryBuf.append(", ");
          }
        }      
        queryBuf.append(" )");        
      } else {
        // If user doesn't belong to any labs, just apply criteria that will 
        // ensure that lab list is empty
        this.addWhereOrAnd();
        queryBuf.append(" lab.idLab = -1 ");
      }
      
    } 
    
  }
  
  
  protected boolean addWhereOrAnd() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

  

  public Integer getIdLab() {
    return idLab;
  }

  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public boolean isUnbounded() {
    return isUnbounded;
  }

  
  public void setUnbounded(boolean isUnbounded) {
    this.isUnbounded = isUnbounded;
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

  public Integer getIdInstitution() {
    return idInstitution;
  }

  public void setIdInstitution(Integer idInstitution) {
    this.idInstitution = idInstitution;
  }

  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility) {
    this.idCoreFacility = idCoreFacility;
  }

  public String getIsExternal() {
    return isExternal;
  }

  public void setIsExternal(String isExternal) {
    this.isExternal = isExternal;
  }
 

  
}
