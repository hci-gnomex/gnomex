package hci.gnomex.model;

import java.util.Date;

import hci.hibernate3utils.HibernateDetailObject;


public class PlateWell extends HibernateDetailObject {
  
  private Integer  idPlateWell;
  private String   row;
  private Integer  col;
  private Integer  position;
  private Integer  idPlate;
  private Plate    plate;
  private Integer  idSample;
  private Sample   sample;
  private Integer  idRequest;
  private String   codeReactionType;
  private Date     createDate;
  private String   redoFlag = "N";
  private Integer  idAssay;
  private Integer  idPrimer;
  
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



  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
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

  public void setCodeReactionType(String codeReactionType)
  {
    this.codeReactionType = codeReactionType;
  }

  public String getCodeReactionType()
  {
    return codeReactionType;
  }

  public void setCreateDate(Date createDate)
  {
    this.createDate = createDate;
  }

  public Date getCreateDate()
  {
    return createDate;
  }

  public String getRedoFlag() {
    return redoFlag;
  }

  public void setRedoFlag(String redoFlag) {
    this.redoFlag = redoFlag;
  }

  public Integer getIdAssay() {
    return idAssay;
  }

  public void setIdAssay(Integer idAssay) {
    this.idAssay = idAssay;
  }

  public Integer getIdPrimer() {
    return idPrimer;
  }

  public void setIdPrimer(Integer idPrimer) {
    this.idPrimer = idPrimer;
  }
  
    
}