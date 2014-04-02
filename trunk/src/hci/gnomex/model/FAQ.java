package hci.gnomex.model;

import hci.hibernate3utils.HibernateDetailObject;

public class FAQ extends HibernateDetailObject {
  
  private static final long serialVersionUID = 42L;
  
  private Integer			idFAQ;
  private String			url;
  private String 			title;
  private Integer			views;
  
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
	public Integer getViews() {
		return views;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
}