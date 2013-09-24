package hci.gnomex.model;

import java.io.Serializable;

import hci.framework.model.DetailObject;


public class Segment extends DetailObject implements Serializable {

  private Integer idSegment;
  private Integer length;
  private String  name;
  private Integer idGenomeBuild;
  private Integer sortOrder;

  public Integer getIdSegment() {
    return idSegment;
  }
  public void setIdSegment(Integer idSegment) {
    this.idSegment = idSegment;
  }
  public Integer getLength() {
    return length;
  }
  public void setLength(Integer length) {
    this.length = length;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Integer getSortOrder() {
    return sortOrder;
  }
  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
  public Integer getIdGenomeBuild() {
    return idGenomeBuild;
  }
  public void setIdGenomeBuild(Integer idGenomeBuild) {
    this.idGenomeBuild = idGenomeBuild;
  }

}
