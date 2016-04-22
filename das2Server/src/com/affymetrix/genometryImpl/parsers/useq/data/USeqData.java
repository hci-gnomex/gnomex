// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class USeqData
{
    protected SliceInfo sliceInfo;
    protected File binaryFile;
    protected String header;
    
    public USeqData() {
        this.header = "";
    }
    
    public SliceInfo getSliceInfo() {
        return this.sliceInfo;
    }
    
    public File getBinaryFile() {
        return this.binaryFile;
    }
    
    public String getHeader() {
        return this.header;
    }
    
    public void setHeader(final String header) {
        this.header = header;
    }
    
    public void setSliceInfo(final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
    }
    
    public void setBinaryFile(final File binaryFile) {
        this.binaryFile = binaryFile;
    }
    
    public void read(final File binaryFile) {
        FileInputStream workingFIS = null;
        DataInputStream workingDIS = null;
        try {
            this.binaryFile = binaryFile;
            workingFIS = new FileInputStream(binaryFile);
            workingDIS = new DataInputStream(new BufferedInputStream(workingFIS));
            this.read(workingDIS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            USeqUtilities.safeClose(workingFIS);
            USeqUtilities.safeClose(workingDIS);
        }
    }
    
    public void read(final DataInputStream dis) {
    }
}
