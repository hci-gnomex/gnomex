package hci.gnomex.controller;

import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PendingSampleFilter;
import hci.gnomex.model.Project;
import hci.gnomex.model.ProjectRequestFilter;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Sample;
import hci.gnomex.model.Visibility;


public class GetPendingSampleList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetPendingSampleList.class);
  
  private PendingSampleFilter  filter;
  private Element              rootNode = null;
  private Element              redoNode = null;
  private Element              pendingNode = null;
  private Element              requestNode = null;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_MANAGE_DNA_SEQ_CORE)) {
      this.addInvalidField("perm", "Insufficient permissions to view pending samples for DNA Seq core");
    }

    filter = new PendingSampleFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    
  }

  public Command execute() throws RollBackCommandException {
    Document doc = new Document(new Element("SampleList"));
    rootNode = doc.getRootElement();
    
    redoNode = new Element("Status");
    redoNode.setAttribute("label", "Redos");
    rootNode.addContent(redoNode);

    pendingNode = new Element("Status");
    pendingNode.setAttribute("label", "Pending");
    rootNode.addContent(pendingNode);
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
      
      // Get the 'redo' samples
      StringBuffer buf = filter.getRedoQuery();
      log.info("Redo sample query for GetPendingSampleList: " + buf.toString());
      Query query = sess.createQuery(buf.toString());
      List redoResults = (List)query.list();
      
      fillStatusNode(redoNode, redoResults, dictionaryHelper);

      
      // Get the pending samples
      buf = filter.getPendingQuery();
      log.info("Pending sample query for GetPendingSampleList: " + buf.toString());
      query = sess.createQuery(buf.toString());
      List pendingResults = (List)query.list();

      fillStatusNode(pendingNode, pendingResults, dictionaryHelper);
      
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
      
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetPendingSampleList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetPendingSampleList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (Exception e) {
      log.error("An exception has occurred in GetPendingSampleList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private void fillStatusNode(Element parentNode, List results, DictionaryHelper dictionaryHelper) {
    
    Integer prevIdRequest  = new Integer(-1);

    for(Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      
      Integer idRequest           = (Integer)row[0];
      
      Element n = null;
      if (idRequest.intValue() != prevIdRequest.intValue()) {
        addRequestNode(parentNode, row, dictionaryHelper);          
        addSampleNode(row, dictionaryHelper);
      } else {
        addSampleNode(row, dictionaryHelper);
      }

      prevIdRequest = idRequest;
    }
    
  }
  
  private void addRequestNode(Element parentNode, Object[] row, DictionaryHelper dictionaryHelper) {
    
    Integer idRequest           = (Integer)row[0];
    String requestNumber        = (String)row[1]  == null ? ""  : (String)row[1];
    String codeRequestStatus    = (String)row[2]  == null ? ""  : (String)row[2];
    String codeRequestCategory  = (String)row[3]  == null ? ""  : (String)row[3];
    Date createDate             = (Date)row[4];
    Integer idLab               = (Integer)row[5];
    String labLastName          = (String)row[6]  == null ? ""  : (String)row[6];
    String labFirstName         = (String)row[7]  == null ? ""  : (String)row[7];
    AppUser submitter           = (AppUser)row[8];
    Sample sample               = (Sample)row[9];

    
    requestNode = new Element("Request");
    requestNode.setAttribute("idRequest",              idRequest.toString());
    requestNode.setAttribute("label",                  requestNumber);
    requestNode.setAttribute("submitDate",             createDate == null ? ""  : this.formatDate((java.sql.Date)createDate, this.DATE_OUTPUT_DASH));
    requestNode.setAttribute("idLab",                  idLab == null ? "" : idLab.toString());
    requestNode.setAttribute("submitter",              submitter != null ? submitter.getDisplayName() : "?");
  
    parentNode.addContent(requestNode);
  
  }
  
  private void addSampleNode(Object[] row, DictionaryHelper dictionaryHelper) {
    
    Integer idRequest           = (Integer)row[0];
    String requestNumber        = (String)row[1]  == null ? ""  : (String)row[1];
    String codeRequestStatus    = (String)row[2]  == null ? ""  : (String)row[2];
    String codeRequestCategory  = (String)row[3]  == null ? ""  : (String)row[3];
    Date createDate             = (Date)row[4];
    Integer idLab               = (Integer)row[5];
    String labLastName          = (String)row[6]  == null ? ""  : (String)row[6];
    String labFirstName         = (String)row[7]  == null ? ""  : (String)row[7];
    AppUser submitter           = (AppUser)row[8];
    Sample sample               = (Sample)row[9];
    
    RequestCategory requestCategory = dictionaryHelper.getRequestCategoryObject(codeRequestCategory);
      
    Element n = new Element("Sample");
    n.setAttribute("name",           sample.getName());
    n.setAttribute("idRequest",      idRequest != null ? idRequest.toString() : "");
    n.setAttribute("idLab",          idLab != null ? idLab.toString() : "");
    n.setAttribute("idSample",       sample.getIdSample().toString());
    n.setAttribute("type",           requestCategory.getRequestCategory());
    n.setAttribute("idPlate",        "");
    
    requestNode.addContent(n);      
  
  }
}