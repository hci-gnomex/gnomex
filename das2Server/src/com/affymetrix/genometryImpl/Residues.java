// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl;

public class Residues
{
    int start;
    int end;
    int length;
    StringBuffer residues;
    
    public Residues(final String residues) {
        this.length = 0;
        this.residues = null;
        this.setResidues(residues);
    }
    
    public Residues(final String residues, final int start) {
        this.length = 0;
        this.residues = null;
        this.setStart(start);
        this.setResidues(residues);
    }
    
    public final void setResidues(final String residues) {
        if (residues != null) {
            this.residues = new StringBuffer(residues);
            this.length = residues.length();
            if (this.length == 0) {
                this.end = this.start;
            }
            else {
                this.end = this.start + this.length - 1;
            }
        }
    }
    
    public String getResidues() {
        if (this.residues == null) {
            return null;
        }
        return this.residues.toString();
    }
    
    public String getResidues(final int start, final int end) {
        if (this.length == 0) {
            return "";
        }
        final char[] carray = new char[end - start];
        try {
            this.residues.getChars(start - this.start, end - this.start, carray, 0);
        }
        catch (Exception e) {
            System.out.println("exception in Sequence.getResidues(start, end)");
            System.out.println("start = " + start + ", end = " + end);
            return null;
        }
        return new String(carray);
    }
    
    public final void setStart(final int start) {
        if (start < 0) {
            throw new IllegalArgumentException("Start cannot be negative");
        }
        this.start = start;
        this.end = this.start + this.length - 1;
    }
    
    public int getStart() {
        return this.start;
    }
    
    public int getLength() {
        return this.length;
    }
}
