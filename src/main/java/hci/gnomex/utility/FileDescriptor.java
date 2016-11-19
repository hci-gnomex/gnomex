package hci.gnomex.utility;

import hci.framework.model.DetailObject;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.apache.log4j.Logger;

/**
 * Created by u0395021 on 5/4/2016.
 */
public class FileDescriptor extends DetailObject implements Serializable {
	private static final double KB = Math.pow(2, 10);
	private static final double MB = Math.pow(2, 20);
	private static final double GB = Math.pow(2, 30);

	private static final Logger LOG = Logger.getLogger(FileDescriptor.class);

	private String displayName;
	private String number; // this is the object number (ie: analysis number, product order number, request number)
	private long fileSize;
	private String fileName;
	private Date lastModifyDate;
	private String type;
	private String zipEntryName;
	private String directoryName;
	private String flowCellIndicator;
	private String absolutePath;
	private String simpleName;
	private String qualifiedFilePath;
	private Integer idLab;

	private String idFileString; // this is a string that contains the id of the object file (analysisFile, experimentFIle, productOrderFile)

	private String comments;
	private Integer id;
	private Date uploadDate;

	private String baseFilePath;
	private List children = new ArrayList();
	private boolean found = false;

	public FileDescriptor() {

	}

	public FileDescriptor(String number, String displayName, File file, String baseDir) {
		this.setNumber(number);
		this.setDisplayName(displayName);
		this.setFileSize(file.length());
		this.setLastModifyDate(new Date(file.lastModified()));
		this.setAbsolutePath(file.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
		this.setSimpleName(file.getName());

		try {
			if (Util.isSymlink(file)) {
				this.setFileName(file.getPath().replace("\\", Constants.FILE_SEPARATOR));
			} else {
				this.setFileName(file.getCanonicalPath().replace("\\", Constants.FILE_SEPARATOR));
			}
		} catch (Exception e) {
			LOG.error("Error in FileDescriptor", e);
			this.setFileName(file.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
		}
		// this.setZipEntryName(PropertyDictionaryHelper.parseZipEntryName(baseDir, this.getFileName()));
		if (new File(baseDir).isAbsolute()) {
			this.setZipEntryName(PropertyDictionaryHelper.parseZipEntryName(baseDir, this.getFileName()));
		} else {
			this.setZipEntryName(baseDir);
		}

		String ext = "";

		// first scan DataTrack types, some are xxx.vcf.gz or xxx.vcf.gz.tbi
		// Nix
		for (String t : Constants.DATATRACK_FILE_EXTENSIONS) {
			if (this.getFileName().toLowerCase().endsWith(t)) {
				ext = t.substring(1);
				// watch out for .bam.bai
				if (ext.equals("bam.bai"))
					ext = "bai";
				break;
			}
		}
		// any found? if not then do as before
		if (ext.equals("")) {
			String[] fileParts = file.getName().split("\\.");
			if (fileParts != null && fileParts.length >= 2) {
				ext = fileParts[fileParts.length - 1];
			}
		}

		this.setType(ext);

	}

	public String getFileSizeText() {

		long theFileSize = getFileSize();

		long size = 0;
		String sizeTxt = "";
		if (theFileSize > GB) {
			size = Math.round(theFileSize / GB);
			sizeTxt = size + " " + " gb";
		} else if (theFileSize > MB) {
			size = Math.round(theFileSize / MB);
			sizeTxt = size + " " + " mb";
		} else if (theFileSize > KB) {
			size = Math.round(theFileSize / KB);
			sizeTxt = size + " " + " kb";
		} else {
			sizeTxt = theFileSize + " b";
		}
		return sizeTxt;

	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public long getChildFileSize() {

		if (isDirectory()) {
			long total = 0;
			for (Iterator i = children.iterator(); i.hasNext();) {
				FileDescriptor fd = (FileDescriptor) i.next();
				total += fd.getChildFileSize();
			}
			return total;

		} else {
			return fileSize;
		}

	}

	public long getFileSize() {

		if (isDirectory()) {
			long theFileSize = 0;
			theFileSize = this.getChildFileSize();
			return theFileSize;
		} else {
			return fileSize;
		}
	}

	public String getType() {
		return type;
	}

	public java.util.Date getLastModifyDate() {
		return lastModifyDate;
	}

	public String getFileName() {
		return fileName;
	}

	public String getZipEntryName() {
		return zipEntryName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setZipEntryName(String zipEntryName) {
		this.zipEntryName = zipEntryName;
	}

	public String getMainFolderName(Session sess, String serverName, Integer idCoreFacility) {
		if (fileName != null && !fileName.equals("")) {
			return PropertyDictionaryHelper.getInstance(sess).parseMainFolderName(serverName, fileName, idCoreFacility);
		} else {
			return "";
		}
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public String getLastModifyDateDisplay() {
		if (this.lastModifyDate != null) {
			return this.formatDate(this.lastModifyDate, DATE_OUTPUT_SQL);
		} else {
			return "";
		}
	}

	public String getIsSelected() {
		return "false";
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getFlowCellIndicator() {
		return flowCellIndicator;
	}

	public void setFlowCellIndicator(String flowCellIndicator) {
		this.flowCellIndicator = flowCellIndicator;
	}

	private Boolean isDirectory() {
		return (this.type != null && this.type.equals("dir"));
	}

	public String getViewURL(String viewType) {
		String viewURL = "";
		String dirParm = this.getDirectoryName() != null && !this.getDirectoryName().equals("") ? "&dir=" + this.getDirectoryName() : "";
		dirParm.replace(Constants.FILE_SEPARATOR, "&#47;");
		if (!isDirectory()) {
			Boolean found = false;
			for (String ext : Constants.FILE_EXTENSIONS_FOR_VIEW) {
				if (this.fileName.toLowerCase().endsWith(ext)) {
					found = true;
					break;
				}
			}
			if (found) {
				Double maxSize = Math.pow(2, 20) * 50;
				try {
					maxSize = Math.pow(2, 20)
							* Double.parseDouble(PropertyDictionaryHelper.getInstance(null).getProperty(PropertyDictionary.FILE_MAX_VIEWABLE_SIZE));
				} catch (Exception ex) {
				}

				if (this.fileSize < maxSize) { // Only allow viewing for files under specified max MB
					String vfilename = this.getFileName();
					vfilename = Util.encodeName(vfilename);

					viewURL = viewType + "&fileName=" + vfilename + "&view=Y" + dirParm;

				}
			}
		}
		return viewURL;
	}

	public void isFound(boolean isFound) {
		this.found = isFound;
	}

	public boolean isFound() {
		return this.found;
	}

	public void registerMethodsToExcludeFromXML() {
		this.excludeMethodFromXML("getCreateDate");
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getQualifiedFilePath() {
		return qualifiedFilePath;
	}

	public void setQualifiedFilePath(String qualifiedFilePath) {
		this.qualifiedFilePath = qualifiedFilePath;
	}

	public String getBaseFilePath() {
		return baseFilePath;
	}

	public void setBaseFilePath(String baseFilePath) {
		this.baseFilePath = baseFilePath;
	}

	public String getQualifiedFileName() {
		String fullPathName = "";

		if (qualifiedFilePath != null && qualifiedFilePath.length() != 0) {
			fullPathName += getQualifiedFilePath() + Constants.FILE_SEPARATOR;
		}
		fullPathName += getDisplayName();

		return fullPathName;
	}

	public String getDirectoryNumber(int fileDirectoryLength) {
		String number = "";
		if (fileName != null && !fileName.equals("")) {
			// Get the directory name starting after the year
			String relativePath = fileName.substring(fileDirectoryLength + 5);
			String tokens[] = relativePath.split(Constants.FILE_SEPARATOR, 2);
			if (tokens == null || tokens.length == 1) {
				tokens = relativePath.split(Constants.FILE_SEPARATOR, 2);
			}
			if (tokens.length == 2) {
				number = tokens[0];
			}
		}
		return number;
	}

	public Integer getIdLab() {
		return idLab;
	}

	public void setIdLab(Integer idLab) {
		this.idLab = idLab;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getIdFileString() {
		return idFileString;
	}

	public void setIdFileString(String idFileString) {
		this.idFileString = idFileString;
	}

	// The methods below will most likely only be used for FileDescriptors.
	public String getIsUCSCViewerAllowed() {
		boolean found = false;
		if (this.getType() != null) {
			String extension = "." + this.getType();
			for (int x = 0; x < Constants.FILE_EXTENSIONS_FOR_UCSC_LINKS_NO_INDEX.length; x++) {
				if (extension.equalsIgnoreCase(Constants.FILE_EXTENSIONS_FOR_UCSC_LINKS_NO_INDEX[x])) {
					found = true;
					break;
				}
			}

		}
		return (found ? "Y" : "N");
	}

	public String getIsIGVViewerAllowed() {
		boolean found = false;
		if (this.getType() != null) {
			String extension = "." + this.getType();
			for (int x = 0; x < Constants.FILE_EXTENSIONS_FOR_IGV_LINKS_NO_INDEX.length; x++) {
				if (extension.equalsIgnoreCase(Constants.FILE_EXTENSIONS_FOR_IGV_LINKS_NO_INDEX[x])) {
					found = true;
					break;
				}
			}

		}
		return (found ? "Y" : "N");
	}

	public String getIsBAMIOBIOViewerAllowed() {
		boolean found = false;
		if (this.getType() != null) {
			String extension = "." + this.getType();
			for (int x = 0; x < Constants.FILE_EXTENSIONS_FOR_BAMIOBIO_LINKS_NO_INDEX.length; x++) {
				if (extension.equalsIgnoreCase(Constants.FILE_EXTENSIONS_FOR_BAMIOBIO_LINKS_NO_INDEX[x])) {
					found = true;
					break;
				}
			}

		}
		return (found ? "Y" : "N");
	}

	public String getIsURLLinkAllowed() {
		boolean found = false;
		if (this.getType() != null) {
			String extension = "." + this.getType();
			for (int x = 0; x < Constants.DATATRACK_FILE_EXTENSIONS_NO_INDEX.length; x++) {
				if (extension.equalsIgnoreCase(Constants.DATATRACK_FILE_EXTENSIONS_NO_INDEX[x])) {
					found = true;
					break;
				}
			}

		}
		return (found ? "Y" : "N");
	}

	public String getIsGENELinkAllowed() {
		boolean found = false;
		if (this.getType() != null) {
			String extension = "." + this.getType();

		if (extension.equalsIgnoreCase(Constants.PED_FILE_EXTENSION)) {
			found = true;
		}
	}

		return (found ? "Y" : "N");
	}

	public String getIsSupportedDataTrack() {
		boolean found = false;
		if (this.getType() != null) {
			String extension = "." + this.getType();
			for (int x = 0; x < Constants.DATATRACK_FILE_EXTENSIONS.length; x++) {
				if (extension.equalsIgnoreCase(Constants.DATATRACK_FILE_EXTENSIONS[x])) {
					found = true;
					break;
				}
			}

		}
		return (found ? "Y" : "N");
	}

}
