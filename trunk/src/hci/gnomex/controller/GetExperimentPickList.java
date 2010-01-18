package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ExperimentPickListFilter;
import hci.gnomex.model.SeqRunType;
import hci.gnomex.model.NumberSequencingCycles;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.SampleType;
import hci.gnomex.model.SlideDesign;

import java.io.Serializable;
import java.sql.SQLException;
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


public class GetExperimentPickList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetExperimentPickList.class);
  
  private ExperimentPickListFilter       filter;
  private HashMap                        slideDesignMap = new HashMap();
  private HashMap                        seqRunTypeMap = new HashMap();
  private HashMap                        sampleTypeMap = new HashMap();
  private HashMap                        numberSeqCyclesMap = new HashMap();
  
  private HashMap                        requestSeqRunTypeMap = new HashMap();
  private HashMap                        requestSampleTypeMap = new HashMap();

  private Element                        rootNode = null;
  private Element                        projectNode = null;
  private Element                        requestNode = null;
  private Element                        itemNode = null;
  
  private static final String          KEY_DELIM = "&-&-&";
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new ExperimentPickListFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
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
      
      List seqRunTypes = sess.createQuery("SELECT fct from SeqRunType fct ").list();
      for(Iterator i = seqRunTypes.iterator(); i.hasNext();) {
        SeqRunType fct = (SeqRunType)i.next();
        seqRunTypeMap.put(fct.getIdSeqRunType(), fct.getSeqRunType());
      }
      List sampleTypes = sess.createQuery("SELECT st from SampleType st ").list();
      for(Iterator i = sampleTypes.iterator(); i.hasNext();) {
        SampleType st = (SampleType)i.next();
        sampleTypeMap.put(st.getIdSampleType(), st.getSampleType());
      }
      List numberSeqCycles = sess.createQuery("SELECT sc from NumberSequencingCycles sc ").list();
      for(Iterator i = numberSeqCycles.iterator(); i.hasNext();) {
        NumberSequencingCycles sc = (NumberSequencingCycles)i.next();
        numberSeqCyclesMap.put(sc.getIdNumberSequencingCycles(), sc.getNumberSequencingCycles());
      }      
      
      TreeMap projectMap = new TreeMap();
      
    
      StringBuffer buf = filter.getMicroarrayQuery(this.getSecAdvisor());
      log.debug("Query for GetExperimentPickList (1): " + buf.toString());
      List rows1 = (List)sess.createQuery(buf.toString()).list();
      TreeMap rowMap = new TreeMap(new HybLaneComparator());
      for(Iterator i = rows1.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String projectName   = (String)row[0];
        String requestNumber = (String)row[3];
        String hybNumber     = row[10] == null || row[10].equals("") ? "" : (String)row[10];
        
        String createDate    = this.formatDate((java.sql.Date)row[2]);
        String tokens[] = createDate.split("/");
        String createMonth = tokens[0];
        String createDay   = tokens[1];
        String createYear  = tokens[2];
        String sortDate = createYear + createMonth + createDay;
        
        String key = projectName + KEY_DELIM + createYear + KEY_DELIM + sortDate + KEY_DELIM + requestNumber + KEY_DELIM + hybNumber;
        
        rowMap.put(key, row);
      }

      buf = filter.getSolexaQuery(this.getSecAdvisor());
      log.debug("Query for GetExperimentPickList (2): " + buf.toString());
      List rows2 = (List)sess.createQuery(buf.toString()).list();
      for(Iterator i = rows2.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String projectName   = (String)row[0];
        String requestNumber = (String)row[3];
        String laneNumber     = row[10] == null || row[10].equals("") ? "" : (String)row[10];
        
        String createDate    = this.formatDate((java.sql.Date)row[2]);
        String tokens[] = createDate.split("/");
        String createMonth = tokens[0];
        String createDay   = tokens[1];
        String createYear  = tokens[2];
        String sortDate = createYear + createMonth + createDay;
        
        String key = projectName + KEY_DELIM + createYear + KEY_DELIM + sortDate + KEY_DELIM + requestNumber + KEY_DELIM + laneNumber;
        
        rowMap.put(key, row);
      }
      

 
      
    
      Document doc = new Document(new Element("AnalysisExperimentPickList"));
      String prevProjectName  = "";
      Integer prevIdRequest  = new Integer(-1);
      
      rootNode = doc.getRootElement();
      for(Iterator i = rowMap.keySet().iterator(); i.hasNext();) {
        String key = (String)i.next();
        Object[] row = (Object[])rowMap.get(key);
        
        
        String  projectName = (String)row[0];
        Integer idRequest = row[1] == null ? new Integer(-2) : (Integer)row[1];
        
        Element n = null;
        if (!projectName.equals(prevProjectName)) {
          addProjectNode(row);
          if (idRequest.intValue() != -2) {
            addRequestNode(row);          
            addItemNode(row);
          }
        } else if (idRequest.intValue() != prevIdRequest.intValue()) {
          if (idRequest.intValue() != -2) {
            addRequestNode(row);          
            addItemNode(row);
          }
        } else {
          if (idRequest.intValue() != -2) {
            addItemNode(row);
          }
        }

        prevIdRequest = idRequest;
        prevProjectName = projectName;
      }
      
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
    
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetExperimentPickList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetExperimentPickList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetExperimentPickList ", e);
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
  
  private void addProjectNode(Object[] row) {
    projectNode = new Element("Project");
    projectNode.setAttribute("projectName",            row[0] == null ? ""  : (String)row[0]);
    projectNode.setAttribute("label",                  row[0] == null ? ""  : (String)row[0]);
    
    rootNode.addContent(projectNode);
  }
  
  private void addRequestNode(Object[] row) {
    requestNode = new Element("Request");
    requestNode.setAttribute("idRequest",              row[1] == null ? ""  : ((Integer)row[1]).toString());
    requestNode.setAttribute("createDate",             row[2] == null ? ""  : this.formatDate((java.sql.Date)row[2], this.DATE_OUTPUT_ALTIO));
    requestNode.setAttribute("createDateDisplay",      row[2] == null ? ""  : this.formatDate((java.sql.Date)row[2], this.DATE_OUTPUT_SQL));
    requestNode.setAttribute("number",                 row[3] == null ? ""  : (String)row[3]);
    requestNode.setAttribute("codeRequestCategory",    row[4] == null ? "" : ((String)row[4]).toString());
    requestNode.setAttribute("codeApplication", row[5] == null ? "" : ((String)row[5]).toString());
    requestNode.setAttribute("slideProduct",           row[6] == null ? ""  : ((String)row[6]).toString());
    requestNode.setAttribute("isSlideSet",             row[7] == null ? ""  : ((String)row[7]).toString());
    requestNode.setAttribute("ownerFirstName",         row[8] == null ? "" : (String)row[8]);
    requestNode.setAttribute("ownerLastName",          row[9] == null ? "" : (String)row[9]);
    
    if (requestNode.getAttributeValue("codeRequestCategory").equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
      requestNode.setAttribute("label", requestNode.getAttributeValue("number") + " - " + requestNode.getAttributeValue("createDateDisplay"));
    } else {
      requestNode.setAttribute("label", requestNode.getAttributeValue("number") + " - " + requestNode.getAttributeValue("createDateDisplay") + " - " + requestNode.getAttributeValue("slideProduct"));
    }
    
    projectNode.addContent(requestNode);
    
    this.requestSeqRunTypeMap = new HashMap();
    this.requestSampleTypeMap = new HashMap();
  }

  private void addItemNode(Object[] row) {
    itemNode = new Element("Item");
    itemNode.setAttribute("idRequest",                row[1] == null ? ""  : ((Integer)row[1]).toString());
    itemNode.setAttribute("itemNumber",               row[10] == null ? ""  : (String)row[10]);
    itemNode.setAttribute("idSlideDesign",            row[11] == null ? ""  : ((Integer)row[11]).toString());
    itemNode.setAttribute("idNumberSequencingCycles", row[12] == null ? ""  : ((Integer)row[12]).toString());
    itemNode.setAttribute("idSeqRunType",           row[13] == null ? ""  : ((Integer)row[13]).toString());
    itemNode.setAttribute("sampleNumber1",            row[14] == null ? ""  : (String)row[14]);
    itemNode.setAttribute("sampleName1",              row[15] == null ? ""  : (String)row[15]);
    itemNode.setAttribute("sampleNumber2",            row[16] == null ? ""  : (String)row[16]);
    itemNode.setAttribute("sampleName2",              row[17] == null ? ""  : (String)row[17]);
    itemNode.setAttribute("idGenomeBuildAlignTo",     row[19] == null ? ""  : ((Integer)row[19]).toString());
    itemNode.setAttribute("analysisInstructions",     row[20] == null ? ""  : (String)row[20]);
    itemNode.setAttribute("flowCellChannelNumber",    row[21] == null ? ""  : ((Integer)row[21]).toString());
    itemNode.setAttribute("flowCellNumber",           row[22] == null ? ""  : (String)row[22]);

    
    
    
    
    Integer idSlideDesign = (Integer)row[11];
    if (idSlideDesign != null && idSlideDesign.intValue() != -1) {
      String slideDesignName = (String)this.slideDesignMap.get(idSlideDesign);
      itemNode.setAttribute("slideDesign", slideDesignName);      
    }
    
    Integer idNumberSequencingCycles = (Integer)row[12];
    if (idNumberSequencingCycles.intValue() != -1) {
      Integer numberSeqCycles = (Integer)this.numberSeqCyclesMap.get(idNumberSequencingCycles);
      itemNode.setAttribute("numberSequencingCycles", numberSeqCycles.toString());      
    }    

    Integer idSeqRunType = (Integer)row[13];
    if (idSeqRunType.intValue() != -1) {
      String seqRunType = (String)this.seqRunTypeMap.get(idSeqRunType);
      itemNode.setAttribute("seqRunType", seqRunType);      
      this.requestSeqRunTypeMap.put(seqRunType, null);
    }    

    Integer idSampleType = (Integer)row[18];
    if (idSampleType.intValue() != -1) {
      String sampleType = (String)this.sampleTypeMap.get(idSampleType);
      itemNode.setAttribute("sampleType", sampleType);      
      this.requestSampleTypeMap.put(sampleType, null);
    }  
    

    StringBuffer label = new StringBuffer(itemNode.getAttributeValue("itemNumber"));
    if (requestNode.getAttributeValue("codeRequestCategory").equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
      label.append(" -  ");
      label.append(itemNode.getAttributeValue("sampleName1"));
      requestNode.setAttribute("seqRunType", itemNode.getAttributeValue("seqRunType"));
    } else {
      label.append(" - ");
      label.append(itemNode.getAttributeValue("sampleName1"));
      if (!itemNode.getAttributeValue("sampleName2").equals("")) {
        label.append(", ");
        label.append(itemNode.getAttributeValue("sampleName2"));
        if (requestNode.getAttributeValue("isSlideSet") != null &&
            requestNode.getAttributeValue("isSlideSet").equals("Y")) {
          label.append(" - ");
          label.append(itemNode.getAttributeValue("slideDesign"));
        }
      }
    }
    itemNode.setAttribute("label", label.toString());

    // Set the solexa request label to the concatenation of sample types and flow cell types
    if (requestNode.getAttributeValue("codeRequestCategory").equals(RequestCategory.SOLEXA_REQUEST_CATEGORY)) {
      StringBuffer buf = new StringBuffer();
      for (Iterator i = requestSeqRunTypeMap.keySet().iterator(); i.hasNext();) {
        buf.append(i.next());
        if (i.hasNext()) {
          buf.append(", ");
        } else {
          buf.append(" - ");
        }
      }
      for (Iterator i = requestSampleTypeMap.keySet().iterator(); i.hasNext();) {
        buf.append(i.next());
        if (i.hasNext()) {
          buf.append(", ");
        }
      }
      requestNode.setAttribute("label", requestNode.getAttributeValue("number") + " - " + requestNode.getAttributeValue("createDateDisplay") + " - " + buf.toString());
      itemNode.setAttribute("type", "SequenceLane");
      itemNode.setAttribute("idSequenceLane", ((Integer)row[23]).toString());
    } else {
      itemNode.setAttribute("type", "Hybridization");
      itemNode.setAttribute("idHybridization", ((Integer)row[23]).toString());
    }
    
    
    requestNode.addContent(itemNode);

  }
  
  public static class  HybLaneComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split(KEY_DELIM);
      String[] tokens2 = key2.split(KEY_DELIM);
      
      String proj1         = tokens1[0];
      String yr1           = tokens1[1];
      String date1         = tokens1[2];
      String reqNumber1    = tokens1[3];
      String hybNumber1    = tokens1[4];
      
      
      String proj2         = tokens2[0];
      String yr2           = tokens2[1];
      String date2         = tokens2[2];
      String reqNumber2    = tokens2[3];
      String hybNumber2    = tokens2[4];
      
      
      String itemNumber1 = null;
      String seq1 = null;
      
      String splitLetter = "";
      if (hybNumber1.indexOf("E") >= 0) {
        splitLetter = "E";
      } else if (hybNumber1.indexOf("L") >= 0) {
        splitLetter = "L";
      } else if (hybNumber1.indexOf("F") >= 0) {
        splitLetter = "F";
      } 
      String[] hybNumberTokens1 = hybNumber1.split(splitLetter);
      itemNumber1 = hybNumberTokens1[hybNumberTokens1.length - 1];     
      
      if (splitLetter.equals("F")) {
        String[] numberTokens  = itemNumber1.split("_");
        itemNumber1            = numberTokens[0];
        seq1                   = numberTokens[1];                
      } else {
        seq1 = "0";
      }
      
      
      String itemNumber2 = null;
      String seq2 = null;
      splitLetter = "";
      if (hybNumber2.indexOf("E") >= 0) {
        splitLetter = "E";
      } else if (hybNumber2.indexOf("L") >= 0) {
        splitLetter = "L";
      } else if (hybNumber2.indexOf("F") >= 0) {
        splitLetter = "F";
      } 

      String[] hybNumberTokens2 = hybNumber2.split(splitLetter);
      itemNumber2 = hybNumberTokens2[hybNumberTokens2.length - 1];     
      if (splitLetter.equals("F")) {
        String[] numberTokens  = itemNumber2.split("_");
        itemNumber2            = numberTokens[0];
        seq2                   = numberTokens[1];                
      } else {
        seq2 = "0";
      }
     
     

      if (proj1.equals(proj2)) {
        if (date1.equals(date2)) {
          if (reqNumber1.equals(reqNumber2)) {
            if (itemNumber1.equals(itemNumber2)) {
              return new Integer(seq1).compareTo(new Integer(seq2));
            } else {
              return new Integer(itemNumber1).compareTo(new Integer(itemNumber2));                      
            }
          } else {
            return reqNumber2.compareTo(reqNumber1);
          }  
        } else {
          return date2.compareTo(date1);
        }        
      } else {
        return proj1.compareTo(proj2);
      }
              
      
      
    }
  }
  
  
}