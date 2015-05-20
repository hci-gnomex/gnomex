package hci.gnomex.model;

import java.io.File;
import java.math.BigDecimal;

import org.hibernate.Session;

import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.hibernate3utils.HibernateDetailObject;

public class VisitLog extends HibernateDetailObject {
  
  private Integer			idVisitLog;
  private Integer			idAppUser;
  private String			ipAddress;  
  private java.util.Date	visitDateTime;
  private String			sessionID;
  
	public Integer getIdVisitLog() {
		return idVisitLog;
	}
	public void setIdVisitLog(Integer idVisitLog) {
		this.idVisitLog = idVisitLog;
	}
	public Integer getIdAppUser() {
		return idAppUser;
	}
	public void setIdAppUser(Integer idAppUser) {
		this.idAppUser = idAppUser;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public java.util.Date getVisitDateTime() {
		return visitDateTime;
	}
	public void setVisitDateTime(java.util.Date visitDateTime) {
		this.visitDateTime = visitDateTime;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}

      