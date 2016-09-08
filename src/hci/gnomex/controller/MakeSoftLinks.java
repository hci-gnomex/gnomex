package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.FileDescriptorParser;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.apache.log4j.Logger;
public class MakeSoftLinks extends GNomExCommand implements Serializable {

	private static Logger LOG = Logger.getLogger(MakeSoftLinks.class);

	private String serverName;
	private String directory_bioinformatics_scratch;

	private FileDescriptorParser parser = null;

	protected final static String SESSION_KEY_FILE_DESCRIPTOR_PARSER = "GNomExFileDescriptorParser";

	public void validate() {
	}

	public void loadCommand(HttpServletRequest request, HttpSession session) {

		// Get the files XML string
		if (request.getParameter("fileDescriptorXMLString") != null && !request.getParameter("fileDescriptorXMLString").equals("")) {
			String fileDescriptorXMLString = "<FileDescriptorList>" + request.getParameter("fileDescriptorXMLString") + "</FileDescriptorList>";

			// System.out.println("\n[MakeSoftLinks] " + fileDescriptorXMLString + "\n");

			StringReader reader = new StringReader(fileDescriptorXMLString);
			try {
				SAXBuilder sax = new SAXBuilder();
				Document doc = sax.build(reader);
				parser = new FileDescriptorParser(doc);
			} catch (JDOMException je) {
				LOG.error("Cannot parse fileDescriptorXMLString", je);
				System.out.println("[MakeSoftLinks] Error: Cannot parse fileDescriptorXMLString, " + je);

			}
		}

		serverName = request.getServerName();
	}

	public Command execute() throws RollBackCommandException {

		try {

			Session sess = HibernateSession.currentSession(this.getSecAdvisor().getUsername());

			directory_bioinformatics_scratch = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DIRECTORY_BIOINFORMATICS_SCRATCH);

			String softLinkPath = makeSoftLinks(sess);

			// System.out.println("\n[MakeSoftLinks] softLinkPath: " + softLinkPath + "\n");
			this.xmlResult = "<SUCCESS softLinkPath=\"" + softLinkPath + "\"" + "/>";
			setResponsePage(this.SUCCESS_JSP);

		} catch (Exception e) {
			LOG.error("An exception has occurred in MakeSoftLinks ", e);
			throw new RollBackCommandException(e.getMessage());
		} finally {
			try {
				HibernateSession.closeSession();
			} catch (Exception e) {
				LOG.error("An exception has occurred in MakeSoftLinks ", e);
			}
		}

		return this;
	}

	private String makeSoftLinks(Session sess) throws Exception {

		String softLinkPath = "";

		// Create the users' soft link directory
		File dir = new File(directory_bioinformatics_scratch, username);
		if (!dir.exists())
			dir.mkdir();

		String baseDir = dir.getPath();

		parser.parse();

		String requestNumber = (String) parser.getRequestNumbers().iterator().next();

		// directory for the request
		File rdir = new File(baseDir, requestNumber);
		if (rdir.exists()) {
			destroyFolder(rdir);
		} else {
			rdir.mkdir();
		}

		softLinkPath = rdir.getPath();

		List fileDescriptors = parser.getFileDescriptors(requestNumber);

		// For each file to create a soft link for
		for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext();) {

			FileDescriptor fd = (FileDescriptor) i1.next();

			// Ignore md5 files
			if (fd.getType().equals("md5")) {
				continue;
			}

			String targetPath = fd.getFileName();

			// build the soft link path
			String linkPath = softLinkPath + File.separator;

			String zipFileName = fd.getZipEntryName();
			String[] pieces = zipFileName.split("/");

			// make any directories we need
			int lpieces = pieces.length;
			for (int i = 1; i <= lpieces - 2; i++) {
				File ldir = new File(linkPath, pieces[i]);
				if (!ldir.exists()) {
					ldir.mkdir();
				}

				linkPath = linkPath + pieces[i] + File.separator;
			}

			linkPath = linkPath + pieces[lpieces - 1];

			// make the soft link
			// System.out.println("[MakeSoftLinks] targetPath: " + targetPath + "\n                     linkPath: " + linkPath);
			makeSoftLinkViaUNIXCommandLine(targetPath, linkPath);

		}

		return softLinkPath;
	}

	public static boolean makeSoftLinkViaUNIXCommandLine(String realFile, String link) {
		try {
			String[] cmd = { "ln", "-s", realFile, link };
			Runtime.getRuntime().exec(cmd);
			return true;
		} catch (IOException e) {

		}
		return false;
	}

	private static void destroyFolder(File linkDir) {
		File[] directoryList = linkDir.listFiles();

		for (File directory : directoryList) {
			delete(directory);
		}
	}

	private static void delete(File f) {
		try {
			if (f.isDirectory()) {
				for (File c : f.listFiles()) {
					delete(c);
				}
			}

			f.delete();
		} catch (Exception e) {
			LOG.error("Error in makeSoftLinks.java", e);
		}
	}

}
