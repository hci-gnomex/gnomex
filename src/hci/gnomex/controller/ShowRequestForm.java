package hci.gnomex.controller;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.*;
import hci.gnomex.utility.*;
import org.hibernate.Session;

import com.itextpdf.text.*;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.security.SecurityAdvisor;
import hci.report.constants.ReportFormats;
import hci.report.model.ReportTray;
import hci.report.utility.ReportCommand;
import org.apache.log4j.Logger;
@SuppressWarnings("serial")
public class ShowRequestForm extends ReportCommand implements Serializable {

    private static Logger LOG = Logger.getLogger(ShowRequestForm.class);

    public String SUCCESS_JSP = "/form_pdf.jsp";

    private SecurityAdvisor		secAdvisor;

    private Integer          	idRequest;
    private Request          	request;

    private String           	amendState;

    private Boolean				comingFromEmail = false;

    private AppUser          	appUser;
    private BillingAccount   	billingAccount;

    private DictionaryHelper 	dictionaryHelper;

    private String				serverName;

    @Override
    public void validate() {
    }

    @Override
    public void loadCommand(HttpServletRequest request, HttpSession session) {
        if (request.getParameter("idRequest") != null) {
            idRequest = new Integer(request.getParameter("idRequest"));
        } else {
            this.addInvalidField("idRequest", "idRequest is required");
        }

        amendState = "";
        if (request.getParameter("amendState") != null && !request.getParameter("amendState").equals("")) {
            amendState = request.getParameter("amendState");
        }

        if(request.getParameter("comingFromEmail") != null && !request.getParameter("comingFromEmail").equals("")){
            comingFromEmail = request.getParameter("comingFromEmail").equals("Y");
        }

        serverName = request.getServerName();
        secAdvisor = (SecurityAdvisor) session.getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Command execute() throws RollBackCommandException {
        try {

            Session sess = secAdvisor.getReadOnlyHibernateSession(this.getUsername());

            dictionaryHelper = DictionaryHelper.getInstance(sess);

            request = (Request) sess.get(Request.class, idRequest);
            if (request == null) {
                this.addInvalidField("no request", "Request not found");
            }

            if (isValid()) {

                if (secAdvisor.canRead(request)) {

                    if (request.getIdAppUser() != null) {
                        appUser = (AppUser) sess.get(AppUser.class, request.getIdAppUser());
                    }

                    if (request.getAcceptingBalanceAccountId(sess) != null) {
                        billingAccount = (BillingAccount) sess.get(BillingAccount.class, request.getAcceptingBalanceAccountId(sess));
                    }

                    // Set up the ReportTray
                    String title = dictionaryHelper.getRequestCategory(request.getCodeRequestCategory()) + " Request " + request.getNumber();
                    String fileName = "gnomex_request_report_" + request.getNumber().toLowerCase();

                    tray = new ReportTray();
                    tray.setReportDate(new java.util.Date(System.currentTimeMillis()));
                    tray.setReportTitle(title);
                    tray.setReportDescription(title);
                    tray.setFileName(fileName);
                    tray.setFormat(ReportFormats.PDF);

                    @SuppressWarnings("rawtypes")
                    java.util.List rows = new ArrayList();

                    // Build PDF elements
                    RequestPDFFormatter formatter = new RequestPDFFormatter(secAdvisor, request, appUser, billingAccount, dictionaryHelper, sess, amendState);
                    ArrayList<Element> content = formatter.makeContent();
                    for (Element e : content) {
                        rows.add(e);
                    }

                    tray.setRows(rows);

                    if(comingFromEmail) {
                        sendAcknowledgementEmail(sess);
                    }
                }
            }

            if (isValid()) {
                setResponsePage(this.SUCCESS_JSP);
            } else {
                setResponsePage(this.ERROR_JSP);
            }

        } catch (Exception e) {
            LOG.error("An exception has occurred in ShowRequestForm ", e);

            throw new RollBackCommandException(e.getMessage());
        } finally {
            try {
                secAdvisor.closeReadOnlyHibernateSession();
            } catch(Exception e){
        LOG.error("Error", e);
      }
        }

        return this;
    }


    private void sendAcknowledgementEmail(Session sess){

        CoreFacility cf = sess.load(CoreFacility.class, request.getIdCoreFacility());
        // get core facility email
        String toAddress = cf.getContactEmail();

        String fromAddress = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.GENERIC_NO_REPLY_EMAIL);

        // new property for additional people who should get this email
        String ccAddress = "";
        if(request.getSubmitterEmail() != null && !request.getSubmitterEmail().equals("")){
            ccAddress += request.getSubmitterEmail() + ",";
        }
        if(request.getLab().getContactEmail() != null && !request.getLab().getContactEmail().equals("")){
            ccAddress += request.getLab().getContactEmail();
        }

        String subject = "Request " + request.getNumber() + " has been acknowledged by the " + cf.getFacilityName() + " core.";

        StringBuffer emailBody = new StringBuffer();

        emailBody.append("The request to add services to existing experiment " + request.getNumber() + " has been acknowledged by the " + cf.getFacilityName() + " core.\n");

        emailBody.append("The request will enter the workflow shortly.\n");

        try {
            MailUtilHelper helper = new MailUtilHelper(toAddress, ccAddress, null, fromAddress, subject, emailBody.toString(), null, true, DictionaryHelper.getInstance(sess), serverName);
            MailUtil.validateAndSendEmail(helper);
        } catch(Exception e){
            LOG.error("Error in ShowRequestForm", e);

        }

    }
    /* (non-Javadoc)
     * @see hci.framework.control.Command#setRequestState(javax.servlet.http.HttpServletRequest)
     */
    @Override
    public HttpServletRequest setRequestState(HttpServletRequest request) {
        request.setAttribute("tray", this.tray);
        return request;
    }

    /**
     *  The callback method called after the loadCommand, and execute methods,
     *  this method allows you to manipulate the HttpServletResponse object prior
     *  to forwarding to the result JSP (add a cookie, etc.)
     *
     *@param  request  The HttpServletResponse for the command
     *@return          The processed response
     */
    @Override
    public HttpServletResponse setResponseState(HttpServletResponse response) {
        return response;
    }

    /* (non-Javadoc)
     * @see hci.framework.control.Command#setSessionState(javax.servlet.http.HttpSession)
     */
    @Override
    public HttpSession setSessionState(HttpSession session) {
        return session;
    }

    /* (non-Javadoc)
     * @see hci.report.utility.ReportCommand#loadContextPermissions()
     */
    @Override
    public void loadContextPermissions() {
    }

    public void loadContextPermissions(String userName) throws SQLException {
    }

}
