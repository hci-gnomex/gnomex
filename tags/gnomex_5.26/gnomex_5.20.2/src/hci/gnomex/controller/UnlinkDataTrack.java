package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.UnloadDataTrack;
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



public class UnlinkDataTrack extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UnlinkDataTrack.class);
  
  
  private Integer idDataTrack = null;
  private Integer idGenomeBuild = null;
  private Integer idDataTrackFolder = null;
 
  
  
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
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrack dataTrack = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      dataTrack = (DataTrack)sess.load(DataTrack.class, idDataTrack);
      


      dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));
      GenomeBuild genomeBuild = GenomeBuild.class.cast(sess.load(GenomeBuild.class, idGenomeBuild));

      // Make sure the user can write this dataTrack 
      if (this.getSecAdvisor().canUpdate(dataTrack)) {


        // Get the dataTrack grouping this dataTrack should be removed from.
        DataTrackFolder dataTrackGrouping = null;
        if (idDataTrackFolder == null) {
          // If this is a root dataTrack, find the default root dataTrack
          // grouping for the genome build.
          dataTrackGrouping = genomeBuild.getRootDataTrackFolder();
          if (dataTrackGrouping == null) {
            throw new Exception("Cannot find root dataTrack grouping for " + genomeBuild.getGenomeBuildName());
          }
        } else {
          // Otherwise, find the dataTrack grouping passed in as a request parameter.
          dataTrackGrouping = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
        }

        // Create a pending unload of the dataTrack
        String typeName = dataTrackGrouping.getQualifiedTypeName() + "/" + dataTrack.getName();
        UnloadDataTrack unload = new UnloadDataTrack();
        unload.setTypeName(typeName);
        unload.setIdAppUser(this.getSecAdvisor().getIdAppUser());
        unload.setIdGenomeBuild(dataTrack.getIdGenomeBuild());
        sess.save(unload);


        // Remove the dataTrack grouping the dataTrack was in
        // by adding back the dataTracks to the dataTrack grouping, 
        // excluding the dataTrack to be removed
        Set<DataTrack> dataTracksToKeep = new TreeSet<DataTrack>(new DataTrackComparator());
        for(Iterator<?>i = dataTrackGrouping.getDataTracks().iterator(); i.hasNext();) {
          DataTrack a = DataTrack.class.cast(i.next());
          if (a.getIdDataTrack().equals(dataTrack.getIdDataTrack())) {
            continue;
          }
          dataTracksToKeep.add(a);

        }
        dataTrackGrouping.setDataTracks(dataTracksToKeep);

        sess.flush();

        // Send back XML attributes showing remaining references to dataTrack groupings
        sess.refresh(dataTrack);
        StringBuffer remainingDataTrackFolders = new StringBuffer();
        int folderCount = 0;
        for (Iterator<?> i1 = dataTrack.getFolders().iterator(); i1.hasNext();) {
          DataTrackFolder folder = DataTrackFolder.class.cast(i1.next());
          if (remainingDataTrackFolders.length() > 0) {
            remainingDataTrackFolders.append(",\n");         
          }
          remainingDataTrackFolders.append("    '" + folder.getName() + "'");
          folderCount++;

        }

        Element root = new Element("SUCCESS");
        Document doc = new Document(root);
        root.setAttribute("idDataTrack", dataTrack.getIdDataTrack().toString());
        root.setAttribute("name", dataTrack.getName());
        root.setAttribute("numberRemainingDataTrackFolders", Integer.valueOf(folderCount).toString());
        root.setAttribute("remainingDataTrackFolders", remainingDataTrackFolders.toString());
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        out.setOmitEncoding(true);
        this.xmlResult = out.outputString(doc);
        this.setResponsePage(SUCCESS_JSP);

      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to unlink data track.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in UnlinkDataTrack ", e);
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