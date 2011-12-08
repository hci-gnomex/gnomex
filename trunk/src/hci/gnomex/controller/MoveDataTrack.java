package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.HibernateSession;

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



public class MoveDataTrack extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MoveDataTrack.class);
  
  
  private Integer idDataTrack = null;
  private Integer idGenomeBuild = null;
  private Integer idDataTrackFolder = null;
  private Integer idDataTrackFolderOld = null;
  private String  isMove = null;
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
     idDataTrack = new Integer(request.getParameter("idDataTrack"));
   } else {
     this.addInvalidField("idDataTrack", "idDataTrack is required.");
   }
   if (request.getParameter("idGenomeBuild") != null && !request.getParameter("idGenomeBuild").equals("")) {
     idGenomeBuild = new Integer(request.getParameter("idGenomeBuild"));
   } else {
     this.addInvalidField("idGenomeBuild", "idGenomeBuild is required.");
   }
   if (request.getParameter("idDataTrackFolder") != null && !request.getParameter("idDataTrackFolder").equals("")) {
     idDataTrackFolder = new Integer(request.getParameter("idDataTrackFolder"));
   } else {
     this.addInvalidField("idDataTrackFolder", "idDataTrackFolder is required.");
   }
   if (request.getParameter("idDataTrackFolderOld") != null && !request.getParameter("idDataTrackFolderOld").equals("")) {
     idDataTrackFolderOld = new Integer(request.getParameter("idDataTrackFolderOld"));
   } else {
     this.addInvalidField("idDataTrackFolderOld", "idDataTrackFolderOld is required.");
   }
   if (request.getParameter("isMove") != null && !request.getParameter("isMove").equals("")) {
     isMove = request.getParameter("isMove");
   } else {
     this.addInvalidField("isMove", "isMove is required.");
   }
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrack dataTrack = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      dataTrack = (DataTrack)sess.load(DataTrack.class, idDataTrack);
      
      GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

      // Make sure the user can write this dataTrack 
      if (isMove.equals("Y")) {
        if (!this.getSecAdvisor().canUpdate(dataTrack)) {
          addInvalidField("writep", "Insufficient permision to move dataTrack.");
        }
      }

      if (this.isValid()) {
      
        // Get the dataTrack grouping this dataTrack should be moved to.
        DataTrackFolder folderNew = null;
        if (idDataTrackFolder == null) {
          // If this is a root dataTrack, find the default root dataTrack
          // grouping for the genome version.
          folderNew = genomeBuild.getRootDataTrackFolder();
          if (folderNew == null) {
            throw new Exception("Cannot find root dataTrack grouping for " + genomeBuild.getGenomeBuildName());
          }
        } else {
          // Otherwise, find the dataTrack grouping passed in as a request parameter.
          folderNew = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
        }
  
  
  
        // The move/copy is disallowed if the parent dataTrack grouping belongs to a 
        // different genome version
        if (!folderNew.getIdGenomeBuild().equals(dataTrack.getIdGenomeBuild())) {
          throw new Exception("DataTrack '" + dataTrack.getName() + 
          "' cannot be moved to a different genome version");
        }
  
        // The move/copy is disallowed if the from and to dataTrack grouping are the
        // same
        if (idDataTrackFolderOld != null) {
          if (folderNew.getIdDataTrackFolder().equals(idDataTrackFolderOld)) {
            this.addInvalidField("movesame", "Move/copy operation to same dataTrack folder is not allowed.");
          }       
        } else {
          if (idDataTrackFolder == null) {
            this.addInvalidField("movesame1", "Move/copy operation to same folder is not allowed.");
          }
        }
  
        if (this.isValid()) {
          //
          // Add the dataTrack to the dataTrack grouping
          //
          Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
          for(Iterator<?> i = folderNew.getDataTracks().iterator(); i.hasNext();) {
            DataTrack a = DataTrack.class.cast(i.next());
            newDataTracks.add(a);
          }
          newDataTracks.add(dataTrack);
          folderNew.setDataTracks(newDataTracks);
          sess.flush();
    
    
    
          // If this is a move instead of a copy,
          // get the dataTrack folder this dataTrack should be removed from.
          if (isMove.equals("Y")) {
            DataTrackFolder dataTrackGroupingOld = null;
            if (idDataTrackFolderOld == null) {
              // If this is a root dataTrack, find the default root dataTrack
              // grouping for the genome build.
              dataTrackGroupingOld = genomeBuild.getRootDataTrackFolder();
              if (dataTrackGroupingOld == null) {
                throw new Exception("Cannot find root dataTrack grouping for " + genomeBuild.getGenomeBuildName());
              }
            } else {
              // Otherwise, find the dataTrack folder passed in as a request parameter.
              dataTrackGroupingOld = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolderOld));
            }
    
            //
            // Remove the dataTrack folder the dataTrack was in
            // by adding back the dataTracks to the dataTrack folder, 
            // excluding the dataTrack that has moved
            Set<DataTrack> dataTracksToKeep = new TreeSet<DataTrack>(new DataTrackComparator());
            for(Iterator<?> i1 = dataTrackGroupingOld.getDataTracks().iterator(); i1.hasNext();) {
              DataTrack a = DataTrack.class.cast(i1.next());
              if (a.getIdDataTrack().equals(dataTrack.getIdDataTrack())) {
                continue;
              }
              dataTracksToKeep.add(a);
            }
            dataTrackGroupingOld.setDataTracks(dataTracksToKeep);
            sess.flush();
    
          }          
          Element root = new Element("SUCCESS");
          Document doc = new Document(root);
          root.setAttribute("idDataTrack", dataTrack.getIdDataTrack().toString());
          root.setAttribute("idGenomeBuild", idGenomeBuild.toString());
          root.setAttribute("idDataTrackFolder", idDataTrackFolder != null ? idDataTrackFolder.toString() : "");
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding(true);
          this.xmlResult = out.outputString(doc);
          this.setResponsePage(SUCCESS_JSP);
        } else {
          setResponsePage(this.ERROR_JSP);
        }
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in MoveDataTrack ", e);
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