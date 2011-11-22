package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.AnalysisFileDescriptorParser;
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


public class FastDataTransferDownloadAnalysisServlet extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferDownloadAnalysisServlet.class);

  private AnalysisFileDescriptorParser parser = null;


  //private ArchiveHelper archiveHelper = new ArchiveHelper();

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
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
        log.debug("Requested from local host");
      }
      else {
        showError(response, "Accessing secure command over non-secure line from remote host is not allowed");
        return;
      }
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
      //from session variable stored by CacheAnalysisFileDownloadList.
      parser = (AnalysisFileDescriptorParser) req.getSession().getAttribute(CacheAnalysisFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER);

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {

        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);

        // Make sure the system is configured to run FDT
        String fdtSupported = PropertyHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_SUPPORTED);
        if (fdtSupported == null || !fdtSupported.equals("Y")) {
          showError(response, "GNomEx is not configured to support FDT.  Please contact GNomEx support to set appropriate property");
          return;
        }

        parser.parse();

        String softlinks_dir = "";
        // Create random name directory for storing softlinks
        UUID uuid = UUID.randomUUID();

        String analysisNumberBase = "";
        

        // For each analysis
        for(Iterator i = parser.getAnalysisNumbers().iterator(); i.hasNext();) {
          String analysisNumber = (String)i.next();

          Analysis analysis = null;
          List analysisList = sess.createQuery("SELECT a from Analysis a where a.number = '" + analysisNumber + "'").list();
          if (analysisList.size() == 1) {
            analysis = (Analysis)analysisList.get(0);
          }

          // If we can't find the analysis in the database, just bypass it.
          if (analysis == null) {
            log.error("Unable to find analysis " + analysisNumber + ".  Bypassing fdt download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }

          // Check permissions - bypass this analysis if the user 
          // does not have  permission to read it.
          if (!secAdvisor.canRead(analysis)) {  
            log.error("Insufficient permissions to read analysis " + analysisNumber + ".  Bypassing fdt download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }

          List fileDescriptors = parser.getFileDescriptors(analysisNumber);

          // For each file to be downloaded for the analysis
          for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext();) {

            AnalysisFileDescriptor fd = (AnalysisFileDescriptor) i1.next();

            // Ignore file descriptors that represent directories.  We will
            // just download  actual files.
            if (fd.getType().equals("dir")) {
              continue;
            }


            // Since we use the request number to determine if user has permission to read the data, match sure
            // it matches the request number of the directory.  If it doesn't bypass the download
            // for this file.
            analysisNumberBase = fd.getNumber();
            if (!analysisNumber.equalsIgnoreCase(analysisNumberBase)) {
              log.error("Analysis number does not match directory for attempted download on " + fd.getFileName() + " for user " + req.getUserPrincipal().getName() + ".  Bypassing fdt download." );
              continue;
            }

            // Make softlinks directory
            if(softlinks_dir.length() == 0) {							
              softlinks_dir = PropertyHelper.getInstance(sess).getFDTDirectoryForGNomEx(req.getServerName())+uuid.toString();
              File dir = new File(softlinks_dir);
              boolean success = dir.mkdir();
              if (!success) {
                response.setStatus(999);
                System.out.println("Error. Unable to create softlinks directory.");
                return;
              }
              
              // change ownership to HCI_fdt user
              String fdtUser = PropertyHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_USER);
              if (fdtUser == null || fdtUser.equals("")) {
                fdtUser = "fdt";
              }
              String fdtGroup = PropertyHelper.getInstance(sess).getProperty(PropertyDictionary.FDT_GROUP);
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

              softlinks_dir = softlinks_dir + File.separator;
            }


            if (fd.getFileSize() == 0) {
              // Ignore files with length of zero
              continue;
            }


            // Get file/location of file to create symbolic link to
            String fullPath = fd.getFileName();
            int indxFileName = fullPath.lastIndexOf("/");
            int indxDirPath = fullPath.indexOf(analysisNumberBase);

            // Get fileName to use for the name of the softlink
            String fileName = softlinks_dir+fullPath.substring(indxDirPath);	

            // Make intermediate directories if necessary
            String dirsName = softlinks_dir+fullPath.substring(indxDirPath, indxFileName);
            File dir = new File(dirsName);
            if(!dir.exists()) {
              boolean isDirCreated = dir.mkdirs();  
              if (!isDirCreated) {
                response.setStatus(999);
                System.out.println("Error. Unable to create " + dirsName);
                return;
              }						    				    					    	
            }

            Process process = Runtime.getRuntime().exec( new String[] { "ln", "-s", fd.getFileName(), fileName } );					
            process.waitFor();
            process.destroy();						
          }     
        }

        secAdvisor.closeReadOnlyHibernateSession();
        
        // clear out session variable
        req.getSession().setAttribute(CacheFileDownloadList.SESSION_KEY_FILE_DESCRIPTOR_PARSER, null);
        

        response.setHeader("Content-Disposition","attachment;filename=\"gnomex.jnlp\"");
        response.setContentType("application/jnlp");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        try {
          ServletOutputStream out = response.getOutputStream();

          out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
          out.println("<jnlp spec=\"1.0\"");
          String codebase_param = PropertyHelper.getInstance(sess).getFDTClientCodebase(req.getServerName());
          out.println("codebase=\""+codebase_param+"\">");
          out.println("<!--");
          out.println("");
          out.println("Command line download instructions:");
          out.println("");
          String fdtJarLoc = PropertyHelper.getInstance(sess).getFDTJarLocation(req.getServerName());
          String fdtServerName = PropertyHelper.getInstance(sess).getFDTServerName(req.getServerName());
          String softLinksPath = PropertyHelper.getInstance(sess).GetFDTDirectory(req.getServerName())+uuid.toString()+File.separator+analysisNumberBase;          
          if (fdtJarLoc == null || fdtJarLoc.equals("")) {
            fdtJarLoc = "http://monalisa.cern.ch/FDT/";
          }
          out.println("1) Download the fdt.jar app from " + fdtJarLoc);
          out.println("2) Open port 54321 in all firewalls surrounding your computer (this may occur automatically upon transfer).");
          out.println("3) Execute the following on the command line after changing the path2xxx variables:");
          out.println("");
          out.println("java -jar path2YourLocalCopyOfFDT/fdt.jar -pull -r -c " + fdtServerName + " -d path2SaveDataOnYourLocalComputer " + softLinksPath);
          out.println("");
          out.println("-->");
          out.println("<information>");
          out.println("<title>GNomEx FDT Download Analysis Files</title>");
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
          log.error( "Unable to get response output stream.", e );
        }	          

      } else {
        response.setStatus(999);
        System.out.println( "FastDataTransferDownloadAnalysisServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setStatus(999);
      System.out.println( "FastDataTransferDownloadAnalysisServlet: An exception occurred " + e.toString());
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
