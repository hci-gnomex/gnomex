package hci.gnomex.model;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import hci.hibernate3utils.HibernateDetailObject;


public class Plate extends HibernateDetailObject {
  
  private Integer        idPlate;
  private Integer        idInstrumentRun;
  private Set            plateWells = new TreeSet();
  private Integer        quadrant;
  private Date           createDate;
  private String         comments;
  private String         label;
  private String         codeReactionType;
  private String         creator;
  private String         codeSealType;
  private String         codePlateType;
  private InstrumentRun  instrumentRun;
  
  
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

  public Integer getQuadrant()
  {
    return quadrant;
  }

  public void setQuadrant(Integer quadrant)
  {
    this.quadrant = quadrant;
  }

  public Date getCreateDate()
  {
    return createDate;
  }

  public void setCreateDate(Date createDate)
  {
    this.createDate = createDate;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments(String comments)
  {
    this.comments = comments;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public String getCodeReactionType()
  {
    return codeReactionType;
  }

  public void setCodeReactionType(String codeReactionType)
  {
    this.codeReactionType = codeReactionType;
  }

  public String getCreator()
  {
    return creator;
  }

  public void setCreator(String creator)
  {
    this.creator = creator;
  }

  public void setCodeSealType(String sealType)
  {
    this.codeSealType = sealType;
  }

  public String getCodeSealType()
  {
    return codeSealType;
  }

  public void setCodePlateType(String plateType)
  {
    this.codePlateType = plateType;
  }

  public String getCodePlateType()
  {
    return codePlateType;
  }

  public InstrumentRun getInstrumentRun() {
    return instrumentRun;
  }

  public void setInstrumentRun(InstrumentRun instrumentRun) {
    this.instrumentRun = instrumentRun;
  }

  
    
}