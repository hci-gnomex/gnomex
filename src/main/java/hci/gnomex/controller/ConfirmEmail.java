package hci.gnomex.controller;

import hci.gnomex.model.AppUser;
import hci.gnomex.utility.HibernateSession;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class ConfirmEmail extends HttpServlet {

private static Logger LOG = Logger.getLogger(ConfirmEmail.class);
private String guid = "";
private String idAppUser = "";
private String message = "";

protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	doPost(req, res);
}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	try {

		Session sess = HibernateSession.currentSession("confirmEmailServlet");
		guid = (String) ((request.getParameter("guid") != null) ? request.getParameter("guid") : "");
		idAppUser = (String) ((request.getParameter("idAppUser") != null) ? request.getParameter("idAppUser") : "");

		AppUser au = (AppUser) sess.createQuery("Select au from AppUser au where au.idAppUser = '" + idAppUser + "'")
				.uniqueResult();

		message = "";

		if (au == null) {
			message = "This user does not exist.";
		} else if (au.getConfirmEmailGuid() != null && au.getConfirmEmailGuid().equals(guid)) { // guid matches
			au.setConfirmEmailGuid(null);
			sess.save(au);
			message = "Thanks for verifying your email address!";
		} else if (au.getConfirmEmailGuid() == null && au.getEmail() != null) {
			message = "Your email has already been verified.  Thanks!";
		} else {
			message = "The link has expired.  Your email address has been removed from Gnomex.  Please update your account with your preferred contact email address at your earliest convenience.  Thanks!";
		}

		sess.flush();

	} catch (Exception e) {
		message = "There was an issue verifying your email address.  Please contact GNomEx support.  Thanks!";
		LOG.error(message, e);
	} finally {
		try {
			HibernateSession.closeSession();
			String url = "/confirm_email.jsp"; // relative url for display jsp page
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			request.setAttribute("message", message);
			rd.forward(request, response);
		} catch (Exception e) {
			LOG.error("ConfirmEmail warning - cannot close hibernate session", e);
		}

	}

}
}
