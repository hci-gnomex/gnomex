package hci.gnomex.model;

import hci.framework.model.DetailObject;
import hci.gnomex.security.SecurityAdvisor;

import java.util.Iterator;

public class AppUserFilter extends DetailObject {

  // Criteria
  private String lab;
  private String userLastName;
  private String userFirstName;
  private Integer idLab;
  private Integer idAppUser;
  private Boolean membersOnly = false;

  private SecurityAdvisor secAdvisor;

  private StringBuffer queryBuf;
  private boolean addWhere = true;

  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    addWhere = true;
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();

    queryBuf.append(" SELECT distinct user");

    getQueryBody(queryBuf);

    return queryBuf;

  }

  public void getQueryBody(StringBuffer queryBuf) {

    queryBuf.append(" FROM           AppUser as user ");
    if (hasLabCriteria()) {
      queryBuf.append(" LEFT JOIN           user.labs as lab ");
      queryBuf.append(" LEFT JOIN           user.collaboratingLabs as collabLab ");
      queryBuf.append(" LEFT JOIN           user.managingLabs as managerLab ");
    }

    addUserCriteria();
    addLabCriteria();

    addSecurityCriteria();

    queryBuf.append(" order by user.lastName, user.firstName ");
  }

  private boolean hasLabCriteria() {
    // If we will add security criteria by authorized lab, join to lab
    if (!secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      return true;
    }

    // If we have any lab selection criteria, join to lab
    if ((lab != null && !lab.equals("")) || (idLab != null)) {
      return true;
    } else {
      return false;
    }
  }

  private void addUserCriteria() {
    // Search by user name
    if (userLastName != null && !userLastName.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" user.lastName like '%");
      queryBuf.append(userLastName);
      queryBuf.append("%'");
    }
    if (userFirstName != null && !userFirstName.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" user.FirstName like '%");
      queryBuf.append(userFirstName);
      queryBuf.append("%'");
    }
    // Search by user id
    if (idAppUser != null) {
      this.addWhereOrAnd();
      queryBuf.append(" user.idAppUser = ");
      queryBuf.append(idAppUser);
    }
  }

  private void addLabCriteria() {
    // Search by lab name
    if (lab != null && !lab.equals("")) {
      this.addWhereOrAnd();
      queryBuf.append(" lab.name like '%");
      queryBuf.append(lab);
      queryBuf.append("%'");
    }
    // Search by lab id
    if (idLab != null) {
      this.addWhereOrAnd();
      queryBuf.append(" lab.idLab = ");
      queryBuf.append(idLab);
    }
  }

  private void addSecurityCriteria() {
    if (secAdvisor.hasPermission(SecurityAdvisor.CAN_ADMINISTER_ALL_CORE_FACILITIES) || secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      // No criteria needed for super users or admins
      // Admins are able to see everyone to facilitate adding users to labs
    } else if (secAdvisor.getGroupsIManage().size() > 0 && !membersOnly) {
      // Lab managers must be able to add any user to his/her lab,
      // so only criteria applied is to get active gnomex accounts
      this.addWhereOrAnd();
      queryBuf.append(" user.isActive = 'Y' ");

    } else if (secAdvisor.hasPermission(SecurityAdvisor.CAN_PARTICIPATE_IN_GROUPS)) {
      this.addWhereOrAnd();
      queryBuf.append(" user.isActive = 'Y' ");

      if (this.secAdvisor.getAllMyGroups().size() > 0) {
        // Limit to users who are members of the authorized labs
        this.addWhereOrAnd();

        queryBuf.append(" ( ");

        String myLabsInClause = getMyLabsInClause();
        queryBuf.append(" lab.idLab " + myLabsInClause + " OR ");
        queryBuf.append(" collabLab.idLab " + myLabsInClause + " OR ");
        queryBuf.append(" managerLab.idLab " + myLabsInClause);

        queryBuf.append(")");

      } else {
        // We have a user that is not set up in any labs. Just
        // Limit to this user only.

        this.addWhereOrAnd();
        queryBuf.append(" user.idAppUser = ");
        queryBuf.append(secAdvisor.getIdAppUser());
      }

    } else {
      // Limit to this user only
      this.addWhereOrAnd();
      queryBuf.append(" user.idAppUser = ");
      queryBuf.append(secAdvisor.getIdAppUser());
    }

  }

  private String getMyLabsInClause() {
    String myLabs = "";
    for (Object objLab : secAdvisor.getAllMyGroups()) {
      if (!myLabs.isEmpty()) {
        myLabs += ", ";
      }
      myLabs += ((Lab) objLab).getIdLab();
    }
    return " IN ( " + myLabs + " ) ";
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

  public String getLab() {
    return lab;
  }

  public void setLab(String lab) {
    this.lab = lab;
  }

  public Boolean getMembersOnly() {
    return membersOnly;
  }

  public void setMembersOnly(String membersOnly) {
    if (membersOnly == null || membersOnly.equals("") || membersOnly.equals("N")) {
      this.membersOnly = false;
    } else {
      this.membersOnly = true;
    }
  }

  public Integer getIdLab() {
    return idLab;
  }

  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  public boolean isAddWhere() {
    return addWhere;
  }

  public void setAddWhere(boolean addWhere) {
    this.addWhere = addWhere;
  }

  public Integer getIdAppUser() {
    return idAppUser;
  }

  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  public String getUserFirstName() {
    return userFirstName;
  }

  public void setUserFirstName(String userFirstName) {
    this.userFirstName = userFirstName;
  }

  public String getUserLastName() {
    return userLastName;
  }

  public void setUserLastName(String userLastName) {
    this.userLastName = userLastName;
  }

}
