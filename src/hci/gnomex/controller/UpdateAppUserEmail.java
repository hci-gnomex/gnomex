package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.utility.HibernateSession;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

public class UpdateAppUserEmail extends GNomExCommand implements Serializable {
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UpdateAppUserEmail.class);

  private Integer idAppUser;
  private String  email = "";

  public void loadCommand(HttpServletRequest request, HttpSession sess) {
    if (request.getParameter("idAppUser") != null && !request.getParameter("idAppUser").equals("")) {
      idAppUser = Integer.parseInt(request.getParameter("idAppUser"));
    } else {
      this.addInvalidField("No idAppUser", "Please provide an idAppUser to run this command");
    }

    if (request.getParameter("email") != null) {
      email = request.getParameter("email");
    } else {
      this.addInvalidField("No email", "Please provide an email to run this command");
    }

  }


  public Command execute() throws RollBackCommandException {
    try {
      if(this.isValid()) {
        Session sess = HibernateSession.currentSession(this.getUsername());
        AppUser au = (AppUser)sess.load(AppUser.class, idAppUser);
        au.setEmail(email);
        au.setConfirmEmailGuid(null);
        au.setIsActive("Y");
        sess.save(au);
        sess.flush();


        setResponsePage(this.SUCCESS_JSP);

      } else {
        setResponsePage(this.ERROR_JSP);
      }

    }catch(Exception e) {

    }finally {
      try {
        HibernateSession.closeSession();        
      } catch(Exception e) {

      }
    }



    return this;
  }

  public void validate() {

  }

}
