package hci.gnomex.utility;

import hci.gnomex.model.DataTrack;

import java.io.Serializable;


public class QualifiedDataTrack implements Serializable {
  private DataTrack dataTrack;
  private String     typePrefix;
  private String     resourceName;

  public QualifiedDataTrack(DataTrack dataTrack, String typePrefix, String resourceName) {
    super();
    this.dataTrack = dataTrack;
    this.typePrefix = typePrefix;
    this.resourceName = resourceName;
  }
  public DataTrack getDataTrack() {
    return dataTrack;
  }
  public void setDataTrack(DataTrack dataTrack) {
    this.dataTrack = dataTrack;
  }
  public String getTypePrefix() {
    return typePrefix;
  }
  public void setTypePrefix(String typePrefix) {
    this.typePrefix = typePrefix;
  }
  public String getResourceName() {
    return resourceName;
  }
  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }
}
