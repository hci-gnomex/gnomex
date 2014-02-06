package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.BSTSampleInformation;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class DownloadOncoCartaFDFServlet extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadOncoCartaFDFServlet.class);
  
  private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

  private String serverName = "";

  public void init() {

  }

  protected void doGet(HttpServletRequest req, HttpServletResponse response)
  throws ServletException, IOException {

    serverName = req.getServerName();

    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()
          || serverName.equals("h005973")) {
        log.debug("Requested from local host");
      }
      else {
        showError(response, "Accessing secure command over non-secure line from remote host is not allowed");
        return;
      }
    }

    Integer idRequest = null;
    if (req.getParameter("idRequest") != null && !req.getParameter("idRequest").equals("")) {
      idRequest = Integer.parseInt(req.getParameter("idRequest"));
    } else {
      showError(response, "idRequest required.");
      return;
    }
    

    try {


      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {

        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");

        Request request = (Request)sess.load(Request.class, idRequest);
        if (!secAdvisor.canRead(request) || !secAdvisor.hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT)) {
          showError(response, "Insufficient privileges");
          return;
        }

        if (request.getSamples().size() == 0) {
          showError(response, "Selected request has no samples.");
        }
        
        String template = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.ONCOCARTA_FDF_TEMPLATE);
        if (template == null || template.length() == 0) {
          showError(response, "OncoCarta FDF template property not found.");
          return;
        }

        String outString = populateTemplate(sess, request, template);

        secAdvisor.closeReadOnlyHibernateSession();

        writeResponse(response, outString);

      } else {
        response.setStatus(999);
        System.out.println( "DownloadOncoCartaFDF: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setStatus(999);
      System.out.println( "DownloadOncoCartaFDF: An exception occurred " + e.toString());
      e.printStackTrace();
    }           

  }    

  private String populateTemplate(Session sess, Request request, String template) {
    Sample sample = (Sample)request.getSamples().toArray()[0];
    
    BSTSampleInformation info = (BSTSampleInformation)sess.load(BSTSampleInformation.class, sample.getIdSample());
    
    String spNumber = info.getNonNullString(info.getPatientSpNumber());
    String blockId = "";
    char breakChar = '&';
    if (spNumber.lastIndexOf(' ') >= 0) {
      breakChar = ' ';
    } else if (spNumber.lastIndexOf('-') >= 0) {
      breakChar = '-';
    }
    if (breakChar != '&') {
      blockId = spNumber.substring(spNumber.lastIndexOf(breakChar)+1);
      spNumber = spNumber.substring(0, spNumber.lastIndexOf(breakChar));
    }
    
    String outString = template
        .replace("&260280&", info.getNonNullString(info.getQual260nmTo280nmRatio()))
        .replace("&birthdate&", getNonNullDateString(info.getBirthDate()))
        .replace("&blockid&", blockId)
        .replace("&percenttumor&", info.getNonNullString(info.getPercentTumor()))
        .replace("&datereceived&", getNonNullDateString(info.getCreateDate()))
        .replace("&resultsreported&", getNonNullDateString(info.getCompletedDate()))
        .replace("&diagnosis&", info.getNonNullString(info.getDiagnosisIcd9Description()))
        .replace("&name&", info.getNonNullString(info.getName()))
        .replace("&mrn&", info.getNonNullString(info.getMrn()))
        .replace("&sex&", info.getNonNullString(info.getExpandedGender()))
        .replace("&acquisitiondate&", getNonNullDateString(info.getCollectDate()))
        .replace("&specimentested&", info.getNonNullString(info.getSpecimenTested()))
        .replace("&spnumber&", spNumber)
        .replace("&concentration&", info.getNonNullString(info.getConcentration()));
            
    return outString;
  }
  
  private void writeResponse(HttpServletResponse response, String outString) throws IOException {
    response.setHeader("Cache-Control", "max-age=0, must-revalidate");
    response.setContentType("fdf");
    OutputStream out = response.getOutputStream();
    byte b[] = outString.getBytes();
    out.write(b, 0, outString.length());
    out.close();
    out.flush();
  }
  
  private void showError(HttpServletResponse response, String message) throws IOException {
    log.error(message);
    response.setContentType("text/html");
    response.getOutputStream().println(
    "<html><head><title>Error</title></head>");
    response.getOutputStream().println("<body><b>");
    response.getOutputStream().println(message + "<br>");
    response.getOutputStream().println("</body>");
    response.getOutputStream().println("</html>");

  }
  
  private String getNonNullDateString(java.sql.Date date) {
    String rtn = "";
    if (date != null) {
      rtn = dateFormatter.format(date);
    }
    return rtn;
  }
}
