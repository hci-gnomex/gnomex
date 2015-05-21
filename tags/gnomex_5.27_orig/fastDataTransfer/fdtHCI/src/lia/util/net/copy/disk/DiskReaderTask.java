/*
 * $Id: DiskReaderTask.java,v 1.1 2012-10-29 22:30:18 HCI\rcundick Exp $
 */
package lia.util.net.copy.disk;

import gui.FdtMain;
import gui.GUIFileStatus;
import gui.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import lia.util.net.common.Config;
import lia.util.net.common.Utils;
import lia.util.net.copy.FDT;
import lia.util.net.copy.FDTReaderSession;
import lia.util.net.copy.FileBlock;
import lia.util.net.copy.FileSession;

/**
 *
 * This class will read the files for a specific partitionID
 * and FDTReaderSession. The tuple ( partitionID, FDTReaderSession ) identifies
 * this task.
 *
 */
public class DiskReaderTask extends GenericDiskTask {

    private static final Log logger = Log.getLoggerInstance();

    private static GUIFileStatus guiFileStatus = null;
    
    private static final DiskReaderManager diskReaderManager = DiskReaderManager.getInstance();
    
    List<FileSession> fileSessions;
    
    private final MessageDigest md5Sum;
    private final boolean computeMD5;
    
    private AtomicBoolean isFinished = new AtomicBoolean(false);
    private int addedFBS = 0;
    
    private final FDTReaderSession fdtSession;

    
    /**
     *
     * @throws NullPointerException if fdtSession is null or fileSessions list is null
     **/
    public DiskReaderTask(final int partitionID, final int taskIndex, final List<FileSession> fileSessions, final FDTReaderSession fdtSession) {
        super(partitionID, taskIndex);
        boolean bComputeMD5  = Config.getInstance().computeMD5();
        MessageDigest md5SumTMP = null;
        
        if(fdtSession == null) throw new NullPointerException("FDTSession cannot be null");
        if(fileSessions == null) throw new NullPointerException("FileSessions cannot be null");
        
        this.fileSessions = fileSessions;
        this.fdtSession = fdtSession;
        this.myName = new StringBuilder("DiskReaderTask - partitionID: ").append(partitionID).append(" taskID: ").append(taskIndex).append(" - [ ").append(fdtSession.toString()).append(" ]").toString();
        
        if(bComputeMD5) {
            try {
                md5SumTMP = MessageDigest.getInstance("MD5");
            } catch(Throwable t) {
                logger.log(Level.WARNING, " \n\n\n Cannot compute MD5. Unable to initiate the MessageDigest engine. Cause: ", t);
                md5SumTMP = null;
            }
        }
        
        if(md5SumTMP != null) {
            bComputeMD5 = true;
        } else {
            bComputeMD5 = false;
        }
        
        md5Sum = md5SumTMP;
        computeMD5 = bComputeMD5;
    }

    public void stopIt() {
        if(isFinished.compareAndSet(false, true)) {
            //interrupt it if it's blocked in waiting
            fdtSession.finishReader(partitionID, this);
        }
    }
    
    private void logToAppLogger(FileSession fileSession) {
      if (FDT.config.getAppLogger() != null && !FDT.config.getAppLogger().equals("")) {
        try {
          SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS a");
          Process process = Runtime.getRuntime().exec( new String[] { "/bin/sh", FDT.config.getAppLogger(), 
              "-fileName", fileSession.getFile().getAbsolutePath(), 
              "-type", "download",
              "-method", "fdt", 
              "-startDateTime", df.format(new java.util.Date(fileSession.startTimeMillis)),
              "-endDateTime",  df.format(new java.util.Date(System.currentTimeMillis())),
              "-fileSize", Long.valueOf(fileSession.getFile().length()).toString()
              });    
          process.waitFor();
          process.destroy();
        } catch(Exception e) {
          logger.log(Level.WARNING, " \n\n\n Cannot write to app logger " + FDT.config.getAppLogger() + " Cause: ", e);
        }
      }
    }

    
    public void run() {
        
        
        String cName = Thread.currentThread().getName();
        
        Thread.currentThread().setName(myName);
        logger.log("Inside diskreadertask.run()");
        if(logger.isLoggable(Level.FINE))
        {
            logger.log(Level.FINE,  myName + " started" );
        }

        if(!FdtMain.isIsServerMode())
            guiFileStatus = GUIFileStatus.getGUIFileStatusInstance();
        Throwable downCause = null;
        ByteBuffer buff = null;
        FileBlock fileBlock = null;
        long cPosition, readBytes;
        
        FileSession currentFileSession = null;
        FileChannel cuurentFileChannel = null;
        long startTime = System.nanoTime();
        long lastTimeCalled = startTime;
        long duration;
        long lastTransferredBytes =0;
        long rate =0;
        
        try {
            if(fdtSession == null)
            {
                logger.log(Level.WARNING, " FDT Session is null in DiskReaderTask !!");
            }
           
            while(!fdtSession.isClosed())
            {

                for(final FileSession fileSession: fileSessions)
                {

                    currentFileSession = fileSession;
                    
                    if(fileSession.isClosed()) {
                        fdtSession.finishFileSession(fileSession.sessionID(), null);



                        if(!FdtMain.isIsServerMode())
                        {
                            guiFileStatus.setEndTime(fileSession.sessionID().toString(), System.nanoTime());
                            guiFileStatus.updatePercentCompleted(fileSession.sessionID().toString(), fileSession.sessionSize(), fileSession.sessionSize());
                        }
                        
                        // If there is an app logger, log the file session info
                        if (FdtMain.isIsServerMode()) {
                          logToAppLogger(fileSession);
                        }
                        continue;
                    }
                    
                    if(logger.isLoggable(Level.FINE))
                    {
                        logger.log(Level.FINE, " [ FileReaderTask ] for FileSession (" + fileSession.sessionID() + ") " + fileSession.fileName() + " started");
                    }

                    //
                    // Plug-in point for application transfer logging
                    //

                    if(!FdtMain.isIsServerMode())                      
                        guiFileStatus.setStartTime(fileSession.sessionID().toString(), System.nanoTime());

                    downCause = null;
                    
                    final FileChannel fileChannel = fileSession.getChannel();
                    cuurentFileChannel = fileChannel;
                    
                    if(fileChannel != null) {
                        if(computeMD5) {
                            md5Sum.reset();
                        }

                        
                        cPosition = fileChannel.position();
                        for(;;)
                        {
                            //try to get a new buffer from the pool
                            buff = null;
                            fileBlock = null;

                            while(buff == null)
                            {
                                if(fdtSession.isClosed())
                                {
                                    return;
                                }
                                buff = bufferPool.poll(2, TimeUnit.SECONDS);
                            }
                            
                            if(fileSession.isZero())
                            {
                                //Just play with the buffer markers ... do not even touch /dev/zero
                                readBytes = buff.capacity();
                                buff.position(0);
                                buff.limit(buff.capacity());
                            } 
                            else
                            {
                                readBytes = fileChannel.read(buff);
                                
                                //buff = encryptStream.encryptData(buff);

                                StringBuilder sb = new StringBuilder(1024);
                                sb.append(" [ DiskReaderTask ] FileReaderSession ").append(fileSession.sessionID()).append(": ").append(fileSession.fileName());
                                sb.append(" fdtSession: ").append(fdtSession.sessionID()).append(" read: ").append(readBytes);
                                    
                            }
                            
                            if(readBytes == -1)
                            {//EOF
                                if(fileSession.cProcessedBytes.get() == fileSession.sessionSize())
                                {
                                    fdtSession.finishFileSession(fileSession.sessionID(), null);
                                    // If there is an app logger, log the file session info
                                    if (FdtMain.isIsServerMode()) {
                                      logToAppLogger(fileSession);
                                    }

                                    if(!FdtMain.isIsServerMode())
                                    {
                                        guiFileStatus.setEndTime(fileSession.sessionID().toString(), System.nanoTime());
                                        guiFileStatus.updatePercentCompleted(fileSession.sessionID().toString(), fileSession.sessionSize(), fileSession.sessionSize());
                                    }
                                } 
                                else
                                {
                                    if(!fdtSession.loop())
                                    {
                                        StringBuilder sbEx = new StringBuilder();
                                        sbEx.append("FileSession: ( ").append(fileSession.sessionID()).append(" ): ").append(fileSession.fileName());
                                        sbEx.append(" total length: ").append(fileSession.sessionSize()).append(" != total read until EOF: ").append(fileSession.cProcessedBytes.get());
                                        fdtSession.finishFileSession(fileSession.sessionID(), new IOException(sbEx.toString()));
                                    } 
                                    else
                                    {
                                        fileChannel.position(0);
                                    }
                                }

                                bufferPool.put(buff);
                                buff = null;
                                fileBlock = null;
                                if(computeMD5)
                                {
                                    byte[] md5ByteArray = md5Sum.digest();
                                        logger.log(Level.FINEST, Utils.md5ToString(md5ByteArray) + "   --->  " + fileSession.fileName()
                                        );
                                    fdtSession.setMD5Sum(fileSession.sessionID(), md5ByteArray);
                                }
                                break;
                            }
                            
                            if(computeMD5) {
                                buff.flip();
                                md5Sum.update(buff);
                            }

                            fileSession.cProcessedBytes.addAndGet(readBytes);
                            diskReaderManager.addAndGetTotalBytes(readBytes);
                            diskReaderManager.addAndGetUtilBytes(readBytes);
                            addAndGetTotalBytes(readBytes);
                            addAndGetUtilBytes(readBytes);
                            
                            fdtSession.addAndGetTotalBytes(readBytes);
                            fdtSession.addAndGetUtilBytes(readBytes);

                            if(!FdtMain.isIsServerMode())
                            {
                                duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - lastTimeCalled);

                                if(duration >2000)
                                {
                                    rate =  (long) ((fdtSession.getTotalBytes() - lastTransferredBytes) * 1000D / duration);
                                    long remainingSeconds = 0;
                                    if(rate>0)
                                    remainingSeconds = (fdtSession.getSize() - fdtSession.getTotalBytes())/rate ;

                                    guiFileStatus.setTransferRate(rate);

                                    guiFileStatus.setETA(Utils.getETA((long) remainingSeconds));
                                    
                                    lastTransferredBytes = fdtSession.getTotalBytes();

                                    lastTimeCalled = System.nanoTime();
                                }


                                
                            }

                            
                            if(!fileSession.isZero()) {
                                buff.flip();
                            }
                            
                            fileBlock = FileBlock.getInstance(fdtSession.sessionID(), fileSession.sessionID(), cPosition, buff);
                            cPosition += readBytes;
                            if(!FdtMain.isIsServerMode())
                                guiFileStatus.updatePercentCompleted(fileSession.sessionID().toString(), cPosition, fileSession.sessionSize());

                            if(!fdtSession.isClosed()) {
                                while(!fdtSession.fileBlockQueue.offer(fileBlock, 2, TimeUnit.SECONDS)) {
                                    if(fdtSession.isClosed()) {
                                        // If there is an app logger, log the file session info
                                        if (FdtMain.isIsServerMode()) {
                                          logToAppLogger(fileSession);
                                        }

                                        if(!FdtMain.isIsServerMode())
                                        {
                                            guiFileStatus.setEndTime(fileSession.sessionID().toString(), System.nanoTime());
                                            guiFileStatus.updatePercentCompleted(fileSession.sessionID().toString(), fileSession.sessionSize(), fileSession.sessionSize());
                                        }
                                        return;
                                    }
                                }
                                fileBlock = null;
                                buff = null;
                                addedFBS++;
                            } else {
                                try {
                                    if(fileBlock != null && fileBlock.buff != null) {
                                        bufferPool.put(fileBlock.buff);
                                        buff = null;
                                        fileBlock = null;
                                        // If there is an app logger, log the file session info
                                        if (FdtMain.isIsServerMode()) {
                                          logToAppLogger(fileSession);
                                        }
                                        if(!FdtMain.isIsServerMode())
                                        {
                                            guiFileStatus.setEndTime(fileSession.sessionID().toString(), System.nanoTime());
                                            guiFileStatus.updatePercentCompleted(fileSession.sessionID().toString(), fileSession.sessionSize(), fileSession.sessionSize());
                                        }
                                    }
                                    return;
                                }catch(Throwable t1) {
                                    if(logger.isLoggable(Level.FINER)) {
                                        logger.log(Level.FINER, " Got exception returning bufer to the buffer pool", t1);
                                    }
                                }
                            }
                            
                            fileBlock = null;
                        }//while()
                        
                    } else {
                        if(logger.isLoggable(Level.FINER)) {
                            logger.log(Level.FINER, " Null file channel for fileSession" + fileSession);
                        }
                        downCause = new NullPointerException("Null File Channel inside reader worker");
                        downCause.fillInStackTrace();
                        fdtSession.finishFileSession(fileSession.sessionID(), downCause);
                    }
                    
                }//for - fileSession
                
                if(!fdtSession.loop()) {
                    break;
                }
            }//for
            
        } catch(IOException ioe) {
            if(!isFinished.getAndSet(true) && !fdtSession.isClosed()) {//most likely a normal error
                logger.log(Level.INFO, " [ HANDLED ] Got I/O Exception reading FileSession (" + currentFileSession.sessionID() + ") / "  + currentFileSession.fileName(), ioe);
                return;
            }
            logger.logError(ioe);
            //check for down
            if(isFinished.get() || fdtSession.isClosed()) {//most likely a normal error
                logger.log(Level.FINEST, " [ HANDLED ] Got I/O Exception reading FileSession (" + currentFileSession.sessionID() + ") "  + currentFileSession.fileName(), ioe);
                return;
            }
            
            downCause = ioe;
            fdtSession.finishFileSession(currentFileSession.sessionID(), downCause);
            
        } catch (Throwable t) {
            if(isFinished.get() || fdtSession.isClosed()) {//most likely a normal error
                logger.log(Level.FINEST, "Got General Exception reading FileSession (" + currentFileSession.sessionID() + ") " + currentFileSession.fileName(), t);
                return;
            }
            
            downCause = t;
            fdtSession.finishFileSession(currentFileSession.sessionID(), downCause);
        } finally {

            if(logger.isLoggable(Level.FINE)) {
                final StringBuilder logMsg = new StringBuilder("DiskReaderTask - partitionID: ").append(partitionID).append(" taskID: ").append(this.taskID);
                if(downCause == null) {
                    logMsg.append(" Normal exit fdtSession.isClosed() = ").append(fdtSession.isClosed());
                } else {
                    logMsg.append(" Exit with error: ").append(Utils.getStackTrace(downCause)).append("fdtSession.isClosed() = ").append(fdtSession.isClosed());
                }
                logger.log(Level.FINE, logMsg.toString());
            }
            
            if(cuurentFileChannel != null) {
                try {
                    cuurentFileChannel.close();
                }catch(Throwable ignore){}
            }
            
            try {
                if(buff != null) {
                    bufferPool.put(buff);
                    try {
                        if(fileBlock != null && fileBlock.buff != null && fileBlock.buff != buff) {
                            boolean bPut = bufferPool.put(fileBlock.buff);
                            if(logger.isLoggable(Level.FINEST)) {
                                logger.log(Level.FINEST, " [ FINALLY ] DiskReaderTask RETURNING FB buff: " + fileBlock.buff + " to pool [ " + bPut + " ]" );
                            }
                        }
                    } catch(Throwable t1) {
                        if(logger.isLoggable(Level.FINER)) {
                            logger.log(Level.FINER, " Got exception returning bufer to the buffer pool", t1);
                        }
                    }
                    buff = null;
                    fileBlock = null;
                }
            } catch(Throwable t1) {
                if(logger.isLoggable(Level.FINER)) {
                    logger.log(Level.FINER, " Got exception returning bufer to the buffer pool", t1);
                }
            }
            
            if(logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "\n\n " + myName + " finishes. " +
                           "\n fdtSession is " + ((fdtSession.isClosed())?"closed":"open") + "" +
                           " Processed FBS = " + addedFBS + " \n\n");
            }
            
            Thread.currentThread().setName(cName);
        }
        
    }//run()
    
}
