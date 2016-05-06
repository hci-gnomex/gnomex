package hci.gnomex.controller;

import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.UCSCLinkFiles;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Edited 1/15/2013 Mosbruger
 * 
 * 
 * /**Used for making html url links formatted for IGV and softlinked to GNomEx files.
 */
public class MakeDataTrackIGVLink extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MakeDataTrackIGVLink.class);
	private String dataTrackFileServerWebContext;
	private String baseURL;
	private String baseDir;
	private String analysisBaseDir;
	private String dataTrackFileServerURL;
	private String serverName;
	private SecurityAdvisor secAdvisor;
	private Session sess;
	private String username;
	private ArrayList<String[]> linksToMake = null;

	public static final Pattern TO_STRIP = Pattern.compile("\\n");

	public void validate() {
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		log.error("Post not implemented");
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		serverName = req.getServerName();

		try {
			sess = HibernateSession.currentSession(req.getUserPrincipal().getName());

			// Get the dictionary helper
			// DictionaryHelper dh = DictionaryHelper.getInstance(sess);

			username = req.getUserPrincipal().getName();

			// Get security advisor
			secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
			if (secAdvisor == null) {
				System.out.println("MakeDataTrackIGVLink:  Warning - unable to find existing session. Creating security advisor.");
				secAdvisor = SecurityAdvisor.create(sess, username);
			}

			execute(res);
		} catch (Exception ex) {
			HibernateSession.rollback();
			log.error("MakeDataTrackIGVLink -- Unhandled exception", ex);
		} finally {
			if (sess != null) {
				try {
					HibernateSession.closeSession();
				} catch (Exception ex1) {
				}
			}
		}
	}

	private String linkContents(String path, DataTrackFolder folder, int depth, Session sess) throws Exception {
		// Create prefix for indentation
		StringBuilder prefix = new StringBuilder("");
		for (int i = 0; i < depth; i++) {
			prefix.append("\t");
		}

		path += "/" + DataTrackUtil.stripBadURLChars(folder.getName(), "_");

		// Create StringBuilder
		StringBuilder xmlResult = new StringBuilder("");

		boolean toWrite = false;

		// Print out data within the folder
		StringBuilder dataTrackResult = new StringBuilder("");
		List<String> dtr = new ArrayList<String>();

		ArrayList<DataTrack> dts = new ArrayList<DataTrack>(folder.getDataTracks());
		for (DataTrack dt : dts) {
			// If one of the datatracks is readable by the user, create folder and links

			if (secAdvisor.canRead(dt)) {
				File dir = new File(path);
				dir.mkdirs();

				String trackResults = makeIGVLink(sess, dt, path, prefix);
				if (!trackResults.equals("")) {
					dtr.add(trackResults);
					// dataTrackResult.append(trackResults);
					toWrite = true;
				}
			}

		}

		// sort it
		if (toWrite) {
			Collections.sort(dtr, new MyComp());
			for (String s : dtr) {
				dataTrackResult.append(s);
			}
		}

		// Recursively call printContents in subfolders
		ArrayList<DataTrackFolder> dtfs = new ArrayList<DataTrackFolder>(folder.getFolders());
		StringBuilder dataFolderResult = new StringBuilder("");
		for (DataTrackFolder dtf : dtfs) {
			String result = linkContents(path, dtf, depth + 1, sess);
			if (!result.equals("")) {
				dataFolderResult.append(result);
				toWrite = true;
			}
		}

		if (toWrite) {
			xmlResult.append(prefix + "<Category name=\"" + folder.getName() + "\">\n");
			xmlResult.append(dataTrackResult);
			xmlResult.append(dataFolderResult);
			xmlResult.append(prefix + "</Category>\n");
		}

		return xmlResult.toString();

	}

	public void execute(HttpServletResponse res) throws RollBackCommandException {
		try {
			baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
			analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
			dataTrackFileServerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_URL);
			dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);

			// We have to serve files from Tomcat, so use das2 base url
			baseURL = dataTrackFileServerURL + "/";

			// If the user already has a directory, get the existing name and destroy everything beneath it. This way,
			// existing path still work. This will be nice if we set up a cron job that automatically populates these directories
			// If the user doesn't have a directory, a new random string will be created.
			File linkDir = checkIGVLinkDirectory(baseDir, dataTrackFileServerWebContext);
			String linkPath = this.checkForIGVUserFolderExistence(linkDir, username);

			if (linkPath == null) {
				linkPath = UUID.randomUUID().toString() + username;
			}

			// Create the users' data directory
			File dir = new File(linkDir.getAbsoluteFile(), linkPath);
			dir.mkdir();
			String rootPath = dir.getAbsolutePath(); // Path via server
			String htmlPath = dataTrackFileServerURL + Constants.IGV_LINK_DIR_NAME + "/" + linkPath + "/"; // Path wia web

			// Clear out links to make
			linksToMake = new ArrayList<String[]>();

			/*****************************************************************
			 * Grab the list of available genomes. Check if the user has data for the genome and if the genome is supported by IGV. If so, create a repository
			 * for the local data and add to Broad repository
			 */

			// Grab the list of available GNomEx genomes
			boolean permissionForAny = false; // If the user own something, report IGV Link
			String queryString = "SELECT idGenomeBuild from GenomeBuild where igvName is not null";
			Query query = sess.createQuery(queryString);
			List<Integer> genomeIndexList = query.list();

			for (Iterator<Integer> i = genomeIndexList.iterator(); i.hasNext();) {
				// Grab genome build, check if data exists and if IGV supports it. If no data or no IGV support, skip to next genome
				Integer gnIdx = i.next();
				GenomeBuild gb = GenomeBuild.class.cast(sess.load(GenomeBuild.class, gnIdx));
				String igvGenomeBuildName = gb.getIgvName();
				ArrayList<DataTrackFolder> dataTrackFolders = new ArrayList<DataTrackFolder>(gb.getDataTrackFolders());
				if (dataTrackFolders.size() == 0 || igvGenomeBuildName == null) {
					continue;
				}

				// Find the root folder of Genome. Should be the one with the UCSC identifier.
				DataTrackFolder rootFolder = null;
				boolean found = false;
				for (DataTrackFolder folder : dataTrackFolders) {
					if (folder.getIdParentDataTrackFolder() == null) {
						if (found) {
							log.error("MakeDataTrackIGVLink -- Found two parental folders???? " + gb.getGenomeBuildName());
						} else {
							rootFolder = folder;
							found = true;
						}
					}
				}
				if (!found) {
					log.error("MakeDataTrackIGVLink -- Found no parental folders???? " + gb.getGenomeBuildName());
				} else {

					// Create data repository
					String result = this.linkContents(rootPath, rootFolder, 1, sess);

					// If there was a result, create the repository file and add to registry.
					if (!result.equals("")) {
						// Write registry file
						File registry = new File(dir, "igv_registry_" + igvGenomeBuildName + ".txt");

						// add any data sets available from the broad institute to our local registry
						StringBuilder broadAnnData = new StringBuilder("");
						String theURL = "http://data.broadinstitute.org/igvdata/" + igvGenomeBuildName + "_dataServerRegistry.txt";
						URL broad = new URL(theURL);
						try {
							BufferedReader br2 = new BufferedReader(new InputStreamReader(broad.openStream()));
							String line2;
							while ((line2 = br2.readLine()) != null) {
								if (line2.startsWith("http")) {
									broadAnnData.append(line2 + "\n");
								}
							}
							br2.close();

							if (broadAnnData.length() > 0) {
								BufferedWriter bw2 = new BufferedWriter(new FileWriter(registry));
								bw2.write(broadAnnData.toString());
								bw2.close();
							}
						} catch (IOException ex) {
							log.error("MakeDataTrackIGVLink -- Could not read from the Broad repository file: " + theURL);
						}

						BufferedWriter br = new BufferedWriter(new FileWriter(registry, true));
						br.write(htmlPath + igvGenomeBuildName + "_dataset.xml\n");
						br.close();

						// Create repository
						StringBuilder dataSetContents = new StringBuilder("");
						dataSetContents.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
						dataSetContents.append("<Global name=\"" + username + "\" version=\"1\">\n");
						dataSetContents.append(result);
						dataSetContents.append("</Global>\n");
						File xmlFile = new File(dir, igvGenomeBuildName + "_dataset.xml");
						br = new BufferedWriter(new FileWriter(xmlFile));
						br.write(dataSetContents.toString());
						br.close();

						// signal results
						permissionForAny = true;
					} // end if results
				} // end if_else data tracks found
			} // end for each genome

			secAdvisor.closeHibernateSession();

			// If the user has permission for any data track, give the the repository link
			if (permissionForAny) {
				boolean success = this.makeSoftLinkViaUNIXCommandLine(dir.toString()); // deal with embedded spaces

				if (success) {
					String preamble = new String(
							"Launch IGV and replace the default Data Registry URL (View->Preferences->Advanced) with the following link: \n\n");

					StringBuilder sbo = new StringBuilder(preamble + htmlPath + "igv_registry_$$.txt");

					res.setContentType("application/xml");
					res.getOutputStream().println("<SUCCESS igvURL=\"" + sbo.toString() + "\"/>");
				} else {
					throw new Exception("Could not create IGV Links");
				}
			} else {
				log.error("MakeDataTrackIGVLink -- No data tracks associated with this user!");
			}

		} catch (Exception e) {
			log.error("An exception has occurred in MakeDataTrackIGVLinks ", e);
			e.printStackTrace(System.out);
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				secAdvisor.closeHibernateSession();
			} catch (Exception e) {
			}
		}
	}

	private String checkForIGVUserFolderExistence(File igvLinkDir, String username) throws Exception {
		File[] directoryList = igvLinkDir.listFiles();

		String desiredDirectory = null;

		for (File directory : directoryList) {
			if (directory.getName().length() > 36) {
				String parsedUsername = directory.getName().substring(36);
				if (parsedUsername.equals(username)) {
					desiredDirectory = directory.getName();
					delete(directory);
				}
			}
		}

		return desiredDirectory;

	}

	private void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new FileNotFoundException("Failed to delete file: " + f);
	}

	private File checkIGVLinkDirectory(String baseURL, String webContextPath) throws Exception {
		File igvLinkDir = new File(webContextPath, Constants.IGV_LINK_DIR_NAME);
		igvLinkDir.mkdirs();
		if (igvLinkDir.exists() == false)
			throw new Exception("\nFailed to find and or make a directory to contain url softlinks for IGV data distribution.\n");

		// add redirect index.html if not present, send them to genopub
		File redirect = new File(igvLinkDir, "index.html");
		if (redirect.exists() == false) {
			String toWrite = "<html> <head> <META HTTP-EQUIV=\"Refresh\" Content=\"0; URL=" + baseURL + "genopub\"> </head> <body>Access denied.</body>";
			PrintWriter out = new PrintWriter(new FileWriter(redirect));
			out.println(toWrite);
			out.close();
		}

		return igvLinkDir;
	}

	private String makeIGVLink(Session sess, DataTrack dataTrack, String directory, StringBuilder prefix) throws Exception {
		StringBuilder sb = new StringBuilder("");
		try {

			List<File> dataTrackFiles = dataTrack.getFiles(baseDir, analysisBaseDir);

			// disallow AUTOCONVERT!!!!
			UCSCLinkFiles link;
			link = DataTrackUtil.fetchUCSCLinkFiles(dataTrackFiles, GNomExFrontController.getWebContextPath(), false);

			// 'link' will be null if the user can read the track, doesn't own the track and the bw has not yet been created.
			if (link != null) {
				// check if dataTrack has exportable file type (xxx.bam, xxx.bai, xxx.bw, xxx.bb, xxx.vcf.gz, xxx.vcf.gz.tbi, xxx.useq (will be converted if
				// autoConvert is true))
				// UCSCLinkFiles link = DataTrackUtil.fetchUCSCLinkFiles(dataTrackFiles, GNomExFrontController.getWebContextPath());
				File[] filesToLink = link.getFilesToLink();
				if (filesToLink == null)
					throw new Exception("No files to link?!");

				// When new .bw/.bb files are created, add analysis files and then link via data
				// track file to the data track.
				MakeDataTrackUCSCLinks.registerDataTrackFiles(sess, analysisBaseDir, dataTrack, filesToLink);

				// for each file, there might be two for xxx.bam and xxx.bai files, vcf files, possibly two for converted useq files, plus/minus strands,
				// otherwise just one.
				ArrayList<String> names = new ArrayList<String>();
				ArrayList<String> fileURLs = new ArrayList<String>();
				for (File f : filesToLink) {

					File annoFile = new File(directory, DataTrackUtil.stripBadURLChars(f.getName(), "_"));
					String annoString = annoFile.toString();

					// We are now storing the links and creating them in a batch.
					String[] links = { f.toString(), annoString };
					linksToMake.add(links);

					// is it a bam index xxx.bai? If so then skip AFTER making soft link.
					if (annoString.endsWith(".bai") || annoString.endsWith(".vcf.gz.tbi"))
						continue;

					// stranded?
					String strand = "";
					if (link.isStranded()) {
						if (annoString.endsWith("_Plus.bw"))
							strand = " + ";
						else if (annoString.endsWith("_Minus.bw"))
							strand = " - ";
						else
							throw new Exception("\nCan't determine strand of bw file? " + annoString);
					}

					// dataset name
					String datasetName = dataTrack.getName() + strand + " " + dataTrack.getFileName();
					names.add(datasetName);

					// make bigData URL e.g. bigDataUrl=http://genome.ucsc.edu/goldenPath/help/examples/bigBedExample.bb
					int index = annoString.indexOf(Constants.IGV_LINK_DIR_NAME);
					String annoPartialPath = annoString.substring(index);
					String bigDataUrl = baseURL + annoPartialPath;
					fileURLs.add(bigDataUrl);

					sb.append(prefix + "\t<Resource name=\"" + dataTrack.getName() + "\" path=\"" + bigDataUrl + "\"/>\n");
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return sb.toString();

	}

	private boolean makeSoftLinkViaUNIXCommandLine(String path) {
		try {
			File script = new File(path, "makeLinks.sh");
			script.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(script));
			for (String[] links : linksToMake) {
				bw.write("ln -s \"" + links[0] + "\" \"" + links[1] + "\"\n");
			}
			bw.close();

			String[] cmd = { "sh", script.toString() };
			Process p = Runtime.getRuntime().exec(cmd);

			// script.delete();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String stripBadNameCharacters(String name) {
		name = name.trim();
		name.replaceAll(",", "_");
		return name;
	}
}

class MyComp implements Comparator<String> {

	@Override
	public int compare(String str1, String str2) {
		return str1.compareTo(str2);
	}

}
