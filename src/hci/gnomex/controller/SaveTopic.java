package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Notification;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Topic;
import hci.gnomex.model.Visibility;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.RequestParser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;


public class SaveTopic extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveTopic.class);
  
  private Topic       load;
  private Integer     idParentTopic = null;
  private Topic       topic;
  private boolean     isNewTopic = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    load = new Topic();
    HashMap errors = this.loadDetailObject(request, load);
    this.addInvalidFields(errors);

    if (request.getParameter("idParentTopic") != null && !request.getParameter("idParentTopic").equals("")) {
      idParentTopic = Integer.valueOf(request.getParameter("idParentTopic"));
    }

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      this.initializeTopic(load, sess);
      
      
      if (this.getSecAdvisor().canUpdate(topic)) {
        
        String topicName = RequestParser.unEscape(topic.getName());
        
        StringBuffer queryBuf = new StringBuffer("select topic");
        queryBuf.append(" from Topic as topic");
        queryBuf.append(" where topic.name = '" + topicName.replaceAll("'", "''") + "' and");
        if(idParentTopic == null) {
          queryBuf.append(" topic.idParentTopic is null");
        } else {
          queryBuf.append(" topic.idParentTopic = " + idParentTopic.toString());
          
        }
        
        if(isNewTopic) {
          // If this is a new topic then check for duplicate topic name.
          Query query = sess.createQuery(queryBuf.toString());
          List<Object[]> topicRows = (List<Object[]>)query.list();
          
          if(topicRows.size() > 0) {
            this.addInvalidField("Illegal Topic Name", "A duplicate topic already exists at this level of the hierarchy.");
            setResponsePage(this.ERROR_JSP); 
            return this;
          }           
        }

        this.topic.setName(topicName);
        this.topic.setIdParentTopic(idParentTopic);
        
        String visibilityMessage = checkAgainstParentVisibility(topic, sess);
        
        sess.save(topic);
        sess.flush();
        
        if(isNewTopic){
          sendNotification(topic, sess, Notification.NEW_NOTIFICATION, Notification.SOURCE_TYPE_USER, Notification.TYPE_TOPIC);
          sendNotification(topic, sess, Notification.NEW_NOTIFICATION, Notification.SOURCE_TYPE_ADMIN, Notification.TYPE_TOPIC);
        } else{
          sendNotification(topic, sess, Notification.EXISTING_NOTIFICATION, Notification.SOURCE_TYPE_USER, Notification.TYPE_TOPIC);
          sendNotification(topic, sess, Notification.EXISTING_NOTIFICATION, Notification.SOURCE_TYPE_ADMIN, Notification.TYPE_TOPIC);
        }
        
        this.xmlResult = "<SUCCESS idTopic=\"" + topic.getIdTopic() 
            + "\" codeVisibility=\"" + topic.getCodeVisibility()
            + "\" visibilityMsg=\"" + visibilityMessage + "\"/>";
       
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save topic.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveTopic ", e);
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
  
  private String checkAgainstParentVisibility(Topic topic, Session sess) throws Exception {
    Topic parentTopic = null;
    String retValue = "";
    if(topic.getIdParentTopic() != null) {
      parentTopic = (Topic)sess.load(Topic.class, topic.getIdParentTopic());
      if(parentTopic != null && parentTopic.getCodeVisibility().equals(Visibility.VISIBLE_TO_OWNER)) {
        if(!topic.getCodeVisibility().equals(Visibility.VISIBLE_TO_OWNER)) {
          retValue = "the owner";               
        }
      }
      if(parentTopic != null && parentTopic.getCodeVisibility().equals(Visibility.VISIBLE_TO_GROUP_MEMBERS)) {
        if(topic.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS) || topic.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          retValue = "group members";          
        }
      }
      if(parentTopic != null && parentTopic.getCodeVisibility().equals(Visibility.VISIBLE_TO_INSTITUTION_MEMBERS)) {
        if(topic.getCodeVisibility().equals(Visibility.VISIBLE_TO_PUBLIC)) {
          retValue = "institution members";          
        }
      }
    
    }
    
    // If visibility of child less restrictive than parent then set child visibility to same as parent
    if(parentTopic != null && retValue.length() > 0) {
      topic.setCodeVisibility(parentTopic.getCodeVisibility());
      topic.setIdInstitution(parentTopic.getIdInstitution());      
    }
    return retValue;
  }
  
  private void initializeTopic(Topic load, Session sess) throws Exception {
    
    if (load.getIdTopic() == null || load.getIdTopic().intValue() == 0) {
      topic = load;
      topic.setCreatedBy(this.getSecAdvisor().getUID());
      topic.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
      isNewTopic = true;
    } else {
      topic = (Topic)sess.load(Topic.class, load.getIdTopic());
      
    }
    
    topic.setName(RequestParser.unEscape(load.getName()));
    topic.setDescription(load.getDescription());
    topic.setIdLab(load.getIdLab());
    topic.setIdAppUser(load.getIdAppUser());
    topic.setCodeVisibility(load.getCodeVisibility());
    topic.setIdInstitution(load.getIdInstitution());
    
    if(isNewTopic) {
      PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);
      String defaultVisibility = propertyHelper.getProperty(PropertyDictionary.DEFAULT_VISIBILITY_TOPIC);
      if (defaultVisibility != null && defaultVisibility.length() > 0) {
        topic.setCodeVisibility(defaultVisibility);
        if(defaultVisibility.compareTo(hci.gnomex.model.Visibility.VISIBLE_TO_INSTITUTION_MEMBERS) == 0) {
          boolean institutionSet = false;
          if (topic.getIdLab() != null) {
            Lab lab = (Lab)sess.load(Lab.class, topic.getIdLab());
            Hibernate.initialize(lab.getInstitutions());
            Iterator it = lab.getInstitutions().iterator();
            while(it.hasNext()) {
              Institution thisInst = (Institution) it.next();
              if(thisInst.getIsDefault().compareTo("Y") == 0) {
                topic.setIdInstitution(thisInst.getIdInstitution()); 
                institutionSet = true;
              }
            }
          }
          if(!institutionSet) {
            // If default visibility is VISIBLE_TO_INSTITUTION_MEMBERS but this lab
            // is not a member of the institution then set default to VISIBLE_TO_GROUP_MEMBERS      
            topic.setCodeVisibility(hci.gnomex.model.Visibility.VISIBLE_TO_GROUP_MEMBERS);
          }
        }
      }
    } else {
      if (load.getIdLab() == null) {
        throw new Exception("Please assign this topic to a lab.");
      }
      Lab lab = (Lab)sess.load(Lab.class, topic.getIdLab());
      if (!lab.validateVisibilityInLab(topic)) {
        throw new Exception("You must choose an institution when Institution visibility is chosen.");
      }
    }
  }  
}