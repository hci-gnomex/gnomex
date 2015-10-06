package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class NewsItem extends HibernateDetailObject {

  private static final long serialVersionUID = 42L;

  private Integer			idNewsItem;
  private String			message;
  private String 			title;
  private java.util.Date	date;
  private Integer			idSubmitter;
  private Integer			idCoreFacility;

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

  public Integer getIdCoreFacility(){
    return idCoreFacility;
  }

  public void setIdCoreFacility(Integer idCoreFacility){
    this.idCoreFacility = idCoreFacility;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}