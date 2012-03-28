package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;


public class PlateWell extends HibernateDetailObject {
  
  private Integer  idPlateWell;
  private String   row;
  private Integer  col;
  private Integer  index;
  private Integer  idPlate;
  private Plate    plate;
  private Integer  idSample;
  private Sample   sample;
  private Integer  idRequest;
  
  public Integer getIdPlateWell() {
    return idPlateWell;
  }
  
  public void setIdPlateWell(Integer idPlateWell) {
    this.idPlateWell = idPlateWell;
  }

  public String getRow()
  {
    return row;
  }

  public void setRow(String row)
  {
    this.row = row;
  }

  public Integer getCol()
  {
    return col;
  }

  public void setCol(Integer col)
  {
    this.col = col;
  }

  public Integer getIndex()
  {
    return index;
  }

  public void setIndex(Integer index)
  {
    this.index = index;
  }

  public Integer getIdPlate()
  {
    return idPlate;
  }

  public void setIdPlate(Integer idPlate)
  {
    this.idPlate = idPlate;
  }

  public Plate getPlate()
  {
    return plate;
  }

  public void setPlate(Plate plate)
  {
    this.plate = plate;
  }

  public Integer getIdSample()
  {
    return idSample;
  }

  public void setIdSample(Integer idSample)
  {
    this.idSample = idSample;
  }

  public Sample getSample()
  {
    return sample;
  }

  public void setSample(Sample sample)
  {
    this.sample = sample;
  }
  
  public String getSampleName()
  {
    if (this.sample!=null) {
      return this.sample.getName();
    }
    return "";
  }
  
  public Integer getIdRequest()
  {
    return idRequest;
  }

  public void setIdRequest(Integer idRequest)
  {
    this.idRequest = idRequest;
  }
  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getPlate");
  }
  
    
}