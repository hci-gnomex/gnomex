package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.security.EncrypterService;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisGenomeBuildParser;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveAppUserPublic extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveAppUserPublic.class);
  

  private AppUser     appUserScreen;
  private Document    userNotificationLabsDoc;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    appUserScreen = new AppUser();
    HashMap errors = this.loadDetailObject(request, appUserScreen);
    this.addInvalidFields(errors);
    if (appUserScreen.getIdAppUser() == null || appUserScreen.getIdAppUser().intValue() == 0) {
      this.addInvalidField( "idAppUser", "idAppUser is null or zero");
    }
    
    StringReader reader = null;
    if (request.getParameter("userNotificationLabsXMLString") != null && !request.getParameter("userNotificationLabsXMLString").equals("")) {
      String userNotificationLabsXMLString = request.getParameter("userNotificationLabsXMLString");
      reader = new StringReader(userNotificationLabsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        userNotificationLabsDoc = sax.build(reader);

      } catch (JDOMException je ) {
        log.error( "Cannot parse userNotificationLabsXMLString", je );
        this.addInvalidField( "userNotificationLabsXMLString", "Invalid userNotificationLabsXMLString");
      }
    }       
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    Connection con = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      
      AppUser appUser = (AppUser)sess.load(AppUser.class, appUserScreen.getIdAppUser());
      initializeAppUser(appUser);            

      if (this.isValid()) {
        sess.flush();
        
        
        Element root = userNotificationLabsDoc.getRootElement();
        
        Statement stmt = null;
        con = sess.connection();      
        
        for(Iterator i = root.getChildren("Lab").iterator(); i.hasNext();) {
          Element node = (Element)i.next();
          
          String idLab = node.getAttributeValue("idLab");
          String role = node.getAttributeValue("role");
          String doUploadAlert = node.getAttributeValue("doUploadAlert");
          
          String tableName = "Lab" + role;

          StringBuffer buf = new StringBuffer("update " + tableName + "\n");
          buf.append(" set sendUploadAlert = '" + doUploadAlert + "'\n");
          buf.append(" where idLab = " + idLab + "\n");
          buf.append("       and idAppUser = " + appUserScreen.getIdAppUser().intValue() + "\n");
          stmt = con.createStatement();
          stmt.executeUpdate(buf.toString());
          stmt.close();
        }        
        this.xmlResult = "<SUCCESS idAppUser=\"" + appUser.getIdAppUser() + "\"/>";
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
      
    } catch (Exception e){
      log.error("An exception has occurred in SaveAppUserPublic ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        if(sess != null) {
          if (con != null) {
            con.close();
          }
          HibernateSession.closeSession();  
        }         
      } catch(Exception e) {
    
      }
    }
    
    return this;
  }
  
  private void initializeAppUser(AppUser appUser) {
    appUser.setFirstName(appUserScreen.getFirstName());
    appUser.setLastName(appUserScreen.getLastName());
    appUser.setInstitute(appUserScreen.getInstitute());
    appUser.setDepartment(appUserScreen.getDepartment());
    appUser.setEmail(appUserScreen.getEmail());
    appUser.setPhone(appUserScreen.getPhone());    
  }

}