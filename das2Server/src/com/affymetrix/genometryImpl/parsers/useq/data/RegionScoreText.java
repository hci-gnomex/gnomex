// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

import java.io.IOException;
import java.io.EOFException;
import java.util.Arrays;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.File;

public class RegionScoreText extends RegionScore
{
    protected String text;
    private static final long serialVersionUID = 1L;
    
    public RegionScoreText(final int start, final int stop, final float score, final String text) {
        super(start, stop, score);
        this.text = text;
    }
    
    public static RegionScoreText[] oldLoadBinary_DEPRECIATED(final File binaryFile, final boolean sort) {
        final ArrayList<RegionScoreText> sss = new ArrayList<RegionScoreText>(10000);
        DataInputStream dis = null;
        int start = 0;
        int stop = 0;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(binaryFile)));
            while (true) {
                start = dis.readInt();
                stop = dis.readInt();
                final float score = dis.readFloat();
                final byte[] barray = new byte[dis.readInt()];
                dis.readFully(barray);
                final String name = new String(barray);
                sss.add(new RegionScoreText(start, stop, score, name));
            }
        }
        catch (EOFException eof) {
            final RegionScoreText[] s = new RegionScoreText[sss.size()];
            sss.toArray(s);
            if (sort) {
                Arrays.sort(s);
            }
            return s;
        }
        catch (Exception e) {
            System.out.println("\nBad binary file " + binaryFile);
            e.printStackTrace();
            return null;
        }
        finally {
            if (dis != null) {
                try {
                    dis.close();
                }
                catch (IOException ex) {}
            }
        }
    }
    
    @Override
    public String toString() {
        return this.start + "\t" + this.stop + "\t" + this.score + "\t" + this.text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
}
