package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class NewsItem extends HibernateDetailObject {
  
  private static final long serialVersionUID = 42L;
  
  private Integer			idNewsItem;
  private String			message;
  private java.util.Date	date;
  private Integer			idSubmitter;
  private Integer			idCoreSender;
  private Integer			idCoreTarget;

  private Integer			submitter;
  private Integer			coreSender;
  private Integer			coreTarget;  
  
  public Integer getIdNewsItem(){
	 return idNewsItem;
  }
  
  public void setIdNewsItem(Integer idNewsItem){
	  this.idNewsItem = idNewsItem;
  }
  
    
  public String getMessage(){
	  return message;
  }
  
  public void setMessage(String message){
	  this.message = message;
  }
  
  public java.util.Date getDate(){
	  return date;
  }
  
  public void setDate(java.util.Date date){
	  this.date = date;
  }
  
  public Integer getIdSubmitter(){
	  return idSubmitter;
  }
  
  public void setIdSubmitter(Integer idSubmitter){
	  this.idSubmitter = idSubmitter;
  }
  
  public Integer getIdCoreSender(){
	  return idCoreSender;
  }
  
  public void setIdCoreSender(Integer idCoreSender){
	  this.idCoreSender = idCoreSender;
  }

  public Integer getIdCoreTarget(){
	  return idCoreTarget;
  }
  
  public void setIdCoreTarget(Integer coreTarget){
	  this.idCoreTarget = coreTarget;
  }
  
  public Integer getSubmitter(){
	  return submitter;
  }
  
  public void setSubmitter(Integer submitter){
	  this.submitter = submitter;
  }
  
  public Integer getCoreTarget(){
	  return coreTarget;
  }
  
  public void setCoreTarget(Integer idCoreTarget){
	  this.idCoreTarget = idCoreTarget;
  }
  
  public Integer getCoreSender(){
	  return coreSender;
  }
  
  public void setCoreSender(Integer coreSender){
	  this.coreSender = coreTarget;
  }
  
}