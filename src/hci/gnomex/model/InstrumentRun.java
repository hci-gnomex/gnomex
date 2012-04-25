package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.Date;


public class InstrumentRun extends HibernateDetailObject {
  
  private Integer        idInstrumentRun;
  private Date           runDate;
  private Date           createDate;
  private String         codeInstrumentRunStatus;
  private String         comments;
  private String         label;
  private String         codeReactionType;
  private String         creator;
  private String         codeSealType;
  
  
  public Integer getIdInstrumentRun()
  {
    return idInstrumentRun;
  }

  public void setIdInstrumentRun(Integer idInstrumentRun)
  {
    this.idInstrumentRun = idInstrumentRun;
  }

  public Date getRunDate()
  {
    return runDate;
  }

  public void setRunDate(Date runDate)
  {
    this.runDate = runDate;
  }

  public Date getCreateDate()
  {
    return createDate;
  }

  public void setCreateDate(Date createDate)
  {
    this.createDate = createDate;
  }

  public String getCodeInstrumentRunStatus()
  {
    return codeInstrumentRunStatus;
  }

  public void setCodeInstrumentRunStatus(String codeInstrumentRunStatus)
  {
    this.codeInstrumentRunStatus = codeInstrumentRunStatus;
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
  
    
}