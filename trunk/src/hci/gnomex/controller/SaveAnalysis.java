package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisCollaborator;
import hci.gnomex.model.AnalysisExperimentItem;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AnalysisGroup;
import hci.gnomex.model.AnalysisType;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Lab;
import hci.gnomex.model.Organism;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.TransferLog;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisCollaboratorParser;
import hci.gnomex.utility.AnalysisFileParser;
import hci.gnomex.utility.AnalysisGenomeBuildParser;
import hci.gnomex.utility.AnalysisGroupParser;
import hci.gnomex.utility.AnalysisHybParser;
import hci.gnomex.utility.AnalysisLaneParser;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.PropertyOptionComparator;
import hci.gnomex.utility.RequestParser;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.Element;




public class SaveAnalysis extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveLab.class);
  
  private String                baseDir;
  
  private String                analysisGroupsXMLString;
  private Document              analysisGroupsDoc;
  private AnalysisGroupParser   analysisGroupParser;

  private String                analysisFilesXMLString;
  private Document              analysisFilesDoc;
  private String                analysisFilesToDeleteXMLString;
  private Document              analysisFilesToDeleteDoc;
  private AnalysisFileParser    analysisFileParser;
  
  private Integer               originalIdLab = null;
  
  private String                hybsXMLString;
  private Document              hybsDoc;
  private AnalysisHybParser     hybParser;

  private String                lanesXMLString;
  private Document              lanesDoc;
  private AnalysisLaneParser    laneParser;

  private String                     collaboratorsXMLString;
  private Document                   collaboratorsDoc;
  private AnalysisCollaboratorParser collaboratorParser;

  private String                     genomeBuildsXMLString;
  private Document                   genomeBuildsDoc;
  private AnalysisGenomeBuildParser  genomeBuildParser;

  private Analysis              analysisScreen;
  private boolean              isNewAnalysis = false;
  private boolean              isNewAnalysisGroup = false;
  
  private String                newAnalysisGroupName;
  private String                newAnalysisGroupDescription;
  private Integer               newAnalysisGroupId = new Integer(-1);
  
  private String                codeVisibilityToUpdate;
  
  private boolean               isBatchMode = false;
  private String                organism;
  private String                genomeBuild;
  private String                labName;
  private String                analysisType;
  private AnalysisGroup         existingAnalysisGroup;
  
  private String                propertiesXML;
  
  private String                serverName;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    baseDir = request.getServerName();

    analysisScreen = new Analysis();
    HashMap errors = this.loadDetailObject(request, analysisScreen);
    this.addInvalidFields(errors);
    if (analysisScreen.getIdAnalysis() == null || analysisScreen.getIdAnalysis().intValue() == 0) {
      isNewAnalysis = true;
    }
    
    StringReader reader = null;
    
    if (request.getParameter("analysisGroupsXMLString") != null && !request.getParameter("analysisGroupsXMLString").equals("")) {
      analysisGroupsXMLString = request.getParameter("analysisGroupsXMLString");
      reader = new StringReader(analysisGroupsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        analysisGroupsDoc = sax.build(reader);
        analysisGroupParser = new AnalysisGroupParser(analysisGroupsDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse analysisGroupsXMLString", je );
        this.addInvalidField( "analysisGroupsXMLString", "Invalid analysisGroupsXMLString");
      }
    }
    
    
    if (request.getParameter("analysisFilesXMLString") != null && !request.getParameter("analysisFilesXMLString").equals("")) {
      analysisFilesXMLString = request.getParameter("analysisFilesXMLString");
      if (request.getParameter("analysisFilesToDeleteXMLString") != null && !request.getParameter("analysisFilesToDeleteXMLString").equals("")) {
        analysisFilesToDeleteXMLString = request.getParameter("analysisFilesToDeleteXMLString");
      }
      
      reader = new StringReader(analysisFilesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        analysisFilesDoc = sax.build(reader);

        reader = new StringReader(analysisFilesToDeleteXMLString);
        analysisFilesToDeleteDoc = sax.build(reader);
        
        analysisFileParser = new AnalysisFileParser(analysisFilesDoc, analysisFilesToDeleteDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse analysisFilesXMLString", je );
        this.addInvalidField( "analysisFilesXMLString", "Invalid analysisFilesXMLString");
      }
    }
    
    if (request.getParameter("hybsXMLString") != null && !request.getParameter("hybsXMLString").equals("")) {
      hybsXMLString = request.getParameter("hybsXMLString");
      reader = new StringReader(hybsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        hybsDoc = sax.build(reader);
        hybParser = new AnalysisHybParser(hybsDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse hybsXMLString", je );
        this.addInvalidField( "hybsXMLString", "Invalid hybsXMLString");
      }
    }
    

    if (request.getParameter("lanesXMLString") != null && !request.getParameter("lanesXMLString").equals("")) {
      lanesXMLString = request.getParameter("lanesXMLString");
      reader = new StringReader(lanesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        lanesDoc = sax.build(reader);
        laneParser = new AnalysisLaneParser(lanesDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse lanesXMLString", je );
        this.addInvalidField( "lanesXMLString", "Invalid lanesXMLString");
      }
    }
    
    
    if (request.getParameter("collaboratorsXMLString") != null && !request.getParameter("collaboratorsXMLString").equals("")) {
      collaboratorsXMLString = request.getParameter("collaboratorsXMLString");
      reader = new StringReader(collaboratorsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        collaboratorsDoc = sax.build(reader);
        collaboratorParser = new AnalysisCollaboratorParser(collaboratorsDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse collaboratorsXMLString", je );
        this.addInvalidField( "collaboratorsXMLString", "Invalid collaboratorsXMLString");
      }
    }

    if (request.getParameter("genomeBuildsXMLString") != null && !request.getParameter("genomeBuildsXMLString").equals("")) {
      genomeBuildsXMLString = request.getParameter("genomeBuildsXMLString");
      reader = new StringReader(genomeBuildsXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        genomeBuildsDoc = sax.build(reader);
        genomeBuildParser = new AnalysisGenomeBuildParser(genomeBuildsDoc);
      } catch (JDOMException je ) {
        log.error( "Cannot parse genomeBuildsXMLString", je );
        this.addInvalidField( "genomeBuildsXMLString", "Invalid genomeBuildsXMLString");
      }
    }

    
    if (request.getParameter("newAnalysisGroupName") != null && !request.getParameter("newAnalysisGroupName").equals("")) {
      newAnalysisGroupName = request.getParameter("newAnalysisGroupName");
      isNewAnalysisGroup = true;
    }    
    if (request.getParameter("newAnalysisGroupDescription") != null && !request.getParameter("newAnalysisGroupDescription").equals("")) {
      newAnalysisGroupDescription = request.getParameter("newAnalysisGroupDescription");
    }    
    
    if (request.getParameter("propertiesXML") != null && !request.getParameter("propertiesXML").equals("")) {
      propertiesXML = request.getParameter("propertiesXML");    
    }
    
    serverName = request.getServerName();
    
    if (request.getParameter("isBatchMode") != null && request.getParameter("isBatchMode").equals("Y")) {
      isBatchMode = true;
    }
    if (isBatchMode) {
      labName = request.getParameter("labName");
      genomeBuild = request.getParameter("genomeBuild");
      organism = request.getParameter("organism");
      analysisType = request.getParameter("analysisType");
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      // If the request parameters came from a batch java program 
      // (see hci.gnomex.httpclient.CreateAnalysis), then
      // the names of the lab, genome build, organism, and
      // analysis type are passed in.  Now lookup the
      // objects to get the ids.
      if (isBatchMode) {
        getLab(sess);
        getAnalysisType(sess);
        getOrganism(sess);
        getGenomeBuilds(sess);
        getExistingAnalysisGroup(sess);
      }
      
      Analysis analysis = null;
      if (isNewAnalysis) {
        analysis = analysisScreen;
        analysis.setCodeVisibility(Visibility.VISIBLE_TO_GROUP_MEMBERS);
        analysis.setIdAppUser(this.getSecAdvisor().getIdAppUser());        
      } else {
        analysis = (Analysis)sess.load(Analysis.class, analysisScreen.getIdAnalysis());       
        originalIdLab = analysis.getIdLab();
        
        if (this.getSecAdvisor().canUpdate(analysis, SecurityAdvisor.PROFILE_OBJECT_VISIBILITY)) {
          analysis.setCodeVisibility(codeVisibilityToUpdate);
        }
      }
      
      if (this.getSecurityAdvisor().canUpdate(analysis)) {
        if (analysisGroupParser != null) {
          analysisGroupParser.parse(sess);          
        }
        if (analysisFileParser != null) {
          analysisFileParser.parse(sess);          
        }
        if (hybParser != null) {
          hybParser.parse(sess);
        }
        if (laneParser != null) {
          laneParser.parse(sess, isBatchMode);          
        }
        if (collaboratorParser != null) {
          collaboratorParser.parse(sess);          
        }
        if (genomeBuildParser != null) {
          genomeBuildParser.parse(sess);          
        }
        
        // Make sure that there are not any data track files linked to analysis files
        if (analysisFileParser != null) {
          String message = validateAnalysisFilesToRemove(sess, analysis, analysisFileParser);
          if (message != null && !message.equals("")) {
            throw new RollBackCommandException(message);
          }
        }
        
              
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
          analysis.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
          sess.save(analysis);

          
          
        } else {
          initializeAnalysis(sess, analysis);
        }


        //
        // Save analysis groups
        //
        if (!isNewAnalysisGroup) {
          TreeSet analysisGroups = new TreeSet(new AnalysisGroupComparator());
          if (existingAnalysisGroup != null) {
            analysisGroups.add(existingAnalysisGroup);
          } else if (analysisGroupParser != null && analysisGroupParser.getAnalysisGroupMap().isEmpty()) {
            // If analysis group wasn't provided, create a default one
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
            if (hybParser != null) {
              for(Iterator i1 = hybParser.getIdHybridizations().iterator(); i1.hasNext();) {
                Integer idHybridization = (Integer)i1.next();
                if (idHybridization.equals(ex.getIdHybridization())) {
                  found = true;
                  break;
                }
              }
              
            }
            if (!found) {
              if (laneParser != null) {
                for(Iterator i1 = laneParser.getIdSequenceLanes().iterator(); i1.hasNext();) {
                  Integer idSequenceLane = (Integer)i1.next();
                  if (idSequenceLane.equals(ex.getIdSequenceLane())) {
                    found = true;
                    break;
                  }
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
        if (hybParser != null) {
          for(Iterator i = hybParser.getIdHybridizations().iterator(); i.hasNext();) {
            Integer idHybridization = (Integer)i.next();
            AnalysisExperimentItem experimentItem = null;
            // The experiment item may already exist; if so, just save it.
            for(Iterator i1 = analysis.getExperimentItems().iterator(); i1.hasNext();) {
              AnalysisExperimentItem x = (AnalysisExperimentItem)i1.next();
              if (x.getIdHybridization().equals(idHybridization)) {
                experimentItem = x;
                break;
              }
            }
            if (experimentItem == null) {
              experimentItem = new AnalysisExperimentItem();
              experimentItem.setIdAnalysis(analysis.getIdAnalysis());
              experimentItem.setIdHybridization(idHybridization);
              experimentItem.setIdRequest(hybParser.getIdRequest(idHybridization));
            }
            experimentItems.add(experimentItem);
          }          
        }
        if (laneParser != null) {
          for(Iterator i = laneParser.getIdSequenceLanes().iterator(); i.hasNext();) {
            Integer idSequenceLane = (Integer)i.next();
            AnalysisExperimentItem experimentItem = null;
            // The experiment item may already exist; if so, just save it.
            for(Iterator i1 = analysis.getExperimentItems().iterator(); i1.hasNext();) {
              AnalysisExperimentItem x = (AnalysisExperimentItem)i1.next();
              if (x.getIdSequenceLane().equals(idSequenceLane)) {
                experimentItem = x;
                break;
              }
            }
            if (experimentItem == null) {
              experimentItem = new AnalysisExperimentItem();
              experimentItem.setIdAnalysis(analysis.getIdAnalysis());
              experimentItem.setIdSequenceLane(idSequenceLane);
              experimentItem.setIdRequest(laneParser.getIdRequest(idSequenceLane));
            }
            experimentItems.add(experimentItem);
          }          
        }
        if (hybParser != null || laneParser != null) {
          analysis.setExperimentItems(experimentItems);          
        }
        
        sess.flush();        

        //
        // Save analysis files
        //
        if (!isNewAnalysisGroup) {
          if (analysisFileParser != null) {
            for(Iterator i = analysisFileParser.getAnalysisFileMap().keySet().iterator(); i.hasNext();) {
              String idAnalysisFileString = (String)i.next();
              AnalysisFile af = (AnalysisFile)analysisFileParser.getAnalysisFileMap().get(idAnalysisFileString);
              sess.save(af);
            }            
          }
        }
        
        // If the analysis lab was changed, reassign the lab on the transfer logs.
        if (!isNewAnalysis) {
          if (!analysis.getIdLab().equals(originalIdLab)) {
            reassignLabForTransferLog(sess, analysis);
            sess.flush();
          }
        }
        
        // Get rid of removed analysis files
        if (analysisFileParser != null) {
          DictionaryHelper dh = DictionaryHelper.getInstance(sess);
          String analysisBaseDir = dh.getAnalysisWriteDirectory(baseDir);
          
          for(Iterator i = analysisFileParser.getAnalysisFileToDeleteMap().keySet().iterator(); i.hasNext();) {
            String idAnalysisFileString = (String)i.next();
            AnalysisFile af = (AnalysisFile)analysisFileParser.getAnalysisFileToDeleteMap().get(idAnalysisFileString);

            // Only delete from db if it was already present.
            if (!idAnalysisFileString.startsWith("AnalysisFile") && !idAnalysisFileString.equals("")) {              
//              Set aFiles = analysis.getFiles();
              analysis.getFiles().remove(af);
            }

            // Remove reference of analysis from from TransferLog
            removeAnalysisFileFromTransferLog(sess, analysisBaseDir, analysis, af);
            sess.flush();
            
            // Remove file from file system.
            removeAnalysisFileFromFileSystem(analysisBaseDir, analysis, af);
            
          }          
          sess.flush();
        }

        
        // Delete any collaborators that were removed
        if (collaboratorParser != null) {
          for (Iterator i1 = analysis.getCollaborators().iterator(); i1.hasNext();) {
            AnalysisCollaborator ac = (AnalysisCollaborator)i1.next();
            if (!collaboratorParser.getCollaboratorMap().containsKey(ac.getIdAppUser())) {
              sess.delete(ac);
            }
          }
        }
        
        // Add/update collaborators
        if (collaboratorParser != null) {
          Set collaborators = new TreeSet();
          for(Iterator i = collaboratorParser.getCollaboratorMap().keySet().iterator(); i.hasNext();) {
            Integer idAppUser = (Integer)i.next();
            String canUploadData = (String)collaboratorParser.getCollaboratorMap().get(idAppUser);
            
            // TODO (performance):  Would be better if app user was cached.
            AnalysisCollaborator collaborator = (AnalysisCollaborator)sess.createQuery("SELECT ac from AnalysisCollaborator ac where idAnalysis = " + analysis.getIdAnalysis() + " and idAppUser = " + idAppUser).uniqueResult();
            
            // If the collaborator doesn't exist, create it.
            if (collaborator == null) {
              collaborator = new AnalysisCollaborator();
              collaborator.setIdAppUser(idAppUser);
              collaborator.setIdAnalysis(analysis.getIdAnalysis());
              collaborator.setCanUploadData(canUploadData);
              sess.save(collaborator);
            } else {
              // If the collaborator does exist, just update the upload permission flag.
              collaborator.setCanUploadData(canUploadData);
            }
          }
          sess.flush();          
        }

        
        //
        // Save genomeBuilds
        //
        if (genomeBuildParser != null) {
          Set genomeBuilds = new TreeSet();
          for(Iterator i = genomeBuildParser.getIdGenomeBuildList().iterator(); i.hasNext();) {
            Integer idGenomeBuild = (Integer)i.next();
            
            GenomeBuild genomeBuild = (GenomeBuild)sess.load(GenomeBuild.class, idGenomeBuild);
            genomeBuilds.add(genomeBuild);
          }
          analysis.setGenomeBuilds(genomeBuilds);          
        }         
        
        sess.flush();
          
        //
        // Save properties
        //
        this.saveAnalysisProperties(sess, analysis);
        
        String filePathInfo = "";
        if (isBatchMode) {
          String baseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisWriteDirectory(serverName);
          filePathInfo = " filePath=\"" + getAnalysisDirectory(baseDir, analysis) + "\"";
        }
        this.xmlResult = "<SUCCESS idAnalysis=\"" + analysis.getIdAnalysis() + "\"" +  " idAnalysisGroup=\"" + newAnalysisGroupId + "\"" + filePathInfo + "/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save " + analysis.getNumber() + " analysis.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveAnalysis ", e);
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
  
  public static String validateAnalysisFilesToRemove(Session sess, Analysis a, AnalysisFileParser analysisFileParser) throws UnknownPermissionException {
      String message = null;
      
      // If there are no analysis files to delete, this validation check can be bypassed.
      if (analysisFileParser != null) {
        if (analysisFileParser.getAnalysisFileToDeleteMap().size() == 0) {
          return message;
        }
      } else {
        if (a.getFiles().size() == 0) {
          return message;
        }
      }
      StringBuffer queryBuf = new StringBuffer();
      int inCount = 0;
      queryBuf.append("SELECT COUNT(*) FROM DataTrack dt ");
      queryBuf.append("JOIN dt.dataTrackFiles dtf ");
      queryBuf.append("WHERE dtf.idAnalysisFile IN (");
      if (analysisFileParser != null) {
        for (Iterator i = analysisFileParser.getAnalysisFileToDeleteMap().keySet().iterator(); i.hasNext();) {
          String idAnalysisFile = (String)i.next();
          // Analysis files that are on the file system but not yet saved should
          // be ignored.
          if (idAnalysisFile.startsWith("AnalysisFile")) {
            continue;
          }
          queryBuf.append(idAnalysisFile);
          inCount++;
          if (i.hasNext()) {
            queryBuf.append(",");
          }
        } 
        queryBuf.append(")");
      } else {
        for (Iterator i = a.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          queryBuf.append(af.getIdAnalysisFile());
          inCount++;
          if (i.hasNext()) {
            queryBuf.append(",");
          }
        }
        queryBuf.append(")");
      }
      // Only perform query if we have at least one idAnalysisFile in the "in" clause
      if (inCount > 0) {
        Integer count = (Integer)sess.createQuery(queryBuf.toString()).uniqueResult();
        if (count != null && count > 0) {
          message = "Cannot remove analysis file.  Please remove or unlink releated data tracks first.";
        }
      }
      return message;
      
  }
  
  
  private void getLab(Session sess) throws Exception{
    String lastName = null;
    String firstName = null;
    String[] tokens = labName.split(", ");
    if (tokens != null && tokens.length == 2) {
      lastName = tokens[0];
      firstName = tokens[1];
    } else {
      tokens = labName.split(" ");
      if (tokens != null && tokens.length == 2) {
        firstName = tokens[0];
        lastName = tokens[1];
      } else if (tokens != null && tokens.length == 1) {
        lastName = tokens[1];
      } else {
        lastName = labName;
      }
    }
    
    if (firstName == null && lastName == null) {
      throw new RollBackCommandException("Lab name not provided or does not parse correctly: " + labName);
    }
    
    StringBuffer buf = new StringBuffer("SELECT l from Lab l where l.lastName = '" + lastName + "'");
    if (firstName != null) {
      buf.append(" AND l.firstName = '" + firstName + "'");
    }
    Lab lab = (Lab)sess.createQuery(buf.toString()).uniqueResult();
    if (lab == null) {
      throw new RollBackCommandException("Lab " + labName + " not found in gnomex db");
    }
    analysisScreen.setIdLab(lab.getIdLab());
    
  }
  
  private void getAnalysisType(Session sess) throws Exception{
    if (analysisType == null || analysisType.equals("")) {
      throw new RollBackCommandException("Analysis type not provided");
    }
    
    StringBuffer buf = new StringBuffer("SELECT at from AnalysisType at where at.analysisType = '" + analysisType + "'");
    AnalysisType at = (AnalysisType)sess.createQuery(buf.toString()).uniqueResult();
    if (at == null) {
      throw new RollBackCommandException("Analysis type " + analysisType + " not found in gnomex db");
    }
    analysisScreen.setIdAnalysisType(at.getIdAnalysisType());
    
  }
  
  private void getGenomeBuilds(Session sess) throws Exception{
    if (this.isBatchMode) {
      if (genomeBuild == null || genomeBuild.equals("")) {
        throw new RollBackCommandException("genomeBuild not provided");
      }
      
      StringBuffer buf = new StringBuffer("SELECT gb from GenomeBuild gb where gb.genomeBuildName like '%" + genomeBuild + "%'");
      GenomeBuild gb = (GenomeBuild)sess.createQuery(buf.toString()).uniqueResult();
      if (gb == null) {
        throw new RollBackCommandException("Genome build " + genomeBuild + " not found in gnomex db");
      }
      Set genomeBuilds = new TreeSet();
      genomeBuilds.add(gb);
      analysisScreen.setGenomeBuilds(genomeBuilds);
    }
  }
  
  private void getOrganism(Session sess) throws Exception{
    if (organism == null || organism.equals("")) {
      throw new RollBackCommandException("organism not provided");
    }
    
    StringBuffer buf = new StringBuffer("SELECT o from Organism o where o.organism = '" + organism + "'");
    Organism o = (Organism)sess.createQuery(buf.toString()).uniqueResult();
    if (o == null) {
      throw new RollBackCommandException("Organism " + organism + " not found in gnomex db");
    }
    analysisScreen.setIdOrganism(o.getIdOrganism());
  }
  
  private void getExistingAnalysisGroup(Session sess) throws Exception{
    if (newAnalysisGroupName == null || newAnalysisGroupName.equals("")) {
      throw new RollBackCommandException("analysis group name not provided");
    }
    
    StringBuffer buf = new StringBuffer("SELECT ag from AnalysisGroup ag where ag.name = '" + newAnalysisGroupName + "' and ag.idLab = " + analysisScreen.getIdLab());
    List results = (List)sess.createQuery(buf.toString()).list();
    if (results.size() > 0) {
      existingAnalysisGroup = (AnalysisGroup)results.get(0);
      isNewAnalysisGroup = false;
    } else {
      existingAnalysisGroup = null;
    }
  }

  private void initializeAnalysis(Session sess, Analysis analysis) throws Exception {
    analysis.setName(RequestParser.unEscape(analysisScreen.getName()));
    analysis.setDescription(RequestParser.unEscapeBasic(analysisScreen.getDescription()));
    analysis.setIdLab(analysisScreen.getIdLab());
    analysis.setIdAnalysisProtocol(analysisScreen.getIdAnalysisProtocol());
    analysis.setIdAnalysisType(analysisScreen.getIdAnalysisType());
    analysis.setIdOrganism(analysisScreen.getIdOrganism());
    analysis.setCodeVisibility(analysisScreen.getCodeVisibility());
    analysis.setIdInstitution(analysisScreen.getIdInstitution());
    analysis.setPrivacyExpirationDate(analysisScreen.getPrivacyExpirationDate());
  }
  
  private static String getAnalysisDirectory(String baseDir, Analysis analysis) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
    String createYear = formatter.format(analysis.getCreateDate());
    
    if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
      baseDir += "/";
    }
    
    String directoryName = baseDir + createYear + "/" + analysis.getNumber();    
    return directoryName;
  }
  
  private static void removeAnalysisFileFromTransferLog(Session sess, String baseDir, Analysis analysis, AnalysisFile analysisFile) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//    String createYear = formatter.format(analysis.getCreateDate());
    
    String fileName = analysisFile.getBaseFilePath() + analysisFile.getQualifiedFilePath() + analysisFile.getFileName();    

    // Remove references of the file in TransferLog
    String queryBuf = "SELECT tl from TransferLog tl where tl.idAnalysis = " + analysis.getIdAnalysis() + " AND tl.fileName like '%" + new File(fileName).getName() + "'";
    List transferLogs = sess.createQuery(queryBuf).list();
    // Go ahead and delete the transfer log if there is just one row.
    // If there are multiple transfer log rows for this filename, just
    // bypass deleting the transfer log since it is not possible
    // to tell which entry should be deleted.
    if (transferLogs.size() == 1) {
      TransferLog transferLog = (TransferLog)transferLogs.get(0);
      sess.delete(transferLog);
    }
    
  }
  
  public static void removeAnalysisFileFromFileSystem(String baseDir, Analysis analysis, AnalysisFile analysisFile) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//    String createYear = formatter.format(analysis.getCreateDate());
    
    String fileName;
    if (!analysisFile.getQualifiedFilePath().equals("") && analysisFile.getQualifiedFilePath() != null) {
      fileName = analysisFile.getBaseFilePath() + "/" + analysisFile.getQualifiedFilePath() + "/" + analysisFile.getFileName();    
    } else {
      fileName = analysisFile.getBaseFilePath() + "/" + analysisFile.getFileName();    
    }
    
    File f = new File(fileName);
    if (!f.delete()) {
      log.error("Unable to remove " + analysisFile.getFileName() + " from file system for analysis " + analysis.getNumber());
    }      
    
  }
  
  public static void removeAnalysisDirectoryFromFileSystem(String baseDir, Analysis analysis) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
//    String createYear = formatter.format(analysis.getCreateDate());
    
    String dirName = getAnalysisDirectory(baseDir, analysis);
    File f = new File(dirName);
    if (!f.delete()) {
      log.error("Unable to remove " + dirName + " from file system for analysis " + analysis.getNumber());
    }
    
  }
  
  private void reassignLabForTransferLog(Session sess, Analysis analysis) {
      // If an existing request has been assigned to a different lab, change
      // the idLab on the TransferLogs.
      String buf = "SELECT tl from TransferLog tl where idAnalysis = " + analysis.getIdAnalysis();
      List transferLogs = sess.createQuery(buf).list();
      for (Iterator i = transferLogs.iterator(); i.hasNext();) {
        TransferLog tl = (TransferLog)i.next();
        tl.setIdLab(analysis.getIdLab());
      }
  }
  
  private void saveAnalysisProperties(Session sess, Analysis analysis) throws org.jdom.JDOMException {
    // Delete analysis properties  
    if (propertiesXML != null && !propertiesXML.equals("")) {
      StringReader reader = new StringReader(propertiesXML);
      SAXBuilder sax = new SAXBuilder();
      Document propsDoc = sax.build(reader);
      for(Iterator<?> i = analysis.getPropertyEntries().iterator(); i.hasNext();) {
        PropertyEntry pe = PropertyEntry.class.cast(i.next());
        boolean found = false;
        for(Iterator<?> i1 = propsDoc.getRootElement().getChildren().iterator(); i1.hasNext();) {
          Element propNode = (Element)i1.next();
          String idPropertyEntry = propNode.getAttributeValue("idPropertyEntry");
          if (idPropertyEntry != null && !idPropertyEntry.equals("")) {
            if (pe.getIdPropertyEntry().equals(new Integer(idPropertyEntry))) {
              found = true;
              break;
            }
          }                   
        }
        if (!found) {
          // delete dataTrack property values
          for(Iterator<?> i1 = pe.getValues().iterator(); i1.hasNext();) {
            PropertyEntryValue av = PropertyEntryValue.class.cast(i1.next());
            sess.delete(av);
          }  
          sess.flush();
          // delete dataTrack property
          sess.delete(pe);
        }
      } 
      sess.flush();
      // Add dataTrack properties
      for(Iterator<?> i = propsDoc.getRootElement().getChildren().iterator(); i.hasNext();) {
        Element node = (Element)i.next();
        //Adding dataTracks
        String idPropertyEntry = node.getAttributeValue("idPropertyEntry");

        PropertyEntry pe = null;
        if (idPropertyEntry == null || idPropertyEntry.equals("")) {
          pe = new PropertyEntry();
          pe.setIdProperty(Integer.valueOf(node.getAttributeValue("idProperty")));
        } else {
          pe  = PropertyEntry.class.cast(sess.get(PropertyEntry.class, Integer.valueOf(idPropertyEntry))); 
        }
        pe.setValue(node.getAttributeValue("value"));
        pe.setIdAnalysis(analysis.getIdAnalysis());

        if (idPropertyEntry == null || idPropertyEntry.equals("")) {
          sess.save(pe);
          sess.flush();
        }

        // Remove PropertyEntryValues
        if (pe.getValues() != null) {
          for(Iterator<?> i1 = pe.getValues().iterator(); i1.hasNext();) {
            PropertyEntryValue av = PropertyEntryValue.class.cast(i1.next());
            boolean found = false;
            for(Iterator<?> i2 = node.getChildren().iterator(); i2.hasNext();) {
              Element n = (Element)i2.next();
              if (n.getName().equals("PropertyEntryValue")) {
                String idPropertyEntryValue = n.getAttributeValue("idPropertyEntryValue");
                if (idPropertyEntryValue != null && !idPropertyEntryValue.equals("")) {
                  if (av.getIdPropertyEntryValue().equals(new Integer(idPropertyEntryValue))) {
                    found = true;
                    break;
                  }
                }                   
              }
            }
            if (!found) {
              sess.delete(av);
            }
          }
          sess.flush();
        }

        // Add and update PropertyEntryValues
        for(Iterator<?> i1 = node.getChildren().iterator(); i1.hasNext();) {
          Element n = (Element)i1.next();
          if (n.getName().equals("PropertyEntryValue")) {
            String idPropertyEntryValue = n.getAttributeValue("idPropertyEntryValue");
            String value = n.getAttributeValue("value");
            PropertyEntryValue av = null;
            // Ignore 'blank' url value
            if (value == null || value.equals("") || value.equals("Enter URL here...")) {
              continue;
            }
            if (idPropertyEntryValue == null || idPropertyEntryValue.equals("")) {
              av = new PropertyEntryValue();
              av.setIdPropertyEntry(pe.getIdPropertyEntry());
            } else {
              av = PropertyEntryValue.class.cast(sess.load(PropertyEntryValue.class, Integer.valueOf(idPropertyEntryValue)));              
            }
            av.setValue(n.getAttributeValue("value"));

            if (idPropertyEntryValue == null || idPropertyEntryValue.equals("")) {
              sess.save(av);
            }
          }
        }
        sess.flush();


        String optionValue = "";
        TreeSet<PropertyOption> options = new TreeSet<PropertyOption>(new PropertyOptionComparator());
        for(Iterator<?> i1 = node.getChildren().iterator(); i1.hasNext();) {
          Element n = (Element)i1.next();
          if (n.getName().equals("PropertyOption")) {
            Integer idPropertyOption = Integer.parseInt(n.getAttributeValue("idPropertyOption"));
            String selected = n.getAttributeValue("selected");
            if (selected != null && selected.equals("Y")) {
              PropertyOption option = PropertyOption.class.cast(sess.load(PropertyOption.class, idPropertyOption));
              options.add(option);
              if (optionValue.length() > 0) {
                optionValue += ",";
              }
              optionValue += option.getOption();
            }
          }
        }
        pe.setOptions(options);
        if (options.size() > 0) {
          pe.setValue(optionValue);
        }
        sess.flush();
      }
    }

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