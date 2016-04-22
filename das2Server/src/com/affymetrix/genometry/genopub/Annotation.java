//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometry.genopub;

import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import org.dom4j.Element;
import java.util.TreeMap;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.DocumentHelper;
import org.dom4j.Document;
import java.util.Map;
import java.sql.Date;
import java.util.Set;
import java.io.Serializable;

public class Annotation implements Serializable, Owned
{
    public static final String PROP_NAME = "name";
    public static final String PROP_SUMMARY = "summary";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_OWNER = "owner";
    public static final String PROP_OWNER_EMAIL = "owner_institute";
    public static final String PROP_OWNER_INSTITUTE = "owner_email";
    public static final String PROP_GROUP = "group";
    public static final String PROP_GROUP_CONTACT = "group_contact";
    public static final String PROP_GROUP_EMAIL = "group_email";
    public static final String PROP_GROUP_INSTITUTE = "group_institute";
    public static final String PROP_VISIBILITY = "visibility";
    public static final String PROP_INSTITUTE = "institute";
    public static final String PROP_ANALYSIS_TYPE = "analysis_type";
    public static final String PROP_EXPERIMENT_METHOD = "experiment_method";
    public static final String PROP_EXPERIMENT_PLATFORM = "experiment_platform";
    public static final String PROP_URL = "url";
    private Integer idAnnotation;
    private String name;
    private String summary;
    private String description;
    private String codeVisibility;
    private String fileName;
    private String dataPath;
    private Integer idGenomeVersion;
    private Set annotationGroupings;
    private Integer idUser;
    private Integer idUserGroup;
    private UserGroup userGroup;
    private Integer idInstitute;
    private String createdBy;
    private Date createDate;
    private String isLoaded;
    private Set collaborators;
    private Set annotationProperties;
    private Map<String, Object> props;

    public Integer getIdAnnotation() {
        return this.idAnnotation;
    }

    public void setIdAnnotation(final Integer idAnnotation) {
        this.idAnnotation = idAnnotation;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCodeVisibility() {
        return this.codeVisibility;
    }

    public void setCodeVisibility(final String codeVisibility) {
        this.codeVisibility = codeVisibility;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getDataPath() {
        return this.dataPath;
    }

    public void setDataPath(final String dataPath) {
        this.dataPath = dataPath;
    }

    public Integer getIdGenomeVersion() {
        return this.idGenomeVersion;
    }

    public void setIdGenomeVersion(final Integer idGenomeVersion) {
        this.idGenomeVersion = idGenomeVersion;
    }

    public Set getAnnotationGroupings() {
        return this.annotationGroupings;
    }

    public void setAnnotationGroupings(final Set annotationGroupings) {
        this.annotationGroupings = annotationGroupings;
    }

    public Integer getIdUser() {
        return this.idUser;
    }

    public void setIdUser(final Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdUserGroup() {
        return this.idUserGroup;
    }

    public void setIdUserGroup(final Integer idUserGroup) {
        this.idUserGroup = idUserGroup;
    }

    @Override
    public boolean isOwner(final Integer idUser) {
        return this.getIdUser() != null && this.getIdUser().equals(idUser);
    }

    @Override
    public boolean isUserGroup(final Integer idUserGroup) {
        return this.getIdUserGroup() != null && this.getIdUserGroup().equals(idUserGroup);
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(final String summary) {
        this.summary = summary;
    }

    public String getNumber() {
        if (this.getIdAnnotation() != null) {
            return "A" + this.getIdAnnotation();
        }
        return "";
    }

    public Document getXML(final GenoPubSecurity genoPubSecurity, final DictionaryHelper dh, final String data_root) throws Exception {
        final Document doc = DocumentHelper.createDocument();
        final Element root = doc.addElement("Annotation");
        final GenomeVersion genomeVersion = dh.getGenomeVersion(this.getIdGenomeVersion());
        if (genomeVersion == null) {
            Logger.getLogger(Annotation.class.getName()).log(Level.SEVERE, "Unable to find genome version " + this.getIdGenomeVersion() + " for annotation " + this.getName());
            throw new Exception("Unable to find genome version " + this.getIdGenomeVersion() + " for annotation " + this.getName());
        }
        root.addAttribute("idAnnotation", this.getIdAnnotation().toString());
        root.addAttribute("number", this.getNumber());
        root.addAttribute("label", this.getName());
        root.addAttribute("name", this.getName());
        root.addAttribute("summary", this.getSummary());
        root.addAttribute("description", this.getDescription());
        root.addAttribute("codeVisibility", this.getCodeVisibility());
        root.addAttribute("idGenomeVersion", (this.getIdGenomeVersion() != null) ? this.getIdGenomeVersion().toString() : "");
        root.addAttribute("idUser", (this.getIdUser() != null) ? this.getIdUser().toString() : "");
        root.addAttribute("idUserGroup", (this.getIdUserGroup() != null) ? this.getIdUserGroup().toString() : "");
        root.addAttribute("idInstitute", (this.getIdInstitute() != null) ? this.getIdInstitute().toString() : "");
        root.addAttribute("owner", dh.getUserFullName(this.getIdUser()));
        root.addAttribute("genomeVersion", (genomeVersion != null) ? genomeVersion.getName() : "");
        root.addAttribute("organism", dh.getOrganismName(genomeVersion.getIdOrganism()));
        root.addAttribute("securityGroup", dh.getUserGroupName(this.getIdUserGroup()));
        root.addAttribute("createdBy", (this.getCreatedBy() != null) ? this.getCreatedBy() : "");
        root.addAttribute("createDate", (this.getCreateDate() != null) ? Util.formatDate(this.getCreateDate()) : "");
        root.addAttribute("annotationGroupingCount", Integer.valueOf(this.getAnnotationGroupings().size()).toString());
        root.addAttribute("number", this.getNumber());
        if (data_root != null) {
            final Element agsNode = root.addElement("AnnotationGroupings");
            for (final AnnotationGrouping ag : (Set<AnnotationGrouping>) this.getAnnotationGroupings()) {
                final Element agNode = agsNode.addElement("AnnotationGrouping");
                agNode.addAttribute("name", ag.getQualifiedName());
            }
            final Element filesNode = root.addElement("Files");
            final String filePath = this.getDirectory(data_root);
            final File fd = new File(filePath);
            if (fd.exists()) {
                final Element fileNode = filesNode.addElement("Dir");
                fileNode.addAttribute("name", this.getFileName());
                fileNode.addAttribute("url", filePath);
                final String ucscLinkFile = appendFileXML(filePath, fileNode, null);
                root.addAttribute("ucscLinkFile", ucscLinkFile);
            }
        }
        if (data_root != null && this.getCollaborators() != null) {
            final Element collaboratorsNode = root.addElement("Collaborators");
            for (final User u : (Set<User>) this.getCollaborators()) {
                final Element userNode = collaboratorsNode.addElement("User");
                userNode.addAttribute("idUser", u.getIdUser().toString());
                userNode.addAttribute("name", u.getName());
                userNode.addAttribute("userDisplayName", u.getUserDisplayName());
            }
        }
        if (data_root != null && this.getUserGroup() != null) {
            final TreeMap<String, User> possibleCollaboratorMap = new TreeMap<String, User>();
            final Element possibleCollaboratorsNode = root.addElement("PossibleCollaborators");
            for (final User user : (Set<User>) this.getUserGroup().getMembers()) {
                possibleCollaboratorMap.put(user.getName(), user);
            }
            for (final User user : (Set<User>) this.getUserGroup().getCollaborators()) {
                possibleCollaboratorMap.put(user.getName(), user);
            }
            for (final User user : (Set<User>)this.getUserGroup().getManagers()) {
                possibleCollaboratorMap.put(user.getName(), user);
            }
            for (final String name : possibleCollaboratorMap.keySet()) {
                final User user2 = possibleCollaboratorMap.get(name);
                final Element userNode2 = possibleCollaboratorsNode.addElement("User");
                userNode2.addAttribute("idUser", user2.getIdUser().toString());
                userNode2.addAttribute("name", user2.getName());
                userNode2.addAttribute("userDisplayName", user2.getUserDisplayName());
            }
        }
        if (data_root != null && this.getUserGroup() != null) {
            final Element institutesNode = root.addElement("PossibleInstitutes");
            final Element emptyNode = institutesNode.addElement("Institute");
            emptyNode.addAttribute("idInstitute", "");
            emptyNode.addAttribute("name", "");
            for (final Institute institute : (Set<Institute>) this.userGroup.getInstitutes()) {
                final Element userNode3 = institutesNode.addElement("Institute");
                userNode3.addAttribute("idInstitute", institute.getIdInstitute().toString());
                userNode3.addAttribute("name", institute.getName());
            }
        }
        if (data_root != null) {
            final Element propertiesNode = root.addElement("AnnotationProperties");
            for (final Property property : dh.getPropertyList()) {
                final Element propNode = propertiesNode.addElement("AnnotationProperty");
                AnnotationProperty ap = null;
                for (final AnnotationProperty annotationProperty : (Set<AnnotationProperty>) this.getAnnotationProperties()) {
                    if (annotationProperty.getProperty().getIdProperty().equals(property.getIdProperty())) {
                        ap = annotationProperty;
                        break;
                    }
                }
                propNode.addAttribute("idAnnotationProperty", (ap != null) ? ap.getIdAnnotationProperty().toString() : "");
                propNode.addAttribute("name", property.getName());
                propNode.addAttribute("value", (ap != null && ap.getValue() != null) ? ap.getValue() : "");
                propNode.addAttribute("codePropertyType", property.getCodePropertyType());
                propNode.addAttribute("idProperty", property.getIdProperty().toString());
                if (ap != null && ap.getValues() != null && ap.getValues().size() > 0) {
                    for (final AnnotationPropertyValue av : (Set<AnnotationPropertyValue>) ap.getValues()) {
                        final Element valueNode = propNode.addElement("AnnotationPropertyValue");
                        valueNode.addAttribute("idAnnotationPropertyValue", av.getIdAnnotationPropertyValue().toString());
                        valueNode.addAttribute("value", (av.getValue() != null) ? av.getValue() : "");
                    }
                }
                if (property.getCodePropertyType().equals("URL")) {
                    final Element emptyNode2 = propNode.addElement("AnnotationPropertyValue");
                    emptyNode2.addAttribute("idAnnotationPropertyValue", "");
                    emptyNode2.addAttribute("value", "Enter URL here...");
                }
                if (property.getOptions() != null && property.getOptions().size() > 0) {
                    for (final PropertyOption option : (Set<PropertyOption>) property.getOptions()) {
                        final Element optionNode = propNode.addElement("PropertyOption");
                        optionNode.addAttribute("idPropertyOption", option.getIdPropertyOption().toString());
                        optionNode.addAttribute("name", option.getName());
                        boolean isSelected = false;
                        if (ap != null && ap.getOptions() != null) {
                            for (final PropertyOption optionSelected : (Set<PropertyOption>) ap.getOptions()) {
                                if (optionSelected.getIdPropertyOption().equals(option.getIdPropertyOption())) {
                                    isSelected = true;
                                    break;
                                }
                            }
                        }
                        optionNode.addAttribute("selected", isSelected ? "Y" : "N");
                    }
                }
            }
        }
        root.addAttribute("canRead", genoPubSecurity.canRead(this) ? "Y" : "N");
        root.addAttribute("canWrite", genoPubSecurity.canWrite(this) ? "Y" : "N");
        return doc;
    }

    public static String appendFileXML(final String filePath, final Element parentNode, final String subDirName) {
        final File fd = new File(filePath);
        String ucscLinkFile = "none";
        if (fd.isDirectory()) {
            final String[] fileList = fd.list();
            for (int x = 0; x < fileList.length; ++x) {
                final String fileName = filePath + "/" + fileList[x];
                final File f1 = new File(fileName);
                if (fileList[x].endsWith(".useq") && ucscLinkFile.equals("none")) {
                    ucscLinkFile = "convert";
                }
                else if (fileList[x].endsWith(".bb") || fileList[x].endsWith(".bw") || fileList[x].endsWith(".bam")) {
                    ucscLinkFile = "link";
                }
                String displayName = "";
                if (subDirName != null) {
                    displayName = subDirName + "/" + fileList[x];
                }
                else {
                    displayName = f1.getName();
                }
                if (f1.isDirectory()) {
                    final Element fileNode = parentNode.addElement("Dir");
                    fileNode.addAttribute("name", displayName);
                    fileNode.addAttribute("url", fileName);
                    appendFileXML(fileName, fileNode, (subDirName != null) ? (subDirName + "/" + f1.getName()) : f1.getName());
                }
                else {
                    final Element fileNode = parentNode.addElement("File");
                    final long kb = Util.getKilobytes(f1.length());
                    final String kilobytes = kb + " kb";
                    fileNode.addAttribute("name", displayName);
                    fileNode.addAttribute("url", fileName);
                    fileNode.addAttribute("size", kilobytes);
                    fileNode.addAttribute("lastModified", Util.formatDate(new Date(f1.lastModified())));
                }
            }
        }
        return ucscLinkFile;
    }

    public void removeFiles(final String data_root) throws IOException {
        final String filePath = this.getDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; ++x) {
                    final String fileName = filePath + "/" + childFileNames[x];
                    final File f = new File(fileName);
                    final boolean success = f.delete();
                    if (!success) {
                        Logger.getLogger(Annotation.class.getName()).log(Level.WARNING, "Unable to delete file " + fileName);
                    }
                }
            }
            final boolean success2 = dir.delete();
            if (!success2) {
                Logger.getLogger(Annotation.class.getName()).log(Level.WARNING, "Unable to delete directory " + filePath);
            }
        }
    }

    public List<File> getFiles(final String data_root) throws IOException {
        final ArrayList<File> files = new ArrayList<File>();
        final String filePath = this.getDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; ++x) {
                    final String fileName = filePath + "/" + childFileNames[x];
                    final File f = new File(fileName);
                    files.add(f);
                }
            }
        }
        return files;
    }

    public boolean isBarGraphData(final String data_root) throws IOException {
        boolean isExtension = false;
        final String filePath = this.getDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; ++x) {
                    if (childFileNames[x].endsWith("bar")) {
                        isExtension = true;
                        break;
                    }
                }
            }
        }
        return isExtension;
    }

    public boolean isBamData(final String data_root) throws IOException {
        boolean isExtension = false;
        final String filePath = this.getDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; ++x) {
                    if (childFileNames[x].endsWith("bam")) {
                        isExtension = true;
                        break;
                    }
                }
            }
        }
        return isExtension;
    }

    public boolean isUseqGraphData(final String data_root) throws IOException {
        boolean isExtension = false;
        final String filePath = this.getDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                for (int x = 0; x < childFileNames.length; ++x) {
                    if (USeqUtilities.USEQ_ARCHIVE.matcher(childFileNames[x]).matches()) {
                        isExtension = true;
                        break;
                    }
                }
            }
        }
        return isExtension;
    }

    public int getFileCount(final String data_root) throws IOException {
        int fileCount = 0;
        final String filePath = this.getDirectory(data_root);
        final File dir = new File(filePath);
        if (dir.exists()) {
            final String[] childFileNames = dir.list();
            if (childFileNames != null) {
                fileCount = childFileNames.length;
            }
        }
        return fileCount;
    }

    public String getQualifiedFileName(final String data_root) {
        if (this.getFileName() == null || this.getFileName().equals("")) {
            return "";
        }
        String filePath = this.getDirectory(data_root);
        final File file = new File(filePath);
        final File[] files = file.listFiles();
        if (files != null) {
            if (files.length == 1) {
                final String[] childFileNames = file.list();
                filePath = filePath + "/" + childFileNames[0];
            }
            else {
                for (final File f : files) {
                    final String fileName = f.getName();
                    if (fileName.endsWith("bam")) {
                        filePath = filePath + "/" + fileName;
                        break;
                    }
                    if (fileName.endsWith(".useq")) {
                        filePath = filePath + "/" + fileName;
                        break;
                    }
                }
            }
            if (filePath.endsWith(".bb") || filePath.endsWith(".bw")) {
                filePath = "";
            }
        }
        return filePath;
    }

    public String getDirectory(final String data_root) {
        String dataPath = null;
        if (this.getDataPath() != null && !this.getDataPath().equals("")) {
            dataPath = this.getDataPath();
        }
        else {
            dataPath = data_root;
        }
        return dataPath + this.getFileName();
    }

    public Map<String, Object> loadProps(final DictionaryHelper dictionaryHelper) {
        (this.props = new TreeMap<String, Object>()).put("name", this.getName());
        this.props.put("description", (this.getDescription() != null) ? Util.removeHTMLTags(this.getDescription()) : "");
        this.props.put("summary", (this.getSummary() != null) ? Util.removeHTMLTags(this.getSummary()) : "");
        this.props.put("visibility", Visibility.getDisplay(this.getCodeVisibility()));
        this.props.put("owner", (this.getIdUser() != null) ? dictionaryHelper.getUserFullName(this.getIdUser()) : "");
        this.props.put("owner_institute", (this.getIdUser() != null) ? dictionaryHelper.getUserEmail(this.getIdUser()) : "");
        this.props.put("owner_email", (this.getIdUser() != null) ? dictionaryHelper.getUserInstitute(this.getIdUser()) : "");
        this.props.put("group", (this.getIdUserGroup() != null) ? dictionaryHelper.getUserGroupName(this.getIdUserGroup()) : "");
        this.props.put("group_contact", (this.getIdUserGroup() != null) ? dictionaryHelper.getUserGroupContact(this.getIdUserGroup()) : "");
        this.props.put("group_email", (this.getIdUserGroup() != null) ? dictionaryHelper.getUserGroupEmail(this.getIdUserGroup()) : "");
        this.props.put("group_institute", (this.getIdInstitute() != null) ? dictionaryHelper.getInstituteName(this.getIdInstitute()) : "");
        for (final AnnotationProperty ap : (Set<AnnotationProperty>) this.getAnnotationProperties()) {
            this.props.put(ap.getName(), (ap.getValue() != null) ? ap.getValue() : "");
        }
        return this.props;
    }

    public Map<String, Object> getProperties() {
        return this.props;
    }

    public Map<String, Object> cloneProperties() {
        return this.props;
    }

    public Object getProperty(final String key) {
        if (this.props != null) {
            return this.props.get(key);
        }
        return null;
    }

    public boolean setProperty(final String key, final Object val) {
        if (this.props != null) {
            this.props.put(key, val);
            return true;
        }
        return false;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(final String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(final Date createDate) {
        this.createDate = createDate;
    }

    public String getIsLoaded() {
        return this.isLoaded;
    }

    public void setIsLoaded(final String isLoaded) {
        this.isLoaded = isLoaded;
    }

    public UserGroup getUserGroup() {
        return this.userGroup;
    }

    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Set getCollaborators() {
        return this.collaborators;
    }

    public void setCollaborators(final Set collaborators) {
        this.collaborators = collaborators;
    }

    public Integer getIdInstitute() {
        return this.idInstitute;
    }

    public void setIdInstitute(final Integer idInstitute) {
        this.idInstitute = idInstitute;
    }

    public Set getAnnotationProperties() {
        return this.annotationProperties;
    }

    public void setAnnotationProperties(final Set annotationProperties) {
        this.annotationProperties = annotationProperties;
    }
}
