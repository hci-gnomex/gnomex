// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FilterInputStream;

public class CachingInputStream extends FilterInputStream
{
    private OutputStream outputStream;
    private String url;
    
    public CachingInputStream(final InputStream is, final File cacheFile, final String url) {
        super(is);
        this.outputStream = null;
        try {
            this.outputStream = new BufferedOutputStream(new FileOutputStream(cacheFile));
            this.url = url;
        }
        catch (FileNotFoundException e) {
            this.fail(e);
        }
    }
    
    private synchronized void fail(final Throwable e) {
        final StackTraceElement ste = new Exception().getStackTrace()[1];
        Logger.getLogger(CachingInputStream.class.getName()).logp(Level.SEVERE, ste.getClassName(), ste.getMethodName(), "Caching of " + this.url + " failed", e);
        GeneralUtils.safeClose(this.outputStream);
        this.outputStream = null;
        LocalUrlCacher.invalidateCacheFile(this.url);
    }
    
    @Override
    public int read() throws IOException {
        final byte[] b = { 0 };
        final int ret = this.read(b, 0, b.length);
        return (ret == -1) ? ret : (b[0] & 0xFF);
    }
    
    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        try {
            final int bytesRead = super.read(b, off, len);
            if (bytesRead > 0 && this.outputStream != null) {
                this.outputStream.write(b, off, bytesRead);
            }
            return bytesRead;
        }
        catch (IOException e) {
            this.fail(e);
            throw e;
        }
    }
    
    @Override
    public long skip(final long n) throws IOException {
        final byte[] b = new byte[8192];
        long bytesSkipped = 0L;
        int bytesRead = 0;
        try {
            while (bytesSkipped < n) {
                final long bytesRemaining = n - bytesSkipped;
                bytesRead = this.read(b, 0, (bytesRemaining < b.length) ? ((int)bytesRemaining) : b.length);
                if (bytesRead <= 0) {
                    return bytesSkipped;
                }
                bytesSkipped += bytesRead;
            }
        }
        catch (IOException e) {
            this.fail(e);
            throw e;
        }
        return bytesSkipped;
    }
    
    @Override
    public synchronized void close() throws IOException {
        try {
            if (this.outputStream != null) {
                try {
                    this.skip(Long.MAX_VALUE);
                }
                catch (IOException ex) {}
                GeneralUtils.safeClose(this.outputStream);
                this.outputStream = null;
            }
            super.close();
        }
        catch (IOException e) {
            this.fail(e);
            throw e;
        }
    }
    
    @Override
    public synchronized void mark(final int readlimit) {
    }
    
    @Override
    public boolean markSupported() {
        return false;
    }
}
