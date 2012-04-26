package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetUsageDetail extends GNomExCommand implements Serializable {
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetUsageDetail.class);

  private Date startDate = null;
  private Calendar endDate = null;
  private String chartName = "";
  private String fieldName = "";
  private String usageUserVisibility = "";
  
  DateFormat dfShort    = new SimpleDateFormat("MMM yyyy");
  DateFormat dfDataTip  = new SimpleDateFormat("MMM dd yyyy");
  DateFormat dfNormal   = new SimpleDateFormat("MM-dd-yyyy");

  Calendar today = Calendar.getInstance();
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("startDate") != null && !request.getParameter("startDate").equals("")) {
      startDate = this.parseDate(request.getParameter("startDate"));
      endDate = new GregorianCalendar();
      endDate.setTime(startDate);
      endDate.add(Calendar.DAY_OF_YEAR, +8);
    }
    if (request.getParameter("chartName") != null && !request.getParameter("chartName").equals("")) {
      chartName = request.getParameter("chartName");
    }
    if (request.getParameter("fieldName") != null && !request.getParameter("fieldName").equals("")) {
      fieldName = request.getParameter("fieldName");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
           
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      usageUserVisibility = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.USAGE_USER_VISIBILITY);

      // Guests cannot run this command
      if (this.getSecAdvisor().isGuest()) {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to get usage data.  Guests cannot access usage data.");
        setResponsePage(this.ERROR_JSP);
      } 
    
      // Admins can run this command.  Normal gnomex users can if usage_user_visibility
      // property is set to an appropriate level ('masked' or 'full').
      if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      } else if (usageUserVisibility.equals("") || usageUserVisibility.equals(PropertyDictionary.OPTION_USER_USER_VISIBILITY_NONE)) {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to get usage data.  Property usage_user_visibility does not allow users to access usage data");
        setResponsePage(this.ERROR_JSP);
      }

      
      if (this.isValid()) {
        Document doc = new Document(new Element("UsageDetail"));
        if (chartName.equals(GetUsageData.SUMMARY_ACTIVITY_BY_WEEK)) {
          if (fieldName.equals("experimentCount")) {
            getActivityExperimentDetail(sess, doc.getRootElement());
          } else if (fieldName.equals("analysisCount")) {
            getActivityAnalysisDetail(sess, doc.getRootElement());
          } else if (fieldName.equals("uploadCount")) {
            getActivityTransferDetail(sess, doc.getRootElement(), "upload");
          } else if (fieldName.equals("downloadCount")) {
            getActivityTransferDetail(sess, doc.getRootElement(), "download");
          }
        }

        XMLOutputter out = new org.jdom.output.XMLOutputter();
        this.xmlResult = out.outputString(doc);

        setResponsePage(this.SUCCESS_JSP);
      }
      
    }catch (NamingException e){
      log.error("An exception has occurred in GetUsageDetail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetUsageDetail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (XMLReflectException e){
      log.error("An exception has occurred in GetUsageDetail ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e){
      log.error("An exception has occurred in GetUsageDetail ", e);
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
  
  private void getActivityExperimentDetail(Session sess, Element parentElement) {

    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, r.createDate, r.number from Request r ");
    queryBuf.append("join r.lab as lab ");
    queryBuf.append("where r.createDate >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and r.createDate < '" + this.formatDate(endDate.getTime(), this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, r.createDate, r.number");
    
    List rows = sess.createQuery(queryBuf.toString()).list();
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      String labLastName = (String)row[1];
      String labFirstName = (String)row[2];
      Date createDate = (Date)row[3];
      String requestNumber = (String)row[4];
      
      
      Element node = new Element("Entry");
      node.setAttribute("labName", getLabName(idLab, labLastName, labFirstName));
      node.setAttribute("labNameDisplay", getLabName(idLab, labLastName, labFirstName));
      node.setAttribute("number", requestNumber);
      node.setAttribute("createDate", this.formatDate(createDate));
      
      parentElement.addContent(node);
    }
  }
  
  private void getActivityAnalysisDetail(Session sess, Element parentElement) {

    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, a.createDate, a.number from Analysis a ");
    queryBuf.append("join a.lab as lab ");
    queryBuf.append("where a.createDate >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and a.createDate < '" + this.formatDate(endDate.getTime(), this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, a.createDate, a.number");
    
    List rows = sess.createQuery(queryBuf.toString()).list();
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      String labLastName = (String)row[1];
      String labFirstName = (String)row[2];
      Date createDate = (Date)row[3];
      String analysisNumber = (String)row[4];
      
      
      Element node = new Element("Entry");
      node.setAttribute("labName", getLabName(idLab, labLastName, labFirstName));
      node.setAttribute("number", analysisNumber);
      node.setAttribute("createDate", this.formatDate(createDate));
      
      parentElement.addContent(node);
    }
  }

  private void getActivityTransferDetail(Session sess, Element parentElement, String transferType) {
    List rows;
    TreeMap nodeMap = new TreeMap();
    StringBuffer queryBuf = new StringBuffer();
    
    String distinctStr = "";
    if(transferType.compareTo("upload")==0) {
      distinctStr = "distinct";
    }
    
    /*
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, r.number, count(*) ");
    queryBuf.append("from TransferLog tl, Request r, Lab lab ");
    queryBuf.append("where tl.idRequest = r.idRequest ");
    queryBuf.append("and r.idLab = lab.idLab ");
    queryBuf.append("and tl.startDateTime >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.startDateTime < '" + this.formatDate(endDate.getTime(), this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.transferType = '" + transferType + "' ");
    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, r.number ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, r.number"); 
    rows = sess.createQuery(queryBuf.toString()).list();
    */

    queryBuf.append("SELECT t.idLab, t.lastName, t.firstName, t.sDateTime, t.number, count(*)  ");
    queryBuf.append("from ");
    queryBuf.append("(SELECT " + distinctStr + " lab.idLab, lab.lastName, lab.firstName, CAST(tl.startDateTime AS DATE) as sDateTime, r.number, tl.fileName ");
    queryBuf.append("from TransferLog tl, Request r, Lab lab ");
    queryBuf.append("where tl.idRequest = r.idRequest ");
    queryBuf.append("and r.idLab = lab.idLab ");
    queryBuf.append("and tl.startDateTime >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.startDateTime < '" + this.formatDate(endDate.getTime(), this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.transferType = '" + transferType + "') t ");
    queryBuf.append("group by t.idLab, t.lastName, t.firstName, t.sDateTime, t.number ");
    queryBuf.append("order by t.idLab, t.lastName, t.firstName, t.sDateTime, t.number ");
    rows = sess.createSQLQuery(queryBuf.toString()).list();


    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      String labLastName = (String)row[1];
      String labFirstName = (String)row[2];
      Date createDate = (Date)row[3];
      String requestNumber = (String)row[4];
      Integer count = (Integer)row[5];

      Element node = new Element("Entry");
      node.setAttribute("labName", getLabName(idLab, labLastName, labFirstName));
      node.setAttribute("number", requestNumber);
      node.setAttribute("transferDate", this.formatDate(createDate));
      node.setAttribute( transferType + "Count", count.toString());

      nodeMap.put(Lab.formatLabName(labLastName, labFirstName) + requestNumber, node);
    }

    queryBuf = new StringBuffer();
    
    /*
    queryBuf.append("SELECT lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, a.number, count(*) ");
    queryBuf.append("from TransferLog tl, Analysis a, Lab lab ");
    queryBuf.append("where tl.idAnalysis = a.idAnalysis ");
    queryBuf.append("and a.idLab = lab.idLab ");
    queryBuf.append("and tl.startDateTime >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.startDateTime < '" + this.formatDate(endDate.getTime(), this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.transferType = '" + transferType + "' ");
    queryBuf.append("group by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, a.number ");
    queryBuf.append("order by lab.idLab, lab.lastName, lab.firstName, tl.startDateTime, a.number");
    rows = sess.createQuery(queryBuf.toString()).list();
    */

    queryBuf.append("SELECT t.idLab, t.lastName, t.firstName, t.sDateTime, t.number, count(*)  ");
    queryBuf.append("from ");
    queryBuf.append("(SELECT " + distinctStr + " lab.idLab, lab.lastName, lab.firstName, CAST(tl.startDateTime AS DATE) as sDateTime, a.number, tl.fileName ");
    queryBuf.append("from TransferLog tl, Analysis a, Lab lab  ");
    queryBuf.append("where tl.idAnalysis = a.idAnalysis and a.idLab = lab.idLab ");
    queryBuf.append("and tl.startDateTime >= '" + this.formatDate(startDate, this.DATE_OUTPUT_SQL) + "' ");
    queryBuf.append("and tl.startDateTime < '" + this.formatDate(endDate.getTime(), this.DATE_OUTPUT_SQL) + "'  ");
    queryBuf.append("and tl.transferType = '" + transferType + "') t ");
    queryBuf.append("group by t.idLab, t.lastName, t.firstName, t.sDateTime, t.number ");
    queryBuf.append("order by t.idLab, t.lastName, t.firstName, t.sDateTime, t.number ");
    rows = sess.createSQLQuery(queryBuf.toString()).list();
    
    for(Iterator i = rows.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      Integer idLab = (Integer)row[0];
      String labLastName = (String)row[1];
      String labFirstName = (String)row[2];
      Date createDate = (Date)row[3];
      String analysisNumber = (String)row[4];
      Integer count = (Integer)row[5];

      Element node = new Element("Entry");
      node.setAttribute("labName", getLabName(idLab, labLastName, labFirstName));
      node.setAttribute("number", analysisNumber);
      node.setAttribute("transferDate", this.formatDate(createDate));
      node.setAttribute(transferType + "Count", count.toString());

      nodeMap.put(Lab.formatLabName(labLastName, labFirstName) + analysisNumber, node);
    }

    for (Iterator i = nodeMap.keySet().iterator(); i.hasNext();) {
      String key = (String)i.next();
      Element node = (Element)nodeMap.get(key);
      parentElement.addContent(node);
    }

    
  }
  /**
   * Show lab label if logged in user is admin or usage_user_visibility set to 'full'.
   * If usage_user_visibility set to 'masked', mask lab names for labs that user
   * is not a part of (member, manager, or collaborator).
   */
  private String getLabName(Integer idLab, String labLastName, String labFirstName) {
    String labName = "";
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
      labName = Lab.formatLabName(labLastName, labFirstName);
    } else if (usageUserVisibility.equals(PropertyDictionary.OPTION_USER_USER_VISIBILITY_MASKED)) {
      if (this.getSecAdvisor().isGroupIAmMemberOf(idLab) || this.getSecAdvisor().isGroupICollaborateWith(idLab)) {
        labName = Lab.formatLabName(labLastName, labFirstName);
      } else {
        labName = "-";
      }
    } else {
      labName = Lab.formatLabName(labLastName, labFirstName);
    }
    return labName;
  }

}