package hci.gnomex.controller;

import hci.gnomex.constants.Constants;
import org.apache.commons.codec.binary.Base64;

import hci.gnomex.model.FlowCell;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.TransferLog;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.UploadDownloadHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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

  private String                          serverName = null;
  private String                          baseDir = null;
  private String                          baseDirFlowCell = null;
  private Integer                         idRequest = null;
  private String                          requestNumber = null;
  private String                          fileName = null;
  private String                          dir = null;
  private String                          view = "N";

  private boolean                         needToPreprocess = false;
  private String						  experimentDir = null;
  private StringBuilder                   htmlText = new StringBuilder(1024000);

  public void init() {

  }

  protected void doGet(HttpServletRequest req, HttpServletResponse response)
      throws ServletException, IOException {

    idRequest = null;
    baseDir = null;
    baseDirFlowCell = null;
    requestNumber = null;
    fileName = null;
    dir = "";
    view = "N";
    needToPreprocess = false;
    htmlText = new StringBuilder(1024000);
    experimentDir = null;

    serverName = req.getServerName();

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
    	String mimeType = req.getSession().getServletContext().getMimeType(fileName); // recognized mime types are defined in Tomcat's web.xml
        if (view.equals("Y") && mimeType != null) {
          response.setContentType(mimeType);
          response.setHeader("Content-Disposition", "filename=" + "\"" + fileName + "\"");
          response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        } else {
          response.setContentType("application/x-download");
          response.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fileName + "\"");
          response.setHeader("Cache-Control", "max-age=0, must-revalidate");
        }

    	needToPreprocess = false;
        if (view.equals("Y")) {
          needToPreprocess = true;
          if (!(fileName.toLowerCase().endsWith("html") || fileName.toLowerCase().endsWith("htm"))) {
        	  needToPreprocess = false;
          }
	  }


        Session sess = secAdvisor.getHibernateSession(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : "guest");


        baseDirFlowCell = PropertyDictionaryHelper.getInstance(sess).getFlowCellDirectory(req.getServerName());



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

        baseDir = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(req.getServerName(), experiment.getIdCoreFacility());

        // Now get the files that exist on the file server for this experiment
        Map requestMap = new TreeMap();
        Map directoryMap = new TreeMap();
        Map fileMap = new HashMap();
        List requestNumbers = new ArrayList<String>();
        Set folders = GetRequestDownloadList.getRequestDownloadFolders(baseDir, Request.getBaseRequestNumber(experiment.getNumber()), experiment.getCreateYear(), experiment.getCodeRequestCategory());
        StringBuffer keys = new StringBuffer();
        keys.append(experiment.getKey(""));  // add base directory
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

          String fcKey = flowCell.getCreateYear() + Constants.DOWNLOAD_KEY_SEPARATOR
              + sortDate + Constants.DOWNLOAD_KEY_SEPARATOR
              + experiment.getNumber() + Constants.DOWNLOAD_KEY_SEPARATOR
              + flowCell.getNumber() + Constants.DOWNLOAD_KEY_SEPARATOR
              + experiment.getIdCoreFacility() + Constants.DOWNLOAD_KEY_SEPARATOR
              + PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG);
          if (keys.length() > 0) {
            keys.append(":");
          }
          keys.append(fcKey);
        }
        UploadDownloadHelper.getFileNamesToDownload(sess, serverName, baseDirFlowCell, keys.toString(), requestNumbers, requestMap, directoryMap, PropertyDictionaryHelper.getInstance(sess).getProperty(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG));



        // Find the file matching the fileName passed in as a parameter
        FileDescriptor experimentFd = null;
        List directoryKeys   = (List)requestMap.get(experiment.getNumber());
        for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
          String directoryKey = (String)i1.next();
          String dirTokens[] = directoryKey.split(Constants.DOWNLOAD_KEY_SEPARATOR);
          String theDirectory = "";
          if (dirTokens.length > 1) {
            theDirectory = dirTokens[1];
          }

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
          // Insert a transfer log entry
          TransferLog xferLog = new TransferLog();
          xferLog.setFileName(experimentFd.getFileName().substring(baseDir.length() + 5));
          xferLog.setStartDateTime(new java.util.Date(System.currentTimeMillis()));
          xferLog.setTransferType(TransferLog.TYPE_DOWNLOAD);
          xferLog.setTransferMethod(TransferLog.METHOD_HTTP);
          xferLog.setPerformCompression("Y");
          xferLog.setIdRequest(experiment.getIdRequest());
          xferLog.setIdLab(experiment.getIdLab());

          experimentDir = experimentFd.getFileName().substring(0,experimentFd.getFileName().lastIndexOf('/')+1);

          in = new FileInputStream(experimentFd.getFileName());
          OutputStream out = response.getOutputStream();
          byte b[] = new byte[102400];
          int numRead = 0;
          int size = 0;
          while (numRead != -1) {
            numRead = in.read(b);
            if (numRead != -1) {
              if (!needToPreprocess) {
                  out.write(b, 0, numRead);
              }
              else {
            	  // we are going to preprocess this, save it for now
            	  byte b1[] = new byte[numRead];
            	  System.arraycopy(b, 0, b1, 0, numRead);
            	  htmlText.append(new String (b1,"UTF-8"));
              }
              size += numRead;
            }
          }
          
          // Save transfer log 
          xferLog.setFileSize(new BigDecimal(size));
          xferLog.setEndDateTime(new java.util.Date(System.currentTimeMillis()));
          sess.save(xferLog);
          
          in.close();

          if (needToPreprocess) {
        	  // remember htmlText is global
        	  preProcessIMGTags (out);
          }

          out.flush();
          out.close();

          in = null;
          out = null;
        }

        sess.flush();
        
      } else {
        response.setContentType("text/html");
        response.getOutputStream().println(
            "<html><head><title>Error</title></head>");
        response.getOutputStream().println("<body><b>");
        response.getOutputStream().println(
            "DownloadSingleFileServlet: You must have a SecurityAdvisor in order to run this command."
                + "<br>");
        response.getOutputStream().println("</body>");
        response.getOutputStream().println("</html>");
        System.out.println( "DownloadSingleFileServlet: You must have a SecurityAdvisor in order to run this command.");
      }
    } catch (Exception e) {
      HibernateSession.rollback();
      response.setContentType("text/html");
      response.getOutputStream().println(
          "<html><head><title>Error</title></head>");
      response.getOutputStream().println("<body><b>");
      response.getOutputStream().println(
          "DownloadSingleFileServlet: An exception occurred " + e.toString()
              + "<br>");
      response.getOutputStream().println("</body>");
      response.getOutputStream().println("</html>");
      System.out.println( "DownloadSingleFileServlet: An exception occurred " + e.toString());
      e.printStackTrace();
    } finally {
      try {
        secAdvisor.closeHibernateSession();
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


    if (fdFileName.endsWith(fileName) && (dir.equals(theDirectory) || (dir.length() == 0 && theDirectory.equals("upload_staging")))) {
      return fd;
    } else if (fd.getChildren() != null && fd.getChildren().size() > 0) {
      for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
        FileDescriptor childFd = (FileDescriptor)i.next();
        FileDescriptor matchingFd = recurseGetMatchingFileDescriptor(childFd, fileName, childFd.getDirectoryName());
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

private void preProcessIMGTags (OutputStream out) {
	int 					ipos = -1; 			// start of <img tag
	int						epos = -1; 			// > end of tag
	int						nxtpos = 0;			// next position in htmlText to search
	int						lenhtmlText = -1;	// size of htmlText

	lenhtmlText = htmlText.length();

	while (nxtpos < lenhtmlText) {
		// find the start of the <img tag
		ipos = htmlText.indexOf("<img",nxtpos);

		if (ipos == -1) {
			// we are done, put the rest out
			epos = lenhtmlText - 1;
			outString (htmlText,nxtpos,epos,out);
			break;
		}

		epos = htmlText.indexOf(">",ipos+4);
		if (epos == -1) {
			// assume it was the last characer
			epos = lenhtmlText - 1;
		}

		// put out everything up to the image tag
		outString (htmlText,nxtpos,ipos,out);

		// get the line
		String imgline = htmlText.substring (ipos, epos);

		// process it
		if (!processIMG (imgline,out)) {
			// not the kind of img we are interested in, output the original text here
			outString (htmlText,ipos,epos+1,out);
		}

		nxtpos = epos + 1;

	} // end of while

}

private void outString (StringBuilder theText, int startpos, int endpos, OutputStream out) {

	String theBytes = theText.substring(startpos,endpos);
	byte[] asBytes = null;

	try {
		asBytes = theBytes.getBytes("UTF-8");
		out.write(asBytes);
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private boolean processIMG (String imgline, OutputStream out) {
	boolean processed = false;

	// we are only interested in images being handled by downloadsingleservlet
	int ipos = imgline.indexOf("DownloadSingleFileServlet");
	if (ipos == -1) {
		return processed;
	}

	// get the image filename, here's an example of what the html looks like
	// <img src="https://b2b.hci.utah.edu/gnomex/DownloadSingleFileServlet.gx?requestNumber=103R&fileName=per_base_quality.png&view=Y&dir=Images">
	ipos = imgline.indexOf("fileName=");
	if (ipos == -1) {
		// not a format we can deal with
		return processed;
	}

	int epos = imgline.indexOf("&",ipos+9);
	if (epos == -1) {
		return processed;
	}

	String fileName = imgline.substring(ipos+9,epos);
	
	// figure out what type of image it is
	String imageType = "png";
	ipos = fileName.lastIndexOf('.');
	if (ipos != -1 && (ipos+1 < fileName.length()) ) {
		imageType = fileName.substring(ipos+1);
	}
	
	// get the directory
	ipos = imgline.indexOf("&dir=");
	if (ipos == -1) {
		return processed;
	}

	epos = imgline.indexOf('"',ipos+5);
	if (epos == -1) {
		return processed;
	}

	String dir = imgline.substring(ipos+5,epos);

	// get the file
	String pathname = experimentDir + dir + "/" + fileName;
	File imageFd = new File(pathname);

	// read it in
	long filesize = imageFd.length();
	byte thefile[] = new byte[(int)filesize];

    FileInputStream inf;
	try {
		inf = new FileInputStream(pathname);
		int numRead = 0;
        numRead = inf.read(thefile);
        inf.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	// convert it to base64
	byte[] encodedBytes = Base64.encodeBase64(thefile);

	// now build the <img tag
	StringBuilder imgtag = new StringBuilder (1024000);
	//imgtag.append("<img src=\"data:image/png;base64,");
	imgtag.append("<img src=\"data:image/");
	imgtag.append(imageType);
	imgtag.append(";base64,"); 
			
	imgtag.append(new String(encodedBytes));
	imgtag.append("\" />");

	// write it out
	outString (imgtag,0,imgtag.length(),out);
	processed = true;

	return processed;
}


}