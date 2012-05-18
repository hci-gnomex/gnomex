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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;




public class MakeDataTrackLinks extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MakeDataTrackLinks.class);
  
  private Integer idDataTrack;
  private String contextPath;
  private String baseURL;
  private String baseDir;
  private String analysisBaseDir;
  private String serverName;
  private String dataTrackFileServerURL;
  private String dataTrackFileServerWebContext;


  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
      idDataTrack = new Integer(request.getParameter("idDataTrack"));   
    } else {
      this.addInvalidField("idDataTrack", "idDataTrack is required");
    }

    contextPath = request.getContextPath();
    
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getHibernateSession(this.getUsername());
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackReadDirectory(serverName);
      analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisReadDirectory(serverName);
      dataTrackFileServerURL = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_URL);
      dataTrackFileServerWebContext = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.DATATRACK_FILESERVER_WEB_CONTEXT);
      
      
      String portNumber = PropertyDictionaryHelper.getInstance(sess).getQualifiedProperty(PropertyDictionary.HTTP_PORT, serverName);
      if (portNumber == null) {
        portNumber = "";
      } else {
        portNumber = ":" + portNumber;           
      }
      
      // We have to serve files from Tomcat, so use das2 base url
      baseURL =  dataTrackFileServerURL;

      DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

      if (this.getSecAdvisor().canRead(dataTrack)) {
        
        //make links fetching url(s)
        ArrayList<String>  urlsToLink = makeURLLinks(sess);
        StringBuilder sb = new StringBuilder(urlsToLink.get(0));
        for (int i=1; i< urlsToLink.size(); i++){
          sb.append("\n\n");
          sb.append(urlsToLink.get(i));
        }
        
        
        //post results with link urls
        this.xmlResult = "<SUCCESS urlsToLink=\"" +  sb.toString() + "\"" + "/>";
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
        this.getSecAdvisor().closeHibernateSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  private ArrayList<String>  makeURLLinks(Session sess) throws Exception {

    ArrayList<String> urlsToLoad = new ArrayList<String>();
    try {

      //load dataTrack
      DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));    
      String dataTrackName = DataTrackUtil.stripBadURLChars(dataTrack.getName(), "_") +"_"+dataTrack.getFileName()+"_";

      //check genome has UCSC name
      GenomeBuild gv = GenomeBuild.class.cast(sess.load(GenomeBuild.class, dataTrack.getIdGenomeBuild()));
      String ucscGenomeBuildName = gv.getUcscName();

      //pull all files and if needed auto convert xxx.useq to xxx.bb/.bw
      UCSCLinkFiles link = DataTrackUtil.fetchURLLinkFiles(dataTrack.getFiles(baseDir, analysisBaseDir), GNomExFrontController.getWebContextPath());
      File[] filesToLink = link.getFilesToLink();
      if (filesToLink== null)  throw new Exception ("No files to link?!");
      
      // When new .bw/.bb files are created, add analysis files and then link via data
      // track file to the data track.
      MakeDataTrackUCSCLinks.registerDataTrackFiles(sess, analysisBaseDir, dataTrack, filesToLink);


      //look and or make directory to hold softlinks to data, also removes old softlinks
      File urlLinkDir = DataTrackUtil.checkUCSCLinkDirectory(baseURL, dataTrackFileServerWebContext);

      //make randomWord 6 char long and append genome build names
      String randomWord = UUID.randomUUID().toString();
      if (randomWord.length() > 6) randomWord = randomWord.substring(0, 6) +"_"+gv.getDas2Name();
      if (ucscGenomeBuildName != null && ucscGenomeBuildName.length() !=0) randomWord = randomWord+"_"+ ucscGenomeBuildName;

      //create directory to hold links, need to do this so one can get the actual age of the links and not the age of the linked file
      File dir = new File (urlLinkDir, randomWord);
      dir.mkdir();

      //for each file, there might be two for xxx.bam and xxx.bai files, possibly two for converted useq files, plus/minus strands.

      for (File f: filesToLink){
        File annoFile = new File(dir, dataTrackName + DataTrackUtil.stripBadURLChars(f.getName(), "_"));
        String dataTrackString = annoFile.toString();

        //make soft link
        DataTrackUtil.makeSoftLinkViaUNIXCommandLine(f, annoFile);

        //is it a bam index xxx.bai? If so then skip after making soft link.
        if (dataTrackString.endsWith(".bai")) continue;

        //make URL to link
        int index = dataTrackString.indexOf(Constants.URL_LINK_DIR_NAME);
        String dataTrackPartialPath = dataTrackString.substring(index);

        urlsToLoad.add(baseURL + dataTrackPartialPath);
      }

    } catch (Exception e) {
      throw e;      
    } finally {
      if (sess != null) sess.close();
    }
    return urlsToLoad;

  }
}