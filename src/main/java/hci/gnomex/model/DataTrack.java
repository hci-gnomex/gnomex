package hci.gnomex.model;


import hci.dictionary.utility.DictionaryManager;
import hci.framework.model.DetailObject;
import hci.framework.model.FieldFormatter;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.USeqUtilities;
import org.jdom.Document;
import org.jdom.Element;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataTrack extends DetailObject implements Serializable, Owned, VisibilityInterface {

    private Integer idDataTrack;
    private String name;
    private String summary;
    private String description;
    private String codeVisibility;
    private String fileName;
    private String dataPath;
    private Integer idGenomeBuild;
    private Set folders;
    private Integer idAppUser;
    private Integer idLab;
    private Lab lab;
    private Integer idInstitution;
    private String createdBy;
    private java.sql.Date createDate;
    private String isLoaded;
    private Set collaborators;
    private Set propertyEntries;
    private Set dataTrackFiles;
    private AppUser appUser;
    private Set topics;
    private Integer folderCount;  // transient variable - initialized in DataTrackQuery

    /**
     * Returns 'none' if no files available for UCSC linking, 'convert' for files requiring conversion, or 'link' if they are ready to go.
     */
    public static String appendFileXML(String filePath, Element parentNode, String subDirName, DataTrackFile dataTrackFile, String ucscLinkFile) {
        File fd = new File(filePath);

        if (fd.exists()) {
            if (fd.isDirectory()) {
                String[] fileList = fd.list();
                for (int x = 0; x < fileList.length; x++) {
                    String fileName = filePath + "/" + fileList[x];
                    File f1 = new File(fileName);

                    ucscLinkFile = formatUCSCLink(fileList[x], ucscLinkFile);

                    // Show the subdirectory in the name if we are not at the main folder level
                    String displayName = formatDisplayName(fileList[x], f1, subDirName);

                    if (f1.isDirectory()) {
                        Element fileNode = new Element("Dir");
                        parentNode.addContent(fileNode);
                        fileNode.setAttribute("name", displayName);
                        fileNode.setAttribute("url", fileName);
                        ucscLinkFile = appendFileXML(fileName, fileNode, subDirName != null ? subDirName + "/" + f1.getName() : f1.getName(), null, ucscLinkFile);
                    } else {
                        Element fileNode = new Element("File");
                        parentNode.addContent(fileNode);

                        long kb = DataTrackUtil.getKilobytes(f1.length());
                        String kilobytes = kb + " kb";

                        fileNode.setAttribute("name", displayName);
                        fileNode.setAttribute("url", fileName);
                        fileNode.setAttribute("size", kilobytes);
                        fileNode.setAttribute("lastModified", new FieldFormatter().formatDate(new java.sql.Date(f1.lastModified())));
                        fileNode.setAttribute("idDataTrackFile", dataTrackFile != null ? dataTrackFile.getIdDataTrackFile().toString() : "");
                        fileNode.setAttribute("idAnalysis", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getIdAnalysis().toString() : "");
                        fileNode.setAttribute("analysisNumber", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getNumber() : "");
                        fileNode.setAttribute("analysisLabel", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getNumber() + " " + dataTrackFile.getAnalysisFile().getAnalysis().getName() : "");
                    }
                }
            } else {
                // fd is a File
                ucscLinkFile = formatUCSCLink(fd.getName(), ucscLinkFile);

                // Show the subdirectory in the name if we are not at the main folder level
                String displayName = formatDisplayName(fd.getName(), fd, subDirName);

                Element fileNode = new Element("File");
                parentNode.addContent(fileNode);

                long kb = DataTrackUtil.getKilobytes(fd.length());
                String kilobytes = kb + " kb";

                fileNode.setAttribute("name", displayName);
                fileNode.setAttribute("url", fd.getName());
                fileNode.setAttribute("size", kilobytes);
                fileNode.setAttribute("lastModified", new FieldFormatter().formatDate(new java.sql.Date(fd.lastModified())));
                fileNode.setAttribute("idDataTrackFile", dataTrackFile != null ? dataTrackFile.getIdDataTrackFile().toString() : "");
                fileNode.setAttribute("idAnalysis", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getIdAnalysis().toString() : "");
                fileNode.setAttribute("analysisNumber", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getNumber() : "");
                fileNode.setAttribute("analysisLabel", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getNumber() + " " + dataTrackFile.getAnalysisFile().getAnalysis().getName() : "");

            }
        } else {
            // If we can't find the analysis file, just show the entry with ? for file size and last modify date
            String displayName = formatDisplayName(fd.getName(), fd, subDirName);

            Element fileNode = new Element("File");
            parentNode.addContent(fileNode);
            ucscLinkFile = "none";

            fileNode.setAttribute("name", displayName);
            fileNode.setAttribute("url", fd.getName());
            fileNode.setAttribute("size", "?");
            fileNode.setAttribute("lastModified", "?");
            fileNode.setAttribute("idDataTrackFile", dataTrackFile != null ? dataTrackFile.getIdDataTrackFile().toString() : "");
            fileNode.setAttribute("idAnalysis", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getIdAnalysis().toString() : "");
            fileNode.setAttribute("analysisNumber", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getNumber() : "");
            fileNode.setAttribute("analysisLabel", dataTrackFile != null ? dataTrackFile.getAnalysisFile().getAnalysis().getNumber() + " " + dataTrackFile.getAnalysisFile().getAnalysis().getName() : "");

        }

        return ucscLinkFile;
    }

    private static String formatUCSCLink(String fileName, String ucscLinkFile) {
        //link file?
        String ucscLink = ucscLinkFile;
        if (fileName.endsWith(".useq") && ucscLink.equals("none")) {
            ucscLink = "convert";
        } else if (fileName.endsWith(".bb") || fileName.endsWith(".bw") || fileName.endsWith(".bam") || fileName.endsWith(".vcf.gz")) {
            ucscLink = "link";
        }
        return ucscLink;

    }

    private static String formatDisplayName(String fileName, File file, String subDirName) {
        String displayName = "";
        if (subDirName != null) {
            displayName = subDirName + "/" + fileName;
        } else {
            displayName = file.getName();
        }
        return displayName;
    }

    public Set getTopics() {
        return topics;
    }

    public void setTopics(Set topics) {
        this.topics = topics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public boolean isOwner(Integer idAppUser) {
        if (this.getIdAppUser() != null && this.getIdAppUser().equals(idAppUser)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLab(Integer idLab) {
        if (this.getLab() != null && this.getLab().equals(idLab)) {
            return true;
        } else {
            return false;
        }
    }

    public Integer getIdAppUser() {
        return idAppUser;
    }

    public void setIdAppUser(Integer idAppUser) {
        this.idAppUser = idAppUser;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getIdGenomeBuild() {
        return idGenomeBuild;
    }

    public void setIdGenomeBuild(Integer idGenomeBuild) {
        this.idGenomeBuild = idGenomeBuild;
    }

    public Integer getIdLab() {
        return idLab;
    }

    public void setIdLab(Integer idLab) {
        this.idLab = idLab;
    }

    public Integer getFolderCount() {
        return folderCount;
    }

    public void setFolderCount(Integer folderCount) {
        this.folderCount = folderCount;
    }

    public void setFolderCount(Long folderCount) {
        this.folderCount = Integer.valueOf(folderCount.intValue());
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser u) {
        appUser = u;
    }

    public Integer getIdDataTrackFolder() {
        int id = 0;
        if (this.getFolders() != null) {
            for (DataTrackFolder dtf : (Set<DataTrackFolder>) this.getFolders()) {
                if (dtf != null && dtf.getIdDataTrackFolder() != 0) {
                    id = dtf.getIdDataTrackFolder();
                    break;
                }
            }
        }
        return id;
    }

    public Set getFolders() {
        return folders;
    }

    public void setFolders(Set folders) {
        this.folders = folders;
    }

    @SuppressWarnings("unchecked")
    public Document getXML(SecurityAdvisor secAdvisor, DictionaryHelper dh, String data_root, String analysis_data_root) throws Exception {
        Document doc = new Document(new Element("DataTrack"));
        Element root = doc.getRootElement();

        GenomeBuild genomeBuild = dh.getGenomeBuildObject(this.getIdGenomeBuild());
        if (genomeBuild == null) {
            Logger.getLogger(DataTrack.class.getName()).log(Level.SEVERE, "Unable to find genome version " + this.getIdGenomeBuild() + " for annotation " + this.getName());
            throw new Exception("Unable to find genome version " + this.getIdGenomeBuild() + " for annotation " + this.getName());
        }

        root.setAttribute("idDataTrack", this.getIdDataTrack().toString());
        root.setAttribute("number", this.getNumber());
        root.setAttribute("label", this.getName() != null ? this.getName() : "");
        root.setAttribute("name", this.getName() != null ? this.getName() : "");
        root.setAttribute("summary", this.getSummary() != null ? this.getSummary() : "");
        root.setAttribute("description", this.getDescription() != null ? this.getDescription() : "");
        root.setAttribute("codeVisibility", this.getCodeVisibility() != null ? this.getCodeVisibility() : "");
        root.setAttribute("idGenomeBuild", this.getIdGenomeBuild() != null ? this.getIdGenomeBuild().toString() : "");
        root.setAttribute("idAppUser", this.getIdAppUser() != null ? this.getIdAppUser().toString() : "");
        root.setAttribute("idLab", this.getIdLab() != null ? this.getIdLab().toString() : "");
        root.setAttribute("idInstitution", this.getIdInstitution() != null ? this.getIdInstitution().toString() : "");
        root.setAttribute("owner", this.getIdAppUser() != null ? dh.getAppUserObject(this.getIdAppUser()).getDisplayName() : "");
        root.setAttribute("genomeBuild", genomeBuild.getGenomeBuildName() );
        String orgString = DictionaryManager.getDisplay("hci.gnomex.model.OrganismLite", genomeBuild.getIdOrganism().toString());
        root.setAttribute("idOrganism", genomeBuild.getIdOrganism() != null ? genomeBuild.getIdOrganism().toString() : "");
        root.setAttribute("organism", orgString != null ? orgString : "");
        root.setAttribute("securityGroup", this.getLab() != null ? this.getLab().getName() : "");
        root.setAttribute("createdBy", this.getCreatedBy() != null ? this.getCreatedBy() : "");
        root.setAttribute("createDate", this.getCreateDate() != null ? this.formatDate(this.getCreateDate()) : "");
        root.setAttribute("number", this.getNumber());
        root.setAttribute("folderCount", this.getFolderCount() != null ? this.getFolderCount().toString() : "");

        // Only show data track folders and data track files for detail
        // (when data_root is provided).
        // Also look for files that can be linked to the UCSC Genome Browser
        if (data_root != null) {
            root.setAttribute("folderCount", Integer.valueOf(this.getFolders().size()).toString());
            Element foldersNode = new Element("DataTrackFolders");
            root.addContent(foldersNode);
            for (DataTrackFolder dtf : (Set<DataTrackFolder>) this.getFolders()) {
                Element folderNode = new Element("DataTrackFolder");
                foldersNode.addContent(folderNode);
                folderNode.setAttribute("name", dtf.getQualifiedName());
            }
            Element filesNode = new Element("Files");
            root.addContent(filesNode);

            String filePath = getDirectory(data_root);
            File fd = new File(filePath);
            if (this.getDataTrackFiles() != null && this.getDataTrackFiles().size() > 0) {
                // We have linked analysis files
                Element dirNode = new Element("Dir");
                filesNode.addContent(dirNode);
                dirNode.setAttribute("name", this.getFileName());
                dirNode.setAttribute("url", filePath);

                String ucscLinkFile = "none";
                if (this.getDataTrackFiles() != null && this.getDataTrackFiles().size() > 0) {
                    for (DataTrackFile dtFile : (Set<DataTrackFile>) this.getDataTrackFiles()) {
                        ucscLinkFile = appendFileXML(dtFile.getAssociatedFilePath(analysis_data_root), dirNode, null, dtFile, ucscLinkFile);
                    }
                }
                dirNode.setAttribute("ucscLinkFile", ucscLinkFile);
            } else if (fd.exists()) {
                // We have files in the Data Track folder
                Element dirNode = new Element("Dir");
                filesNode.addContent(dirNode);
                dirNode.setAttribute("name", this.getFileName());
                dirNode.setAttribute("url", filePath);
                String ucscLinkFile = "none";
                ucscLinkFile = appendFileXML(filePath, dirNode, null, null, ucscLinkFile);
                dirNode.setAttribute("ucscLinkFile", ucscLinkFile);
            }
        }

        // Show list of collaborators.  Only show for
        // annotation detail (when data_root is provided)
        if (data_root != null) {
            if (getCollaborators() != null) {
                Element collaboratorsNode = new Element("Collaborators");
                root.addContent(collaboratorsNode);
                for (Iterator i = getCollaborators().iterator(); i.hasNext(); ) {
                    AppUser u = (AppUser) i.next();
                    Element userNode = new Element("AppUser");
                    collaboratorsNode.addContent(userNode);
                    userNode.setAttribute("idAppUser", u.getIdAppUser().toString());
                    userNode.setAttribute("name", u.getDisplayName());
                    userNode.setAttribute("userDisplayName", u.getDisplayName());
                }
            }
        }

        // Show list of topics.  Only show for
        // annotation detail (when data_root is provided)
        if (data_root != null) {
            if (getTopics() != null) {
                Element topicsNode = new Element("topics");
                root.addContent(topicsNode);
                for (Iterator i = getTopics().iterator(); i.hasNext(); ) {
                    Topic t = (Topic) i.next();
                    Element topicNode = new Element("Topic");
                    topicsNode.addContent(topicNode);
                    topicNode.setAttribute("idTopic", t.getIdTopic().toString());
                    topicNode.setAttribute("name", t.getName());
                    topicNode.setAttribute("description", t.getDescription() == null ? "" : t.getDescription());
                    topicNode.setAttribute("idAppUser", t.getIdAppUser() == null ? "" : t.getIdAppUser().toString());
                    topicNode.setAttribute("idLab", t.getIdLab() == null ? "" : t.getIdLab().toString());
                    topicNode.setAttribute("createdBy", t.getCreatedBy() == null ? "" : t.getCreatedBy());
                    topicNode.setAttribute("createDate", t.getCreateDate() == null ? "" : this.formatDate(t.getCreateDate()));
                    topicNode.setAttribute("idParentTopic", t.getIdParentTopic() == null ? "" : t.getIdParentTopic().toString());
                }
            }
        }

        // Show list of possible collaborators.  Only show
        // for annotation detail (when data_root is provided).
        if (data_root != null) {
            if (getLab() != null) {
                TreeMap<String, AppUser> possibleCollaboratorMap = new TreeMap<String, AppUser>();

                Element possibleCollaboratorsNode = new Element("PossibleCollaborators");
                root.addContent(possibleCollaboratorsNode);

                for (Iterator i = getLab().getMembers().iterator(); i.hasNext(); ) {
                    AppUser user = (AppUser) i.next();
                    possibleCollaboratorMap.put(user.getDisplayName(), user);
                }
                for (Iterator i = getLab().getCollaborators().iterator(); i.hasNext(); ) {
                    AppUser user = (AppUser) i.next();
                    possibleCollaboratorMap.put(user.getDisplayName(), user);
                }
                for (Iterator i = getLab().getManagers().iterator(); i.hasNext(); ) {
                    AppUser user = (AppUser) i.next();
                    possibleCollaboratorMap.put(user.getDisplayName(), user);
                }

                for (Iterator i = possibleCollaboratorMap.keySet().iterator(); i.hasNext(); ) {
                    String name = (String) i.next();
                    AppUser user = possibleCollaboratorMap.get(name);
                    Element userNode = new Element("AppUser");
                    possibleCollaboratorsNode.addContent(userNode);
                    userNode.setAttribute("idAppUser", user.getIdAppUser().toString());
                    userNode.setAttribute("name", user.getDisplayName());
                    userNode.setAttribute("userDisplayName", user.getDisplayName());
                }

            }

        }

        // Show list of possible institutes.  Only show for
        // annotation detail (when data_root is provided).
        if (data_root != null) {
            if (getLab() != null) {
                Element institutesNode = new Element("PossibleInstitutions");
                root.addContent(institutesNode);
                Element emptyNode = new Element("Institution");
                institutesNode.addContent(emptyNode);
                emptyNode.setAttribute("idInstitution", "");
                emptyNode.setAttribute("name", "");

                for (Iterator i = lab.getInstitutions().iterator(); i.hasNext(); ) {
                    Institution institute = (Institution) i.next();
                    Element userNode = new Element("Institution");
                    institutesNode.addContent(userNode);
                    userNode.setAttribute("idInstitution", institute.getIdInstitution().toString());
                    userNode.setAttribute("name", institute.getInstitution());
                    userNode.setAttribute("isDefault", institute.getIsDefault());
                }

            }

        }

        // Show list annotation properties.
        // Only show for data track detail (when data_root is provided).
        if (data_root != null) {
            Element propertiesNode = new Element("DataTrackProperties");
            root.addContent(propertiesNode);
            for (Property property : dh.getPropertyList()) {

                if (property.getForDataTrack() == null || !property.getForDataTrack().equals("Y")) {
                    continue;
                }

                PropertyEntry ap = null;
                for (Iterator i = getPropertyEntries().iterator(); i.hasNext(); ) {
                    PropertyEntry propertyEntry = (PropertyEntry) i.next();
                    if (propertyEntry.getIdProperty().equals(property.getIdProperty())) {
                        ap = propertyEntry;
                        break;
                    }
                }

                // Skip property if it has no data and is not active.
                if (ap == null && property.getIsActive().equals("N")) {
                    continue;
                }

                Element propNode = new Element("PropertyEntry");
                propertiesNode.addContent(propNode);
                propNode.setAttribute("idPropertyEntry", ap != null ? ap.getIdPropertyEntry().toString() : "");
                propNode.setAttribute("name", property.getName());
                propNode.setAttribute("value", ap != null && ap.getValue() != null ? ap.getValue() : "");
                propNode.setAttribute("codePropertyType", property.getCodePropertyType());
                propNode.setAttribute("idProperty", property.getIdProperty().toString());
                propNode.setAttribute("isRequired", property.getIsRequired());

                Property.appendEntryContentXML(property, ap, propNode);
            }
        }


        root.setAttribute("canRead", secAdvisor.canRead(this) ? "Y" : "N");
        root.setAttribute("canWrite", secAdvisor.canUpdate(this) ? "Y" : "N");

        return doc;
    }

    public void removeFiles(String data_root) throws IOException {

        String filePath = getDirectory(data_root);
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
                        Logger.getLogger(DataTrack.class.getName()).log(Level.WARNING, "Unable to delete file " + fileName);
                    }
                }

            }

            // Delete the data track directory
            boolean success = dir.delete();
            if (!success) {
                Logger.getLogger(DataTrack.class.getName()).log(Level.WARNING, "Unable to delete directory " + filePath);
            }
        }
    }

    public String getDirectory(String data_root) {
      /*
	  String dataPath = null;
	  if (this.getDataPath() != null && !this.getDataPath().equals("")) {
      dataPath = this.getDataPath();
    } else {
      dataPath = data_root;
    }

	  return dataPath + this.getFileName();
	  */
        return data_root + this.getFileName();
    }

    /**
     * This is the name of the directory that contains the annotation files
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isBarGraphData(String data_root, String analysis_data_root) throws IOException {
        boolean isExtension = false;
        for (File childFile : getFiles(data_root, analysis_data_root)) {
            if (childFile.getName().endsWith("bar")) {
                isExtension = true;
                break;
            }
        }
        return isExtension;
    }

    @SuppressWarnings("unchecked")
    public List<File> getFiles(String data_root, String analysis_data_root) throws IOException {

        ArrayList<File> files = new ArrayList<File>();

        String filePath = getDirectory(data_root);
        File dir = new File(filePath);

        if (dir.exists()) {
            String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; x++) {
                    String fileName = filePath + "/" + childFileNames[x];
                    File f = new File(fileName);
                    files.add(f);
                }

            }
        }

        // Now list any analysis files associated with this dataTrack
        if (this.getDataTrackFiles() != null && this.getDataTrackFiles().size() > 0) {
            for (DataTrackFile dtFile : (Set<DataTrackFile>) this.getDataTrackFiles()) {
                String fileName = dtFile.getAssociatedFilePath(analysis_data_root);
                File f = new File(fileName);
                files.add(f);
            }
        }

        return files;
    }

    public Set getDataTrackFiles() {
        return dataTrackFiles;
    }

    public void setDataTrackFiles(Set dataTrackFiles) {
        this.dataTrackFiles = dataTrackFiles;
    }

    public boolean isBamData(String data_root, String analysis_data_root) throws IOException {
        boolean isExtension = false;
        for (File childFile : getFiles(data_root, analysis_data_root)) {
            if (childFile.getName().endsWith("bam")) {
                isExtension = true;
                break;
            }
        }
        return isExtension;

    }

    public boolean isUseqGraphData(String data_root, String analysis_data_root) throws IOException {
        boolean isExtension = false;
        for (File childFile : getFiles(data_root, analysis_data_root)) {
            if (USeqUtilities.USEQ_ARCHIVE.matcher(childFile.getName()).matches()) {
                isExtension = true;
                break;
            }
        }
        return isExtension;
    }

    public int getFileCount(String data_root, String analysis_data_root) throws IOException {
        return getFiles(data_root, analysis_data_root).size();
    }

    public String getQualifiedFileName(String data_root, String analysis_data_root) throws IOException {
        List<File> files = getFiles(data_root, analysis_data_root);

        String filePath = getDirectory(data_root);
        if (files != null) {
            //one file return file
            if (files.size() == 1) {
                File file = files.get(0);
                filePath = file.getAbsolutePath();
            }
            //multiple files, might contain a useq file with URL link files (xxx.bw, xxx.bb) that should be skipped or bam and it's associated bai index file
            else {
                for (File f : files) {
                    String fileName = f.getName();
                    //bam?
                    if (fileName.endsWith("bam")) {
                        filePath = f.getAbsolutePath();
                        break;
                    }
                    //useq?
                    else if (fileName.endsWith(USeqUtilities.USEQ_EXTENSION_WITH_PERIOD)) {
                        filePath = f.getAbsolutePath();
                        break;
                    }
                }
            }
            //make sure it's not a ucsc big file xxx.bw, or xxx.bb
            if (filePath.endsWith(".bb") || filePath.endsWith(".bw")) {

                filePath = "";
            } else {
                //bar files should return the directory so don't do anything
            }


        }
        return filePath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public java.sql.Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(java.sql.Date createDate) {
        this.createDate = createDate;
    }

    public String getIsLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(String isLoaded) {
        this.isLoaded = isLoaded;
    }

    public Set getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(Set collaborators) {
        this.collaborators = collaborators;
    }

    public Set getPropertyEntries() {
        return propertyEntries;
    }

    public void setPropertyEntries(Set propertyEntries) {
        this.propertyEntries = propertyEntries;
    }

    public void registerMethodsToExcludeFromXML() {
        this.excludeMethodFromXML("getFolders");
        this.excludeMethodFromXML("getFolderCount");
        this.excludeMethodFromXML("getCollaborators");
        this.excludeMethodFromXML("getPropertyEntries");
        this.excludeMethodFromXML("getDataTrackFiles");
        this.excludeMethodFromXML("getExcludedMethodsMap");
    }

    /*
     * This is a convenience method used by GetRequest, GetAnalysis, GetDataTrack to fill in the XML for a "related" analysis.
     * This experiment may be related in terms of the Experiment->Analysis->DataTrack links or the links to Topics.
     */
    public Element appendBasicXML(SecurityAdvisor secAdvisor, Element parentNode) throws UnknownPermissionException {
        Element dtNode = new Element("DataTrack");
        dtNode.setAttribute("idDataTrack", this.getIdDataTrack().toString());
        dtNode.setAttribute("label", this.getNumber() + " " + (secAdvisor.canRead(this) ? (this.getName() != null ? this.getName() : "") : "(Not authorized)"));
        dtNode.setAttribute("codeVisibility", this.getCodeVisibility());
        dtNode.setAttribute("number", this.getNumber());
        parentNode.addContent(dtNode);
        return dtNode;
    }

    public Integer getIdDataTrack() {
        return idDataTrack;
    }

    public void setIdDataTrack(Integer idDataTrack) {
        this.idDataTrack = idDataTrack;
    }

    public String getNumber() {
        if (this.getIdDataTrack() != null) {
            return "DT" + this.getIdDataTrack();
        } else {
            return "";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeVisibility() {
        return codeVisibility;
    }

    public void setCodeVisibility(String codeVisibility) {
        this.codeVisibility = codeVisibility;
    }

    public Lab getLab() {
        return lab;
    }

    public Integer getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(Integer idInstitution) {
        this.idInstitution = idInstitution;
    }

    public void setLab(Lab lab) {
        this.lab = lab;
    }

}
