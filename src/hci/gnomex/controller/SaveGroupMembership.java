package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.BillingAccountParser;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.LabMemberParser;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveGroupMembership extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveGroupMembership.class);
  
  private String                membersXMLString;
  private Document              membersDoc;
  private LabMemberParser       labMemberParser;
  
  private String                collaboratorsXMLString;
  private Document              collaboratorsDoc;
  private LabMemberParser       collaboratorParser;

  private Integer               idLab;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
      idLab = new Integer(request.getParameter("idLab"));
    } else {
      this.addInvalidField("idLab", "idLab required");
    }
    
    if (request.getParameter("membersXMLString") != null && !request.getParameter("membersXMLString").equals("")) {
      membersXMLString = request.getParameter("membersXMLString");
    }
    
    StringReader reader = new StringReader(membersXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      membersDoc = sax.build(reader);
      labMemberParser = new LabMemberParser(membersDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse membersXMLString", je );
      this.addInvalidField( "membersXMLString", "Invalid membersXMLString");
    }
    
    if (request.getParameter("collaboratorsXMLString") != null && !request.getParameter("collaboratorsXMLString").equals("")) {
      collaboratorsXMLString = request.getParameter("collaboratorsXMLString");
    }
    
    reader = new StringReader(collaboratorsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      collaboratorsDoc = sax.build(reader);
      collaboratorParser = new LabMemberParser(collaboratorsDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse collaboratorsXMLString", je );
      this.addInvalidField( "collaboratorsXMLString", "Invalid collaboratorsXMLString");
    }
   
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      Lab lab = (Lab)sess.load(Lab.class, idLab);
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS) ||
          this.getSecAdvisor().canUpdate(lab, SecurityAdvisor.PROFILE_GROUP_MEMBERSHIP)) {
        
        labMemberParser.parse(sess);
        collaboratorParser.parse(sess);
        
        
        
        //
        // Save lab members
        //
        TreeSet members = new TreeSet(new AppUserComparator());
        for(Iterator i = labMemberParser.getAppUserMap().keySet().iterator(); i.hasNext();) {
          Integer idAppUser = (Integer)i.next();
          AppUser appUser = (AppUser)labMemberParser.getAppUserMap().get(idAppUser);     
          members.add(appUser);
        }
        lab.setMembers(members);
        
        sess.flush();
        
        
        //
        // Save lab collaborators
        //
        TreeSet collaborators = new TreeSet(new AppUserComparator());
        for(Iterator i = collaboratorParser.getAppUserMap().keySet().iterator(); i.hasNext();) {
          Integer idAppUser = (Integer)i.next();
          AppUser appUser = (AppUser)collaboratorParser.getAppUserMap().get(idAppUser);     
          collaborators.add(appUser);
        }
        lab.setCollaborators(collaborators);
        
        sess.flush();
        
                
        this.xmlResult = "<SUCCESS idLab=\"" + lab.getIdLab() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to assign group membership.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveLab ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
 
  
  private class AppUserComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AppUser u1 = (AppUser)o1;
      AppUser u2 = (AppUser)o2;
      
      return u1.getIdAppUser().compareTo(u2.getIdAppUser());
      
    }
  }

}