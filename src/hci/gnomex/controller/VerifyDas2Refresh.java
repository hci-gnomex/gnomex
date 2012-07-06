package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.model.Organism;
import hci.gnomex.model.Segment;
import hci.gnomex.model.UnloadDataTrack;
import hci.gnomex.utility.DataTrackQuery;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.QualifiedDataTrack;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.hibernate.Session;


public class VerifyDas2Refresh extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VerifyDas2Refresh.class);
  
  private String serverName;
  private String analysisBaseDir;
  private String dataTrackBaseDir;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    StringBuffer invalidGenomeBuilds = new StringBuffer();
    StringBuffer emptyDataTracks = new StringBuffer();
    int loadCount = 0;
    int unloadCount = 0;
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      
      analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getAnalysisDirectory(serverName);
      dataTrackBaseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackDirectory(serverName);
      
      
      DataTrackQuery DataTrackQuery = new DataTrackQuery();
      DataTrackQuery.runDataTrackQuery(sess, this.getSecAdvisor(), true);
      for (Organism organism : DataTrackQuery.getOrganisms()) {
        for (String GenomeBuildName : DataTrackQuery.getGenomeBuildNames(organism)) {

          GenomeBuild gv = DataTrackQuery.getGenomeBuild(GenomeBuildName);

          List<Segment> segments = DataTrackQuery.getSegments(organism, GenomeBuildName);  
          // Make sure that genome versions with DataTracks or sequence have at least
          // one segment.
          if (DataTrackQuery.getQualifiedDataTracks(organism, GenomeBuildName).size() > 0 || gv.hasSequence(dataTrackBaseDir)) {
            if (segments == null || segments.size() == 0) {
              if (invalidGenomeBuilds.length() > 0) {
                invalidGenomeBuilds.append(", ");
              }
              invalidGenomeBuilds.append(GenomeBuildName);
            }
          }
          // Keep track of how many DataTracks have missing files
          for(Iterator i = DataTrackQuery.getQualifiedDataTracks(organism, GenomeBuildName).iterator(); i.hasNext();) {
            QualifiedDataTrack qa = (QualifiedDataTrack)i.next();

            if (qa.getDataTrack().getFileCount(dataTrackBaseDir, analysisBaseDir) == 0) {
              if (emptyDataTracks.length() > 0) {
                emptyDataTracks.append("\n");
              }
              emptyDataTracks.append(gv.getDas2Name() + ":  ");
              break;
            }
          }
          boolean firstAnnot = true;
          for(Iterator i = DataTrackQuery.getQualifiedDataTracks(organism, GenomeBuildName).iterator(); i.hasNext();) {
            QualifiedDataTrack qa = (QualifiedDataTrack)i.next();
            if (qa.getDataTrack().getFileCount(dataTrackBaseDir, analysisBaseDir) == 0) {
              if (firstAnnot) {
                firstAnnot = false;
              } else {
                if (emptyDataTracks.length() > 0) {
                  emptyDataTracks.append(", ");
                }               
              }
              emptyDataTracks.append(qa.getDataTrack().getName());
            } else {
              loadCount++; 
            }
          }
          List<UnloadDataTrack> unloadDataTracks = DataTrackQuery.getUnloadedDataTracks(sess, this.getSecAdvisor(), gv);
          unloadCount = unloadCount + unloadDataTracks.size();

        }
      }


      StringBuffer confirmMessage = new StringBuffer();

      if (loadCount > 0 || unloadCount > 0) {
        if (loadCount > 0) {
          confirmMessage.append(loadCount + " DataTrack(s) and ready to load to DAS/2.\n\n");
        }
        if (unloadCount > 0) {
          confirmMessage.append(unloadCount + " DataTrack(s) ready to unload from DAS/2.\n\n");
        } 
        confirmMessage.append("Do you wish to continue?\n\n");          
      } else {
        confirmMessage.append("No DataTracks are queued for reload.  Do you wish to continue?\n\n");
      }

      StringBuffer message = new StringBuffer();
      if (invalidGenomeBuilds.length() > 0 || emptyDataTracks.length() > 0) {

        if (invalidGenomeBuilds.length() > 0) {
          message.append("DataTracks and sequence for the following genome versions will be bypassed due to missing segment information:\n" + 
              invalidGenomeBuilds.toString() +  
          ".\n\n");     
        }
        if (emptyDataTracks.length() > 0) {
          message.append("The following empty DataTracks will be bypassed:\n" + 
              emptyDataTracks.toString() +  
          ".\n\n");     
        }
        message.append(confirmMessage.toString());
        this.addInvalidField("invalid", message.toString());
        setResponsePage(this.ERROR_JSP);

      } 
      this.xmlResult = "<SUCCESS message=\"" + confirmMessage.toString() + "\"/>";
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in VerifyDas2Refresh ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in VerifyDas2Refresh ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in VerifyDas2Refresh ", e);
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
}