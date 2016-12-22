package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.AppUser;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.apache.log4j.Logger;
public class UpdateAppUserEmail extends GNomExCommand implements Serializable {
  private static Logger LOG = Logger.getLogger(UpdateAppUserEmail.class);

  private Integer idAppUser;
  private String email = "";

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
      if (this.isValid()) {
        Session sess = HibernateSession.currentSession(this.getUsername());
        AppUser au = sess.load(AppUser.class, idAppUser);
        Integer idUser = getIdAppUserFromEmail(sess, email);
        Boolean dupEmail = false;
        Boolean badEmail = false;
        String outMsg = "";

        if (idUser != -1 && !idUser.equals(au.getIdAppUser())) {
          dupEmail = true;
        } else if (!MailUtil.isValidEmail(email)) {
          badEmail = true;
        } else {
          au.setEmail(email);
          au.setConfirmEmailGuid(null);
          au.setIsActive("Y");
          sess.save(au);
          sess.flush();
        }

        if (badEmail) {
          outMsg = "The email address " + email + " is not formatted properly.";
          this.xmlResult = "<ERROR message=\"" + outMsg + "\"/>";
        } else if (dupEmail) {
          outMsg = "The email address " + email + " is already in use.";
          this.xmlResult = "<ERROR message=\"" + outMsg + "\"/>";
        }

        setResponsePage(this.SUCCESS_JSP);

      } else {
        setResponsePage(this.ERROR_JSP);
      }

    } catch (Exception e) {
      LOG.error("An exception has occurred in UpdateAppUserEmail ", e);
    } finally {
      try {
        //closeHibernateSession;
      } catch (Exception e) {
        LOG.error("An exception has occurred in UpdateAppUserEmail ", e);
      }
    }

    return this;
  }

  public void validate() {

  }

  private Integer getIdAppUserFromEmail(Session sess, String email) {

    StringBuffer buf = new StringBuffer();
    buf.append("SELECT a.idAppUser from AppUser as a where a.email = :email");

    Query usersQuery = sess.createQuery(buf.toString());

    usersQuery.setParameter("email", email);

    List users = usersQuery.list();
    if (users.size() == 1) {
      return (Integer) users.get(0);
    } else {
      return -1;
    }
  }

}
