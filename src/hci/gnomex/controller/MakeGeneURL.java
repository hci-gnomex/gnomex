package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;

public class MakeGeneURL extends GNomExCommand implements Serializable {
  
  private static Logger LOG = Logger.getLogger(MakeDataTrackLinks.class);
  
  private String serverName;
  private String triopath;
  private String geneiobioviewerURL;
  private String baseDir;
  private String dataTrackFileServerWebContext;
  private String baseURL;
  private File dir;


  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {   
    // the gene.iobio definition file
    if (request.getParameter("fileName") != null && !request.getParameter("fileName").equals("")) {
    	triopath = request.getParameter("fileName");   
      }    
    serverName = request.getServerName();
    
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = HibernateSession.currentSession(this.getSecAdvisor().getUsername());
      
      String portNumber = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(PropertyDictionary.HTTP_PORT, serverName);
      if (portNumber == null) {
        portNumber = "";
      } else {
        portNumber = ":" + portNumber;           
      }
      
      geneiobioviewerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.GENE_IOBIO_VIEWER_URL);
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
      dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);
      baseURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_URL);
      
      // parse the definition file and construct the URL
      ArrayList<String>  urlsToLink = processTrioFile(triopath);
      System.out.println ("[MakeGeneURL] the url: " + urlsToLink.get(0));
      
      this.xmlResult = "<SUCCESS urlsToLink=\"" +  urlsToLink.get(0) + "\"" + "/>"; 
      setResponsePage(this.SUCCESS_JSP);
      
    }catch (Exception e) {
      LOG.error("An exception has occurred in MakeGeneURL ", e);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e){
        LOG.error("Error", e);
      }
    }
    
    return this;
  }
  
  private ArrayList<String> processTrioFile (String triopath) {
	  
	  ArrayList<String> theURL = new ArrayList<String>();
	  String error = null;
  
	  String genename = "";
	  HashMap<String,String []> samples = new HashMap<String,String []>();
	  
	  try {
		  BufferedReader br = new BufferedReader(new FileReader(triopath));

		  String line = null;
		  StringBuffer lines = new StringBuffer();

		  while ((line = br.readLine()) != null) {
			  lines.append(line);
		  }
		  
		  br.close();
		  
		  String text = lines.toString();
		  
		  // get rid of leading { and trailing } if present
		  text = text.trim();
		  if (text.substring(0,1).equals("{")) {
			  text = text.substring(1, text.length()-1);
		  }
		  
		  int ipos = text.indexOf(",");
		  if (ipos == -1) {
			  // incorrect format
			  error = "Incorrect format for definition.";
		  } else {
			  // get the gene name
			  String gene = text.substring(0,ipos);
			  text = text.substring(ipos+1);
			  
			  String [] geneinfo = parseElement(gene);
			  
			  if (geneinfo[0].equals("gene")) {
				  genename = geneinfo[1];
				  
				  // split into samples
				  String [] theSamples = text.split("}");
				  
				  for (int i=0; i< theSamples.length; i++) {
					  int jpos = theSamples[i].indexOf("{");
					  if (jpos == -1) {
						  error = "Incorrect format for definition.";
						  break;
					  }
					  
					  // make sure we get rid of any extra comma's
					  String theSample1 = theSamples[i].substring(0, jpos+1).replace(",", "");	// note we include the {
					  String [] sampleinfo = parseElement(theSample1);     // note we include the {
					  if (sampleinfo == null) {
						  error = "Incorrect format for sample definition";
						  break;
					  }
					  
					  
					  // get the values for this sample
					  String [] aSample = parseSample (theSamples[i].substring(jpos+1));
					  
					  if (aSample == null) {
						  error = "Incorrect format for sample definition";
						  break;
					  }
					  
					  samples.put(sampleinfo[0], aSample);					  
				  } // end of for
				  
				  if (error == null) {
					  // build the url
					  String url = buildURL (genename,samples);
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

  
  private String buildURL (String genename, HashMap<String,String[]> samples) {
	  String url = geneiobioviewerURL + "/?rel0=proband";
	  
	  dir = setupDirectories();
	  
	  // get the proband
	  String [] proband = samples.get("sample0");
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
		  String [] mother = samples.get(proband[2]);
		  if (mother != null) {
			  bam1 = makeURLLink (mother[5]);
			  vcf1 = makeURLLink (mother[6]);
			  sample1 = mother[0];
		  }		  
	  }
	  
	  String bam2 = "";
	  String vcf2 = "";
	  String sample2 = "";
	  
	  if (!theFather.equals("")) {
		  String [] father = samples.get(proband[3]);
		  if (father != null) {
			  bam2 = makeURLLink(father[5]);
			  vcf2 = makeURLLink(father[6]);
			  sample2 = father[0];
		  }		  
	  }
	  
	  String bam0 = makeURLLink (proband[5]);
	  String vcf0 = makeURLLink (proband[6]);
	  
	  url = url + "&rel1=" + theMother + "&rel2=" + theFather + "&gene=" + genename + "&name0=proband" + "&bam0=" + bam0;
	  url = url + "&name1=" + theMother + "&bam1=" + bam1;
	  url = url + "&name2=" + theFather + "&bam2=" + bam2;
	  url = url + "&vcf0=" + vcf0 + "&sample0=" + proband[0];
	  url = url + "&vcf1=" + vcf1 + "&sample1=" + sample1;
	  url = url + "&vcf2=" + vcf2 + "&sample2=" + sample2;
	  
	  return url;
  }

  
  private String [] parseElement (String element) {
//	  System.out.println ("[parseElement] element: " + element);
	  String [] result = new String[2];
	  
	  String [] pieces = element.split(":");
	  if (pieces.length < 2 || pieces.length > 3) {
		  return null;		  
	  }
	  
	  if (pieces.length == 3) {
		  // must be a : in a url, fix it
		  pieces[1] = pieces[1] + ":" + pieces[2];
	  }
	  
	  for (int i = 0; i < 2; i++) {
		  result[i] = pieces[i].replace('"',' ').trim();		  
	  } // end of for
 	  	  
//	  System.out.println ("[parseElement] result: " + result[0] + " " + result[1]);
	  return result;
  }

  
  private String [] parseSample (String sample) {
//	  System.out.println ("[parseSample] sample: " + sample);
	  
	  String [] result = new String[8];
	  String [] name = {"id", "name", "mother id", "father id", "affected", "bam", "vcf", "gender"};
	  int [] required = {1,0,0,0,1,0,1,0};
	  
	  String [] pieces = sample.split(",");
	  
	  for (int i=0;i<pieces.length;i++) {
		  String [] attrvalue = parseElement(pieces[i]);
//		  System.out.println ("[parseSample] i: " + i + " attrvalue[0]: " + attrvalue[0] + " attrvalue[1]: " + attrvalue[1]);

		  if (attrvalue == null) {
			  return null;
		  }
		  
		  boolean foundit = false;
		  for (int j=0;j<name.length;j++) {
			  // find the attribute name
			  if (attrvalue[0].equalsIgnoreCase(name[j])) {
				  required[j] = 0;
				  result[j] = attrvalue[1];
				  foundit = true;
				  break;
			  }
		  }
		  if (!foundit) {
			  System.out.println ("[parseSample] did not find attribute name: " + attrvalue[0]);
		  }
		  
	  } // end of for i
	  
	  // get rid of any nulls
	  for (int j=0;j<result.length;j++) {
		  if (result[j] == null) {
			  result[j] = "";
		  }
	  }	  
	  
	  // did we miss any required ones?
	  for (int j=0;j<required.length;j++) {
		  if (required[j] == 0) {
			  continue;
		  }
		  
		  result = null;
		  break;
	  }
	  
	  // debug
//	  if (result != null) {
//		  for (int i = 0; i < result.length; i++) {
//			  System.out.println ("[parseSample] result[" + i + "]: " + result[i]);
//		  }
//	  }
	  return result;
  }
  
  private String makeURLLink (String pathName) {
//	  System.out.println ("[makeURLLink] pathName: " + pathName);
	  
	String theLink = "";
	ArrayList<String> urlsToLoad = new ArrayList<String>();
	  
  	// the file we want to link to
  	File [] filesToLink = new File[2];
  	filesToLink[0] = new File(pathName);
  	
  	// add the correct index file
  	if (pathName.endsWith(".vcf.gz")) {
  		filesToLink[1] = new File(pathName + ".tbi");
  	} else {
  		// figure out whether the .bam.bai or the .bai file exists
  		File bambai = new File (pathName + ".bai");
  		if (bambai.exists()) {
  			filesToLink[1] = new File(pathName + ".bai");
  		} else {
  			// we will assume the index file ends in .bai (without the .bam)
  			// we don't check or complain if it doesn't because the user can't do anything anyway
  			filesToLink[1] = new File (pathName.substring(0, pathName.length() - 4) + ".bai");
  		}
  	}
	  

//  	System.out.println ("[MakeURLLink] dir.getName(): " + dir.getName());
    for (File f: filesToLink) {
      File annoFile = new File(dir, DataTrackUtil.stripBadURLChars(f.getName(), "_"));
      String dataTrackString = annoFile.toString();
      
//      System.out.println ("[makeURLLink] f.getName(): " + f.getName());
//      System.out.println ("[makeURLLink] dataTrackString: " + dataTrackString);

      //make soft link
      DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);

      //is it a bam index xxx.bai? If so then skip after making soft link.
      if (dataTrackString.endsWith(".bam.bai") || dataTrackString.endsWith(".vcf.gz.tbi")) continue;
      
      // if it's just a .bai, make a .bam.bai link so IOBIO will work
      if (!dataTrackString.endsWith(".bam.bai") && dataTrackString.endsWith(".bai")) {
    	  // fix the name
    	  dataTrackString = dataTrackString.substring(0, dataTrackString.length() - 4) + ".bam.bai";
    	  
    	  // make the soft link
//          System.out.println ("[makeURLLink] index f.getName(): " + f.getName());
//          System.out.println ("[makeURLLink] index dataTrackString: " + dataTrackString);
    	  
    	  DataTrackUtil.makeSoftLinkViaUNIXCommandLine (f, dataTrackString);
    	  
    	  continue;    	  
      }

      // make URL to link
//      System.out.println ("[MakeGeneURL] dataTrackString: " + dataTrackString);
      String dataTrackPartialPath = dataTrackString;
      int index = dataTrackString.indexOf(Constants.URL_LINK_DIR_NAME);
      if (index != -1) {
    	  dataTrackPartialPath = dataTrackString.substring(index);
      }
      
//      System.out.println ("[MakeURLLink] adding to urlsToLoad: " + baseURL + dataTrackPartialPath);
      urlsToLoad.add(baseURL + dataTrackPartialPath);
    }
    
    // get the non-index file
	if (urlsToLoad.size() > 0) {
	     theLink = urlsToLoad.get(0);
	}
	
//	System.out.println ("[makeURLLink] theLink: " + theLink);
	return theLink;	  
  }
  
  private File setupDirectories () {
	  File dir = null;
	  
	  try {
	  		
	    // look and or make directory to hold soft links to data
//		System.out.println ("[setupDirectories] baseURL: " + baseURL + " dataTrackFileServerWebContext: " + dataTrackFileServerWebContext);
	    File urlLinkDir = DataTrackUtil.checkUCSCLinkDirectory(baseURL, dataTrackFileServerWebContext);
//	    System.out.println ("[setupDirectories] urlLinkDir: " + urlLinkDir.getName());
	    
	    String linkPath = this.checkForUserFolderExistence(urlLinkDir, username);
//	    System.out.println ("[setupDirectories] linkPath: " + linkPath);
	  	
		  if (linkPath == null) {
		    linkPath = UUID.randomUUID().toString() + username;
		  }
	      
		  //Create the users' data directory
//		  System.out.println ("[setupDirectories] urlLinkDir.getAbsoluteFile(): " + urlLinkDir.getAbsoluteFile());
//		  System.out.println ("[setupDirectories] linkPath: " + linkPath);
		  dir = new File(urlLinkDir.getAbsoluteFile(),linkPath);
//		  System.out.println ("[setupDirectories] dir.getPath(): " + dir.getPath());
		  
		  if (!dir.exists())
			  dir.mkdir();
	  }
	  catch (Exception e) {
	  	LOG.error("[MakeGeneURL] Error in setupDirectories: ", e);
	  }
	  
	  return dir;
  }	  
  
  private String checkForUserFolderExistence(File igvLinkDir, String username) throws Exception{
		File[] directoryList = igvLinkDir.listFiles();
		
		String desiredDirectory = null;
		
		for (File directory: directoryList) {
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