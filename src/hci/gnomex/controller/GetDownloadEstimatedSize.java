package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;


public class GetDownloadEstimatedSize extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetDownloadEstimatedSize.class);
  
  private String    keysString = null;
  private String    includeTIF = "N";
  private String    includeJPG = "N";
  private String    serverName = null;

  private String    baseDir;
  private String    baseDirFlowCell;

  
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
    serverName         = request.getServerName();
  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDirFlowCell = PropertyDictionaryHelper.getInstance(sess).getFlowCellDirectory(serverName);
      
      
      Map fileNameMap = new HashMap();      
      long compressedFileSizeTotal = DownloadResultsServlet.getFileNamesToDownload(sess, serverName, baseDirFlowCell, keysString, fileNameMap, includeTIF.equals("Y"), includeJPG.equals("Y"), dh.getPropertyDictionary(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG));
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
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();
      } catch (Exception e) {
      }
    }
    
    return this;
  }

}