package hci.gnomex.controller;

import hci.framework.control.Command;
import hci.gnomex.utility.*;
import hci.framework.control.RollBackCommandException;
import hci.gnomex.constants.Constants;
import hci.gnomex.model.Analysis;
import hci.gnomex.model.AnalysisFile;
import hci.gnomex.model.TransferLog;
import hci.gnomex.utility.AnalysisFileDescriptorUploadParser;
import hci.gnomex.utility.HibernateSession;
import hci.gnomex.utility.PropertyDictionaryHelper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class OrganizeAnalysisUploadFiles extends GNomExCommand implements Serializable {

    // the static field for logging in Log4J
    private static Logger LOG = Logger.getLogger(OrganizeAnalysisUploadFiles.class);

    private Integer idAnalysis;
    private String filesXMLString;
    private Document filesDoc;
    private String filesToRemoveXMLString;
    private Document filesToRemoveDoc;
    private AnalysisFileDescriptorUploadParser parser;
    private AnalysisFileDescriptorUploadParser filesToRemoveParser;

    private String serverName;

    public void validate() {
    }

    public void loadCommand(HttpServletRequest request, HttpSession session) {

        if (request.getParameter("idAnalysis") != null && !request.getParameter("idAnalysis").equals("")) {
            idAnalysis = new Integer(request.getParameter("idAnalysis"));
        } else {
            this.addInvalidField("idAnalysis", "idAnalysis is required");
        }

        if (request.getParameter("filesXMLString") != null && !request.getParameter("filesXMLString").equals("")) {
            filesXMLString = "<Files>" + request.getParameter("filesXMLString") + "</Files>";
//            System.out.println ("[OAUF] filesXMLString: " + filesXMLString);

            StringReader reader = new StringReader(filesXMLString);
            try {
                SAXBuilder sax = new SAXBuilder();
                filesDoc = sax.build(reader);
                parser = new AnalysisFileDescriptorUploadParser(filesDoc);
            } catch (JDOMException je) {
                LOG.error("Cannot parse filesXMLString", je);
                this.addInvalidField("FilesLXMLString", "Invalid files xml");
            }
        }

        if (request.getParameter("filesToRemoveXMLString") != null
                && !request.getParameter("filesToRemoveXMLString").equals("")) {
            filesToRemoveXMLString = "<FilesToRemove>" + request.getParameter("filesToRemoveXMLString")
                    + "</FilesToRemove>";
//            System.out.println ("[OAUF] filesXMLString: " + filesXMLString);

            StringReader reader = new StringReader(filesToRemoveXMLString);

            try {
                SAXBuilder sax = new SAXBuilder();
                filesToRemoveDoc = sax.build(reader);
                filesToRemoveParser = new AnalysisFileDescriptorUploadParser(filesToRemoveDoc);
            } catch (JDOMException je) {
                LOG.error("Cannot parse filesToRemoveXMLString", je);
                this.addInvalidField("FilesToRemoveXMLString", "Invalid filesToRemove xml");
            }
        }

        serverName = request.getServerName();

    }

    public Command execute() throws RollBackCommandException {

        List<String> problemFiles = new ArrayList<String>();
        Session sess = null;
        if (filesXMLString != null) {
            try {
                sess = this.getSecAdvisor().getHibernateSession(this.getUsername());

                Analysis analysis = (Analysis) sess.load(Analysis.class, idAnalysis);

                String baseDir = PropertyDictionaryHelper.getInstance(sess).getDirectory(serverName, null,
                        PropertyDictionaryHelper.PROPERTY_ANALYSIS_DIRECTORY);
                baseDir += analysis.getCreateYear();

                if (this.getSecAdvisor().canUploadData(analysis)) {
                    parser.parse();

                    // Add new directories to the file system
                    // These are directories the user created explicitly in UI Manage_Files
                    for (Iterator i = parser.getNewDirectoryNames().iterator(); i.hasNext(); ) {
                        String directoryName = (String) i.next();
                        File dir = new File(baseDir + "/" + directoryName);
                        if (!dir.exists()) {
//                            System.out.println("[OAUF] creating new directory: " + baseDir + "/" + directoryName);
                            boolean success = dir.mkdirs();
                            if (!success) {
                                // Directory not successfully created
                                throw new Exception("Unable to create directory " + directoryName);
                            }
                        }
                    }

                    // Rename files for(Iterator i = parser.getFilesToRenameMap().keySet().iterator(); i.hasNext();)
                    // These are files that were explicitly renamed (or moved) or a directory was renamed (or moved)
                    // thus all the children were too
                    Object[] keys = parser.getFilesToRenameMap().keySet().toArray();
                    for (int i = keys.length - 1; i >= 0; i--) {
                        String file = (String) keys[i];
                        File f1 = new File(file);
                        String[] contents = (String[]) parser.getFilesToRenameMap().get(file);
                        File f2 = new File(contents[0]);
                        String idFileString = contents[1];
                        String qualifiedFilePath = contents[2];
                        String displayName = contents[3];


                        if (!FileUtil.renameTo(f1,f2)) {
//                        System.out.println("[OAUF] renaming files f1: " + f1.getPath() + " f2: " + f2.getPath());
                            throw new Exception("Error Renaming File");
                        } else {
                            // Rename the files in the DB
                            if (idFileString != null) {
                                AnalysisFile af;
                                if (!idFileString.startsWith("AnalysisFile") && !idFileString.equals("")) {
//                                    System.out.println("[OAUF] loading AnalysisFile idFileString: " + idFileString);
                                    af = (AnalysisFile) sess.load(AnalysisFile.class, new Integer(idFileString));
                                    af.setFileName(displayName);
                                    af.setBaseFilePath(f2.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
                                    af.setQualifiedFilePath(qualifiedFilePath);
//                                    System.out.println("[OAUF] l 154");
                                    sess.save(af);
                                    sess.flush();
                                } else if (idFileString.startsWith("AnalysisFile") && !f2.exists()) {
                                    af = new AnalysisFile();
                                    af.setFileName(displayName);
                                    af.setBaseFilePath(f2.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
                                    af.setQualifiedFilePath(qualifiedFilePath);
//                                    System.out.println("[OAUF] l 162");
                                    sess.save(af);
                                    sess.flush();
                                } else {
                                    for (Iterator j = parser.getChildrenToMoveMap().keySet().iterator(); j.hasNext(); ) {
                                        String oldFileName = (String) j.next();
                                        String[] afParts = (String[]) parser.getChildrenToMoveMap().get(oldFileName);
                                        if (afParts[1].startsWith("AnalysisFile")) {
                                            continue;
                                        }
                                        af = (AnalysisFile) sess.load(AnalysisFile.class, new Integer(afParts[1]));
                                        af.setFileName(afParts[3]);
                                        af.setBaseFilePath(afParts[0]);

                                        String[] filePath = afParts[0].replace("\\", Constants.FILE_SEPARATOR).split(Constants.FILE_SEPARATOR);
                                        af.setQualifiedFilePath(filePath[filePath.length - 2]);
//                                        System.out.println("[OAUF] set qualfilepath: " + filePath[filePath.length - 2]);
//                                        System.out.println("[OAUF] l 178");
                                        sess.save(af);
                                        sess.flush();

                                    } // end of for
                                } // end of else
                            } // end of if
                        }  // end of else

                    }

                    List<File> directoriesToDelete = new ArrayList<>();

                    // Move files to designated folder
                    for (Iterator i = parser.getFileNameMap().keySet().iterator(); i.hasNext(); ) {

                        String directoryName = (String) i.next();

                        // Get the qualifiedFilePath (need to remove the analysis number folder from directory name)
                        String[] pathTokens = directoryName.split(Constants.FILE_SEPARATOR);
                        String qualifiedFilePath = "";
                        if (pathTokens.length > 1) {
                            qualifiedFilePath = pathTokens[1];
                        }
                        for (int i2 = 2; i2 < pathTokens.length; i2++) {
                            qualifiedFilePath += "/" + pathTokens[i2];
                        }

                        List fileNames = (List) parser.getFileNameMap().get(directoryName);

                        for (Iterator i1 = fileNames.iterator(); i1.hasNext(); ) {
                            String fileName = (String) i1.next();
                            File sourceFile = new File(fileName);
                            // don't move it it doesn't exist.
                            if (!sourceFile.exists()) {
                                continue;
                            }

                            fileName = fileName.replace("\\", Constants.FILE_SEPARATOR);
                            int lastIndex = fileName.lastIndexOf(Constants.FILE_SEPARATOR);

                            String baseFileName = fileName;
                            if (lastIndex != -1) {
                                baseFileName = fileName.substring(lastIndex);
                            }
                            Boolean duplicateUpload = fileNames.contains(baseDir + "/" + analysis.getNumber() + "/"
                                    + Constants.UPLOAD_STAGING_DIR + baseFileName);
                            String mostRecentFile = "";
                            if (duplicateUpload) {
                                mostRecentFile = (String) fileNames.get(fileNames.indexOf(baseDir + "/"
                                        + analysis.getNumber() + "/" + Constants.UPLOAD_STAGING_DIR + baseFileName));
                            }

                            // Change qualifiedFilePath if the file is registered in the db
                            // There is NO reason to do this if we haven't moved or renamed the file!!!!
                            if (parser.getFileIdMap().containsKey(fileName)) {

                                String idFileString = (String) parser.getFileIdMap().get(fileName);
//                                System.out.println("[OAUF] idFileString: " + idFileString + " fileName: " + fileName);

                                if (idFileString != null) {
                                    AnalysisFile af = new AnalysisFile();
                                    if (!idFileString.startsWith("AnalysisFile") && !idFileString.equals("")) {
                                        af = (AnalysisFile) sess.load(AnalysisFile.class, new Integer(idFileString));
                                    } else if (idFileString.startsWith("AnalysisFile")) {
                                        af = new AnalysisFile();
                                        af.setUploadDate(new java.sql.Date(System.currentTimeMillis()));
                                        af.setCreateDate(new java.sql.Date(System.currentTimeMillis()));
                                        af.setIdAnalysis(Integer.valueOf(idAnalysis));
                                        af.setFileName(new File(fileName).getName());
                                        af.setBaseFilePath(baseDir + "/" + analysis.getNumber());
                                    }

                                    if (duplicateUpload) {
//                                        System.out.println("[OAUF] duplicateUpload idFileString: " + idFileString + " fileName: " + fileName);
                                        af.setFileSize(new BigDecimal(new File(mostRecentFile).length()));
                                        Boolean firstUpload = true;
                                        while (i1.hasNext()) {
                                            String test = (String) i1.next();
                                            if (test.equals(mostRecentFile)) {
                                                i1.remove();
                                                i1 = fileNames.iterator();
                                                new File(mostRecentFile).delete();
                                                firstUpload = false;
                                                break;
                                            }
                                        }
                                        if (firstUpload) {
                                            i1 = fileNames.iterator();
                                            i1.next();
                                            i1.remove();
                                            i1 = fileNames.iterator();
                                        }
                                    } else {
                                        if (new File(fileName).exists()) {
                                            af.setFileSize(new BigDecimal(new File(fileName).length()));
                                        }
                                    }
                                    af.setQualifiedFilePath(qualifiedFilePath);
//                                    System.out.println("[OAUF] l 280 idFileString: " + idFileString + " fileName: " + fileName);
                                    sess.save(af);
                                }
                            }

                            sourceFile = sourceFile.getAbsoluteFile();
                            String targetDirName = baseDir + Constants.FILE_SEPARATOR + analysis.getNumber() + Constants.FILE_SEPARATOR + qualifiedFilePath;
                            File targetDir = new File(targetDirName);
                            targetDir = targetDir.getAbsoluteFile();

                            if (!targetDir.exists()) {
                                boolean success = targetDir.mkdirs();
                                if (!success) {
                                    throw new Exception("Unable to create directory " + targetDir.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
                                }
                            }

                            // Don't try to move if the file is in the same directory
                            String td = targetDir.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR);
                            String sd = sourceFile.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR);
                            sd = sd.substring(0, sd.lastIndexOf(Constants.FILE_SEPARATOR));

                            if (td.equals(sd)) {
                                // we should never get here, but we do....
                                continue;
                            }

                            File destFile = new File(targetDir, sourceFile.getName());

                            // if the destination file exists and is not a directory, this is bad, don't mess with it
                            // tell the user we have a problem
                            if (destFile.exists() && !destFile.isDirectory()) {
                                problemFiles.add(fileName);
//                                System.out.println("[OAUF] renameTo sourceFile **FAILED** file exists: " + sourceFile.getPath() + " targetFile: " + destFile.getPath());
                                continue;
                            }

                            // it's not there and should be a directory, make it
                            if (!destFile.exists() && sourceFile.isDirectory()) {
                                destFile.mkdirs();
                            }

                            if (sourceFile.isDirectory() && destFile.exists() && destFile.isDirectory()) {
                                // we'll delete the original directory after moving everything
                                directoriesToDelete.add(sourceFile);
                                continue;
                            }

//                            System.out.println("[OAUF] renameTo sourceFile: " + sourceFile.getPath() + " targetFile: " + destFile.getPath());
                            if (sourceFile.isDirectory() && destFile.exists() && destFile.isDirectory()) {
                                // nothing to do, the directory already exists
//                                System.out.println("[OAUF] renameTo sourceFile: " + sourceFile.getPath() + " targetFile: " + destFile.getPath() + " directory already exists");
                                continue;
                            }

                            if (!FileUtil.renameTo(sourceFile, destFile)) {
                                problemFiles.add(fileName);
                            }
                        }
                    }

                    // Remove files from file system
                    if (filesToRemoveParser != null) {
                        filesToRemoveParser.parseFilesToRemove();

                        if (filesToRemoveParser.getFilesToDeleteMap() != null) {
//                            System.out.println("[OAUF] l 333");
                            SaveAnalysis.removeDataTrackFiles(sess, this.getSecAdvisor(), analysis,
                                    filesToRemoveParser.getFilesToDeleteMap());
                        }

                        for (Iterator i = filesToRemoveParser.getFilesToDeleteMap().keySet().iterator(); i.hasNext(); ) {

                            String idFileString = (String) i.next();

                            List fileNames = (List) filesToRemoveParser.getFilesToDeleteMap().get(idFileString);

                            for (Iterator i1 = fileNames.iterator(); i1.hasNext(); ) {
                                String fileName = (String) i1.next();

//                                System.out.println("[OAUF] delete file: " + fileName);

                                // Remove references of file in TransferLog
                                String queryBuf = "SELECT tl from TransferLog tl where tl.idAnalysis = :idAnalysis AND tl.fileName like :fileName";
                                Query query = sess.createQuery(queryBuf);
                                query.setParameter("idAnalysis", idAnalysis);
                                query.setParameter("fileName", "%" + new File(fileName).getName());
                                List transferLogs = query.list();

                                // Go ahead and delete the transfer log if there is just one row.
                                // If there are multiple transfer log rows for this filename, just;
                                // bypass deleting the transfer log since it is not possible
                                // to tell which entry should be deleted.
                                if (transferLogs.size() == 1) {
                                    TransferLog transferLog = (TransferLog) transferLogs.get(0);
//                                    System.out.println("[OAUF] l 360");
                                    sess.delete(transferLog);
                                }

                                // Delete the file from the DB
                                if (idFileString != null) {
                                    AnalysisFile af;
                                    if (!idFileString.startsWith("AnalysisFile") && !idFileString.equals("")) {
                                        af = (AnalysisFile) sess.load(AnalysisFile.class, new Integer(idFileString));
//                                        System.out.println("[OAUF] trying to remove: " + af.getFileName());
                                        Set aFiles = analysis.getFiles();
//                                        System.out.println("[OAUF] removing analysisfile: " + fileName);
                                        analysis.getFiles().remove(af);
                                    }
                                }
//                                System.out.println("[OAUF] right before the flush");
                                sess.flush();

                                // Delete the file from the file system
                                File deleteFile = new File(fileName);
                                if (deleteFile.exists()) {
//                                    System.out.println("[OAUF] actual filesystem delete " + fileName);

                                    if (deleteFile.isDirectory()) {
                                        if (!deleteDir(deleteFile)) {
                                            throw new Exception("Unable to delete files");
                                        }
                                    }

                                    boolean success = new File(fileName).delete();
                                    if (!success) {
                                        // File was not successfully deleted
                                        throw new Exception("Unable to delete file " + fileName);
                                    }
                                }

//                                System.out.println("[OAUF] l 394");
                                sess.flush();
                            }
                        }
                    }

                    Comparator<File> sortByPath = new Comparator<File>() {
                        public int compare(File one, File two) {
                            return one.getAbsolutePath().compareTo(two.getAbsolutePath());
                        }
                    };
                    // sorts directories in reverse order so subdirectories are deleted first
                    directoriesToDelete.sort(sortByPath.reversed());
                    for (File directory : directoriesToDelete) {
                        if (directory.isDirectory() && directory.list().length == 0) {
                            directory.delete();
                        }
                    }

                    // clean up ghost files
                    String queryBuf = "SELECT af from AnalysisFile af where af.idAnalysis = :idAnalysis";
                    Query query = sess.createQuery(queryBuf);
                    query.setParameter("idAnalysis", idAnalysis);
                    List ghostFiles = query.list();

                    for (Iterator i = ghostFiles.iterator(); i.hasNext(); ) {
                        AnalysisFile af = (AnalysisFile) i.next();
                        String filePath = af.getBaseFilePath() + "/" + af.getQualifiedFilePath()
                                + "/" + af.getFileName();

                        File checkit = new File(filePath);
                        if (checkit.exists()) {
                            continue;
                        }
//                        System.out.println("[OAUF] remove ghost file: " + filePath);
                        analysis.getFiles().remove(af);
                    }

//                    System.out.println("[OAUF] l 430 *** at the end ***");
                    sess.flush();

                    String stagingDirectory = baseDir + Constants.FILE_SEPARATOR + analysis.getNumber() + Constants.FILE_SEPARATOR + Constants.UPLOAD_STAGING_DIR;
                    FileUtil.pruneEmptyDirectories(stagingDirectory);

                    this.xmlResult = "<SUCCESS";
                    if (problemFiles.size() > 0) {
                        String problemFileWarning = "Warning: Unable to move some files:\n" + Util.listToString(problemFiles, "\n", 5);
                        this.xmlResult += " warning=" + '"' + problemFileWarning + '"';
                    }
                        this.xmlResult += "/>";
//                    System.out.println ("[OAULF] this.xmlResult: " + this.xmlResult);
                    setResponsePage(this.SUCCESS_JSP);

                } else {
                    this.addInvalidField("Insufficient permissions", "Insufficient permission to organize uploaded files");
                    setResponsePage(this.ERROR_JSP);
                }

            } catch (Exception e) {
                this.errorDetails = Util.GNLOG(LOG, "An exception has occurred in OrganizeAnalysisUploadFiles ", e);

                throw new RollBackCommandException(e.getMessage());

                // Note: we throw a new RollBackCommandException here even though we can't reverse any file system changes that have been made
                // because we'd like to get the trace back info...
            }

        } else {
            // only get here if filesXMLString is null
            this.xmlResult = "<SUCCESS/>";
            setResponsePage(this.SUCCESS_JSP);
        }

        return this;
    }

    private Boolean deleteDir(File childFile) throws IOException {
        for (String f : childFile.list()) {
            File delFile = new File(childFile.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR) + Constants.FILE_SEPARATOR + f);
            if (delFile.isDirectory()) {
                deleteDir(delFile);
                if (!delFile.delete()) {
                    return false;
                }
            } else {
                if (!delFile.delete()) {
                    return false;
                }
            }
        }

        return true;
    }

                    }
