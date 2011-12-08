package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DataTrackFolderComparator;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class SaveGenomeBuild extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SaveGenomeBuild.class);
  

  private GenomeBuild                    gbScreen;
  private boolean                        isNewGenomeBuild = false;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    gbScreen = new GenomeBuild();
    HashMap errors = this.loadDetailObject(request, gbScreen);
    this.addInvalidFields(errors);
    if (gbScreen.getIdGenomeBuild() == null || gbScreen.getIdGenomeBuild().intValue() == 0) {
      isNewGenomeBuild = true;
    }
    
    // Das2 name is required
    if (gbScreen.getDas2Name() == null || gbScreen.getDas2Name().equals("")) {
      this.addInvalidField("namer", "DAS2 Name is required");
    }
    
    // Make sure that the DAS2 name has no spaces or special characters
    if (isValid()) {
      if (gbScreen.getDas2Name().indexOf(" ") >= 0) {
        addInvalidField("namespaces", "The genome build DAS2 name cannot have spaces.");
      }
      Pattern pattern = Pattern.compile("\\W");
      Matcher matcher = pattern.matcher(gbScreen.getDas2Name());
      if (matcher.find()) {
        this.addInvalidField("specialc", "The genome build DAS2 name cannot have special characters.");
      }      
    }
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = HibernateSession.currentSession(this.getUsername());
      
      if (this.getSecurityAdvisor().hasPermission(SecurityAdvisor.CAN_SUBMIT_REQUESTS)) {

        
        GenomeBuild gb = null;
              
        if (isNewGenomeBuild) {
          gb = gbScreen;
          
          sess.save(gb);
          sess.flush();

        } else {
          
          gb = (GenomeBuild)sess.load(GenomeBuild.class, gbScreen.getIdGenomeBuild());
          
          initializeGenomeBuild(gb);
          

          sess.flush();
        }
        

        if (isNewGenomeBuild || gb.getRootDataTrackFolder() == null) {
          // Now add a root folder for a new genome build
          DataTrackFolder folder = new DataTrackFolder();
          folder.setName(gb.getDas2Name());
          folder.setIdGenomeBuild(gb.getIdGenomeBuild());
          folder.setIdParentDataTrackFolder(null);
          sess.save(folder);

          Set<DataTrackFolder>  foldersToKeep = new TreeSet<DataTrackFolder>(new DataTrackFolderComparator());
          foldersToKeep.add(folder);
          gb.setDataTrackFolders(foldersToKeep);            
          sess.flush();
        }
                       
        DictionaryHelper.reload(sess);
        
        this.xmlResult = "<SUCCESS idGenomeBuild=\"" + gb.getIdGenomeBuild() + "\"/>";
      
        setResponsePage(this.SUCCESS_JSP);
      } else {
        this.addInvalidField("Insufficient permissions", "Insufficient permission to save genome build.");
        setResponsePage(this.ERROR_JSP);
      }
      
    }catch (Exception e){
      log.error("An exception has occurred in SaveGenomeBuild ", e);
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
  
  private void initializeGenomeBuild(GenomeBuild gb) {
    gb.setGenomeBuildName(gbScreen.getGenomeBuildName());
    gb.setIsActive(gbScreen.getIsActive());
    gb.setIdAppUser(gbScreen.getIdAppUser());
    gb.setDas2Name(gbScreen.getDas2Name());
    gb.setBuildDate(gbScreen.getBuildDate());
    gb.setCoordAuthority(gbScreen.getCoordAuthority());
    gb.setCoordTestRange(gbScreen.getCoordTestRange());
    gb.setCoordURI(gbScreen.getCoordURI());
    gb.setCoordVersion(gbScreen.getCoordVersion());
    gb.setCoordSource(gbScreen.getCoordSource());
    gb.setUcscName(gbScreen.getUcscName());
  }
  

}