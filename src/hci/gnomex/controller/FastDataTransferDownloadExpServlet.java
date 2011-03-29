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


public class FastDataTransferDownloadExpServlet extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(FastDataTransferDownloadExpServlet.class);

  private FileDescriptorParser parser = null;


  //private ArchiveHelper archiveHelper = new ArchiveHelper();

  private String serverName = "";



  public void init() {

  }

  protected void doPost(HttpServletRequest req, HttpServletResponse response)
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

      String fileDescriptorXMLString = "<FileDescriptorList>" + xmlText + "</FileDescriptorList>";

      StringReader reader = new StringReader(fileDescriptorXMLString);

      SAXBuilder sax = new SAXBuilder();
      Document doc = sax.build(reader);
      parser = new FileDescriptorParser(doc);

      // Get security advisor
      SecurityAdvisor secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {

        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");
        DictionaryHelper dh = DictionaryHelper.getInstance(sess);


        // Make sure the system is configured to run FDT
        String fdtSupported = PropertyHelper.getInstance(sess).getProperty(Property.FDT_SUPPORTED);
        if (fdtSupported == null || !fdtSupported.equals("Y")) {
          showError(response, "GNomEx is not configured to support FDT.  Please contact GNomEx support to set appropriate property");
          return;
        }

        parser.parse();

        String softlinks_dir = "";
        // Create random name directory for storing softlinks
        UUID uuid = UUID.randomUUID();

        String requestNumberBase = "";

        // For each request
        for(Iterator i = parser.getRequestNumbers().iterator(); i.hasNext();) {
          String requestNumber = (String)i.next();

          Request request = findRequest(sess, requestNumber);

          // If we can't find the request in the database, just bypass it.
          if (request == null) {
            log.error("Unable to find request " + requestNumber + ".  Bypassing fdt download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }

          // Check permissions - bypass this request if the user 
          // does not have  permission to read it.
          if (!secAdvisor.canRead(request)) {  
            log.error("Insufficient permissions to read request " + requestNumber + ".  Bypassing fdt download for user " + req.getUserPrincipal().getName() + ".");
            continue;
          }

          List fileDescriptors = parser.getFileDescriptors(requestNumber);

          // For each file to be downloaded for the request
          for (Iterator i1 = fileDescriptors.iterator(); i1.hasNext();) {

            FileDescriptor fd = (FileDescriptor) i1.next();

            // Ignore file descriptors that represent directories.  We will
            // just download  actual files.
            if (fd.getType().equals("dir")) {
              continue;
            }


            // Since we use the request number to determine if user has permission to read the data, make sure
            // it matches the request number of the directory.  If it doesn't bypass the download
            // for this file.
            requestNumberBase = Request.getBaseRequestNumber(requestNumber);
            if (!requestNumberBase.equalsIgnoreCase(fd.getMainFolderName(dh, serverName))) {
              boolean isAuthorizedDirectory = false;
              // If this is a flow cell, make sure that that a sequence lane on this request has this flow cell
              for(Iterator i2 = request.getSequenceLanes().iterator(); i2.hasNext();) {
                SequenceLane lane = (SequenceLane)i2.next();
                if (lane.getFlowCellChannel() != null && 
                    lane.getFlowCellChannel().getFlowCell().getNumber().equals(fd.getMainFolderName(dh, serverName))) {
                  isAuthorizedDirectory = true;
                  break;
                }

              }
              if (!isAuthorizedDirectory) {
                log.error("Request number " + requestNumber + " does not correspond to the directory " + fd.getMainFolderName(dh, serverName) + " for attempted download on " + fd.getFileName() +  ".  Bypassing download." );
                continue;              
              }
            }


            // Make softlinks dir
            if(softlinks_dir.length() == 0) {							
              softlinks_dir = PropertyHelper.getInstance(sess).getFDTDirectoryForGNomEx(req.getServerName())+uuid.toString();
              File dir = new File(softlinks_dir);
              boolean success = dir.mkdir();
              if (!success) {
                response.setStatus(999);
                System.out.println("Error. Unable to create softlinks directory.");
                return;
              } 
              // change ownership to fdt
              //Process process = Runtime.getRuntime().exec( new String[] { "chown", "-R", "fdt:fdt", softlinks_dir } );          
              //process.waitFor();
              //process.destroy(); 
              
              // only fdt user (and root) can read from this directory
              //process = Runtime.getRuntime().exec( new String[] { "chmod", "500", softlinks_dir } );          
              Process process = Runtime.getRuntime().exec( new String[] { "chmod", "777", softlinks_dir } );          
              process.waitFor();
              process.destroy();        
            }


            if (fd.getFileSize() == 0) {
              // Ignore files with length of zero
              continue;
            }


            // Get file/location of file to create symbolic link to
            String fullPath = fd.getFileName();
            int indxFileName = fullPath.lastIndexOf("/");
            int indxDirPath = fullPath.indexOf(requestNumberBase);

            // Get fileName to use for the name of the softlink
            String fileName = softlinks_dir+fullPath.substring(indxDirPath-1);	

            // Make intermediate directories if necessary
            String dirsName = softlinks_dir+fullPath.substring(indxDirPath-1, indxFileName);
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

        response.setHeader("Content-Disposition","attachment;filename=\"gnomex.jnlp\"");
        response.setContentType("application/jnlp");
        response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        try {
          ServletOutputStream out = response.getOutputStream();

          out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		      out.println("<jnlp spec=\"1.0\"");
		      String codebase_param = PropertyHelper.getInstance(sess).getFDTClientCodebase(req.getServerName());
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
		      String argument_param = PropertyHelper.getInstance(sess).getFDTServerName(req.getServerName());
		      out.println("<argument>"+argument_param+"</argument>");
		      out.println("<argument>download</argument>");
		      String softLinksPath = PropertyHelper.getInstance(sess).GetFDTDirectory(req.getServerName())+uuid.toString()+System.getProperty("file.separator")+requestNumberBase;					
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
		    System.out.println( "FastDataTransferDownloadExpServlet: You must have a SecurityAdvisor in order to run this command.");
		  }
		} catch (Exception e) {
		  response.setStatus(999);
		  System.out.println( "FastDataTransferDownloadExpServlet: An exception occurred " + e.toString());
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
