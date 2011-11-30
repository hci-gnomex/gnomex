package hci.gnomex.model;

import hci.dictionary.model.DictionaryEntry;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class GenomeBuild extends DictionaryEntry implements Serializable, DictionaryEntryUserOwned {
  private Integer       idGenomeBuild;
  private String        genomeBuildName;
  private Integer       idOrganism;
  private String        isActive;
  private String        isLatestBuild;
  private Integer       idAppUser;
  private String        das2Name;
  private java.sql.Date buildDate;
  private String        coordURI;
  private String        coordVersion;
  private String        coordSource;
  private String        coordTestRange;
  private String        coordAuthority;
  private String        ucscName;
  private String        dataPath;
  private Set           segments = new TreeSet();
  private Set           dataTracks = new TreeSet();;
  private Set           dataTrackFolders = new TreeSet();
  private Set           aliases = new TreeSet();
  
  public String getDisplay() {
    String display = this.getNonNullString(getGenomeBuildName());
    return display;
  }

  public String getValue() {
    return getIdGenomeBuild().toString();
  }


  
  public String getIsActive() {
    return isActive;
  }

  
  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  
  public String getGenomeBuildName() {
    return genomeBuildName;
  }

  
  public void setGenomeBuildName(String genomeBuildName) {
    this.genomeBuildName = genomeBuildName;
  }

  
  public Integer getIdGenomeBuild() {
    return idGenomeBuild;
  }

  
  public void setIdGenomeBuild(Integer idGenomeBuild) {
    this.idGenomeBuild = idGenomeBuild;
  }

  
  public Integer getIdOrganism() {
    return idOrganism;
  }

  
  public void setIdOrganism(Integer idOrganism) {
    this.idOrganism = idOrganism;
  }

  
  public String getIsLatestBuild() {
    return isLatestBuild;
  }

  
  public void setIsLatestBuild(String isLatestBuild) {
    this.isLatestBuild = isLatestBuild;
  }

  
  public Integer getIdAppUser() {
    return idAppUser;
  }

  
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }

  public String getDas2Name() {
    return das2Name;
  }

  public void setDas2Name(String das2Name) {
    this.das2Name = das2Name;
  }

  public java.sql.Date getBuildDate() {
    return buildDate;
  }

  public void setBuildDate(java.sql.Date buildDate) {
    this.buildDate = buildDate;
  }

  public String getCoordURI() {
    return coordURI;
  }

  public String getCoordVersion() {
    return coordVersion;
  }

  public String getCoordTestRange() {
    return coordTestRange;
  }

  public String getCoordAuthority() {
    return coordAuthority;
  }

  public String getUcscName() {
    return ucscName;
  }

  public String getDataPath() {
    return dataPath;
  }

  public void setCoordURI(String coordURI) {
    this.coordURI = coordURI;
  }

  public Set getSegments() {
    return segments;
  }

  public void setSegments(Set segments) {
    this.segments = segments;
  }

  public Set getDataTracks() {
    return dataTracks;
  }

  public void setDataTracks(Set dataTracks) {
    this.dataTracks = dataTracks;
  }

  public Set getDataTrackFolders() {
    return dataTrackFolders;
  }

  public void setDataTrackFolders(Set dataTrackFolders) {
    this.dataTrackFolders = dataTrackFolders;
  }

  public Set getAliases() {
    return aliases;
  }

  public void setAliases(Set aliases) {
    this.aliases = aliases;
  }

  public void setCoordVersion(String coordVersion) {
    this.coordVersion = coordVersion;
  }

  public void setCoordTestRange(String coordTestRange) {
    this.coordTestRange = coordTestRange;
  }

  private String getCoordSource() {
    return coordSource;
  }

  private void setCoordSource(String coordSource) {
    this.coordSource = coordSource;
  }

  public void setCoordAuthority(String coordAuthority) {
    this.coordAuthority = coordAuthority;
  }

  public void setUcscName(String ucscName) {
    this.ucscName = ucscName;
  }

  public void setDataPath(String dataPath) {
    this.dataPath = dataPath;
  }
  
  
  public void registerMethodsToExcludeFromXML() {
    this.excludeMethodFromXML("getSegments");
    this.excludeMethodFromXML("getAliases");
    this.excludeMethodFromXML("getDataTracks");
    this.excludeMethodFromXML("getDataTrackFolders");
    this.excludeMethodFromXML("getRootDataTrackFolder");
    this.excludeMethodFromXML("getRootDataTrackFolders");
    this.excludeMethodFromXML("getExcludedMethodsMap");
  }
  @SuppressWarnings("unchecked")
  public Document getXML(SecurityAdvisor secAdvisor, String data_root) throws UnknownPermissionException {
    Document doc = DocumentHelper.createDocument();
    Element root = doc.addElement("GenomeBuild");

    root.addAttribute("label", this.getDas2Name());       
    root.addAttribute("idGenomeBuild",this.getIdGenomeBuild().toString());        
    root.addAttribute("name",           this.getDas2Name());
    root.addAttribute("ucscName",          this.getUcscName());
    root.addAttribute("buildDate",      this.getBuildDate() != null ? DataTrackUtil.formatDate(this.getBuildDate()) : "");       
    root.addAttribute("idOrganism",     this.getIdOrganism().toString());       
    root.addAttribute("coordURI",       this.getCoordURI() != null ? this.getCoordURI().toString() : ""); 
    root.addAttribute("coordVersion",   this.getCoordVersion() != null ? this.getCoordVersion().toString() : ""); 
    root.addAttribute("coordSource",    this.getCoordSource() != null ? this.getCoordSource().toString() : ""); 
    root.addAttribute("coordTestRange", this.getCoordTestRange() != null ? this.getCoordTestRange().toString() : ""); 
    root.addAttribute("coordAuthority", this.getCoordAuthority() != null ? this.getCoordAuthority().toString() : ""); 

    // Only show the sequence files and segments for genome version detail 
    // (if data_root provided).
    if (data_root != null) {

      // Sequence files
      Element filesNode = root.addElement("SequenceFiles");

      String filePath = getSequenceDirectory(data_root);
      File fd = new File(filePath);
      if (fd.exists()) {


        Element fileNode = filesNode.addElement("Dir");
        fileNode.addAttribute("name", getSequenceFileName());
        fileNode.addAttribute("url", filePath);
        appendSequenceFileXML(filePath, fileNode, null);        
      }

      // Segments
      Element segmentsNode = root.addElement("Segments");
      for (Segment segment : (Set<Segment>)this.getSegments()) {
        Element sNode = segmentsNode.addElement("Segment");
        sNode.addAttribute("idSegment", segment.getIdSegment().toString());
        sNode.addAttribute("name", segment.getName());

        sNode.addAttribute("length", segment.getLength() != null ? NumberFormat.getInstance().format(segment.getLength()) : "");
        sNode.addAttribute("sortOrder", segment.getSortOrder() != null ? segment.getSortOrder().toString() : "");
      }
    }


    root.addAttribute("canRead", secAdvisor.canRead(this) ? "Y" : "N");
    root.addAttribute("canWrite", secAdvisor.canUpdate(this) ? "Y" : "N");

    return doc;
  }

  public String getSequenceDirectory(String data_root) {
    String dataPath = null;
    if (this.getDataPath() != null && !this.getDataPath().equals("")) {
      dataPath = this.getDataPath();
    } else {
      dataPath = data_root;
    }
    return dataPath + getSequenceFileName();
  }


  public  String getSequenceFileName() {
    return Constants.SEQUENCE_DIR_PREFIX + this.getIdGenomeBuild();
  }

  public boolean hasSequence(String data_root) throws IOException {

    boolean hasSequence = false;
    String filePath = getSequenceDirectory(data_root);
    File dir = new File(filePath);

    if (dir.exists()) {
      // Delete the files in the directory
      String[] childFileNames = dir.list();
      if (childFileNames != null && childFileNames.length > 0) {
        hasSequence = true;
      }
    }

    return hasSequence;
  }

  public void removeSequenceFiles(String data_root) throws IOException {

    String filePath = getSequenceDirectory(data_root);
    File dir = new File(filePath);

    if (dir.exists()) {
      // Delete the files in the directory
      String[] childFileNames = dir.list();
      if (childFileNames != null) {
        for (int x = 0; x < childFileNames.length; x++) {
          String fileName = filePath + "/" + childFileNames[x];
          File f = new File(fileName);
          boolean success = f.delete();
          if (!success) {
            //Logger.getLogger(GenomeBuild.class.getName()).log(Level.WARNING, "Unable to delete file " + fileName);
          }
        }

      }

      // Delete the annotation directory
      boolean success = dir.delete();
      if (!success) {
        //Logger.getLogger(GenomeBuild.class.getName()).log(Level.WARNING, "Unable to delete directory " + filePath);       
      }
    }
  }

  public static void appendSequenceFileXML(String filePath, Element parentNode, String subDirName) {
    File fd = new File(filePath);

    if (fd.isDirectory()) {
      String[] fileList = fd.list();
      for (int x = 0; x < fileList.length; x++) {
        String fileName = filePath + "/" + fileList[x];
        File f1 = new File(fileName);

        // Show the subdirectory in the name if we are not at the main folder level
        String displayName = "";
        if (subDirName != null) {
          displayName = subDirName + "/" + fileList[x];
        } else {
          displayName = f1.getName();
        }

        if (f1.isDirectory()) {
          Element fileNode = parentNode.addElement("Dir");
          fileNode.addAttribute("name", displayName);
          fileNode.addAttribute("url", fileName);
          appendSequenceFileXML(fileName, fileNode,
              subDirName != null ? subDirName + "/"
                  + f1.getName() : f1.getName());
        } else {
          Element fileNode = parentNode.addElement("File");

          long kb = DataTrackUtil.getKilobytes(f1.length());
          String kilobytes = kb + " kb";

          fileNode.addAttribute("name", displayName);
          fileNode.addAttribute("url", fileName);
          fileNode.addAttribute("size", kilobytes);
          fileNode.addAttribute("lastModified", DataTrackUtil.formatDate(new java.sql.Date(f1.lastModified())));

        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private List getRootDataTrackFolders() {
    ArrayList rootGroupings = new ArrayList();
    for (DataTrackFolder annotationGrouping : (Set<DataTrackFolder>) this
        .getDataTrackFolders()) {
      if (annotationGrouping.getIdParentDataTrackFolder() == null) {
        rootGroupings.add(annotationGrouping);
      }
    }
    return rootGroupings;
  }

  public DataTrackFolder getRootDataTrackFolder() {
    List rootGroupings = this.getRootDataTrackFolders();
    if (rootGroupings.size() > 0) {
      return DataTrackFolder.class.cast(rootGroupings.get(0));
    } else {
      return null;
    }
  }


}