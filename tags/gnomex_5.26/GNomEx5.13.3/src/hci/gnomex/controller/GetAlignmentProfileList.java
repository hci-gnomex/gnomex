package hci.gnomex.controller;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AlignmentProfile;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;


public class GetAlignmentProfileList extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAlignmentProfileList.class);

  private Integer  idAlignmentPlatform = null;
  private Integer  idSeqRunType = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idAlignmentPlatform") != null && !request.getParameter("idAlignmentPlatform").equals("")) {
      idAlignmentPlatform = new Integer(request.getParameter("idAlignmentPlatform"));
    } 
    
    if (request.getParameter("idSeqRunType") != null && !request.getParameter("idSeqRunType").equals("")) {
      idSeqRunType = new Integer(request.getParameter("idSeqRunType"));
    } 
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

  }

  public Command execute() throws RollBackCommandException {
    List alignmentProfiles;
    
    try {

      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());

      DictionaryManager dictionaryManager = DictionaryManager.getDictionaryManager(ManageDictionaries.DICTIONARY_NAMES_XML, sess, this, true);

      Document doc = new Document(new Element("AlignmentProfileList"));
      
      if(idAlignmentPlatform != null && idSeqRunType != null) {
        StringBuffer queryStr = new StringBuffer("SELECT ap from AlignmentProfile ap");
        queryStr.append(" where ap.idAlignmentPlatform = " + this.idAlignmentPlatform.intValue());
        queryStr.append(" and ap.idSeqRunType = " + this.idSeqRunType.intValue());
        queryStr.append(" order by ap.alignmentProfileName");
        alignmentProfiles = sess.createQuery(queryStr.toString()).list();        
      } else {
        alignmentProfiles = sess.createQuery("SELECT ap from AlignmentProfile ap order by ap.alignmentProfileName").list();        
      }



      for(Iterator i = alignmentProfiles.iterator(); i.hasNext();) {
        AlignmentProfile alignmentProfile = (AlignmentProfile)i.next();
        this.getSecAdvisor().flagPermissions(alignmentProfile);
        Element node = alignmentProfile.toXMLDocument(null, DetailObject.DATE_OUTPUT_SQL).getRootElement();
        doc.getRootElement().addContent(node);
      }

      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetAlignmentProfileList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetAlignmentProfileList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetAlignmentProfileList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetAlignmentProfileList ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }

    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }
    
    return this;
  }

}