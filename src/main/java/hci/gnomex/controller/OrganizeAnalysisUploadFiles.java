package hci.gnomex.controller;

import hci.framework.control.Command;import hci.gnomex.utility.Util;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import hci.gnomex.utility.Util;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

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

        String status = null;
        int[] nlines = {0};
        Session sess = null;
        ArrayList tryLater = null;
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
                            boolean success = dir.mkdirs();
                            if (!success) {
                                // Directory not successfully created
                                throw new Exception("Unable to create directory " + directoryName);
                            }
                        }
                    }

                    // Rename files for(Iterator i = parser.getFilesToRenameMap().keySet().iterator(); i.hasNext();)
                    // These are files that were explicitly renamed or a directory was renamed thus all the children were too
                    Object[] keys = parser.getFilesToRenameMap().keySet().toArray();
                    for (int i = keys.length - 1; i >= 0; i--) {
                        String file = (String) keys[i];
                        File f1 = new File(file);
                        String[] contents = (String[]) parser.getFilesToRenameMap().get(file);
                        File f2 = new File(contents[0]);
                        String idFileString = contents[1];
                        String qualifiedFilePath = contents[2];
                        String displayName = contents[3];

                        if (!Util.renameTo(f1,f2)) {
                            throw new Exception("Error Renaming File");
                        } else {
                            // Rename the files in the DB
                            if (idFileString != null) {
                                AnalysisFile af;
                                if (!idFileString.startsWith("AnalysisFile") && !idFileString.equals("")) {
                                    af = (AnalysisFile) sess.load(AnalysisFile.class, new Integer(idFileString));
                                    af.setFileName(displayName);
                                    af.setBaseFilePath(f2.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
                                    af.setQualifiedFilePath(qualifiedFilePath);
                                    sess.save(af);
                                    sess.flush();
                                } else if (idFileString.startsWith("AnalysisFile") && !f2.exists()) {
                                    af = new AnalysisFile();
                                    af.setFileName(displayName);
                                    af.setBaseFilePath(f2.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
                                    af.setQualifiedFilePath(qualifiedFilePath);
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

                                        sess.save(af);
                                        sess.flush();

                                    }
                                }
                            }
                        }

                    }

                    // Move files to designated folder
                    tryLater = new ArrayList();
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
                            if (parser.getFileIdMap().containsKey(fileName)) {

                                String idFileString = (String) parser.getFileIdMap().get(fileName);

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
                                    sess.save(af);
                                }
                            }
                            sess.flush();

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
                                continue;
                            }

                            File destFile = new File(targetDir, sourceFile.getName());
                            if (!destFile.exists() && sourceFile.isDirectory()) {
                                destFile.mkdirs();
                            }

                            boolean success = Util.renameTo(sourceFile,destFile);

                            // If the rename didn't work, check to see if the destination file was created, if so
                            // delete the source file.
                            // NOTE: Nothing should ever end up in tryLater now things are processed in filesystem order
                            if (!success) {
                                if (destFile.exists()) {
                                    if (sourceFile.exists()) {
                                            if (sourceFile.isDirectory()) {
                                                // If can't delete directory then try again after everything has been moved
                                                tryLater.add(sourceFile.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
                                            } else {
                                                status = Util.addProblemFile(status,fileName,nlines);
                                            }
                                    }
                                } else {
                                    status = Util.addProblemFile(status,fileName,nlines);
                                }
                            }
                        }
                    }

                    // Remove files from file system
                    if (filesToRemoveParser != null) {
                        filesToRemoveParser.parseFilesToRemove();

                        if (filesToRemoveParser.getFilesToDeleteMap() != null) {
                            SaveAnalysis.removeDataTrackFiles(sess, this.getSecAdvisor(), analysis,
                                    filesToRemoveParser.getFilesToDeleteMap());
                        }

                        for (Iterator i = filesToRemoveParser.getFilesToDeleteMap().keySet().iterator(); i.hasNext(); ) {

                            String idFileString = (String) i.next();

                            List fileNames = (List) filesToRemoveParser.getFilesToDeleteMap().get(idFileString);

                            for (Iterator i1 = fileNames.iterator(); i1.hasNext(); ) {
                                String fileName = (String) i1.next();

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
                                    sess.delete(transferLog);
                                }

                                // Delete the file from the DB
                                if (idFileString != null) {
                                    AnalysisFile af;
                                    if (!idFileString.startsWith("AnalysisFile") && !idFileString.equals("")) {
                                        af = (AnalysisFile) sess.load(AnalysisFile.class, new Integer(idFileString));
                                        Set aFiles = analysis.getFiles();
                                        analysis.getFiles().remove(af);
                                    }
                                }
                                sess.flush();

                                // Delete the file from the file system
                                if (new File(fileName).exists()) {
                                    File deleteFile = new File(fileName);

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

                                sess.flush();
                            }
                        }
                    }

                    if (tryLater != null) {
                        for (Iterator i = tryLater.iterator(); i.hasNext(); ) {
                            String fileName = (String) i.next();
                            System.out.println("[OAUF] trylater file: " + fileName + " idAnalysis: " + idAnalysis);
                            File deleteFile = new File(fileName);
                            if (deleteFile.exists()) {
                                // Try to delete but don't throw error if unsuccessful.
                                // Just leave it to user to sort out the problem.
                                // Directory probably contains files we couldn't move (because they're already there)
                                deleteFile.delete();

                            }
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

                        if (!new File(filePath).exists()) {
                            analysis.getFiles().remove(af);
                        }
                    }

                    sess.flush();

                    // get rid of empty upload_staging directory
                    deleteEmptyUploadStagingDirs(baseDir + Constants.FILE_SEPARATOR + analysis.getNumber() + Constants.FILE_SEPARATOR + Constants.UPLOAD_STAGING_DIR);

                    XMLOutputter out = new org.jdom.output.XMLOutputter();
                    this.xmlResult = "<SUCCESS";
                    if (status != null) {
                        this.xmlResult += " warning= \"" + status;
                        this.xmlResult += "\"/>";
                    } else {
                        this.xmlResult += "/>";
                    }
//                    System.out.println ("[OAULF] this.xmlResult: " + this.xmlResult);

                    setResponsePage(this.SUCCESS_JSP);

                } else {
                    this.addInvalidField("Insufficient permissions", "Insufficient permission to organize uploaded files");
                    setResponsePage(this.ERROR_JSP);
                }

            } catch (Exception e) {
                this.errorDetails = Util.GNLOG(LOG,"An exception has occurred in OrganizeAnalysisUploadFiles ", e);

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

    private void deleteEmptyUploadStagingDirs(String filePath) {
        File f = new File(filePath);
        if (f.exists()) {
            String[] files = f.list();
            if (f.list().length == 0) {
                f.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    File fChild = new File(f.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR) + Constants.FILE_SEPARATOR + files[i]);
                    if (fChild.isDirectory()) {
                        deleteEmptyUploadStagingDirs(fChild.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR));
                    }
                }
                // if after going through the list the file list is now empty then delete the file
                if (f.list().length == 0) {
                    f.delete();
                }
            }
        }

    }


}
