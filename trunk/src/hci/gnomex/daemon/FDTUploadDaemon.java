package hci.gnomex.daemon;

import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;

public class FDTUploadDaemon {

  private static  long     maxWaitMin = 60 * 24;     // minutes this daemon should run (24 hrs) after last activity
 
  private static final int shutdownIntervalSeconds = 2;  // shutdown hook delay time in seconds
  
  private static String  sourceDir;
  private static String  targetDir;
  private static boolean isFinished = false;
  private static long    lastActivityMillis = 0;
  
  public static void main (String[] args) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-sourceDir")) {
        sourceDir = args[++i];
      } else if (args[i].equals("-targetDir")) {
        targetDir = args[++i];
      } else if (args[i].equals("-maxWaitMin")) {
        maxWaitMin = Integer.valueOf(args[++i]);
      } 
    }
    
    Thread runtimeHookThread = new Thread() {
      public void run() {
        shutdownHook(); 
        }
    };
    
    Runtime.getRuntime().addShutdownHook (runtimeHookThread);
    try {
      lastActivityMillis = System.currentTimeMillis();
      while (true) {
        Thread.sleep (3000);
        moveFiles();
        if (isFinished) {
          break;
        }
        // Terminate daemon if it has gone X minutes (maxWaitMin) with no activity
        if (System.currentTimeMillis() - lastActivityMillis > maxWaitMin * 60 * 1000) {
          log("reached daemon max wait time");
          isFinished = true;
          break;
        }
      }
    } catch (Throwable t) {
      log ("Exception: "+ t.toString()); 
    }
  }
  
  private static void moveFiles() {
    // Look in the directory for file
    File stagingDir = new File(sourceDir);
    if (!stagingDir.exists()) {
      isFinished = true;
      log("ERROR - source directory does not exist.");
      return;
    }
    
    File[] fileList = stagingDir.listFiles();
    for( int x = 0; x < fileList.length; x++) {
      File f = fileList[x];
      
      // Ignore files that start with .
      // These are files 'in process' by fdt upload.
      if (f.getName().startsWith(".")) {
        continue;
      }
      
      
      // If the 'done' file is encountered,
      // shut down the daemon
      if (f.getName().equals("done")) {
        log("done file encountered.");
        isFinished = true;
        break;        
      }
      
      // We have encountered a file to move.  Set the last activity
      // time to current time.
      lastActivityMillis = System.currentTimeMillis();
      
      String targetFileName = targetDir + File.separator + f.getName();
      
      // Try to move to file.  If we can't move it, copy then delete it.
      String operation = "move";
      boolean success = f.renameTo(new File(targetFileName));
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
      
      if (success) {
        log(operation + " " + f.getName());        
      } else {
        log("ERROR - " + operation + " " + f.getName() + " to " + targetFileName + " failed.");
      }
    }
    
  }

  private static void shutdownHook() {
    long t0 = System.currentTimeMillis();
    while (true) {
      try {
        Thread.sleep (500); }
      catch (Exception e) {
        log ("Exception during shutdown: " + e.toString());
        break; 
      }
      if (System.currentTimeMillis() - t0 > shutdownIntervalSeconds * 1000) {
        break;
      }
    }
    log ("shutdown completed."); 
  }

  private static void log (String msg) {
    System.out.println (getTimeStamp() + " " + msg + " " + sourceDir + " -> " + targetDir); 
  }

  private static String getTimeStamp() {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return f.format(new Date()); 
  }

} 
