package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Topic;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        queryBuf.append(" where topic.name = '" + topicName + "' and");
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
        //this.topic.setDescription(RequestParser.unEscapeBasic(topic.getDescription()));
        this.topic.setIdParentTopic(idParentTopic);
        
        sess.save(topic);
        sess.flush();

        this.xmlResult = "<SUCCESS idTopic=\"" + topic.getIdTopic() + "\"/>";
      
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
  
  private void initializeTopic(Topic load, Session sess) throws Exception {
    
    if (load.getIdTopic() == null || load.getIdTopic().intValue() == 0) {
      topic = load;
      topic.setCreatedBy(this.getSecAdvisor().getUID());
      topic.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
      topic.setIdAppUser(this.getSecAdvisor().getIdAppUser());
      isNewTopic = true;
      
      
    } else {
      topic = (Topic)sess.load(Topic.class, load.getIdTopic());
      
    }
    
    topic.setName(RequestParser.unEscape(load.getName()));
    //topic.setDescription(RequestParser.unEscapeBasic(load.getDescription()));
    topic.setDescription(load.getDescription());
    topic.setIdLab(load.getIdLab());

    // If parent annotation grouping is owned by a user group, this
    // child annotation grouping must be as well.
    if (!isNewTopic) {
      if (topic.getParentTopic() != null &&
          topic.getParentTopic().getIdLab() != null) {

        if (load.getIdLab() == null ||
            !topic.getParentTopic().getIdLab().equals(load.getIdLab())) {
          throw new Exception("Topic '" + load.getName() + "' must belong to lab '" + 
              DictionaryHelper.getInstance(sess).getLabObject(topic.getParentTopic().getIdLab()).getName() + "'");
        }
      } 
      
    }

  }  
  
 
  
  

}