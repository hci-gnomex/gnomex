package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrack;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;



public class CheckDataTrackPermission extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(CheckDataTrackPermission.class);
  
  private Integer idDataTrack = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    String dataTrackNumber = "";
    if (request.getParameter("dataTrackNumber") != null && !request.getParameter("dataTrackNumber").equals("")) {
      dataTrackNumber = request.getParameter("dataTrackNumber");   
    } else {
      this.addInvalidField("dataTrackNumber", "dataTrackNumber is required");
    }
    if(dataTrackNumber.length() > 0) {
      dataTrackNumber = dataTrackNumber.substring(dataTrackNumber.indexOf("DT")+2);
      idDataTrack = new Integer(dataTrackNumber);
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      if(idDataTrack != null) {
        DataTrack dataTrack = (DataTrack) (sess.load(DataTrack.class, idDataTrack));
        if (!this.getSecAdvisor().canRead(dataTrack)) {
          this.addInvalidField("perm", "Insufficient permission to access this data track");
        }          
      } else {
        this.addInvalidField("dataTrackNumber", "dataTrackNumber is either invalid or not provided");
      }
      if (isValid()) {
        this.xmlResult = "<SUCCESS/>";
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e) {
      LOG.error("An exception has occurred in CheckDataTrackPermission ", e);

      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        LOG.error("An exception has occurred in CheckDataTrackPermission ", e);
      }
    }
    
    return this;
  }
}