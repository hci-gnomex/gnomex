package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.UCSCLinkFiles;
import hci.gnomex.utility.DataTrackUtil;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.USeqUtilities;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.hibernate.Session;

import edu.utah.seq.useq.apps.USeq2UCSCBig;




public class MakeDataTrackUCSCLinks extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MakeDataTrackUCSCLinks.class);
  
  private Integer idDataTrack;
  private String baseURL;
  private String baseDir;
  private String analysisBaseDir;
  private String serverName;


  private static boolean autoConvertUSeqArchives = true;


  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
      idDataTrack = new Integer(request.getParameter("idDataTrack"));   
    } else {
      this.addInvalidField("idDataTrack", "idDataTrack is required");
    }
    
    baseURL = "";
    StringBuffer fullPath = request.getRequestURL();
    String extraPath = request.getServletPath() + request.getPathInfo();
    int pos = fullPath.lastIndexOf(extraPath);
    if (pos > 0) {
      baseURL = fullPath.substring(0, pos);
    };
    
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackReadDirectory(serverName);
      analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisReadDirectory(serverName);
      
      DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

      if (this.getSecAdvisor().canRead(dataTrack)) {
        
      
        //make links fetching url(s)
        ArrayList<String>  urlsToLoad = makeUCSCLink(sess);
        String url1 = urlsToLoad.get(0);
        String url2 = "";
        if (urlsToLoad.size() == 2) url2 = urlsToLoad.get(1);
        
        //post results with link urls
        this.xmlResult = "<SUCCESS ucscURL1=\"" +  url1 + "\" ucscURL2=\"" +  url2 + "\"" + "/>";
        setResponsePage(this.SUCCESS_JSP);
        
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permission to access data track");
      }

    }catch (NamingException e){
      log.error("An exception has occurred in MakeDataTrackUCSCLinks ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in MakeDataTrackUCSCLinks ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in MakeDataTrackUCSCLinks ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private ArrayList<String>  makeUCSCLink(Session sess) throws Exception {

    ArrayList<String> urlsToLoad = new ArrayList<String>();
    try {


      // What is the users preferred ucsc url?
      String ucscUrl = this.getSecAdvisor().getUserUcscUrl();

      //load dataTrack
      DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));      

      //check genome has UCSC name
      GenomeBuild gv = GenomeBuild.class.cast(sess.load(GenomeBuild.class, dataTrack.getIdGenomeBuild()));
      String ucscGenomeBuildName = gv.getUcscName();
      if (ucscGenomeBuildName == null || ucscGenomeBuildName.length() ==0) throw new Exception ("Missing UCSC Genome Version name, update, and resubmit.");

      //check if dataTrack has exportable file type (xxx.bam, xxx.bai, xxx.bw, xxx.bb, xxx.useq (will be converted if autoConvert is true))
      UCSCLinkFiles link = DataTrackUtil.fetchUCSCLinkFiles(dataTrack.getFiles(baseDir, analysisBaseDir), GNomExFrontController.getWebContextPath());
      File[] filesToLink = link.getFilesToLink();
      if (filesToLink== null)  throw new Exception ("No files to link?!");

      //look and or make directory to hold softlinks to data, also removes old softlinks
      File urlLinkDir = DataTrackUtil.checkUCSCLinkDirectory(baseURL, GNomExFrontController.getWebContextPath());

      //what data type (bam, bigBed, bigWig)
      String type = "type=" + DataTrackUtil.fetchUCSCDataType (filesToLink);

      //is there a summary?
      String summary = dataTrack.getSummary();
      if (summary !=null && summary.trim().length() !=0) {
        summary = Constants.HTML_BRACKETS.matcher(summary).replaceAll("");
        summary = "description=\""+summary+"\"";
      }
      else summary = "";

      //TODO: color indicated? look for property named color, convert to RGB, comma delimited and set 'color='

      String randomWord = UUID.randomUUID().toString();

      //create directory to hold links, need to do this so one can get the actual age of the links and not the age of the linked file
      File dir = new File (urlLinkDir, randomWord);
      dir.mkdir();

      //for each file, there might be two for xxx.bam and xxx.bai files, possibly two for converted useq files, plus/minus strands, otherwise just one.
      String customHttpLink = null;
      String toEncode = null;
      for (File f: filesToLink){
        File annoFile = new File(dir, DataTrackUtil.stripBadURLChars(f.getName(), "_"));
        String annoString = annoFile.toString();

        //make soft link
        DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);

        //is it a bam index xxx.bai? If so then skip after making soft link.
        if (annoString.endsWith(".bai")) continue;

        //stranded?
        String strand = "";
        if (link.isStranded()){
          if (annoString.endsWith("_Plus.bw")) strand = " + ";
          else if (annoString.endsWith("_Minus.bw")) strand = " - ";
          else throw new Exception ("\nCan't determine strand of bw file? "+annoString);
        }

        String datasetName = "name=\""+dataTrack.getName()+ strand +" "+dataTrack.getFileName()+"\"";

        //make bigData URL e.g. bigDataUrl=http://genome.ucsc.edu/goldenPath/help/examples/bigBedExample.bb
        int index = annoString.indexOf(Constants.URL_LINK_DIR_NAME);
        String annoPartialPath = annoString.substring(index);
        String bigDataUrl = "bigDataUrl="+ baseURL + annoPartialPath;

        //make final html link
        customHttpLink = ucscUrl + "/cgi-bin/hgTracks?db=" + ucscGenomeBuildName + "&hgct_customText=track+visibility=full+";
        toEncode = type +" "+ datasetName +" "+ summary +" "+ bigDataUrl;

        //System.out.println("LinkForLoading "+customHttpLink + toEncode);
        //System.out.println(customHttpLink+ GeneralUtils.URLEncode(toEncode)+"\n");

        urlsToLoad.add(customHttpLink + URLEncoder.encode(toEncode, "UTF-8"));
      }

    } catch (Exception e) {
      throw e;      
    } finally {
      if (sess != null) sess.close();
    }
    return urlsToLoad;

  }
  


}