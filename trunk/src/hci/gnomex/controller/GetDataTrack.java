package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrack;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.hibernate.Session;




public class GetDataTrack extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetDataTrack.class);
  
  private Integer idDataTrack;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
      idDataTrack = new Integer(request.getParameter("idDataTrack"));   
    } else {
      this.addInvalidField("idDataTrack", "idDataTrack is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

      // TODO: GENOPUB Need to send in analysis file data path?  
      if (this.getSecAdvisor().canRead(dataTrack)) {
        Document doc = dataTrack.getXML(this.getSecAdvisor(), DictionaryHelper.getInstance(sess), "c:/temp/GenoPub/");
        this.xmlResult = doc.asXML();
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permission to access data track");
      }

    }catch (NamingException e){
      log.error("An exception has occurred in GetDataTrack ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetDataTrack ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetDataTrack ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
}