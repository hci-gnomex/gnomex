package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.model.RequestCategory;
import hci.gnomex.model.RequestDownloadFilter;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SeqRunType;
import hci.gnomex.model.SlideDesign;
import hci.gnomex.security.SecurityAdvisor;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.PropertyDictionaryHelper;
import hci.gnomex.utility.UploadDownloadHelper;

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
import java.util.ListIterator;
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


public class GetLinkedSampleFiles extends GNomExCommand implements Serializable {
  
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetLinkedSampleFiles.class);
  
  private Integer                        idRequest;
  private StringBuffer                   queryBuf = new StringBuffer();
  private String                         serverName;
  
  public void validate() {
  }
  
  public void loadCommand(HttpServletRequest request, HttpSession session) {
    
    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      System.out.println(request.getParameter("idRequest"));//DEBUGGING LINE
      idRequest = Integer.parseInt(request.getParameter("idRequest"));
    }
    
    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {
    
    try {
      Session sess = this.getSecAdvisor().getReadOnlyHibernateSession(this.getUsername());
      Request request = (Request) sess.load(Request.class, this.idRequest);
      
      String baseExperimentDir   = PropertyDictionaryHelper.getInstance(sess).getExperimentDirectory(serverName, request.getIdCoreFacility());
      String directoryName = baseExperimentDir + Request.getCreateYear(request.getCreateDate()) + "/";
      directoryName.replace("\\", "/");
      
      queryBuf.append("SELECT s.name, s.number, s.idSample, sef.idExpFileRead1, sef.idExpFileRead2, sef.seqRunNumber ");
      queryBuf.append("FROM Sample s LEFT JOIN s.sampleExperimentFiles as sef ");
      queryBuf.append("WHERE s.idRequest =  " + this.idRequest);
      
      List samples = sess.createQuery(queryBuf.toString()).list();
      
      Document doc = new Document(new Element("SampleList"));
      
      Integer previousSampleID = null;
      Element sampleNode = new Element("Sample");
      for(ListIterator i = samples.listIterator(); i.hasNext();) {
        Object[] row = (Object[]) i.next();
        if(previousSampleID != null && !previousSampleID.equals((Integer)row[2])) {
          sampleNode = new Element("Sample");
        }
        Element seqRunNumberNode = new Element("SeqRunNumber");
        ExperimentFile ef = null;
        File f = null;
        FileDescriptor fd = null;
        sampleNode.setAttribute("name", row[0] != null ? (String)row[0] : "");
        sampleNode.setAttribute("number", row[1] != null ? (String)row[1] : "");
        sampleNode.setAttribute("idSample", row[2] != null ? String.valueOf((Integer)row[2]) : "");
        previousSampleID = (Integer)row[2];
        
        if(row[3] != null) {
          ef = (ExperimentFile)sess.load(ExperimentFile.class, (Integer)row[3]);
          Element sefNode = new Element("FileDescriptor");
          f = new File(directoryName + ef.getFileName());
          fd = new FileDescriptor("", ef.getFileName().substring(ef.getFileName().lastIndexOf("/") + 1), f, "");
          sefNode.setAttribute("displayName", fd.getDisplayName());
          sefNode.setAttribute("fileSizeText", fd.getFileSizeText());
          sefNode.setAttribute("lastModifyDateDisplay", fd.getLastModifyDateDisplay());
          sefNode.setAttribute("idExperimentFile", String.valueOf((Integer)row[3]));
          sefNode.setAttribute("readID", "1");
          seqRunNumberNode.addContent(sefNode);
        }
      
        if(row[4] != null) {
          ef = (ExperimentFile)sess.load(ExperimentFile.class, (Integer)row[4]);
          Element sefNode = new Element("FileDescriptor");
          f = new File(directoryName + ef.getFileName());
          fd = new FileDescriptor("", ef.getFileName().substring(ef.getFileName().lastIndexOf("/") + 1), f, "");
          sefNode.setAttribute("displayName", fd.getDisplayName());
          sefNode.setAttribute("fileSizeText", fd.getFileSizeText());
          sefNode.setAttribute("lastModifyDateDisplay", fd.getLastModifyDateDisplay());
          sefNode.setAttribute("idExperimentFile", String.valueOf((Integer)row[4]));
          sefNode.setAttribute("readID", "2");
          seqRunNumberNode.addContent(sefNode);
        }
        if(seqRunNumberNode.hasChildren()) {
          sampleNode.addContent(seqRunNumberNode);
        }
        
        if(i.hasNext()) {
          Object [] nextRow = (Object [])samples.get(i.nextIndex());
          if(!((Integer)nextRow[2]).equals((Integer)row[2])) {
            doc.getRootElement().addContent(sampleNode);
          }
        } else {
          doc.getRootElement().addContent(sampleNode);
        }
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
  
}