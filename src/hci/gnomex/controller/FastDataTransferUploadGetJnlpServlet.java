package hci.gnomex.controller;

import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.ServletUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.log4j.Logger;

public class FastDataTransferUploadGetJnlpServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static Logger LOG = Logger.getLogger(FastDataTransferUploadGetJnlpServlet.class);

    private String serverName = "";


    public void init() {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        serverName = req.getServerName();

        String uuid = (String) req.getParameter("uuid");
        if (uuid == null) {
            ServletUtil.reportServletError(response, "Missing UUID parameter.", LOG);
            return;
        }

        String showCommandLineInstructions = "N";
        if (req.getParameter("showCommandLineInstructions") != null && !req.getParameter("showCommandLineInstructions").equals("")) {
            showCommandLineInstructions = req.getParameter("showCommandLineInstructions");
        }

        // Restrict commands to local host if request is not secure
        if (!ServletUtil.checkSecureRequest(req, LOG)) {
            ServletUtil.reportServletError(response, "Secure connection is required. Prefix your request with 'https'",
                    LOG, "Accessing secure command over non-secure line from remote host is not allowed.");
            return;
        }


        try {

            // Get security advisor
            SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

            if (secAdvisor != null) {

                Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
                DictionaryHelper dh = DictionaryHelper.getInstance(sess);

                // Make sure the system is configured to run FDT
                String fdtSupported = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_SUPPORTED);
                if (fdtSupported == null || !fdtSupported.equals("Y")) {
                    ServletUtil.reportServletError(response, "GNomEx is not configured to support FDT.  Please contact GNomEx support to set " +
                            "appropriate property.", LOG);
                    return;
                }

                secAdvisor.closeReadOnlyHibernateSession();

                req.getSession().setAttribute(CacheFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER, null);
                String fdtJarLoc = PropertyDictionaryHelper.getInstance(sess).getFDTJarLocation(req.getServerName());
                String fdtServerName = PropertyDictionaryHelper.getInstance(sess).getFDTServerName(req.getServerName());
                String softLinksPath = PropertyDictionaryHelper.getInstance(sess).GetFDTDirectory(req.getServerName())+uuid;
                if (fdtJarLoc == null || fdtJarLoc.equals("")) {
                    fdtJarLoc = "http://monalisa.cern.ch/FDT/";
                }

                if(showCommandLineInstructions != null && showCommandLineInstructions.equals("Y")) {
                    response.setContentType("text/html");
                    response.getOutputStream().println("Complete the following steps to run FDT from the command line:");
                    response.getOutputStream().println("1) Download the fdt.jar app from " + fdtJarLoc);
                    response.getOutputStream().println("2) Open port 54321 in all firewalls surrounding your computer (this may occur automatically upon transfer).");
                    response.getOutputStream().println("3) Execute the following on the command line(Make sure paths reflect your environment):");
                    response.getOutputStream().println("4) There is a 24 hour timeout on this command.  After that time please generate a new command line using the FDT Upload Command Line link.");
                    response.getOutputStream().println("java -jar ./fdt.jar -r -c " + fdtServerName + " -d " + softLinksPath + " ./");
                    response.getOutputStream().flush();
                    return;
                }

                response.setHeader("Content-Disposition","attachment;filename=\"gnomex.jnlp\"");
                response.setContentType("application/jnlp");
                response.setHeader("Cache-Control", "max-age=0, must-revalidate");
                try {
                    ServletOutputStream out = response.getOutputStream();


                    out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                    out.println("<jnlp spec=\"1.0\"");
                    String codebase_param = PropertyDictionaryHelper.getInstance(sess).getFDTClientCodebase(req.getServerName());
                    out.println("codebase=\""+codebase_param+"\">");
                    out.println("<!--");
                    out.println("");
                    out.println("Command line upload instructions:");
                    out.println("");
                    out.println("1) Download the fdt.jar app from " + fdtJarLoc);
                    out.println("2) Open port 54321 in all firewalls surrounding your computer (this may occur automatically upon transfer).");
                    out.println("3) Execute the following on the command line after changing the path2xxx variables:");
                    out.println("4) There is a 24 hour timeout on this command.  After that time please generate a new command line using the FDT Upload Command Line link.");
                    out.println("");
                    out.println("java -jar path2YourLocalCopyOfFDT/fdt.jar -r -c " + fdtServerName + " -d " + softLinksPath + " path2YourLocalDirContainingFiles2Upload/");
                    out.println("");
                    out.println("-->");
                    out.println("<information>");
                    out.println("<title>GNomEx FDT Upload</title>");
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
                    out.println("<argument>"+fdtServerName+"</argument>");
                    out.println("<argument>upload</argument>");
                    out.println("<argument>" + softLinksPath + "</argument>");
                    out.println("</application-desc>");
                    out.println("</jnlp>");
                    out.close();
                    out.flush();

                } catch (IOException e) {
                    LOG.error( "Unable to get response output stream.", e );
                }

            } else {
                response.setStatus(999);
                System.out.println( "FastDataTransferUploadGetJnlpServlet: You must have a SecurityAdvisor in order to run this command.");
            }
        } catch (Exception e) {
            response.setStatus(999);
            LOG.error( "FastDataTransferUploadGetJnlpServlet: An exception occurred ", e);

        }

    }

}
