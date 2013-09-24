package hci.gnomex.model;

import java.io.Serializable;

import hci.framework.model.DetailObject;


public class GenomeBuildAlias extends DetailObject implements Serializable {

  private Integer idGenomeBuildAlias;
  private String  alias;

  public Integer getIdGenomeBuildAlias() {
    return idGenomeBuildAlias;
  }
  public void setIdGenomeBuildAlias(Integer idGenomeBuildAlias) {
    this.idGenomeBuildAlias = idGenomeBuildAlias;
  }
  public String getAlias() {
    return alias;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }
}
