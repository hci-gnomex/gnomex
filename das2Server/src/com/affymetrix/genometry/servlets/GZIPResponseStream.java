// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.servlets;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;

public final class GZIPResponseStream extends ServletOutputStream
{
    private ByteArrayOutputStream baos;
    private GZIPOutputStream gzipstream;
    private boolean closed;
    private HttpServletResponse response;
    private ServletOutputStream output;
    
    public GZIPResponseStream(final HttpServletResponse response) throws IOException {
        this.baos = null;
        this.gzipstream = null;
        this.closed = false;
        this.response = null;
        this.output = null;
        this.closed = false;
        this.response = response;
        this.output = response.getOutputStream();
        this.baos = new ByteArrayOutputStream();
        this.gzipstream = new GZIPOutputStream(this.baos);
    }
    
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.gzipstream.finish();
        final byte[] bytes = this.baos.toByteArray();
        this.response.addHeader("Content-Length", Integer.toString(bytes.length));
        this.response.addHeader("Content-Encoding", "gzip");
        this.output.write(bytes);
        this.output.flush();
        this.output.close();
        this.closed = true;
    }
    
    public void flush() throws IOException {
        if (this.closed) {
            throw new IOException("Cannot flush a closed output stream");
        }
        this.gzipstream.flush();
    }
    
    public void write(final int b) throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        this.gzipstream.write((byte)b);
    }
    
    public void write(final byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }
    
    public void write(final byte[] b, final int off, final int len) throws IOException {
        if (this.closed) {
            throw new IOException("Cannot write to a closed output stream");
        }
        this.gzipstream.write(b, off, len);
    }
    
    public boolean closed() {
        return this.closed;
    }
    
    public void reset() {
    }
}
