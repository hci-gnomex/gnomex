package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.FileDescriptorParser;
import hci.gnomex.utility.PropertyHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;


public class FastDataTransferUploadExpGetJnlpServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadExpGetJnlpServlet.class);

	//private ArchiveHelper archiveHelper = new ArchiveHelper();

	private String serverName = "";



	public void init() {

	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
	throws ServletException, IOException {

		serverName = req.getServerName();
		
		String uuid = (String) req.getParameter("uuid");
		if (uuid == null) {
			showError(response, "Missing UUID parameter.");
			return;			
		}
		Integer idRequest = null;
		if (req.getParameter("idRequest") != null && !req.getParameter("idRequest").equals("")) {
			idRequest = new Integer(req.getParameter("idRequest"));
		}
		if (idRequest == null) {
			showError(response, "Missing idRequest parameter.");
			return;			
		}


		// restrict commands to local host if request is not secure
		if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
			if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
					|| req.getRemoteAddr().equals("127.0.0.1")
					|| InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
				log.debug("Requested from local host");
			}
			else {
				showError(response, "Accessing secure command over non-secure line from remote host is not allowed");
				return;
			}
		}


		try {
			

			
			// Get security advisor
			SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

			if (secAdvisor != null) {

				Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
				
		        Request request = (Request)sess.get(Request.class, idRequest);
		        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		        String createYear = formatter.format(request.getCreateDate());
		        
				String softLinksPath = PropertyHelper.getInstance(sess).getFastDataTransferDirectory(req.getServerName())+uuid			
				+ System.getProperty("file.separator") + createYear
				+ System.getProperty("file.separator") + request.getNumber()
				+ System.getProperty("file.separator") + Constants.UPLOAD_STAGING_DIR;			        
		        
				
				DictionaryHelper dh = DictionaryHelper.getInstance(sess);
				

                // Make sure the system is configured to run FDT
                String fdtSupported = PropertyHelper.getInstance(sess).getProperty(Property.FDT_SUPPORTED);
                if (fdtSupported == null || !fdtSupported.equals("Y")) {
                      showError(response, "GNomEx is not configured to support FDT.  Please contact GNomEx support to set appropriate property");
                      return;
                }

				secAdvisor.closeReadOnlyHibernateSession();

				response.setHeader("Content-Disposition","attachment;filename=\"gnomex.jnlp\"");
				response.setContentType("application/jnlp");
				response.setHeader("Cache-Control", "max-age=0, must-revalidate");
				try {
			        ServletOutputStream out = response.getOutputStream();

					out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
					out.println("<jnlp spec=\"1.0\"");
					String codebase_param = PropertyHelper.getInstance(sess).getFastDataTransferCodebaseParam(req.getServerName());
					out.println("codebase=\""+codebase_param+"\"");
					out.println("href=\"upLoad.jnlp\">");
					out.println("<information>");
					out.println("<title>FDT GUI</title>");
					out.println("<vendor>Sun Microsystems, Inc.</vendor>");
					out.println("<offline-allowed/>");
					out.println("</information>");
					out.println("<security> ");
					out.println("<all-permissions/> ");
					out.println("</security>");
					out.println("<resources>");
					out.println("<jar href=\"fdtClient.jar\"/>");
					out.println("<j2se version=\"1.6+\"/>");
					out.println("</resources>");
					out.println("<application-desc main-class=\"gui.FdtMain\">");
					String argument_param = PropertyHelper.getInstance(sess).getFastDataTransferArgumentParam(req.getServerName());
					out.println("<argument>"+argument_param+"</argument>");
					out.println("<argument>upload</argument>");					
					out.println("<argument>" + softLinksPath + "</argument>");
					out.println("</application-desc>");
					out.println("</jnlp>");
			        out.close();
			        out.flush();

				} catch (IOException e) {
					log.error( "Unable to get response output stream.", e );
				}	          

			} else {
				response.setStatus(999);
				System.out.println( "DownloadFileServlet: You must have a SecurityAdvisor in order to run this command.");
			}
		} catch (Exception e) {
			response.setStatus(999);
			System.out.println( "DownloadFileServlet: An exception occurred " + e.toString());
			e.printStackTrace();
		} 					

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

	public static Request findRequest(Session sess, String requestNumber) {
		Request request = null;
		List requests = sess.createQuery("SELECT req from Request req where req.number = '" + requestNumber + "'").list();
		if (requests.size() == 1) {
			request = (Request)requests.get(0);
		}
		return request;    
	}

}
