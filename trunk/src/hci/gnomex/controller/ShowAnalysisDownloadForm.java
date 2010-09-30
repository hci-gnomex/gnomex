package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.security.UnknownPermissionException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.HibernateGuestSession;
import hci.gnomex.utility.PropertyHelper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class ShowAnalysisDownloadForm extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ShowAnalysisDownloadForm.class);
  
  public String SUCCESS_JSP = "/getHTML.jsp";
  
  private Integer          idAnalysis;
  private String           serverName;
  private String           baseURL;

  private boolean         createdSecurityAdvisor = false;
  private SecurityAdvisor  secAdvisor = null;
  
  

  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idAnalysis") != null) {
      idAnalysis = new Integer(request.getParameter("idAnalysis"));
    } else {
      this.addInvalidField("idAnalysis", "idAnalysis is required");
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

      // Get the analysis
      Analysis analysis = (Analysis)sess.get(Analysis.class, idAnalysis);
      if (analysis == null) {
        this.addInvalidField("no analysis", "Analysis not found");
      }
      

      if (this.isValid()) {
        // Make sure the user can read the analysis
        if (secAdvisor.canRead(analysis)) { 

          // Format an HTML page with links to download the files
          String baseDir = PropertyHelper.getInstance(sess).getAnalysisDirectory(serverName);
          Document doc = formatDownloadHTML(analysis, baseDir, baseURL);
          
          XMLOutputter out = new org.jdom.output.XMLOutputter();
          out.setOmitEncoding(true);
          this.xmlResult = out.outputString(doc);
          this.xmlResult = this.xmlResult.replaceAll("&amp;", "&");
          this.xmlResult = this.xmlResult.replaceAll("�",     "&micro");

        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to show analysis download form.");
        }

      }

      if (isValid()) {
        setResponsePage(this.SUCCESS_JSP);
      } else {
        setResponsePage(this.ERROR_JSP);
      }
    
    }catch (UnknownPermissionException e){
      log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (NamingException e){
      log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
        
    }catch (SQLException e) {
      log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
      e.printStackTrace();
      throw new RollBackCommandException(e.getMessage());
      
    } catch (Exception e) {
      log.error("An exception has occurred in ShowAnalysisDownloadForm ", e);
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
   * Format an HTML page showing download links for each of the files of this analysis
   * 
   */
  public static Document formatDownloadHTML(Analysis analysis, String baseDir, String baseURL) {
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
    title.addContent("Download Files for Analysis - " + analysis.getNumber());
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
    h.addContent("Download Analysis Files");
    maindiv.addContent(h);

    h = new Element("h2");
    h.setAttribute("class", "downloadHeader");
    h.addContent(analysis.getNumber() + " - " + analysis.getName());
    maindiv.addContent(h);

    h = new Element("h3");
    h.setAttribute("class", "downloadHint");
    h.addContent("Note to Internet Explorer users: your browser is unable to download files over 4 gigabytes (IE 6 limit is 2 gigabytes). To download large files, switch to another browser like Firefox or Opera."); 
    maindiv.addContent(h);

    
    Element tableNode = new Element("table");
    maindiv.addContent(tableNode);
    tableNode.addContent(makeHeaderRow());
    
    
    // Hash the know analysis files
    Map knownAnalysisFileMap = new HashMap();
    for(Iterator i = analysis.getFiles().iterator(); i.hasNext();) {
      AnalysisFile af = (AnalysisFile)i.next();
      knownAnalysisFileMap.put(af.getFileName(), af);
    }
    
    // Now get the files that exist on the file server for this analysis
    Map analysisMap = new TreeMap();
    Map directoryMap = new TreeMap();
    Map fileMap = new HashMap();
    List analysisNumbers = new ArrayList<String>();
    GetExpandedAnalysisFileList.getFileNamesToDownload(baseDir, analysis.getKey(), analysisNumbers, analysisMap, directoryMap);

    // Find the file matching the fileName passed in as a parameter
    AnalysisFileDescriptor analysisFd = null;
    List directoryKeys   = (List)analysisMap.get(analysis.getNumber());
    for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
      String directoryKey = (String)i1.next();
      List   theFiles     = (List)directoryMap.get(directoryKey);
      for(Iterator i2 = theFiles.iterator(); i2.hasNext();) {
        AnalysisFileDescriptor fd = (AnalysisFileDescriptor)i2.next();
        AnalysisFile af = (AnalysisFile)knownAnalysisFileMap.get(fd.getDisplayName());
        if (af != null) {
          fd.setComments(af.getComments());
        }
        
        
        Element downloadLink = new Element("A");
        downloadLink.setAttribute("href", baseURL + "/DownloadAnalysisSingleFileServlet.gx?idAnalysis=" + analysis.getIdAnalysis() + "&fileName=" + fd.getDisplayName());
        downloadLink.addContent(fd.getDisplayName());
        
        tableNode.addContent(makeRow(downloadLink, fd.getComments(), fd.getFileSizeText()));
      }
    }
    return doc;
    
  }
  
  private static Element makeHeaderRow() {
    Element row = new Element("TR");
    
    Element cell = new Element("TH");
    cell.addContent("File");
    row.addContent(cell);
    
    cell = new Element("TH");
    cell.addContent("Comments");
    row.addContent(cell);
    
    cell = new Element("TH");
    cell.addContent("Size");
    row.addContent(cell);
    
    return row;
  }

  private static Element makeRow(Element link, String comment, String fileSize) {
    Element row = new Element("TR");
    
    Element cell = new Element("TD");
    cell.setAttribute("class", "gridleft");
    cell.addContent(link);
    row.addContent(cell);
    
    cell = new Element("TD");
    cell.addContent(comment == null || comment.equals("") ? "&nbsp;" : comment);
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