package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class Notification extends HibernateDetailObject {
  
  private static final long serialVersionUID = 42L;
  
  private Integer			idNotification;
  private String			sourceType;
  private String			message;
  private java.util.Date	date;
  private Integer			idUserTarget;
  private Integer			idLabTarget;
  private Integer			userTarget;
  private Integer			labTarget;
  
  public Integer getIdNotification(){
	 return idNotification;
  }
  
  public void setIdNotification(Integer idNotification){
	  this.idNotification = idNotification;
  }
  
  public String getSourceType(){
	  return sourceType;
  }
  
  public void setSourceType(String sourceType){
	  this.sourceType = sourceType;
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
  
  public Integer getIdUserTarget(){
	  return idUserTarget;
  }
  
  public void setIdUserTarget(Integer idUserTarget){
	  this.idUserTarget = idUserTarget;
  }

  public Integer getUserTarget(){
	  return userTarget;
  }
  
  public void setUserTarget(Integer userTarget){
	  this.userTarget = userTarget;
  }
  
  public Integer getIdLabTarget(){
	  return idLabTarget;
  }
  
  public void setIdLabTarget(Integer idLabTarget){
	  this.idLabTarget = idLabTarget;
  }
  
  public Integer getLabTarget(){
	  return labTarget;
  }
  
  public void setLabTarget(Integer labTarget){
	  this.labTarget = labTarget;
  }
    
}