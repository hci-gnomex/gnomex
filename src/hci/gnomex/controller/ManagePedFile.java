package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class ManagePedFile extends GNomExCommand implements Serializable {

private static Logger LOG = Logger.getLogger(ManagePedFile.class);

private Integer idDataTrack;
private String baseURL;
private String baseDir;
private String analysisBaseDir;
private String serverName;

private Integer idAnalysis;
private String VCFpathName;
private String pedFilePathName;
private String PEDFileXMLString;
private String PEDInfoXMLString;
private String BAMInfoXMLString;
private String VCFInfoXMLString;
private String action = null;
private String theProband = null;

private Document pedFileDoc;
private Document pedInfoDoc;

public void validate() {
}

public void loadCommand(HttpServletRequest request, HttpSession session) {

	// idAnalysis is required to save a .ped file
	idAnalysis = null;
	if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
		idAnalysis = new Integer(request.getParameter("idAnalysis"));
	}

	action = null;
	if (request.getParameter("action") != null && !request.getParameter("action").equals("")) {
		action = request.getParameter("action");
		System.out.println("[ManagePedFile] action: " + action);
	}

	theProband = null;
	if (request.getParameter("proband") != null && !request.getParameter("proband").equals("")) {
		theProband = request.getParameter("proband");
		System.out.println("[ManagePedFile] proband: " + theProband);
	}

	// getPedFile requires path to .ped file
	pedFilePathName = null;
	if (request.getParameter("getPedFile") != null && !request.getParameter("getPedFile").equals("")) {
		pedFilePathName = request.getParameter("getPedFile");
	}

	// getIds requires pathname of .vcf.gz file
	VCFpathName = null;
	if (request.getParameter("getIds") != null && !request.getParameter("getIds").equals("")) {
		VCFpathName = request.getParameter("getIds");
	}

	PEDFileXMLString = null;
	if (request.getParameter("PEDFile") != null && !request.getParameter("PEDFile").equals("")) {
		PEDFileXMLString = request.getParameter("PEDFile");
	}

	PEDInfoXMLString = null;
	if (request.getParameter("PEDInfo") != null && !request.getParameter("PEDInfo").equals("")) {
		PEDInfoXMLString = request.getParameter("PEDInfo");
	}

	BAMInfoXMLString = null;
	if (request.getParameter("BAMInfo") != null && !request.getParameter("BAMInfo").equals("")) {
		BAMInfoXMLString = request.getParameter("BAMInfo");
	}

	VCFInfoXMLString = null;
	if (request.getParameter("VCFInfo") != null && !request.getParameter("VCFInfo").equals("")) {
		VCFInfoXMLString = request.getParameter("VCFInfo");
	}

	serverName = request.getServerName();
}

public Command execute() throws RollBackCommandException {

	try {

		Session sess = HibernateSession.currentSession(this.getSecAdvisor().getUsername());
		baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
				PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
		analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
				PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);

		String portNumber = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(
				PropertyDictionary.HTTP_PORT, serverName);
		if (portNumber == null) {
		} else {
			portNumber = ":" + portNumber;
		}

		Analysis a = sess.get(Analysis.class, idAnalysis);
		String analysisDirectory = GetAnalysisDownloadList.getAnalysisDirectory(analysisBaseDir, a);

		if (VCFpathName != null) {
			// parse the header and get the sample id's into XML
			Document vcfIds = getVCFIds(VCFpathName);

			XMLOutputter out = new org.jdom.output.XMLOutputter();
			this.xmlResult = out.outputString(vcfIds);
			setResponsePage(this.SUCCESS_JSP);
		}

		// save the ped file renaming the old one to .bak
		if (action != null && action.equals("save")) {
			String pedpath = getPedFilePathname(PEDInfoXMLString, analysisDirectory);

			String status = savePedFile(pedpath, PEDFileXMLString);
			if (status == null) {
				this.xmlResult = "<SUCCESS pedpath=\"" + pedpath + "\"" + "/>";
				setResponsePage(this.SUCCESS_JSP);
			} else {
				// return the error
				this.addInvalidField("Error saving ped file:", status);
			}
		}

		Map<Integer, String> headerMap = new HashMap<Integer, String>();
		Map<String, String[]> peopleMap = new HashMap<String, String[]>();

		Map<String, String> vcfMap = new HashMap<String, String>();
		ArrayList<String> bamList = new ArrayList<String>();

		String[] theProbands = null;

        // if action is create and there is no PEDInfoXMLString set action = create
        if (action != null && action.equals("setup")) {
            if (PEDInfoXMLString == null) {
                action = "create";
            } else {
                String pedpath = getPedFilePathname(PEDInfoXMLString, analysisDirectory);
                if (pedpath == null) {
                    action = "create";
                }
            }
        }

		// action = setup; read the pedfile and augment it if needed then return the XML
		if (action != null && action.equals("setup")) {
			String pedpath = getPedFilePathname(PEDInfoXMLString, analysisDirectory);

			String status = MakeGeneURL.setupPedFile(pedpath, headerMap, peopleMap);
			System.out.println("[ManagePedFile] after setupPedFile status: " + status);
			if (status != null && status.equals("extend")) {

				// get the vcf info
				MakeGeneURL.vcfInfoParser(VCFInfoXMLString, analysisDirectory, vcfMap);
				System.out.println("[ManagePedFile] vcfMap.size: " + vcfMap.size());

				// get the bam info
				MakeGeneURL.bamInfoParser(BAMInfoXMLString, analysisDirectory, bamList);
				System.out.println("[ManagePedFile] bamList.size: " + bamList.size());

				// add bam and vcf information to everyone we can
				MakeGeneURL.augmentPedFile(headerMap, peopleMap, vcfMap, bamList);
				status = "save";

				// are there any trio's?
				theProbands = MakeGeneURL.findTrio(headerMap, peopleMap);
				if (theProbands != null) {
					status = "save choose";
					System.out.println("[ManagePedFile] theProbands[0]: " + theProbands[0] + "theProbands.length: "
							+ theProbands.length);
				}

			} else {
                // are there any trio's?
                theProbands = MakeGeneURL.findTrio(headerMap, peopleMap);
                if (theProbands != null) {
                    status = "choose";
                    System.out.println("[ManagePedFile] action = setup theProbands[0]: " + theProbands[0] + "theProbands.length: "
                            + theProbands.length);
                } else {
                    status = "parent save choose";
                }

                System.out.println ("[ManagePedFile] final status: " + status);

            }

			// build the xml needed for the UI to call ManagePedFile
			Document ManagePedFile = MakeGeneURL.buildManagePedFileXML(pedpath, VCFInfoXMLString, BAMInfoXMLString,
					headerMap, peopleMap, status);

			XMLOutputter out = new org.jdom.output.XMLOutputter();
			this.xmlResult = out.outputString(ManagePedFile);

			System.out.println("[ManagePedFile] xmlResult:\n" + this.xmlResult);
			setResponsePage(this.SUCCESS_JSP);

		}

		// NOTE: action = launch is done in MakeGeneURL

		// action = create; setup pedpath, PEDInfoXMLString and create pedfile based on bam and vcf info
		if (action != null && action.equals("create")) {
			String pedpath = makePedFilePathname(idAnalysis, analysisDirectory);

			// get the vcf info
			MakeGeneURL.vcfInfoParser(VCFInfoXMLString, analysisDirectory, vcfMap);
			System.out.println("[ManagePedFile] vcfMap.size: " + vcfMap.size());

			// get the bam info
			MakeGeneURL.bamInfoParser(BAMInfoXMLString, analysisDirectory, bamList);
			System.out.println("[ManagePedFile] bamList.size: " + bamList.size());

			// create whatever ped file we can from the bam and vcf information
			createPedFile(headerMap, peopleMap, vcfMap, bamList);
			String status = "save";

			// build the xml needed for the UI to call ManagePedFile
			Document ManagePedFile = MakeGeneURL.buildManagePedFileXML(pedpath, VCFInfoXMLString, BAMInfoXMLString,
					headerMap, peopleMap, status);

			XMLOutputter out = new org.jdom.output.XMLOutputter();
			this.xmlResult = out.outputString(ManagePedFile);

			System.out.println("[ManagePedFile] xmlResult:\n" + this.xmlResult);
			setResponsePage(this.SUCCESS_JSP);

		}

	} catch (Exception e) {
		LOG.error("An exception has occurred in ManagePedFile ", e);
		throw new RollBackCommandException(e.getMessage());
	} finally {
		try {
			HibernateSession.closeSession();
		} catch (Exception e) {

		}
	}

	return this;
}

private void createPedFile(Map<Integer, String> headerMap, Map<String, String[]> peopleMap, Map<String, String> vcfMap,
		ArrayList<String> bamList) {
	String[] columnNames = { "kindred_id", "sample_id", "paternal_id", "maternal_id", "sex", "affection_status", "bam",
			"vcf" };

	// build headerMap
	int numcol = 0;
	for (String acolname : columnNames) {
		headerMap.put(numcol, acolname);
		numcol++;
	}

	// add every sample_id we got from the vcf.gz header as person if they have a bam file
	for (Map.Entry<String, String> entry : vcfMap.entrySet()) {
		String key = entry.getKey();
		String value = entry.getValue();

		// is there a bam file?
		String bamfile = MakeGeneURL.findBAM(key, bamList);
		if (bamfile != null) {
			// add the person
			String[] pedentry = makePedEntry(key, bamfile, value);
			peopleMap.put(key, pedentry);
		}
	} // end of for

}

private String[] makePedEntry(String sample_id, String bamfile, String vcffile) {
	String[] pedEntry = new String[8];

	// since we are making the pedentry we already know the order of the column
	pedEntry[0] = "";
	pedEntry[1] = sample_id;
	pedEntry[2] = "0";
	pedEntry[3] = "0";
	pedEntry[4] = "0";
	pedEntry[5] = "0";
	pedEntry[6] = bamfile;
	pedEntry[7] = vcffile;
	return pedEntry;
}

private String makePedFilePathname(int idAnalysis, String analysisDirectory) {
	String pedpath = null;

	System.out.println("[makePedFilePathname] idAnalysis: " + idAnalysis + " analysisDirectory: " + analysisDirectory);

	pedpath = analysisDirectory + "/A" + idAnalysis + ".ped";
	System.out.println("[makePedFilePathname] pedpath: " + pedpath);

	return pedpath;
}

private String getPedFilePathname(String PEDInfoXMLString, String analysisDirectory) {

	System.out.println("[getPedFilePathname] PEDInfoXMLString: " + PEDInfoXMLString + " analysisDirectory: "
			+ analysisDirectory);
	String pedpath = null;

    if (PEDInfoXMLString == null) {
        return pedpath;
    }

	StringReader reader = new StringReader(PEDInfoXMLString);
	try {
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(reader);

		Element root = doc.getRootElement();

		for (Iterator i = root.getChildren("PEDPath").iterator(); i.hasNext();) {
			Element node = (Element) i.next();

			String path = node.getAttributeValue("path");
			pedpath = path;
			String cpath = path.replace("\\","/").toLowerCase();
			String canaldir = analysisDirectory.replace("\\","/").toLowerCase();

			if (!cpath.startsWith(canaldir)) {
				pedpath = analysisDirectory + "/" + path;
			}
			break;
		}

	} catch (JDOMException je) {
		LOG.error("Cannot parse PEDInfoXMLString", je);
		this.addInvalidField("ManagePedFile", "Invalid PEDInfoXMLString");
	}

	return pedpath;
}

private String savePedFile(String pedpath, String PEDFileXMLString) {
	String status = null;

	String separator = "";
	ArrayList<String> columnNames = new ArrayList<String>();

	StringReader reader = new StringReader(PEDFileXMLString);
	try {
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(reader);

		Element root = doc.getRootElement();

		String theHeader = "#";
		int numnames = 0;
		for (Iterator i = root.getChildren("PEDHeader").iterator(); i.hasNext();) {
			Element node = (Element) i.next();

			if (numnames > 0) {
				separator = "\t";
			}
			String columnName = node.getAttributeValue("name");
			theHeader += (separator + columnName);
			columnNames.add(columnName);
			numnames++;
		}
		theHeader += "\n";
		System.out.println("[savePedFile] theHeader: " + theHeader);

		backupPedFile(pedpath);

		BufferedWriter pout = new BufferedWriter(new FileWriter(pedpath));
		pout.write(theHeader);

		separator = "";
		String theEntry = "";
		int numcols = 0;
		int numentry = 0;
		for (Iterator i = root.getChildren("PEDEntry").iterator(); i.hasNext();) {
			Element node = (Element) i.next();

			if (numcols > 0) {
				separator = "\t";
			}

			theEntry = "";
			numcols = 0;
			separator = "";
			for (String theColumn : columnNames) {
				if (numcols > 0) {
					separator = "\t";
				}

				String value = node.getAttributeValue(theColumn);
				theEntry += (separator + value);
				numcols++;
			}
			theEntry += "\n";
			System.out.println("[savePedFile] numentry: " + numentry + " theEntry: " + theEntry);

			pout.write(theEntry);
			numentry++;
		}

		pout.flush();
		pout.close();

	} catch (JDOMException je) {
		LOG.error("Cannot parse PEDFileXMLString", je);
		this.addInvalidField("ManagePedFile", "Invalid PEDFileXMLString");
	} catch (IOException e) {
		LOG.error("Error writing ped file ", e);
		this.addInvalidField("ManagePedFile", "Error writing ped file");
	}

	return status;
}

private void backupPedFile(String pedpath) {

	File backup = new File(pedpath + ".bak");
	if (backup.exists()) {
		backup.delete();
	}

	File pedFile = new File(pedpath);
	pedFile.renameTo(backup);
}

public static Document getVCFIds(String VCFpathName) {

	System.out.println("[getVCFIds] VCFpathName: " + VCFpathName);

	Document vcfIds = null;
	ArrayList theIds = new ArrayList();
	String lastline = "";

	String[] cmd = { "tabix", "-H", "" };
	cmd[2] = VCFpathName;

	// run tabix to get the header
	try {
		ProcessBuilder pb = new ProcessBuilder(cmd);

		Process p = pb.start();

		BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line = "";
		while ((line = bri.readLine()) != null) {
			lastline = line;
		}

		// System.out.println("[getVCFIds] linelast: " + lastline);
	} catch (Exception e) {
		LOG.error("ManagePedFile error procing tabix", e);
		System.out.println("[getVCFIds] tabix proc error: " + e);
	}

	vcfIds = new Document(new Element("VCFIdList"));

	// parse the ids out of the last line
	String[] pieces = lastline.split("\t");
	int numids = 0;

	boolean sawFormat = false;
	for (int ii = 0; ii < pieces.length; ii++) {
		if (sawFormat) {
			numids++;
			Element viNode = new Element("VCFId");
			viNode.setAttribute("id", pieces[ii]);
			vcfIds.getRootElement().addContent(viNode);
			continue;
		}

		if (pieces[ii].equals("FORMAT")) {
			sawFormat = true;
		}
	}

	System.out.println("[getVCFIds] numids: " + numids);

	return vcfIds;
}

}
