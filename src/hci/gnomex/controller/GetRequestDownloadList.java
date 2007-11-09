package hci.gnomex.controller;

import hci.gnomex.model.RequestDownloadFilter;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetRequestDownloadList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequestDownloadList.class);
  
  private RequestDownloadFilter          filter;
  private HashMap                        slideDesignMap = new HashMap();
  private static final String          QUALITY_CONTROL_DIRECTORY = "bioanalysis";

  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new RequestDownloadFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    String idRequestStringList = request.getParameter("idRequestStringList");
    if (idRequestStringList != null&& !idRequestStringList.equals("")) {
      List idRequests = new ArrayList();
      String[] keys = idRequestStringList.split(":");
      for (int i = 0; i < keys.length; i++) {
        String idRequest = keys[i];
        idRequests.add(new Integer(idRequest));
      }
      filter.setIdRequests(idRequests);
    }
    

    
    if  (!filter.hasCriteria()) {
      this.addInvalidField("filterRequired", "Please enter at least one search criterion.");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      List slideDesigns = sess.createQuery("SELECT sd from SlideDesign sd ").list();
      for(Iterator i = slideDesigns.iterator(); i.hasNext();) {
        SlideDesign sd = (SlideDesign)i.next();
        slideDesignMap.put(sd.getIdSlideDesign(), sd.getName());
      }
      
      
      
    
      StringBuffer buf = filter.getMicroarrayResultQuery(this.getSecAdvisor());
      log.debug("Query for GetRequestDownloadList (1): " + buf.toString());
      List rows1 = (List)sess.createQuery(buf.toString()).list();
      TreeMap rowMap = new TreeMap(new HybSampleComparator());
      for(Iterator i = rows1.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber = (String)row[1];
        String hybNumber     = row[5] == null || row[5].equals("") ? "" : (String)row[5];
        
        String createDate    = this.formatDate((java.sql.Date)row[0]);
        String tokens[] = createDate.split("/");
        String createMonth = tokens[0];
        String createDay   = tokens[1];
        String createYear  = tokens[2];
        String sortDate = createYear + createMonth + createDay;
        
        String key = createYear + "-" + sortDate + "-" + requestNumber + "-" + hybNumber;
        
        rowMap.put(key, row);
      }
      
      buf = filter.getQualityControlResultQuery(this.getSecAdvisor());
      log.debug("Query for GetRequestDownloadList (2): " + buf.toString());
      List rows2 = (List)sess.createQuery(buf.toString()).list();
      for(Iterator i = rows2.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber = (String)row[1];
        String sampleNumber     = row[11] == null || row[11].equals("") ? "" : (String)row[11];

        String createDate    = this.formatDate((java.sql.Date)row[0]);
        String tokens[] = createDate.split("/");
        String createMonth = tokens[0];
        String createDay   = tokens[1];
        String createYear  = tokens[2];
        String sortDate = createYear + createMonth + createDay;

        
        String key = createYear + "-" + sortDate + "-" + requestNumber + "-" + this.QUALITY_CONTROL_DIRECTORY;
        
        rowMap.put(key, row);
      }
      
      boolean alt = false;
      String prevRequestNumber = "";
      
      
    
      Document doc = new Document(new Element("RequestDownloadList"));
      for(Iterator i = rowMap.keySet().iterator(); i.hasNext();) {
        String key = (String)i.next();
        Object[] row = (Object[])rowMap.get(key);
        
        String requestNumber = (String)row[1];
        if (!requestNumber.equals(prevRequestNumber)) {
          alt = !alt;
        }
        

        
        Element n = new Element("RequestDownload");
        n.setAttribute("key", key);
        n.setAttribute("isSelected", "N");
        n.setAttribute("altColor", new Boolean(alt).toString());
        n.setAttribute("idRequest", row[21].toString());
        n.setAttribute("createDate", this.formatDate((java.sql.Date)row[0]));
        n.setAttribute("requestNumber", (String)row[1]);
        n.setAttribute("codeRequestCategory", row[2] == null ? "" : (String)row[2]);
        n.setAttribute("codeMicroarrayCategory", row[3] == null ? "" : (String)row[3]);
        n.setAttribute("idAppUser", row[4] == null ? "" : ((Integer)row[4]).toString());
        n.setAttribute("hybNumber", row[5] == null ? "" : (String)row[5]);
        n.setAttribute("hybDate", row[6] == null || row[6].equals("") ? "" : this.formatDate((java.sql.Date)row[6]));
        n.setAttribute("extractionDate", row[7] == null || row[7].equals("") ? "" : this.formatDate((java.sql.Date)row[7]));
        n.setAttribute("hybFailed", row[8] == null ? "" : (String)row[8]);
        n.setAttribute("labelingDateSample1", row[9] == null || row[9].equals("")? "" : this.formatDate((java.sql.Date)row[9]));
        n.setAttribute("qualDateSample1", row[10] == null || row[10].equals("")? "" : this.formatDate((java.sql.Date)row[10]));
        n.setAttribute("numberSample1", row[11] == null ? "" :  (String)row[11]);
        n.setAttribute("nameSample1", row[12] == null ? "" :  (String)row[12]);
        n.setAttribute("labelingDateSample2", row[13] == null || row[13].equals("") ? "" : this.formatDate((java.sql.Date)row[13]));
        n.setAttribute("qualDateSample2", row[14] == null || row[14].equals("") ? "" : this.formatDate((java.sql.Date)row[14]));
        n.setAttribute("numberSample2", row[15] == null ? "" :  (String)row[15]);
        n.setAttribute("nameSample2", row[16] == null ? "" :  (String)row[16]);
        n.setAttribute("idLab", row[17] == null ? "" : ((Integer)row[17]).toString());
        
        
        Integer idSlideDesign = row[20] == null || row[20].equals("") ? null : (Integer)row[20];
        
        String  sample1QualFailed             = row[22] == null || row[22].equals("") ? "N" : (String)row[22];
        String  sample2QualFailed             = row[23] == null || row[23].equals("") ? "N" : (String)row[23];
        String  labeledSample1LabelingFailed  = row[24] == null || row[24].equals("") ? "N" : (String)row[24];
        String  labeledSample2LabelingFailed  = row[25] == null || row[25].equals("") ? "N" : (String)row[25];
        String  extractionFailed              = row[26] == null || row[26].equals("") ? "N" : (String)row[26];
        String  extractionBypassed            = row[27] == null || row[27].equals("") ? "N" : (String)row[27];

        n.setAttribute("ownerFirstName", row[28] == null ? "" :  (String)row[28]);
        n.setAttribute("ownerLastName",  row[29] == null ? "" :  (String)row[29]);
        
        if (idSlideDesign == null) {
          n.setAttribute("results", "bioanalyzer");
        } else {
          n.setAttribute("results", (String)slideDesignMap.get(idSlideDesign));
        }

        if (n.getAttributeValue("results").equals("bioanalyzer")) {
          boolean hasMaxQualDate = false;
          if (row[19] != null && !row[19].equals("")) {
            hasMaxQualDate = true;
          }
          if(hasMaxQualDate) {
            n.setAttribute("hasResults","Y");                       
          } else {
            n.setAttribute("status", "in progress");            
            n.setAttribute("hasResults","N");
          }
        } else {
          if(!n.getAttributeValue("extractionDate").equals("")) {
            n.setAttribute("hasResults","Y");                       
          } else if(extractionBypassed.equals("Y")) {
            n.setAttribute("status", "bypassed scan/fe");          
            n.setAttribute("hasResults","Y");                       
          } else if(extractionFailed.equals("Y")) {
            n.setAttribute("status", "failed scan/fe");          
            n.setAttribute("hasResults","N");                       
          } else  if (n.getAttributeValue("hybFailed").equals("Y")){
            n.setAttribute("status", "failed hyb");          
            n.setAttribute("hasResults","N");                       
          } else  if (sample1QualFailed.equals("Y") || sample2QualFailed.equals("Y")){
            n.setAttribute("status", "failed QC");          
            n.setAttribute("hasResults","N");                       
          } else  if (labeledSample1LabelingFailed.equals("Y") || labeledSample2LabelingFailed.equals("Y")){
            n.setAttribute("status", "failed labeling");          
            n.setAttribute("hasResults","N");                       
          } else {
            n.setAttribute("status", "in progress");          
            n.setAttribute("hasResults","N");                       
          }          
        }

        doc.getRootElement().addContent(n);
        
        prevRequestNumber = requestNumber;
        
      }
    
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
    
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetRequestDownloadList ", e);
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

  public static class  HybSampleComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split("-");
      String[] tokens2 = key2.split("-");
      
      String yr1           = tokens1[0];
      String date1         = tokens1[1];
      String reqNumber1    = tokens1[2];
      String hybNumber1    = tokens1[3];
      
      
      String yr2           = tokens2[0];
      String date2         = tokens2[1];
      String reqNumber2    = tokens2[2];
      String hybNumber2    = tokens2[3];
      
      
      String number1 = null;
      
      if (hybNumber1.equals(QUALITY_CONTROL_DIRECTORY)) {
        number1 = "0";
         
      } else {
        String[] hybNumberTokens1 = hybNumber1.split("E");
        number1 = hybNumberTokens1[hybNumberTokens1.length - 1];        
      }
      
      
      String number2 = null;
      
      
      if (hybNumber2.equals(QUALITY_CONTROL_DIRECTORY)) {
        number2 = "0";
          
      } else {
        String[] hybNumberTokens2 = hybNumber2.split("E");
        number2 = hybNumberTokens2[hybNumberTokens2.length - 1];        
      }


      if (date1.equals(date2)) {
        if (reqNumber1.equals(reqNumber2)) {
          return new Integer(number1).compareTo(new Integer(number2));        
        } else {
          return reqNumber2.compareTo(reqNumber1);
        }  
      } else {
        return date2.compareTo(date1);
      }
              
      
      
    }
  }
  
  
}