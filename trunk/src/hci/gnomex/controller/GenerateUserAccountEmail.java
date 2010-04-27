package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.RequestEmailBodyFormatter;
import hci.gnomex.utility.VerifyLabUsersEmailFormatter;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class GenerateUserAccountEmail extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysisGroup.class);
  
  private Integer idLab;
  private String  serverName;
  private int     emailCount = 0;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idLab") != null) {
      idLab = new Integer(request.getParameter("idLab"));
    }
    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
      this.addInvalidField("permission", "Insufficient permissions to generate lab account emails.");
    }
    

    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {
    emailCount = 0;
    try {
      
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      List<Lab> labs = new ArrayList<Lab>();
      if (this.idLab != null) {
        Lab lab = (Lab)sess.load(Lab.class, idLab);
        labs.add(lab);
      } else {
        labs = (List<Lab>)sess.createQuery("SELECT l from Lab as l ORDER BY l.lastName, l.firstName ").list();
      }
      
      for (Iterator i = labs.iterator(); i.hasNext();) {
        Lab l = (Lab)i.next();
        
        // Bypass labs without contact email
        if (l.getContactEmail() == null || l.getContactEmail().equals("")) {
          log.warn("Bypassing email of user accounts to lab " + l.getName() + ". Contact email not filled in.");
          continue;
        }
        
        String sendTo = null;
        String[] tokens = l.getContactEmail().split(",", 2);
        if (tokens.length > 0) {
          sendTo = tokens[0];
        }
        
        
        
        
        TreeMap<String, String> managers = new TreeMap<String, String>();
        for(Iterator i1 = l.getManagers().iterator(); i1.hasNext();) {
          AppUser manager = (AppUser)i1.next();
          if (manager.getIsActive() != null && manager.getIsActive().equalsIgnoreCase("Y")) {
            managers.put(manager.getQualifiedDisplayName(), null);
          }
        }          
        TreeMap<String, String> members = new TreeMap<String, String>();
        for(Iterator i2 = l.getMembers().iterator(); i2.hasNext();) {
          AppUser member = (AppUser)i2.next();
          if (member.getIsActive() != null && member.getIsActive().equalsIgnoreCase("Y")) {
            if (!managers.containsKey(member.getQualifiedDisplayName())) {
              members.put(member.getQualifiedDisplayName(), null);
            }
          }
        }          
        TreeMap<String, String> collaborators = new TreeMap<String, String>();
        for(Iterator i3 = l.getCollaborators().iterator(); i3.hasNext();) {
          AppUser collab = (AppUser)i3.next();
          if (collab.getIsActive() != null && collab.getIsActive().equalsIgnoreCase("Y")) {
            collaborators.put(collab.getQualifiedDisplayName(), null);
          }
        }          

        String labName = "";
        if (l.getFirstName() != null && !l.getFirstName().equals("")) {
          labName = l.getFirstName();
        }
        if (l.getLastName() != null && !l.getLastName().equals("")) {
          if (labName.length() > 0) {
            labName += " ";
          }
          labName += l.getLastName();
        }

        // Bypass labs without any user accounts
        if (managers.isEmpty() && members.isEmpty() && collaborators.isEmpty()) {
          log.warn("Bypassing email of user accounts to lab " + labName + ". No user accounts exist for this lab.");
          continue;
        }
        
        // Format email body
        VerifyLabUsersEmailFormatter emailFormatter = new VerifyLabUsersEmailFormatter(sess, labName, managers, members, collaborators);
        String emailBody = emailFormatter.format();
        
        
        String subject = "GNomEx user accounts for " + labName + (labName.contains("Lab") || labName.contains("lab") ? "" : " Lab");
        this.sendEmail(sess, dh, sendTo, null, emailBody, subject);
      }
      
    
      if (isValid()) {
        this.xmlResult = "<SUCCESS emailCount='" + emailCount + "'" + "/>";
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GenerateUserAccountEmail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in GenerateUserAccountEmail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GenerateUserAccountEmail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GenerateUserAccountEmail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GenerateUserAccountEmail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private void sendEmail(Session sess, DictionaryHelper dictionaryHelper, String sendTo, String ccTo, String emailBody, String subject) throws NamingException, MessagingException {
    
   
    boolean send = false;
    if (serverName.equals(dictionaryHelper.getProperty(Property.PRODUCTION_SERVER))) {
      send = true;
    } else {
      if (sendTo.equals(dictionaryHelper.getProperty(Property.CONTACT_EMAIL_SOFTWARE_TESTER))) {
        send = true;
        subject = "TEST - " + subject;
      }
    }
    
    if (send) {
      MailUtil.send(sendTo, 
          ccTo,
          dictionaryHelper.getProperty(Property.CONTACT_EMAIL_CORE_FACILITY), 
          subject, 
          emailBody,
          true);
      emailCount++;
    }
    
  }

}