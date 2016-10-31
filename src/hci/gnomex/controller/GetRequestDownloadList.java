package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestDownloadFilter;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.UploadDownloadHelper;
import hci.gnomex.utility.Util;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class GetRequestDownloadList extends GNomExCommand implements Serializable {

private static Logger LOG = Logger.getLogger(GetRequestDownloadList.class);

private RequestDownloadFilter filter;
private String includeUploadStagingDir = "Y";
private HashMap slideDesignMap = new HashMap();
private static final String DUMMY_DIRECTORY = "DUMMY_DIRECTORY";
private static final String QUALITY_CONTROL_DIRECTORY = "bioanalysis";

private String serverName;
private String baseDirFlowCell;
private SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
private static boolean noLinkedSamples;
private static String whereami;
private static String viewType;

public void validate() {
}

public void loadCommand(HttpServletRequest request, HttpSession session) {

	filter = new RequestDownloadFilter();
	HashMap errors = this.loadDetailObject(request, filter);
	this.addInvalidFields(errors);

	if (request.getParameter("includeUploadStagingDir") != null
			&& !request.getParameter("includeUploadStagingDir").equals("")) {
		includeUploadStagingDir = request.getParameter("includeUploadStagingDir");
	}

	if (request.getParameter("whereami") != null) {
		whereami = request.getParameter("whereami");
	} else {
		whereami = "unknown";
	}

	if (request.getParameter("whereami") != null) {
		whereami = request.getParameter("whereami");
	} else {
		whereami = "unknown";
	}

	String idRequestStringList = request.getParameter("idRequestStringList");
	if (idRequestStringList != null && !idRequestStringList.equals("")) {
		List idRequests = new ArrayList<Integer>();
		String[] keys = idRequestStringList.split(":");
		for (int i = 0; i < keys.length; i++) {
			String idRequest = keys[i];
			idRequests.add(new Integer(idRequest));
		}
		filter.setIdRequests(idRequests);
	}

	if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !filter.hasCriteria()) {
		this.addInvalidField("filterRequired", "Please enter at least one search criterion.");
	}

	serverName = request.getServerName();

}

public Command execute() throws RollBackCommandException {

	long startTime = System.currentTimeMillis();
	String reqNumber = "";

	try {

		Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
		DictionaryHelper dh = DictionaryHelper.getInstance(sess);
		baseDirFlowCell = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
				PropertyDictionaryHelper.PROPERTY_FLOWCELL_DIRECTORY);

		List slideDesigns = sess.createQuery("SELECT sd from SlideDesign sd ").list();
		for (Iterator i = slideDesigns.iterator(); i.hasNext();) {
			SlideDesign sd = (SlideDesign) i.next();
			slideDesignMap.put(sd.getIdSlideDesign(), sd.getName());
		}

		StringBuffer buf = filter.getMicroarrayResultQuery(this.getSecAdvisor(), dh);
		LOG.debug("Query for GetRequestDownloadList (1): " + buf.toString());
		List rows1 = sess.createQuery(buf.toString()).list();
		TreeMap rowMap = new TreeMap(new HybSampleComparator());
		for (Iterator i = rows1.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();

			String requestNumber = (String) row[1];
			reqNumber = requestNumber;
			String codeRequestCategory = (String) row[2];
			String hybNumber = row[5] == null || row[5].equals("") ? "" : (String) row[5];
			Integer idCoreFacility = (Integer) row[31];

			String createDate = this.formatDate((java.util.Date) row[0]);
			String tokens[] = createDate.split("/");
			String createMonth = tokens[0];
			String createDay = tokens[1];
			String createYear = tokens[2];
			String sortDate = createYear + createMonth + createDay;

			String baseKey = createYear + Constants.DOWNLOAD_KEY_SEPARATOR + sortDate
					+ Constants.DOWNLOAD_KEY_SEPARATOR + requestNumber;
			String key = baseKey + Constants.DOWNLOAD_KEY_SEPARATOR + hybNumber + Constants.DOWNLOAD_KEY_SEPARATOR
					+ idCoreFacility;

			rowMap.put(key, row);
		}

		buf = filter.getSolexaResultQuery(this.getSecAdvisor(), dh);
		LOG.debug("Query for GetRequestDownloadList (2): " + buf.toString());
		List rows2 = sess.createQuery(buf.toString()).list();

		for (Iterator i = rows2.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();

			String requestNumber = (String) row[1];
			reqNumber = requestNumber;
			String codeRequestCategory = (String) row[2];

			String createDate = this.formatDate((java.util.Date) row[0]);
			String tokens[] = createDate.split("/");
			String createMonth = tokens[0];
			String createDay = tokens[1];
			String createYear = tokens[2];
			String sortDate = createYear + createMonth + createDay;
			Integer idCoreFacility = (Integer) row[31];

			// The data files are always in the base request number folder,
			// not the folder with the revision number. (example: all
			// files will be in 7633R even though request # is now 7633R1).
			String requestNumberBase = Request.getBaseRequestNumber(requestNumber);

			String baseKey = createYear + Constants.DOWNLOAD_KEY_SEPARATOR + sortDate
					+ Constants.DOWNLOAD_KEY_SEPARATOR + requestNumber;

			// Now read the request directory to identify all its subdirectories
			String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, idCoreFacility,
					PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
			Set folders = this.getRequestDownloadFolders(baseDir, requestNumberBase,
					yearFormat.format((java.util.Date) row[0]), codeRequestCategory);
			this.hashFolders(folders, rowMap, dh, baseKey, row);
		}

		buf = filter.getSolexaFlowCellQuery(this.getSecAdvisor(), dh);
		LOG.debug("Query for get illumina flow cell: " + buf.toString());
		List flowCellRows = sess.createQuery(buf.toString()).list();
		HashMap flowCellMap = new HashMap();
		for (Iterator i = flowCellRows.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();

			String requestNumber = (String) row[0];
			reqNumber = requestNumber;
			String flowCellNumber = (String) row[1];
			java.sql.Date createDate = (java.sql.Date) row[2];
			Integer idCoreFacility = (Integer) row[3];

			List flowCellFolders = (List) flowCellMap.get(requestNumber);
			if (flowCellFolders == null) {
				flowCellFolders = new ArrayList<FlowCellFolder>();
			}
			flowCellFolders.add(new FlowCellFolder(requestNumber, flowCellNumber, createDate, idCoreFacility));

			flowCellMap.put(requestNumber, flowCellFolders);
		}

		buf = filter.getQualityControlResultQuery(this.getSecAdvisor(), dh);
		LOG.debug("Query for GetRequestDownloadList (3): " + buf.toString());
		List rows3 = sess.createQuery(buf.toString()).list();
		Map<Integer, Integer> idsToSkip = this.getSecAdvisor().getBSTXSecurityIdsToExclude(sess, dh, rows3, 21, 2);

		// remember the requestNumbers for use in checkSampleExperimentFile
		List<Integer> requestIdList = new ArrayList<Integer>();

		// we will always get here...
		for (Iterator i = rows3.iterator(); i.hasNext();) {
			Object[] row = (Object[]) i.next();

			if (idsToSkip.get(row[21]) != null) {
				// skip for BSTX security
				continue;
			}

			String requestNumber = (String) row[1];
			reqNumber = requestNumber;
			requestIdList.add((Integer) row[21]);

			// setup viewType
			viewType = Constants.DOWNLOAD_SINGLE_FILE_SERVLET + "?idRequest=" + row[21];

			String codeRequestCategory = (String) row[2];
			String sampleNumber = row[11] == null || row[11].equals("") ? "" : (String) row[11];

			String createDate = this.formatDate((java.util.Date) row[0]);
			String tokens[] = createDate.split("/");
			String createMonth = tokens[0];
			String createDay = tokens[1];
			String createYear = tokens[2];
			String sortDate = createYear + createMonth + createDay;
			Integer idCoreFacility = (Integer) row[31];

			String requestNumberBase = Request.getBaseRequestNumber(requestNumber);

			String baseKey = createYear + Constants.DOWNLOAD_KEY_SEPARATOR + sortDate
					+ Constants.DOWNLOAD_KEY_SEPARATOR + requestNumber;

			// Now read the request directory to identify all its subdirectories
			String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, idCoreFacility,
					PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
			Set folders = this.getRequestDownloadFolders(baseDir, requestNumberBase,
					yearFormat.format((java.util.Date) row[0]), codeRequestCategory);
			if (folders.isEmpty()) {
				// If we didn't add any row map entries (because there are no subdirectories under
				// request directory), just add the base directory so that we get have
				// something in the rowmap to get the root files from.
				rowMap.put(baseKey + Constants.DOWNLOAD_KEY_SEPARATOR + this.DUMMY_DIRECTORY
						+ Constants.DOWNLOAD_KEY_SEPARATOR + idCoreFacility, row);
			} else {
				int foldersHashed = this.hashFolders(folders, rowMap, dh, baseKey, row);

				// If we didn't actually hash any folders (for example upload_staging directory is
				// ignored), then add the dummy directory to the row map so that we
				// have the request in the hash to get the root files from or at least
				// create the Request node.
				if (foldersHashed == 0) {
					rowMap.put(baseKey + Constants.DOWNLOAD_KEY_SEPARATOR + this.DUMMY_DIRECTORY
							+ Constants.DOWNLOAD_KEY_SEPARATOR + idCoreFacility, row);
				}
			}

		} // end of for rows3.iterator

		// if we are never going to find anything in SampleExperimentFile, avoid those queries
		noLinkedSamples = checkSampleExperimentFile(sess, requestIdList);

		boolean alt = false;
		String prevRequestNumber = "";
		Element requestNode = null;

		Document doc = new Document(new Element("RequestDownloadList"));

		// rowmap has all of the directories we are going to look at
		for (Iterator i = rowMap.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			String[] tokens = key.split(Constants.DOWNLOAD_KEY_SEPARATOR);
			String createYear = tokens[0];
			String resultDir = "";
			if (tokens.length > 3) {
				resultDir = tokens[3];
			}

			Object[] row = (Object[]) rowMap.get(key);
			String codeRequestCategory = (String) row[2];
			String hybNumber = (String) row[5];
			String createDate = this.formatDate((java.util.Date) row[0]);
			Integer idRequest = row[21] != null ? (Integer) row[21] : Integer.valueOf(0);
			Integer idCoreFacility = (Integer) row[31];

			viewType = Constants.DOWNLOAD_SINGLE_FILE_SERVLET + "?idRequest=" + row[21];

			String appUserName = "";
			if (row[29] != null) {
				appUserName = (String) row[29];
			}
			if (row[28] != null) {
				if (appUserName.length() > 0) {
					appUserName += ", ";
				}
				appUserName += (String) row[28];
			}

			boolean isSolexaRequest = RequestCategory.isIlluminaRequestCategory(codeRequestCategory);
			boolean isMicroarrayRequest = RequestCategory.isMicroarrayRequestCategory(codeRequestCategory);

			String requestNumber = (String) row[1];

			// first time for this requestNumber?
			if (!requestNumber.equals(prevRequestNumber)) {
				// yes
				alt = !alt;

				RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);

				requestNode = new Element("Request");
				requestNode.setAttribute("displayName", requestNumber);
				requestNode.setAttribute("requestNumber", requestNumber);
				requestNode.setAttribute("idRequest", idRequest.toString());
				requestNode.setAttribute("codeRequestCategory", codeRequestCategory);
				requestNode.setAttribute("icon",
						requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
				requestNode.setAttribute("type",
						requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
				requestNode.setAttribute("isSelected", "false");
				requestNode.setAttribute("state", "unchecked");
				requestNode.setAttribute("isEmpty", "Y"); // will be set to yes if any files exist for downloading
				requestNode.setAttribute("canDelete", "N");
				requestNode.setAttribute("canRename", "N");
				requestNode.setAttribute("info", appUserName);

				doc.getRootElement().addContent(requestNode);

				// Show files under the root experiment directory
				String createDateString = this.formatDate((java.util.Date) row[0]);
				String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, idCoreFacility,
						PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
				addRootFileNodes(baseDir, requestNode, requestNumber, createDateString, null, sess);

			} // end of if first time we have seen this request number

			// Add directories for flow cells
			if (isSolexaRequest) {
				List flowCellNumbers = (List) flowCellMap.get(requestNumber);
				if (flowCellNumbers != null) {
					for (Iterator i1 = flowCellNumbers.iterator(); i1.hasNext();) {
						FlowCellFolder fcFolder = (FlowCellFolder) i1.next();

						String theCreateDate = this.formatDate((java.sql.Date) fcFolder.getCreateDate());
						String dateTokens[] = theCreateDate.split("/");
						String createMonth = dateTokens[0];
						String createDay = dateTokens[1];
						String theCreateYear = dateTokens[2];
						String sortDate = theCreateYear + createMonth + createDay;

						String fcKey = theCreateYear + Constants.DOWNLOAD_KEY_SEPARATOR + sortDate
								+ Constants.DOWNLOAD_KEY_SEPARATOR + fcFolder.getRequestNumber()
								+ Constants.DOWNLOAD_KEY_SEPARATOR + fcFolder.getFlowCellNumber()
								+ Constants.DOWNLOAD_KEY_SEPARATOR + fcFolder.getIdCoreFacility()
								+ Constants.DOWNLOAD_KEY_SEPARATOR
								+ dh.getPropertyDictionary(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG);
						String fcCodeRequestCategory = row[2] == null ? "" : (String) row[2];

						Element n1 = new Element("RequestDownload");
						n1.setAttribute("key", fcKey);
						n1.setAttribute("isSelected", "N");
						n1.setAttribute("state", "unchecked");
						n1.setAttribute("altColor", new Boolean(alt).toString());
						n1.setAttribute("idRequest", row[21].toString());
						n1.setAttribute("createDate", this.formatDate((java.util.Date) row[0]));
						n1.setAttribute("requestNumber", (String) row[1]);
						n1.setAttribute("codeRequestCategory", fcCodeRequestCategory);
						n1.setAttribute("codeApplication", row[3] == null ? "" : (String) row[3]);
						n1.setAttribute("idAppUser", row[4] == null ? "" : ((Integer) row[4]).toString());
						n1.setAttribute("idLab", row[17] == null ? "" : ((Integer) row[17]).toString());
						n1.setAttribute("results", "flow cell quality report");
						n1.setAttribute("hasResults", "Y");
						n1.setAttribute("status", "");
						n1.setAttribute("canDelete", "N");
						n1.setAttribute("canRename", "N");
						n1.setAttribute("displayName",fcFolder.getFlowCellNumber());
						n1.setAttribute("itemNumber", fcFolder.getFlowCellNumber());

						requestNode.addContent(n1);

						addExpandedFileNodes(sess, serverName, baseDirFlowCell, requestNode, n1,
								fcFolder.getRequestNumber(), fcKey, fcCodeRequestCategory, dh, true);
					}
					// We only want to show the list of flow cells once
					// per request.
					flowCellMap.remove(requestNumber);

				}
			}

			prevRequestNumber = requestNumber;
		}

		XMLOutputter out = new org.jdom.output.XMLOutputter();
		this.xmlResult = out.outputString(doc);

		setResponsePage(this.SUCCESS_JSP);
	} catch (Exception e) {
		LOG.error("An exception has occurred in GetRequestDownloadList ", e);
		throw new RollBackCommandException(e.getMessage());
	} finally {
		try {
			//closeReadOnlyHibernateSession;
		} catch (Exception e) {
			LOG.error("Error in getRequestDownloadList", e);
		}
	}

	String dinfo = "GetRequestDownloadList (" + this.getUsername() + " - " + reqNumber + " - " + whereami + "), ";
	Util.showTime(startTime, dinfo);

	return this;
}

private int hashFolders(Set folders, TreeMap rowMap, DictionaryHelper dh, String baseKey, Object[] row) {
	Integer idCoreFacility = (Integer) row[31];
	int foldersHashed = 0;
	for (Iterator i1 = folders.iterator(); i1.hasNext();) {
		String folderName = (String) i1.next();
		if (folderName.equals(Constants.UPLOAD_STAGING_DIR)) {
			continue;
		}
		String key = baseKey + Constants.DOWNLOAD_KEY_SEPARATOR + folderName + Constants.DOWNLOAD_KEY_SEPARATOR
				+ idCoreFacility;
		Object[] newRow = new Object[row.length];
		for (int x = 0; x < row.length; x++) {
			newRow[x] = row[x];
		}
		newRow[5] = folderName;
		rowMap.put(key, newRow);
		foldersHashed++;
	}
	return foldersHashed;
}

public static void addExpandedFileNodes(Session sess, String serverName, String baseDirFlowCell, Element requestNode,
		Element requestDownloadNode, String requestNumber, String key, String codeRequestCategory, DictionaryHelper dh,
		boolean isFlowCellDirectory) throws XMLReflectException {

	long stime = System.currentTimeMillis();
	//
	// Get expanded file list
	//
	Map requestMap = new TreeMap();
	Map directoryMap = new TreeMap();
	List requestNumbers = new ArrayList<String>();
	UploadDownloadHelper.getFileNamesToDownload(sess, serverName, baseDirFlowCell, key, requestNumbers, requestMap,
			directoryMap, dh.getPropertyDictionary(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG));
	List directoryKeys = (List) requestMap.get(requestNumber);
	if (directoryKeys != null) {
		for (Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
			String directoryKey = (String) i1.next();
			String[] dirTokens = directoryKey.split(Constants.DOWNLOAD_KEY_SEPARATOR);
			String directoryName = dirTokens[1];

			List theFiles = (List) directoryMap.get(directoryKey);

			// For each file in the directory
			if (theFiles != null && theFiles.size() > 0) {
				for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
					FileDescriptor fd = (FileDescriptor) i2.next();
					fd.setDirectoryName(directoryName);
					fd.excludeMethodFromXML("getChildren");

					Element fdNode = fd.toXMLDocument(null, fd.DATE_OUTPUT_ALTIO).getRootElement();
					fdNode.setAttribute("canDelete", isFlowCellDirectory ? "N" : "Y");
					fdNode.setAttribute("canRename", isFlowCellDirectory ? "N" : "Y");
					fdNode.setAttribute("isSelected", "N");
					fdNode.setAttribute("state", "unchecked");
					fdNode.setAttribute("linkedSampleNumber", getLinkedSampleNumber(sess, fd.getZipEntryName()));

					if (fd.getChildren().size() > 0) {
						recurseAddChildren(fdNode, fd, isFlowCellDirectory, sess);
					} else {
						fdNode.setAttribute("viewURL", fd.getViewURL(viewType));
					}

					requestDownloadNode.addContent(fdNode);
					requestDownloadNode.setAttribute("isEmpty", "N");
					requestNode.setAttribute("isEmpty", "N");
				}

			} else {
				if (!requestDownloadNode.hasChildren()) {
					requestDownloadNode.setAttribute("isEmpty", "Y");
				}
			}
		}

	}

	String info = "GetRequest addExpandedFileNodes, key: " + key + ", ";
	// Util.showTime(stime, info);
}

private static void recurseAddChildren(Element fdNode, FileDescriptor fd, boolean isFlowCellDirectory, Session sess)
		throws XMLReflectException {
	if (fd.getChildren() == null || fd.getChildren().size() == 0) {
		if (fd.getType().equals("dir")) {
			fdNode.setAttribute("isEmpty", "Y");
		}
	} else if (fd.getChildren() == null || fd.getChildren().size() > 0) {
		if (fd.getType().equals("dir")) {
			fdNode.setAttribute("isEmpty", "N");
		}
	}

	for (Iterator i = fd.getChildren().iterator(); i.hasNext();) {
		FileDescriptor childFd = (FileDescriptor) i.next();

		childFd.excludeMethodFromXML("getChildren");
		Element childFdNode = childFd.toXMLDocument(null, childFd.DATE_OUTPUT_ALTIO).getRootElement();
		childFdNode.setAttribute("isSelected", "N");
		childFdNode.setAttribute("state", "unchecked");
		childFdNode.setAttribute("dir",
				fdNode.getAttributeValue("displayName") != null ? fdNode.getAttributeValue("displayName") : "");

		childFdNode.setAttribute("canDelete", isFlowCellDirectory ? "N" : "Y");
		childFdNode.setAttribute("canRename", isFlowCellDirectory ? "N" : "Y");
		childFdNode.setAttribute("linkedSampleNumber", getLinkedSampleNumber(sess, childFd.getZipEntryName()));

		if (!childFd.getType().equals("dir")) {
			childFdNode.setAttribute("viewURL", childFd.getViewURL(viewType));
		}

		fdNode.addContent(childFdNode);

		if (childFd.getChildren() != null && childFd.getChildren().size() > 0) {
			recurseAddChildren(childFdNode, childFd, isFlowCellDirectory, sess);
		} else {
			if (childFd.getType().equals("dir")) {
				childFdNode.setAttribute("isEmpty", "Y");
			}
		}
	}

}

private boolean checkSampleExperimentFile(Session sess, List requestIdList) {
	boolean nolinkedsamples = true;

	String efsl_enabled = PropertyDictionaryHelper.getInstance(sess).getProperty(
			PropertyDictionary.EXPERIMENT_FILE_SAMPLE_LINKING_ENABLED);
	if (efsl_enabled == null || efsl_enabled.equalsIgnoreCase("N")) {
		return nolinkedsamples;
	}

	StringBuffer buf = new StringBuffer("SELECT count(*) from SampleExperimentFile");
	List results = sess.createQuery(buf.toString()).list();
	int qty = (int) (long) results.get(0);

	if (qty > 0) {
		// check to see if there are any for the experiment files we have
		// buf = new StringBuffer
		// ("SELECT count(*) from SampleExperimentFile sef, ExperimentFile ef where sef.idExpFileRead1 = ef.idExperimentFile and ef.idRequest in (" +
		// Util.listIntToString(requestIdList) + ")");
		buf = new StringBuffer(
				"SELECT count(*) from SampleExperimentFile sef where sef.idExpFileRead1 in (select idExperimentFile from ExperimentFile where idRequest in ("
						+ Util.listIntToString(requestIdList) + "))");
		List results1 = sess.createQuery(buf.toString()).list();
		int qty1 = (int) (long) results1.get(0);

		if (qty1 == 0) {
			// check idExpFileRead2
			// buf = new StringBuffer
			// ("SELECT count(*) from SampleExperimentFile sef, ExperimentFile ef where sef.idExpFileRead2 = ef.idExperimentFile and ef.idRequest in (" +
			// Util.listIntToString(requestIdList) + ")");
			buf = new StringBuffer(
					"SELECT count(*) from SampleExperimentFile sef where sef.idExpFileRead2 in (select idExperimentFile from ExperimentFile where idRequest in ("
							+ Util.listIntToString(requestIdList) + "))");
			List results2 = sess.createQuery(buf.toString()).list();
			int qty2 = (int) (long) results2.get(0);

			if (qty2 > 0) {
				nolinkedsamples = false;
			}
		} else {
			nolinkedsamples = false;
		}
	}

	return nolinkedsamples;
}

private static String getLinkedSampleNumber(Session sess, String fileName) {
	// can we skip this?
	if (noLinkedSamples) {
		return "";
	}

	String queryString = "Select ef from ExperimentFile ef WHERE ef.fileName = :fileName";
	Query query = sess.createQuery(queryString);
	query.setParameter("fileName", fileName.replace("\\", "/"));
	List expFile = query.list();
	if (expFile.size() > 0) {
		ExperimentFile ef = (ExperimentFile) expFile.get(0);
		List sampleNumber = sess.createQuery(
				"Select samp.number from SampleExperimentFile sef JOIN sef.sample as samp where sef.idExpFileRead1 = "
						+ ef.getIdExperimentFile() + " or sef.idExpFileRead2 = " + ef.getIdExperimentFile()).list();
		if (sampleNumber.size() > 0) {
			return (String) sampleNumber.get(0);
		}
	}

	return "";

}

// returns a set containing any folders that exist UNDER the experiment directory
public static Set getRequestDownloadFolders(String baseDir, String requestNumber, String createYear,
		String codeRequestCategory) {

	TreeSet folders = new TreeSet<String>(new FolderComparator(codeRequestCategory));
	String directoryName = baseDir + createYear + Constants.FILE_SEPARATOR + requestNumber;
	File fd = new File(directoryName);

	if (fd.isDirectory()) {
		String[] fileList = fd.list();
		for (int x = 0; x < fileList.length; x++) {
			String fileName = directoryName + Constants.FILE_SEPARATOR + fileList[x];
			File f1 = new File(fileName);
			if (f1.isDirectory()) {
				folders.add(fileList[x]);
			}
		}
	}
	return folders;

}

private void addRootFileNodes(String baseDir, Element requestNode, String requestNumber, String createDate,
		String subDirectory, Session sess) throws Exception {

	String dirTokens[] = createDate.split("/");
	String createYear = dirTokens[2];

	String directoryName = baseDir + Constants.FILE_SEPARATOR + createYear + Constants.FILE_SEPARATOR
			+ Request.getBaseRequestNumber(requestNumber)
			+ (subDirectory != null ? Constants.FILE_SEPARATOR + Constants.UPLOAD_STAGING_DIR : "");
	File fd = new File(directoryName);
	if (fd.exists() && fd.isDirectory()) {
		String[] fileList = fd.list();
		for (int x = 0; x < fileList.length; x++) {
			String fileName = directoryName + Constants.FILE_SEPARATOR + fileList[x];
			File f1 = new File(fileName);

			// bypass temp files
			if (f1.getName().toLowerCase().endsWith("thumbs.db") || f1.getName().toUpperCase().startsWith(".DS_STORE")
					|| f1.getName().startsWith("._")) {
				continue;
			}

			// bypass directories and soft links.
			// if (f1.isDirectory()) { // || Util.isSymlink(f1)) {
			// continue;
			// }

			if (includeUploadStagingDir.equals("N") && f1.getName().equals(Constants.UPLOAD_STAGING_DIR)) {
				continue;
			}

			// Hide that the files are in the upload staging directory. Show them in the root experiment directory instead.
			// It's FileDescriptor that does the hiding
			String zipEntryName = getPathForZipFileName(f1, requestNumber);
			FileDescriptor fdesc = new FileDescriptor(requestNumber, f1.getName(), f1, zipEntryName);
			fdesc.setDirectoryName("");
			fdesc.excludeMethodFromXML("getChildren");

			Element fdNode = fdesc.toXMLDocument(null, fdesc.DATE_OUTPUT_ALTIO).getRootElement();
			fdNode.setAttribute("isSelected", "N");
			fdNode.setAttribute("state", "unchecked");
			fdNode.setAttribute("canDelete", "Y");
			fdNode.setAttribute("canRename", "Y");
			fdNode.setAttribute(
					"linkedSampleNumber",
					getLinkedSampleNumber(sess,
							fileName.substring(fileName.indexOf(Request.getBaseRequestNumber(requestNumber)))));
			fdNode.setAttribute("viewURL", fdesc.getViewURL(viewType));

			if (f1.isDirectory()) {
				fdNode.setAttribute("type", "dir");
				recurseAddFiles(fdNode, f1, requestNumber, "", sess);
			}

			requestNode.addContent(fdNode);
			requestNode.setAttribute("isEmpty", "N");

		}

	}

}

private String getPathForZipFileName(File f, String requestNumber) {
	StringBuffer fname = new StringBuffer();
	fname.append(f.getAbsolutePath()
			.substring(f.getAbsolutePath().indexOf(Request.getBaseRequestNumber(requestNumber))).replace("\\", "/"));
	// fname.append(f.getName());
	return fname.toString();
}

private void recurseAddFiles(Element fdNode, File f1, String requestNumber, String directoryName, Session sess)
		throws Exception {
	String files[] = f1.list();
	String fullPath = f1.getAbsolutePath() + Constants.FILE_SEPARATOR;

	// don't include empty directories
	if (f1.list() == null || f1.list().length == 0) {
		return;
	}

	// don't include upload staging dir if it wasn't requested
	if (includeUploadStagingDir.equals("N") && f1.getName().equals(Constants.UPLOAD_STAGING_DIR)) {
		return;
	}

	if (f1.isDirectory()) {
		if (!directoryName.equals("")) {
			directoryName += "/" + f1.getName();
		} else {
			directoryName = f1.getName();
		}

	}

	for (int i = 0; i < files.length; i++) {
		File f = new File(fullPath + files[i]);
		String fName = this.getPathForZipFileName(f, requestNumber);
		FileDescriptor fd = new FileDescriptor(requestNumber, f.getName(), f, fName);
		fd.setDirectoryName(directoryName);
		fd.excludeMethodFromXML("getChildren");
		Element fileNode = fd.toXMLDocument(null, fd.DATE_OUTPUT_ALTIO).getRootElement();
		fileNode.setAttribute("isSelected", "N");
		fileNode.setAttribute("state", "unchecked");
		fileNode.setAttribute("canDelete", "Y");
		fileNode.setAttribute("canRename", "Y");

		fileNode.setAttribute("linkedSampleNumber", getLinkedSampleNumber(sess, fd.getZipEntryName()));
		fileNode.setAttribute("viewURL", fd.getViewURL(viewType));
		if (f.isDirectory()) {
			fileNode.setAttribute("type", "dir");
			recurseAddFiles(fileNode, f, requestNumber, directoryName, sess);
		}

		fdNode.addContent(fileNode);

	}
}

public static class FolderComparator implements Comparator, Serializable {
private String codeRequestCategory;

public FolderComparator(String codeRequestCategory) {
	this.codeRequestCategory = codeRequestCategory;
}

public int compare(Object o1, Object o2) {
	String key1 = (String) o1;
	String key2 = (String) o2;

	Integer sorta1 = null;
	Integer sorta2 = null;
	if (key1.equals(QUALITY_CONTROL_DIRECTORY)) {
		sorta1 = new Integer(0);
	} else {
		sorta1 = new Integer(1);
	}
	if (key2.equals(QUALITY_CONTROL_DIRECTORY)) {
		sorta2 = new Integer(0);
	} else {
		sorta2 = new Integer(1);
	}

	String sortb1 = "";
	String sortb2 = "";
	Integer sortc1 = null;
	Integer sortc2 = null;
	if (RequestCategory.isMicroarrayRequestCategory(codeRequestCategory)) {
		String tokens[] = key1.split("E");
		if (tokens.length == 2) {
			try {
				Integer.parseInt(tokens[1]);
				sortc1 = new Integer(tokens[1]);
				sortb1 = tokens[0];
			} catch (Exception e) {
				// ***** NOTE ***** do not log this error
				sortc1 = new Integer(0);
				sortb1 = key1;
			}
		} else {
			sortc1 = new Integer(0);
			sortb1 = key1;
		}
		tokens = key2.split("E");
		if (tokens.length == 2) {
			try {
				Integer.parseInt(tokens[1]);
				sortc2 = new Integer(tokens[1]);
				sortb2 = tokens[0];

			} catch (Exception e) {
				// ***** NOTE ***** do not log this error
				sortc2 = new Integer(2);
				sortb2 = key2;

			}
		} else {
			sortc2 = new Integer(2);
			sortb2 = key2;
		}

	} else {
		sortb1 = key1;
		sortc1 = new Integer(0);

		sortb2 = key2;
		sortc2 = new Integer(0);
	}

	if (sorta1.equals(sorta2)) {
		if (sortb1.equals(sortb2)) {
			return sortc1.compareTo(sortc2);
		} else {
			return sortb1.compareTo(sortb2);
		}
	} else {
		return sorta1.compareTo(sorta2);
	}

}
}

public static class HybSampleComparator implements Comparator, Serializable {
public int compare(Object o1, Object o2) {
	String key1 = (String) o1;
	String key2 = (String) o2;

	String[] tokens1 = key1.split(Constants.DOWNLOAD_KEY_SEPARATOR, 4);
	String[] tokens2 = key2.split(Constants.DOWNLOAD_KEY_SEPARATOR, 4);

	String yr1 = tokens1[0];
	String date1 = tokens1[1];
	String reqNumber1 = tokens1[2];
	String hybNumber1 = tokens1[3];
	String folder1 = tokens1[3];

	String yr2 = tokens2[0];
	String date2 = tokens2[1];
	String reqNumber2 = tokens2[2];
	String hybNumber2 = tokens2[3];
	String folder2 = tokens2[3];

	String number1 = null;

	if (hybNumber1.equals(QUALITY_CONTROL_DIRECTORY)) {
		number1 = "0";

	} else {
		String splitLetter = null;
		if (hybNumber1.indexOf("E") >= 0) {
			splitLetter = "E";
		} else if (hybNumber1.indexOf("X") >= 0) {
			splitLetter = "X";
		}
		if (splitLetter != null) {
			String[] hybNumberTokens1 = hybNumber1.split(splitLetter);
			number1 = hybNumberTokens1[hybNumberTokens1.length - 1];
			try {
				new Integer(number1);
			} catch (Exception e) {
				// ***** NOTE ***** do not log this error
				number1 = "1";
			}
		} else {
			number1 = "1";
		}
	}

	String number2 = null;

	if (hybNumber2.equals(QUALITY_CONTROL_DIRECTORY)) {
		number2 = "0";

	} else {
		String splitLetter = null;
		if (hybNumber2.indexOf("E") >= 0) {
			splitLetter = "E";
		} else if (hybNumber2.indexOf("X") >= 0) {
			splitLetter = "X";
		}

		if (splitLetter != null) {
			String[] hybNumberTokens2 = hybNumber2.split(splitLetter);
			number2 = hybNumberTokens2[hybNumberTokens2.length - 1];
			try {
				new Integer(number2);
			} catch (Exception e) {
				// ***** NOTE ***** do not log this error
				number2 = "1";
			}
		} else {
			number2 = "1";
		}

	}

	if (date1.equals(date2)) {
		if (reqNumber1.equals(reqNumber2)) {
			if (number1.equals(number2)) {
				return folder1.compareTo(folder2);
			} else {
				return new Integer(number1).compareTo(new Integer(number2));
			}
		} else {
			return reqNumber2.compareTo(reqNumber1);
		}
	} else {
		return date2.compareTo(date1);
	}

}
}

private static class LaneStatusInfo {
private java.sql.Date firstCycleDate;
private String firstCycleFailed;
private java.sql.Date lastCycleDate;
private String lastCycleFailed;

public java.sql.Date getFirstCycleDate() {
	return firstCycleDate;
}

public void setFirstCycleDate(java.sql.Date firstCycleDate) {
	this.firstCycleDate = firstCycleDate;
}

public String getFirstCycleFailed() {
	return firstCycleFailed;
}

public void setFirstCycleFailed(String firstCycleFailed) {
	this.firstCycleFailed = firstCycleFailed;
}

public java.sql.Date getLastCycleDate() {
	return lastCycleDate;
}

public void setLastCycleDate(java.sql.Date lastCycleDate) {
	this.lastCycleDate = lastCycleDate;
}

public String getLastCycleFailed() {
	return lastCycleFailed;
}

public void setLastCycleFailed(String lastCycleFailed) {
	this.lastCycleFailed = lastCycleFailed;
}

}

private static class FlowCellFolder {
private String requestNumber;
private String flowCellNumber;
private java.sql.Date createDate;
private Integer idCoreFacility;

public FlowCellFolder(String requestNumber, String flowCellNumber, Date createDate, Integer idCoreFacility) {
	super();
	this.requestNumber = requestNumber;
	this.flowCellNumber = flowCellNumber;
	this.createDate = createDate;
	this.idCoreFacility = idCoreFacility;
}

public java.sql.Date getCreateDate() {
	return createDate;
}

public void setCreateDate(java.sql.Date createDate) {
	this.createDate = createDate;
}

public String getFlowCellNumber() {
	return flowCellNumber;
}

public void setFlowCellNumber(String flowCellNumber) {
	this.flowCellNumber = flowCellNumber;
}

public String getRequestNumber() {
	return requestNumber;
}

public void setRequestNumber(String requestNumber) {
	this.requestNumber = requestNumber;
}

public Integer getIdCoreFacility() {
	return idCoreFacility;
}

}

}