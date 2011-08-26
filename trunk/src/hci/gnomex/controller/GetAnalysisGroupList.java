package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisGroupFilter;
import hci.gnomex.model.Lab;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.Visibility;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetAnalysisGroupList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysisGroupList.class);
  
  private static int           MAX_ANALYSIS_COUNT = 2000;
  
  private AnalysisGroupFilter  filter;
  private Element              rootNode = null;
  private Element              labNode = null;
  private Element              analysisGroupNode = null;
  private Element              analysisNode = null;
  private String               listKind = "AnalysisGroupList";
  private String               showMyLabsAlways = "N";
  
  private String               message = "";
  private int                  analysisCount = 0;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new AnalysisGroupFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    if (request.getParameter("showMyLabsAlways") != null && !request.getParameter("showMyLabsAlways").equals("")) {
      showMyLabsAlways = request.getParameter("showMyLabsAlways");
    }
    
    if (request.getParameter("listKind") != null && !request.getParameter("listKind").equals("")) {
      listKind = request.getParameter("listKind");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    Document doc = new Document(new Element(listKind));
    rootNode = doc.getRootElement();
    List results = null;

    try {
      if (!filter.hasSufficientCriteria(this.getSecAdvisor())) {
        message = "Please select a filter";
        rootNode.setAttribute("message", message);
        
      } else {
        Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
        
        HashMap myLabMap = new HashMap();
        if (showMyLabsAlways.equals("Y")) {
          for(Iterator i = this.getSecAdvisor().getAllMyGroups().iterator(); i.hasNext();) {
            Lab lab = (Lab)i.next();
            if (this.getSecAdvisor().isGroupIAmMemberOrManagerOf(lab.getIdLab())) {
              myLabMap.put(lab.getIdLab(), lab);
            }
          }
        }
        
      
        StringBuffer buf = filter.getQuery(this.getSecAdvisor());
        log.info("Query for GetAnalysisGroupList: " + buf.toString());
        results = (List)sess.createQuery(buf.toString()).list();

        
        Integer prevIdLab            = new Integer(-1);
        Integer prevIdAnalysisGroup  = new Integer(-1);
        Integer prevIdAnalysis       = new Integer(-1);
        
        for(Iterator i = results.iterator(); i.hasNext();) {
          Object[] row = (Object[])i.next();
          
          
          Integer idAnalysisGroup = row[0] == null ? new Integer(-2) : (Integer)row[0];
          Integer idAnalysis      = row[7] == null ? new Integer(-2) : (Integer)row[7];
          Integer idLab           = row[3] == null ? new Integer(-2) : (Integer)row[3];    
          
          Element n = null;
          if (idLab.intValue() != prevIdLab.intValue()) {
            // Keep track of which of users labs are in results set
            if (showMyLabsAlways.equals("Y")) {
              myLabMap.remove(idLab);            
            }
            addLabNode(row);
            addAnalysisGroupNode(row);
            if (idAnalysis.intValue() != -2) {
              addAnalysisNode(row);
            }
          } else if (idAnalysisGroup.intValue() != prevIdAnalysisGroup.intValue()) {
            addAnalysisGroupNode(row);
            if (idAnalysis.intValue() != -2) {
              addAnalysisNode(row);
            }
          } else if (idAnalysis.intValue() != prevIdAnalysis.intValue()) {
            if (idAnalysis.intValue() != -2) {
              addAnalysisNode(row);          
            }
          } 

          prevIdAnalysis      = idAnalysis;
          prevIdAnalysisGroup = idAnalysisGroup;
          prevIdLab           = idLab;
          
          if (analysisCount >= MAX_ANALYSIS_COUNT) {
            break;
          }
        }
        
        
        // For those labs that user is member of that do not have any projects,
        // create a lab node in the XML document.
        if (showMyLabsAlways.equals("Y")) {
          for(Iterator i = myLabMap.keySet().iterator(); i.hasNext();) {
            Lab lab = (Lab)myLabMap.get(i.next());
            addLabNode(lab);
          }
        }
        rootNode.setAttribute("analysisCount", Integer.valueOf(analysisCount).toString());
        message = analysisCount == MAX_ANALYSIS_COUNT ? "First " + MAX_ANALYSIS_COUNT + " displayed" : ""; 
        rootNode.setAttribute("message", message);
    }
   
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);

      // Garbage collect
      out = null;
      doc = null;
      rootNode = null;
      results = null;
      System.gc();
      
      
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetAnalysisGroupList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in GetAnalysisGroupList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (Exception e) {
      log.error("An exception has occurred in GetAnalysisGroupList ", e);
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

  
  private void addLabNode(Object[] row) {
    String labName = Lab.formatLabName((String)row[5], (String)row[6]);

    labNode = new Element("Lab");
    labNode.setAttribute("idLab",            ((Integer)row[3]).toString());
    labNode.setAttribute("labName",          labName);
    labNode.setAttribute("label",            labName);
    rootNode.addContent(labNode);
  }

  private void addLabNode(Lab lab) {
    labNode = new Element("Lab");
    labNode.setAttribute("idLab",            lab.getIdLab().toString());
    labNode.setAttribute("labName",          lab.getName());
    labNode.setAttribute("label",            lab.getName());
    rootNode.addContent(labNode);
  }
  
  private void addAnalysisGroupNode(Object[] row) {
    String labName = Lab.formatLabName((String)row[5], (String)row[6]);

    analysisGroupNode = new Element("AnalysisGroup");
    analysisGroupNode.setAttribute("idAnalysisGroup", row[0] == null ? ""  : ((Integer)row[0]).toString());
    analysisGroupNode.setAttribute("name",            row[1] == null ? ""  : (String)row[1]);
    analysisGroupNode.setAttribute("label",           row[1] == null ? ""  : (String)row[1]);
    analysisGroupNode.setAttribute("description",     row[2] == null ? ""  : (String)row[2]);
    
    analysisGroupNode.setAttribute("idLab",           row[3] == null ? "" : ((Integer)row[3]).toString());
    analysisGroupNode.setAttribute("labName",         labName);
    
    labNode.addContent(analysisGroupNode);
  }
  
  private void addAnalysisNode(Object[] row) {
    String labName = Lab.formatLabName( (String)row[5], (String)row[6]);
    
    String aLabName = Lab.formatLabName( (String)row[13], (String)row[14]);

    analysisNode = new Element("Analysis");
    analysisNode.setAttribute("idAnalysis",         row[7] == null ? ""  : ((Integer)row[7]).toString());
    analysisNode.setAttribute("number",             row[8] == null ? ""  : (String)row[8]);
    analysisNode.setAttribute("name",               row[9] == null ? ""  : (String)row[9]);
    analysisNode.setAttribute("label",              row[9] == null ? ""  : (String)row[9]);
    analysisNode.setAttribute("description",        row[10] == null ? ""  : (String)row[10]);
    analysisNode.setAttribute("createDateDisplay",  row[11] == null ? ""  : this.formatDate((java.sql.Date)row[11], this.DATE_OUTPUT_SQL));
    analysisNode.setAttribute("createDate",         row[11] == null ? ""  : this.formatDate((java.sql.Date)row[11], this.DATE_OUTPUT_SLASH));
    analysisNode.setAttribute("idLab",              row[12] == null ? ""  : ((Integer)row[12]).toString());
    analysisNode.setAttribute("labName",            aLabName);    
    analysisNode.setAttribute("idAnalysisType",     row[15] == null ? ""  : ((Integer)row[15]).toString());
    analysisNode.setAttribute("idAnalysisProtocol", row[16] == null ? ""  : ((Integer)row[16]).toString());
    analysisNode.setAttribute("idOrganism",         row[17] == null ? ""  : ((Integer)row[17]).toString());
    analysisNode.setAttribute("idGenomeBuild",      row[18] == null ? ""  : ((Integer)row[18]).toString());
    analysisNode.setAttribute("codeVisibility",     row[19] == null ? ""  : (String)row[19]);    
    analysisNode.setAttribute("idAppUser",          row[22] == null ? ""  : ((Integer)row[22]).toString());    

    String lastName   = row[20] != null ? (String)row[20] : "";
    String firstName  = row[21] != null ? (String)row[21] : "";
    String ownerName = "";
    if (firstName != "") {
      ownerName += firstName;
    }
    if (lastName != null) {
      ownerName += " " + lastName;
    }
    analysisNode.setAttribute ("ownerName", ownerName);
    
    analysisNode.setAttribute("isDirty",                "N");
    analysisNode.setAttribute("isSelected",             "N");
    
    if (analysisNode.getAttributeValue("codeVisibility").equals(Visibility.VISIBLE_TO_PUBLIC)) {
      analysisNode.setAttribute("requestPublicNote",          "(Public) ");
    } else {
      analysisNode.setAttribute("requestPublicNote", "");
    }
    
    String createDate    = this.formatDate((java.sql.Date)row[11]);
    String analysisNumber = (String)row[8];
    String tokens[] = createDate.split("/");
    String createMonth = tokens[0];
    String createDay   = tokens[1];
    String createYear  = tokens[2];
    String sortDate = createYear + createMonth + createDay;
    String key = createYear + "-" + sortDate + "-" + analysisNumber;    
    analysisNode.setAttribute("key", key);

    Integer idLab = (Integer)row[3];
    Integer idAppUser = (Integer)row[22];
    analysisNode.setAttribute("canUpdateVisibility", this.getSecAdvisor().canUpdateVisibility(idLab, idAppUser) ? "Y" : "N");

    analysisGroupNode.addContent(analysisNode);
    
    analysisCount++;
  }
}