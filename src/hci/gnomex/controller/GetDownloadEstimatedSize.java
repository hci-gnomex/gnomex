package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;

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

  private String    baseDir;
  private String    baseDirFlowCell;

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
    baseDir         = Constants.getMicroarrayDirectoryForReading(request.getServerName());
    baseDirFlowCell = Constants.getFlowCellDirectory(request.getServerName());
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Map fileNameMap = new HashMap();      
      long compressedFileSizeTotal = DownloadResultsServlet.getFileNamesToDownload(baseDir, baseDirFlowCell, keysString, fileNameMap, includeTIF.equals("Y"), includeJPG.equals("Y"));
      this.xmlResult = "<DownloadEstimatedSize size='" + compressedFileSizeTotal + "'/>";
      
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