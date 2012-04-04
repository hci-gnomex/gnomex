package hci.gnomex.controller;

import hci.dictionary.model.DictionaryEntry;
import hci.dictionary.utility.DictionaryManager;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.lucene.AllObjectsIndexHelper;
import hci.gnomex.lucene.AnalysisIndexHelper;
import hci.gnomex.lucene.ExperimentIndexHelper;
import hci.gnomex.model.Lab;
import hci.gnomex.model.LabFilter;
import hci.gnomex.model.Property;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.PropertyType;
import hci.gnomex.model.SlideProductFilter;
import hci.gnomex.utility.DictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;

public class GetSearchMetaInformation extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SearchIndex.class);
  
  private Map<String, List<DictionaryEntry>> dictionaryMap = new HashMap<String, List<DictionaryEntry>>();
  private List<SearchListEntry> experimentSearchList = new ArrayList<SearchListEntry>();
  private List<SearchListEntry> analysisSearchList = new ArrayList<SearchListEntry>();
  private List<SearchListEntry> dataTrackSearchList = new ArrayList<SearchListEntry>();
  private List<SearchListEntry> protocolSearchList = new ArrayList<SearchListEntry>();
  private List<SearchListEntry> allObjectsSearchList = new ArrayList<SearchListEntry>();
  
  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      
      // All Objects Fields
      addLabEntry(sess, allObjectsSearchList);
      addEntry(sess, "Organism", AllObjectsIndexHelper.ID_ORGANISM, DictionaryManager.getDictionaryEntries("hci.gnomex.model.OrganismLite"), allObjectsSearchList);
      
      // Experiment fields
      addLabEntry(sess, experimentSearchList);
      addEntry(sess, "Organism", AllObjectsIndexHelper.ID_ORGANISM, DictionaryManager.getDictionaryEntries("hci.gnomex.model.OrganismLite"), experimentSearchList);
      addEntry(sess, "Experiment Design", ExperimentIndexHelper.CODE_EXPERIMENT_DESIGNS, "SELECT ed from ExperimentDesign as ed ", experimentSearchList);
      addEntry(sess, "Experiment Factor", ExperimentIndexHelper.CODE_EXPERIMENT_FACTORS, "SELECT ed from ExperimentFactor as ed ", experimentSearchList);
      addEntry(sess, "Experiment Category", ExperimentIndexHelper.CODE_REQUEST_CATEGORY, DictionaryManager.getDictionaryEntries("hci.gnomex.model.RequestCategory"), experimentSearchList);
      addEntry(sess, "Experiment Sample Type", ExperimentIndexHelper.ID_SAMPLE_TYPES, DictionaryManager.getDictionaryEntries("hci.gnomex.model.SampleType"), experimentSearchList);
      addEntry(sess, "Experiment Application", ExperimentIndexHelper.CODE_APPLICATION, DictionaryManager.getDictionaryEntries("hci.gnomex.model.Application"), experimentSearchList);
      SlideProductFilter filter = new SlideProductFilter();
      addEntry(sess, "Experiment Microarray", ExperimentIndexHelper.ID_SLIDE_PRODUCT, filter.getSlideProductList(sess, this.getSecAdvisor()), experimentSearchList);
      // Add experiment properties
      for (Property property : dh.getPropertyList()) {

        if (property.getForSample() == null || !property.getForSample().equals("Y") || property.getIsActive().equals("N")) {
          continue;
        }
        
        addEntry(sess, property, experimentSearchList);
      }
      
      // Analysis Fields
      addLabEntry(sess, analysisSearchList);
      addEntry(sess, "Organism", AllObjectsIndexHelper.ID_ORGANISM, DictionaryManager.getDictionaryEntries("hci.gnomex.model.OrganismLite"), analysisSearchList);
      addEntry(sess, "Analysis Type", AnalysisIndexHelper.ID_ANALYSIS_TYPE, DictionaryManager.getDictionaryEntries("hci.gnomex.model.AnalysisType"), analysisSearchList);
      addEntry(sess, "Analysis Protocol", AnalysisIndexHelper.ID_ANALYSIS_PROTOCOL, DictionaryManager.getDictionaryEntries("hci.gnomex.model.AnalysisProtocol"), analysisSearchList);
      // Add analysis properties
      for (Property property : dh.getPropertyList()) {

        if (property.getForAnalysis() == null || !property.getForAnalysis().equals("Y") || property.getIsActive().equals("N")) {
          continue;
        }
        
        addEntry(sess, property, analysisSearchList);
      }

      // Add data track properties
      addLabEntry(sess, dataTrackSearchList);
      addEntry(sess, "Organism", AllObjectsIndexHelper.ID_ORGANISM, DictionaryManager.getDictionaryEntries("hci.gnomex.model.OrganismLite"), dataTrackSearchList);
      for (Property property : dh.getPropertyList()) {

        if (property.getForDataTrack() == null || !property.getForDataTrack().equals("Y") || property.getIsActive().equals("N")) {
          continue;
        }
        
        addEntry(sess, property, dataTrackSearchList);
      }

      Document xmlDoc = this.buildXMLDocument();
      
      org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(xmlDoc);


    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in GetSearchMetaInformation ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    } catch (Exception e){
      log.error("An exception has occurred in getSearchMetaInformation ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    if (isValid()) {
      setResponsePage(this.SUCCESS_JSP);
    } else {
      setResponsePage(this.ERROR_JSP);
    }

    return this;
  }
  
  private void addEntry(Session sess, Property property, List<SearchListEntry>searchList) {
    String displayName = property.getName();
    String searchName = property.getName().replaceAll("[^A-Za-z0-9]", "");

    if (property.getOptions() != null && property.getOptions().size() > 0) {
      addEntry(sess, displayName, searchName, property.getOptions(), searchList);
    } else if (property.getCodePropertyType() != null && property.getCodePropertyType().equals(PropertyType.CHECKBOX)) {
      ArrayList<CheckBoxDictionaryEntry> cbEntries = new ArrayList<CheckBoxDictionaryEntry>();
      cbEntries.add(new CheckBoxDictionaryEntry(true));
      cbEntries.add(new CheckBoxDictionaryEntry(false));
      addEntry(sess, displayName, searchName, cbEntries, searchList, "Y", "N");
    } else {
      addEntry(sess, displayName, searchName, null, searchList, "N", "N");
    }
  }
  
  private void addEntry(Session sess, String displayName, String searchName, String query, List<SearchListEntry> searchList) throws HibernateException {
    List entries = (List)sess.createQuery(query).list();
    addEntry(sess, displayName, searchName, entries, searchList);
  }
  
  private void addEntry(Session sess, String displayName, String searchName, Iterable entries, List<SearchListEntry> searchList) {
    addEntry(sess, displayName, searchName, entries, searchList, "Y", "Y");
  }
  
  private void addLabEntry(Session sess, List<SearchListEntry> searchList) {
    SearchListEntry entry = new SearchListEntry();
    
    entry.DisplayName = "Group";
    entry.SearchName = AllObjectsIndexHelper.ID_LAB;
    entry.IsOptionChoice = "Y";
    entry.AllowMultipleChoice = "Y";
    searchList.add(entry);

    LabFilter filter = new LabFilter();
    filter.setUnbounded(true);
    StringBuffer queryBuf = filter.getQuery(this.getSecAdvisor());
    List labs = (List)sess.createQuery(queryBuf.toString()).list();
    
    List<DictionaryEntry> designList = new ArrayList<DictionaryEntry>();
    for(Iterator i = labs.iterator(); i.hasNext();) {
      Lab lab = (Lab)i.next();
      GenericDictionaryEntry e = new GenericDictionaryEntry(lab.getIdLab().toString(), lab.getName());
      if (!e.getDisplay().equals("")) {
        designList.add(e);
      }
      dictionaryMap.put("Group", designList);
    }
  }
  
  private void addEntry(Session sess, String displayName, String searchName, Iterable entries, List<SearchListEntry> searchList, String isOptionChoice, String allowMultipleChoice) {
    SearchListEntry entry = new SearchListEntry();
    entry.DisplayName = displayName;
    entry.SearchName = searchName;
    entry.IsOptionChoice = isOptionChoice;
    entry.AllowMultipleChoice = allowMultipleChoice;
    searchList.add(entry);
    
    if (entries != null) {
      List<DictionaryEntry> designList = new ArrayList<DictionaryEntry>();
      for(Iterator i = entries.iterator(); i.hasNext();) {
        DictionaryEntry e = (DictionaryEntry)i.next();
        if (!e.getDisplay().equals("")) {
          designList.add(e);
        }
      }
      dictionaryMap.put(displayName, designList);
    }
  }
  
  private Document buildXMLDocument() {
    Document doc = new Document(new Element("SearchInfo"));

    buildSearchList(doc.getRootElement(), experimentSearchList, "ExperimentSearchList");
    buildSearchList(doc.getRootElement(), analysisSearchList, "AnalysisSearchList");
    buildSearchList(doc.getRootElement(), dataTrackSearchList, "DataTrackSearchList");
    buildSearchList(doc.getRootElement(), protocolSearchList, "ProtocolSearchList");
    buildSearchList(doc.getRootElement(), allObjectsSearchList, "AllObjectsSearchList");
    buildDictionaryMap(doc.getRootElement());
    
    return doc;
  }
  
  private void buildSearchList(Element root, List<SearchListEntry> searchList, String nodeName) {
    Element parent = new Element(nodeName);
    root.addContent(parent);
    
    for(SearchListEntry entry:searchList) {
      Element fieldNode = new Element("Field");
      fieldNode.setAttribute("displayName", entry.DisplayName);
      fieldNode.setAttribute("searchName", entry.SearchName);
      fieldNode.setAttribute("isOptionChoice", entry.IsOptionChoice);
      fieldNode.setAttribute("allowMultipleChoice", entry.AllowMultipleChoice);
      fieldNode.setAttribute("value", "");
      parent.addContent(fieldNode);
    }
  }
  
  private void buildDictionaryMap(Element root) {
    Element parent = new Element("DictionaryMap");
    root.addContent(parent);

    for (String fieldName:dictionaryMap.keySet()) {
      Element dictNode = new Element("Dictionary");
      dictNode.setAttribute("fieldName", fieldName);
      parent.addContent(dictNode);
      List<DictionaryEntry> list = dictionaryMap.get(fieldName);
      for(DictionaryEntry entry : list) {
        Element entryNode = new Element("DictionaryEntry");
        dictNode.addContent(entryNode);
        entryNode.setAttribute("display", entry.getDisplay());
        entryNode.setAttribute("value", entry.getValue());
      }
    }
  }
 
  private class SearchListEntry implements Serializable {
    public String DisplayName;
    public String SearchName;
    public String IsOptionChoice;
    public String AllowMultipleChoice;
  }
  
  private class CheckBoxDictionaryEntry extends DictionaryEntry implements Serializable {
    private Boolean isYes;
    
    public CheckBoxDictionaryEntry(Boolean isYes) {
      this.isYes = isYes;
    }
    
    public String getValue() {
      if (isYes) return "Y";
      else return "N";
    }
    
    public String getDisplay() {
      if (isYes) return "Yes";
      else return "No";
    }
  }
  
  private class GenericDictionaryEntry extends DictionaryEntry implements Serializable {
    private String value;
    private String display;
    
    public GenericDictionaryEntry(String v, String d) {
      value = v;
      display = d;
    }
    
    public String getValue() {
      return value;
    }
    
    public String getDisplay() {
      return display;
    }
  }
}
