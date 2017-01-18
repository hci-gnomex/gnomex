package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UCSCLinkFiles;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class MakeDataTrackUCSCLinks extends GNomExCommand implements Serializable {

private static Logger LOG = Logger.getLogger(MakeDataTrackUCSCLinks.class);

private Integer idDataTrack;
private String baseURL;
private String baseDir;
private String analysisBaseDir;
private String serverName;
private String dataTrackFileServerURL;
private String dataTrackFileServerWebContext;
private Integer idAnalysisFile;
private Integer idAnalysis;
private String pathName;

public static final Pattern TO_STRIP = Pattern.compile("\\n");

private static boolean autoConvertUSeqArchives = true;

public void validate() {
}

public void loadCommand(HttpServletRequest request, HttpSession session) {
	idDataTrack = null;
	if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
		idDataTrack = new Integer(request.getParameter("idDataTrack"));
	}

	// if idAnalysisFile is a parameter we will need to figure out idDataTrack
	idAnalysisFile = null;
	if (request.getParameter("idAnalysisFile") != null && !request.getParameter("idAnalysisFile").equals("")) {
		idAnalysisFile = new Integer(request.getParameter("idAnalysisFile"));
	}

	// are we doing this based on files or datatracks?
	pathName = null;
	if (request.getParameter("pathName") != null && !request.getParameter("pathName").equals("")) {
		pathName = request.getParameter("pathName");
	}

	// we need to have idAnalysis if dealing with a file
	idAnalysis = null;
	if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
		idAnalysis = new Integer(request.getParameter("idAnalysis"));
	}

	serverName = request.getServerName();
}

public Command execute() throws RollBackCommandException {

	try {

		Session sess = this.getSecAdvisor().getHibernateSession(this.getUsername());
		baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
				PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
		analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
				PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
		dataTrackFileServerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(
				PropertyDictionary.DATATRACK_FILESERVER_URL);
		dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(
				PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);

		// We have to serve files from Tomcat, so use das2 base url
		baseURL = dataTrackFileServerURL;

		// do we need to figure out idDataTrack?
		if (idAnalysisFile != null) {
			idDataTrack = getidDataTrack(idAnalysisFile, sess);
		}

		if (pathName != null
				|| this.getSecAdvisor().canRead(DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack)))) {

			// make links fetching url(s)
			ArrayList<String> urlsToLoad = makeUCSCLink(sess);
			String url1 = urlsToLoad.get(0);
			String url2 = "";
			if (urlsToLoad.size() == 2)
				url2 = urlsToLoad.get(1);

			// post results with link urls
			System.out.println("[MakeDataTrackUCSCLinks] url1: " + url1);
			this.xmlResult = "<SUCCESS ucscURL1=\"" + url1 + "\" ucscURL2=\"" + url2 + "\"" + "/>";
			setResponsePage(this.SUCCESS_JSP);

		} else {
			this.addInvalidField("insufficient permission", "Insufficient permission to access data track");
		}

	} catch (Exception e) {
		this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in MakeDataTrackUCSCLinks ", e);
		throw new RollBackCommandException(e.getMessage());
	}

	return this;
}

private ArrayList<String> makeUCSCLink(Session sess) throws Exception {

	ArrayList<String> urlsToLoad = new ArrayList<String>();

	// What is the users preferred ucsc url?
	String ucscUrl = this.getSecAdvisor().getUserUcscUrl();

	File[] filesToLink = null;
	String ucscGenomeBuildName = null;
	String summary = "";
	UCSCLinkFiles link = null;
	String DTName = "";
	String DTNumber = "";
	String datasetName = "";
	if (pathName == null) {
		// load dataTrack
		DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

		// check genome has UCSC name
		GenomeBuild gv = GenomeBuild.class.cast(sess.load(GenomeBuild.class, dataTrack.getIdGenomeBuild()));
		ucscGenomeBuildName = gv.getUcscName();
		if (ucscGenomeBuildName == null || ucscGenomeBuildName.length() == 0)
			throw new Exception("Missing UCSC Genome Version name, update, and resubmit.");

		List<File> dataTrackFiles = dataTrack.getFiles(baseDir, analysisBaseDir);

		// check if dataTrack has exportable file type (xxx.bam, xxx.bai, xxx.bw, xxx.bb, xxx.vcf.gz, xxx.vcf.gz.tbi, xxx.useq (will be converted if autoConvert
		// is true))
		link = DataTrackUtil.fetchUCSCLinkFiles(dataTrackFiles, GNomExFrontController.getWebContextPath());
		if (link == null) {
			throw new Exception("No files to link?!");
		}
		filesToLink = link.getFilesToLink();
		if (filesToLink == null) {
			throw new Exception("No files to link?!");
		}

		// When new .bw/.bb files are created, add analysis files and then link via data
		// track file to the data track.
		registerDataTrackFiles(sess, analysisBaseDir, dataTrack, filesToLink);

		// is there a summary?
		summary = dataTrack.getSummary();
		if (summary != null && summary.trim().length() != 0) {
			summary = Constants.HTML_BRACKETS.matcher(summary).replaceAll("");
			summary = "description=\"" + summary + "\"";
		} else
			summary = "";

		DTName = dataTrack.getName();
		DTNumber = dataTrack.getFileName();

	} else {

		// check genome has UCSC name
		Analysis analysis = (Analysis) sess.load(Analysis.class, idAnalysis);
		Set<GenomeBuild> gvs = analysis.getGenomeBuilds();
		if (gvs != null) {
			GenomeBuild gv = gvs.iterator().next();
			ucscGenomeBuildName = gv.getUcscName();
		}
		if (ucscGenomeBuildName == null || ucscGenomeBuildName.length() == 0)
			throw new Exception("Missing UCSC Genome Version name, update, and resubmit.");

		// the file we want to link to
		filesToLink = new File[2];
		filesToLink[0] = new File(pathName);

		// get the data track name out of the pathName
		String thename = pathName.toLowerCase().replace("\\", Constants.FILE_SEPARATOR);
		if (thename.lastIndexOf(Constants.FILE_SEPARATOR) != -1) {
			thename = thename.substring(thename.lastIndexOf(Constants.FILE_SEPARATOR) + 1);
		}

		int ipos = thename.lastIndexOf(".");
		int jpos = thename.lastIndexOf(".vcf.gz");
		if (jpos != -1)
			ipos = jpos;

		DTName = thename.substring(0, ipos);
		DTNumber = "DTNone";

		// add the correct index file
		if (pathName.endsWith(".vcf.gz")) {
			filesToLink[1] = new File(pathName + ".tbi");
		} else {
			// figure out whether the .bam.bai or the .bai file exists
			File bambai = new File(pathName + ".bai");
			if (bambai.exists()) {
				filesToLink[1] = new File(pathName + ".bai");
			} else {
				// we will assume the index file ends in .bai (without the .bam)
				// we don't check or complain if it doesn't because the user can't do anything anyway
				filesToLink[1] = new File(pathName.substring(0, pathName.length() - 4) + ".bai");
			}
		}
	}

	// look and or make directory to hold softlinks to data, also removes old softlinks
	File urlLinkDir = DataTrackUtil.checkUCSCLinkDirectory(baseURL, dataTrackFileServerWebContext);

	String linkPath = this.checkForUserFolderExistence(urlLinkDir, username);

	if (linkPath == null) {
		linkPath = UUID.randomUUID().toString() + username;
	}

	// Create the users' data directory
	File dir = new File(urlLinkDir.getAbsoluteFile(), linkPath);
	if (!dir.exists())
		dir.mkdir();

	// what data type (bam, bigBed, bigWig, vcfTabix)
	String type = "type=" + DataTrackUtil.fetchUCSCDataType(filesToLink);

	// for each file, there might be two for xxx.bam and xxx.bai files, xxx.vcf.gz and xxx.vcf.gz.tbi, possibly two for converted useq files, plus/minus
	// strands, otherwise just one.
	String customHttpLink = null;
	String toEncode = null;
	for (File f : filesToLink) {
		File annoFile = new File(dir, DataTrackUtil.stripBadURLChars(f.getName(), "_"));
		String annoString = annoFile.toString().replace("\\", Constants.FILE_SEPARATOR);

		// make soft link
		DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);

		// is it a bam index xxx.bai or vcf index? If so then skip after making soft link.
		if (annoString.endsWith(".bai") || annoString.endsWith(".vcf.gz.tbi"))
			continue;

		// stranded?
		String strand = "";
		if (link != null && link.isStranded()) {
			if (annoString.endsWith("_Plus.bw"))
				strand = " + ";
			else if (annoString.endsWith("_Minus.bw"))
				strand = " - ";
			else
				throw new Exception("\nCan't determine strand of bw file? " + annoString);
		}

		datasetName = "name=\"" + DTName + strand + " " + DTNumber + "\"";

		// make bigData URL e.g. bigDataUrl=http://genome.ucsc.edu/goldenPath/help/examples/bigBedExample.bb
		int index = annoString.indexOf(Constants.URL_LINK_DIR_NAME);
		String annoPartialPath = annoString.substring(index);
		String bigDataUrl = "bigDataUrl=" + baseURL + annoPartialPath;

		// make final html link
		customHttpLink = ucscUrl + "/cgi-bin/hgTracks?db=" + ucscGenomeBuildName
				+ "&hgct_customText=track+visibility=full+";
		toEncode = type + " " + datasetName + " " + summary + " " + bigDataUrl;

		// remove any returns
		toEncode = TO_STRIP.matcher(toEncode).replaceAll(" ");

		urlsToLoad.add(customHttpLink + URLEncoder.encode(toEncode, "UTF-8"));
	}
	return urlsToLoad;

}

private String checkForUserFolderExistence(File igvLinkDir, String username) throws Exception {
	File[] directoryList = igvLinkDir.listFiles();

	String desiredDirectory = null;

	for (File directory : directoryList) {
		if (directory.getName().length() > 36) {
			String parsedUsername = directory.getName().substring(36);
			if (parsedUsername.equals(username)) {
				desiredDirectory = directory.getName();
			}
		}
	}

	return desiredDirectory;
}

public static void registerDataTrackFiles(Session sess, String analysisBaseDir, DataTrack dataTrack, File[] filesToLink)
		throws Exception {
	// We need to create an AnalysisFile and DataTrackFile in the db for newly converted files
	// Only do this if we have linked to an existing analysis file
	if (dataTrack.getDataTrackFiles() != null && dataTrack.getDataTrackFiles().size() > 0) {
		// First get the analysis file to clone; this should be the .useq file already
		// linked to the data track.
		AnalysisFile analysisFileToClone = null;
		for (DataTrackFile dtFile1 : (Set<DataTrackFile>) dataTrack.getDataTrackFiles()) {
			analysisFileToClone = dtFile1.getAnalysisFile();
			break;
		}
		// Now we will look at each of the converted files. If there isn't an existing
		// data track file that points back to it (an analysis file), then create it
		// and link it to the data track.
		if (analysisFileToClone != null) {
			for (int x = 0; x < filesToLink.length; x++) {
				File f = filesToLink[x];
				boolean found = false;
				for (DataTrackFile dtFile1 : (Set<DataTrackFile>) dataTrack.getDataTrackFiles()) {
					String fileName = dtFile1.getAssociatedFilePath(analysisBaseDir);
					File dtf = new File(fileName);
					if (dtf.equals(f)) {
						found = true;
						analysisFileToClone = dtFile1.getAnalysisFile();
						break;
					}
				}
				if (!found) {

					AnalysisFile af = new AnalysisFile();
					af.setIdAnalysis(analysisFileToClone.getIdAnalysis());
					af.setBaseFilePath(analysisFileToClone.getBaseFilePath());
					af.setQualifiedFilePath(analysisFileToClone.getQualifiedFilePath());
					af.setFileName(f.getName());
					af.setFileSize(new BigDecimal(f.length()));

					sess.save(af);

					DataTrackFile dtf = new DataTrackFile();
					dtf.setIdAnalysisFile(af.getIdAnalysisFile());
					dtf.setIdDataTrack(dataTrack.getIdDataTrack());
					sess.save(dtf);
				}
			}
		}
	}

}

public static int getidDataTrack(int idAnalysisFile, Session sess) {
	// System.out.println ("[MakeDataTrackUCSCLinks:getidDataTrack] ** starting ** idAnalysisFile: " + idAnalysisFile);

	int idDataTrack = -1;

	StringBuffer buf = new StringBuffer("SELECT idDataTrack from DataTrackFile where idAnalysisFile = "
			+ idAnalysisFile);
	List results = sess.createQuery(buf.toString()).list();

	if (results.size() > 0) {
		idDataTrack = (Integer) results.get(0);
	}

	// System.out.println ("[MakeDataTrackUCSCLinks:getidDataTrack] ** leaving ** idDataTrack: " + idDataTrack);
	return idDataTrack;
}

}