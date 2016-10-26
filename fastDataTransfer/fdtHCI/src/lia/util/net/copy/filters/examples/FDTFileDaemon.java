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

/**
 * Moves the files from the temporary download location to their final resting place
 * Builds the list of files to add to the TransferLog table
 * Deletes the files/directories from the temporary download location
 *
 */

public class FDTFileDaemon implements Postprocessor {

    public void postProcessFileList(ProcessorInfo processorInfo, Subject peerSubject, Throwable downCause, String downMessage) throws Exception {

        String softlinksDirPath = "/scratch/fdtswap/fdt_sandbox_gnomex";
        String taskFileDirectory = "";
        long currentLastActivity;


        System.out.println("Number of files: " + processorInfo.fileList.length);
        System.out.println("Destination dir: " + processorInfo.destinationDir);

        for (int i = 0; i < processorInfo.fileList.length; i++) {

            // list all the files
            final String inFilename = processorInfo.destinationDir + File.separator + processorInfo.fileList[i];
            System.out.println(inFilename);
        }

        String infoPath = processorInfo.destinationDir;
        System.out.println("infoPath: (1) " + infoPath);
        int ipos = infoPath.lastIndexOf("/");
        System.out.println("ipos: " + ipos);

        String finalDirectory = infoPath.substring(0, ipos);

        infoPath = infoPath.substring(0, ipos + 1) + "info";
        System.out.println("infoPath: (2) " + infoPath);

        File currentFile = new File(infoPath);

        // This is what the info file contains:
        // Started: 2016-10-07 12:46:59
        // LastActivity: 0
        // SourceDirectory: /scratch/fdtswap/fdt_sandbox_gnomex/a0e0c0ea-76d6-454b-ba7f-9be51c0f03de/A3643
        // TargetDirectory: /Repository/AnalysisData/2016/A3643/upload_staging

        BufferedReader br = new BufferedReader(new FileReader(currentFile));
        String line = null;
        int lineCount = 4;
        currentLastActivity = -1;
        String sourceDirectory = "";
        String targetDirectory = "";
        while ((line = br.readLine()) != null) {
            if (line.indexOf("LastActivity:") == 0) {
                currentLastActivity = Long.parseLong(line.substring(14));
            } else if (line.indexOf("SourceDirectory:") == 0) {
                sourceDirectory = line.substring(17);
            } else if (line.indexOf("TargetDirectory:") == 0) {
                targetDirectory = line.substring(17);
            }
            lineCount--;
            if (lineCount == 0) {
                // Don't look at more than 4 lines
                break;
            }
        }
        br.close();

        System.out.println("LastActivity=" + currentLastActivity);
        System.out.println("SourceDirectory=" + sourceDirectory);
        System.out.println("TargetDirectory=" + targetDirectory);

        if (currentLastActivity == -1 || sourceDirectory.length() == 0 || targetDirectory.length() == 0) {
            System.out.println(getTimeStamp() + "Error: unable to process info file due to missing parameters.");
            System.out.println("LastActivity=" + currentLastActivity);
            System.out.println("SourceDirectory=" + sourceDirectory);
            System.out.println("TargetDirectory=" + targetDirectory);
            return;
        }

        boolean isFinished = false;
        isFinished = processFile(sourceDirectory, targetDirectory);

        // delete the last of the directories
        deleteDirectory(processorInfo.destinationDir);

        // and the info file
        currentFile.delete();

        // and the temporary directory
        deleteDirectory(finalDirectory);
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


    public boolean processFile(String sourceDirectory, String targetDirectory) throws NumberFormatException, IOException {

        return moveFiles(sourceDirectory, targetDirectory, true);
    }


    public boolean moveFiles(String sourceDir, String targetDir, boolean isTopLevel) {
        System.out.println("[moveFiles] source: " + sourceDir + " target: " + targetDir);

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

            // If this is a directory then don't move. Make a recursive call to moveFiles.
            // If files exist within the source directory, the target directory will be
            // created at that point
            if (f.isDirectory()) {
                moveFiles(sourceDir + File.separator + f.getName(), targetFileName, false);
                continue;
            }


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
                    System.out.println("[moveFiles] rename failed, trying mv: " + f.getCanonicalPath() + " " + targetFileName);
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

                // Java rename and "mv" commend didn't work.  Now we just
                // throw up out hands and do a copy.
                if (!success) {
                    operation = "copy";
                    // If move doesn't work then do a copy/delete
                    FileChannel in = null;
                    FileChannel out = null;

                    try {
                        System.out.println("[moveFiles] mv failed, trying copy: " + f.getCanonicalPath() + " " + targetFileName);
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

}
