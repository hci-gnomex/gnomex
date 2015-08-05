package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.AppUser;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.MailUtilHelper;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

public class ApproveUser extends HttpServlet {

  private String guid = "";
  private String idAppUser = "";
  private AppUser au;
  private String message = "";
  private Boolean deleteUser = false;
  private String serverName;

  protected void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
	serverName = req.getServerName();
    doPost(req, res);
  }


  protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
    try {
      Session sess =  HibernateSession.currentSession("approveUserServlet");
      guid = ( String ) ( ( request.getParameter( "guid" ) != null ) ? request.getParameter( "guid" ) : "" );
      idAppUser = ( String ) ( ( request.getParameter( "idAppUser" ) != null ) ? request.getParameter( "idAppUser" ) : "" );

      deleteUser = false;
      if(request.getParameter("deleteUser") != null && !request.getParameter("deleteUser").equals("") && request.getParameter("deleteUser").equals("Y")) {
        deleteUser = true;
      }

      PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
      DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(sess);
      String doNotReplyEmail = pdh.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);

      au = ( AppUser ) sess.createQuery( "from AppUser au where au.idAppUser = '" + idAppUser + "'" ).uniqueResult();

      if( au == null ) {
        message = "This user does not exist.";
      } else if(au.getIsActive().equals("Y")) {
        message = "This user has already been activated.  Thanks.";
      } else if(au.getIsActive().equals("N") && deleteUser) {
        String email = au.getEmail();
        sess.delete(au);
        sess.flush();
        
        MailUtilHelper helper = new MailUtilHelper(
        		email,
        		doNotReplyEmail,
        		"Your GNomEx account has NOT been approved.",
        		"You have not been given access to GNomEx. Please contact the P.I. of the lab you were requesting access to for the reason behind this.  If you requested the creation of a new lab, please contact the core facility director whose core you were trying to join.  Thank you.",
        		null,
				true, 
				dictionaryHelper,
				serverName);
        helper.setRecipientAppUser(au);
        MailUtil.validateAndSendEmail(helper);

        message = "The user has been successfully deleted.  The user has been notified of this action.";


      } else if(au.getGuid() != null && au.getGuid().equals(guid) && au.getGuidExpiration() != null && au.getGuidExpiration().after(new Date(System.currentTimeMillis())) ) { //guid matches
        au.setIsActive("Y");
        au.setGuid(null);
        au.setGuidExpiration(null);
        sess.save(au);
        sess.flush();

        String url = request.getRequestURL().substring(0, request.getRequestURL().indexOf("ApproveUser.gx"));
        String gnomexURL =  "<a href='" + url + "'>Click here</a> to login.";

        String body = "Welcome to GNomEx.  Your user account has been activated.<br><br>" + gnomexURL + "<br><br> **Please note if you requested the creation of a new lab please wait until you receive notification that you have been added to that lab before trying to submit an experiment.**";
        
        MailUtilHelper helper = new MailUtilHelper(
        		au.getEmail(),
        		doNotReplyEmail,
        		"Your GNomEx account is now active",
        		body,
        		null,
				true, 
				dictionaryHelper,
				serverName);
        helper.setRecipientAppUser(au);
        MailUtil.validateAndSendEmail(helper);

        message = "User successfully activated.  The user will be notified that their account is now active";
      } else {
        message = "The link you clicked on has expired.  You will have to activate the user through the GNomEx app.";
        au.setGuid(null);
        au.setGuidExpiration(null);
      }


    } catch (Exception e) {
      message = "There was an issue activating the user.  Please activate through the GNomEx app and contact GNomEx support.  Thanks.";
      e.printStackTrace();
    } finally {
      try {
        HibernateSession.closeSession();
        HibernateSession.closeTomcatSession();
        String url= "/approve_user.jsp"; //relative url for display jsp page
        ServletContext sc = getServletContext();
        RequestDispatcher rd = sc.getRequestDispatcher(url);
        request.setAttribute("message",message);

        rd.forward(request, response);
      } catch (Exception e1) {
        System.out.println("ApproveUser warning - cannot close hibernate session");
      }
    }
  }

}
