package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.FlowCell;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class ShowRequestDownloadForm extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowRequestDownloadForm.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";
  
  private Integer          idRequest;
  private String           serverName;
  private String           baseURL;

  private boolean         createdSecurityAdvisor = false;
  private SecurityAdvisor  secAdvisor = null;
  
  

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }
    
    serverName = request.getServerName();
    
    baseURL =  (request.isSecure() ? "https://" : "http://") + serverName + request.getContextPath();
    
  }

  public Command execute() throws RollBackCommandException {
    Session sess = null;
    try {
      
      sess = HibernateGuestSession.currentGuestSession(getUsername());

      // Get security advisor, create one hasn't already been created for this session.
      secAdvisor = this.getSecAdvisor();
      if (secAdvisor == null) {
        secAdvisor = SecurityAdvisor.create(sess, this.getUsername());          
        createdSecurityAdvisor = true;
      }

      // Get the experiment
      Request experiment = (Request)sess.get(Request.class, idRequest);
      if (experiment == null) {
        this.addInvalidField("no experiment", "Request not found");
      }
      

      if (this.isValid()) {
        // Make sure the user can read the experiment
        if (secAdvisor.canRead(experiment)) { 

          // Format an HTML page with links to download the files
          String baseDir = PropertyHelper.getInstance(sess).getMicroarrayDirectoryForReading(serverName);
          String baseDirFlowCell = PropertyHelper.getInstance(sess).getFlowCellDirectory(serverName);
          Document doc = formatDownloadHTML(sess, experiment, baseDir, baseDirFlowCell, baseURL);
          
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding(true);
          this.xmlResult = out.outputString(doc);
          this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
          this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");

        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to show experiment download form.");
        }

      }

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowRequestDownloadForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowRequestDownloadForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowRequestDownloadForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowRequestDownloadForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        if (sess != null) {
          HibernateGuestSession.closeGuestSession();
        }
      } catch(Exception e) {
        
      }
    }
    
    return this;
  }
  
  /***
   * Format an HTML page showing download links for each of the files of this experiment
   * 
   */
  public static Document formatDownloadHTML(Session sess, Request experiment, String baseDir, String baseDirFlowCell, String baseURL) {
    Element root = new Element("HTML");
    Document doc = new Document(root);

    Element head = new Element("HEAD");
    root.addContent(head);

    Element link = new Element("link");
    link.setAttribute("rel", "stylesheet");
    link.setAttribute("type", "text/css");
    link.setAttribute("href", Constants.REQUEST_FORM_CSS);
    head.addContent(link);

    Element title = new Element("TITLE");
    title.addContent("Download Files for Request - " + experiment.getNumber());
    head.addContent(title);

    Element body = new Element("BODY");
    root.addContent(body);

    Element outerDiv = new Element("DIV");
    outerDiv.setAttribute("id", "container");
    body.addContent(outerDiv);

    Element maindiv = new Element("DIV");
    maindiv.setAttribute("id", "containerForm");
    outerDiv.addContent(maindiv);
    
    Element img = new Element("img");
    img.setAttribute("src", "images/navbar.png");
    maindiv.addContent(img);
    
    Element h = new Element("h1");
    h.setAttribute("class", "downloadHeader");
    h.addContent("Download Experiment Files");
    maindiv.addContent(h);

    h = new Element("h2");
    h.setAttribute("class", "downloadHeader");
    h.addContent(experiment.getNumber());
    maindiv.addContent(h);

    h = new Element("h3");
    h.setAttribute("class", "downloadHint");
    h.addContent("Note to Internet Explorer users: your browser is unable to download files over 4 gigabytes (IE 6 limit is 2 gigabytes). To download large files, switch to another browser like Firefox or Opera."); 
    maindiv.addContent(h);
    
    // Now get the files that exist on the file server for this experiment
    Set folders = GetRequestDownloadList.getRequestDownloadFolders(baseDir, Request.getBaseRequestNumber(experiment.getNumber()), experiment.getCreateYear());
    for(Iterator i = folders.iterator(); i.hasNext();) {
      String folder = (String)i.next();
    
      Map requestMap = new TreeMap();
      Map directoryMap = new TreeMap();
      Map fileMap = new HashMap();
      List requestNumbers = new ArrayList<String>();
      GetExpandedFileList.getFileNamesToDownload(baseDir, baseDirFlowCell, experiment.getKey(folder), requestNumbers, requestMap, directoryMap, PropertyHelper.getInstance(sess).getProperty(Property.FLOWCELL_DIRECTORY_FLAG));
      addDownloadTable(baseURL, maindiv, folder, requestMap, directoryMap, experiment.getNumber(), experiment.getIdRequest(), null);
      
      if (i.hasNext()) {
        Element br = new Element("br");
        maindiv.addContent(br);
      }
    }
    
    Element br = new Element("br");
    maindiv.addContent(br);
    
    for(Iterator i = DownloadSingleFileServlet.getFlowCells(sess, experiment).iterator(); i.hasNext();) {
      FlowCell flowCell      =  (FlowCell)i.next();
      
      String theCreateDate    = flowCell.formatDate((java.sql.Date)flowCell.getCreateDate());
      String dateTokens[] = theCreateDate.split("/");
      String createMonth = dateTokens[0];
      String createDay   = dateTokens[1];
      String theCreateYear  = dateTokens[2];
      String sortDate = theCreateYear + createMonth + createDay;    
      
      String fcKey = flowCell.getCreateYear() + "-" + sortDate + "-" + experiment.getNumber() + "-" + flowCell.getNumber() + "-" + PropertyHelper.getInstance(sess).getProperty(Property.FLOWCELL_DIRECTORY_FLAG);
      
      Map requestMap = new TreeMap();
      Map directoryMap = new TreeMap();
      Map fileMap = new HashMap();
      List requestNumbers = new ArrayList<String>();
      GetExpandedFileList.getFileNamesToDownload(baseDir, baseDirFlowCell, fcKey, requestNumbers, requestMap, directoryMap, PropertyHelper.getInstance(sess).getProperty(Property.FLOWCELL_DIRECTORY_FLAG));
      addDownloadTable(baseURL, maindiv, flowCell.getNumber(), requestMap, directoryMap, experiment.getNumber(), experiment.getIdRequest(), flowCell.getIdFlowCell());
      
    }
    

    return doc;
    
  }
  
  private static void addDownloadTable(String baseURL, Element maindiv, String folder, Map requestMap, Map directoryMap, String requestNumber, Integer idRequest, Integer idFlowCell) {
    Element tableNode = new Element("table");
    maindiv.addContent(tableNode);
    Element caption = new Element("caption");
    caption.addContent((idFlowCell != null ? "Flow Cell " : "") + folder);
    tableNode.addContent(caption);
    tableNode.addContent(makeHeaderRow());
    
    FileDescriptor experimentFd = null;
    List directoryKeys   = (List)requestMap.get(requestNumber);
    if (directoryKeys != null) {
      for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
        String directoryKey = (String)i1.next();
        String dirTokens[] = directoryKey.split("-");
        List   theFiles     = (List)directoryMap.get(directoryKey);
        for(Iterator i2 = theFiles.iterator(); i2.hasNext();) {
          FileDescriptor fd = (FileDescriptor)i2.next();
          fd.setDirectoryName(dirTokens[1]);
          
          String dirParm = fd.getDirectoryName() != null && !fd.getDirectoryName().equals("") ? "&dir=" + fd.getDirectoryName() : "";
          String flowCellParm = idFlowCell != null  ? "&idFlowCell=" + idFlowCell : "";
          Element downloadLink = new Element("A");
          downloadLink.setAttribute("href", baseURL + "/DownloadSingleFileServlet.gx?idRequest=" + idRequest + "&fileName=" + fd.getDisplayName() + dirParm + flowCellParm);
          downloadLink.addContent(fd.getDisplayName());
          
          tableNode.addContent(makeRow(downloadLink, fd.getFileSizeText()));
        }
      }
    }
    
  }
  
  
  private static Element makeHeaderRow() {
    Element row = new Element("TR");
    
    Element cell = new Element("TH");
    cell.addContent("File");
    row.addContent(cell);

    cell = new Element("TH");
    cell.addContent("Size");
    row.addContent(cell);
    
    return row;
  }

  private static Element makeRow(Element link, String fileSize) {
    Element row = new Element("TR");
    
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridleft");
    cell.addContent(link);
    row.addContent(cell);

    cell = new Element("TD");
    cell.addContent(fileSize);
    row.addContent(cell);
    
    return row;
  }
  
 

  /**
   *  The callback method called after the loadCommand, and execute methods,
   *  this method allows you to manipulate the HttpServletResponse object prior
   *  to forwarding to the result JSP (add a cookie, etc.)
   *
   *@param  request  The HttpServletResponse for the command
   *@return          The processed response
   */
  public HttpServletResponse setResponseState(HttpServletResponse response) {
    return response;
  } 
  
  /**
   *  The callback method called after the loadCommand and execute methods
   *  allowing you to do any post-execute processing of the HttpSession. Should
   *  be used to add/remove session data resulting from the execution of this
   *  command
   *
   *@param  session  The HttpSession
   *@return          The processed HttpSession
   */
  public HttpSession setSessionState(HttpSession session) {
    if (createdSecurityAdvisor) {
      session.setAttribute(SecurityAdvisor.SECURITY_ADVISOR_SESSION_KEY, secAdvisor);      
    }
    return session;
  }


  
}