package hci.gnomex.model;

import hci.hibernate5utils.HibernateDetailObject;

public class FAQ extends HibernateDetailObject {
  
  private static final long serialVersionUID = 42L;
  
  private Integer			idFAQ;
  private String			url;
  private String 			title;
  private Integer     idCoreFacility;
  
	public Integer getIdFAQ() {
		return idFAQ;
	}
	public void setIdFAQ(Integer idFAQ) {
		this.idFAQ = idFAQ;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
  public Integer getIdCoreFacility() {
    return idCoreFacility;
  }
  public void setIdCoreFacility(Integer idCoreFacility) {
    this.idCoreFacility = idCoreFacility;
  }
}