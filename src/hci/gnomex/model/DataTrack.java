package hci.gnomex.model;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import hci.dictionary.utility.DictionaryManager;
import hci.framework.model.DetailObject;
import hci.framework.model.FieldFormatter;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.USeqUtilities;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.DataTrackUtil;



public class DataTrack extends DetailObject implements Serializable, Owned {

	public static final String PROP_NAME                = "name";
	public static final String PROP_SUMMARY             = "summary";
	public static final String PROP_DESCRIPTION         = "description";
	public static final String PROP_OWNER               = "owner";
	public static final String PROP_OWNER_EMAIL         = "owner_institute";
	public static final String PROP_OWNER_INSTITUTE     = "owner_email";
	public static final String PROP_GROUP               = "group";
	public static final String PROP_GROUP_CONTACT       = "group_contact";
	public static final String PROP_GROUP_EMAIL         = "group_email";
	public static final String PROP_GROUP_INSTITUTE     = "group_institute";
	public static final String PROP_VISIBILITY          = "visibility";
	public static final String PROP_INSTITUTE           = "institute";
	public static final String PROP_ANALYSIS_TYPE       = "analysis_type";
	public static final String PROP_EXPERIMENT_METHOD   = "experiment_method";
	public static final String PROP_EXPERIMENT_PLATFORM = "experiment_platform";
	public static final String PROP_URL                 = "url";


	private Integer             idDataTrack;
	private String              name;
	private String              summary;
	private String              description;
	private String              codeVisibility;
	private String              fileName;
	private String              dataPath;
	private Integer             idGenomeBuild;
	private Set                 folders;
	private Integer             idAppUser;
	private Integer             idLab;
	private Lab                 lab;
	private Integer             idInstitution;
	private String              createdBy;
	private java.sql.Date       createDate;
	private String              isLoaded;
  private Set                 collaborators;
	private Set                 propertyEntries;

	private Map<String, Object> props;  // tag/value representation of annotation properties


	public Integer getIdDataTrack() {
		return idDataTrack;
	}
	public void setIdDataTrack(Integer idDataTrack) {
		this.idDataTrack = idDataTrack;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCodeVisibility() {
		return codeVisibility;
	}
	public void setCodeVisibility(String codeVisibility) {
		this.codeVisibility = codeVisibility;
	}
	/**This is the name of the directory that contains the annotation files*/
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getNumber() {
		if (this.getIdDataTrack() != null) {
			return "A" + this.getIdDataTrack();
		} else {
			return "";
		}
	}

	public Integer getIdGenomeBuild() {
    return idGenomeBuild;
  }
  public Set getFolders() {
    return folders;
  }
  public Integer getIdAppUser() {
    return idAppUser;
  }
  public Integer getIdLab() {
    return idLab;
  }
  public Lab getLab() {
    return lab;
  }
  public Integer getIdInstitution() {
    return idInstitution;
  }
  public void setIdGenomeBuild(Integer idGenomeBuild) {
    this.idGenomeBuild = idGenomeBuild;
  }
  public void setFolders(Set folders) {
    this.folders = folders;
  }
  public void setIdAppUser(Integer idAppUser) {
    this.idAppUser = idAppUser;
  }
  public void setIdLab(Integer idLab) {
    this.idLab = idLab;
  }
  public void setLab(Lab lab) {
    this.lab = lab;
  }
  public void setIdInstitution(Integer idInstitution) {
    this.idInstitution = idInstitution;
  }
  @SuppressWarnings("unchecked")
	public Document getXML(SecurityAdvisor secAdvisor, DictionaryHelper dh, String data_root) throws Exception {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("DataTrack");

		GenomeBuild genomeBuild = dh.getGenomeBuildObject(this.getIdGenomeBuild());
		if (genomeBuild == null) {
			Logger.getLogger(DataTrack.class.getName()).log(Level.SEVERE,"Unable to find genome version " + this.getIdGenomeBuild() + " for annotation " + this.getName());
			throw new Exception("Unable to find genome version " + this.getIdGenomeBuild() + " for annotation " + this.getName());
		}

		root.addAttribute("idDataTrack", this.getIdDataTrack().toString());
		root.addAttribute("number", this.getNumber());
		root.addAttribute("label", this.getName());
		root.addAttribute("name", this.getName());
		root.addAttribute("summary", this.getSummary());
		root.addAttribute("description", this.getDescription());
		root.addAttribute("codeVisibility", this.getCodeVisibility());
		root.addAttribute("idGenomeBuild", this.getIdGenomeBuild() != null ? this.getIdGenomeBuild().toString() : "");
		root.addAttribute("idAppUser", this.getIdAppUser() != null ? this.getIdAppUser().toString() : "");
		root.addAttribute("idLab", this.getLab() != null ? this.getLab().toString() : "");
		root.addAttribute("idInstitution", this.getIdInstitution() != null ? this.getIdInstitution().toString() : "");
		root.addAttribute("owner", this.getIdAppUser() != null ? dh.getAppUserObject(this.getIdAppUser()).getDisplayName() : "");
		root.addAttribute("genomeBuild", genomeBuild != null ? genomeBuild.getGenomeBuildName() : "");
		root.addAttribute("organism", DictionaryManager.getDisplay("hci.gnomex.model.Organism", genomeBuild.getIdOrganism().toString()));
		root.addAttribute("securityGroup", this.getLab() != null ? this.getLab().getName() : "");
		root.addAttribute("createdBy", this.getCreatedBy() != null ? this.getCreatedBy() : "");
		root.addAttribute("createDate", this.getCreateDate() != null ? this.formatDate(this.getCreateDate()) : "");
		//root.addAttribute("folderCount", Integer.valueOf(this.getFolders().size()).toString());
		root.addAttribute("number", this.getNumber());

		// Only show data track folders and data track files for detail
		// (when data_root is provided).
		// Also look for files that can be linked to the UCSC Genome Browser
		if (data_root != null) {
			Element agsNode = root.addElement("DataTrackFolders");
			for(DataTrackFolder ag : (Set<DataTrackFolder>)this.getFolders()) {
				Element agNode = agsNode.addElement("DataTrackFolder");
				agNode.addAttribute("name", ag.getQualifiedName());
			}
			Element filesNode = root.addElement("Files");

			String filePath = getDirectory(data_root);
			File fd = new File(filePath);
			if (fd.exists()) {
				Element fileNode = filesNode.addElement("Dir");
				fileNode.addAttribute("name", this.getFileName());
				fileNode.addAttribute("url", filePath);
				String ucscLinkFile = appendFileXML(filePath, fileNode, null);
				root.addAttribute("ucscLinkFile", ucscLinkFile);
			}			
		}

		// Show list of collaborators.  Only show for
		// annotation detail (when data_root is provided)
		if (data_root != null) {
			if (getCollaborators() != null) {
				Element collaboratorsNode = root.addElement("Collaborators");
				for(Iterator i = getCollaborators().iterator(); i.hasNext();) {
					AppUser u = (AppUser)i.next();
					Element userNode = collaboratorsNode.addElement("AppUser");
					userNode.addAttribute("idAppUser", u.getIdAppUser().toString());  
					userNode.addAttribute("name", u.getDisplayName());
					userNode.addAttribute("userDisplayName", u.getDisplayName());
				}
			}
		}

		// Show list of possible collaborators.  Only show
		// for annotation detail (when data_root is provided).
		if (data_root != null) {
			if (getLab() != null) {
				TreeMap<String, AppUser> possibleCollaboratorMap = new TreeMap<String, AppUser>();

				Element possibleCollaboratorsNode = root.addElement("PossibleCollaborators");

				for(Iterator i = getLab().getMembers().iterator(); i.hasNext();) {
					AppUser user = (AppUser)i.next();
					possibleCollaboratorMap.put(user.getDisplayName(), user);
				}
				for(Iterator i = getLab().getCollaborators().iterator(); i.hasNext();) {
					AppUser user = (AppUser)i.next();
					possibleCollaboratorMap.put(user.getDisplayName(), user);
				}
				for(Iterator i = getLab().getManagers().iterator(); i.hasNext();) {
					AppUser user = (AppUser)i.next();
					possibleCollaboratorMap.put(user.getDisplayName(), user);
				}

				for(Iterator i = possibleCollaboratorMap.keySet().iterator(); i.hasNext();) {
					String name = (String)i.next();
					AppUser user = possibleCollaboratorMap.get(name);
					Element userNode = possibleCollaboratorsNode.addElement("AppUser");
					userNode.addAttribute("idAppUser", user.getIdAppUser().toString());  
					userNode.addAttribute("name", user.getDisplayName());
					userNode.addAttribute("userDisplayName", user.getDisplayName());
				}

			}

		}

		// Show list of possible institutes.  Only show for
		// annotation detail (when data_root is provided).
		if (data_root != null) {
			if (getLab() != null) {
				Element institutesNode = root.addElement("PossibleInstitutions");

				Element emptyNode = institutesNode.addElement("Institution");
				emptyNode.addAttribute("idInstitution", "");  
				emptyNode.addAttribute("name", "");

				for(Iterator i = lab.getInstitutions().iterator(); i.hasNext();) {
					Institution institute = (Institution)i.next();
					Element userNode = institutesNode.addElement("Institution");
					userNode.addAttribute("idInstitution", institute.getIdInstitution().toString());  
					userNode.addAttribute("name", institute.getInstitution());
				}

			}

		}

		// Show list annotation properties.
		// Only show for data track detail (when data_root is provided).
		if (data_root != null) {
			Element propertiesNode = root.addElement("DataTrackProperties");
			for (Property property : dh.getPropertyList()) {

			  if (property.getForDataTrack() == null || !property.getForDataTrack().equals("Y")) {
			    continue;
			  }
			  
				Element propNode = propertiesNode.addElement("PropertyEntry");

				PropertyEntry ap = null;
				for(Iterator i = getPropertyEntries().iterator(); i.hasNext();) {
					PropertyEntry propertyEntry = (PropertyEntry)i.next();
					if (propertyEntry.getIdProperty().equals(property.getIdProperty())) {
						ap = propertyEntry;
						break;
					}
				}

				propNode.addAttribute("idPropertyEntry", ap != null ? ap.getIdPropertyEntry().toString() : "");  
				propNode.addAttribute("name", property.getName());
				propNode.addAttribute("value", ap != null && ap.getValue() != null ? ap.getValue() : "");
				propNode.addAttribute("codePropertyType", property.getCodePropertyType());
				propNode.addAttribute("idProperty", property.getIdProperty().toString());

				if (ap != null && ap.getValues() != null && ap.getValues().size() > 0) {
					for (Iterator i1 = ap.getValues().iterator(); i1.hasNext();) {
						PropertyEntryValue av = (PropertyEntryValue)i1.next();
						Element valueNode = propNode.addElement("PropertyEntryValue");
						valueNode.addAttribute("idPropertyEntryValue", av.getIdPropertyEntryValue().toString());
						valueNode.addAttribute("value", av.getValue() != null ? av.getValue() : "");
            valueNode.addAttribute("url", av.getUrl() != null ? av.getUrl() : "");
            valueNode.addAttribute("urlDisplay", av.getUrlDisplay() != null ? av.getUrlDisplay() : "");
            valueNode.addAttribute("urlAlias", av.getUrlAlias() != null ? av.getUrlAlias() : "");
					}
				}
				if (property.getCodePropertyType().equals(PropertyType.URL)) {
					// Add an empty value for URL
					Element emptyNode = propNode.addElement("PropertyEntryValue");
					emptyNode.addAttribute("idPropertyEntryValue", "");
					emptyNode.addAttribute("url", "Enter URL here...");
					emptyNode.addAttribute("urlAlias", "Enter alias here...");
          emptyNode.addAttribute("urlDisplay", "");
          emptyNode.addAttribute("value", "");
				}

				if (property.getOptions() != null && property.getOptions().size() > 0) {
					for (Iterator i1 = property.getOptions().iterator(); i1.hasNext();) {
						PropertyOption option = (PropertyOption)i1.next();
						Element optionNode = propNode.addElement("PropertyOption");
						optionNode.addAttribute("idPropertyOption", option.getIdPropertyOption().toString());
						optionNode.addAttribute("name", option.getOption());
						boolean isSelected = false;
						if (ap != null && ap.getOptions() != null) {
							for (Iterator i2 = ap.getOptions().iterator(); i2.hasNext();) {
								PropertyOption optionSelected = (PropertyOption)i2.next();
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


	  root.addAttribute("canRead", secAdvisor.canRead(this) ? "Y" : "N");
		root.addAttribute("canWrite", secAdvisor.canUpdate(this) ? "Y" : "N");

		return doc;
	}
  

	/**Returns 'none' if no files available for UCSC linking, 'convert' for files requiring conversion, or 'link' if they are ready to go.*/
	public static String appendFileXML(String filePath, Element parentNode, String subDirName) {
		File fd = new File(filePath);
		String ucscLinkFile = "none";
		if (fd.isDirectory()) {
			String[] fileList = fd.list();
			for (int x = 0; x < fileList.length; x++) {
				String fileName = filePath + "/" + fileList[x];
				File f1 = new File(fileName);
				
				//link file?
				if (fileList[x].endsWith(".useq") && ucscLinkFile.equals("none")) ucscLinkFile = "convert";
				else if (fileList[x].endsWith(".bb") || fileList[x].endsWith(".bw") || fileList[x].endsWith(".bam")) ucscLinkFile = "link";

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
					appendFileXML(fileName, fileNode,
							subDirName != null ? subDirName + "/"
									+ f1.getName() : f1.getName());
				} else {
					Element fileNode = parentNode.addElement("File");

					long kb = DataTrackUtil.getKilobytes(f1.length());
					String kilobytes = kb + " kb";

					fileNode.addAttribute("name", displayName);
					fileNode.addAttribute("url", fileName);
					fileNode.addAttribute("size", kilobytes);
					fileNode.addAttribute("lastModified", new FieldFormatter().formatDate(new java.sql.Date(f1.lastModified())));

				}
			}
		}
		return ucscLinkFile;
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

			// Delete the annotation directory
			boolean success = dir.delete();	    	
			if (!success) {
				Logger.getLogger(DataTrack.class.getName()).log(Level.WARNING, "Unable to delete directory " + filePath);
			}
		}
	}


	public List<File> getFiles(String data_root) throws IOException {

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

		return files;
	}
	public boolean isBarGraphData(String data_root) throws IOException {
		boolean isExtension = false;
		String filePath = getDirectory(data_root);
		File dir = new File(filePath);

		if (dir.exists()) {
			// Delete the files in the directory
			String[] childFileNames = dir.list();
			if (childFileNames != null) {
				for (int x = 0; x < childFileNames.length; x++) {
					if (childFileNames[x].endsWith("bar")) {
						isExtension = true;
						break;
					}
				}

			}
		}
		return isExtension;
	}

	public boolean isBamData(String data_root) throws IOException {
		boolean isExtension = false;
		String filePath = getDirectory(data_root);
		File dir = new File(filePath);

		if (dir.exists()) {
			String[] childFileNames = dir.list();
			if (childFileNames != null) {
				for (int x = 0; x < childFileNames.length; x++) {
					if (childFileNames[x].endsWith("bam")) {
						isExtension = true;
						break;
					}
				}

			}
		}
		return isExtension;
	}

	public boolean isUseqGraphData(String data_root) throws IOException {
		boolean isExtension = false;
		String filePath = getDirectory(data_root);
		File dir = new File(filePath);
		if (dir.exists()) {
			String[] childFileNames = dir.list();
			if (childFileNames != null) {
				for (int x = 0; x < childFileNames.length; x++) {
					if (USeqUtilities.USEQ_ARCHIVE.matcher(childFileNames[x]).matches() ) {
						isExtension = true;
						break;
					}
				}
			}
		}
		return isExtension;
	}

	public int getFileCount(String data_root) throws IOException {
		int fileCount = 0;
		String filePath = getDirectory(data_root);
		File dir = new File(filePath);

		if (dir.exists()) {
			// Delete the files in the directory
			String[] childFileNames = dir.list();
			if (childFileNames != null) {
				fileCount = childFileNames.length;
			}
		}
		return fileCount;
	}

	public String getQualifiedFileName(String data_root) {
		if (this.getFileName() == null || this.getFileName().equals("")) {
			return "";
		}
		
		String filePath =  getDirectory(data_root);
		File file = new File(filePath);

		File[] files = file.listFiles();

		if (files != null) {
			//one file return file
			if (files.length == 1){
				String[] childFileNames = file.list();
				filePath += "/" + childFileNames[0];
			}
			//multiple files, might contain a useq file with URL link files (xxx.bw, xxx.bb) that should be skipped or bam and it's associated bai index file
			else {
				for (File f: files){
					String fileName = f.getName();
					//bam?
					if (fileName.endsWith("bam")) {
						filePath += "/" + fileName;
						break;
					}
					//useq?
					else if (fileName.endsWith(USeqUtilities.USEQ_EXTENSION_WITH_PERIOD)) {
						filePath += "/" + fileName;
						break;
					}
				}
			}
			//make sure it's not a ucsc big file xxx.bw, or xxx.bb
			if (filePath.endsWith(".bb") || filePath.endsWith(".bw")) filePath = "";
			
			//bar files should return the directory so don't do anything
			
		}
		return filePath;
	}

	public String getDirectory(String data_root) {
	  String dataPath = null;
	  if (this.getDataPath() != null && !this.getDataPath().equals("")) {
      dataPath = this.getDataPath();
    } else {
      dataPath = data_root;
    }
	  return dataPath + this.getFileName();
	}


	@SuppressWarnings("unchecked")
	public Map<String, Object> loadProps(DictionaryHelper dictionaryHelper) {
		props = new TreeMap<String, Object>();
		props.put(PROP_NAME, this.getName());
		props.put(PROP_DESCRIPTION, this.getDescription() != null ? DataTrackUtil.removeHTMLTags(this.getDescription()) : "");
		props.put(PROP_SUMMARY, this.getSummary() != null ? DataTrackUtil.removeHTMLTags(this.getSummary()) : "");
		props.put(PROP_VISIBILITY,  DictionaryManager.getDisplay("hci.gnomex.model.Visibility", this.getCodeVisibility()));
		props.put(PROP_OWNER, this.getIdAppUser() != null ? dictionaryHelper.getAppUserObject(this.getIdAppUser()).getDisplayName() : "");
		props.put(PROP_OWNER_EMAIL, this.getIdAppUser() != null ? dictionaryHelper.getAppUserObject(this.getIdAppUser()).getEmail() : "");
		props.put(PROP_OWNER_INSTITUTE, this.getIdAppUser() != null ? dictionaryHelper.getAppUserObject(this.getIdAppUser()).getInstitute() : "");
		props.put(PROP_GROUP, this.getLab() != null ? this.getLab().getName() : "");
		props.put(PROP_GROUP_CONTACT, this.getLab() != null ? this.getLab().getContactName() : "");
		props.put(PROP_GROUP_EMAIL, this.getLab() != null ? getLab().getContactEmail() : "");
		props.put(PROP_GROUP_INSTITUTE, this.getIdInstitution() != null ? DictionaryManager.getDisplay("hci.gnomex.model.Institution", this.getIdInstitution().toString()) : "");

		for (PropertyEntry propEntry : (Set<PropertyEntry>)this.getPropertyEntries()) {
		  Property property = dictionaryHelper.getPropertyObject(propEntry.getIdProperty());
			props.put(property.getName(), propEntry.getValue() != null ? propEntry.getValue() : "");
		}
		return props;
	}

	public Map<String,Object> getProperties() {
	  if (props == null) {
	    props = new TreeMap<String, Object>();
	  }
		return props;
	}
	public Map<String,Object> cloneProperties() {
		return props;
	}

	public Object getProperty(String key) {
		if (props != null) {
			return props.get(key);
		} else {
			return null;
		}
	}
	public boolean setProperty(String key, Object val) {
		if (props != null) {
			props.put(key, val);
			return true;
		} else {
			return false;
		}
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
    this.excludeMethodFromXML("getProperties");
    this.excludeMethodFromXML("getFolders");
    this.excludeMethodFromXML("getCollaborators");
    this.excludeMethodFromXML("getPropertyEntries");
    this.excludeMethodFromXML("getExcludedMethodsMap");

  }
}
