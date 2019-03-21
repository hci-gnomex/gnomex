package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Institution;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.apache.log4j.Logger;


public class LinkDataTrackFile extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(LinkDataTrackFile.class);


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

      PropertyDictionaryHelper propertyHelper = PropertyDictionaryHelper.getInstance(sess);
      baseDirDataTrack = propertyHelper.getDirectory(serverName, null, propertyHelper.getProperty(PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY));
      baseDirAnalysis = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
      String use_altstr = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.USE_ALT_REPOSITORY);
      if (use_altstr != null &&use_altstr.equalsIgnoreCase("yes")) {
        baseDirAnalysis = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
                PropertyDictionaryHelper.ANALYSIS_DIRECTORY_ALT,this.getUsername());
      }

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
          String dtName = analysisFile.getFileName();
          if(dtName.toUpperCase().contains("_PLUS")) {
            int plusIndex = dtName.toUpperCase().indexOf("_PLUS");
            dtName = dtName.substring(0, plusIndex);
          } else if(dtName.toUpperCase().contains("_MINUS")) {
            int minusIndex = dtName.toUpperCase().indexOf("_MINUS");
            dtName = dtName.substring(0, minusIndex);
          } else {
            dtName = dtName.substring(0, dtName.lastIndexOf("."));
          }

          dataTrack.setName(dtName);
          dataTrack.setIdLab(idLab);
          dataTrack.setIdGenomeBuild(idGenomeBuild);

          String defaultVisibility = propertyHelper.getProperty(PropertyDictionary.DEFAULT_VISIBILITY_DATATRACK);
          if (defaultVisibility != null && defaultVisibility.length() > 0) {
            dataTrack.setCodeVisibility(defaultVisibility);
            if(defaultVisibility.compareTo(hci.gnomex.model.Visibility.VISIBLE_TO_INSTITUTION_MEMBERS) == 0) {
              if (dataTrack.getIdLab() != null) {
                Lab lab = (Lab)sess.load(Lab.class, dataTrack.getIdLab());
                Hibernate.initialize(lab.getInstitutions());
                Iterator it = lab.getInstitutions().iterator();
                while(it.hasNext()) {
                  Institution thisInst = (Institution) it.next();
                  if(thisInst.getIsDefault().compareTo("Y") == 0) {
                    dataTrack.setIdInstitution(thisInst.getIdInstitution());
                  }
                }
              }
            }
          } else {
            dataTrack.setCodeVisibility(Visibility.VISIBLE_TO_GROUP_MEMBERS);
          }

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

      // Validate the the bam or cram file
      if (analysisFile.getFileName().endsWith(".bam") || analysisFile.getFileName().endsWith(".BAM")  ||
              (analysisFile.getFileName().endsWith(".bam") || analysisFile.getFileName().endsWith(".BAM")) ) {
        File file = analysisFile.getFile(baseDirAnalysis);
        String error = DataTrackUtil.checkBamFile(file);
        if (error != null) {
          addInvalidField("bamv", "Invalid BAM or CRAM file: " + error + ". Please correct errors before distributing the data track");
        }
      }


      // If we are linking a .bw/.bb, .bai/.bam, or .vcf.gz/.vcf.gz.tbi see if we have linked to its pair.
      // If not, fill in idAnalysisFileOther, so that the pair is linked as well.
      List pairedFileNames = new  ArrayList();
      if (idAnalysisFileOther == null) {

        boolean lookForBam = false;
        boolean lookForBai = false;
        boolean lookForCram = false;
        boolean lookForCrai = false;

        boolean lookForBigWig = false;
        boolean lookForUSeq = false;
        boolean lookForVCF = false;
        boolean lookForVCFTBI = false;
        boolean lookForBigWigOrphan = false;

        String baseFileName = fetchBaseName(analysisFile.getFullPathName(), Constants.DATATRACK_FILE_EXTENSIONS);
        baseFileName = baseFileName.replace("\\", Constants.FILE_SEPARATOR);

        if(baseFileName.toUpperCase().contains("_PLUS")) {
          baseFileName = baseFileName.toUpperCase().substring(0, baseFileName.toUpperCase().indexOf("_PLUS"));
        } else if(baseFileName.toUpperCase().contains("_MINUS")) {
          baseFileName = baseFileName.toUpperCase().substring(0, baseFileName.toUpperCase().indexOf("_MINUS"));
        }

        String fileName = analysisFile.getFileName().toUpperCase();
        if (fileName.endsWith(".BAI")) lookForBam = true;
        else if (fileName.endsWith(".BAM")) lookForBai = true;
        else if (fileName.endsWith(".CRAI")) lookForCram = true;
        else if (fileName.endsWith(".CRAM")) lookForCrai = true;
        else if (fileName.endsWith(".USEQ")) lookForBigWig = true;
        else if (fileName.endsWith(".BW") || fileName.endsWith(".BB")  ) { lookForUSeq = true; lookForBigWigOrphan = true; }
        else if (fileName.endsWith(".VCF.GZ")) lookForVCFTBI = true;
        else if (fileName.endsWith(".VCF.GZ.TBI")) lookForVCF = true;
        else if (fileName.contains("_PLUS") || fileName.contains("_MINUS")) {lookForUSeq = true; lookForBigWig = true;}

        for (Iterator i = analysisFile.getAnalysis().getFiles().iterator(); i.hasNext();) {
          idAnalysisFileOther = null;
          AnalysisFile af = (AnalysisFile)i.next();
          String afBaseFileName = fetchBaseName(af.getFullPathName(), Constants.DATATRACK_FILE_EXTENSIONS);
          afBaseFileName = afBaseFileName.replace("\\", Constants.FILE_SEPARATOR);

          if(afBaseFileName.toUpperCase().contains("_PLUS")) {
            afBaseFileName = afBaseFileName.toUpperCase().substring(0, afBaseFileName.toUpperCase().indexOf("_PLUS"));
          } else if(afBaseFileName.toUpperCase().contains("_MINUS")) {
            afBaseFileName = afBaseFileName.toUpperCase().substring(0, afBaseFileName.toUpperCase().indexOf("_MINUS"));
          }



          //do the baseNames match?
          String afFileNameUpperCase = af.getFileName().toUpperCase();
          if (baseFileName.toUpperCase().equals(afBaseFileName.toUpperCase())) {
            if (lookForBai && afFileNameUpperCase.endsWith(".BAI")) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            } else if (lookForBam && afFileNameUpperCase.endsWith(".BAM")) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            }  else if (lookForCrai && afFileNameUpperCase.endsWith(".CRAI")) {
                idAnalysisFileOther = af.getIdAnalysisFile();
            } else if (lookForCram && afFileNameUpperCase.endsWith(".CRAM")) {
                idAnalysisFileOther = af.getIdAnalysisFile();
            } else if ((lookForBigWig || lookForBigWigOrphan) && (afFileNameUpperCase.endsWith(".BW") || afFileNameUpperCase.endsWith(".BB")) && !af.getIdAnalysisFile().equals(idAnalysisFile)) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            } else if (lookForUSeq && (afFileNameUpperCase.endsWith(".USEQ") || afFileNameUpperCase.endsWith(".USEQ"))) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            } else if (lookForVCFTBI && afFileNameUpperCase.endsWith(".VCF.GZ.TBI")) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            } else if (lookForVCF && afFileNameUpperCase.endsWith(".VCF.GZ")) {
              idAnalysisFileOther = af.getIdAnalysisFile();
            }
          }
          if(idAnalysisFileOther != null) {
            pairedFileNames.add(idAnalysisFileOther);
          }
        }
      } else {
        pairedFileNames.add(idAnalysisFileOther);
      }

      //is it a paired file set? then must have other
      String afFileNameUpper = analysisFile.getFileName().toUpperCase();
      boolean saveDataTrack = true;
      if (afFileNameUpper.endsWith(".BAM") || afFileNameUpper.endsWith(".BAI") || afFileNameUpper.endsWith(".CRAM") || afFileNameUpper.endsWith(".CRAI") ||
              afFileNameUpper.endsWith(".VCF.GZ") || afFileNameUpper.endsWith(".VCF.GZ.TBI")){
        if (pairedFileNames.size() == 0){
          //not sure if this makes this invalid so using boolean
          addInvalidField("bamv", "Missing indexed file or file index?!  Please add either a matching xxx.bam or xxx.bai; or add a xxx.cram or xxx.crai; or add a xxx.vcf.gz or xxx.vcf.gz.tbi.");
          saveDataTrack = false;
        }
      }

      if (saveDataTrack){

        //Create datatrackfile
        DataTrackFile dtFile = new DataTrackFile();
        dtFile.setIdAnalysisFile(idAnalysisFile);
        dtFile.setIdDataTrack(dataTrack.getIdDataTrack());
        sess.save(dtFile);

        // If this is a file pair, add the other analysis file
        if (pairedFileNames.size() > 0) {
          for(Iterator i = pairedFileNames.iterator(); i.hasNext();) {
            Integer idAnalysisFile = (Integer) i.next();
            DataTrackFile dtFileOther = new DataTrackFile();
            dtFileOther.setIdAnalysisFile(idAnalysisFile);
            dtFileOther.setIdDataTrack(dataTrack.getIdDataTrack());
            sess.save(dtFileOther);
          }
        }
        sess.flush();
      }

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
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in LinkDataTrackFile ", e);

      throw new RollBackCommandException(e.getMessage());

    }

    return this;
  }

  /**Removes the extension and its period.  Thus alta.is.great.bam.bai -> alta.is.great.bam .*/
  private static String fetchBaseName(String fileName, String[] lowerCaseExtensions){
    int extLength = 0;
    String lower = fileName.toLowerCase();
    for (String ext: lowerCaseExtensions){
      if (lower.endsWith(ext)){
        extLength = ext.length();
        break;
      }
    }
    return fileName.substring(0, fileName.length() - extLength);
  }

  private void cloneDataTrack(DataTrack sourceDT, DataTrack dataTrack, AnalysisFile analysisFile, Session sess) {
    dataTrack.setCodeVisibility(sourceDT.getCodeVisibility());
    dataTrack.setName(sourceDT.getName() + "_" + analysisFile.getFileName().substring(0, analysisFile.getFileName().lastIndexOf(".")));
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
