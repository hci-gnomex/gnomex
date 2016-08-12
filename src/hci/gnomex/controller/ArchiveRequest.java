package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.BillingItem;
import hci.gnomex.model.BillingStatus;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestStatus;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;

public class ArchiveRequest extends GNomExCommand implements Serializable {

  private static Logger LOG = Logger.getLogger(ArchiveRequest.class);

  private Integer idRequest;
  private String serverName;


  @Override
  public void loadCommand(HttpServletRequest req, HttpSession sess) {
    if(req.getParameter("idRequest") != null && !req.getParameter("idRequest").equals("")){
      this.idRequest = Integer.parseInt(req.getParameter("idRequest"));
    } else{
      this.addInvalidField("Missing idRequest", "idRequest is required");
    }


    serverName = req.getServerName();

  }
  @Override
  public Command execute() throws RollBackCommandException {

    try{
    Session sess = HibernateSession.currentSession(this.getUsername());
    Request reqToArchive = sess.load(Request.class, idRequest);

    if(!reqToArchive.getCodeRequestStatus().equals(RequestStatus.COMPLETED)){
      this.addInvalidField("Request isn't Complete", "Request must be marked as complete to archive.");
    }

    if(!this.getSecAdvisor().canArchive(reqToArchive)){
      this.addInvalidField("Insufficient permissions", "Only admins and lab managers can archive requests.");
    }

    for(Iterator i = reqToArchive.getBillingItems().iterator(); i.hasNext();){
      BillingItem bi = (BillingItem) i.next();
      if(!bi.getCodeBillingStatus().equals(BillingStatus.APPROVED_CC) &&
          !bi.getCodeBillingStatus().equals(BillingStatus.APPROVED) &&
          !bi.getCodeBillingStatus().equals(BillingStatus.APPROVED_PO)){

        this.addInvalidField("All billing items are not approved", "Request must have all billing items approved before archiving.");
        break;
      }
    }

    if(this.isValid()){
      reqToArchive.setArchived("Y");

      //Delete files from filesystem
      String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, reqToArchive.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
      String createYear = formatter.format(reqToArchive.getCreateDate());
      if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
        baseDir += "/";
      }
      String directoryName = baseDir + createYear + "/" + reqToArchive.getNumber().replaceFirst("R+\\d", "R");

      DeleteRequest.removeExperimentFiles(directoryName);

      sess.save(reqToArchive);

      this.setResponsePage(this.SUCCESS_JSP);


    }else{
      this.setResponsePage(this.ERROR_JSP);
    }

    } catch(Exception e){
      LOG.error( "An exception has occurred in ArchiveRequest ", e );
      ;
      throw new RollBackCommandException( e.getMessage() );

    } finally{
      try {
        HibernateSession.closeSession();
      } catch( Exception e ) {

      }
    }
    return this;
  }

  @Override
  public void validate() {
    // TODO Auto-generated method stub

  }

}
