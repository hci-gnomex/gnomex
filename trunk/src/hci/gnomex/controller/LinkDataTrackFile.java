package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFile;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Visibility;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;



public class LinkDataTrackFile extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LinkDataTrackFile.class);
  
  
  private Integer idDataTrack = null;
  private Integer idGenomeBuild = null;
  private Integer idDataTrackFolder = null;
  private Integer idAnalysisFile = null;
  private Integer idLab = null;
  private boolean isNewDataTrack = false;
  
  private String serverName = null;
  private String baseDir = null;
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
     idDataTrack = new Integer(request.getParameter("idDataTrack"));
   } 
   if (idDataTrack == null) {
     isNewDataTrack = true;
   }
   if (request.getParameter("idGenomeBuild") != null && !request.getParameter("idGenomeBuild").equals("")) {
     idGenomeBuild = new Integer(request.getParameter("idGenomeBuild"));
   } else {
     this.addInvalidField("idGenomeBuild", "idGenomeBuild is required.");
   }
   if (request.getParameter("idDataTrackFolder") != null && !request.getParameter("idDataTrackFolder").equals("")) {
     idDataTrackFolder = new Integer(request.getParameter("idDataTrackFolder"));
   } 
   if (request.getParameter("idAnalysisFile") != null && !request.getParameter("idAnalysisFile").equals("")) {
     idAnalysisFile = new Integer(request.getParameter("idAnalysisFile"));
   } else {
     this.addInvalidField("idAnalysisFile", "idAnalysisFile is required.");
   }
   if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
     idLab = new Integer(request.getParameter("idLab"));
   } else if (isNewDataTrack) {
     this.addInvalidField("idLab", "idLab is required.");
   }
   serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrack dataTrack = null;
    AnalysisFile analysisFile = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackWriteDirectory(serverName);
      
      analysisFile = (AnalysisFile)sess.load(AnalysisFile.class, idAnalysisFile);
      
      if (isNewDataTrack) {
        dataTrack = new DataTrack();
        dataTrack.setName(analysisFile.getAnalysis().getNumber() + "_" + analysisFile.getFileName());
        dataTrack.setCodeVisibility(Visibility.VISIBLE_TO_GROUP_MEMBERS);
        dataTrack.setIdLab(idLab);
        dataTrack.setIdGenomeBuild(idGenomeBuild);
        dataTrack.setDataPath(baseDir);
        dataTrack.setCreatedBy(this.getUsername());
        dataTrack.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
        if (!this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_WRITE_ANY_OBJECT)) {
          dataTrack.setIdAppUser(this.getSecAdvisor().getIdAppUser());
        }
        dataTrack.setIsLoaded("N");
        
        sess.save(dataTrack);
        dataTrack.setFileName("DT" + dataTrack.getIdDataTrack());
        sess.flush();
      } else {
        dataTrack = (DataTrack)sess.load(DataTrack.class, idDataTrack);               
      }
      // Make sure the user can write this data Track 
      if (!this.getSecAdvisor().canUpdate(dataTrack)) {
        addInvalidField("writep", "Insufficient permision to write to data track.");
      }
      
      // Add the analysis file
      DataTrackFile dtFile = new DataTrackFile();
      dtFile.setIdAnalysisFile(idAnalysisFile);
      dtFile.setIdDataTrack(dataTrack.getIdDataTrack());
      sess.save(dtFile);
      sess.flush();
      

      // If this is a new data track, add it to the folder
      if (this.isValid()) {
      
        if (isNewDataTrack) {
          GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

          DataTrackFolder folderNew = null;
          if (idDataTrackFolder == null) {
            // If this is a root dataTrack, find the default root dataTrack
            // grouping for the genome version.
            folderNew = genomeBuild.getRootDataTrackFolder();
            if (folderNew == null) {
              throw new Exception("Cannot find root dataTrack grouping for " + genomeBuild.getGenomeBuildName());
            }
            idDataTrackFolder = folderNew.getIdDataTrackFolder();
          } else {
            // Otherwise, find the dataTrack grouping passed in as a request parameter.
            folderNew = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
          }
          if (this.isValid()) {
            //
            // Add the dataTrack to the dataTrack folder
            //
            Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
            for(Iterator<?> i = folderNew.getDataTracks().iterator(); i.hasNext();) {
              DataTrack a = DataTrack.class.cast(i.next());
              newDataTracks.add(a);
            }
            newDataTracks.add(dataTrack);
            folderNew.setDataTracks(newDataTracks);
            sess.flush();
          }
        }
      }
  
      if (isValid()) {
          Element root = new Element("SUCCESS");
          Document doc = new Document(root);
          root.setAttribute("idDataTrack", dataTrack.getIdDataTrack().toString());
          root.setAttribute("idGenomeBuild", idGenomeBuild.toString());
          root.setAttribute("idDataTrackFolder", idDataTrackFolder != null ? idDataTrackFolder.toString() : "");
          root.setAttribute("idAnalysisFile", idAnalysisFile.toString());
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding(true);
          this.xmlResult = out.outputString(doc);
          this.setResponsePage(SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in LinkDataTrackFile ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
}