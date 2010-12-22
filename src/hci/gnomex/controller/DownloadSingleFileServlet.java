package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.PropertyHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;


public class DownloadSingleFileServlet extends HttpServlet { 

  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DownloadSingleFileServlet.class);
  
  private String                          baseDir = null;
  private String                          baseDirFlowCell = null;
  private Integer                         idRequest = null;
  private String                          requestNumber = null;
  private String                          fileName = null;
  private String                          dir = null;
  private String                          view = "N";
  
  
  public void init() {
  
  }
    
  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {
    
    idRequest = null;
    baseDir = null;
    baseDirFlowCell = null;
    requestNumber = null;
    fileName = null;
    dir = null;
    view = "N";
    
    // restrict commands to local host if request is not secure
    if (Constants.REQUIRE_SECURE_REMOTE && !req.isSecure()) {
      if (req.getRemoteAddr().equals(InetAddress.getLocalHost().getHostAddress())
          || req.getRemoteAddr().equals("127.0.0.1")
          || InetAddress.getByName(req.getRemoteAddr()).isLoopbackAddress()) {
        log.debug("Requested from local host");
      }
      else {
        log.error("Accessing secure command over non-secure line from remote host is not allowed");
        
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "Secure connection is required. Prefix your request with 'https: "
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        return;
      }
    }

    // Get the idRequest parameter
    if (req.getParameter("idRequest") != null && !req.getParameter("idRequest").equals("")) {
      idRequest = new Integer(req.getParameter("idRequest"));
    } else if (req.getParameter("requestNumber") != null && !req.getParameter("requestNumber").equals("")) {
      requestNumber = req.getParameter("requestNumber");
    }
    // Get the fileName parameter
    if (req.getParameter("fileName") != null && !req.getParameter("fileName").equals("")) {
      fileName = req.getParameter("fileName");
      // Change all backslash to forward slash for comparison
      fileName = fileName.replaceAll("\\\\", "/");
    } 
    // Get the dir parameter
    if (req.getParameter("dir") != null && !req.getParameter("dir").equals("")) {
      dir = req.getParameter("dir");
    } 
    // Get the view flag
    if (req.getParameter("view") != null && !req.getParameter("view").equals("")) {
      view = req.getParameter("view");
    } 
    if ((idRequest == null && requestNumber == null) || fileName == null) {
      log.error("idRequest/requestNumber and fileName required");
      
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "Missing parameters:  idRequest and fileName required"
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      return;
      
    }

    InputStream in = null;
    SecurityAdvisor secAdvisor = null;
    try {
      

      // Get security advisor
     secAdvisor = (SecurityAdvisor) req.getSession().getAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY);

      if (secAdvisor != null) {
        
        // Set the content type and content disposition based on whether we
        // want to serve the file to the browser or download it.
        if (view.equals("Y")) {
          String mimeType = req.getSession().getServletContext().getMimeType(fileName);
          response.setContentType(mimeType);
        } else {
          response.setContentType("application/x-download");
          response.setHeader("Content-Disposition", "attachment;filename=" + fileName);          
          response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        }
        
        
        Session sess = secAdvisor.getReadOnlyHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");

        
        baseDir = PropertyHelper.getInstance(sess).getMicroarrayDirectoryForReading(req.getServerName());
        baseDirFlowCell = PropertyHelper.getInstance(sess).getFlowCellDirectory(req.getServerName());
        
          
        
        Request experiment = null;
        if (idRequest != null) {
          experiment = (Request)sess.load(Request.class, idRequest);
        } else if (requestNumber != null){
          String queryBuf = "";
          if (requestNumber.toUpperCase().endsWith("R")) {
            queryBuf = "SELECT r from Request as r where r.number like '" + requestNumber +"%'";           
          } else {
            queryBuf = "SELECT r from Request as r where r.number = '" + requestNumber +"'";
          }
          List rows = sess.createQuery(queryBuf).list();
          if (rows.size() > 0) {
            experiment = (Request)rows.iterator().next();
          }
        }


        // If we can't find the experiment in the database, just bypass it.
        if (experiment == null) {
          throw new Exception("Cannot find experiment " + idRequest);
        }

        // Check permissions - bypass this experiment if the user 
        // does not have  permission to read it.
        if (!secAdvisor.canRead(experiment)) {  
          throw new Exception("Insufficient permissions to read experiment " + experiment.getNumber() + ".  Bypassing download.");
        }

        // Now get the files that exist on the file server for this experiment
        Map requestMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List requestNumbers = new ArrayList<String>();
        Set folders = GetRequestDownloadList.getRequestDownloadFolders(baseDir, Request.getBaseRequestNumber(experiment.getNumber()), experiment.getCreateYear());
        StringBuffer keys = new StringBuffer();
        for(Iterator i = folders.iterator(); i.hasNext();) {
          String folder = (String)i.next();
          if (keys.length() > 0) {
            keys.append(":");
          }
          keys.append(experiment.getKey(folder));
        }
        // Also get the flow cells directories for this request
        for(Iterator i = DownloadSingleFileServlet.getFlowCells(sess, experiment).iterator(); i.hasNext();) {
          FlowCell flowCell      =  (FlowCell)i.next();
          
          String theCreateDate    = flowCell.formatDate((java.sql.Date)flowCell.getCreateDate());
          String dateTokens[] = theCreateDate.split("/");
          String createMonth = dateTokens[0];
          String createDay   = dateTokens[1];
          String theCreateYear  = dateTokens[2];
          String sortDate = theCreateYear + createMonth + createDay;    
          
          String fcKey = flowCell.getCreateYear() + "-" + sortDate + "-" + experiment.getNumber() + "-" + flowCell.getNumber() + "-" + PropertyHelper.getInstance(sess).getProperty(Property.FLOWCELL_DIRECTORY_FLAG);
          if (keys.length() > 0) {
            keys.append(":");
          }
          keys.append(fcKey);
        }
        GetExpandedFileList.getFileNamesToDownload(baseDir, baseDirFlowCell, keys.toString(), requestNumbers, requestMap, directoryMap, PropertyHelper.getInstance(sess).getProperty(Property.FLOWCELL_DIRECTORY_FLAG));
        
        
        
        // Find the file matching the fileName passed in as a parameter
        FileDescriptor experimentFd = null;
        List directoryKeys   = (List)requestMap.get(experiment.getNumber());
        for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
          String directoryKey = (String)i1.next();
          String dirTokens[] = directoryKey.split("-");
          String theDirectory = dirTokens[1];
          
          List   theFiles     = (List)directoryMap.get(directoryKey);
          for(Iterator i2 = theFiles.iterator(); i2.hasNext();) {
            FileDescriptor fd = (FileDescriptor) i2.next();
            FileDescriptor matchingFd = recurseGetMatchingFileDescriptor(fd, fileName, theDirectory);
            if (matchingFd != null) {
              experimentFd = matchingFd;
              break;
            }
          }
          if (experimentFd != null) {
            break;
          }
        }

        // If we found the experiment, download it
        if (experimentFd != null) {
          in = new FileInputStream(experimentFd.getFileName());
          OutputStream out = response.getOutputStream();
          byte b[] = new byte[102400];
          int numRead = 0;
          int size = 0;
          while (numRead != -1) {
            numRead = in.read(b);
            if (numRead != -1) {
              out.write(b, 0, numRead);                                    
              size += numRead;
            }
          }
          in.close();
          out.close();
          out.flush();
          in = null;
        }

        
        

      } else {
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "DownloadAnalyisSingleFileServlet: You must have a SecurityAdvisor in order to run this command."
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        System.out.println( "DownloadSingleFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "DownloadAnalyisSingleFileServlet: An exception occurred " + e.toString()
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      System.out.println( "DownloadSingleFileServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      try {
        secAdvisor.closeReadOnlyHibernateSession();        
      } catch (Exception e) {
        
      }
      
      if (in != null) {
        in.close();
      }
    }

  }    
  
  private FileDescriptor recurseGetMatchingFileDescriptor(FileDescriptor fd, String fileName, String theDirectory) {
    // Change all backslash to forward slash for comparison
    String fdFileName = fd.getFileName().replaceAll("\\\\", "/");
    
    
    if (fdFileName.endsWith(fileName) &&
        (dir == null || dir.equals(theDirectory))) {
      return fd;
    } else if (fd.getChildren() != null && fd.getChildren().size() > 0) {
      for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
        FileDescriptor childFd = (FileDescriptor)i.next();
        FileDescriptor matchingFd = recurseGetMatchingFileDescriptor(childFd, fileName, theDirectory);
        if (matchingFd != null) {
          return matchingFd;
        }
      } 
      return null;
    } else {
      return null;
    }
  }
  
  public static List getFlowCells(Session sess, Request experiment) {
    StringBuffer queryBuf = new StringBuffer();
    queryBuf.append(" SELECT DISTINCT fc ");
    queryBuf.append(" FROM           Request as req ");
    queryBuf.append(" JOIN           req.sequenceLanes as l ");
    queryBuf.append(" JOIN           l.flowCellChannel as ch ");
    queryBuf.append(" JOIN           ch.flowCell as fc ");
    queryBuf.append(" WHERE          req.idRequest = " + experiment.getIdRequest());
    queryBuf.append(" ORDER BY fc.number");
    
    return sess.createQuery(queryBuf.toString()).list();
  }
 
 
 
}