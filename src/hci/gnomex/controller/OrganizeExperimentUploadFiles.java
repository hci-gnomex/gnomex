package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.model.ExperimentFile;
import hci.gnomex.model.Request;
import hci.gnomex.model.Sample;
import hci.gnomex.model.SampleExperimentFile;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.DictionaryHelper;
import hci.gnomex.utility.FileDescriptorUploadParser;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;
import java.sql.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class OrganizeExperimentUploadFiles extends GNomExCommand implements Serializable {

  // the static field for logging in Log4J
  private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OrganizeExperimentUploadFiles.class);

  private Integer idRequest;
  private String filesXMLString;
  private String experimentFileXMLString;
  private Document experimentFileDoc;
  private Document filesDoc;
  private String filesToRemoveXMLString;
  private String linkedSampleFileXMLString;
  private Document linkedSampleFileDoc;
  private Document filesToRemoveDoc;
  private FileDescriptorUploadParser parser;
  private FileDescriptorUploadParser filesToRemoveParser;
  private Document filesToUnlinkDoc;
  private String filesToUnlinkXMLString;
  private List directoryFilesToUnlink = new ArrayList();
  private List deletedSefEntries = new ArrayList();

  private String serverName;

  private DictionaryHelper dictionaryHelper = null;

  public void validate() {
  }

  public void loadCommand(HttpServletRequest request, HttpSession session) {

    if (request.getParameter("idRequest") != null && !request.getParameter("idRequest").equals("")) {
      idRequest = new Integer(request.getParameter("idRequest"));
    } else {
      this.addInvalidField("idRequest", "idRequest is required");
    }

    if (request.getParameter("filesXMLString") != null && !request.getParameter("filesXMLString").equals("")) {
      filesXMLString = request.getParameter("filesXMLString");

      StringReader reader = new StringReader(filesXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesDoc = sax.build(reader);
        parser = new FileDescriptorUploadParser(filesDoc);
      } catch (JDOMException je) {
        log.error("Cannot parse filesXMLString", je);
        this.addInvalidField("FilesLXMLString", "Invalid files xml");
      }
    }

    if (request.getParameter("experimentFileXMLString") != null && !request.getParameter("experimentFileXMLString").equals("")) {
      experimentFileXMLString = "<experimentFiles>" + request.getParameter("experimentFileXMLString") + "</experimentFiles>";

      StringReader reader = new StringReader(experimentFileXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        experimentFileDoc = sax.build(reader);
      } catch (JDOMException je) {
        log.error("Cannot parse experimentFileXMLString", je);
        this.addInvalidField("experimentFileXMLString", "Invalid experimentFileXMLString xml");
      }
    }

    if (request.getParameter("filesToRemoveXMLString") != null && !request.getParameter("filesToRemoveXMLString").equals("")) {
      filesToRemoveXMLString = "<FilesToRemove>" + request.getParameter("filesToRemoveXMLString") + "</FilesToRemove>";

      StringReader reader = new StringReader(filesToRemoveXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesToRemoveDoc = sax.build(reader);
        filesToRemoveParser = new FileDescriptorUploadParser(filesToRemoveDoc);
      } catch (JDOMException je) {
        log.error("Cannot parse filesToRemoveXMLString", je);
        this.addInvalidField("FilesToRemoveXMLString", "Invalid filesToRemove xml");
      }
    }

    if (request.getParameter("filesToUnlinkXMLString") != null && !request.getParameter("filesToUnlinkXMLString").equals("")) {
      filesToUnlinkXMLString = "<FilesToUnlink>" + request.getParameter("filesToUnlinkXMLString") + "</FilesToUnlink>";

      StringReader reader = new StringReader(filesToUnlinkXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        filesToUnlinkDoc = sax.build(reader);
      } catch (JDOMException je) {
        log.error("Cannot parse filesToUnlinkXMLString", je);
        this.addInvalidField("FilesToUnlinkXMLString", "Invalid filesToUnlink xml");
      }
    }

    if (request.getParameter("linkedSampleFileXMLString") != null && !request.getParameter("linkedSampleFileXMLString").equals("")) {
      linkedSampleFileXMLString = "<linkedSampleFiles>" + request.getParameter("linkedSampleFileXMLString") + "</linkedSampleFiles>";

      StringReader reader = new StringReader(linkedSampleFileXMLString);
      try {
        SAXBuilder sax = new SAXBuilder();
        linkedSampleFileDoc = sax.build(reader);
      } catch (JDOMException je) {
        log.error("Cannot parse linkedSampleFileXMLString", je);
        this.addInvalidField("linkedSampleFileXMLString", "Invalid linkedSampleFiles xml");
      }
    }

    serverName = request.getServerName();

  }

  public Command execute() throws RollBackCommandException {

    Session sess = null;
    ArrayList tryLater = null;
    if (filesXMLString != null) {
      try {
        sess = this.getSecAdvisor().getHibernateSession(this.getUsername());

        Request request = (Request) sess.load(Request.class, idRequest);
        String baseRequestNumber = Request.getBaseRequestNumber(request.getNumber());
        String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, request.getIdCoreFacility(), PropertyDictionaryHelper.PROPERTY_EXPERIMENT_DIRECTORY);
        baseDir += request.getCreateYear() + File.separator + Request.getBaseRequestNumber(request.getNumber());

        if (this.getSecAdvisor().canUploadData(request)) {

          parser.parse();

          // Add new directories to the file system
          for (Iterator i = parser.getNewDirectoryNames().iterator(); i.hasNext();) {
            String directoryName = (String) i.next();
            File dir = new File(baseDir + File.separator + directoryName);
            if (!dir.exists()) {
              boolean success = dir.mkdirs();
              if (!success) {
                // File was not successfully created
                throw new Exception("Unable to create directory " + directoryName);
              }

            }
          }

          // Rename files
          for (Iterator i = parser.getFilesToRenameMap().keySet().iterator(); i.hasNext();) {
            String file = (String) i.next();
            String newFileName = (String) parser.getFilesToRenameMap().get(file);
            File f1 = new File(file);
            File f2 = new File(newFileName);
            boolean success = f1.renameTo(f2);
            if (success) {
              for (Iterator k = parser.getFileNameMap().keySet().iterator(); k.hasNext();) {
                String directory = (String) k.next();
                List fileNames = (List) parser.getFileNameMap().get(directory);
                for (Iterator i1 = fileNames.iterator(); i1.hasNext();) {
                  String parserFile = (String) i1.next();
                  if (parserFile.equals(file)) {
                    fileNames.remove(parserFile);
                    fileNames.add(f2.getCanonicalPath());
                    parser.getFileNameMap().put(directory, fileNames);
                    break;
                  }
                }
              }
              // Update experiment file name if registered in the db
              String oldExpFileName = file.substring(file.indexOf(baseRequestNumber)).replace("\\", "/");
              String newExpFileName = newFileName.substring(newFileName.indexOf(baseRequestNumber)).replace("\\", "/");
              String queryBuf = "Select exp from ExperimentFile exp where fileName = :oldExpFileName";
              Query query = sess.createQuery(queryBuf);
              query.setParameter("oldExpFileName", oldExpFileName);
              List expFiles = query.list();
              if (expFiles.size() == 1) {
                ExperimentFile ef = (ExperimentFile) expFiles.get(0);
                ef.setFileName(newExpFileName);
                sess.save(ef);
              }
            } else {
              throw new Exception("Unable to rename file.  Invalid file name");
            }

          }

          // Rename Folders
          for (Iterator i = parser.getFoldersToRenameMap().keySet().iterator(); i.hasNext();) {
            String folder = (String) i.next();
            String newFolder = (String) parser.getFoldersToRenameMap().get(folder);
            File f1 = new File(baseDir + File.separator + folder);
            File f2 = new File(baseDir + File.separator + newFolder);
            f2.mkdir();
            for (Iterator j = parser.getFileNameMap().keySet().iterator(); j.hasNext();) {
              String directory = (String) j.next();
              if (directory.contains(folder + File.separator)) {
                parser.getFileNameMap().remove(directory);
                j = parser.getFileNameMap().keySet().iterator();
              }
              if (directory.equals(folder)) {
                List fileNames = (List) parser.getFileNameMap().get(directory);
                parser.getFileNameMap().remove(directory);
                parser.getFileNameMap().put(newFolder, fileNames);
                j = parser.getFileNameMap().keySet().iterator();
              }
            }
          }

          // Move files to designated folder
          tryLater = new ArrayList();
          File oldFileToDelete = null;
          for (Iterator i = parser.getFileNameMap().keySet().iterator(); i.hasNext();) {
            String directoryName = (String) i.next();
            List fileNames = (List) parser.getFileNameMap().get(directoryName);
            String targetDirName = "";

            List<String> path = Arrays.asList(directoryName.split(Pattern.quote(File.separator)));
            directoryName = "";
            for(Iterator<String> iter = path.iterator(); iter.hasNext();){
              String s = iter.next();
              if(!baseDir.contains(s)){
                directoryName += s + File.separator;
              }
            }






            for (Iterator i1 = fileNames.iterator(); i1.hasNext();) {
              String fileName = (String) i1.next();

              File sourceFile = new File(fileName);

              if (baseDir.contains(directoryName) || baseDir.contains(directoryName.subSequence(0, directoryName.length() - 1))) {
                targetDirName = baseDir + File.separator;
              } else {
                targetDirName = baseDir + File.separator + directoryName;
              }
              File targetDir = new File(targetDirName);

              if (!targetDir.exists()) {
                boolean success = targetDir.mkdirs();
                if (!success) {
                  throw new Exception("Unable to create directory " + targetDir.getCanonicalPath());
                }
              }

              // Don't try to move if the file is in the same directory

              String td = targetDir.getAbsolutePath();
              String sd = sourceFile.getAbsolutePath();
              sd = sd.substring(0, sd.lastIndexOf(File.separator));
              oldFileToDelete = new File(sd);

              if (td.equals(sd)) {
                continue;
              }

              File destFile = new File(targetDir, sourceFile.getName());
              boolean success = sourceFile.renameTo(destFile);

              // If we have renamed a file that is registered in the database
              // under the ExperimentFile table, then update the ExperimentFile
              // name
              // so that we don't do an unnecessary delete in the register files
              // servlet.
              if (success) {
                String currentExpFileName = fileName.substring(fileName.indexOf(baseRequestNumber)).replace("\\", "/"); // REMOVE
                                                                                                                        // REPLACE
                                                                                                                        // AFTER
                                                                                                                        // DEBUGGING
                String queryBuf = "Select exp from ExperimentFile exp where fileName = :currentExpFileName";
                Query query = sess.createQuery(queryBuf);
                query.setParameter("currentExpFileName", currentExpFileName);
                List expFiles = query.list();
                if (expFiles.size() == 1) {
                  String newExpFileName = targetDirName.substring(targetDirName.indexOf(baseRequestNumber)).replace("\\", "/"); // Remove
                                                                                                                                // replace
                                                                                                                                // after
                                                                                                                                // debugging
                  newExpFileName += "/" + destFile.getName();
                  newExpFileName = newExpFileName.replace("//", "/");
                  ExperimentFile ef = (ExperimentFile) expFiles.get(0);
                  ef.setFileName(newExpFileName);
                  ef.setFileSize(BigDecimal.valueOf(destFile.length()));
                  sess.save(ef);
                }
              } else {
                if (destFile.exists()) {
                  if (sourceFile.exists()) {
                    if (!sourceFile.delete()) {
                      if (sourceFile.isDirectory()) {
                        // If can't delete directory then try again after
                        // everything has been moved
                        tryLater.add(sourceFile.getAbsolutePath());
                      } else {
                        throw new Exception("Unable to move file " + fileName + " to " + targetDirName);
                      }
                    }
                  }
                } else {
                  // throw new Exception("Unable to move file " + fileName +
                  // " to " + targetDirName);
                }
              }

            }
            if (oldFileToDelete != null && oldFileToDelete.exists() && oldFileToDelete.list().length == 0) {
              try {
                oldFileToDelete.delete();
              } catch (Exception e) {
                log.warn("Unable to delete " + oldFileToDelete.getAbsolutePath() + ": ");
                e.printStackTrace();
              }
            } else {
              log.warn("Unable to delete " + oldFileToDelete.getAbsolutePath() + ": directory is not empty");
            }

          }

          // Remove files from file system
          if (filesToRemoveParser != null) {
            for (Iterator i = filesToRemoveParser.parseFilesToRemove().iterator(); i.hasNext();) {
              String fileName = (String) i.next();
              File f = new File(fileName);

              // Remove references of file in TransferLog
              String queryBuf = "SELECT tl from TransferLog tl where tl.idRequest = :idRequest AND tl.fileName like :fileName";
              Query query = sess.createQuery(queryBuf);
              query.setParameter("idRequest", idRequest);
              query.setParameter("fileName", "%" + new File(fileName).getName());
              List transferLogs = query.list();

              // Go ahead and delete the transfer log if there is just one row.
              // If there are multiple transfer log rows for this filename, just
              // bypass deleting the transfer log since it is not possible
              // to tell which entry should be deleted.
              if (transferLogs.size() == 1) {
                TransferLog transferLog = (TransferLog) transferLogs.get(0);
                sess.delete(transferLog);
              }

              // The "file" might be a directory so we have to delete all of the
              // files underneath it first
              if (f.isDirectory()) {
                deleteDir(f, fileName);
              }

              if (f.exists()) {
                boolean success = f.delete();
                if (!success) {
                  // File was not successfully deleted
                  throw new Exception("Unable to delete file " + fileName);
                } else {
                  // Delete ExperimentFile if one is registered in the db and
                  // unlink sample file if need be
                  String queryString = "Select exp from ExperimentFile exp where fileName = :fileName";
                  Query query2 = sess.createQuery(queryString);
                  String currentFileName = fileName.substring(fileName.indexOf(baseRequestNumber)).replace("\\", "/"); // REMOVE
                                                                                                                       // REPLACE
                                                                                                                       // AFTER
                                                                                                                       // DEBUGGING
                  query2.setParameter("fileName", currentFileName);
                  List expFiles = query2.list();
                  if (expFiles.size() == 1) {
                    ExperimentFile ef = (ExperimentFile) expFiles.get(0);
                    String queryBuf3 = "SELECT DISTINCT sef from SampleExperimentFile sef where sef.idExpFileRead1 = :idExperimentFile1 OR sef.idExpFileRead2 = :idExperimentFile2";
                    Query query3 = sess.createQuery(queryBuf3);
                    query3.setParameter("idExperimentFile1", ef.getIdExperimentFile());
                    query3.setParameter("idExperimentFile2", ef.getIdExperimentFile());
                    List l = query3.list();

                    if (l.size() == 1) {
                      SampleExperimentFile sef = (SampleExperimentFile) l.get(0);
                      if (sef.getIdExpFileRead1() != null && sef.getIdExpFileRead1().equals(ef.getIdExperimentFile())) {
                        sef.setIdExpFileRead1(null);
                      } else if (sef.getIdExpFileRead2() != null && sef.getIdExpFileRead2().equals(ef.getIdExperimentFile())) {
                        sef.setIdExpFileRead2(null);
                      }

                      if (sef.getIdExpFileRead1() == null && sef.getIdExpFileRead2() == null) {
                        deletedSefEntries.add(sef.getIdSampleExperimentFile());
                        sess.delete(sef);
                      } else {
                        sess.save(sef);
                      }
                    }
                    request.getFiles().remove(ef);
                    sess.delete(ef);
                    sess.flush();
                  }
                }
              }

            }
            sess.flush();
          }

          if (tryLater != null) {
            for (Iterator i = tryLater.iterator(); i.hasNext();) {
              String fileName = (String) i.next();
              File deleteFile = new File(fileName);
              if (deleteFile.exists()) {
                // Try to delete but don't throw error if unsuccessful.
                // Just leave it to user to sort out the problem.
                deleteFile.delete();
              }
            }
          }

          sess.flush();

          if (directoryFilesToUnlink.size() > 0) {
            for (Iterator i = directoryFilesToUnlink.iterator(); i.hasNext();) {
              String fileName = (String) i.next();
              String currentFileName = fileName.substring(fileName.indexOf(baseRequestNumber)).replace("\\", "/");
              String queryBuf = "Select exp from ExperimentFile exp where fileName = :currentFileName";
              Query query = sess.createQuery(queryBuf);
              query.setParameter("currentFileName", currentFileName);
              List expFiles = query.list();
              if (expFiles.size() == 1) {
                ExperimentFile ef = (ExperimentFile) expFiles.get(0);
                String queryBuf2 = "SELECT DISTINCT sef from SampleExperimentFile sef where sef.idExpFileRead1 = :idExperimentFile1 OR sef.idExpFileRead2 = :idExperimentFile2";
                Query query2 = sess.createQuery(queryBuf2);
                query2.setParameter("idExperimentFile1", ef.getIdExperimentFile());
                query2.setParameter("idExperimentFile2", ef.getIdExperimentFile());
                List l = query2.list();

                if (l.size() == 1) {
                  SampleExperimentFile sef = (SampleExperimentFile) l.get(0);
                  if (sef.getIdExpFileRead1() != null && sef.getIdExpFileRead1().equals(ef.getIdExperimentFile())) {
                    sef.setIdExpFileRead1(null);
                  } else if (sef.getIdExpFileRead2() != null && sef.getIdExpFileRead2().equals(ef.getIdExperimentFile())) {
                    sef.setIdExpFileRead2(null);
                  }

                  if (sef.getIdExpFileRead1() == null && sef.getIdExpFileRead2() == null) {
                    deletedSefEntries.add(sef.getIdSampleExperimentFile());
                    sess.delete(sef);
                  } else {
                    sess.save(sef);
                  }
                }
                request.getFiles().remove(ef);
                sess.delete(ef);
                sess.flush();
              }
            }

            sess.flush();

          }

          // Unlink experiment files from Samples
          if (filesToUnlinkDoc != null) {
            Element root = this.filesToUnlinkDoc.getRootElement();
            for (Iterator i = root.getChildren().iterator(); i.hasNext();) {
              Element fileDescriptor = (Element) i.next();

              if (fileDescriptor.getAttributeValue("idExperimentFile") != null && !fileDescriptor.getAttributeValue("idExperimentFile").equals("")) {
                Integer idExperimentFile = Integer.parseInt(fileDescriptor.getAttributeValue("idExperimentFile"));
                String queryBuf = "SELECT DISTINCT sef from SampleExperimentFile sef where sef.idExpFileRead1 = :idExperimentFile1 OR sef.idExpFileRead2 = :idExperimentFile2";
                Query query = sess.createQuery(queryBuf);
                query.setParameter("idExperimentFile1", idExperimentFile);
                query.setParameter("idExperimentFile2", idExperimentFile);
                List l = query.list();

                if (l.size() == 1) {
                  SampleExperimentFile sef = (SampleExperimentFile) l.get(0);
                  if (sef.getIdExpFileRead1() != null && sef.getIdExpFileRead1().equals(idExperimentFile)) {
                    sef.setIdExpFileRead1(null);
                  } else if (sef.getIdExpFileRead2() != null && sef.getIdExpFileRead2().equals(idExperimentFile)) {
                    sef.setIdExpFileRead2(null);
                  }

                  if (sef.getIdExpFileRead1() == null && sef.getIdExpFileRead2() == null) {
                    deletedSefEntries.add(sef.getIdSampleExperimentFile());
                    sess.delete(sef);
                  } else {
                    sess.update(sef);
                  }
                }
              }
            }
            sess.flush();
          }

          // Map existing experiment files to file names that are coming in so
          // we don't create duplicate experiment files
          HashMap expFileDictionary = new HashMap();
          if (experimentFileDoc != null) {
            Element root = this.experimentFileDoc.getRootElement();
            for (Iterator i = root.getChildren("FileDescriptor").iterator(); i.hasNext();) {
              Element fd = (Element) i.next();
              String fileName = fd.getAttributeValue("zipEntryName");
              fileName = fileName.replace("\\", "/");
              String queryBuf = "Select expFile from ExperimentFile expFile where fileName = :fileName";
              Query query = sess.createQuery(queryBuf);
              query.setParameter("fileName", fileName);
              List expFile = query.list();
              if (expFile.size() > 0) {
                ExperimentFile ef = (ExperimentFile) expFile.get(0);
                expFileDictionary.put(fileName, ef);
              }
            }

          }

          Map<String, List<Element>> sampleGroup = new TreeMap<String, List<Element>>();
          if (linkedSampleFileDoc != null) {
            Element root = this.linkedSampleFileDoc.getRootElement();

            for (Iterator i = root.getChildren().iterator(); i.hasNext();) {
              Element child = (Element) i.next();
              if (child.getName().equals("Sample")) {
                if (sampleGroup.containsKey("*||*")) {
                  List<Element> samples = sampleGroup.get("*||*");
                  samples.add(child);
                  sampleGroup.put("*||*", samples);
                } else {
                  List<Element> samples = new ArrayList<Element>();
                  samples.add(child);
                  sampleGroup.put("*||*", samples);
                }
              } else if (child.getName().equals("SampleGroup")) {
                recurseAddSamples(child, sampleGroup, child.getAttributeValue("displayName"));
              }
            }
          }

          for (Iterator i = sampleGroup.keySet().iterator(); i.hasNext();) {
            String displayName = (String) i.next();
            int fileCount = 1;
            List<Element> sampleNodes = sampleGroup.get(displayName);
            for (Iterator j = sampleNodes.iterator(); j.hasNext();) {
              Element sampleNode = (Element) j.next();
              Integer idSample = Integer.parseInt(sampleNode.getAttributeValue("idSample"));
              Sample s = (Sample) sess.load(Sample.class, idSample);
              s.setGroupName(displayName);
              sess.save(s);
              int seqRunNumber = 0;
              for (Iterator k = sampleNode.getChildren().iterator(); k.hasNext();) {
                Element seqRunNode = (Element) k.next();
                SampleExperimentFile sef = new SampleExperimentFile();
                Integer idSampleExperimentFile = null;
                if (seqRunNode.getAttributeValue("idSampleExperimentFile") != null) {
                  idSampleExperimentFile = Integer.parseInt(seqRunNode.getAttributeValue("idSampleExperimentFile"));
                }

                // If we have already deleted the sef in above code. Don't
                // bother doing anything else.
                if (deletedSefEntries.contains(idSampleExperimentFile)) {
                  continue;
                }

                seqRunNumber = seqRunNumber + 1;
                if (idSampleExperimentFile != null && !idSampleExperimentFile.equals("")) {
                  sef = (SampleExperimentFile) sess.load(SampleExperimentFile.class, idSampleExperimentFile);
                }

                fileCount = 1;
                for (Iterator l = seqRunNode.getChildren().iterator(); l.hasNext();) {
                  Element expFile = (Element) l.next();
                  ExperimentFile ef = new ExperimentFile();

                  // If it is in the dictionary use it.
                  if (expFileDictionary.containsKey(expFile.getAttributeValue("zipEntryName").replace("\\", "/"))) {
                    ef = (ExperimentFile) expFileDictionary.get(expFile.getAttributeValue("zipEntryName").replace("\\", "/"));
                  } else if (expFile.getAttributeValue("idExperimentFile") != null && !expFile.getAttributeValue("idExperimentFile").equals("")) {
                    ef = (ExperimentFile) sess.get(ExperimentFile.class, Integer.parseInt(expFile.getAttributeValue("idExperimentFile")));
                    // The experiment file may have been deleted from above code
                    if (ef == null) {
                      continue;
                    }
                  } else {
                    java.util.Date d = new java.util.Date();
                    ef.setIdRequest(this.idRequest);
                    ef.setFileName(expFile.getAttributeValue("zipEntryName").replace("\\", "/"));
                    ef.setFileSize(new BigDecimal(expFile.getAttributeValue("fileSize")));
                    ef.setCreateDate(new Date(d.getTime()));
                    sess.save(ef);
                  }
                  if (sef != null && ef != null) {
                    if (sef.getIdExpFileRead1() != null && ef.getIdExperimentFile() != null && fileCount == 1 && sef.getIdExpFileRead1().equals(ef.getIdExperimentFile())) {
                      fileCount++;
                      continue;
                    } else if (sef.getIdExpFileRead2() != null && ef.getIdExperimentFile() != null && fileCount == 2 && sef.getIdExpFileRead2().equals(ef.getIdExperimentFile())) {
                      fileCount++;
                      continue;
                    }

                  }

                  if (fileCount == 1) {
                    sef.setIdSample(idSample);
                    sef.setSeqRunNumber(seqRunNumber);
                    sef.setIdExpFileRead1(ef.getIdExperimentFile());
                    if (!k.hasNext()) {
                      sef.setIdExpFileRead2(null);
                      sess.saveOrUpdate(sef);
                    }
                  } else if (fileCount == 2) {
                    sef.setIdExpFileRead2(ef.getIdExperimentFile());
                    sess.saveOrUpdate(sef);
                  }
                  fileCount++;
                }
              }

            }
          }
          sess.flush();

          XMLOutputter out = new org.jdom.output.XMLOutputter();
          this.xmlResult = "<SUCCESS/>";
          setResponsePage(this.SUCCESS_JSP);

        } else {
          this.addInvalidField("Insufficient permissions", "Insufficient permission to organize uploaded files");
          setResponsePage(this.ERROR_JSP);
        }

      } catch (Exception e) {
        log.error("An exception has occurred in OrganizeExperimentUploadFiles ", e);
        e.printStackTrace();
        throw new RollBackCommandException(e.getMessage());

      } finally {
        try {
          if (sess != null) {
            this.getSecAdvisor().closeHibernateSession();
          }
        } catch (Exception e) {

        }
      }

    } else {
      this.xmlResult = "<SUCCESS/>";
      setResponsePage(this.SUCCESS_JSP);
    }

    return this;
  }

  private void recurseAddSamples(Element child, Map<String, List<Element>> sampleGroup, String displayName) {
    for (Iterator i = child.getChildren().iterator(); i.hasNext();) {
      Element subChild = (Element) i.next();
      if (subChild.getName().equals("Sample")) {
        if (sampleGroup.containsKey(displayName)) {
          List<Element> samples = sampleGroup.get(displayName);
          samples.add(subChild);
          sampleGroup.put(displayName, samples);
        } else {
          List<Element> samples = new ArrayList<Element>();
          samples.add(subChild);
          sampleGroup.put(displayName, samples);
        }
      } else if (subChild.getName().equals("SampleGroup")) {
        recurseAddSamples(subChild, sampleGroup, displayName + "/" + subChild.getAttributeValue("displayName"));
      }
    }
  }

  public void deleteDir(File f, String fileName) throws Exception {
    for (String file : f.list()) {
      File child = new File(fileName + File.separator + file);
      if (child.isDirectory()) {
        deleteDir(child, child.getCanonicalPath());
      } else if (!(new File(fileName + File.separator + file).delete())) {
        throw new Exception("Unable to delete file " + fileName + File.separator + file);
      } else {
        filesToRemoveParser.parseFilesToRemove().remove(fileName + File.separator + file);
        directoryFilesToUnlink.add(fileName + "/" + file);
      }

    }
    if (f.list().length == 0) {
      if (!f.delete()) {
        throw new Exception("Unable to delete file " + f.getCanonicalPath());
      }
      return;
    }

  }

}
