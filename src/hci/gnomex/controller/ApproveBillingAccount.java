package hci.gnomex.controller;

import hci.gnomex.model.BillingAccount;
import hci.gnomex.model.Lab;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.BillingAccountUtil;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class ApproveBillingAccount extends HttpServlet {

private static Logger LOG = Logger.getLogger(SaveLab.class);
private String idBillingAccount = "";
private BillingAccount ba;
private String message = "";
private String serverName = "";
private String launchAppURL = "";
private String approverEmail = "";

protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	doPost(req, res);
}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	try {
		Session sess = HibernateSession.currentSession("approveBillingAccountServlet");
		idBillingAccount = (String) ((request.getParameter("idBillingAccount") != null) ? request
				.getParameter("idBillingAccount") : "");
		approverEmail = (String) ((request.getParameter("approverEmail") != null) ? request
				.getParameter("approverEmail") : "");

		PropertyDictionaryHelper pdh = PropertyDictionaryHelper.getInstance(sess);
		String doNotReplyEmail = pdh.getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);
		serverName = request.getServerName();

		ba = (BillingAccount) sess.createQuery(
				"from BillingAccount ba where ba.idBillingAccount = '" + idBillingAccount + "'").uniqueResult();

		if (ba == null) {
			message = "This billing account does not exist";
		} else if (ba.getIsApproved() != null && ba.getIsApproved().equals("Y")) {
			message = "This billing account has already been approved";
		} else {
			Lab lab = (Lab) sess.load(Lab.class, ba.getIdLab());
			ba.setApprovedDate(new Date(System.currentTimeMillis()));
			ba.setIsApproved("Y");
			ba.setApproverEmail(approverEmail);
			BillingAccountUtil.sendApprovedBillingAccountEmail(sess, serverName, launchAppURL, ba, lab, approverEmail);

			message = "Billing Account " + ba.getAccountNameDisplay() + " has been successfully approved.";

		}

	} catch (Exception e) {
		message = "There was an issue activating the billing account.  Please activate through the GNomEx app and contact GNomEx support.  Thanks.";
		LOG.error(message, e);
	} finally {
		try {
			HibernateSession.closeSession();
			String url = "/approve_billing_account.jsp"; // relative url for display jsp page
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(url);
			request.setAttribute("message", message);

			rd.forward(request, response);
		} catch (Exception e1) {
			System.out.println("ApproveUser warning - cannot close hibernate session");
		}
	}
}

}
