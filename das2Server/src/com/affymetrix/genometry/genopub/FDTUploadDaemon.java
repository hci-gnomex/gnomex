// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class FDTUploadDaemon
{
    private static long maxWaitMin;
    private static final int shutdownIntervalSeconds = 2;
    private static String sourceDir;
    private static String targetDir;
    private static boolean isFinished;
    private static long lastActivityMillis;
    
    public static void main(final String[] args) {
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equals("-sourceDir")) {
                FDTUploadDaemon.sourceDir = args[++i];
            }
            else if (args[i].equals("-targetDir")) {
                FDTUploadDaemon.targetDir = args[++i];
            }
            else if (args[i].equals("-maxWaitMin")) {
                FDTUploadDaemon.maxWaitMin = Integer.valueOf(args[++i]);
            }
        }
        final Thread runtimeHookThread = new Thread() {
            @Override
            public void run() {
                shutdownHook();
            }
        };
        Runtime.getRuntime().addShutdownHook(runtimeHookThread);
        try {
            FDTUploadDaemon.lastActivityMillis = System.currentTimeMillis();
            do {
                Thread.sleep(3000L);
                moveFiles();
                if (FDTUploadDaemon.isFinished) {
                    return;
                }
            } while (System.currentTimeMillis() - FDTUploadDaemon.lastActivityMillis <= FDTUploadDaemon.maxWaitMin * 60L * 1000L);
            log("reached daemon max wait time");
            FDTUploadDaemon.isFinished = true;
        }
        catch (Throwable t) {
            log("Exception: " + t.toString());
        }
    }
    
    private static void moveFiles() {
        final File stagingDir = new File(FDTUploadDaemon.sourceDir);
        if (!stagingDir.exists()) {
            FDTUploadDaemon.isFinished = true;
            log("ERROR - source directory does not exist.");
            return;
        }
        final File[] fileList = stagingDir.listFiles();
        for (int x = 0; x < fileList.length; ++x) {
            final File f = fileList[x];
            if (!f.getName().startsWith(".")) {
                if (f.getName().equals("done")) {
                    log("done file encountered.");
                    FDTUploadDaemon.isFinished = true;
                    break;
                }
                FDTUploadDaemon.lastActivityMillis = System.currentTimeMillis();
                final String targetFileName = FDTUploadDaemon.targetDir + File.separator + f.getName();
                String operation = "move";
                boolean success = f.renameTo(new File(targetFileName));
                if (!success) {
                    operation = "copy";
                    FileChannel in = null;
                    FileChannel out = null;
                    try {
                        in = new FileInputStream(f).getChannel();
                        final File outFile = new File(targetFileName);
                        out = new FileOutputStream(outFile).getChannel();
                        in.transferTo(0L, in.size(), out);
                        in.close();
                        in = null;
                        out.close();
                        out = null;
                        f.delete();
                        success = true;
                    }
                    catch (Exception e) {
                        success = false;
                    }
                    finally {
                        if (in != null) {
                            try {
                                in.close();
                            }
                            catch (Exception ex) {}
                        }
                        if (out != null) {
                            try {
                                out.close();
                            }
                            catch (Exception ex2) {}
                        }
                    }
                }
                if (!success) {
                    log("ERROR - " + operation + " " + f.getName() + " to " + targetFileName + " failed.");
                    FDTUploadDaemon.isFinished = true;
                    break;
                }
                log(operation + " " + f.getName());
            }
        }
    }
    
    private static void shutdownHook() {
        final long t0 = System.currentTimeMillis();
        do {
            try {
                Thread.sleep(500L);
            }
            catch (Exception e) {
                log("Exception during shutdown: " + e.toString());
                break;
            }
        } while (System.currentTimeMillis() - t0 <= 2000L);
        log("shutdown completed.");
    }
    
    private static void log(final String msg) {
        System.out.println(getTimeStamp() + " " + msg + " " + FDTUploadDaemon.sourceDir + " -> " + FDTUploadDaemon.targetDir);
    }
    
    private static String getTimeStamp() {
        final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(new Date());
    }
    
    static {
        FDTUploadDaemon.maxWaitMin = 1440L;
        FDTUploadDaemon.isFinished = false;
        FDTUploadDaemon.lastActivityMillis = 0L;
    }
}
