// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq.data;

import java.util.Iterator;
import java.io.BufferedReader;
import java.util.Arrays;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.util.HashMap;
import java.io.File;
import java.util.ArrayList;
import java.io.Serializable;

public class Region implements Comparable<Region>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected int start;
    protected int stop;
    
    public Region(final int start, final int stop) {
        this.start = start;
        this.stop = stop;
    }
    
    @Override
    public String toString() {
        return this.start + "\t" + this.stop;
    }
    
    public boolean intersects(final int start, final int stop) {
        return stop > this.start && start < this.stop;
    }
    
    public static boolean checkStartStops(final Region[] ss) {
        for (int i = 0; i < ss.length; ++i) {
            if (ss[i].start > ss[i].stop) {
                return false;
            }
        }
        return true;
    }
    
    public static Region[] makeStartStops(final short[] baseHitCount) {
        final ArrayList<Region> ss = new ArrayList<Region>();
        int i;
        for (i = 0; i < baseHitCount.length && baseHitCount[i] == 0; ++i) {}
        if (i == baseHitCount.length) {
            return null;
        }
        int start = i;
        int val = baseHitCount[start];
        while (i < baseHitCount.length) {
            if (baseHitCount[i] != val) {
                if (val != 0) {
                    ss.add(new Region(start, i));
                }
                start = i;
                val = baseHitCount[start];
            }
            ++i;
        }
        if (val != 0) {
            ss.add(new Region(start, i));
        }
        final Region[] ssA = new Region[ss.size()];
        ss.toArray(ssA);
        return ssA;
    }
    
    @Override
    public int compareTo(final Region se) {
        if (this.start < se.start) {
            return -1;
        }
        if (this.start > se.start) {
            return 1;
        }
        final int len = this.stop - this.start;
        final int otherLen = se.stop - se.start;
        if (len < otherLen) {
            return -1;
        }
        if (len > otherLen) {
            return 1;
        }
        return 0;
    }
    
    public static int totalBP(final Region[] ss) {
        int total = 0;
        for (int i = 0; i < ss.length; ++i) {
            total += ss[i].getLength();
        }
        return total;
    }
    
    public static int[] startsInBases(final Region[] ss) {
        final int[] indexes = new int[ss.length];
        int index = 0;
        for (int i = 0; i < ss.length; ++i) {
            index += ss[i].getLength();
            indexes[i] = index;
        }
        return indexes;
    }
    
    public int getLength() {
        return this.stop - this.start;
    }
    
    public int getStop() {
        return this.stop;
    }
    
    public int getStart() {
        return this.start;
    }
    
    public int[] getStartStop() {
        return new int[] { this.start, this.stop };
    }
    
    public boolean isContainedBy(final int beginningBP, final int endingBP) {
        return this.start >= beginningBP && this.stop < endingBP;
    }
    
    public static HashMap<String, Region[]> parseStartStops(final File bedFile, final int subStart, final int subEnd, final int minSize) {
        final HashMap<String, ArrayList<Region>> ss = new HashMap<String, ArrayList<Region>>();
        try {
            final BufferedReader in = USeqUtilities.fetchBufferedReader(bedFile);
            ArrayList<Region> al = new ArrayList<Region>();
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.length() != 0) {
                    if (line.startsWith("#")) {
                        continue;
                    }
                    final String[] tokens = line.split("\\s+");
                    if (tokens.length < 3) {
                        continue;
                    }
                    if (ss.containsKey(tokens[0])) {
                        al = ss.get(tokens[0]);
                    }
                    else {
                        al = new ArrayList<Region>();
                        ss.put(tokens[0], al);
                    }
                    final int start = Integer.parseInt(tokens[1]);
                    final int stop = Integer.parseInt(tokens[2]);
                    if (start > stop) {
                        throw new Exception("\nFound a start that is greater than stop!  Cannot parse file " + bedFile + ", bad line->\n\t" + line);
                    }
                    if (start < 0) {
                        throw new Exception("\nFound a start with a negative value!  Cannot parse file " + bedFile + ", bad line->\n\t" + line);
                    }
                    final int length = stop - start;
                    if (length < minSize) {
                        continue;
                    }
                    al.add(new Region(start - subStart, stop - subEnd));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (ss.size() == 0) {
            return null;
        }
        final HashMap<String, Region[]> ssReal = new HashMap<String, Region[]>();
        for (final String chrom : ss.keySet()) {
            final ArrayList<Region> al = ss.get(chrom);
            final Region[] array = new Region[al.size()];
            al.toArray(array);
            Arrays.sort(array);
            ssReal.put(chrom, array);
        }
        return ssReal;
    }
    
    public int getMiddle() {
        if (this.start == this.stop) {
            return this.start;
        }
        final double length = this.stop - this.start;
        final double halfLength = length / 2.0;
        return (int)Math.round(halfLength) + this.start;
    }
    
    public static int findLastBase(final Region[] r) {
        int lastBase = -1;
        for (int i = 0; i < r.length; ++i) {
            if (r[i].stop > lastBase) {
                lastBase = r[i].stop;
            }
        }
        return lastBase;
    }
}
