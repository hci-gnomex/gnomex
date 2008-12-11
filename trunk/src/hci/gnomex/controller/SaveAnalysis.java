package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisExperimentItem;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisFileParser;
import hci.gnomex.utility.AnalysisGroupParser;
import hci.gnomex.utility.AnalysisHybParser;
import hci.gnomex.utility.AnalysisLaneParser;
import hci.gnomex.utility.HibernateSession;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;




public class SaveAnalysis extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveLab.class);
  
  private String                baseDir;
  
  private String                analysisGroupsXMLString;
  private Document              analysisGroupsDoc;
  private AnalysisGroupParser   analysisGroupParser;

  private String                analysisFilesXMLString;
  private Document              analysisFilesDoc;
  private AnalysisFileParser    analysisFileParser;
  
  private String                hybsXMLString;
  private Document              hybsDoc;
  private AnalysisHybParser     hybParser;

  private String                lanesXMLString;
  private Document              lanesDoc;
  private AnalysisLaneParser    laneParser;

  private Analysis               analysisScreen;
  private boolean              isNewAnalysis = false;
  private boolean              isNewAnalysisGroup = false;
  
  private String                newAnalysisGroupName;
  private String                newAnalysisGroupDescription;
  private Integer               newAnalysisGroupId = new Integer(-1);
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    baseDir = Constants.getAnalysisDirectory(request.getServerName());

    analysisScreen = new Analysis();
    HashMap errors = this.loadDetailObject(request, analysisScreen);
    this.addInvalidFields(errors);
    if (analysisScreen.getIdAnalysis() == null || analysisScreen.getIdAnalysis().intValue() == 0) {
      isNewAnalysis = true;
    }
    
    
    if (request.getParameter("analysisGroupsXMLString") != null && !request.getParameter("analysisGroupsXMLString").equals("")) {
      analysisGroupsXMLString = request.getParameter("analysisGroupsXMLString");
    }
    
    StringReader reader = new StringReader(analysisGroupsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      analysisGroupsDoc = sax.build(reader);
      analysisGroupParser = new AnalysisGroupParser(analysisGroupsDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse analysisGroupsXMLString", je );
      this.addInvalidField( "analysisGroupsXMLString", "Invalid analysisGroupsXMLString");
    }
    
    
    if (request.getParameter("analysisFilesXMLString") != null && !request.getParameter("analysisFilesXMLString").equals("")) {
      analysisFilesXMLString = request.getParameter("analysisFilesXMLString");
    }
    
    reader = new StringReader(analysisFilesXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      analysisFilesDoc = sax.build(reader);
      analysisFileParser = new AnalysisFileParser(analysisFilesDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse analysisFilesXMLString", je );
      this.addInvalidField( "analysisFilesXMLString", "Invalid analysisFilesXMLString");
    }
    
    if (request.getParameter("hybsXMLString") != null && !request.getParameter("hybsXMLString").equals("")) {
      hybsXMLString = request.getParameter("hybsXMLString");
    }
    
    reader = new StringReader(hybsXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      hybsDoc = sax.build(reader);
      hybParser = new AnalysisHybParser(hybsDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse hybsXMLString", je );
      this.addInvalidField( "hybsXMLString", "Invalid hybsXMLString");
    }

    if (request.getParameter("lanesXMLString") != null && !request.getParameter("lanesXMLString").equals("")) {
      lanesXMLString = request.getParameter("lanesXMLString");
    }
    
    reader = new StringReader(lanesXMLString);
    try {
      SAXBuilder sax = new SAXBuilder();
      lanesDoc = sax.build(reader);
      laneParser = new AnalysisLaneParser(lanesDoc);
    } catch (JDOMException je ) {
      log.error( "Cannot parse lanesXMLString", je );
      this.addInvalidField( "lanesXMLString", "Invalid lanesXMLString");
    }
    
    if (request.getParameter("newAnalysisGroupName") != null && !request.getParameter("newAnalysisGroupName").equals("")) {
      newAnalysisGroupName = request.getParameter("newAnalysisGroupName");
      isNewAnalysisGroup = true;
    }    
    if (request.getParameter("newAnalysisGroupDescription") != null && !request.getParameter("newAnalysisGroupDescription").equals("")) {
      newAnalysisGroupDescription = request.getParameter("newAnalysisGroupDescription");
    }    
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      Analysis analysis = null;
      if (isNewAnalysis) {
        analysis = analysisScreen;
        analysis.setIdAppUser(this.getSecAdvisor().getIdAppUser());        
      } else {
        analysis = (Analysis)sess.load(Analysis.class, analysisScreen.getIdAnalysis());        
      }
      
      if (this.getSecurityAdvisor().canUpdate(analysis)) {
        analysisGroupParser.parse(sess);
        analysisFileParser.parse(sess);
        hybParser.parse(sess);
        laneParser.parse(sess);
        
              
        if (isNewAnalysis) {
          sess.save(analysis);
          
          if (isNewAnalysisGroup) {
            AnalysisGroup newAnalysisGroup = new AnalysisGroup();
            newAnalysisGroup.setIdLab(analysisScreen.getIdLab());
            newAnalysisGroup.setName(newAnalysisGroupName);
            newAnalysisGroup.setDescription(newAnalysisGroupDescription);
            sess.save(newAnalysisGroup);
            newAnalysisGroupId = newAnalysisGroup.getIdAnalysisGroup();

            TreeSet analysisGroups = new TreeSet(new AnalysisGroupComparator());
            analysisGroups.add(newAnalysisGroup);
            analysis.setAnalysisGroups(analysisGroups);            
          }
          
          analysis.setNumber("A" + analysis.getIdAnalysis().toString());
          analysis.setCodeVisibility(Visibility.VISIBLE_TO_GROUP_MEMBERS);
          analysis.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
          sess.save(analysis);

          
          
        } else {
          initializeAnalysis(analysis);
        }


        //
        // Save analysis groups
        //
        if (!isNewAnalysisGroup) {
          TreeSet analysisGroups = new TreeSet(new AnalysisGroupComparator());
          // If analysis group wasn't provided, create a default one
          if (analysisGroupParser.getAnalysisGroupMap().isEmpty()) {
            AnalysisGroup defaultAnalysisGroup = new AnalysisGroup();
            defaultAnalysisGroup.setName(analysis.getName());
            defaultAnalysisGroup.setIdLab(analysisScreen.getIdLab());
            defaultAnalysisGroup.setIdAppUser(this.getSecAdvisor().getIdAppUser());
            sess.save(defaultAnalysisGroup);
            
            newAnalysisGroupId = defaultAnalysisGroup.getIdAnalysisGroup();
            
            analysisGroups.add(defaultAnalysisGroup);
          } else {
            // Relate the analysis to the specified analysis groups
            for(Iterator i = analysisGroupParser.getAnalysisGroupMap().keySet().iterator(); i.hasNext();) {
              String idAnalysisGroupString = (String)i.next();
              AnalysisGroup ag = (AnalysisGroup)analysisGroupParser.getAnalysisGroupMap().get(idAnalysisGroupString);
              analysisGroups.add(ag);
            }
          }
          analysis.setAnalysisGroups(analysisGroups);          
        }
        
        sess.flush();
        
        //
        // Get rid of removed experiment items files
        //
        ArrayList experimentItemsToRemove = new ArrayList();
        if (!isNewAnalysisGroup) {
          ArrayList filesToRemove = new ArrayList();
          for (Iterator i = analysis.getExperimentItems().iterator(); i.hasNext();) {
            AnalysisExperimentItem ex = (AnalysisExperimentItem) i.next();
            boolean found = false;
            for(Iterator i1 = hybParser.getIdHybridizations().iterator(); i1.hasNext();) {
              Integer idHybridization = (Integer)i1.next();
              if (idHybridization.equals(ex.getIdHybridization())) {
                found = true;
                break;
              }
            }
            if (!found) {
              for(Iterator i1 = laneParser.getIdSequenceLanes().iterator(); i1.hasNext();) {
                Integer idSequenceLane = (Integer)i1.next();
                if (idSequenceLane.equals(ex.getIdSequenceLane())) {
                  found = true;
                  break;
                }
              }
              
            }
            if (!found) {
              experimentItemsToRemove.add(ex);
            }
          }
          for (Iterator i = experimentItemsToRemove.iterator(); i.hasNext();) {
            AnalysisExperimentItem ex = (AnalysisExperimentItem) i.next();
            sess.delete(ex);
            analysis.getExperimentItems().remove(ex);
          }
        }
        
        //
        // Save experiment items        
        //
        TreeSet experimentItems = new TreeSet(new AnalysisExperimentItemComparator());
        for(Iterator i = hybParser.getIdHybridizations().iterator(); i.hasNext();) {
          Integer idHybridization = (Integer)i.next();
          AnalysisExperimentItem experimentItem = new AnalysisExperimentItem();
          experimentItem.setIdAnalysis(analysis.getIdAnalysis());
          experimentItem.setIdHybridization(idHybridization);
          experimentItem.setIdRequest(hybParser.getIdRequest(idHybridization));
          experimentItems.add(experimentItem);
        }
        for(Iterator i = laneParser.getIdSequenceLanes().iterator(); i.hasNext();) {
          Integer idSequenceLane = (Integer)i.next();
          AnalysisExperimentItem experimentItem = new AnalysisExperimentItem();
          experimentItem.setIdAnalysis(analysis.getIdAnalysis());
          experimentItem.setIdSequenceLane(idSequenceLane);
          experimentItem.setIdRequest(laneParser.getIdRequest(idSequenceLane));
          experimentItems.add(experimentItem);
        }
        analysis.setExperimentItems(experimentItems);
        
        sess.flush();        

        //
        // Save analysis files
        //
        if (!isNewAnalysisGroup) {
          for(Iterator i = analysisFileParser.getAnalysisFileMap().keySet().iterator(); i.hasNext();) {
            String idAnalysisFileString = (String)i.next();
            AnalysisFile af = (AnalysisFile)analysisFileParser.getAnalysisFileMap().get(idAnalysisFileString);
            sess.save(af);
          }
        }
        // Get rid of removed analysis files
        ArrayList filesToRemove = new ArrayList();
        for(Iterator i = analysis.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          boolean found = false;
          for(Iterator i1 = analysisFileParser.getAnalysisFileMap().values().iterator(); i1.hasNext();) {
            AnalysisFile afAdded = (AnalysisFile)i1.next();
            if (afAdded.getFileName().equals(af.getFileName())) {
              found = true;
              break;
            }
          }
          if (!found) {
            filesToRemove.add(af);
          }
        }
        for(Iterator i = filesToRemove.iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          sess.delete(af);
          analysis.getFiles().remove(af);

          removeAnalysisFileFromFileSystem(baseDir, analysis, af);
        }
        
        sess.flush();
        
        
        this.xmlResult = "<SUCCESS idAnalysis=\"" + analysis.getIdAnalysis() + "\"" +  " idAnalysisGroup=\"" + newAnalysisGroupId + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save analysis.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveLab ", e);
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
  
  private void initializeAnalysis(Analysis analysis) {
    analysis.setName(analysisScreen.getName());
    analysis.setDescription(analysisScreen.getDescription());
    analysis.setIdLab(analysisScreen.getIdLab());
    analysis.setIdAnalysisProtocol(analysisScreen.getIdAnalysisProtocol());
    analysis.setIdAnalysisType(analysisScreen.getIdAnalysisType());
    analysis.setIdOrganism(analysisScreen.getIdOrganism());
    analysis.setIdGenomeBuild(analysisScreen.getIdGenomeBuild());
  }
  
  
  public static boolean removeAnalysisFileFromFileSystem(String baseDir, Analysis analysis, AnalysisFile analysisFile) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(analysis.getCreateDate());
    
    String fileName = baseDir + createYear + "\\" + analysis.getNumber() + "\\" + analysisFile.getFileName();
    File f = new File(fileName);
    return f.delete();
    
  }
  
  public static boolean removeAnalysisDirectoryFromFileSystem(String baseDir, Analysis analysis) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(analysis.getCreateDate());
    
    String dirName = baseDir + createYear + "\\" + analysis.getNumber();
    File f = new File(dirName);
    return f.delete();
    
  }
  
  
  private class AnalysisGroupComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AnalysisGroup ag1 = (AnalysisGroup)o1;
      AnalysisGroup ag2 = (AnalysisGroup)o2;
      
      return ag1.getIdAnalysisGroup().compareTo(ag2.getIdAnalysisGroup());
      
    }
  }

  private class AnalysisExperimentItemComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      AnalysisExperimentItem e1 = (AnalysisExperimentItem)o1;
      AnalysisExperimentItem e2 = (AnalysisExperimentItem)o2;
      
      String key1 = e1.getIdHybridization() != null ? "hyb" + e1.getIdHybridization() : "lane" + e1.getIdSequenceLane();
      String key2 = e2.getIdHybridization() != null ? "hyb" + e2.getIdHybridization() : "lane" + e2.getIdSequenceLane();
      
      return key1.compareTo(key2);
      
    }
  }

}