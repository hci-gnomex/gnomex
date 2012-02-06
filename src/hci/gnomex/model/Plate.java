package hci.gnomex.model;

import java.util.Set;
import java.util.TreeSet;

import hci.hibernate3utils.HibernateDetailObject;


public class Plate extends HibernateDetailObject {
  
  private Integer  idPlate;
  private Integer  idInstrumentRun;
  private Set      plateWells = new TreeSet();
  
  
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

  
    
}