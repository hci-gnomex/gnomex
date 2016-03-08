package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.security.EncryptionUtility;
import hci.gnomex.utility.HibernateSession;
//import hci.gnomex.utility.HibernateUtil;





import java.io.Serializable;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveAppUserPublic extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveAppUserPublic.class);

  private static String LAB_USER = "USER";
  private static String LAB_MANAGER = "MANAGER";
  private static String LAB_COLLABORATOR = "COLLABORATOR";


  private AppUser     appUserScreen;
  private Document    userNotificationLabsDoc;
  private EncryptionUtility passwordEncrypter;

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

    try {
      sess = HibernateSession.currentSession(this.getUsername());
      passwordEncrypter = new EncryptionUtility();

      AppUser appUser = sess.load(AppUser.class, appUserScreen.getIdAppUser());
      if(SaveAppUser.emailAlreadyExists(sess, appUserScreen.getEmail(), appUserScreen.getIdAppUser())){
        this.addInvalidField("invalid email", "The email address " + appUserScreen.getEmail() + " is already in use.");
      }else {
        initializeAppUser(appUser);
        sess.save(appUser);
      }

      if (this.isValid()) {
        sess.flush();

        Element root = userNotificationLabsDoc.getRootElement();

        MyWork mw = new MyWork(root, appUserScreen.getIdAppUser());
        sess.doWork(mw);

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
          HibernateSession.closeTomcatSession();
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
    appUser.setUcscUrl(appUserScreen.getUcscUrl());

    if (appUser.getuNID() != null &&
        !appUser.getuNID().trim().equals("")) {
      appUser.setUserNameExternal(null);
      appUser.setPasswordExternal(null);

    } else {
      if (appUserScreen.getUserNameExternal() != null  && !appUserScreen.getUserNameExternal().trim().equals("")) {
        appUser.setuNID(null);
      }
      if (appUserScreen.getPasswordExternal() != null && !appUserScreen.getPasswordExternal().equals("") && !appUserScreen.getPasswordExternal().equals(AppUser.MASKED_PASSWORD)) {
        String salt = passwordEncrypter.createSalt();
        String encryptedPassword = passwordEncrypter.createPassword(appUserScreen.getPasswordExternal(), salt);
        appUser.setSalt(salt);
        appUser.setPasswordExternal(encryptedPassword);

      }

    }

  }


  class MyWork implements Work{
    private Element root;
    private Integer idAppUser;

    public MyWork(Element e, Integer idAppUser){
      this.root = e;
      this.idAppUser = idAppUser;
    }

    @Override
    public void execute(Connection conn) throws SQLException {
      Statement stmt = null;
      for(Iterator i = root.getChildren("Lab").iterator(); i.hasNext();) {
        Element node = (Element)i.next();
        stmt = conn.createStatement();
        String idLab = node.getAttributeValue("idLab");
        String role = node.getAttributeValue("role");
        String doUploadAlert = node.getAttributeValue("doUploadAlert");

        String tableName = "Lab" + role;

        StringBuffer buf = new StringBuffer("update " + tableName + "\n");
        buf.append(" set sendUploadAlert = '" + doUploadAlert + "'\n");
        buf.append(" where idLab = " + idLab + "\n");
        buf.append("       and idAppUser = " + idAppUser.intValue() + "\n");
        stmt.executeUpdate(buf.toString());
      }

    }
  }
}