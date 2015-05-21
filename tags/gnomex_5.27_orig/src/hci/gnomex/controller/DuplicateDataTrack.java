package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.PropertyOption;
import hci.gnomex.utility.AppUserComparator;
import hci.gnomex.utility.DataTrackComparator;
import hci.gnomex.utility.DataTrackFolderComparator;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyOptionComparator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;




public class DuplicateDataTrack extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DuplicateDataTrack.class);
  
  
  private Integer idDataTrack = null;
  private Integer idDataTrackFolder = null;


  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
     idDataTrack = new Integer(request.getParameter("idDataTrack"));
   } else {
     this.addInvalidField("idDataTrack", "idDataTrack is required.");
   }
   if (request.getParameter("idDataTrackFolder") != null && !request.getParameter("idDataTrackFolder").equals("")) {
     idDataTrackFolder = new Integer(request.getParameter("idDataTrackFolder"));
   } else {
     this.addInvalidField("idDataTrackFolder", "idDataTrackFolder is required.");
   }
  
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrack sourceDataTrack  = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      sourceDataTrack = (DataTrack)sess.load(DataTrack.class, idDataTrack);

      
      // Make sure the user can write this dataTrack 
      if (this.getSecAdvisor().canUpdate(sourceDataTrack)) {
        DataTrack dup = new DataTrack();

        dup.setName(sourceDataTrack.getName() + "_copy");
        dup.setDescription(sourceDataTrack.getDescription());
        dup.setSummary(sourceDataTrack.getSummary());
        dup.setCodeVisibility(sourceDataTrack.getCodeVisibility());
        dup.setIdLab(sourceDataTrack.getIdLab());
        dup.setIdAppUser(sourceDataTrack.getIdAppUser());
        dup.setIdGenomeBuild(sourceDataTrack.getIdGenomeBuild());
        dup.setIsLoaded("N");
        dup.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
        dup.setCreatedBy(this.getSecAdvisor().getUID());

        //save DataTrack so that it's assigned an ID
        sess.save(dup);

        //add dataTrack properties     
        Set<PropertyEntry> clonesPESet = new HashSet<PropertyEntry>(); 

        //for each PropertyEntry in the source DataTrack
        for(Iterator<?> i = sourceDataTrack.getPropertyEntries().iterator(); i.hasNext();) {

          //get the PropertyEntry
          PropertyEntry sourcePE = (PropertyEntry)i.next();

          //make clone and copy over params from source
          PropertyEntry clonesPE = new PropertyEntry();
          clonesPE.setIdProperty( sourcePE.getIdProperty());
          clonesPE.setValue( sourcePE.getValue());
          clonesPE.setIdDataTrack(dup.getIdDataTrack());

          //save it and flush it to assign the DB id
          sess.save(clonesPE);
          sess.flush();

          //add to set
          clonesPESet.add(clonesPE);

          Set<PropertyEntryValue> clonesPEV = new HashSet<PropertyEntryValue>();

          //for each PropertyEntryValue in the sourcePE
          for (Iterator iX = sourcePE.getValues().iterator(); iX.hasNext();) {

            PropertyEntryValue sourceAV = (PropertyEntryValue)iX.next();

            //make clone and copy over params from source
            PropertyEntryValue clonedAV = new PropertyEntryValue();
            clonedAV.setIdPropertyEntry(clonesPE.getIdPropertyEntry());
            clonedAV.setValue(sourceAV.getValue());

            //save it to DB
            sess.save(clonedAV);

            //add to set
            clonesPEV.add(clonedAV);
          }

          //add set to AP
          clonesPE.setValues(clonesPEV);

          TreeSet<PropertyOption> clonedOptions = new TreeSet<PropertyOption>(new PropertyOptionComparator());

          //for each PropertyOption in the sourcePE
          //don't understand how this will work!
          for (Iterator<?> iY = sourcePE.getOptions().iterator(); iY.hasNext();) {
            PropertyOption sourceOption = (PropertyOption)iY.next();
            clonedOptions.add(sourceOption);    
          }

          //add set to AP
          clonesPE.setOptions(clonedOptions);
        }

        //add Set of DataTrackPropery to cloned DataTrack
        dup.setPropertyEntries(clonesPESet);

        //colaborators 
        TreeSet<AppUser> collaborators = new TreeSet<AppUser>(new AppUserComparator());
        Iterator<?> cIt = sourceDataTrack.getCollaborators().iterator();
        while (cIt.hasNext()) collaborators.add((AppUser)cIt.next());
        dup.setCollaborators(collaborators);

        //folders - only add to the folder that the source data track was selected from.  in other
        // words, if the source folder is under more that one folder, we don't want to duplicate
        // to all of the folders, only the one that the user clicked under.
        Set<DataTrackFolder>  dataTrackFolders= new TreeSet<DataTrackFolder>(new DataTrackFolderComparator());
        DataTrackFolder dataTrackFolder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
        dataTrackFolders.add(dataTrackFolder);
        dup.setFolders(dataTrackFolders);

        sess.save(dup);
        sess.flush();

        // Get the dataTrack folder this dataTrack is in.
        DataTrackFolder folder = null;
        if (idDataTrackFolder == null) {
          // If this is a root dataTrack, find the default root dataTrack
          // folder for the genome build.
          GenomeBuild gb = GenomeBuild.class.cast(sess.load(GenomeBuild.class, sourceDataTrack.getIdGenomeBuild()));
          folder = gb.getRootDataTrackFolder();
          if (folder == null) {
            throw new Exception("Cannot find root dataTrack folder for " + gb.getGenomeBuildName());
          }
        } else {        
          // Otherwise, find the dataTrack folder passed in as a request parameter.
          folder = DataTrackFolder.class.cast(sess.load(DataTrackFolder.class, idDataTrackFolder));
        }

        // Add the dataTrack to the  folder
        Set<DataTrack> newDataTracks = new TreeSet<DataTrack>(new DataTrackComparator());
        for(Iterator<?> i = folder.getDataTracks().iterator(); i.hasNext();) { 
          DataTrack a = DataTrack.class.cast(i.next());
          newDataTracks.add(a);
        }
        newDataTracks.add(dup);
        folder.setDataTracks(newDataTracks);

        // Assign a file directory name
        dup.setFileName("DT" + dup.getIdDataTrack());
        
        sess.flush();
        
        Element root = new Element("SUCCESS");
        Document doc = new Document(root);
        root.setAttribute("idDataTrack", dup.getIdDataTrack().toString());
        root.setAttribute("idDataTrackFolder", folder != null ? folder.getIdDataTrackFolder().toString() : "");
        XMLOutputter out = new org.jdom.output.XMLOutputter();
        out.setOmitEncoding(true);
        this.xmlResult = out.outputString(doc);
        this.setResponsePage(SUCCESS_JSP);

        
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in DuplicateDataTrack ", e);
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