package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.DataTrack;
import hci.gnomex.model.DataTrackFolder;
import hci.gnomex.model.GenomeBuild;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.UUID;
import org.apache.log4j.Logger;

public class FastDataTransferDownloadDataTrackServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static Logger LOG = Logger.getLogger(FastDataTransferDownloadDataTrackServlet.class);

    private static String serverName;

    public void init() {

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        serverName = req.getServerName();

        String downloadDateText = "datatrack_download_" + new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());

        // Restrict commands to local host if request is not secure
        if (!ServletUtil.checkSecureRequest(req, LOG)) {
            ServletUtil.reportServletError(response, "Secure connection is required. Prefix your request with 'https'",
                    LOG, "Accessing secure command over non-secure line from remote host is not allowed.");
            return;
        }

        try {

            String xmlText = "";
            BufferedReader brIn;

            brIn = req.getReader();
            String input;
            while((input = brIn.readLine()) != null) {
                xmlText = xmlText + input;
            }
            brIn.close();

            // Read analysis file parser, which contains a list of selected analysis files,
            //from session variable stored by CacheDataTrackFileDownloadList.
            String keys = (String)req.getSession().getAttribute(GetEstimatedDownloadDataTrackSize.SESSION_DATATRACK_KEYS);

            // Get security advisor
            SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

            if (secAdvisor != null) {

                Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
                DictionaryHelper dh = DictionaryHelper.getInstance(sess);

                String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_DATATRACK_DIRECTORY);
                String analysisBaseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null, PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);

                // Make sure the system is configured to run FDT
                String fdtSupported = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_SUPPORTED);
                if (fdtSupported == null || !fdtSupported.equals("Y")) {
                    ServletUtil.reportServletError(response, "GNomEx is not configured to support FDT.  Please contact GNomEx support to set " +
                            "appropriate property", LOG);
                    return;
                }



                String softlinks_dir = "";
                // Create random name directory for storing softlinks
                UUID uuid = UUID.randomUUID();

                // Make softlinks directory
                if(softlinks_dir.length() == 0) {
                    softlinks_dir = PropertyDictionaryHelper.getInstance(sess).getFDTDirectoryForGNomEx(req.getServerName())+uuid.toString();
                    File dir = new File(softlinks_dir);
                    boolean success = dir.mkdir();
                    if (!success) {
                        response.setStatus(999);
                        System.out.println("Error. Unable to create softlinks directory.");
                        return;
                    }

                    // change ownership to HCI_fdt user
                    String fdtUser = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_USER);
                    if (fdtUser == null || fdtUser.equals("")) {
                        fdtUser = "fdt";
                    }
                    String fdtGroup = PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_GROUP);
                    if (fdtGroup == null || fdtGroup.equals("")) {
                        fdtGroup = "fdt_security";
                    }
                    Process process = Runtime.getRuntime().exec( new String[] { "chown", "-R", fdtUser + ":" + fdtGroup, softlinks_dir } );
                    process.waitFor();
                    process.destroy();

                    // only fdt user and group have permissions on this directory
                    process = Runtime.getRuntime().exec( new String[] { "chmod", "770", softlinks_dir } );
                    process.waitFor();
                    process.destroy();

                    softlinks_dir = softlinks_dir + Constants.FILE_SEPARATOR;
                }


                // For each data track
                String[] keyTokens = keys.split(":");
                for(int x = 0; x < keyTokens.length; x++) {
                    String key = keyTokens[x];

                    String[] idTokens = key.split(",");
                    if (idTokens.length != 2) {
                        throw new Exception("Invalid parameter format " + key + " encountered. Expected 99,99 for idDataTrack and idDataTrackFolder");
                    }
                    Integer idDataTrack = new Integer(idTokens[0]);
                    Integer idDataTrackFolder = new Integer(idTokens[1]);

                    DataTrack dataTrack = DataTrack.class.cast(sess.load(DataTrack.class, idDataTrack));

                    if (!secAdvisor.canRead(dataTrack)) {
                        LOG.error("Insufficient permission to read/download dataTrack.");
                        continue;
                    }

                    DataTrackFolder dataTrackFolder = null;
                    if (idDataTrackFolder.intValue() == -99) {
                        GenomeBuild gv = dh.getGenomeBuildObject(dataTrack.getIdGenomeBuild());
                        dataTrackFolder = gv.getRootDataTrackFolder();
                    } else {
                        for(Iterator<?>i = dataTrack.getFolders().iterator(); i.hasNext();) {
                            DataTrackFolder ag = DataTrackFolder.class.cast(i.next());
                            if (ag.getIdDataTrackFolder().equals(idDataTrackFolder)) {
                                dataTrackFolder = ag;
                                break;

                            }
                        }

                    }
                    if (dataTrackFolder == null) {
                        throw new Exception("Unable to find dataTrack folder " + idDataTrackFolder);
                    }

                    // For each file to be downloaded for the data track
                    for (File file : dataTrack.getFiles(baseDir, analysisBaseDir)) {

                        // Ignore file descriptors that represent directories.  We will
                        // just download  actual files.
                        if (file.isDirectory()) {
                            continue;
                        }

                        if (file.length() == 0) {
                            // Ignore files with length of zero
                            continue;
                        }

                        String path = softlinks_dir + downloadDateText + Constants.FILE_SEPARATOR + dataTrackFolder.getQualifiedName() + Constants.FILE_SEPARATOR + dataTrack.getName() + Constants.FILE_SEPARATOR;
                        String softLinksFileName = path + file.getName();


                        // Make subdirectories of path if necessary
                        File dir = new File(path);
                        if(!dir.exists()) {
                            boolean isDirCreated = dir.mkdirs();
                            if (!isDirCreated) {
                                response.setStatus(999);
                                System.out.println("Error. Unable to create " + path);
                                return;
                            } else {
                                System.out.println("dir created");
                            }
                        }


                        Process process = Runtime.getRuntime().exec( new String[] { "ln", "-s", file.getCanonicalPath().replace("\\", Constants.FILE_SEPARATOR), softLinksFileName } );

                        process.waitFor();
                        process.destroy();
                    }
                }

                secAdvisor.closeReadOnlyHibernateSession();

                // clear out session variable
                req.getSession().setAttribute(GetEstimatedDownloadDataTrackSize.SESSION_DATATRACK_KEYS, null);


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
                    out.println("***** Please read, the directions have changed *****");
                    out.println("Command line download instructions:");
                    out.println("");
                    String fdtJarLoc = PropertyDictionaryHelper.getInstance(sess).getFDTJarLocation(req.getServerName());
                    String fdtServerName = PropertyDictionaryHelper.getInstance(sess).getFDTServerName(req.getServerName());
                    String softLinksPath = PropertyDictionaryHelper.getInstance(sess).GetFDTDirectory(req.getServerName())+uuid.toString()+Constants.FILE_SEPARATOR+downloadDateText;
                    if (fdtJarLoc == null || fdtJarLoc.equals("")) {
                        fdtJarLoc = "http://hci-bio-app.hci.utah.edu/FDT/";
                    }
                    out.println("1) Download the fdtCommandLine.jar app from " + fdtJarLoc);
                    out.println("2) Open port 54321 in all firewalls surrounding your computer (this may occur automatically upon transfer).");
                    out.println("3) Execute the following on the command line after changing the path2xxx variables:");
                    out.println("4) There is a 24 hour timeout on this command.  After that time please generate a new command line using the FDT Upload Command Line link.");
                    out.println("");
                    out.println("java -jar path2YourLocalCopyOfFDT/fdtCommandLine.jar -noupdates -ka 999999 -pull -r -c " + fdtServerName + " -d path2SaveDataOnYourLocalComputer " + softLinksPath);
                    out.println("");
                    out.println("-->");
                    out.println("<information>");
                    out.println("<title>GNomEx FDT Download DataTrack Files</title>");
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
                    out.println("<argument>download</argument>");
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
                System.out.println( "FastDataTransferDownloadDataTrackServlet: You must have a SecurityAdvisor in order to run this command.");
            }
        } catch (Exception e) {
            response.setStatus(999);
            LOG.error( "FastDataTransferDownloadDataTrackServlet: An exception occurred ", e);

        }

    }

}
