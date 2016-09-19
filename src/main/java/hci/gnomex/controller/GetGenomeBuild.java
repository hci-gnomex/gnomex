package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.apache.log4j.Logger;



public class GetGenomeBuild extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(GetGenomeBuild.class);
  
  private Integer idGenomeBuild;
  private String serverName;
  private String baseDir;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idGenomeBuild") != null && !request.getParameter("idGenomeBuild").equals("")) {
      idGenomeBuild = new Integer(request.getParameter("idGenomeBuild"));   
    } else {
      this.addInvalidField("idGenomeBuild", "idGenomeBuild is required");
    }
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
      
      GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

      // TODO: GENOPUB Need to send in genome build file data path?  
      Document doc = genomeBuild.getXML(this.getSecAdvisor(),  baseDir);
      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      setResponsePage(this.SUCCESS_JSP);
      

    }catch (Exception e) {
      LOG.error("An exception has occurred in GetGenomeBuild ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        LOG.error("An exception has occurred in GetGenomeBuild ", e);
      }
    }
    
    return this;
  }
}