package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.hibernate.Session;




public class GetGenomeBuild extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetGenomeBuild.class);
  
  private Integer idGenomeBuild;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idGenomeBuild") != null && !request.getParameter("idGenomeBuild").equals("")) {
      idGenomeBuild = new Integer(request.getParameter("idGenomeBuild"));   
    } else {
      this.addInvalidField("idGenomeBuild", "idGenomeBuild is required");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

      // TODO: GENOPUB Need to send in genome build file data path?  
      Document doc = genomeBuild.getXML(this.getSecAdvisor(),  "c:/temp/GenoPub/");
      this.xmlResult = doc.asXML();
      setResponsePage(this.SUCCESS_JSP);
      

    }catch (NamingException e){
      log.error("An exception has occurred in GetGenomeBuild ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetGenomeBuild ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetGenomeBuild ", e);
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