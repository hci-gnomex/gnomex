package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.VerifyLabUsersEmailFormatter;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);

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

        String labName =  l.getName(false, true);

        // Bypass labs without any user accounts
        if (managers.isEmpty() && members.isEmpty() && collaborators.isEmpty()) {
          log.warn("Bypassing email of user accounts to lab " + labName + ". No user accounts exist for this lab.");
          continue;
        }

        // Format email body
        VerifyLabUsersEmailFormatter emailFormatter = new VerifyLabUsersEmailFormatter(sess, l, labName, managers, members, collaborators);
        String emailBody = emailFormatter.format(this.getSecAdvisor().getAppUser());


        String subject = "GNomEx user accounts for " + labName + (labName.contains("Lab") || labName.contains("lab") ? "" : " Lab");
        this.sendEmail(sess, pdh, sendTo, null, emailBody, subject, l);
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

  private void sendEmail(Session sess, PropertyDictionaryHelper dictionaryHelper, String sendTo, String ccTo, String emailBody, String subject, Lab lab) throws NamingException, MessagingException, IOException {

    if(!MailUtil.isValidEmail(sendTo)){
      log.error("Invalid email " + sendTo);
    }

    String from = "";
    if (this.getSecAdvisor().getAppUser().getEmail()!=null) {
      from = this.getSecAdvisor().getAppUser().getEmail();
    }
    if(from.equals("") || !MailUtil.isValidEmail(from)){
        from = DictionaryHelper.getInstance(sess).getPropertyDictionary(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
    }
    
    MailUtilHelper helper = new MailUtilHelper(	
    		sendTo,
    		ccTo,
    		null,
    		from,
    		subject,
    		emailBody,
    		null,
    		true, 
    		DictionaryHelper.getInstance(sess),
  		    serverName 	);
    MailUtil.validateAndSendEmail(helper);
    
    emailCount++;

  }

}