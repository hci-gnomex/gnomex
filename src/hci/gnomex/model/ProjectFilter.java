package hci.gnomex.model;


import java.sql.Date;
import java.util.Iterator;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class ProjectFilter extends DetailObject {
  
  
  // Criteria
  private Integer               idAppUser;
  private Integer               idLab;
  
  
  private StringBuffer          queryBuf;
  private boolean              addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    queryBuf = new StringBuffer();
    
    queryBuf.append(" SELECT DISTINCT proj.id, proj.name, proj.description, proj.idLab ");
    
    getQueryBody(queryBuf);
    
    return queryBuf;
    
  }
  
  public void getQueryBody(StringBuffer queryBuf) {
    
    queryBuf.append(" FROM        Project as proj ");
    queryBuf.append(" LEFT JOIN   proj.requests as req ");
    queryBuf.append(" LEFT JOIN   req.collaborators as collab ");
    
    addRequestCriteria();
    addSecurityCriteria();
    
    queryBuf.append(" order by proj.name");
  
  }
  
  

  private void addRequestCriteria() {

    // Search by lab 
    if (idLab != null){
      this.addWhereOrAnd();
      queryBuf.append(" proj.idLab =");
      queryBuf.append(idLab);
    } 
    // Search by user 
    if (idAppUser != null){
      this.addWhereOrAnd();
      queryBuf.append(" proj.idAppUser = ");
      queryBuf.append(idAppUser);
    } 
    
    
  }
  
  private void addSecurityCriteria() {
    secAdvisor.buildSpannedSecurityCriteria(queryBuf, "proj", "req", "collab", addWhere, "req.codeVisibility", true, "req.idRequest is NULL");
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

  
  public Integer getIdUser() {
    return idAppUser;
  }

  
  
  
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }

  
  public void setIdUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }



  
  
}
