package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class GetAnalysisDownloadEstimatedSize extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysisDownloadEstimatedSize.class);
  
  private String    keysString = null;
  private static int      totalFileSize = 0;
  private String   baseDir = "";

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Get input parameters
    keysString = request.getParameter("resultKeys");
    baseDir = Constants.getAnalysisDirectory(request.getServerName());
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Map fileNameMap = new HashMap();
      long compressedFileSizeTotal = DownloadAnalysisFolderServlet.getFileNamesToDownload(baseDir, keysString, fileNameMap);
      this.xmlResult = "<DownloadEstimatedSize size='" + compressedFileSizeTotal + "'/>";
      
      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    } catch (Exception e){
      log.error("An exception has occurred in GetAnalysisDownloadEstimatedSize ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } 
    
    return this;
  }

}