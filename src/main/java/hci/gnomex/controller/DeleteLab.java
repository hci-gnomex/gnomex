package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.Lab;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.GNomExRollbackException;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.apache.log4j.Logger;




public class DeleteLab extends GNomExCommand implements Serializable {



  // the static field for logging in Log4J
  private static Logger LOG = Logger.getLogger(DeleteLab.class);


  private Integer      idLab = null;




  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

   if (request.getParameter("idLab") != null && !request.getParameter("idLab").equals("")) {
     idLab = new Integer(request.getParameter("idLab"));
   } else {
     this.addInvalidField("idLab", "idLab is required.");
   }

  }

    public Command execute() throws RollBackCommandException {
      try {
          Session sess = HibernateSession.currentSession(this.getUsername());

          // Check permissions
          if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ADMINISTER_USERS)) {
              Lab lab = (Lab)sess.load(Lab.class, idLab);

              //
              // Clear out lab member list
              //

              // Error handling to make sure lab has no other tables associated with it before deleting
              List errorMessage = new ArrayList<String>();
              boolean analysis = lab.hasAnalysis(sess,idLab);
              boolean dataTrack = lab.hasDataTracks(sess,idLab);
              boolean experiment = lab.hasExperiments(sess,idLab);
              boolean topic = lab.hasTopics(sess,idLab);

              if(analysis){
                  errorMessage.add("Analysis");
              }
              if(dataTrack ){
                  errorMessage.add("Data Tracks");
              }

              if(experiment){
                  errorMessage.add("Experiments");
              }
              if(topic){
                 errorMessage.add("Topics");
              }

              if(errorMessage.size() > 0){
                  StringBuilder frmtErrorMessage = new StringBuilder();
                  frmtErrorMessage.append(" The lab still has these items attached: ");

                  for(int i = 0; i < errorMessage.size(); i++ ){
                      if(i == errorMessage.size() - 1){
                          frmtErrorMessage.append(errorMessage.get(i) + ".");
                      }
                      else {
                          frmtErrorMessage.append(errorMessage.get(i) + ", " );
                      }
                  }
                  throw new GNomExRollbackException("Error in delete lab",true,frmtErrorMessage.toString());
              }

              lab.setMembers(new TreeSet());
              sess.flush();

              //
              // Delete lab
              //

              sess.delete(lab);
              sess.flush();

              this.xmlResult = "<SUCCESS/>";
              setResponsePage(this.SUCCESS_JSP);

          } else {
              this.addInvalidField("insufficient permission", "Insufficient permissions to delete lab.");
              setResponsePage(this.ERROR_JSP);
          }
      }catch(GNomExRollbackException e){
        this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in DeleteLab ", e);
        throw e;
      }catch (Exception e){
        this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in DeleteLab ", e);
        throw new RollBackCommandException(e.getMessage());

    }
    return this;
  }






}