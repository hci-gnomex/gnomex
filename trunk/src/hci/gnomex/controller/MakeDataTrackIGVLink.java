package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UCSCLinkFiles;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;


/**Used for making html url links formatted for IGV and softlinked to GNomEx files.*/
public class MakeDataTrackIGVLink extends GNomExCommand implements Serializable {
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MakeDataTrackIGVLink.class);
	private Integer idDataTrack;
	private String dataTrackFileServerWebContext;
	private String baseURL;
	private String baseDir;
	private String analysisBaseDir;
	private String dataTrackFileServerURL;
	private String serverName;
	private boolean launchIGV = true;
	public static final Pattern TO_STRIP = Pattern.compile("\\n");

	public void validate() {}

	public void loadCommand(HttpServletRequest request, HttpSession session) {
		if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
			idDataTrack = new Integer(request.getParameter("idDataTrack"));   
		} else addInvalidField("idDataTrack", "idDataTrack is required");
		
		//launch IGV?		
		if (request.getParameter("launchIGV")!= null && request.getParameter("launchIGV").equals("yes")) launchIGV = true;
		else launchIGV = false;

		
		serverName = request.getServerName();
	}

	public Command execute() throws RollBackCommandException {

		try {
			Session sess = getSecAdvisor().getHibernateSession(getUsername());
			baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackDirectory(serverName);
			analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(serverName);
			dataTrackFileServerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_URL);      
      dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);

      // We have to serve files from Tomcat, so use das2 base url
      baseURL =  dataTrackFileServerURL;


			DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

			if (getSecAdvisor().canRead(dataTrack)) {
				//make links fetching url
				String  urlToLoad = makeIGVLink(sess);

				//post results with link url
				xmlResult = "<SUCCESS igvURL=\"" +  urlToLoad +"\"/>";
				setResponsePage(SUCCESS_JSP);

			} else {
				addInvalidField("insufficient permission", "Insufficient permission to access data for IGV linking!");
			}

		}catch (Exception e){
			log.error("An exception has occurred in MakeDataTrackIGVLinks ", e);
			e.printStackTrace(System.out);
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				getSecAdvisor().closeHibernateSession();        
			} catch(Exception e) {}
		}
		return this;
	}

	private String  makeIGVLink(Session sess) throws Exception {

		try {
			//load dataTrack
			DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));      

			//check genome has UCSC name
			GenomeBuild gv = GenomeBuild.class.cast(sess.load(GenomeBuild.class, dataTrack.getIdGenomeBuild()));
			String ucscGenomeBuildName = gv.getUcscName();
			if (ucscGenomeBuildName == null || ucscGenomeBuildName.length() ==0) throw new Exception ("Missing UCSC Genome Version name, update, and resubmit.");

			List<File> dataTrackFiles = dataTrack.getFiles(baseDir, analysisBaseDir);

			//check if dataTrack has exportable file type (xxx.bam, xxx.bai, xxx.bw, xxx.bb, xxx.useq (will be converted if autoConvert is true))
			UCSCLinkFiles link = DataTrackUtil.fetchUCSCLinkFiles(dataTrackFiles, this.dataTrackFileServerWebContext);
			File[] filesToLink = link.getFilesToLink();
			if (filesToLink== null)  throw new Exception ("No files to link?!");

			// When new .bw/.bb files are created, add analysis files and then link via data
			// track file to the data track.
			MakeDataTrackUCSCLinks.registerDataTrackFiles(sess, analysisBaseDir, dataTrack, filesToLink);

			//look and or make directory to hold softlinks to data, also removes old softlinks
			File urlLinkDir = DataTrackUtil.checkUCSCLinkDirectory(baseURL, this.dataTrackFileServerWebContext);

			String randomWord = UUID.randomUUID().toString();

			//create directory to hold links, need to do this so one can get the actual age of the links and not the age of the linked file
			File dir = new File (urlLinkDir, randomWord);
			dir.mkdir();

			//for each file, there might be two for xxx.bam and xxx.bai files, possibly two for converted useq files, plus/minus strands, otherwise just one.
			ArrayList<String> names = new ArrayList<String>();
			ArrayList<String> fileURLs = new ArrayList<String>();
			for (File f: filesToLink){
				File annoFile = new File(dir, DataTrackUtil.stripBadURLChars(f.getName(), "_"));
				String annoString = annoFile.toString();

				//make soft link
				DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);

				//is it a bam index xxx.bai? If so then skip AFTER making soft link.
				if (annoString.endsWith(".bai")) continue;

				//stranded?
				String strand = "";
				if (link.isStranded()){
					if (annoString.endsWith("_Plus.bw")) strand = " + ";
					else if (annoString.endsWith("_Minus.bw")) strand = " - ";
					else throw new Exception ("\nCan't determine strand of bw file? "+annoString);
				}

				//dataset name
				String datasetName = dataTrack.getName()+ strand +" "+dataTrack.getFileName();
				names.add(datasetName);

				//make bigData URL e.g. bigDataUrl=http://genome.ucsc.edu/goldenPath/help/examples/bigBedExample.bb
				int index = annoString.indexOf(Constants.URL_LINK_DIR_NAME);
				String annoPartialPath = annoString.substring(index);
				String bigDataUrl = baseURL + annoPartialPath;
				fileURLs.add(bigDataUrl);
			}

			//make final url
			StringBuilder sb = new StringBuilder();


			//add genome
			sb.append("genome="+ucscGenomeBuildName);

			//add name(s), might be two 
			sb.append("&name=");
			sb.append(names.get(0));
			if (names.size() !=1) {
				sb.append(",");
				sb.append(names.get(1));
			}

			//add file link(s), might be two
			if (launchIGV) sb.append("&sessionURL=");
			else sb.append("&file=");

			sb.append(fileURLs.get(0));
			if (fileURLs.size() !=1) {
				sb.append(",");
				sb.append(fileURLs.get(1));
			}

			//remove any returns
			String message =  URLEncoder.encode(TO_STRIP.matcher(sb.toString()).replaceAll(" ") , "UTF-8");

			String launch;
			if (launchIGV) launch= "http://www.broadinstitute.org/igv/projects/current/igv.php?maxHeapSize=1800m&";
			else launch ="http://localhost:60151/load?merge=true&";

System.out.println("IGV Launch URL unconverted : "+launch+sb.toString());			

			return launch+message;

		} catch (Exception e) {
			throw e;      
		} finally {
			if (sess != null) sess.close();
		}

	}

	public void setLaunchIGV(boolean launchIGV) {
		this.launchIGV = launchIGV;
	}


	
}