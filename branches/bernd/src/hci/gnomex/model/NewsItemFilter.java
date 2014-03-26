package hci.gnomex.model;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hci.gnomex.security.SecurityAdvisor;
import hci.framework.model.DetailObject;

public class NewsItemFilter extends DetailObject {
  
  // Criteria / Argumented mx:request variables
	  private Integer			idNewsItem;
	  private String			message;
	  private String 			title;
	  private java.util.Date	date;
	  private Integer			idSubmitter;
	  private Integer			idCoreFacility;
	
  private StringBuffer          queryBuf;
  private boolean               addWhere = true;
  private SecurityAdvisor       secAdvisor;
  
  public StringBuffer getQuery(SecurityAdvisor secAdvisor) {
    this.secAdvisor = secAdvisor;
    
    // Get Base query. 
    return getQuery();

  }

  private StringBuffer getQuery() {
    queryBuf = new StringBuffer();
    addWhere = true;
      
    addBaseColumns(queryBuf);
    addBaseQueryBody(queryBuf);
    addRequestCriteria();
  
    return queryBuf;
    
  }

  public void addBaseColumns(StringBuffer queryBuf) {
	  queryBuf.append("SELECT distinct ");
	  queryBuf.append(" n.idNewsItem, n.title, n.message, n.date, ");
	  queryBuf.append(" n.idSubmitter, n.idCoreFacility ");
   } 

  private void addBaseQueryBody(StringBuffer queryBuf) {
	  queryBuf.append(" FROM         NewsItem n ");
  }
  
  
  private void addRequestCriteria() {
	    // Search by workflowStatus 
	    if (idNewsItem != null && idNewsItem != 0){
	      this.addWhereOrAnd();
	      queryBuf.append(" n.idNewsItem = '");
	      queryBuf.append(idNewsItem);
	      queryBuf.append("'");
	    } 
	    // Search by title 
	    if (title != null){
	      this.addWhereOrAnd();
	      queryBuf.append(" n.title = '");
	      queryBuf.append(title);
	      queryBuf.append("'");
	    } 
	    
	    // Search by date
	    if(date != null){
	      this.addWhereOrAnd();
	      queryBuf.append(" n.date = '");
	      queryBuf.append(date);
	      queryBuf.append("'");
	    }
	    //  Search by idSubmitter
	    if(idSubmitter != null && idSubmitter != 0){
	    	this.addWhereOrAnd();
	    	queryBuf.append(" n.idSubmitter = '");
	    	queryBuf.append(idSubmitter);
	    	queryBuf.append("'");
	    }
	    // Search by idCoreTarget
	    if(idCoreFacility != null && idCoreFacility != 0){
	    	this.addWhereOrAnd();
	    	queryBuf.append(" n.idCoreTarget = '");
	    	queryBuf.append(idCoreFacility);
	    	queryBuf.append("'");
	    }
	    
  }

  protected boolean addWhereOrAnd() {
    if (addWhere) {
      queryBuf.append(" WHERE ");
      addWhere = false;
    } else {
      queryBuf.append(" AND ");
    }
    return addWhere;
  }

	public Integer getIdNewsItem() {
		return idNewsItem;
	}
	
	public void setIdNewsItem(Integer idNewsItem) {
		this.idNewsItem = idNewsItem;
	}
	
	public java.util.Date getDate() {
		return date;
	}
	
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Integer getIdCoreTarget() {
		return idCoreFacility;
	}
	
	public void setIdCoreTarget(Integer idCoreFacility) {
		this.idCoreFacility = idCoreFacility;
	}
	
	public Integer getIdSubmitter() {
		return idSubmitter;
	}
	
	public void setIdSubmitter(Integer idSubmitter) {
		this.idSubmitter = idSubmitter;
	}
 }
