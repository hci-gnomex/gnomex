// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import org.apache.commons.net.ftp.FTPReply;
import java.io.InputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.net.ftp.FTPClient;
import java.util.logging.Logger;
import net.sf.samtools.util.SeekableStream;

public class SeekableFTPStream extends SeekableStream
{
    static final Logger log;
    private long position;
    private String host;
    private String path;
    FTPClient ftp;
    
    public SeekableFTPStream(final URL url) throws IOException {
        this.position = 0L;
        this.ftp = null;
        this.host = url.getHost();
        this.path = url.getPath();
        (this.ftp = this.openClient(this.host)).setFileType(2);
    }
    
    public void seek(final long position) {
        this.position = position;
    }
    
    public long position() {
        return this.position;
    }
    
    public long skip(final long n) throws IOException {
        this.position += n;
        return n;
    }
    
    public int read(final byte[] buffer, final int offset, final int len) throws IOException {
        if (offset < 0 || len < 0 || offset + len > buffer.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        int n = 0;
        InputStream is = null;
        try {
            this.ftp.setRestartOffset(this.position);
            is = this.ftp.retrieveFileStream(this.path);
            while (n < len) {
                final int count = is.read(buffer, offset + n, len - n);
                if (count < 0) {
                    if (n == 0) {
                        return -1;
                    }
                    break;
                }
                else {
                    n += count;
                }
            }
            this.position += n;
            return n;
        }
        catch (EOFException e) {
            if (n < 0) {
                return -1;
            }
            this.position += n;
            return n;
        }
        finally {
            if (is != null) {
                is.close();
            }
            this.ftp.completePendingCommand();
        }
    }
    
    public void close() throws IOException {
    }
    
    public int read() throws IOException {
        throw new UnsupportedOperationException("read() is not supported on SeekableHTTPStream.  Must read in blocks.");
    }
    
    private FTPClient openClient(final String host) throws IOException {
        final FTPClient temp_ftp = new FTPClient();
        temp_ftp.connect(host);
        final int reply = temp_ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            temp_ftp.disconnect();
            System.err.println("FTP server refused connection.");
            throw new RuntimeException("FTP server refused connection.");
        }
        final boolean success = temp_ftp.login("anonymous", "igb");
        if (!success) {
            System.err.println("FTP login failed " + temp_ftp.getReplyString());
            throw new RuntimeException("FTP login failed " + temp_ftp.getReplyString());
        }
        temp_ftp.enterLocalPassiveMode();
        return temp_ftp;
    }
    
    public long length() {
        throw new UnsupportedOperationException("length() is not supported on SeekableFTPStream.");
    }
    
    public boolean eof() throws IOException {
        throw new UnsupportedOperationException("eof() is not supported on SeekableFTPStream.");
    }
    
    public String getSource() {
        return this.path;
    }
    
    static {
        log = Logger.getLogger(SeekableFTPStream.class.getName());
    }
}
