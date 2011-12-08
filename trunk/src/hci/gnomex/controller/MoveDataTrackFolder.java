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



public class MoveDataTrackFolder extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MoveDataTrackFolder.class);
  
  
  private Integer idGenomeBuild = null;
  private Integer idDataTrackFolder = null;
  private Integer idParentDataTrackFolder = null;
  private String  isMove = null;
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
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
   if (request.getParameter("idParentDataTrackFolder") != null && !request.getParameter("idParentDataTrackFolder").equals("")) {
     idParentDataTrackFolder = new Integer(request.getParameter("idParentDataTrackFolder"));
   } else {
     this.addInvalidField("idParentDataTrackFolder", "idParentDataTrackFolder is required.");
   }
   if (request.getParameter("isMove") != null && !request.getParameter("isMove").equals("")) {
     isMove = request.getParameter("isMove");
   } else {
     this.addInvalidField("isMove", "isMove is required.");
   }
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrackFolder dataTrackFolder = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      dataTrackFolder = (DataTrackFolder)sess.load(DataTrackFolder.class, idDataTrackFolder);
      
      GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

      // Make sure the user can write this dataTrack folder
      if (isMove.equals("Y")) {
        if (!this.getSecAdvisor().canUpdate(dataTrackFolder)) {
          addInvalidField("writep", "Insufficient permision to move dataTrack folder.");
        }
      }

      if (this.isValid()) {
      
        // Get the dataTrack grouping this dataTrack grouping should be moved to.
        DataTrackFolder parentDataTrackFolder = null;
        if (idParentDataTrackFolder == null) {
          // If this is a root dataTrack, find the default root dataTrack
          // grouping for the genome build.
          parentDataTrackFolder = genomeBuild.getRootDataTrackFolder();
          if (parentDataTrackFolder == null) {
            throw new Exception("Cannot find root dataTrack grouping for " + genomeBuild.getGenomeBuildName());
          }
        } else {
          // Otherwise, find the dataTrack grouping passed in as a request parameter.
          parentDataTrackFolder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idParentDataTrackFolder));
        }





        // If this is a copy instead of a move,
        // clone the dataTrack grouping, leaving the existing one as-is.
        if (isMove.equals("Y")) {
          dataTrackFolder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
        } else {
          DataTrackFolder folder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
          dataTrackFolder = new DataTrackFolder();
          dataTrackFolder.setName(folder.getName());
          dataTrackFolder.setDescription(folder.getDescription());
          dataTrackFolder.setIdGenomeBuild(folder.getIdGenomeBuild());
          dataTrackFolder.setIdLab(folder.getIdLab());       

          Set<DataTrack> dataTracksToKeep = new TreeSet<DataTrack>(new DataTrackComparator());
          for(Iterator<?> i = folder.getDataTracks().iterator(); i.hasNext();) {
            DataTrack a = DataTrack.class.cast(i.next());
            dataTracksToKeep.add(a);
          }
          dataTrackFolder.setDataTracks(dataTracksToKeep);
          sess.save(dataTrackFolder);
        }

        // The move/copy is disallowed if the parent dataTrack grouping belongs to a 
        // different genome build
        if (!parentDataTrackFolder.getIdGenomeBuild().equals(dataTrackFolder.getIdGenomeBuild())) {
          throw new Exception("DataTrack folder '" + dataTrackFolder.getName() + 
          "' cannot be moved to a different genome version");
        }

        // The move/copy is disallowed if the from and to dataTrack grouping are the
        // same
        if (parentDataTrackFolder.getIdDataTrackFolder().equals(idDataTrackFolder)) {
          throw new Exception("Move/copy operation to same dataTrack folder is not allowed.");
        }

        // Set the parent dataTrack grouping
        dataTrackFolder.setIdParentDataTrackFolder(parentDataTrackFolder.getIdDataTrackFolder());
    
        sess.flush();
    
        Element root = new Element("SUCCESS");
        Document doc = new Document(root);
        root.setAttribute("idDataTrack", dataTrackFolder.getIdDataTrackFolder().toString());
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        out.setOmitEncoding(true);
        this.xmlResult = out.outputString(doc);
        this.setResponsePage(SUCCESS_JSP);

      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in MoveDataTrackFolder ", e);
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