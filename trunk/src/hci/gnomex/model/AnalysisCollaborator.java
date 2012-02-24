package hci.gnomex.model;



import hci.hibernate3utils.HibernateDetailObject;



public class AnalysisCollaborator extends HibernateDetailObject {
  
  private Integer        idAppUser;
  private Integer        idAnalysis;
  private String         canUploadData;
  
  public Integer getIdAppUser() {
    return idAppUser;
  }
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }
  public Integer getIdAnalysis() {
    return idAnalysis;
  }
  public void setIdAnalysis(Integer idAnalysis) {
    this.idAnalysis = idAnalysis;
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
    if (this.getIdAppUser() == null || this.getIdAnalysis() == null) {
      tmpIsEqual = super.equals(obj);
    }
    else {
      if (this == obj) {
        tmpIsEqual = true;
      }
      else if (obj instanceof AnalysisCollaborator) {
        if (this.getIdAppUser().equals(((AnalysisCollaborator) obj).getIdAppUser())
            && this.getIdAnalysis().equals(((AnalysisCollaborator) obj).getIdAnalysis())) {
          tmpIsEqual = true;
        }
      }
    }
    return tmpIsEqual;
  }

  /* Override hashCode() to handle dual key */
  public int hashCode() {
    if (this.getIdAppUser() == null || this.getIdAnalysis() == null) {
      return super.hashCode();
    }
    else {
      // Use exclusive or between hashCodes to get new result
      return this.getIdAppUser().hashCode() ^ this.getIdAnalysis().hashCode();
    }
  }


}