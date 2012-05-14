package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.Request;
import hci.gnomex.model.Topic;
import hci.gnomex.utility.AnalysisComparator;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestComparator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;



public class UnlinkItemFromTopic extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UnlinkItemFromTopic.class);
  
  
  private Integer      idTopic = null;
  private Integer      idRequest = null;
  private Integer      idAnalysis = null;
  private Integer      idDataTrack = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
      idAnalysis = new Integer(request.getParameter("idAnalysis"));
    } else if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
      idDataTrack = new Integer(request.getParameter("idDataTrack"));
    } else {
      this.addInvalidField("Missing id", "idRequest, idAnalysis, or idDataTrack is required.");      
    }
    
    if (request.getParameter("idTopic") != null && !request.getParameter("idTopic").equals("")) {
      idTopic = new Integer(request.getParameter("idTopic"));
    } else {
      this.addInvalidField("Missing idTopic", "idTopic is required.");
    }
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    Topic topic = null;
    boolean topicUpdated = false;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.isValid()) {
        topic = (Topic)sess.load(Topic.class, idTopic);
        if(idRequest != null) {
          Set<Request> newRequests = new TreeSet<Request>(new RequestComparator());
          for(Iterator<?> i = topic.getRequests().iterator(); i.hasNext();) {
            Request r = (Request) i.next();
            if(r.getIdRequest().compareTo(idRequest) != 0) {
              newRequests.add(r);
            }
          } 
          topic.setRequests(newRequests);
          sess.flush();
          topicUpdated = true;       
        } else if (idAnalysis != null) {     
          Set<Analysis> newAnalyses = new TreeSet<Analysis>(new AnalysisComparator());
          for(Iterator<?> i = topic.getAnalyses().iterator(); i.hasNext();) {
            Analysis a = (Analysis) i.next();
            System.out.println(a.getName());
            if(a.getIdAnalysis().compareTo(idAnalysis) != 0) {
              newAnalyses.add(a);
            }                 
          }          
          topic.setAnalyses(newAnalyses);
          sess.flush();
          topicUpdated = true;          
        } else if (idDataTrack != null) {
          Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
          for(Iterator<?> i = topic.getDataTracks().iterator(); i.hasNext();) {
            DataTrack dt = (DataTrack)i.next();
            if(dt.getIdDataTrack().compareTo(idDataTrack) != 0) {
              newDataTracks.add(dt);
            }                 
          }
          topic.setDataTracks(newDataTracks);
          sess.flush();
          topicUpdated = true;          
        } else {
          this.addInvalidField("Error", "Unable to update topic items.");
        }
      }
      
      if (topicUpdated) {
        Element root = new Element("SUCCESS");
        Document doc = new Document(root);
        root.setAttribute("idRequest", idRequest==null?"":idRequest.toString());
        root.setAttribute("idAnalysis", idAnalysis==null?"":idAnalysis.toString());
        root.setAttribute("idDataTrack", idDataTrack==null?"":idDataTrack.toString());
        root.setAttribute("idTopic", idTopic==null?"":idTopic.toString());
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        out.setOmitEncoding(true);
        this.xmlResult = out.outputString(doc);
        this.setResponsePage(SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }      
    } catch (Exception e){
      log.error("An exception has occurred in AddItemToTopic ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
}