package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class NewsItem extends HibernateDetailObject {
  
  private static final long serialVersionUID = 42L;
  
  private Integer			idNewsItem;
  private String			message;
  private String 			title;
  private java.util.Date	date;
  private Integer			idSubmitter;
  private Integer			idCoreSender;
  private Integer			idCoreTarget;
  private Integer			appUser;
  private Integer			coreFacilitySender;
  private Integer			coreFacilityTarget;  
  
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

  public String getTitle() {
	  return title;	
  }

  public void setTitle(String title) {
	  this.title = title;
  }
  
  public Integer getAppUser(){
	  return appUser;
  }
  
  public void setAppUser(Integer appUser){
	  this.appUser = appUser;
  }
  
  public Integer getCoreFacilityTarget(){
	  return coreFacilityTarget;
  }
  
  public void setCoreFacilityTarget(Integer coreFacilityTarget){
	  this.coreFacilityTarget = coreFacilityTarget;
  }
  
  public Integer getCoreFacilitySender(){
	  return coreFacilitySender;
  }
  
  public void setCoreFacilitySender(Integer coreFacilitySender){
	  this.coreFacilitySender = coreFacilityTarget;
  }

  
}