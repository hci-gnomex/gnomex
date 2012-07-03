package hci.gnomex.daemon;

import hci.framework.control.RollBackCommandException;
import hci.framework.model.DetailObject;
import hci.framework.utilities.XMLReflectException;
import hci.gnomex.constants.Constants;
import hci.gnomex.controller.GetExpandedAnalysisFileList;
import hci.gnomex.controller.GetExpandedFileList;
import hci.gnomex.controller.GetRequestDownloadList;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.PropertyDictionary;
import hci.gnomex.model.Request;
import hci.gnomex.utility.AnalysisFileDescriptor;
import hci.gnomex.utility.BatchDataSource;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptor;
import hci.gnomex.utility.MailUtil;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.mail.*;
import javax.mail.internet.*;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.swing.text.DateFormatter;

import org.apache.log4j.Level;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class RegisterFiles extends TimerTask {

  private static long                  fONCE_PER_DAY = 1000*60*60*24; // A day in milliseconds
  private static int                   fONE_DAY = 1;
  private static int                   wakeupHour = 2;    // Default wakupHour is 2 am
  private static int                   fZERO_MINUTES = 0;
  
  private BatchDataSource              dataSource;
  private Session                      sess;

  
  private static boolean               all = false;
  private static Integer               daysSince = null;
  private static String                serverName = "";
  private static RegisterFiles         app = null;
  
  private boolean                      runAsDaemon = false;
  
  private HashMap                      experimentFileMap;
  
  private String                       baseExperimentDir;
  private String                       baseFlowCellDir;
  private String                       baseAnalysisDir;
  private String                       flowCellDirFlag;
  private Calendar                     asOfDate;
  
  private Transaction                  tx;

  public RegisterFiles(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-wakeupHour")) {
        wakeupHour = Integer.valueOf(args[++i]);
      } else if (args[i].equals ("-runAsDaemon")) {
        runAsDaemon = true;
      } else if (args[i].equals ("-all")) {
        all = true;
      } else if (args[i].equals ("-daysSince")) {
        daysSince = Integer.valueOf(args[++i]);
      } else if (args[i].equals ("-server")) {
        serverName = args[i++];
      }
    } 

  }
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    app  = new RegisterFiles(args);
    
    // Can either be run as daemon or run once (for scheduled execution - e.g. crontab)
    if(app.runAsDaemon) {
      // Perform the task once a day at <wakeupHour>., starting tomorrow morning
      Timer timer = new Timer();
      timer.scheduleAtFixedRate(app, getWakeupTime(), fONCE_PER_DAY);       
    } else {
      app.run();
    }
  }

  @Override
  public void run() {
    Calendar calendar = Calendar.getInstance();

    try {
      org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("org.hibernate");
      log.setLevel(Level.ERROR);

      dataSource = new BatchDataSource();
      app.connect();
      
      
      app.initialize();
      app.registerExperimentFiles();
      app.registerAnalysisFiles();

    } catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      
      System.out.println(e.toString());
      e.printStackTrace();
    } finally {
    }

      
  }
  
  private void initialize() throws Exception {
    PropertyDictionaryHelper ph = PropertyDictionaryHelper.getInstance(sess);
    baseExperimentDir   = ph.getMicroarrayDirectoryForReading(serverName);
    baseFlowCellDir     = ph.getFlowCellDirectory(serverName);
    baseAnalysisDir     = ph.getAnalysisReadDirectory(serverName);
    flowCellDirFlag     = ph.getProperty(PropertyDictionary.FLOWCELL_DIRECTORY_FLAG);
    
    // Figure out how far back we should look at experiments
    
    if (all) {
      // Don't filter on date criteria of all experiments and analysis to be registered
    } else {
      asOfDate = GregorianCalendar.getInstance();
      
      // Back up today's date to correct interval
      if (daysSince != null) {
        // Argument was provided that tells use how far to look back
        asOfDate.add(Calendar.DATE, daysSince.intValue() * -1);
      } else {
        // No argument provided.  Just go back a month.
        asOfDate.add(Calendar.MONTH, -1);
      }    
    }
  }
  
  private void registerExperimentFiles() throws Exception {
    // Hash experiment files
    experimentFileMap = new HashMap();
    StringBuffer buf = new StringBuffer("SELECT ef ");
    buf.append(" FROM Request r ");
    buf.append(" JOIN r.files as ef ");
    if (asOfDate != null) {
      buf.append(" WHERE r.createDate >= '" + new SimpleDateFormat("yyyy-MM-dd").format(asOfDate.getTime()) + "'");
    }
    List results = sess.createQuery(buf.toString()).list();
    for (Iterator i = results.iterator(); i.hasNext();) {
      ExperimentFile ef = (ExperimentFile)i.next();
      List files = (List)experimentFileMap.get(ef.getIdRequest());
      if (files == null) {
        files = new ArrayList();
        experimentFileMap.put(ef.getIdRequest(), files);
      }
      files.add(ef);
    }
    
    // Get all of the experiments created on or after the as-of-date
    buf = new StringBuffer("SELECT r.number, r.createDate, r.codeRequestCategory, r.idRequest ");
    buf.append(" FROM Request r");
    if (asOfDate != null) {
      buf.append(" WHERE r.createDate >= '" + new SimpleDateFormat("yyyy-MM-dd").format(asOfDate.getTime()) + "'");
    }
    System.out.println(buf.toString());
    results = sess.createQuery(buf.toString()).list();
    
    // For each experiment
    for (Iterator i = results.iterator(); i.hasNext();) {
      Object[] row = (Object[])i.next();
      String requestNbr            = (String)row[0];
      java.util.Date createDate     = (java.util.Date)row[1];
      String codeRequestCategory   = (String)row[2];
      Integer idRequest            = (Integer)row[3];
      
      String baseRequestNumber = Request.getBaseRequestNumber(requestNbr);

      System.out.println("\n" + baseRequestNumber);
      tx = sess.beginTransaction();
      
      // Get all of the files from the file system
      Map fileMap = hashFiles(baseRequestNumber, createDate, codeRequestCategory);
      for (Iterator i1 = fileMap.keySet().iterator(); i1.hasNext();) {
        String fileName = (String)i1.next();
        FileDescriptor fd = (FileDescriptor)fileMap.get(fileName);
        System.out.println(fileName + " " + fd.getFileSizeText());
      }
      
      // Now compare to the experiment files already registered in the db
      List experimentFiles = (List)experimentFileMap.get(idRequest);
      if (experimentFiles != null) {
        for (Iterator i2 = experimentFiles.iterator(); i2.hasNext();) {
          ExperimentFile ef = (ExperimentFile)i2.next();
          FileDescriptor fd = (FileDescriptor)fileMap.get(ef.getFileName());
          
          // If we don't find the file on the file system, delete it from the db.
          if (fd == null) {
            System.out.println("WARNING - experiment file " + ef.getFileName() + " not found for " + ef.getRequest().getNumber());
            sess.delete(ef);
          } else {
            // Mark that the file system file has been found
            fd.isFound(true);
            // If the file size is different on the file system, update the ExperimentFile
            if (ef.getFileSize() == null || !ef.getFileSize().equals(BigDecimal.valueOf(fd.getFileSize()))) {
              ef.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
              sess.save(ef);
            }
          }
        }        
      }
      
      // Now add ExperimentFiles to the db for any files on the file system
      // not found in the db.
      for (Iterator i3 = fileMap.keySet().iterator(); i3.hasNext();) {
        String fileName = (String)i3.next();
        FileDescriptor fd = (FileDescriptor)fileMap.get(fileName);
        if (!fd.isFound()) {
          ExperimentFile ef = new ExperimentFile();
          ef.setIdRequest(idRequest);
          ef.setFileName(fileName);
          ef.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
          sess.save(ef);
        }
      }
      
      
      sess.flush();
      tx.commit();
    }
  }
  
  
  private void registerAnalysisFiles() throws Exception {
    // Get all of the analysis files created on or after the as-of-date
    StringBuffer buf = new StringBuffer("SELECT a ");
    buf.append(" FROM Analysis a");
    if (asOfDate != null) {
      buf.append(" WHERE a.createDate >= '" + new SimpleDateFormat("yyyy-MM-dd").format(asOfDate.getTime()) + "'");
    }
    List results = sess.createQuery(buf.toString()).list();
    
    // For each analysis
    for (Iterator i = results.iterator(); i.hasNext();) {
      Analysis analysis = (Analysis)i.next();
      
      System.out.println("\n" + analysis.getNumber());
      tx = sess.beginTransaction();
      
      // Get all of the files from the file system
      Map fileMap = hashFiles(analysis);
      for (Iterator i1 = fileMap.keySet().iterator(); i1.hasNext();) {
        String fileName = (String)i1.next();
        AnalysisFileDescriptor fd = (AnalysisFileDescriptor)fileMap.get(fileName);
        System.out.println(fileName + " " + fd.getFileSizeText());
      }
      
      // Now compare to the experiment files already registered in the db
      TreeSet newAnalysisFiles = new TreeSet(new AnalysisFileComparator());
      for (Iterator i2 = analysis.getFiles().iterator(); i2.hasNext();) {
        AnalysisFile af= (AnalysisFile)i2.next();
        String directoryName = analysis.getNumber() + "/";
        if (af.getQualifiedFilePath() != null && !af.getQualifiedFilePath().trim().equals("")) {
          directoryName += af.getQualifiedFilePath() + "/";
        }
        String qualifiedFileName = directoryName + af.getFileName();
        AnalysisFileDescriptor fd = (AnalysisFileDescriptor)fileMap.get(qualifiedFileName);
        
        // If we don't find the file on the file system, delete it from the db.
        if (fd == null) {
          System.out.println("WARNING - analysis file " + qualifiedFileName + " not found for " + af.getAnalysis().getNumber());
          // make sure the analysis file isn't linked to data track(s).  if it is, just
          // log a message and leave the analysis file alone.
          StringBuffer queryBuf = new StringBuffer();
          queryBuf.append("SELECT COUNT(*) FROM DataTrack dt ");
          queryBuf.append("JOIN dt.dataTrackFiles dtf ");
          queryBuf.append("WHERE dtf.idAnalysisFile = " +  af.getIdAnalysisFile().toString());
          Integer count = (Integer)sess.createQuery(queryBuf.toString()).uniqueResult();
          if (count != null && count > 0) {
            System.out.println("WARNING - Bypassing delete of  analysis file " + analysis.getNumber() + " " + af.getIdAnalysisFile() + " "+ af.getQualifiedFileName() + " because at least one data track is linked to it.");
            newAnalysisFiles.add(af);
          } else if (af.getComments() != null && !af.getComments().trim().equals("")) {
            System.out.println("WARNING - Bypassing delete of  analysis file " + analysis.getNumber() + " " + af.getIdAnalysisFile() + " "+ af.getQualifiedFileName() + " because there are comments stored in the db for this file.");
            newAnalysisFiles.add(af);            
          } else {
            // By not adding to newAnalysisFiles set, we are deleting this analysis file
          }
        } else {
          // Mark that the file system file has been found
          fd.isFound(true);
          newAnalysisFiles.add(af);
          // If the file size is different on the file system, update the ExperimentFile
          if (af.getFileSize() == null || !af.getFileSize().equals(BigDecimal.valueOf(fd.getFileSize()))) {
            af.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
          }
        }
      }
      
      // Now add AnalysisFiles to the db for any files on the file system
      // not found in the db.
      for (Iterator i3 = fileMap.keySet().iterator(); i3.hasNext();) {
        String fileName = (String)i3.next();
        AnalysisFileDescriptor fd = (AnalysisFileDescriptor)fileMap.get(fileName);
        if (!fd.isFound()) {
          AnalysisFile af = new AnalysisFile();
          af.setIdAnalysis(analysis.getIdAnalysis());
          af.setFileName(fd.getDisplayName());
          af.setQualifiedFilePath(fd.getQualifiedFilePath());
          af.setBaseFilePath(fd.getBaseFilePath());
          af.setFileSize(BigDecimal.valueOf(fd.getFileSize()));
          af.setBaseFilePath(baseAnalysisDir + analysis.getCreateYear() + File.separatorChar + analysis.getNumber());
          newAnalysisFiles.add(af);
        }
      }
      
      analysis.setFiles(newAnalysisFiles);
      
      sess.flush();
      tx.commit();
    }
  }
  
  private Map hashFiles(String requestNumber, java.util.Date createDate, String codeRequestCategory)  throws Exception {
    HashMap fileMap = new HashMap();
    String baseRequestNumber = Request.getBaseRequestNumber(requestNumber);
    
    // Get all of the folders in the experiment directory
    Set folders = GetRequestDownloadList.getRequestDownloadFolders(baseExperimentDir, baseRequestNumber, Request.getCreateYear(createDate), codeRequestCategory);
    for (Iterator i1 = folders.iterator(); i1.hasNext();) {
      String folderName = (String)i1.next();

      // For each folder under experiment directory, get the files (recursive)
      Map requestMap = new TreeMap();
      Map directoryMap = new TreeMap();
      List requestNumbers = new ArrayList<String>();
      GetExpandedFileList.getFileNamesToDownload(baseExperimentDir, null, Request.getKey(requestNumber, createDate, folderName), requestNumbers, requestMap, directoryMap, flowCellDirFlag);
      List directoryKeys   = (List)requestMap.get(baseRequestNumber);
      if (directoryKeys != null) {
        for(Iterator i2 = directoryKeys.iterator(); i2.hasNext();) {
          String directoryKey = (String)i2.next();
          String[] dirTokens = directoryKey.split("-");
          String directoryName = dirTokens[1];

          List   theFiles     = (List)directoryMap.get(directoryKey);

          // Hash all of the files for this experiment
          for (Iterator i3 = theFiles.iterator(); i3.hasNext();) {
            FileDescriptor fd = (FileDescriptor)i3.next();
            recurseHashFiles(fd, fileMap);
          }
        }
      }
    }
    return fileMap;
  }
  
  private static void recurseHashFiles(FileDescriptor fd, Map fileMap) throws XMLReflectException {
    if (new File(fd.getFileName()).isDirectory()) {
      for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
        FileDescriptor childFd = (FileDescriptor)i.next();
        recurseHashFiles(childFd, fileMap);
      }
      
    } else {
      fileMap.put(fd.getZipEntryName(), fd);
    }
    
  }
  
  public static void recurseHashFiles(AnalysisFileDescriptor fd, Map fileMap) throws XMLReflectException {
    if (new File(fd.getFileName()).isDirectory()) {
      for(Iterator i = fd.getChildren().iterator(); i.hasNext();) {
        AnalysisFileDescriptor childFd = (AnalysisFileDescriptor)i.next();
        recurseHashFiles(childFd, fileMap);
      }
    } else {
      fileMap.put(fd.getZipEntryName(), fd);
    }
  }

  
  private HashMap hashFiles(Analysis analysis) throws Exception {
    HashMap fileMap = new HashMap();
    
    Map analysisMap = new TreeMap();
    Map directoryMap = new TreeMap();
    List analysisNumbers = new ArrayList<String>();
    GetExpandedAnalysisFileList.getFileNamesToDownload(baseAnalysisDir, analysis.getKey(), analysisNumbers, analysisMap, directoryMap, false);
    
    for(Iterator i = analysisNumbers.iterator(); i.hasNext();) {
      String analysisNumber = (String)i.next();
      List directoryKeys   = (List)analysisMap.get(analysisNumber);

      // For each directory of analysis
      boolean firstDirForAnalysis = true;
      int unregisteredFileCount = 0;
      for(Iterator i1 = directoryKeys.iterator(); i1.hasNext();) {
        
        String directoryKey = (String)i1.next();
        List   theFiles     = (List)directoryMap.get(directoryKey);

        // Hash all of the files for this experiment
        for (Iterator i3 = theFiles.iterator(); i3.hasNext();) {
          AnalysisFileDescriptor fd = (AnalysisFileDescriptor)i3.next();
          recurseHashFiles(fd, fileMap);
        }
      }
    }
    return fileMap;
  }
  
 
  private static Date getWakeupTime(){
    Calendar tomorrow = new GregorianCalendar();
    tomorrow.add(Calendar.DATE, fONE_DAY);
    Calendar result = new GregorianCalendar(
      tomorrow.get(Calendar.YEAR),
      tomorrow.get(Calendar.MONTH),
      tomorrow.get(Calendar.DATE),
      wakeupHour,
      fZERO_MINUTES
    );
    return result.getTime();
  } 
  
  
  private void connect()
  throws Exception
  {
    sess = dataSource.connect();
  }
  
  private void disconnect() 
  throws Exception {
    sess.close();
  }   
  


  // Bypassed dtd validation when reading data sources.
  public class DummyEntityRes implements EntityResolver
  {
      public InputSource resolveEntity(String publicId, String systemId)
              throws SAXException, IOException
      {
          return new InputSource(new StringReader(" "));
      }

  } 
  

  
  public static class ExperimentFileComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2)  {
      ExperimentFile ef1 = (ExperimentFile)o1;
      ExperimentFile ef2 = (ExperimentFile)o2;
      
      if (ef1.getIdExperimentFile() == null || ef2.getIdExperimentFile() == null) {
        return ef1.getFileName().compareTo(ef2.getFileName());
      } else {
        return ef1.getIdExperimentFile().compareTo(ef2.getIdExperimentFile());
      }
    }
  }
  
  public static class AnalysisFileComparator implements Comparator, Serializable {
    public int compare(Object o1, Object o2)  {
      AnalysisFile ef1 = (AnalysisFile)o1;
      AnalysisFile ef2 = (AnalysisFile)o2;
      
      if (ef1.getIdAnalysisFile() == null || ef2.getIdAnalysisFile() == null) {
        return ef1.getQualifiedFileName().compareTo(ef2.getQualifiedFileName());
      } else {
        return ef1.getIdAnalysisFile().compareTo(ef2.getIdAnalysisFile());
      }
    }
  }
  
}
