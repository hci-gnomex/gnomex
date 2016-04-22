// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq;

import java.util.regex.Matcher;
import java.io.IOException;
import java.util.regex.Pattern;

public class SliceInfo
{
    public static final Pattern SLICE_NAME_SPLITTER;
    private String notes;
    private String chromosome;
    private String strand;
    private int firstStartPosition;
    private int lastStartPosition;
    private int numberRecords;
    private String binaryType;
    
    public SliceInfo(final String sliceName) throws IOException {
        this.notes = "";
        this.chromosome = null;
        this.strand = null;
        this.binaryType = null;
        this.parseSliceName(sliceName);
    }
    
    public SliceInfo(final String chromosome, final String strand, final int firstStartPosition, final int lastStartPosition, final int numberRecords, final String binaryType) {
        this.notes = "";
        this.chromosome = null;
        this.strand = null;
        this.binaryType = null;
        this.chromosome = chromosome;
        this.strand = strand;
        this.firstStartPosition = firstStartPosition;
        this.lastStartPosition = lastStartPosition;
        this.numberRecords = numberRecords;
        this.binaryType = binaryType;
    }
    
    public void parseSliceName(final String sliceName) throws IOException {
        final Matcher mat = SliceInfo.SLICE_NAME_SPLITTER.matcher(sliceName);
        if (!mat.matches()) {
            throw new IOException("Malformed slice name! Failed to parse the slice info from -> " + sliceName);
        }
        this.chromosome = mat.group(1);
        this.strand = mat.group(2);
        this.firstStartPosition = Integer.parseInt(mat.group(3));
        this.lastStartPosition = Integer.parseInt(mat.group(4));
        this.numberRecords = Integer.parseInt(mat.group(5));
        this.binaryType = mat.group(6);
    }
    
    public String getSliceName() {
        return this.chromosome + this.strand + this.firstStartPosition + "-" + this.lastStartPosition + "-" + this.numberRecords + "." + this.binaryType;
    }
    
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(final String notes) {
        this.notes = notes;
    }
    
    public static Pattern getSliceNameSplitter() {
        return SliceInfo.SLICE_NAME_SPLITTER;
    }
    
    public String getChromosome() {
        return this.chromosome;
    }
    
    public String getStrand() {
        return this.strand;
    }
    
    public int getFirstStartPosition() {
        return this.firstStartPosition;
    }
    
    public int getLastStartPosition() {
        return this.lastStartPosition;
    }
    
    public int getNumberRecords() {
        return this.numberRecords;
    }
    
    public String getBinaryType() {
        return this.binaryType;
    }
    
    public void setChromosome(final String chromosome) {
        this.chromosome = chromosome;
    }
    
    public void setStrand(final String strand) {
        this.strand = strand;
    }
    
    public void setFirstStartPosition(final int firstStartPosition) {
        this.firstStartPosition = firstStartPosition;
    }
    
    public void setLastStartPosition(final int lastStartPosition) {
        this.lastStartPosition = lastStartPosition;
    }
    
    public void setNumberRecords(final int numberRecords) {
        this.numberRecords = numberRecords;
    }
    
    public void setBinaryType(final String binaryType) {
        this.binaryType = binaryType;
    }
    
    public boolean isContainedBy(final int beginningBP, final int endingBP) {
        return this.firstStartPosition >= beginningBP && this.firstStartPosition < endingBP && this.lastStartPosition >= beginningBP && this.lastStartPosition < endingBP;
    }
    
    static {
        SLICE_NAME_SPLITTER = Pattern.compile("^(.+)([+-.])(\\d+)-(\\d+)-(\\d+)\\.(\\w+)$");
    }
}
