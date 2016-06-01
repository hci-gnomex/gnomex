package hci.gnomex.model;



import hci.gnomex.utility.GnomexFile;
import hci.hibernate5utils.HibernateDetailObject;

import java.math.BigDecimal;
import java.sql.Date;



public class ExperimentFile extends GnomexFile {

  private Integer        idExperimentFile;
  private Integer        idRequest;
  private Request        request;

  public Integer getIdExperimentFile() {
    return idExperimentFile;
  }
  public void setIdExperimentFile(Integer idExperimentFile) {
    this.idExperimentFile = idExperimentFile;
  }
  public Integer getIdRequest() {
    return idRequest;
  }
  public void setIdRequest(Integer idRequest) {
    this.idRequest = idRequest;
  }
  public Request getRequest() {
    return request;
  }
  public void setRequest(Request request) {
    this.request = request;
  }

  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getRequest");
  }
}