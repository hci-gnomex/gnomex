// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import net.sf.samtools.util.SeekableStream;

public class SeekableBufferedStream extends SeekableStream
{
    private int bufferSize;
    BufferedInputStream bufferedStream;
    SeekableStream wrappedStream;
    long position;
    
    public SeekableBufferedStream(final SeekableStream httpStream) {
        this.bufferSize = 512000;
        this.wrappedStream = httpStream;
        this.position = 0L;
        this.bufferedStream = new BufferedInputStream((InputStream)this.wrappedStream, this.bufferSize);
    }
    
    public long length() {
        return this.wrappedStream.length();
    }
    
    public void seek(final long position) throws IOException {
        this.position = position;
        this.wrappedStream.seek(position);
        this.bufferedStream = new BufferedInputStream((InputStream)this.wrappedStream, this.bufferSize);
    }
    
    public int read() throws IOException {
        final int b = this.bufferedStream.read();
        ++this.position;
        return b;
    }
    
    public int read(final byte[] buffer, final int offset, final int length) throws IOException {
        final int nBytesRead = this.bufferedStream.read(buffer, offset, length);
        if (nBytesRead > 0) {
            this.position += nBytesRead;
        }
        return nBytesRead;
    }
    
    public void close() throws IOException {
        this.wrappedStream.close();
    }
    
    public boolean eof() throws IOException {
        return this.position >= this.wrappedStream.length();
    }
    
    public long position() {
        return this.position;
    }
    
    public void position(final long position) throws IOException {
        this.seek(position);
    }
    
    public long skip(final long n) throws IOException {
        this.seek(this.position + n);
        return this.position + n;
    }
    
    public String getSource() {
        return this.wrappedStream.getSource();
    }
}
