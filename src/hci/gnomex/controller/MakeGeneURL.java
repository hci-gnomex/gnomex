package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class MakeGeneURL extends GNomExCommand implements Serializable {

private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MakeDataTrackLinks.class);

private static int PEDFILE_MAXLENGTH = 1000;
private String serverName;
private String pedpath;
private String geneiobioviewerURL;
private String baseDir;
private String dataTrackFileServerWebContext;
private String baseURL;
private File dir;
private int idAnalysis;

private String theProband = null;
private String VCFInfoXMLString = null;
private String BAMInfoXMLString = null;

public void validate() {
}

public void loadCommand(HttpServletRequest request, HttpSession session) {

	if (request.getParameter("idAnalysis") != null) {
		idAnalysis = new Integer(request.getParameter("idAnalysis"));
	}

	// the .ped file
	if (request.getParameter("fileName") != null && !request.getParameter("fileName").equals("")) {
		pedpath = request.getParameter("fileName");
	}

	// the proband, needed if .ped file has more than one
	if (request.getParameter("proband") != null && !request.getParameter("proband").equals("")) {
		theProband = request.getParameter("proband");
	}

	// Get the VCFInfo XML string
	if (request.getParameter("VCFInfo") != null && !request.getParameter("VCFInfo").equals("")) {
		VCFInfoXMLString = request.getParameter("VCFInfo");
	}

	// Get the BAMInfo XML string
	if (request.getParameter("BAMInfo") != null && !request.getParameter("BAMInfo").equals("")) {
		BAMInfoXMLString = request.getParameter("BAMInfo");
	}

	serverName = request.getServerName();

}

public Command execute() throws RollBackCommandException {

	try {

		Session sess = HibernateSession.currentSession(this.getSecAdvisor().getUsername());

		String portNumber = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(
				PropertyDictionary.HTTP_PORT, serverName);
		if (portNumber == null) {
			portNumber = "";
		} else {
			portNumber = ":" + portNumber;
		}

		geneiobioviewerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(
				PropertyDictionary.GENE_IOBIO_VIEWER_URL);
		dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(
				PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);
		baseURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_URL);

		baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
				PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);

		Map<Integer, String> headerMap = new HashMap<Integer, String>();
		Map<String, String[]> peopleMap = new HashMap<String, String[]>();

		Map<String, String> vcfMap = new HashMap<String, String>();
		ArrayList<String> bamList = new ArrayList<String>();

		Analysis a = (Analysis) sess.get(Analysis.class, idAnalysis);
		String analysisDirectory = GetAnalysisDownloadList.getAnalysisDirectory(baseDir, a);

		String[] theProbands = null;

		// parse the .ped file
		// adds empty bam and vcf columns if needed
		String status = setupPedFile(pedpath, headerMap, peopleMap);
		System.out.println("[MakeGeneURL] after setupPedFile status: " + status);
		if (status.equals("extend")) {

			// get the vcf info
			vcfInfoParser(VCFInfoXMLString, analysisDirectory, vcfMap);
			System.out.println("[MakeGeneURL] vcfMap.size: " + vcfMap.size());

			// get the bam info
			bamInfoParser(BAMInfoXMLString, analysisDirectory, bamList);
			System.out.println("[MakeGeneURL] bamList.size: " + bamList.size());

			// add bam and vcf information to everyone we can
			augmentPedFile(headerMap, peopleMap, vcfMap, bamList);

			// find the first person in a trio
			theProbands = findTrio(headerMap, peopleMap);
			if (theProbands != null) {
				System.out.println("[MakeGeneURL] (1) theProbands[0]: " + theProbands[0] + "theProbands.length: "
						+ theProbands.length);
			}
			if (theProbands == null) {
				status = "Use ManagePedFile button to edit pedfile.";
			} else if (theProbands.length > 1) {
				status = "save choose";
			} else {
				theProband = theProbands[0];
				status = null;
			}
		}

		if (status == null) {

			// if no proband specified, find the 1st trio
			if (theProband == null) {
				theProbands = findTrio(headerMap, peopleMap);
				if (theProbands != null) {
					System.out.println("[MakeGeneURL] (2) theProbands[0]: " + theProbands[0] + "theProbands.length: "
							+ theProbands.length);
				}

				if (theProbands == null) {
					status = "No trio found.";
				} else if (theProbands.length > 1) {
					status = "choose";
				} else {
					theProband = theProbands[0];
					status = null;
				}

			}
		}

		System.out.println("[MakeGeneURL] before final processTrio status: " + status + " theProband: " + theProband);

		if (status == null) {
			// construct the URL
			ArrayList<String> urlsToLink = processTrio(theProband, headerMap, peopleMap);
			System.out.println("[MakeGeneURL] the url: " + urlsToLink.get(0));

			this.xmlResult = "<SUCCESS urlsToLink=\"" + urlsToLink.get(0) + "\"" + "/>";
		} else {
			// build the xml needed for the UI to call ManagePedFile
			Document ManagePedFile = buildManagePedFileXML(pedpath, VCFInfoXMLString, BAMInfoXMLString, headerMap,
					peopleMap, status);

			// this.xmlResult = "<" + status + "/>";
			XMLOutputter out = new org.jdom.output.XMLOutputter();
			this.xmlResult = out.outputString(ManagePedFile);

			System.out.println("[MakeGeneURL] xmlResult:");

		}
		setResponsePage(this.SUCCESS_JSP);

	} catch (Exception e) {
		log.error("An exception has occurred in MakeGeneURL ", e);
		throw new RollBackCommandException(e.getMessage());
	} finally {
		try {
			HibernateSession.closeSession();
		} catch (Exception e) {

		}
	}

	return this;
}

public static String setupPedFile(String pedpath, Map<Integer, String> headerMap, Map<String, String[]> peopleMap) {
	String status = null;
	int numlines = -1;
	boolean sawOne = false;

	System.out.println("[setupPedFile] pedpath: " + pedpath);

	try {
		BufferedReader br = new BufferedReader(new FileReader(pedpath));

		String line = null;
		boolean sawHeader = false;
		boolean sawbam = false;
		boolean sawvcf = false;
		int addedColumns = 0;
		int sampleId = -1;

		while ((line = br.readLine()) != null) {
			numlines++;
			if (line.equals("")) {
				continue;
			}

			if (!sawHeader) {
				if (!line.startsWith("#")) {
					status = "Header line missing.";
					break;
				}
				sawHeader = true;

				line = line.toLowerCase();
				if (line.indexOf("\tbam") >= 0) {
					sawbam = true;
				}
				if (line.indexOf("\tvcf") >= 0) {
					sawvcf = true;
				}

				if (!sawbam) {
					line += "\tbam";
					addedColumns++;
					status = "extend";
				}

				if (!sawvcf) {
					line += "\tvcf";
					addedColumns++;
					status = "extend";
				}

				String[] header = line.split("\t");

				for (int ii = 0; ii < header.length; ii++) {
					System.out.println("[setupPedFile] column: " + ii + " name: " + header[ii]);
					if (ii == 0) {
						headerMap.put(ii, header[ii].substring(1));
					} else {
						headerMap.put(ii, header[ii]);
					}
				}

				// which column is the sample_id
				sampleId = mapColumn("sample_id", headerMap);
				if (sampleId == -1) {
					// no sample_id is fatal
					status = "No sample id's found";
					break;
				}

				continue;
			} // end of if !sawHeader

			String[] person = line.split("\t");
			int realLength = person.length + addedColumns;
			String[] thePerson = new String[realLength];

			System.arraycopy(person, 0, thePerson, 0, person.length);

			String sampleid = thePerson[sampleId];

			// debug
			if (!sawOne) {
				System.out.println("[setupPedFile] sampleid: " + sampleid + " thePerson.length: " + thePerson.length
						+ " headerMap.size: " + headerMap.size());
				for (int ii = 0; ii < thePerson.length; ii++) {
					System.out.println("[setupPedFile] thePerson ii: " + ii + " value: " + thePerson[ii]);
				}
				sawOne = true;
			}

			peopleMap.put(sampleid, thePerson);
		}

		br.close();
	} catch (IOException e) {
		status = e.toString();
	}

	System.out.println("[setupPedFile] returning status: " + status);
	return status;
}

public static int mapColumn(String colName, Map<Integer, String> headerMap) {
	int column = -1;

	for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
		int key = entry.getKey();
		String value = entry.getValue();
		if (colName.equals(value)) {
			return key;
		}
	}

	return column;
}

/*
 * Add bam and vcf file information to everyone we can
 */
public static void augmentPedFile(Map<Integer, String> headerMap, Map<String, String[]> peopleMap,
		Map<String, String> vcfMap, ArrayList<String> bamList) {

	// column numbers
	int BAM = mapColumn("bam", headerMap);
	int VCF = mapColumn("vcf", headerMap);

	int numsaw = 0;
	int numvcf = 0;
	int numbam = 0;
	int numboth = 0;

	for (Map.Entry<String, String[]> entry : peopleMap.entrySet()) {
		String key = entry.getKey();
		String[] value = entry.getValue();
		numsaw++;

		// add VCF file if needed and we can find it
		if (value[VCF] == null || value[VCF].equals("")) {
			value[VCF] = vcfMap.get(key);
			if (value[VCF] != null) {
				// System.out.println("[augmentPedFile] cant find vcf, key: " + key);
				numvcf++;
			}
		}

		// same for the BAM file
		if (value[BAM] == null || value[BAM].equals("")) {
			value[BAM] = findBAM(key, bamList);
			if (value[BAM] != null) {
				numbam++;
			}
		}

		if (value[BAM] != null && value[VCF] != null) {
			numboth++;
		}

		// save it
		peopleMap.put(key, value);
	}

	System.out.println("[augmentPedFile] numsaw: " + numsaw + " numvcf: " + numvcf + " numbam: " + numbam
			+ " numboth: " + numboth);
}

public static String findBAM(String sampleId, ArrayList<String> bamList) {
	String theBamFile = null;

	for (String aBamFile : bamList) {
		if (aBamFile.contains(sampleId)) {
			theBamFile = aBamFile;
			break;
		}
	}

	return theBamFile;
}

public ArrayList<String> processTrio(String theProband, Map<Integer, String> headerMap, Map<String, String[]> peopleMap) {
	ArrayList<String> theURLS = new ArrayList<String>();

	System.out.println("[processTrio] theProband: " + theProband);

	// note the gene name is always set to BRCA1 because that information is not in the ped file
	String genename = "BRCA1";

	// column numbers
	int SAMPLEID = mapColumn("sample_id", headerMap);
	int FATHER = mapColumn("paternal_id", headerMap);
	int MOTHER = mapColumn("maternal_id", headerMap);
	int BAM = mapColumn("bam", headerMap);
	int VCF = mapColumn("vcf", headerMap);

	String url = geneiobioviewerURL + "/?rel0=proband";

	dir = setupDirectories();

	// get the proband
	String[] proband = peopleMap.get(theProband);
	if (proband == null) {
		return null;
	}

	String sample0 = proband[SAMPLEID];
	String motherId = proband[MOTHER];
	String fatherId = proband[FATHER];

	String theMother = "mother";
	String[] mother = peopleMap.get(motherId);
	if (mother == null) {
		theMother = "";
	}

	String theFather = "father";
	String[] father = peopleMap.get(fatherId);
	if (father == null) {
		theFather = "";
	}

	String bam1 = "";
	String vcf1 = "";
	String sample1 = "";

	if (!theMother.equals("")) {
		bam1 = makeURLLink(mother[BAM]);
		vcf1 = makeURLLink(mother[VCF]);
		sample1 = motherId;
	}

	String bam2 = "";
	String vcf2 = "";
	String sample2 = "";

	if (!theFather.equals("")) {
		bam2 = makeURLLink(father[BAM]);
		vcf2 = makeURLLink(father[VCF]);
		sample2 = fatherId;
	}

	String bam0 = makeURLLink(proband[BAM]);
	String vcf0 = makeURLLink(proband[VCF]);

	url = url + "&rel1=" + theMother + "&rel2=" + theFather + "&gene=" + genename + "&name0=proband" + "&bam0=" + bam0;
	url = url + "&name1=" + theMother + "&bam1=" + bam1;
	url = url + "&name2=" + theFather + "&bam2=" + bam2;
	url = url + "&vcf0=" + vcf0 + "&sample0=" + sample0;
	url = url + "&vcf1=" + vcf1 + "&sample1=" + sample1;
	url = url + "&vcf2=" + vcf2 + "&sample2=" + sample2;

	theURLS.add(url);

	return theURLS;
}

/*
 * Returns the sample_id of everyone with a mother, father, bam and vcf info for them and their parents
 */
public static String[] findTrio(Map<Integer, String> headerMap, Map<String, String[]> peopleMap) {
	String[] theProbands = null;
	ArrayList<String> probands = new ArrayList<String>();

	// column numbers
	int FATHER = mapColumn("paternal_id", headerMap);
	int MOTHER = mapColumn("maternal_id", headerMap);
	if (FATHER == -1 || MOTHER == -1) {
		return theProbands;
	}

	int BAM = mapColumn("bam", headerMap);
	int VCF = mapColumn("vcf", headerMap);

	// go thru the people
	for (Map.Entry<String, String[]> entry : peopleMap.entrySet()) {
		String key = entry.getKey();
		String[] value = entry.getValue();

		// must have a father
		if (value[FATHER] == null || value[FATHER].equals("")) {
			continue;
		}

		// must have a mother
		if (value[MOTHER] == null || value[MOTHER].equals("")) {
			continue;
		}

		// must have a bam file
		if (value[BAM] == null || value[BAM].equals("")) {
			continue;
		}

		// must have a vcf file
		if (value[VCF] == null || value[VCF].equals("")) {
			continue;
		}

		// father must have a bam and vcf file
		if (!hasBAMVCF(BAM, VCF, value[FATHER], peopleMap)) {
			continue;
		}

		// mother must have a bam and vcf file
		if (!hasBAMVCF(BAM, VCF, value[MOTHER], peopleMap)) {
			continue;
		}

		// we found one!
		probands.add(key);
		System.out.println("[findTrio] key: " + key + " probands.size(): " + probands.size());
		// theProband = key;
		// break;
	}

	if (probands.size() == 0) {
		return theProbands;
	}

	theProbands = new String[probands.size()];
	int nump = 0;
	for (String aProband : probands) {
		theProbands[nump] = aProband;
		nump++;
	}

	return theProbands;
}

public static boolean hasBAMVCF(int BAM, int VCF, String sampleId, Map<String, String[]> peopleMap) {
	boolean hasBAMVCF = false;

	String[] value = peopleMap.get(sampleId);

	if (value != null && value[BAM] != null && !value[BAM].equals("") && value[VCF] != null && !value[VCF].equals("")) {
		hasBAMVCF = true;
	}

	return hasBAMVCF;
}

public static void vcfInfoParser(String VCFInfoXMLString, String analysisDirectory, Map<String, String> vcfMap) {

	StringReader reader = new StringReader(VCFInfoXMLString);
	try {
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(reader);

		Element root = doc.getRootElement();

		for (Iterator i = root.getChildren("VCFPath").iterator(); i.hasNext();) {
			Element node = (Element) i.next();

			String pathName = analysisDirectory + "/" + node.getAttributeValue("path");

			addVCFIds(pathName, vcfMap);
		}

	} catch (JDOMException je) {
		log.error("MakeGeneURL Cannot parse VCFInfoXMLString", je);
	}
}

public static void bamInfoParser(String BAMInfoXMLString, String analysisDirectory, ArrayList<String> bamList) {

	StringReader reader = new StringReader(BAMInfoXMLString);
	try {
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(reader);

		Element root = doc.getRootElement();

		for (Iterator i = root.getChildren("BAMPath").iterator(); i.hasNext();) {
			Element node = (Element) i.next();

			String pathName = analysisDirectory + "/" + node.getAttributeValue("path");

			bamList.add(pathName);
		}

	} catch (JDOMException je) {
		log.error("MakeGeneURL Cannot parse BAMInfoXMLString", je);
	}
}

public static void addVCFIds(String VCFpathName, Map<String, String> vcfMap) {

	// System.out.println("[addVCFIds] VCFpathName: " + VCFpathName);

	// if windows we have to fix the path
	if (!File.separator.equals("/")) {
		VCFpathName = VCFpathName.replace("/", "\\");
		// System.out.println("[addVCFIds] VCFpathName: " + VCFpathName);
	}

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
		log.error("MakeGeneURL error procing tabix", e);
		System.out.println("[addVCFIds] tabix proc error: " + e);
	}

	// parse the ids out of the last line
	String[] pieces = lastline.split("\t");
	int numids = 0;

	boolean sawFormat = false;
	for (int ii = 0; ii < pieces.length; ii++) {
		if (sawFormat) {
			numids++;
			vcfMap.put(pieces[ii], VCFpathName);
			// System.out.println("[addVCFIds] ii: " + ii + " pieces[ii]: " + pieces[ii]);
			continue;
		}

		if (pieces[ii].equals("FORMAT")) {
			sawFormat = true;
		}
	}

	System.out.println("[addVCFIds] numids: " + numids);

}

private Document buildManagePedFileXML(String pedpath, String VCFInfoXMLString, String BAMInfoXMLString,
		Map<Integer, String> headerMap, Map<String, String[]> peopleMap, String status) {
	Document ManagePedFile = null;

	ManagePedFile = new Document(new Element("ManagePedFile"));

	Element vcfInfo = new Element("VCFInfo");
	Element bamInfo = new Element("BAMInfo");
	Element pedInfo = new Element("PEDInfo");
	Element pedFile = new Element("PEDFile");
	Element pedAction = new Element("PEDAction");

	StringReader reader = new StringReader(VCFInfoXMLString);
	try {
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(reader);
		Element root = doc.getRootElement();

		if (!root.getChildren("VCFPath").isEmpty()) {
			Element vcfPath = new Element("VCFPath");
			for (Iterator i = root.getChildren("VCFPath").iterator(); i.hasNext();) {
				Element node = (Element) i.next();
				Element node1 = ((Element) node.clone()).detach();

				vcfInfo.addContent(node1);
			}
			// vcfInfo.addContent(vcfPath);
		}

	} catch (JDOMException je) {
		log.error("MakeGeneURL Cannot parse VCFInfoXMLString", je);
	}

	reader = new StringReader(BAMInfoXMLString);
	try {
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(reader);
		Element root = doc.getRootElement();

		if (!root.getChildren("BAMPath").isEmpty()) {
			Element bamPath = new Element("BAMPath");
			for (Iterator i = root.getChildren("BAMPath").iterator(); i.hasNext();) {
				Element node = (Element) i.next();
				Element node1 = ((Element) node.clone()).detach();

				bamInfo.addContent(node1);
			}
			// bamInfo.addContent(bamPath);
		}

	} catch (JDOMException je) {
		log.error("MakeGeneURL Cannot parse BAMInfoXMLString", je);
	}

	Element piPath = new Element("PEDPath");
	piPath.setAttribute("path", pedpath);
	pedInfo.addContent(piPath);

	Element paDescription = new Element("ActionDescription");
	paDescription.setAttribute("reason", status);
	pedAction.addContent(paDescription);

	pedFile = buildPedFileXML(headerMap, peopleMap);

	ManagePedFile.getRootElement().addContent(vcfInfo);
	ManagePedFile.getRootElement().addContent(bamInfo);
	ManagePedFile.getRootElement().addContent(pedInfo);
	ManagePedFile.getRootElement().addContent(pedFile);
	ManagePedFile.getRootElement().addContent(pedAction);

	return ManagePedFile;
}

private Element buildPedFileXML(Map<Integer, String> headerMap, Map<String, String[]> peopleMap) {
	Element pedFile = new Element("PEDFile");

	// get the column names
	String[] columnNames = new String[headerMap.size()];

	for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
		int key = entry.getKey();
		String value = entry.getValue();
		columnNames[key] = value;

		Element pedHeader = new Element("PEDHeader");
		pedHeader.setAttribute("name", value);
		pedFile.addContent(pedHeader);
	}

	for (Map.Entry<String, String[]> entry : peopleMap.entrySet()) {
		String key = entry.getKey();
		String[] value = entry.getValue();

		Element pedEntry = new Element("PEDEntry");
		for (int ii = 0; ii < value.length; ii++) {
			String pvalue = value[ii];
			if (pvalue == null) {
				pvalue = "";
			}
			pedEntry.setAttribute(columnNames[ii], pvalue);
		}
		pedFile.addContent(pedEntry);

	}

	return pedFile;
}

private ArrayList<String> processTrioFile(String pedpath) {

	ArrayList<String> theURL = new ArrayList<String>();
	String error = null;

	String genename = "";
	HashMap<String, String[]> samples = new HashMap<String, String[]>();

	try {
		BufferedReader br = new BufferedReader(new FileReader(pedpath));

		String line = null;
		StringBuffer lines = new StringBuffer();

		while ((line = br.readLine()) != null) {
			lines.append(line);
		}

		br.close();

		String text = lines.toString();

		// get rid of leading { and trailing } if present
		text = text.trim();
		if (text.substring(0, 1).equals("{")) {
			text = text.substring(1, text.length() - 1);
		}

		int ipos = text.indexOf(",");
		if (ipos == -1) {
			// incorrect format
			error = "Incorrect format for definition.";
		} else {
			// get the gene name
			String gene = text.substring(0, ipos);
			text = text.substring(ipos + 1);

			String[] geneinfo = parseElement(gene);

			if (geneinfo[0].equals("gene")) {
				genename = geneinfo[1];

				// split into samples
				String[] theSamples = text.split("}");

				for (int i = 0; i < theSamples.length; i++) {
					int jpos = theSamples[i].indexOf("{");
					if (jpos == -1) {
						error = "Incorrect format for definition.";
						break;
					}

					// make sure we get rid of any extra comma's
					String theSample1 = theSamples[i].substring(0, jpos + 1).replace(",", ""); // note we include the {
					String[] sampleinfo = parseElement(theSample1); // note we include the {
					if (sampleinfo == null) {
						error = "Incorrect format for sample definition";
						break;
					}

					// get the values for this sample
					String[] aSample = parseSample(theSamples[i].substring(jpos + 1));

					if (aSample == null) {
						error = "Incorrect format for sample definition";
						break;
					}

					samples.put(sampleinfo[0], aSample);
				} // end of for

				if (error == null) {
					// build the url
					String url = buildURL(genename, samples);
					if (url != null) {
						theURL.add(url);
					} else {
						error = "Definition file is missing required information";
					}
				}

			} else {
				error = "Gene name not found";
			}
		}

		// did we have problems?
		if (error != null) {
			theURL.add("error");
			theURL.add(error);
		}

	} catch (IOException e) {
		theURL.add("error");
		theURL.add(e.toString());
	}

	return theURL;

}

private String buildURL(String genename, HashMap<String, String[]> samples) {
	String url = geneiobioviewerURL + "/?rel0=proband";

	dir = setupDirectories();

	// get the proband
	String[] proband = samples.get("sample0");
	if (proband == null) {
		return null;
	}

	String theMother = "mother";
	if (proband[2].equals("")) {
		theMother = "";
	}

	String theFather = "father";
	if (proband[3].equals("")) {
		theFather = "";
	}

	String bam1 = "";
	String vcf1 = "";
	String sample1 = "";

	if (!theMother.equals("")) {
		String[] mother = samples.get(proband[2]);
		if (mother != null) {
			bam1 = makeURLLink(mother[5]);
			vcf1 = makeURLLink(mother[6]);
			sample1 = mother[0];
		}
	}

	String bam2 = "";
	String vcf2 = "";
	String sample2 = "";

	if (!theFather.equals("")) {
		String[] father = samples.get(proband[3]);
		if (father != null) {
			bam2 = makeURLLink(father[5]);
			vcf2 = makeURLLink(father[6]);
			sample2 = father[0];
		}
	}

	String bam0 = makeURLLink(proband[5]);
	String vcf0 = makeURLLink(proband[6]);

	url = url + "&rel1=" + theMother + "&rel2=" + theFather + "&gene=" + genename + "&name0=proband" + "&bam0=" + bam0;
	url = url + "&name1=" + theMother + "&bam1=" + bam1;
	url = url + "&name2=" + theFather + "&bam2=" + bam2;
	url = url + "&vcf0=" + vcf0 + "&sample0=" + proband[0];
	url = url + "&vcf1=" + vcf1 + "&sample1=" + sample1;
	url = url + "&vcf2=" + vcf2 + "&sample2=" + sample2;

	return url;
}

private String[] parseElement(String element) {
	// System.out.println ("[parseElement] element: " + element);
	String[] result = new String[2];

	String[] pieces = element.split(":");
	if (pieces.length < 2 || pieces.length > 3) {
		return null;
	}

	if (pieces.length == 3) {
		// must be a : in a url, fix it
		pieces[1] = pieces[1] + ":" + pieces[2];
	}

	for (int i = 0; i < 2; i++) {
		result[i] = pieces[i].replace('"', ' ').trim();
	} // end of for

	// System.out.println ("[parseElement] result: " + result[0] + " " + result[1]);
	return result;
}

private String[] parseSample(String sample) {
	// System.out.println ("[parseSample] sample: " + sample);

	String[] result = new String[8];
	String[] name = { "id", "name", "mother id", "father id", "affected", "bam", "vcf", "gender" };
	int[] required = { 1, 0, 0, 0, 1, 0, 1, 0 };

	String[] pieces = sample.split(",");

	for (int i = 0; i < pieces.length; i++) {
		String[] attrvalue = parseElement(pieces[i]);
		// System.out.println ("[parseSample] i: " + i + " attrvalue[0]: " + attrvalue[0] + " attrvalue[1]: " + attrvalue[1]);

		if (attrvalue == null) {
			return null;
		}

		boolean foundit = false;
		for (int j = 0; j < name.length; j++) {
			// find the attribute name
			if (attrvalue[0].equalsIgnoreCase(name[j])) {
				required[j] = 0;
				result[j] = attrvalue[1];
				foundit = true;
				break;
			}
		}
		if (!foundit) {
			System.out.println("[parseSample] did not find attribute name: " + attrvalue[0]);
		}

	} // end of for i

	// get rid of any nulls
	for (int j = 0; j < result.length; j++) {
		if (result[j] == null) {
			result[j] = "";
		}
	}

	// did we miss any required ones?
	for (int j = 0; j < required.length; j++) {
		if (required[j] == 0) {
			continue;
		}

		result = null;
		break;
	}

	return result;
}

private String makeURLLink(String pathName) {
	// System.out.println ("[makeURLLink] pathName: " + pathName);

	String theLink = "";
	ArrayList<String> urlsToLoad = new ArrayList<String>();

	// the file we want to link to
	File[] filesToLink = new File[2];
	filesToLink[0] = new File(pathName);

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

	// System.out.println ("[MakeURLLink] dir.getName(): " + dir.getName());
	for (File f : filesToLink) {
		File annoFile = new File(dir, DataTrackUtil.stripBadURLChars(f.getName(), "_"));
		String dataTrackString = annoFile.toString();

		// System.out.println ("[makeURLLink] f.getName(): " + f.getName());
		// System.out.println ("[makeURLLink] dataTrackString: " + dataTrackString);

		// make soft link
		DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);

		// is it a bam index xxx.bai? If so then skip after making soft link.
		if (dataTrackString.endsWith(".bam.bai") || dataTrackString.endsWith(".vcf.gz.tbi"))
			continue;

		// if it's just a .bai, make a .bam.bai link so IOBIO will work
		if (!dataTrackString.endsWith(".bam.bai") && dataTrackString.endsWith(".bai")) {
			// fix the name
			dataTrackString = dataTrackString.substring(0, dataTrackString.length() - 4) + ".bam.bai";

			// make the soft link
			// System.out.println ("[makeURLLink] index f.getName(): " + f.getName());
			// System.out.println ("[makeURLLink] index dataTrackString: " + dataTrackString);

			DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, dataTrackString);

			continue;
		}

		// make URL to link
		// System.out.println ("[MakeGeneURL] dataTrackString: " + dataTrackString);
		String dataTrackPartialPath = dataTrackString;
		int index = dataTrackString.indexOf(Constants.URL_LINK_DIR_NAME);
		if (index != -1) {
			dataTrackPartialPath = dataTrackString.substring(index);
		}

		// System.out.println ("[MakeURLLink] adding to urlsToLoad: " + baseURL + dataTrackPartialPath);
		urlsToLoad.add(baseURL + dataTrackPartialPath);
	}

	// get the non-index file
	if (urlsToLoad.size() > 0) {
		theLink = urlsToLoad.get(0);
	}

	// System.out.println ("[makeURLLink] theLink: " + theLink);
	return theLink;
}

private File setupDirectories() {
	File dir = null;

	try {

		// look and or make directory to hold soft links to data
		// System.out.println ("[setupDirectories] baseURL: " + baseURL + " dataTrackFileServerWebContext: " + dataTrackFileServerWebContext);
		File urlLinkDir = DataTrackUtil.checkUCSCLinkDirectory(baseURL, dataTrackFileServerWebContext);
		// System.out.println ("[setupDirectories] urlLinkDir: " + urlLinkDir.getName());

		String linkPath = this.checkForUserFolderExistence(urlLinkDir, username);
		// System.out.println ("[setupDirectories] linkPath: " + linkPath);

		if (linkPath == null) {
			linkPath = UUID.randomUUID().toString() + username;
		}

		// Create the users' data directory
		// System.out.println ("[setupDirectories] urlLinkDir.getAbsoluteFile(): " + urlLinkDir.getAbsoluteFile());
		// System.out.println ("[setupDirectories] linkPath: " + linkPath);
		dir = new File(urlLinkDir.getAbsoluteFile(), linkPath);
		// System.out.println ("[setupDirectories] dir.getPath(): " + dir.getPath());

		if (!dir.exists())
			dir.mkdir();
	} catch (Exception e) {
		System.out.println("[MakeGeneURL] Error in setupDirectories: " + e);
	}

	return dir;
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

}