package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class GetDownloadEstimatedSize extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetDownloadEstimatedSize.class);
  
  private String    keysString = null;
  private String    includeTIF = "N";
  private String    includeJPG = "N";
  private static int      totalFileSize = 0;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Get input parameters
    keysString = request.getParameter("resultKeys");
    if (request.getParameter("includeTIF") != null
        && !request.getParameter("includeTIF").equals("")) {
      includeTIF = request.getParameter("includeTIF");
    }
    if (request.getParameter("includeJPG") != null
        && !request.getParameter("includeJPG").equals("")) {
      includeJPG = request.getParameter("includeJPG");
    }
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Map fileNameMap = new HashMap();
      long fileSizeTotal = DownloadResultsServlet.getFileNamesToDownload(keysString, fileNameMap, includeTIF.equals("Y"), includeJPG.equals("Y"));
      int estimatedCompressedSize = new Double(fileSizeTotal / 2.5).intValue();
      this.xmlResult = "<DownloadEstimatedSize size='" + estimatedCompressedSize + "'/>";
      
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    } catch (Exception e){
      log.error("An exception has occurred in GetProject ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } 
    
    return this;
  }

}