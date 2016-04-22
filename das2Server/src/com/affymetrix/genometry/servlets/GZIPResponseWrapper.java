// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometry.servlets;

import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public final class GZIPResponseWrapper extends HttpServletResponseWrapper
{
    private HttpServletResponse origResponse;
    private ServletOutputStream stream;
    private PrintWriter writer;
    
    public GZIPResponseWrapper(final HttpServletResponse response) {
        super(response);
        this.origResponse = null;
        this.stream = null;
        this.writer = null;
        this.origResponse = response;
    }
    
    public ServletOutputStream createOutputStream() throws IOException {
        return new GZIPResponseStream(this.origResponse);
    }
    
    public void finishResponse() {
        try {
            if (this.writer != null) {
                this.writer.close();
            }
            else if (this.stream != null) {
                this.stream.close();
            }
        }
        catch (IOException ex) {}
    }
    
    public void flushBuffer() throws IOException {
        this.stream.flush();
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
        if (this.writer != null) {
            throw new IllegalStateException("getWriter() has already been called!");
        }
        if (this.stream == null) {
            this.stream = this.createOutputStream();
        }
        return this.stream;
    }
    
    public PrintWriter getWriter() throws IOException {
        if (this.writer != null) {
            return this.writer;
        }
        if (this.stream != null) {
            throw new IllegalStateException("getOutputStream() has already been called!");
        }
        this.stream = this.createOutputStream();
        return this.writer = new PrintWriter(new OutputStreamWriter((OutputStream)this.stream, "UTF-8"));
    }
    
    public void setContentLength(final int length) {
    }
}
