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
  private String			type;
  private Integer			expID;
  
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
  
  public Integer getIdLabTarget(){
	  return idLabTarget;
  }
  
  public void setIdLabTarget(Integer idLabTarget){
	  this.idLabTarget = idLabTarget;
  }

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public Integer getExpID() {
	return expID;
}

public void setExpID(Integer expID) {
	this.expID = expID;
}
   
}