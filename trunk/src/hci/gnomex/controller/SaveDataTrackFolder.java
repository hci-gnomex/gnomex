package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.RequestParser;

import java.io.Serializable;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;





public class SaveDataTrackFolder extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveDataTrackFolder.class);
  
  private DataTrackFolder    load;
  private Integer            idParentDataTrackFolder;
  private DataTrackFolder    dataTrackFolder;
  private boolean            isNewDataTrackFolder = false;
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    load = new DataTrackFolder();
    HashMap errors = this.loadDetailObject(request, load);
    this.addInvalidFields(errors);

    if (request.getParameter("idParentDataTrackFolder") != null && !request.getParameter("idParentDataTrackFolder").equals("")) {
      idParentDataTrackFolder = Integer.valueOf(request.getParameter("idParentDataTrackFolder"));
    }

    // Make sure that name doesn't have forward slashes (/).
    if (load.getName().contains("/") || load.getName().contains("&")) {
      this.addInvalidField("namechar", "The folder name cannnot contain any characters / or &.");
    }

    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      this.initializeDataTrackFolder(load, sess);
      
      
      if (this.getSecAdvisor().canUpdate(dataTrackFolder)) {

        this.dataTrackFolder.setName(RequestParser.unEscape(dataTrackFolder.getName()));
        this.dataTrackFolder.setDescription(RequestParser.unEscapeBasic(dataTrackFolder.getDescription()));
        
        sess.save(dataTrackFolder);
        sess.flush();

        this.xmlResult = "<SUCCESS idDataTrackFolder=\"" + dataTrackFolder.getIdDataTrackFolder() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save data track folder.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveDataTrackFolder ", e);
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
  
  private void initializeDataTrackFolder(DataTrackFolder load, Session sess) throws Exception {
    
    if (load.getIdDataTrackFolder() == null || load.getIdDataTrackFolder().intValue() == 0) {
      dataTrackFolder = load;
      dataTrackFolder.setCreatedBy(this.getSecAdvisor().getUID());
      dataTrackFolder.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
      isNewDataTrackFolder = true;
      
      
    } else {
      dataTrackFolder = (DataTrackFolder)sess.load(DataTrackFolder.class, load.getIdDataTrackFolder());
      
    }
    
    dataTrackFolder.setName(RequestParser.unEscape(load.getName()));
    dataTrackFolder.setDescription(RequestParser.unEscapeBasic(load.getDescription()));
    dataTrackFolder.setIdLab(load.getIdLab());

    // If this is a root annotation grouping, find the default root annotation
    // grouping for the genome version.
    if (this.isNewDataTrackFolder) {
      DataTrackFolder parentDataTrackFolder = null;
      if (idParentDataTrackFolder == null) {
        GenomeBuild gb = GenomeBuild.class.cast(sess.load(GenomeBuild.class, load.getIdGenomeBuild()));
        parentDataTrackFolder = gb.getRootDataTrackFolder();
        if (parentDataTrackFolder == null) {
          throw new Exception("Cannot find root data track folder for " + gb.getGenomeBuildName());
        }
        // If parent data track folder is owned by a user group, this
        // child data track  folder must be as well.
        if (parentDataTrackFolder.getIdLab() != null) {

          if ( load.getIdLab() == null ||
              !parentDataTrackFolder.getIdLab().equals(load.getIdLab())) {
            throw new Exception("Folder '" + load.getName() + "' must belong to lab '" + 
                DictionaryHelper.getInstance(sess).getLabObject(parentDataTrackFolder.getIdLab()).getName() + "'");

          }
        } 
        load.setIdParentDataTrackFolder(parentDataTrackFolder.getIdDataTrackFolder()); 
      }
    }
    
    
    // If parent annotation grouping is owned by a user group, this
    // child annotation grouping must be as well.
    if (!isNewDataTrackFolder) {
      if (dataTrackFolder.getParentFolder() != null &&
          dataTrackFolder.getParentFolder().getIdLab() != null) {

        if (load.getIdLab() == null ||
            !dataTrackFolder.getParentFolder().getIdLab().equals(load.getIdLab())) {
          throw new Exception("Folder '" + load.getName() + "' must belong to lab '" + 
              DictionaryHelper.getInstance(sess).getLabObject(dataTrackFolder.getParentFolder().getIdLab()).getName() + "'");
        }
      } 
      
    }

  }  
  
 
  
  

}