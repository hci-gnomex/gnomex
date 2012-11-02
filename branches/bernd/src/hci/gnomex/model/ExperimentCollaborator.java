package hci.gnomex.model;



import java.math.BigDecimal;
import java.sql.Date;

import hci.hibernate3utils.HibernateDetailObject;



public class ExperimentCollaborator extends HibernateDetailObject {
  
  private Integer        idAppUser;
  private Integer        idRequest;
  private String         canUploadData;
  
  public Integer getIdAppUser() {
    return idAppUser;
  }
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }
  public Integer getIdRequest() {
    return idRequest;
  }
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }
  public String getCanUploadData() {
    return canUploadData;
  }
  public void setCanUploadData(String canUploadData) {
    this.canUploadData = canUploadData;
  }
  
  /* Override equals() to handle dual key */
  public boolean equals(Object obj) {
    boolean tmpIsEqual = false;
    if (this.getIdAppUser() == null || this.getIdRequest() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof ExperimentCollaborator) {
        if (this.getIdAppUser().equals(((ExperimentCollaborator) obj).getIdAppUser())
            && this.getIdRequest().equals(((ExperimentCollaborator) obj).getIdRequest())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getIdAppUser() == null || this.getIdRequest() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getIdAppUser().hashCode() ^ this.getIdRequest().hashCode();
    }
  }

  

}