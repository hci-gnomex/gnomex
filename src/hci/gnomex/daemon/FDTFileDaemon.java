package hci.gnomex.daemon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FDTFileDaemon {
  private static  long    maxWaitMin = (60 * 47) + 55;      // Minutes to wait before ceasing to process a task file 
                                                            // (and deleting the file). Currently set so that the task
                                                            // file will be deleted five minutes before inactive folder
  private static  int     sleepInterval = 3000;             // Number of milliseconds to sleep between monitoring files
  private static  long    deleteFolderCheckWaitMin = 60;    // Number of minutes to sleep between monitoring files
  private static  long    lastDeleteFolderCheckTime = 0;    // Time of last delete folder check
  private static  long    delFolderInactiveMin = 60 * 48;   // Minutes before deleting inactive folders from softlinks_directory
  private static  String  softlinksDirPath = "C:\\GNomExSoftLinks";
  private static  String  taskFileDirectory = "";
  private static  long    currentLastActivity;
  private static  File    currentFile;

  /**
   * @param args
   */
  public static void main(String[] args) {

    try {
      // Default task file path/name
      taskFileDirectory = new java.io.File(".").getCanonicalPath() + File.separator + "tasks";
      for (int i = 0; i < args.length; i++) {
        if (args[i].equals("-sleepSeconds")) {
          sleepInterval = Integer.valueOf(args[++i])*1000;
        } else if (args[i].equals("-maxWaitMin")) {
          maxWaitMin = Integer.valueOf(args[++i]);
        } else if (args[i].equals("-taskFilePath")) {
          taskFileDirectory = args[++i];
        } else if (args[i].equals("-softlinksDirPath")) {
          softlinksDirPath = args[++i];
        } else if (args[i].equals("-delFolderInactiveMin")) {
          delFolderInactiveMin = Integer.valueOf(args[++i]);
        } 
      }     
      
      SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");     
      System.out.println(f.format(new Date(System.currentTimeMillis())) + " FDTFileMonitor started");

      while (true) {
        Thread.sleep (sleepInterval);

        // When the task wakes up traverse the files in the task directory and process each task
        File tasksDir = new File(taskFileDirectory);
        if (!tasksDir.exists()) {
          System.out.println(getTimeStamp() + "ERROR - task file directory does not exist.");
          return;
        }
        File[] taskFileList = tasksDir.listFiles();
        for( int x = 0; x < taskFileList.length; x++) {
          currentFile = taskFileList[x];
          String fileName = currentFile.getName();
          BufferedReader br = new BufferedReader(new FileReader(currentFile));
          String line = null;
          int lineCount = 4;
          currentLastActivity = -1;
          String sourceDirectory = "";
          String targetDirectory = "";     
          while ((line = br.readLine()) != null) {
            if(line.indexOf("LastActivity:")==0) {
              currentLastActivity = Long.parseLong(line.substring(14));
            } else if(line.indexOf("SourceDirectory:")==0) {
              sourceDirectory = line.substring(17);
            } else if(line.indexOf("TargetDirectory:")==0) {
              targetDirectory = line.substring(17);
            }
            lineCount--;
            if (lineCount == 0) {
              // Don't look at more than 4 lines
              break;
            }
          }
          br.close();
          if(currentLastActivity == -1 || sourceDirectory.length()==0 || targetDirectory.length()==0) {
            System.out.println(getTimeStamp() + "Error: unable to process file due to missing parameters."+targetDirectory);                        
            System.out.println("LastActivity="+currentLastActivity);
            System.out.println("SourceDirectory="+sourceDirectory);   
            System.out.println("TargetDirectory="+targetDirectory);              
          } else {
            boolean isFinished = false;
            isFinished = processFile(sourceDirectory, targetDirectory);
            // Terminate daemon if it has gone X minutes (maxWaitMin) with no activity
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis- currentLastActivity > maxWaitMin * 60 * 1000) {
              System.out.println(getTimeStamp() + "Reached max wait time for task " + fileName);
              isFinished = true;
            }
            if(isFinished) {
              // If finished then delete this task file
              currentFile.delete();
            }
          }
        }

        // After processing all tasks, see if it's time to run Delete Folder Check
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastDeleteFolderCheckTime > deleteFolderCheckWaitMin * 60 * 1000) {
          lastDeleteFolderCheckTime = currentTimeMillis;
          File softLinksDir = new File(softlinksDirPath);
          if (!softLinksDir.exists()) {
            System.out.println(getTimeStamp() + "ERROR - softlinks_directory does not exist.");
            return;
          }
          
          File[] folders = softLinksDir.listFiles();
          for( int x = 0; x < folders.length; x++) {
            File thisFile = folders[x];
            // There shouldn't be any regular files in this folder, we'll ignore them
            if(thisFile.isDirectory()) {
              // Check the last modified date to see if this one's been around long enough
              //System.out.println(thisFile.getName() + ":" + currentTimeMillis + ":" + thisFile.lastModified() + ":" + delFolderInactiveMin);
              if(currentTimeMillis - thisFile.lastModified() > delFolderInactiveMin * 60 * 1000) {       
                System.out.println(getTimeStamp() + "Deleting " + thisFile.getCanonicalPath() 
                    + " Last Modified: " + f.format(new Date(thisFile.lastModified())));                  
                deleteDirectory(thisFile.getAbsolutePath());
              }
            }
          }
        }        
      }         
    } catch (IOException e) {
      System.out.println(getTimeStamp() + "IOException: " + e.getMessage());
    } catch (InterruptedException e) {
      System.out.println(getTimeStamp() + "InterruptedException: " + e.getMessage());
    }
  }
  
  private static boolean deleteDirectory(String dirPath)
  {
    File thisFile = new File(dirPath);
    if(thisFile.isDirectory()) {
      File[] contents = thisFile.listFiles();
      for(File f: contents) {
        deleteDirectory(f.getAbsolutePath());
      }
    }
    boolean success = thisFile.delete();

    if (!success) {
      System.out.println(getTimeStamp() + "ERROR - Unable to delete " + dirPath);
    }
    
    return success;
  }


  private static boolean processFile(String sourceDirectory, String targetDirectory) throws NumberFormatException, IOException {
    //System.out.println("LastActivity="+currentLastActivity);
    //System.out.println("SourceDirectory="+sourceDirectory);   
    //System.out.println("TargetDirectory="+targetDirectory); 
    if(currentLastActivity==0) {
      // If lastActivity still zero then initialize to current time
      currentLastActivity = System.currentTimeMillis();
      updateLastActivity(currentLastActivity);
    }
    return moveFiles(sourceDirectory, targetDirectory);

  }

  private static void updateLastActivity(long lastActivity) throws NumberFormatException, IOException {
    BufferedReader br = new BufferedReader(new FileReader(currentFile));
    String line = null;

    // Construct a new file that will later be renamed to the original filename.
    String currentFileName = currentFile.getAbsolutePath();
    String tmpFileName = currentFileName.substring(0, currentFileName.length()-4) + ".tmp";
    File tempFile = new File(tmpFileName); 
    PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

    while ((line = br.readLine()) != null) {
      if(line.indexOf("LastActivity:")==0) {
        pw.println("LastActivity: "+lastActivity);
      } else {
        pw.println(line);
      }
      pw.flush();
    } 
    pw.close();
    br.close();

    //Delete the original file
    if (!currentFile.delete()) {
      System.out.println(getTimeStamp() + "Error in updateLastActivity: could not delete original file");
      tempFile.delete();
      return;
    }

    //Rename the new file to the filename the original file had.
    if (!tempFile.renameTo(currentFile))
      System.out.println(getTimeStamp() + "Could not rename file");  
    currentFile = new File(currentFileName);;
  }


  private static boolean moveFiles(String sourceDir, String targetDir) {
    boolean isFinished = false;
    boolean fileMoved = false;
    // Look in the directory for file
    File stagingDir = new File(sourceDir);
    if (!stagingDir.exists()) {
      System.out.println(getTimeStamp() + "ERROR - source directory does not exist.");
      return true;
    }
    
    if (stagingDir.listFiles() == null) {
      System.out.println(getTimeStamp() + "ERROR - Empty file list. Possible permission error on source directory " + sourceDir);
      return true;
    }

    File[] fileList = stagingDir.listFiles();
    for( int x = 0; x < fileList.length; x++) {
      File f = fileList[x];

      // Ignore files that start with .
      // These are files 'in process' by fdt upload.
      if (f.getName().startsWith(".")) {
        continue;
      }

      String targetFileName = targetDir + File.separator + f.getName();

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
          Process process = Runtime.getRuntime().exec( new String[] { "mv", f.getCanonicalPath(), targetFileName } );          
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
            in = new FileInputStream(f).getChannel();  
            File outFile = new File(targetFileName);  
            out = new FileOutputStream(outFile).getChannel(); 
            in.transferTo(0, in.size(), out);
            in.close();
            in = null;
            out.close();
            out = null; 
            f.delete();
            success  = true;
          } catch (Exception e) {
            success = false;  
          } finally {
            if (in != null) {
              try {
                in.close();                
              } catch (Exception e){
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
        System.out.println(getTimeStamp() + operation + " " + f.getAbsolutePath() +  " to " + targetDir); 
        fileMoved = true;
      } else {
        System.out.println(getTimeStamp() + "ERROR - " + operation + " " + f.getName() + " to " + targetFileName + " failed.");
        isFinished = true;
      }
    }
    if(fileMoved && !isFinished) {
      // If file moved then update last activity
      try {
        currentLastActivity = System.currentTimeMillis();
        updateLastActivity(currentLastActivity);
      } catch (NumberFormatException e) {
        System.out.println(getTimeStamp() + "ERROR: NumberFormatException when trying to update lastActivity.");
        isFinished = true;
      } catch (IOException e) {
        System.out.println(getTimeStamp() + "ERROR: IOException when trying to update lastActivity. " + e.toString());
        isFinished = true;
      }      
    }
    return isFinished;
  }
  private static String getTimeStamp() {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return f.format(new Date()) + " "; 
  }
}
