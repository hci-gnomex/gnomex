package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class GetAnalysisDownloadEstimatedSize extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetAnalysisDownloadEstimatedSize.class);
  
  private String    keysString = null;
  private static int      totalFileSize = 0;

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    // Get input parameters
    keysString = request.getParameter("resultKeys");
    
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
      Map fileNameMap = new HashMap();
      long fileSizeTotal = DownloadAnalysisFolderServlet.getFileNamesToDownload(keysString, fileNameMap);
      int estimatedCompressedSize = new Double(fileSizeTotal / 2.5).intValue();
      this.xmlResult = "<DownloadEstimatedSize size='" + estimatedCompressedSize + "'/>";
      
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