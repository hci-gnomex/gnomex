package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AppUserComparator;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.PropertyOptionComparator;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;



public class LinkDataTrackFile extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LinkDataTrackFile.class);
  
  
  private Integer idDataTrack = null;
  private Integer idGenomeBuild = null;
  private Integer idDataTrackFolder = null;
  private Integer idAnalysisFile = null;
  private Integer idAnalysisFileOther = null; // For file pairs
  private Integer idLab = null;
  private boolean isNewDataTrack = false;
  private Integer idDataTrackToDuplicate = null;
  
  private String serverName = null;
  private String baseDirDataTrack = null;
  private String baseDirAnalysis = null;
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
     idDataTrack = new Integer(request.getParameter("idDataTrack"));
   } 
   if (idDataTrack == null) {
     isNewDataTrack = true;
   }
   if (request.getParameter("idDataTrackToDuplicate") != null && !request.getParameter("idDataTrackToDuplicate").equals("")) {
     idDataTrackToDuplicate = new Integer(request.getParameter("idDataTrackToDuplicate"));
   } 
   if (request.getParameter("idGenomeBuild") != null && !request.getParameter("idGenomeBuild").equals("")) {
     idGenomeBuild = new Integer(request.getParameter("idGenomeBuild"));
   } else {
     this.addInvalidField("idGenomeBuild", "idGenomeBuild is required.");
   }
   if (request.getParameter("idDataTrackFolder") != null && !request.getParameter("idDataTrackFolder").equals("")) {
     idDataTrackFolder = new Integer(request.getParameter("idDataTrackFolder"));
   } 
   if (request.getParameter("idAnalysisFile") != null && !request.getParameter("idAnalysisFile").equals("")) {
     idAnalysisFile = new Integer(request.getParameter("idAnalysisFile"));
   } else {
     this.addInvalidField("idAnalysisFile", "idAnalysisFile is required.");
   }
   if (request.getParameter("idAnalysisFileOther") != null && !request.getParameter("idAnalysisFileOther").equals("")) {
     idAnalysisFileOther = new Integer(request.getParameter("idAnalysisFileOther"));
   }

   if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
     idLab = new Integer(request.getParameter("idLab"));
   } else if (isNewDataTrack) {
     this.addInvalidField("idLab", "idLab is required.");
   }
   serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrack dataTrack = null;
    AnalysisFile analysisFile = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      
      baseDirDataTrack = PropertyDictionaryHelper.getInstance(sess).getDataTrackDirectory(serverName);
      baseDirAnalysis = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(serverName);
      
      analysisFile = (AnalysisFile)sess.load(AnalysisFile.class, idAnalysisFile);
      
      DataTrack sourceDT = null;
      if (idDataTrackToDuplicate != null) {
        sourceDT = (DataTrack)sess.load(DataTrack.class, idDataTrackToDuplicate);
      }
      
      if (isNewDataTrack) {
        dataTrack = new DataTrack();
        if (idDataTrackToDuplicate != null) {
          cloneDataTrack(sourceDT, dataTrack, analysisFile, sess);          
        } else {
          dataTrack.setCodeVisibility(Visibility.VISIBLE_TO_GROUP_MEMBERS);
          dataTrack.setName(analysisFile.getAnalysis().getNumber() + "_" + analysisFile.getFileName());
          dataTrack.setIdLab(idLab);
          dataTrack.setIdGenomeBuild(idGenomeBuild);
          if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
            dataTrack.setIdAppUser(this.getSecAdvisor().getIdAppUser());
          }
        }
        dataTrack.setDataPath(baseDirDataTrack);
        dataTrack.setCreatedBy(this.getUsername());
        dataTrack.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
        dataTrack.setIsLoaded("N");
        
        sess.save(dataTrack);
        dataTrack.setFileName("DT" + dataTrack.getIdDataTrack());
        sess.flush();
      } else {
        dataTrack = (DataTrack)sess.load(DataTrack.class, idDataTrack);               
      }
      // Make sure the user can write this data Track 
      if (!this.getSecAdvisor().canUpdate(dataTrack)) {
        addInvalidField("writep", "Insufficient permission to write to data track.");
      }
      
      // Make sure the user can read this analysis
      if (!this.getSecAdvisor().canRead(analysisFile.getAnalysis())) {
        addInvalidField("readp", "Insufficient permission to read analysis.");
      }
      
      // Validate the the bam file
      if (analysisFile.getFileName().endsWith(".bam") || analysisFile.getFileName().endsWith(".BAM")) {
        File file = analysisFile.getFile(baseDirAnalysis);
        String error = DataTrackUtil.checkBamFile(file);
        if (error != null) {
          addInvalidField("bamv", "Invalid BAM file: " + error + ". Please correct errors before distributing the data track");
        }
      }

      
      // Add the analysis file
      DataTrackFile dtFile = new DataTrackFile();
      dtFile.setIdAnalysisFile(idAnalysisFile);
      dtFile.setIdDataTrack(dataTrack.getIdDataTrack());
      sess.save(dtFile);
      
      // If we are linking a .bw or .bai, see if we have linked to its pair.
      // If not, fill in idAnalysisFileOther, so that the pair is linked as
      // well.
      if (idAnalysisFileOther == null) {
        boolean lookForBam = false;
        boolean lookForBai = false;
        int pos =  analysisFile.getFileName().lastIndexOf(".");
        String baseFileName = analysisFile.getFileName().substring(0, pos);
        
        if (analysisFile.getFileName().toUpperCase().endsWith(".BAI")) {
          lookForBam = true;
        } else if (analysisFile.getFileName().toUpperCase().endsWith(".BAM")){
          lookForBai = true;
        }
        for (Iterator i = analysisFile.getAnalysis().getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          int afPos = af.getFileName().lastIndexOf(".");
          String afBaseFileName = af.getFileName().substring(0, afPos);
          if (baseFileName.toUpperCase().equals(afBaseFileName.toUpperCase())) {
            if (lookForBai && af.getFileName().toUpperCase().endsWith(".BAI")) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            } else if (lookForBam && af.getFileName().toUpperCase().endsWith(".BAM")) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            }
          }
        }
      } 
        
      
      // If this is a file pair, add the other analysis file
      if (idAnalysisFileOther != null) {
        DataTrackFile dtFileOther = new DataTrackFile();
        dtFileOther.setIdAnalysisFile(idAnalysisFileOther);
        dtFileOther.setIdDataTrack(dataTrack.getIdDataTrack());
        sess.save(dtFileOther);
      }
      sess.flush();
      

      // If this is a new data track, add it to the folder
      if (this.isValid()) {
      
        if (isNewDataTrack) {
          GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

          DataTrackFolder folderNew = null;
          if (idDataTrackFolder == null) {
            // If this is a root dataTrack, find the default root dataTrack
            // grouping for the genome version.
            folderNew = genomeBuild.getRootDataTrackFolder();
            if (folderNew == null) {
              throw new Exception("Cannot find root dataTrack grouping for " + genomeBuild.getGenomeBuildName());
            }
            idDataTrackFolder = folderNew.getIdDataTrackFolder();
          } else {
            // Otherwise, find the dataTrack grouping passed in as a request parameter.
            folderNew = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
          }
          if (this.isValid()) {
            //
            // Add the dataTrack to the dataTrack folder
            //
            Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
            for(Iterator<?> i = folderNew.getDataTracks().iterator(); i.hasNext();) {
              DataTrack a = DataTrack.class.cast(i.next());
              newDataTracks.add(a);
            }
            newDataTracks.add(dataTrack);
            folderNew.setDataTracks(newDataTracks);
            sess.flush();
          }
        }
      }
  
      if (isValid()) {
          Element root = new Element("SUCCESS");
          Document doc = new Document(root);
          root.setAttribute("idDataTrack", dataTrack.getIdDataTrack().toString());
          root.setAttribute("idGenomeBuild", idGenomeBuild.toString());
          root.setAttribute("idDataTrackFolder", idDataTrackFolder != null ? idDataTrackFolder.toString() : "");
          root.setAttribute("idAnalysisFile", idAnalysisFile.toString());
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding(true);
          this.xmlResult = out.outputString(doc);
          this.setResponsePage(SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in LinkDataTrackFile ", e);
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
  
  private void cloneDataTrack(DataTrack sourceDT, DataTrack dataTrack, AnalysisFile analysisFile, Session sess) {
    dataTrack.setCodeVisibility(sourceDT.getCodeVisibility());
    dataTrack.setName(sourceDT.getName() + "_" + analysisFile.getFileName());
    dataTrack.setDescription(sourceDT.getDescription());
    dataTrack.setSummary(sourceDT.getSummary());
    dataTrack.setIdInstitution(sourceDT.getIdInstitution());
    dataTrack.setIdLab(sourceDT.getIdLab());
    dataTrack.setIdGenomeBuild(sourceDT.getIdGenomeBuild());
    dataTrack.setIdAppUser(sourceDT.getIdAppUser());

    
    //
    // Clone the data track property entries
    //
    Set<PropertyEntry> clonedPESet = new HashSet<PropertyEntry>(); 

    //for each PropertyEntry in the source data track
    for(Iterator<?> i = sourceDT.getPropertyEntries().iterator(); i.hasNext();) {

      //get the PropertyEntry
      PropertyEntry sourcePE = (PropertyEntry)i.next();

      //make clone and copy over params from source
      PropertyEntry clonedPE = new PropertyEntry();
      clonedPE.setIdProperty( sourcePE.getIdProperty());
      clonedPE.setValue( sourcePE.getValue());
      clonedPE.setIdDataTrack(dataTrack.getIdDataTrack());

      //save it and flush it to assign the DB id
      sess.save(clonedPE);
      sess.flush();

      //add to set
      clonedPESet.add(clonedPE);

      Set<PropertyEntryValue> clonedPEVSet = new HashSet<PropertyEntryValue>();

      //for each PropertyEntryValue in the sourcePE
      for (Iterator iX = sourcePE.getValues().iterator(); iX.hasNext();) {

        PropertyEntryValue sourcePEV = (PropertyEntryValue)iX.next();

        //make clone and copy over params from source
        PropertyEntryValue clonedPEV = new PropertyEntryValue();
        clonedPEV.setIdPropertyEntry(clonedPE.getIdPropertyEntry());
        clonedPEV.setValue(sourcePEV.getValue());

        //save it to DB
        sess.save(clonedPEV);

        //add to set
        clonedPEVSet.add(clonedPEV);
      }

      //add set to AP
      clonedPE.setValues(clonedPEVSet);

      TreeSet<PropertyOption> clonedOptions = new TreeSet<PropertyOption>(new PropertyOptionComparator());

      //for each PropertyOption in the sourcePE
      for (Iterator<?> iY = sourcePE.getOptions().iterator(); iY.hasNext();) {
        PropertyOption sourceOption = (PropertyOption)iY.next();
        clonedOptions.add(sourceOption);    
      }

      //add set to AP
      clonedPE.setOptions(clonedOptions);
    }

    //add Set of AnnotationPropery to cloned Annotation
    dataTrack.setPropertyEntries(clonedPESet);

    
    //
    // Clone the collaborators
    //
    TreeSet<AppUser> collaborators = new TreeSet<AppUser>(new AppUserComparator());
    Iterator<?> cIt = sourceDT.getCollaborators().iterator();
    while (cIt.hasNext()) {
      collaborators.add((AppUser)cIt.next());
      dataTrack.setCollaborators(collaborators);      
    }
  }
}