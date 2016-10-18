// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.genopub;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public class ArchiveHelper
{
    public static final String ZIP_MODE = "zip";
    public static final String TAR_MODE = "tar";
    private String mode;
    private long archiveFileSize;
    private String archiveEntryName;
    private String gzipFileName;
    private String tempDir;
    
    public ArchiveHelper() {
        this.mode = "zip";
        this.archiveFileSize = 0L;
        this.archiveEntryName = "";
        this.gzipFileName = null;
        this.tempDir = ".";
    }
    
    public int transferBytes(final InputStream in, final OutputStream out) throws IOException {
        final byte[] b = new byte[102400];
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
    
    public InputStream getInputStreamToArchive(final String fileName, final String zipEntryName) throws FileNotFoundException, IOException {
        InputStream in = null;
        if (this.mode.equals("zip") || fileName.endsWith(".gz") || fileName.endsWith(".zip") || fileName.endsWith(".gzip")) {
            in = new FileInputStream(fileName);
            this.archiveFileSize = new File(fileName).length();
            this.archiveEntryName = zipEntryName;
        }
        else {
            in = this.compressFile(fileName, zipEntryName);
        }
        return in;
    }
    
    public void removeTemporaryFile() {
        if (this.gzipFileName != null) {
            final File f = new File(this.gzipFileName);
            if (f.exists()) {
                final boolean success = f.delete();
                if (!success) {
                    System.out.println("Warning - temp file " + this.gzipFileName + " not deleted.");
                }
            }
        }
    }
    
    public String getGzipTempFileName(final String zipEntryName) {
        final String name = zipEntryName.replaceAll("/", "_");
        return this.tempDir + "temp_" + name + ".gz";
    }
    
    public InputStream compressFile(final String fileName, final String zipEntryName) throws IOException {
        FileOutputStream out = null;
        FileInputStream in = null;
        GZIPOutputStream gzipOut = null;
        try {
            this.gzipFileName = this.getGzipTempFileName(zipEntryName);
            out = new FileOutputStream(this.gzipFileName);
            gzipOut = new GZIPOutputStream(out);
            in = new FileInputStream(fileName);
            this.transferBytes(in, gzipOut);
            this.archiveFileSize = new File(this.gzipFileName).length();
            this.archiveEntryName = zipEntryName + ".gz";
        }
        catch (IOException e) {
            Logger.getLogger(Annotation.class.getName()).log(Level.WARNING, "Unable to compress file " + fileName + " for zip entry " + zipEntryName, e);
        }
        finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (gzipOut != null) {
                gzipOut.finish();
                gzipOut.close();
            }
        }
        return new FileInputStream(this.gzipFileName);
    }
    
    public String getTempDir() {
        return this.tempDir;
    }
    
    public void setTempDir(final String tempDir) {
        this.tempDir = tempDir;
        if (this.tempDir == null || this.tempDir.equals("")) {
            this.tempDir = "";
        }
        else if (!this.tempDir.endsWith(Constants.FILE_SEPARAT0R)) {
            this.tempDir += Constants.FILE_SEPARATOR;
        }
    }
    
    public String getMode() {
        return this.mode;
    }
    
    public void setMode(final String mode) {
        this.mode = mode;
    }
    
    public long getArchiveFileSize() {
        return this.archiveFileSize;
    }
    
    public void setArchiveFileSize(final long archiveFileSize) {
        this.archiveFileSize = archiveFileSize;
    }
    
    public String getArchiveEntryName() {
        return this.archiveEntryName;
    }
    
    public void setArchiveEntryName(final String archiveEntryName) {
        this.archiveEntryName = archiveEntryName;
    }
    
    public String getGzipFileName() {
        return this.gzipFileName;
    }
    
    public void setGzipFileName(final String gzipFileName) {
        this.gzipFileName = gzipFileName;
    }
    
    public boolean isZipMode() {
        return this.mode.equals("zip");
    }
}
