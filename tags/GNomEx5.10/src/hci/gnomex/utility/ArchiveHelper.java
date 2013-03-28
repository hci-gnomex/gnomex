package hci.gnomex.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedOutputStream;
import java.util.zip.GZIPOutputStream;

import com.Ostermiller.util.CircularByteBuffer;


public class ArchiveHelper {
  
  public static final String  ZIP_MODE = "zip";
  public static final String  TAR_MODE = "tar";

  private String   mode = ZIP_MODE;
  private long    archiveFileSize = 0;
  private String   archiveEntryName = "";
  private String   gzipFileName = null;  
  private String   tempDir = ".";
  
  
  public int transferBytes(InputStream in, OutputStream out) throws IOException {
    byte b[] = new byte[102400];
    int numRead = 0;
    int size = 0;
    while (numRead != -1) {
      numRead = in.read(b);
      if (numRead != -1) {
        out.write(b, 0, numRead);                                    
        size += numRead;
      }
    }
    in.close();
    return size;
  }
  
  public InputStream getInputStreamToArchive(String fileName, String zipEntryName) throws FileNotFoundException, IOException {
    InputStream in = null;
    // For zip archives or already compressed files, we will be reading the file.
    // For tar archives where file is not already compressed, read the compressed file
    if (mode.equals(ZIP_MODE) || fileName.endsWith(".gz") || fileName.endsWith(".zip") || fileName.endsWith(".gzip")) {
      in = new FileInputStream(fileName);
      archiveFileSize = new File(fileName).length();
      archiveEntryName = zipEntryName;
    } else {
       in = compressFile(fileName, zipEntryName);
    }
    return in;
  }
  
  public void removeTemporaryFile() {
    if (gzipFileName != null) {
      File f = new File(gzipFileName);
      if (f.exists()) {
        boolean success = f.delete();
        if (!success) {
          System.out.println("Warning - temp file " + gzipFileName + " not deleted.");
        }
        
      }
    }
  }
  
  public String getGzipTempFileName(String zipEntryName) {
    String name = zipEntryName;
    name = name.replaceAll("/", "_");
    return tempDir + "temp_" + name + ".gz";
  }
    
  public InputStream compressFile(String fileName, String zipEntryName) throws IOException {

    gzipFileName = getGzipTempFileName(zipEntryName);
    FileOutputStream out = new FileOutputStream(gzipFileName);
    
        
    GZIPOutputStream gzipOut = new GZIPOutputStream(out);
    
    transferBytes(new FileInputStream(fileName), gzipOut);
    
    gzipOut.finish();
    gzipOut.close();
    
    archiveFileSize = new File(gzipFileName).length();
    archiveEntryName = zipEntryName + ".gz";
    
    return new FileInputStream(gzipFileName);    
  }
  
  
  public InputStream compressFileUsingBuffer(String fileName, String zipEntryName) throws IOException {

    
    CircularByteBuffer cbb = new CircularByteBuffer(CircularByteBuffer.INFINITE_SIZE); 
    
    GZIPOutputStream gzipOut = new GZIPOutputStream(cbb.getOutputStream());
    
    transferBytes(new FileInputStream(fileName), gzipOut);
    
    gzipOut.finish();
    gzipOut.close();

    
    //archiveFileSize = new File(gzipFileName).length();
    archiveEntryName = zipEntryName + ".gz";
    

    return cbb.getInputStream();    
    
  }

  
  public String getTempDir() {
    return tempDir;
  }

  
  public void setTempDir(String tempDir) {
    this.tempDir = tempDir;
    if (this.tempDir == null || this.tempDir.equals("")) {
      this.tempDir = "";
    } else {
      if (!this.tempDir.endsWith(File.separator)) {
        this.tempDir += File.separator;
      }
    }

  }

  
  public String getMode() {
    return mode;
  }

  
  public void setMode(String mode) {
    this.mode = mode;
  }

  
  public long getArchiveFileSize() {
    return archiveFileSize;
  }

  
  public void setArchiveFileSize(long archiveFileSize) {
    this.archiveFileSize = archiveFileSize;
  }

  
  public String getArchiveEntryName() {
    return archiveEntryName;
  }

  
  public void setArchiveEntryName(String archiveEntryName) {
    this.archiveEntryName = archiveEntryName;
  }

  
  public String getGzipFileName() {
    return gzipFileName;
  }

  
  public void setGzipFileName(String gzipFileName) {
    this.gzipFileName = gzipFileName;
  }

  
  public boolean isZipMode() {
    if (mode.equals(ZIP_MODE)) {
      return true;
    } else {
      return false;
    }
  }

}
