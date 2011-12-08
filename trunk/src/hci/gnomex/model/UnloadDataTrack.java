package hci.gnomex.model;

import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.Date;

public class UnloadDataTrack extends DetailObject implements Serializable {


  private Integer             idUnloadDataTrack;
  private String              typeName;
  private Integer             idAppUser;
  private Integer             idGenomeBuild;

  public Integer getIdUnloadDataTrack() {
    return idUnloadDataTrack;
  }
  public void setIdUnloadDataTrack(Integer idUnloadDataTrack) {
    this.idUnloadDataTrack = idUnloadDataTrack;
  }
  public String getTypeName() {
    return typeName;
  }
  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
  public Integer getIdAppUser() {
    return idAppUser;
  }
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }
  public Integer getIdGenomeBuild() {
    return idGenomeBuild;
  }
  public void setIdGenomeBuild(Integer idGenomeBuild) {
    this.idGenomeBuild = idGenomeBuild;
  }

}