package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Property;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestDownloadFilter;
import hci.gnomex.model.SeqRunType;
import hci.gnomex.model.SequenceLane;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class GetRequestDownloadList extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetRequestDownloadList.class);
  
  private RequestDownloadFilter          filter;
  private String                         includeUploadStagingDir = "Y";
  private HashMap                        slideDesignMap = new HashMap();
  private HashMap                        seqRunTypeMap = new HashMap();
  private static final String          QUALITY_CONTROL_DIRECTORY = "bioanalysis";
  
  private String                         serverName;
  private String                         baseDir;
  private String                         baseDirFlowCell;
  private SimpleDateFormat               yearFormat= new SimpleDateFormat("yyyy");
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {

    filter = new RequestDownloadFilter();
    HashMap errors = this.loadDetailObject(request, filter);
    this.addInvalidFields(errors);
    
    if (request.getParameter("includeUploadStagingDir") != null && !request.getParameter("includeUploadStagingDir").equals("")) {
      includeUploadStagingDir = request.getParameter("includeUploadStagingDir");
    }
    
    String idRequestStringList = request.getParameter("idRequestStringList");
    if (idRequestStringList != null&& !idRequestStringList.equals("")) {
      List idRequests = new ArrayList<Integer>();
      String[] keys = idRequestStringList.split(":");
      for (int i = 0; i < keys.length; i++) {
        String idRequest = keys[i];
        idRequests.add(new Integer(idRequest));
      }
      filter.setIdRequests(idRequests);
    }
    

    
    if (this.getSecAdvisor().hasPermission(SecurityAdvisor.CAN_ACCESS_ANY_OBJECT) && !filter.hasCriteria()) {
      this.addInvalidField("filterRequired", "Please enter at least one search criterion.");
    }
    
    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      
   
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      DictionaryHelper dh = DictionaryHelper.getInstance(sess);
      baseDir = dh.getMicroarrayDirectoryForReading(serverName);
      baseDirFlowCell = dh.getFlowCellDirectory(serverName);
      
      List slideDesigns = sess.createQuery("SELECT sd from SlideDesign sd ").list();
      for(Iterator i = slideDesigns.iterator(); i.hasNext();) {
        SlideDesign sd = (SlideDesign)i.next();
        slideDesignMap.put(sd.getIdSlideDesign(), sd.getName());
      }
      
      List seqRunTypes = sess.createQuery("SELECT fct from SeqRunType fct ").list();
      for(Iterator i = seqRunTypes.iterator(); i.hasNext();) {
        SeqRunType fct = (SeqRunType)i.next();
        seqRunTypeMap.put(fct.getIdSeqRunType(), fct.getSeqRunType());
      }
      
    
      StringBuffer buf = filter.getMicroarrayResultQuery(this.getSecAdvisor());
      log.debug("Query for GetRequestDownloadList (1): " + buf.toString());
      List rows1 = (List)sess.createQuery(buf.toString()).list();
      TreeMap rowMap = new TreeMap(new HybSampleComparator());
      for(Iterator i = rows1.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber = (String)row[1];
        String codeRequestCategory = (String)row[2];
        String hybNumber     = row[5] == null || row[5].equals("") ? "" : (String)row[5];
        
        String createDate    = this.formatDate((java.sql.Date)row[0]);
        String tokens[] = createDate.split("/");
        String createMonth = tokens[0];
        String createDay   = tokens[1];
        String createYear  = tokens[2];
        String sortDate = createYear + createMonth + createDay;
        
        String baseKey = createYear + "-" + sortDate + "-" + requestNumber;
        String key = baseKey + "-" + hybNumber;
        
        rowMap.put(key, row);
      }

      buf = filter.getSolexaResultQuery(this.getSecAdvisor());
      log.debug("Query for GetRequestDownloadList (2): " + buf.toString());
      List rows2 = (List)sess.createQuery(buf.toString()).list();
      for(Iterator i = rows2.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber = (String)row[1];
        String codeRequestCategory = (String)row[2];
        
        String createDate    = this.formatDate((java.sql.Date)row[0]);
        String tokens[] = createDate.split("/");
        String createMonth = tokens[0];
        String createDay   = tokens[1];
        String createYear  = tokens[2];
        String sortDate = createYear + createMonth + createDay;
        
        // The data files are always in the base request number folder,
        // not the folder with the revision number.  (example: all
        // files will be in 7633R even though request # is now 7633R1).
        String requestNumberBase = Request.getBaseRequestNumber(requestNumber);
        
        String baseKey = createYear + "-" + sortDate + "-" + requestNumber;
        
        // Now read the request directory to identify all its subdirectories
        Set folders = this.getRequestDownloadFolders(baseDir, requestNumberBase, yearFormat.format((java.sql.Date)row[0]), codeRequestCategory);
        this.hashFolders(folders, rowMap, dh, baseKey, row);
      }
      
      buf = filter.getSolexaFlowCellQuery(this.getSecAdvisor());
      log.debug("Query for get illumina flow cell: " + buf.toString());
      List flowCellRows = (List)sess.createQuery(buf.toString()).list();
      HashMap flowCellMap = new HashMap();
      for(Iterator i = flowCellRows.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber         = (String)row[0];
        String flowCellNumber        = (String)row[1];
        java.sql.Date createDate     = (java.sql.Date)row[2];
        
        List flowCellFolders = (List)flowCellMap.get(requestNumber);
        if (flowCellFolders == null) {
          flowCellFolders = new ArrayList<FlowCellFolder>();
        }
        flowCellFolders.add(new FlowCellFolder(requestNumber, flowCellNumber, createDate));
        
        flowCellMap.put(requestNumber, flowCellFolders); 
      }
      

      
      buf = filter.getQualityControlResultQuery(this.getSecAdvisor());
      log.debug("Query for GetRequestDownloadList (3): " + buf.toString());
      List rows3 = (List)sess.createQuery(buf.toString()).list();
      for(Iterator i = rows3.iterator(); i.hasNext();) {
        Object[] row = (Object[])i.next();
        
        String requestNumber = (String)row[1];
        String codeRequestCategory = (String)row[2];
        String sampleNumber     = row[11] == null || row[11].equals("") ? "" : (String)row[11];

        String createDate    = this.formatDate((java.sql.Date)row[0]);
        String tokens[] = createDate.split("/");
        String createMonth = tokens[0];
        String createDay   = tokens[1];
        String createYear  = tokens[2];
        String sortDate = createYear + createMonth + createDay;
       
        String baseKey = createYear + "-" + sortDate + "-" + requestNumber;
        String key = baseKey + "-" + this.QUALITY_CONTROL_DIRECTORY;
        
        rowMap.put(key, row);

      }
      
      boolean alt = false;
      String prevRequestNumber = "";
      Element requestNode = null;
      
    
      Document doc = new Document(new Element("RequestDownloadList"));
      
      for(Iterator i = rowMap.keySet().iterator(); i.hasNext();) {
        String key = (String)i.next();
        String[] tokens = key.split("-");
        String createYear = tokens[0];
        String resultDir = tokens[3];
        
        Object[] row = (Object[])rowMap.get(key);
        String codeRequestCategory = (String)row[2];
        String hybNumber =  (String)row[5];
        String createDate = this.formatDate((java.sql.Date)row[0]);
        Integer idRequest = row[21] != null ? (Integer)row[21] : Integer.valueOf(0);
        
        String appUserName = "";
        if (row[29] != null) {
          appUserName = (String)row[29];
        }
        if (row[28] != null) {
          if (appUserName.length() > 0) {
            appUserName += ", ";            
          }
          appUserName += (String)row[28];
        }
        
        
        String requestNumber = (String)row[1];
        if (!requestNumber.equals(prevRequestNumber)) {
          alt = !alt;    
          
          
          RequestCategory requestCategory = dh.getRequestCategoryObject(codeRequestCategory);


          requestNode = new Element("Request");
          requestNode.setAttribute("displayName", requestNumber);
          requestNode.setAttribute("requestNumber", requestNumber);
          requestNode.setAttribute("idRequest", idRequest.toString());
          requestNode.setAttribute("codeRequestCategory", codeRequestCategory);
          requestNode.setAttribute("icon", requestCategory != null && requestCategory.getIcon() != null ? requestCategory.getIcon() : "");
          requestNode.setAttribute("type", requestCategory != null && requestCategory.getType() != null ? requestCategory.getType() : "");
          requestNode.setAttribute("isSelected", "false");
          requestNode.setAttribute("state", "unchecked");
          requestNode.setAttribute("isEmpty", "Y"); // will be set to yes if any files exist for downloading
          requestNode.setAttribute("info", appUserName);
          
          doc.getRootElement().addContent(requestNode);
          
          // Show files under the root experiment directory
          String createDateString = this.formatDate((java.sql.Date)row[0]);
          addRootFileNodes(requestNode, requestNumber,  createDateString, null);

          // Crawl the upload staging directory and show its files under the root experiment directory
          if (includeUploadStagingDir.equals("Y")) {
            addRootFileNodes(requestNode, requestNumber,  createDateString, Constants.UPLOAD_STAGING_DIR);            
          }
        }

        
        Element n = new Element("RequestDownload");
        n.setAttribute("key", key);
        n.setAttribute("isSelected", "N");
        n.setAttribute("state", "unchecked");        
        n.setAttribute("altColor", new Boolean(alt).toString());
        n.setAttribute("showRequestNumber", !requestNumber.equals(prevRequestNumber) ? "Y" : "N");
        n.setAttribute("idRequest", row[21].toString());
        n.setAttribute("createDate", createDate);
        n.setAttribute("requestNumber", (String)row[1]);
        n.setAttribute("idRequest", idRequest.toString());
        n.setAttribute("codeRequestCategory", row[2] == null ? "" : (String)row[2]);
        n.setAttribute("codeApplication", row[3] == null ? "" : (String)row[3]);
        n.setAttribute("idAppUser", row[4] == null ? "" : ((Integer)row[4]).toString());
        n.setAttribute("itemNumber", row[5] == null ? "" : (String)row[5]);
        n.setAttribute("hybDate", row[6] == null || row[6].equals("") ? "" : this.formatDate((java.sql.Date)row[6]));
        n.setAttribute("extractionDate", row[7] == null || row[7].equals("") ? "" : this.formatDate((java.sql.Date)row[7]));
        n.setAttribute("hybFailed", row[8] == null ? "" : (String)row[8]);
        n.setAttribute("labelingDateSample1", row[9] == null || row[9].equals("")? "" : this.formatDate((java.sql.Date)row[9]));
        n.setAttribute("qualDateSample1", row[10] == null || row[10].equals("")? "" : this.formatDate((java.sql.Date)row[10]));
        n.setAttribute("numberSample1", row[11] == null ? "" :  (String)row[11]);
        n.setAttribute("nameSample1", row[12] == null ? "" :  (String)row[12]);
        n.setAttribute("labelingDateSample2", row[13] == null || row[13].equals("") ? "" : this.formatDate((java.sql.Date)row[13]));
        n.setAttribute("qualDateSample2", row[14] == null || row[14].equals("") ? "" : this.formatDate((java.sql.Date)row[14]));
        n.setAttribute("numberSample2", row[15] == null ? "" :  (String)row[15]);
        n.setAttribute("nameSample2", row[16] == null ? "" :  (String)row[16]);
        n.setAttribute("idLab", row[17] == null ? "" : ((Integer)row[17]).toString());
        
        String directoryName =  baseDir + "/" + createYear + "/" + Request.getBaseRequestNumber(requestNumber) + "/" + resultDir;
        n.setAttribute("fileName", directoryName);
        
        boolean isSolexaRequest = false;
        if (n.getAttributeValue("codeRequestCategory") != null && RequestCategory.isIlluminaRequestCategory(n.getAttributeValue("codeRequestCategory"))) {
          isSolexaRequest = true;
        }
        
        
        Integer idSlideDesign = row[20] == null || row[20].equals("") ? null : (Integer)row[20];
        
        String  sample1QualFailed             = row[22] == null || row[22].equals("") ? "N" : (String)row[22];
        String  sample2QualFailed             = row[23] == null || row[23].equals("") ? "N" : (String)row[23];
        String  labeledSample1LabelingFailed  = row[24] == null || row[24].equals("") ? "N" : (String)row[24];
        String  labeledSample2LabelingFailed  = row[25] == null || row[25].equals("") ? "N" : (String)row[25];
        String  extractionFailed              = row[26] == null || row[26].equals("") ? "N" : (String)row[26];
        String  extractionBypassed            = row[27] == null || row[27].equals("") ? "N" : (String)row[27];

        n.setAttribute("ownerFirstName", row[28] == null ? "" :  (String)row[28]);
        n.setAttribute("ownerLastName",  row[29] == null ? "" :  (String)row[29]);
        n.setAttribute("appUserName", appUserName);

        String seqPrepByCore = row[30] == null || row[30].equals("") ? "N" : (String)row[30];
        
        if (idSlideDesign == null && (hybNumber == null || hybNumber.equals(""))) {
            n.setAttribute("results", "sample quality");
        } else {
          if (idSlideDesign != null) {
            n.setAttribute("results", (String)slideDesignMap.get(idSlideDesign));              
          } else if (isSolexaRequest){
            n.setAttribute("results", "");
          } else {
            n.setAttribute("results", "");
          }
        }
        
        if (n.getAttributeValue("results").equals("bioanalyzer")) {
          boolean hasMaxQualDate = false;
          if (row[19] != null && !row[19].equals("")) {
            hasMaxQualDate = true;
          }
          if(hasMaxQualDate) {
            n.setAttribute("hasResults","Y"); 
          } else if (seqPrepByCore.equals("Y")) {
            n.setAttribute("status", "not yet performed");
            n.setAttribute("hasResults", "N");
          } else {
            n.setAttribute("status", "in progress");            
            n.setAttribute("hasResults","N");
          }
        } else if (isSolexaRequest) {
          n.setAttribute("hasResults", "Y"); 
          n.setAttribute("status", "");
        } else {
          if(!n.getAttributeValue("extractionDate").equals("")) {
            n.setAttribute("hasResults","Y");                       
          } else if(extractionBypassed.equals("Y")) {
            n.setAttribute("status", "bypassed scan/fe");          
            n.setAttribute("hasResults","Y");                       
          } else if(extractionFailed.equals("Y")) {
            n.setAttribute("status", "failed scan/fe");          
            n.setAttribute("hasResults","N");                       
          } else  if (n.getAttributeValue("hybFailed").equals("Y")){
            n.setAttribute("status", "failed hyb");          
            n.setAttribute("hasResults","N");                       
          } else  if (sample1QualFailed.equals("Y") || sample2QualFailed.equals("Y")){
            n.setAttribute("status", "failed QC");          
            n.setAttribute("hasResults","N");                       
          } else  if (labeledSample1LabelingFailed.equals("Y") || labeledSample2LabelingFailed.equals("Y")){
            n.setAttribute("status", "failed labeling");          
            n.setAttribute("hasResults","N");                       
          } else {
            n.setAttribute("status", "in progress");          
            n.setAttribute("hasResults","N");                       
          }     
          String sampleInfo = n.getAttributeValue("nameSample1");
          if (!n.getAttributeValue("nameSample2").equals("")) {
            if (sampleInfo.length() > 0) {
              sampleInfo += ", ";
            }
            sampleInfo += n.getAttributeValue("nameSample2");
          }
          n.setAttribute("info", sampleInfo);
        }
        
        requestNode.addContent(n);

        addExpandedFileNodes(baseDir, baseDirFlowCell, requestNode, n, requestNumber, key, codeRequestCategory, dh);

        
        
        // Add directories for flow cells
        if (isSolexaRequest) {
          List flowCellNumbers = (List)flowCellMap.get(requestNumber);
          if (flowCellNumbers != null) {
            for(Iterator i1 = flowCellNumbers.iterator(); i1.hasNext();) {
              FlowCellFolder fcFolder = (FlowCellFolder)i1.next();
              
              String theCreateDate    = this.formatDate((java.sql.Date)fcFolder.getCreateDate());
              String dateTokens[] = theCreateDate.split("/");
              String createMonth = dateTokens[0];
              String createDay   = dateTokens[1];
              String theCreateYear  = dateTokens[2];
              String sortDate = theCreateYear + createMonth + createDay;      

              String fcKey = theCreateYear + "-" + sortDate + "-" + fcFolder.getRequestNumber() + "-" + fcFolder.getFlowCellNumber() + "-" + dh.getProperty(Property.FLOWCELL_DIRECTORY_FLAG);
              String fcCodeRequestCategory = row[2] == null ? "" : (String)row[2];
              
              Element n1 = new Element("RequestDownload");
              n1.setAttribute("key", fcKey);
              n1.setAttribute("isSelected", "N");
              n1.setAttribute("state", "unchecked");
              n1.setAttribute("altColor", new Boolean(alt).toString());
              n1.setAttribute("idRequest", row[21].toString());
              n1.setAttribute("createDate", this.formatDate((java.sql.Date)row[0]));
              n1.setAttribute("requestNumber", (String)row[1]);
              n1.setAttribute("codeRequestCategory", fcCodeRequestCategory);
              n1.setAttribute("codeApplication", row[3] == null ? "" : (String)row[3]);
              n1.setAttribute("idAppUser", row[4] == null ? "" : ((Integer)row[4]).toString());
              n1.setAttribute("idLab", row[17] == null ? "" : ((Integer)row[17]).toString());
              n1.setAttribute("results", "flow cell quality report");
              n1.setAttribute("hasResults", "Y"); 
              n1.setAttribute("status", "");
              n1.setAttribute("itemNumber", fcFolder.getFlowCellNumber());
              
              requestNode.addContent(n1);
              
              addExpandedFileNodes(baseDir, baseDirFlowCell, requestNode, n1, fcFolder.getRequestNumber(), fcKey, fcCodeRequestCategory, dh);
            }
            // We only want to show the list of flow cells once
            // per request.
            flowCellMap.remove(requestNumber);
            
          }
        }
        
        
        prevRequestNumber = requestNumber;
        
      }
    
      XMLOutputter out = new org.jdom.output.XMLOutputter();
      this.xmlResult = out.outputString(doc);
    
      setResponsePage(this.SUCCESS_JSP);
    }catch (NamingException e){
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    }catch (SQLException e) {
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } catch (Exception e) {
      log.error("An exception has occurred in GetRequestDownloadList ", e);
      e.printStackTrace(System.out);
      throw new RollBackCommandException(e.getMessage());
    } finally {
      try {
        this.getSecAdvisor().closeReadOnlyHibernateSession();        
      } catch(Exception e) {
        
     }
    }
    
    return this;
  }
  
  private void hashFolders(Set folders, TreeMap rowMap, DictionaryHelper dh, String baseKey, Object[] row) {
    for(Iterator i1 = folders.iterator(); i1.hasNext();) {
      String folderName = (String)i1.next();
      if (folderName.equals(dh.getProperty(Property.QC_DIRECTORY))) {
        continue;
      }
      if (folderName.equals(Constants.UPLOAD_STAGING_DIR)) {
        continue;
      }
      String key = baseKey + "-" + folderName;
      Object[] newRow = new Object[row.length];
      for(int x = 0; x < row.length; x++) {
        newRow[x] = row[x];
      }
      newRow[5] = folderName;
      rowMap.put(key, newRow);
    }    
  }
  
  public static void addExpandedFileNodes(String baseDir,
      String baseDirFlowCell,
      Element requestNode,
      Element requestDownloadNode, 
      String requestNumber, 
      String key, 
      String codeRequestCategory, 
      DictionaryHelper dh) throws XMLReflectException {
    //
    // Get expanded file list
    //
    Map requestMap = new TreeMap();
    Map directoryMap = new TreeMap();
    List requestNumbers = new ArrayList<String>();
    GetExpandedFileList.getFileNamesToDownload(baseDir, baseDirFlowCell, key, requestNumbers, requestMap, directoryMap, dh.getProperty(Property.FLOWCELL_DIRECTORY_FLAG));
    List directoryKeys   = (List)requestMap.get(requestNumber);
    for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
      String directoryKey = (String)i1.next();
      String[] dirTokens = directoryKey.split("-");
      String directoryName = dirTokens[1];

      
      List   theFiles     = (List)directoryMap.get(directoryKey);

      // For each file in the directory
      if (theFiles != null && theFiles.size() > 0) {
        for (Iterator i2 = theFiles.iterator(); i2.hasNext();) {
          FileDescriptor fd = (FileDescriptor) i2.next();
          fd.setDirectoryName(directoryName);
          fd.excludeMethodFromXML("getChildren");
          
          Element fdNode = fd.toXMLDocument(null, fd.DATE_OUTPUT_ALTIO).getRootElement();
          fdNode.setAttribute("isSelected", "N");
          fdNode.setAttribute("state", "unchecked");
          recurseAddChildren(fdNode, fd);
          
          requestDownloadNode.addContent(fdNode);
          requestDownloadNode.setAttribute("isEmpty", "N");
          requestNode.setAttribute("isEmpty", "N");
        }
        
      } else {
        requestDownloadNode.setAttribute("isEmpty", "Y");
      }
    }
    
  }
  
  private static void recurseAddChildren(Element fdNode, FileDescriptor fd) throws XMLReflectException {
    for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
      FileDescriptor childFd = (FileDescriptor)i.next();
      
      childFd.excludeMethodFromXML("getChildren");
      Element childFdNode = childFd.toXMLDocument(null, childFd.DATE_OUTPUT_ALTIO).getRootElement();
      childFdNode.setAttribute("isSelected", "N");
      childFdNode.setAttribute("state", "unchecked");
      
      fdNode.addContent(childFdNode);
      
      
      if (childFd.getChildren() != null && childFd.getChildren().size() > 0) {
        recurseAddChildren(childFdNode, childFd);
      }
    }
    
  }
  
  
  public static Set getRequestDownloadFolders(String baseDir, String requestNumber, String createYear, String codeRequestCategory) {

    TreeSet folders = new TreeSet<String>(new FolderComparator(codeRequestCategory));
    String directoryName = baseDir + createYear + "/" + requestNumber;
    File fd = new File(directoryName);

    if (fd.isDirectory()) {
      String[] fileList = fd.list();
      for (int x = 0; x < fileList.length; x++) {
        String fileName = directoryName + "/" + fileList[x];
        File f1 = new File(fileName);
        if (f1.isDirectory()) {
          folders.add(fileList[x]);          
        }
      }
    }
    return folders;
    
  }
  
  private void addRootFileNodes(Element requestNode, String requestNumber, String createDate, String subDirectory) throws Exception {
   
    String dirTokens[] = createDate.split("/");
    String createYear  = dirTokens[2];
    
    String directoryName = baseDir + "/" + createYear + "/" + Request.getBaseRequestNumber(requestNumber) + 
                           (subDirectory != null ? "/" + Constants.UPLOAD_STAGING_DIR : "");
    File fd = new File(directoryName);
    if (fd.exists() && fd.isDirectory()) {
      String[] fileList = fd.list();
      for (int x = 0; x < fileList.length; x++) {
        String fileName = directoryName + "/" + fileList[x];
        File f1 = new File(fileName);
       
        // bypass temp files
        if (f1.getName().toLowerCase().endsWith("thumbs.db") || f1.getName().toUpperCase().startsWith(".DS_STORE") || f1.getName().startsWith("._")) {
          continue;
        } 
        
        // bypass directories
        if (f1.isDirectory()) {
          continue;
        }
        
        // Hide that the files are in the upload staging directory.  Show them in the root experiment directory instead.
        String zipEntryName = Request.getBaseRequestNumber(requestNumber) + "/" + f1.getName();

        FileDescriptor fdesc = new FileDescriptor(Request.getBaseRequestNumber(requestNumber), f1.getName(), f1, zipEntryName);
        fdesc.setDirectoryName("");
        fdesc.excludeMethodFromXML("getChildren");

        Element fdNode = fdesc.toXMLDocument(null, fdesc.DATE_OUTPUT_ALTIO).getRootElement();
        fdNode.setAttribute("isSelected", "N");
        fdNode.setAttribute("state", "unchecked");

        requestNode.addContent(fdNode);
        requestNode.setAttribute("isEmpty", "N");
      }
      
    }
    
  }
  
  public static class  FolderComparator implements Comparator, Serializable {
    private String codeRequestCategory;
    
    public FolderComparator(String codeRequestCategory) {
      this.codeRequestCategory = codeRequestCategory;
    }
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;
      
      Integer sorta1 = null;
      Integer sorta2 = null;
      if (key1.equals(QUALITY_CONTROL_DIRECTORY)) {
        sorta1 = new Integer(0);
      } else {
        sorta1 = new Integer(1);
      }
      if (key2.equals(QUALITY_CONTROL_DIRECTORY)) {
        sorta2 = new Integer(0);
      } else {
        sorta2 = new Integer(1);
      }
      
      String sortb1 = "";
      String sortb2 = "";
      Integer sortc1 = null;
      Integer sortc2 = null;
      if (RequestCategory.isMicroarrayRequestCategory(codeRequestCategory)) {        
        String tokens[] = key1.split("E");
        if (tokens.length == 2) {
          try {
            Integer.parseInt(tokens[1]);
            sortc1 = new Integer(tokens[1]);
            sortb1 = tokens[0];
          } catch (Exception e) {
            sortc1 = new Integer(0);
            sortb1 = key1;
          }
        } else {
          sortc1 = new Integer(0);
          sortb1 = key1;
        }
        tokens = key2.split("E");
        if (tokens.length == 2) {
          try {
            Integer.parseInt(tokens[1]);
            sortc2 = new Integer(tokens[1]);
            sortb2 = tokens[0];
            
          } catch(Exception e) {
            sortc2 = new Integer(2);
            sortb2 = key2;
            
          }
        } else {
          sortc2 = new Integer(2);
          sortb2 = key2;
        }
        
      } else {
        sortb1 = key1;
        sortc1 = new Integer(0);

        sortb2 = key2;
        sortc2 = new Integer(0);
      }
    
      
      
      if (sorta1.equals(sorta2)) {
        if (sortb1.equals(sortb2)) {
          return  sortc1.compareTo(sortc2);
        } else {
          return sortb1.compareTo(sortb2);
        }
      } else {
        return sorta1.compareTo(sorta2);
      }
      
    }
  }

  public static class  HybSampleComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2) {
      String key1 = (String)o1;
      String key2 = (String)o2;

      
      
      String[] tokens1 = key1.split("-", 4);
      String[] tokens2 = key2.split("-", 4);
      
      String yr1           = tokens1[0];
      String date1         = tokens1[1];
      String reqNumber1    = tokens1[2];
      String hybNumber1    = tokens1[3];
      String folder1       = tokens1[3];
      
      
      String yr2           = tokens2[0];
      String date2         = tokens2[1];
      String reqNumber2    = tokens2[2];
      String hybNumber2    = tokens2[3];
      String folder2       = tokens2[3];
      
      
      String number1 = null;
      
      
      if (hybNumber1.equals(QUALITY_CONTROL_DIRECTORY)) {
        number1 = "0";
         
      } else {
        String splitLetter = null;
        if (hybNumber1.indexOf("E") >= 0) {
          splitLetter = "E";
        } else if (hybNumber1.indexOf("X") >= 0) {
          splitLetter = "X";
        }
        if (splitLetter != null) {
          String[] hybNumberTokens1 = hybNumber1.split(splitLetter);
          number1 = hybNumberTokens1[hybNumberTokens1.length - 1];   
          try {
            new Integer(number1);
          } catch(Exception e) {
            number1 = "1";
          }
        } else {
          number1 = "1";
        }
      }
      
      
      String number2 = null;
      
      
      if (hybNumber2.equals(QUALITY_CONTROL_DIRECTORY)) {
        number2 = "0";
          
      } else {
        String splitLetter = null;
        if (hybNumber2.indexOf("E") >= 0) {
          splitLetter = "E";
        } else if (hybNumber2.indexOf("X") >= 0) {          splitLetter = "X";
        }
        
        if (splitLetter != null) {
          String[] hybNumberTokens2 = hybNumber2.split(splitLetter);
          number2 = hybNumberTokens2[hybNumberTokens2.length - 1];   
          try {
            new Integer(number2);
          } catch (Exception e) {
            number2 = "1";
          }
        } else {
          number2 = "1";
        }

      }


      if (date1.equals(date2)) {
        if (reqNumber1.equals(reqNumber2)) {    
          if (number1.equals(number2)) {
            return folder1.compareTo(folder2);
          } else {
            return new Integer(number1).compareTo(new Integer(number2));                    
          }
        } else {
          return reqNumber2.compareTo(reqNumber1);
        }  
      } else {
        return date2.compareTo(date1);
      }
              
      
      
    }
  }
  
  private static class LaneStatusInfo {
    private java.sql.Date firstCycleDate;
    private String        firstCycleFailed;
    private java.sql.Date lastCycleDate;
    private String        lastCycleFailed;
    
    public java.sql.Date getFirstCycleDate() {
      return firstCycleDate;
    }
    
    public void setFirstCycleDate(java.sql.Date firstCycleDate) {
      this.firstCycleDate = firstCycleDate;
    }
    
    public String getFirstCycleFailed() {
      return firstCycleFailed;
    }
    
    public void setFirstCycleFailed(String firstCycleFailed) {
      this.firstCycleFailed = firstCycleFailed;
    }
    
    public java.sql.Date getLastCycleDate() {
      return lastCycleDate;
    }
    
    public void setLastCycleDate(java.sql.Date lastCycleDate) {
      this.lastCycleDate = lastCycleDate;
    }
    
    public String getLastCycleFailed() {
      return lastCycleFailed;
    }
    
    public void setLastCycleFailed(String lastCycleFailed) {
      this.lastCycleFailed = lastCycleFailed;
    }
    
    
    
    
  }
  
  private static class FlowCellFolder {
    private String        requestNumber;
    private String        flowCellNumber;
    private java.sql.Date createDate;
    
    
    public FlowCellFolder(String requestNumber,
                          String flowCellNumber,
                          Date createDate) {
      super();
      this.requestNumber = requestNumber;
      this.flowCellNumber = flowCellNumber;
      this.createDate = createDate;
    }


    public java.sql.Date getCreateDate() {
      return createDate;
    }

    
    public void setCreateDate(java.sql.Date createDate) {
      this.createDate = createDate;
    }

    
    public String getFlowCellNumber() {
      return flowCellNumber;
    }

    
    public void setFlowCellNumber(String flowCellNumber) {
      this.flowCellNumber = flowCellNumber;
    }


    
    public String getRequestNumber() {
      return requestNumber;
    }


    
    public void setRequestNumber(String requestNumber) {
      this.requestNumber = requestNumber;
    }
    
  }
  
  
}