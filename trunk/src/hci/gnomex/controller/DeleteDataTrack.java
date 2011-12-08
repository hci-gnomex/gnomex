package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.PropertyEntry;
import hci.gnomex.model.PropertyEntryValue;
import hci.gnomex.model.UnloadDataTrack;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.jdom.Element;


public class DeleteDataTrack extends GNomExCommand implements Serializable {
  
 
  
  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DeleteDataTrack.class);
  
  
  private Integer      idDataTrack = null;
  
 
  
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
   if (request.getParameter("idDataTrack") != null && !request.getParameter("idDataTrack").equals("")) {
     idDataTrack = new Integer(request.getParameter("idDataTrack"));
   } else {
     this.addInvalidField("idDataTrack", "idDataTrack is required.");
   }

  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    DataTrack dataTrack = null;
    
    try {
      sess = HibernateSession.currentSession(this.getUsername());
      dataTrack = (DataTrack)sess.load(DataTrack.class, idDataTrack);
      
      // Check permissions
      if (this.getSecAdvisor().canDelete(dataTrack)) {
        dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));


        // insert dataTrack reload entry which will cause
        // das/2 type to be unloaded on next 'das2 reload' request
        // Note:  If dataTrack is under more than one folder, there
        // can be multiple das/2 types for one dataTrack.
        for(DataTrackFolder folder : (Set<DataTrackFolder>)dataTrack.getFolders()) {
          String path = folder.getQualifiedTypeName();
          if (path.length() > 0) {
            path += "/";
          }
          String typeName = path + dataTrack.getName();

          UnloadDataTrack unload = new UnloadDataTrack();
          unload.setTypeName(typeName);
          unload.setIdAppUser(this.getSecAdvisor().getIdAppUser());
          unload.setIdGenomeBuild(dataTrack.getIdGenomeBuild());

          sess.save(unload);
        }


        // remove dataTrack files
        // TODO: GenoPub need base directory
        dataTrack.removeFiles("c:/temp/GenoPub/");

        // delete dataTrack property entries
        for(Iterator<?> i = dataTrack.getPropertyEntries().iterator(); i.hasNext();) {
          PropertyEntry ap = PropertyEntry.class.cast(i.next());
          for(Iterator<?> i1 = ap.getValues().iterator(); i1.hasNext();) {
            PropertyEntryValue av = PropertyEntryValue.class.cast(i1.next());
            sess.delete(av);
          }  
        }
        sess.flush();

        // delete dataTrack properties
        for(Iterator<?> i = dataTrack.getPropertyEntries().iterator(); i.hasNext();) {
          PropertyEntry ap = PropertyEntry.class.cast(i.next());
          sess.delete(ap);
        }
        sess.flush();



        // delete database object
        sess.delete(dataTrack);

        sess.flush();
        
        this.xmlResult = "<SUCCESS/>";
        setResponsePage(this.SUCCESS_JSP);
   
      } else {
        this.addInvalidField("insufficient permission", "Insufficient permissions to delete data track.");
        setResponsePage(this.ERROR_JSP);
      }
    } catch (Exception e){
      log.error("An exception has occurred in DeleteDataTrack ", e);
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