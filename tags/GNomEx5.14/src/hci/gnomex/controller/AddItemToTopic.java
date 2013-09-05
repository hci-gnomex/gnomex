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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;



public class AddItemToTopic extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AddItemToTopic.class);
  
  private Integer idRequest = null;
  private Integer idAnalysis = null;
  private Integer idDataTrack = null;
  private List<Integer> reqList = new ArrayList<Integer>();
  private List<Integer> anList = new ArrayList<Integer>();
  private List<Integer> dtList = new ArrayList<Integer>();

  private Integer idTopic = null;
  private Integer idTopicOld = null;
  
  private boolean isMove = false;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {   
    int idCnt = 0;
    if (request.getParameter("idRequest0") != null && !request.getParameter("idRequest0").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest0"));
      while(request.getParameter("idRequest"+idCnt) != null && !request.getParameter("idRequest"+idCnt).equals("")) {
        reqList.add(new Integer(request.getParameter("idRequest"+idCnt)));
        idCnt++;
      }
    } else if (request.getParameter("idAnalysis0") != null && !request.getParameter("idAnalysis0").equals("")) {
      idAnalysis = new Integer(request.getParameter("idAnalysis0"));
      while(request.getParameter("idAnalysis"+idCnt) != null && !request.getParameter("idAnalysis"+idCnt).equals("")) {
        anList.add(new Integer(request.getParameter("idAnalysis"+idCnt)));
        idCnt++;
      }
    } else if (request.getParameter("idDataTrack0") != null && !request.getParameter("idDataTrack0").equals("")) {
      idDataTrack = new Integer(request.getParameter("idDataTrack0"));
      while(request.getParameter("idDataTrack"+idCnt) != null && !request.getParameter("idDataTrack"+idCnt).equals("")) {
        dtList.add(new Integer(request.getParameter("idDataTrack"+idCnt)));
        idCnt++;
      }
    } else {
      this.addInvalidField("Missing id", "idRequest, idAnalysis, or idDataTrack is required.");      
    }
    
    if (request.getParameter("idTopic") != null && !request.getParameter("idTopic").equals("")) {
      idTopic = new Integer(request.getParameter("idTopic"));
    } else {
      this.addInvalidField("Missing idTopic", "idTopic is required.");
    }
    
    if (request.getParameter("isMove") != null && !request.getParameter("isMove").equals("")) {
      // If move or copy make sure idTopicOld present as well
      if (request.getParameter("idTopicOld") != null && !request.getParameter("idTopicOld").equals("")) {
        idTopicOld = new Integer(request.getParameter("idTopicOld"));
      } else {
        this.addInvalidField("Missing idTopicOld", "idTopicOld is required.");
        return;
      }      
      if(request.getParameter("isMove").compareTo("Y") == 0) {
        isMove = true;
      }
    }
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    Request request = null;
    Analysis analysis = null;
    DataTrack dataTrack = null;
    Topic topic = null;
    Topic oldTopic = null;
    boolean topicUpdated = false;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.isValid()) {
        topic = (Topic)sess.load(Topic.class, idTopic);
        if(reqList.size() > 0) {         
          Set<Request> newRequests = new TreeSet<Request>(new RequestComparator());
          for(Iterator<?> i = topic.getRequests().iterator(); i.hasNext();) {
            Request r = Request.class.cast(i.next());
            newRequests.add(r);
          } 
          for(Integer thisIdRequest : reqList) {
            request = (Request)sess.load(Request.class, thisIdRequest);
            newRequests.add(request);
          }
          topic.setRequests(newRequests);

          if(idTopicOld != null && isMove) {
            // If this is a move then remove item from the old topic
            Set<Request> oldTopicRequests = new TreeSet<Request>(new RequestComparator());
            oldTopic = (Topic)sess.load(Topic.class, idTopicOld);
            for(Iterator<?> i = oldTopic.getRequests().iterator(); i.hasNext();) {
              Request r = (Request) i.next();
              if(r.getIdRequest().compareTo(idRequest) != 0) {
                oldTopicRequests.add(r);
              }
            } 
            oldTopic.setRequests(oldTopicRequests);
          }
          sess.flush();
          topicUpdated = true;       
        } else if (anList.size() > 0) {
          Set<Analysis> newAnalyses = new TreeSet<Analysis>(new AnalysisComparator());
          for(Iterator<?> i = topic.getAnalyses().iterator(); i.hasNext();) {
            Analysis a = Analysis.class.cast(i.next());
            newAnalyses.add(a);
          }          
          for(Integer thisIdAnalysis : anList) {
            analysis = (Analysis) sess.load(Analysis.class, thisIdAnalysis);       
            newAnalyses.add(analysis);
          }
          topic.setAnalyses(newAnalyses);
          
          if(idTopicOld != null && isMove) {
            // If this is a move then remove item from the old topic
            Set<Analysis> oldTopicAnalyses = new TreeSet<Analysis>(new AnalysisComparator());
            oldTopic = (Topic)sess.load(Topic.class, idTopicOld);
            for(Iterator<?> i = oldTopic.getAnalyses().iterator(); i.hasNext();) {
              Analysis a = (Analysis) i.next();
              if(a.getIdAnalysis().compareTo(idAnalysis) != 0) {
                oldTopicAnalyses.add(a);
              }
            } 
            oldTopic.setAnalyses(oldTopicAnalyses);
          } 
          sess.flush();
          topicUpdated = true;          
        } else if (dtList.size() > 0) {
          Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
          for(Iterator<?> i = topic.getDataTracks().iterator(); i.hasNext();) {
            DataTrack dt = DataTrack.class.cast(i.next());
            newDataTracks.add(dt);
          }
          for(Integer thisIdDataTrack : dtList) {
            dataTrack = (DataTrack)sess.load(DataTrack.class, thisIdDataTrack);
            newDataTracks.add(dataTrack);
          }
          topic.setDataTracks(newDataTracks);
          
          if(idTopicOld != null && isMove) {
            // If this is a move then remove item from the old topic
            Set<DataTrack> oldTopicDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
            oldTopic = (Topic)sess.load(Topic.class, idTopicOld);
            for(Iterator<?> i = oldTopic.getDataTracks().iterator(); i.hasNext();) {
              DataTrack dt = (DataTrack) i.next();
              if(dt.getIdDataTrack().compareTo(idDataTrack) != 0) {
                oldTopicDataTracks.add(dt);
              }
            } 
            oldTopic.setDataTracks(oldTopicDataTracks);
          }
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