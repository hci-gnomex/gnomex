package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import java.io.*;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class MakeGeneURL extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MakeDataTrackLinks.class);
  
  private String serverName;
  private String triopath;


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
      
      // parse the definition file and construct the URL
      ArrayList<String>  urlsToLink = processTrioFile(triopath);
      System.out.println ("[MakeGeneURL] the url: " + urlsToLink.get(0));
      
      this.xmlResult = "<SUCCESS urlsToLink=\"" +  urlsToLink.get(0) + "\"" + "/>"; 
      setResponsePage(this.SUCCESS_JSP);
      
    }catch (NamingException e){
      log.error("An exception has occurred in MakeGeneURL ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in MakeGeneURL ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in MakeGeneURL ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
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
	  String url = Constants.GENE_IOBIO_URL + "/?rel0=proband";
	  
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
			  bam1 = mother[5];
			  vcf1 = mother[6];
			  sample1 = mother[0];
		  }		  
	  }
	  
	  String bam2 = "";
	  String vcf2 = "";
	  String sample2 = "";
	  
	  if (!theFather.equals("")) {
		  String [] father = samples.get(proband[3]);
		  if (father != null) {
			  bam2 = father[5];
			  vcf2 = father[6];
			  sample2 = father[0];
		  }		  
	  }
	  
	  
	  
	  url = url + "&rel1=" + theMother + "&rel2=" + theFather + "&gene=" + genename + "&name0=proband" + "&bam0=" + proband[5];
	  url = url + "&name1=" + theMother + "&bam1=" + bam1;
	  url = url + "&name2=" + theFather + "&bam2=" + bam2;
	  url = url + "&vcf0=" + proband[6] + "&sample0=" + proband[0];
	  url = url + "&vcf1=" + vcf1 + "&sample1=" + sample1;
	  url = url + "&vcf2=" + vcf2 + "&sample2=" + sample2;
	  
	  return url;
  }

  
  private String [] parseElement (String element) {
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
 	  	  
	  return result;
  }

  
  private String [] parseSample (String sample) {
	  String [] result = new String[8];
	  String [] name = {"id", "name", "mother id", "father id", "affected", "bam", "vcf", "gender"};
	  int [] required = {1,0,0,0,1,0,1,0};
	  
	  String [] pieces = sample.split(",");
	  
	  for (int i=0;i<pieces.length;i++) {
		  String [] attrvalue = parseElement(pieces[i]);
		  if (attrvalue == null) {
			  return null;
		  }
		  
		  boolean foundit = false;
		  for (int j=0;j<name.length;j++) {
			  // find the attribute name
			  if (attrvalue[0].equals(name[j])) {
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
	  
	  return result;
  }

 }