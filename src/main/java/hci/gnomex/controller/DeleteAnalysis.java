package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.gnomex.model.*;
import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import org.apache.log4j.Logger;


public class DeleteAnalysis extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(DeleteAnalysis.class);


  private Integer      idAnalysis = null;
  private String       serverName;
  private String       baseDir;
  private String       analysisFolderPath;



  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

   if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
     idAnalysis = new Integer(request.getParameter("idAnalysis"));
   } else {
     this.addInvalidField("idAnalysis", "idAnalysis is required.");
   }
   serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {
    try {

      Session sess = HibernateSession.currentSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
      String use_altstr = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.USE_ALT_REPOSITORY);
      if (use_altstr != null && use_altstr.equalsIgnoreCase("yes")) {
        baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
                PropertyDictionaryHelper.ANALYSIS_DIRECTORY_ALT,this.getUsername());
      }

      Analysis analysis = (Analysis)sess.load(Analysis.class, idAnalysis);
      Hibernate.initialize(analysis.getAnalysisGroups());
      analysis.setAnalysisGroups(null);

      if (this.getSecAdvisor().canDelete(analysis)) {

        // Make sure that there are not any data track files linked to analysis files
        if (analysis.getFiles().size() > 0) {
          SaveAnalysis.removeDataTrackFiles(sess, this.getSecAdvisor(), analysis, null);
        }

        // Remove files from file system
        for(Iterator i = analysis.getFiles().iterator(); i.hasNext();) {
          AnalysisFile af = (AnalysisFile)i.next();
          SaveAnalysis.removeAnalysisFileFromFileSystem(baseDir, analysis, af);
        }
        analysisFolderPath = SaveAnalysis.getAnalysisDirectory(baseDir, analysis);
        removeUnregisteredFiles(analysisFolderPath);

        //SaveAnalysis.removeAnalysisDirectoryFromFileSystem(baseDir, analysis);


        // Remove transfer logs associated with Analysis
        List transferLogs = sess.createQuery("SELECT x from TransferLog x where x.idAnalysis = '" + analysis.getIdAnalysis() + "'").list();
        for(Iterator i = transferLogs.iterator(); i.hasNext();) {
          TransferLog tl = (TransferLog)i.next();
          sess.delete(tl);
        }
        sess.flush();

        //
        // Delete (unlink) collaborators
        //
        for (Iterator i1 = analysis.getCollaborators().iterator(); i1.hasNext();) {
          AnalysisCollaborator ac = (AnalysisCollaborator)i1.next();
          sess.delete(ac);
        }
        sess.flush();


        //
        // Delete Analysis
        //
        sess.delete(analysis);

        sess.flush();



        this.xmlResult = "<SUCCESS/>";

        setResponsePage(this.SUCCESS_JSP);


      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permissions to delete this analysis.");
        setResponsePage(this.ERROR_JSP);
      }
    }catch (Exception e){
      this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in DeleteAnalysis ", e);

      throw new RollBackCommandException(e.getMessage());

    }

    return this;
  }

  private void removeUnregisteredFiles(String folderName) throws IOException{
    File f = new File(folderName);
    if(!f.exists()){
      return;
    }
    String [] folderContents = f.list();

    if(folderContents.length == 0){
      if (!f.delete()) {
        LOG.error("Unable to remove " + f.getName() + " from file system");
      }
      return;
    }

    for(int i = 0; i < folderContents.length; i++){
      File child = new File(folderName + Constants.FILE_SEPARATOR + folderContents[i]);
      if(child.isDirectory()){
        removeUnregisteredFiles(child.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
      }
      else{
        if (!child.delete()) {
          LOG.error("Unable to remove " + child.getName() + " from file system");
        }
      }
    }

    if (!f.delete()) {
      LOG.error("Unable to remove " + f.getName() + " from file system");
    }
  }






}
