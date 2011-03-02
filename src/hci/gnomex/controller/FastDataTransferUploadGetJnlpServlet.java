package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.PropertyHelper;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class FastDataTransferUploadGetJnlpServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferUploadGetJnlpServlet.class);

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
		        
				String softLinksPath = PropertyHelper.getInstance(sess).getFastDataTransferDirectory(req.getServerName())+uuid;		
		        		        
				
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
					out.println("codebase=\""+codebase_param+"\">");
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
}
