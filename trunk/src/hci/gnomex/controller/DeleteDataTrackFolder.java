package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.UnloadDataTrack;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.jdom.Element;



public class DeleteDataTrackFolder extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteDataTrackFolder.class);
  
  
  private Integer      idDataTrackFolder = null;
  private String       serverName;
  private String       baseDir;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idDataTrackFolder") != null && !request.getParameter("idDataTrackFolder").equals("")) {
     idDataTrackFolder = new Integer(request.getParameter("idDataTrackFolder"));
   } else {
     this.addInvalidField("idDataTrackFolder", "idDataTrackFolder is required.");
   }
   serverName = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrackFolder dataTrackFolder = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      
      baseDir = PropertyDictionaryHelper.getInstance(sess).getDataTrackDirectory(serverName);

      dataTrackFolder = (DataTrackFolder)sess.load(DataTrackFolder.class, idDataTrackFolder);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(dataTrackFolder)) {
       
        List<Object> descendents = new ArrayList<Object>();
        descendents.add(dataTrackFolder);
        dataTrackFolder.recurseGetChildren(descendents);


        // Make sure the user can write this data track folder and all of its
        // descendant annotations and data track folders
        for(Iterator<?> i = descendents.iterator(); i.hasNext();) {
          DetailObject descendent = (DetailObject)i.next();
          if (!this.getSecAdvisor().canDelete(descendent)) {
            if (descendent.equals(dataTrackFolder)) {
              this.addInvalidField("folderp", "Insufficient permision to delete this folder.");    
              break;
            } else if (descendent instanceof DataTrackFolder){
              DataTrackFolder ag = (DataTrackFolder)descendent;
              this.addInvalidField("cfolderp", "Insufficent permission to delete child folder '" + ag.getName() + "'.");
              break;
            } else if (descendent instanceof DataTrack){
              DataTrack a = (DataTrack)descendent;
              this.addInvalidField("dtfolderp", "Insufficent permission to delete child data track '" + a.getName() + "'.");
              break;
            }
          }
        }

        // Make sure we are not trying to delete an data track that also exists in
        // another folder (that will not be deleted.)
        if (this.isValid()) {
          for(Iterator<?> i = descendents.iterator(); i.hasNext();) {
            Object descendent = i.next();
            if (descendent instanceof DataTrack) {
              DataTrack dt = (DataTrack)descendent;
              if (dt.getFolders().size() > 1) {
                for(Iterator<?> i1 = dt.getFolders().iterator(); i1.hasNext();) {
                  DataTrackFolder folder = (DataTrackFolder)i1.next();
                  boolean inDeleteList = false;
                  for(Iterator<?> i2 = descendents.iterator(); i2.hasNext();) {
                    Object d = i2.next();
                    if (d instanceof DataTrackFolder) {
                      DataTrackFolder folderToDelete = (DataTrackFolder)d;
                      if (folderToDelete.getIdDataTrackFolder().equals(folder.getIdDataTrackFolder())) {
                        inDeleteList = true;
                        break;
                      }
                    }
                  }
                  if (!inDeleteList) {
                    this.addInvalidField("deletefolderp", "Cannot remove contents of folder '" + folder.getName() + 
                        "' because data track '" + dt.getName() + 
                        "' exists in folder '" + 
                        folder.getName() + 
                    "'.  Please remove this data track first.");
                    break;
                  }
                }
              }
            }
          }
          
        }

        // Now delete all of the contents of the data track folder and then the
        // data track folder itself.  By traversing the list from the
        // in reverse, we are sure to delete the children before the parent
        // folder.
        if (this.isValid()) {
          for(int i = descendents.size() - 1; i >= 0; i--) {
            Object descendent = descendents.get(i);

            // Remove data track file(s)
            if (descendent instanceof DataTrack) {
              DataTrack dt = (DataTrack)descendent;            
              dt.removeFiles(baseDir);  
              
              // insert dataTrack reload entry which will cause
              // das/2 type to be unloaded on next 'das2 reload' request
              // Note:  If dataTrack is under more than one folder, there
              // can be multiple das/2 types for one dataTrack.
              for(DataTrackFolder folder : (Set<DataTrackFolder>)dt.getFolders()) {
                String path = folder.getQualifiedTypeName();
                if (path.length() > 0) {
                  path += "/";
                }
                String typeName = path + dt.getName();

                UnloadDataTrack unload = new UnloadDataTrack();
                unload.setTypeName(typeName);
                unload.setIdAppUser(this.getSecAdvisor().getIdAppUser());
                unload.setIdGenomeBuild(dt.getIdGenomeBuild());

                sess.save(unload);
              }

            } 

            // Delete the object from db
            sess.delete(descendent);          
          }
          
        }


        if (this.isValid()) {
          sess.flush();
          this.xmlResult = "<SUCCESS/>";
          setResponsePage(this.SUCCESS_JSP);
          
        } else {
          setResponsePage(this.ERROR_JSP);
        }
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete data track folder.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in DeleteDataTrackFolder ", e);
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