package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

import java.util.Date;


public class InstrumentRun extends HibernateDetailObject {
  
  private Integer  idInstrumentRun;
  private Date     runDate;
  private String   status;
  
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

  public String getStatus()
  {
    return status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }
  
    
}