/*
 * FDTFileDaemon -- moves the files to the correct /Repository location
 *                  builds the list of files to add to the TransferLog table
 *                  cleans up the temporary download directory
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import javax.security.auth.Subject;

import lia.util.net.copy.filters.Postprocessor;
import lia.util.net.copy.filters.ProcessorInfo;

import lia.util.net.common.Config;

/**
 * Moves the files from the temporary download location to their final resting place
 * Builds the list of files to add to the TransferLog table
 * Deletes the files/directories from the temporary download location
 *
 * @author tmaness
 */

public class FDTFileDaemon implements Postprocessor {

    private static final Config config = Config.getInstance();

    public void postProcessFileList(ProcessorInfo processorInfo, Subject peerSubject, Throwable downCause, String downMessage) throws Exception {

        StringBuilder transferLogInfo = new StringBuilder(8 * 1024000);
        String softlinksDirPath = "";

        boolean downLoad = true;
        boolean upLoad = false;

        boolean finishedOK = false;
        if (downMessage == null && downCause == null) {
            finishedOK = true;
        }

        if (processorInfo != null && processorInfo.fileList != null) {
            downLoad = false;
            upLoad = true;
        }

        boolean haveProcessorInfo = true;
        if (processorInfo == null) {
            haveProcessorInfo = false;

            // do we have a directory
            String ourDestinationDir = config.getDestinationDir();
            System.out.println ("[FDTFileDaemon] ourDestinationDir: " + ourDestinationDir);
        }

        System.out.println("downLoad: " + downLoad + " upLoad: " + upLoad + " haveProcessorInfo: " + haveProcessorInfo);
        if (downMessage != null) {
            System.out.println("--------------> Reason for failure: " + downMessage + "<---------------");
        }

        String whenItHappened = getTimeStamp();

        String downInfo = "fdtDownloadInfoFile";
        String uploadInfo = "fdtUploadInfoFile";
        if (downLoad) {

            String theReason = "";
            if (downMessage != null) {
                theReason = downMessage;
            }

            String lastline = "";
            // read the fdtDownloadInfoFile
            try {
                BufferedReader br = new BufferedReader(new FileReader("./fdtDownloadInfoFile"));

                String line = null;

                int nxtLine = 0;
                while ((line = br.readLine()) != null) {
                    // we just want the last line
                    lastline = line;
                }  // end of while
                br.close();

                System.out.println("fdtDownload name of transferlog file: " + lastline);
            } catch (IOException e1) {
                System.out.println("ERROR reading ./fdtDownloadInfoFile: " + e1);
            }

            int ipos = lastline.indexOf("_");
            String uuid = lastline.substring(ipos);

            // move the transferloginfofile to the correct directory
            String sourceFileName = "./" + lastline;
            String targetFileName = "/home/fdt/transferlog/" + lastline;
            File sf = new File(sourceFileName);

            // if the file exists in /home/fdt/transferlog get rid of it first
            File tf = new File(targetFileName);
            if (tf.exists()) {
                tf.delete();
            }
            boolean success = sf.renameTo(new File(targetFileName));
            if (!success) {
                System.out.println("ERROR: couldnt rename " + sourceFileName + " to " + targetFileName);
            }

            // append the current time and downMessage to the fdtDownloadInfoFile
            BufferedWriter bw = new BufferedWriter(new FileWriter("./fdtDownloadInfoFile",true));
            bw.write(whenItHappened + "\n");
            bw.write(theReason + "\n");
            bw.flush();
            bw.close();

            // move fdtDownloadInfoFile to the /home/fdt/run directory
            String runsourceFileName = "./fdtDownloadInfoFile";
            String runtargetFileName = "/home/fdt/run/fdtDownloadInfoFile" + uuid;
            File rsf = new File(runsourceFileName);

            // if the file exists in /home/fdt/run get rid of it first
            File rtf = new File(runtargetFileName);
            if (rtf.exists()) {
                rtf.delete();
            }

            // *********** copy the file where it belongs ***************
            success = copyFile(runsourceFileName, runtargetFileName);

            if (!success) {
                System.out.println("ERROR: couldnt copy " + runsourceFileName + " to " + runtargetFileName);
            }

            // we are done
            return;
        }

        if (upLoad && finishedOK) {
            // *** uploading ***
            System.out.println("Number of files: " + processorInfo.fileList.length);
            System.out.println("Destination dir: " + processorInfo.destinationDir);

            for (int i = 0; i < processorInfo.fileList.length; i++) {

                // list all the files
                final String inFilename = processorInfo.destinationDir + File.separator + processorInfo.fileList[i];
                System.out.println(inFilename);
            }
        }

        String infoPath = processorInfo.destinationDir;
        int ipos = infoPath.lastIndexOf("/");

        infoPath = infoPath.substring(0, ipos + 1) + uploadInfo;

        File currentFile = new File(infoPath);

        // This is what the uploadInfo file contains:
/*
1        SourceDirectory:/scratch/fdtswap/fdt_sandbox_gnomex/bb18275a-46bf-4e03-8f29-1576267421ca/12362R
2        TargetDirectory:/Repository/MicroarrayData/2016/12362R/upload_staging
3        EmailAddress:
4        Remote ipAddress:155.100.224.222
5        idAppUser:2316
6        idRequest:31126
7        idLab:1125
8        idAnalysis:
9        transferLogFile:fdtUploadTransferLog_bb18275a-46bf-4e03-8f29-1576267421ca
*/
        BufferedReader br = new BufferedReader(new FileReader(currentFile));
        String line = null;
        int lineCount = 9;

        String[] theLines = new String[lineCount];

        int nxtLine = 0;
        while ((line = br.readLine()) != null) {
            // we want everything after the colon
            String[] pieces = line.split(":");
            theLines[nxtLine] = pieces[1];
            nxtLine++;
            lineCount--;
            if (lineCount == 0) {
                // Don't look at more than 'lineCount' lines
                break;
            }
        }  // end of while
        br.close();

        if (nxtLine < 9) {
            System.out.println("WARNING: info file is too short!");
        }
        String sourceDirectory = theLines[0];
        String targetDirectory = theLines[1];
        String emailAddress = theLines[2];
        String remoteIP = theLines[3];
        String idAppUser = theLines[4];
        String idRequest = theLines[5];
        String idLab = theLines[6];
        String idAnalysis = theLines[7];
        String transferLogFile = theLines[8];

        System.out.println("Contents of info file:");
        for (int ii = 0; ii <= 8; ii++) {
            System.out.println("" + ii + ": " + theLines[ii]);
        }

        // file format ok?

        if (!finishedOK) {
            // had an error uploading, get rid of everything

            // and we are done
            return;
        }

        boolean isFinished = false;
        isFinished = processFile(theLines, transferLogInfo);

        // delete the last of the directories
//        deleteDirectory(processorInfo.destinationDir);

        // and finally the info file
//        currentFile.delete();
    }

    public boolean deleteDirectory(String dirPath) {
        File thisFile = new File(dirPath);
        if (thisFile.isDirectory()) {
            File[] contents = thisFile.listFiles();
            for (File f : contents) {
                deleteDirectory(f.getAbsolutePath());
            }
        }
        boolean success = thisFile.delete();

        if (!success) {
            System.out.println(getTimeStamp() + "ERROR - Unable to delete " + dirPath);
        }

        return success;
    }


    public boolean processFile(String[] theLines, StringBuilder transferLogInfo) throws NumberFormatException, IOException {

        boolean ok = moveFiles(theLines[0], theLines[1], true, theLines, transferLogInfo);
        BufferedWriter tlOut = new BufferedWriter(new FileWriter("/home/fdt/transferlog/" + theLines[8]));
        tlOut.write(transferLogInfo.toString());
        tlOut.flush();
        tlOut.close();
        return ok;
    }


    public boolean moveFiles(String sourceDir, String targetDir, boolean isTopLevel, String[] theLines, StringBuilder transferLogInfo) {
//    System.out.println ("[moveFiles] source: " + sourceDir + " target: " + targetDir);

        String UPLOAD_STAGING_DIR = "upload_staging";

        boolean isFinished = false;
        boolean fileMoved = false;

        // Look in the directory for file
        File stagingDir = new File(sourceDir);
        if (!stagingDir.exists()) {
            System.out.println(getTimeStamp() + "ERROR - source directory does not exist: " + sourceDir);
            return true;
        }

        if (stagingDir.listFiles() == null) {
            System.out.println(getTimeStamp() + "ERROR - Empty file list. Possible permission error on source directory " + sourceDir);
            return true;
        }

        // Create the target directory and any req'd parent directories if the don't exist.
        if (!new File(targetDir).exists()) {
            File dir = new File(targetDir);
            boolean success = dir.mkdirs();
            if (!success) {
                System.out.println("Error: unable to create target directory: " + targetDir);
            }
        }


        File[] fileList = stagingDir.listFiles();
        for (int x = 0; x < fileList.length; x++) {
            File f = fileList[x];

            String targetFileName = targetDir + File.separator + f.getName();

            String theFileSize = "0";
            if (f.exists()) {
                theFileSize = "" + f.length();
            }
            if (f.isDirectory()) {
                moveFiles(sourceDir + File.separator + f.getName(), targetFileName, false, theLines, transferLogInfo);
                continue;
            }

            // add the information to the transferlog file
            transferLogInfo.append("insert into TransferLog values (" + "0,'upload','fdt',now(),now()," + "'" +
                    targetFileName + "'," + theFileSize + ",'Y'," + theLines[7] + ',' + theLines[5] + ',' + theLines[6] + ',' +
                    theLines[2] + "','" + theLines[3] + "'," + theLines[4] + ",null);\n");

            // Try to move to file.  If we can't move it, copy then delete it.
            String operation = "move";
            boolean success = f.renameTo(new File(targetFileName));
            if (success) {
                fileMoved = true;
            } else {
                // Java rename didn't work, possibly because we are trying to
                // move file across file systems.  Try "mv" command instead.
                operation = "mv";
                try {
//            System.out.println ("[moveFiles] rename failed, trying mv: " + f.getCanonicalPath() + " " + targetFileName);
                    Process process = Runtime.getRuntime().exec(new String[]{"mv", f.getCanonicalPath(), targetFileName});
                    process.waitFor();
                    process.destroy();

                    // Find out if the move worked
                    if (!new File(targetFileName).exists()) {
                        success = false;
                    } else {
                        success = true;
                        fileMoved = true;
                    }
                } catch (Exception e) {
                    success = false;
                }

                // Java rename and "mv" command didn't work.  Now we just
                // throw up out hands and do a copy.
                if (!success) {
                    operation = "copy";
                    // If move doesn't work then do a copy/delete
                    FileChannel in = null;
                    FileChannel out = null;

                    try {
//              System.out.println ("[moveFiles] mv failed, trying copy: " + f.getCanonicalPath() + " " + targetFileName);
                        in = new FileInputStream(f).getChannel();
                        File outFile = new File(targetFileName);
                        out = new FileOutputStream(outFile).getChannel();
                        in.transferTo(0, in.size(), out);
                        in.close();
                        in = null;
                        out.close();
                        out = null;
                        f.delete();
                        success = true;
                    } catch (Exception e) {
                        System.out.println("[moveFiles] copy failed! targetFileName: " + targetFileName);
                        success = false;
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception e) {
                            }
                        }
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception e) {
                            }
                        }

                    }

                }
            }

            if (success) {
                System.out.println(getTimeStamp() + operation + " " + f.getAbsolutePath() + " to " + targetDir);
                fileMoved = true;
            }
        }
        return isFinished;
    }

    public String getTimeStamp() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(new Date()) + " ";
    }

    public boolean copyFile(String sourceFileName, String targetFileName) {
        boolean success = true;

        File f = new File(sourceFileName);

        FileChannel in = null;
        FileChannel out = null;

        try {
            in = new FileInputStream(f).getChannel();
            File outFile = new File(targetFileName);
            out = new FileOutputStream(outFile).getChannel();
            in.transferTo(0, in.size(), out);
            in.close();
            in = null;
            out.close();
            out = null;
            f.delete();
            success = true;
        } catch (Exception e) {
            System.out.println("[moveFiles] copy failed! targetFileName: " + targetFileName);
            success = false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }

        }
        return success;
    }

}
