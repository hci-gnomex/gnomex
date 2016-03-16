package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

public class ApproveLabMembership extends HttpServlet {

  private String idLab = "";
  private String idAppUser = "";
  private String guid = "";
  private AppUser au;
  private Lab lab;
  private String message = "";
  private String serverName;
  private Boolean denyRequest = false;

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    serverName = req.getServerName();
    doPost(req, res);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      Session sess = HibernateSession.currentSession("approveLabMembershipServlet");
      idAppUser = (request.getParameter("idAppUser") != null) ? request.getParameter("idAppUser") : "";
      idLab = (request.getParameter("idLab") != null) ? request.getParameter("idLab") : "";
      guid = (request.getParameter("guid") != null) ? request.getParameter("guid") : "";

      denyRequest = false;
      if (request.getParameter("denyRequest") != null && !request.getParameter("denyRequest").equals("") && request.getParameter("denyRequest").equals("Y")) {
        denyRequest = true;
      }

      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
      String doNotReplyEmail = pdh.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
      Boolean alreadyMember = false;

      au = (AppUser) sess.createQuery("from AppUser au where au.idAppUser = '" + idAppUser + "' and au.guid='" + guid + "'").uniqueResult();
      lab = (Lab) sess.createQuery("from Lab l where l.idLab = '" + idLab + "'").uniqueResult();

      Set<Lab> labs = new HashSet();

      if (au != null) {
        labs = au.getLabs();

        for (Iterator i = labs.iterator(); i.hasNext();) {
          Lab l = (Lab) i.next();
          if (l.getIdLab() == Integer.parseInt(this.idLab)) {
            alreadyMember = true;
            break;
          }
        }
      }

      if (au == null) {
        message = "This user does not exist or the guid provided is incorrect.";
      } else if (au.getGuidExpiration().before(new Date(System.currentTimeMillis()))) {
        message = "The link has expired";
      } else if (alreadyMember) {
        message = "The user is already a member of this lab.  Thanks.";
      } else if (denyRequest) {
        String email = au.getEmail();

        MailUtilHelper helper = new MailUtilHelper(email, doNotReplyEmail, "Your GNomEx lab membership request was denied", "You have not been given access to the " + lab.getName(true, true) + ". Please contact the P.I. of the lab you were requesting access to for the reason behind this.  Thank you.", null, true, dictionaryHelper, serverName);
        helper.setRecipientAppUser(au);
        MailUtil.validateAndSendEmail(helper);

        message = "The user was denied membership to your lab.  The user has been notified of this action.";

      } else {

        labs.add(lab);
        au.setLabs(labs);

        String url = request.getRequestURL().substring(0, request.getRequestURL().indexOf("ApproveLabMembership.gx"));
        String gnomexURL = "<a href='" + url + "'>Click here</a> to login.";

        String body = "Your request to join the " + lab.getName(true, true) + " was approved. <br><br>" + gnomexURL + "<br><br>";

        MailUtilHelper helper = new MailUtilHelper(au.getEmail(), doNotReplyEmail, "Your GNomEx lab membership request was approved.", body, null, true, dictionaryHelper, serverName);
        helper.setRecipientAppUser(au);
        MailUtil.validateAndSendEmail(helper);

        message = "User successfully added as a member to your lab.  The user will be notified that they are now a member of your lab.";
      }

      sess.save(au);
      sess.flush();

    } catch (Exception e) {
      message = "There was an issue adding the user to your lab.  Please add the user to your lab through the GNomEx app and contact GNomEx support.  Thanks.";
      e.printStackTrace();
    } finally {
      try {
        HibernateSession.closeSession();
        HibernateSession.closeTomcatSession();
        String url = "/approve_user.jsp"; // relative url for display jsp page
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        request.setAttribute("message", message);

        rd.forward(request, response);
      } catch (Exception e1) {
        System.out.println("ApproveLabMembership warning - cannot close hibernate session");
      }
    }
  }

}
