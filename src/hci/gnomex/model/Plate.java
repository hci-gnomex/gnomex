package hci.gnomex.model;

import java.util.Set;
import java.util.TreeSet;

import hci.hibernate3utils.HibernateDetailObject;


public class Plate extends HibernateDetailObject {
  
  private Integer  idPlate;
  private Integer  idInstrumentRun;
  private Set      plateWells = new TreeSet();
  private String   status;
  private Integer  quadrant;
  
  
  public Integer getIdPlate()
  {
    return idPlate;
  }

  public void setIdPlate(Integer idPlate)
  {
    this.idPlate = idPlate;
  }

  public Integer getIdInstrumentRun()
  {
    return idInstrumentRun;
  }

  public void setIdInstrumentRun(Integer idInstrumentRun)
  {
    this.idInstrumentRun = idInstrumentRun;
  }

  public Set getPlateWells()
  {
    return plateWells;
  }

  public void setPlateWells(Set plateWells)
  {
    this.plateWells = plateWells;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public Integer getQuadrant()
  {
    return quadrant;
  }

  public void setQuadrant(Integer quadrant)
  {
    this.quadrant = quadrant;
  }

  
    
}