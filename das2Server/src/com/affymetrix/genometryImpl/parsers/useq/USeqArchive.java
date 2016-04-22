// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.useq;

import java.util.Random;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.zip.ZipOutputStream;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.RegionData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionTextData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionScoreData;
import com.affymetrix.genometryImpl.parsers.useq.data.PositionData;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.parsers.useq.data.USeqData;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.File;

public class USeqArchive
{
    private File zipFile;
    private ZipFile zipArchive;
    private ArchiveInfo archiveInfo;
    private ZipEntry archiveReadMeEntry;
    private String binaryDataType;
    private HashMap<String, DataRange[]> chromStrandRegions;
    private boolean maintainStrandedness;
    private boolean stranded;
    public static String[] nonAmbiguousLetters;
    
    public USeqArchive(final File zipFile) throws Exception {
        this.chromStrandRegions = new HashMap<String, DataRange[]>();
        this.maintainStrandedness = false;
        this.stranded = false;
        this.zipFile = zipFile;
        this.parseZipFile();
    }
    
    public USeqData[] fetch(final String chromosome, final int beginningBP, final int endingBP) {
        final ArrayList<ZipEntry> entries = this.fetchZipEntries(chromosome, beginningBP, endingBP);
        if (entries == null) {
            return null;
        }
        final ArrayList<USeqData> useqDataALPlus = new ArrayList<USeqData>();
        final ArrayList<USeqData> useqDataALMinus = new ArrayList<USeqData>();
        final ArrayList<USeqData> useqDataALNone = new ArrayList<USeqData>();
        BufferedInputStream bis = null;
        try {
            for (int numEntries = entries.size(), i = 0; i < numEntries; ++i) {
                final ZipEntry entry = entries.get(i);
                bis = new BufferedInputStream(this.zipArchive.getInputStream(entry));
                final SliceInfo sliceInfo = new SliceInfo(entry.getName());
                final USeqData d = this.loadSlice(beginningBP, endingBP, sliceInfo, bis);
                if (d != null) {
                    if (sliceInfo.getStrand().equals("+")) {
                        useqDataALPlus.add(d);
                    }
                    else if (sliceInfo.getStrand().equals("+")) {
                        useqDataALMinus.add(d);
                    }
                    else {
                        useqDataALNone.add(d);
                    }
                }
                bis.close();
            }
            USeqData plus = null;
            USeqData minus = null;
            final USeqData non = null;
            if (useqDataALPlus.size() != 0) {
                plus = this.mergeUSeqData(useqDataALPlus);
            }
            if (useqDataALMinus.size() != 0) {
                minus = this.mergeUSeqData(useqDataALMinus);
            }
            if (useqDataALNone.size() != 0) {
                minus = this.mergeUSeqData(useqDataALNone);
            }
            if (plus != null || minus != null || non != null) {
                return new USeqData[] { plus, minus, non };
            }
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(bis);
            return null;
        }
    }
    
    public USeqData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        if (USeqUtilities.POSITION.matcher(this.binaryDataType).matches()) {
            return PositionData.mergeUSeqData(useqDataAL);
        }
        if (USeqUtilities.POSITION_SCORE.matcher(this.binaryDataType).matches()) {
            return PositionScoreData.mergeUSeqData(useqDataAL);
        }
        if (USeqUtilities.POSITION_TEXT.matcher(this.binaryDataType).matches()) {
            return PositionTextData.mergeUSeqData(useqDataAL);
        }
        if (USeqUtilities.POSITION_SCORE_TEXT.matcher(this.binaryDataType).matches()) {
            return PositionScoreTextData.mergeUSeqData(useqDataAL);
        }
        if (USeqUtilities.REGION.matcher(this.binaryDataType).matches()) {
            return RegionData.mergeUSeqData(useqDataAL);
        }
        if (USeqUtilities.REGION_SCORE.matcher(this.binaryDataType).matches()) {
            return RegionScoreData.mergeUSeqData(useqDataAL);
        }
        if (USeqUtilities.REGION_TEXT.matcher(this.binaryDataType).matches()) {
            return RegionTextData.mergeUSeqData(useqDataAL);
        }
        if (USeqUtilities.REGION_SCORE_TEXT.matcher(this.binaryDataType).matches()) {
            return RegionScoreTextData.mergeUSeqData(useqDataAL);
        }
        return null;
    }
    
    public boolean writeSlicesToStream(final OutputStream outputStream, final String chromosome, final int beginningBP, final int endingBP, final boolean closeStream) {
        final ArrayList<ZipEntry> entries = this.fetchZipEntries(chromosome, beginningBP, endingBP);
        if (entries == null) {
            return false;
        }
        entries.add(0, this.archiveReadMeEntry);
        final ZipOutputStream out = new ZipOutputStream(outputStream);
        final DataOutputStream dos = new DataOutputStream(out);
        BufferedInputStream bis = null;
        try {
            final byte[] data = new byte[2048];
            final int numEntries = entries.size();
            SliceInfo sliceInfo = null;
            for (int i = 0; i < numEntries; ++i) {
                final ZipEntry entry = entries.get(i);
                bis = new BufferedInputStream(this.zipArchive.getInputStream(entry));
                if (i != 0) {
                    sliceInfo = new SliceInfo(entry.getName());
                }
                if (i == 0 || sliceInfo.isContainedBy(beginningBP, endingBP)) {
                    out.putNextEntry(entry);
                    int count;
                    while ((count = bis.read(data, 0, 2048)) != -1) {
                        out.write(data, 0, count);
                    }
                    out.closeEntry();
                }
                else {
                    this.sliceAndWriteEntry(beginningBP, endingBP, sliceInfo, bis, out, dos);
                }
                bis.close();
            }
            if (closeStream) {
                out.close();
                outputStream.close();
                dos.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(outputStream);
            USeqUtilities.safeClose(bis);
            USeqUtilities.safeClose(dos);
            return false;
        }
        return true;
    }
    
    private USeqData loadSlice(final int beginningBP, final int endingBP, final SliceInfo sliceInfo, final BufferedInputStream bis) {
        final DataInputStream dis = new DataInputStream(bis);
        USeqData d = null;
        try {
            if (USeqUtilities.POSITION.matcher(this.binaryDataType).matches()) {
                d = new PositionData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((PositionData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
            else if (USeqUtilities.POSITION_SCORE.matcher(this.binaryDataType).matches()) {
                d = new PositionScoreData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((PositionScoreData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
            else if (USeqUtilities.POSITION_TEXT.matcher(this.binaryDataType).matches()) {
                d = new PositionTextData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((PositionTextData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
            else if (USeqUtilities.POSITION_SCORE_TEXT.matcher(this.binaryDataType).matches()) {
                d = new PositionScoreTextData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((PositionScoreTextData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
            else if (USeqUtilities.REGION.matcher(this.binaryDataType).matches()) {
                d = new RegionData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((RegionData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
            else if (USeqUtilities.REGION_SCORE.matcher(this.binaryDataType).matches()) {
                d = new RegionScoreData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((RegionScoreData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
            else if (USeqUtilities.REGION_TEXT.matcher(this.binaryDataType).matches()) {
                d = new RegionTextData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((RegionTextData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
            else {
                if (!USeqUtilities.REGION_SCORE_TEXT.matcher(this.binaryDataType).matches()) {
                    throw new IOException("Unknown USeq data type, '" + this.binaryDataType + "', for slicing data from  -> '" + sliceInfo.getSliceName() + "\n");
                }
                d = new RegionScoreTextData(dis, sliceInfo);
                if (!sliceInfo.isContainedBy(beginningBP, endingBP) && !((RegionScoreTextData)d).trim(beginningBP, endingBP)) {
                    d = null;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(bis);
            return null;
        }
        return d;
    }
    
    private void sliceAndWriteEntry(final int beginningBP, final int endingBP, final SliceInfo sliceInfo, final BufferedInputStream bis, final ZipOutputStream out, final DataOutputStream dos) {
        final String dataType = sliceInfo.getBinaryType();
        final DataInputStream dis = new DataInputStream(bis);
        try {
            if (USeqUtilities.POSITION.matcher(dataType).matches()) {
                final PositionData d = new PositionData(dis, sliceInfo);
                if (d.trim(beginningBP, endingBP)) {
                    d.write(out, dos, true);
                }
            }
            else if (USeqUtilities.POSITION_SCORE.matcher(dataType).matches()) {
                final PositionScoreData d2 = new PositionScoreData(dis, sliceInfo);
                if (d2.trim(beginningBP, endingBP)) {
                    d2.write(out, dos, true);
                }
            }
            else if (USeqUtilities.POSITION_TEXT.matcher(dataType).matches()) {
                final PositionTextData d3 = new PositionTextData(dis, sliceInfo);
                if (d3.trim(beginningBP, endingBP)) {
                    d3.write(out, dos, true);
                }
            }
            else if (USeqUtilities.POSITION_SCORE_TEXT.matcher(dataType).matches()) {
                final PositionScoreTextData d4 = new PositionScoreTextData(dis, sliceInfo);
                if (d4.trim(beginningBP, endingBP)) {
                    d4.write(out, dos, true);
                }
            }
            else if (USeqUtilities.REGION.matcher(dataType).matches()) {
                final RegionData d5 = new RegionData(dis, sliceInfo);
                if (d5.trim(beginningBP, endingBP)) {
                    d5.write(out, dos, true);
                }
            }
            else if (USeqUtilities.REGION_SCORE.matcher(dataType).matches()) {
                final RegionScoreData d6 = new RegionScoreData(dis, sliceInfo);
                if (d6.trim(beginningBP, endingBP)) {
                    d6.write(out, dos, true);
                }
            }
            else if (USeqUtilities.REGION_TEXT.matcher(dataType).matches()) {
                final RegionTextData d7 = new RegionTextData(dis, sliceInfo);
                if (d7.trim(beginningBP, endingBP)) {
                    d7.write(out, dos, true);
                }
            }
            else {
                if (!USeqUtilities.REGION_SCORE_TEXT.matcher(dataType).matches()) {
                    throw new IOException("Unknown USeq data type, '" + dataType + "', for slicing data from  -> '" + sliceInfo.getSliceName() + "\n");
                }
                final RegionScoreTextData d8 = new RegionScoreTextData(dis, sliceInfo);
                if (d8.trim(beginningBP, endingBP)) {
                    d8.write(out, dos, true);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(bis);
        }
        finally {
            USeqUtilities.safeClose(dis);
        }
    }
    
    public File writeSlicesToFile(final File saveDirectory, final String chromosome, final int beginningBP, final int endingBP) {
        final ArrayList<ZipEntry> entries = this.fetchZipEntries(chromosome, beginningBP, endingBP);
        if (entries == null) {
            return null;
        }
        entries.add(0, this.archiveReadMeEntry);
        final File slicedZipArchive = new File(saveDirectory, "USeqDataSlice_" + createRandowWord(7) + "." + "useq");
        ZipOutputStream out = null;
        BufferedInputStream is = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(slicedZipArchive));
            final byte[] data = new byte[2048];
            for (int numEntries = entries.size(), i = 0; i < numEntries; ++i) {
                final ZipEntry entry = entries.get(i);
                out.putNextEntry(entry);
                is = new BufferedInputStream(this.zipArchive.getInputStream(entry));
                int count;
                while ((count = is.read(data, 0, 2048)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
                is.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(is);
        }
        return slicedZipArchive;
    }
    
    public ArrayList<ZipEntry> fetchZipEntries(final String chromStrand, final int beginningBP, final int endingBP) {
        final ArrayList<ZipEntry> al = new ArrayList<ZipEntry>();
        final DataRange[] dr = this.chromStrandRegions.get(chromStrand);
        if (dr == null) {
            return null;
        }
        for (int i = 0; i < dr.length; ++i) {
            if (dr[i].intersects(beginningBP, endingBP)) {
                al.add(dr[i].zipEntry);
            }
        }
        if (al.size() == 0) {
            return null;
        }
        return al;
    }
    
    private void parseZipFile() {
        InputStream is = null;
        try {
            if (!USeqUtilities.USEQ_ARCHIVE.matcher(this.zipFile.getName()).matches()) {
                throw new IOException("This file does not appear to be a USeq archive! " + this.zipFile);
            }
            this.zipArchive = new ZipFile(this.zipFile);
            final Enumeration<? extends ZipEntry> e = this.zipArchive.entries();
            this.archiveReadMeEntry = (ZipEntry)e.nextElement();
            is = this.zipArchive.getInputStream(this.archiveReadMeEntry);
            this.archiveInfo = new ArchiveInfo(is, false);
            final HashMap<String, ArrayList<DataRange>> map = new HashMap<String, ArrayList<DataRange>>();
            while (e.hasMoreElements()) {
                final ZipEntry zipEntry = (ZipEntry)e.nextElement();
                final SliceInfo sliceInfo = new SliceInfo(zipEntry.getName());
                if (this.binaryDataType == null) {
                    this.binaryDataType = sliceInfo.getBinaryType();
                }
                String chromName;
                if (this.maintainStrandedness) {
                    chromName = sliceInfo.getChromosome() + sliceInfo.getStrand();
                }
                else {
                    chromName = sliceInfo.getChromosome();
                }
                if (!sliceInfo.getStrand().equals(".")) {
                    this.stranded = true;
                }
                ArrayList<DataRange> al = map.get(chromName);
                if (al == null) {
                    al = new ArrayList<DataRange>();
                    map.put(chromName, al);
                }
                al.add(new DataRange(zipEntry, sliceInfo.getFirstStartPosition(), sliceInfo.getLastStartPosition()));
            }
            for (final String chromName2 : map.keySet()) {
                final ArrayList<DataRange> al2 = map.get(chromName2);
                final DataRange[] dr = new DataRange[al2.size()];
                al2.toArray(dr);
                Arrays.sort(dr);
                this.chromStrandRegions.put(chromName2, dr);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        finally {
            USeqUtilities.safeClose(is);
        }
    }
    
    public static String[] createRandomWords(final String[] alphabet, final int lengthOfWord, final int numberOfWords) {
        final ArrayList<String> words = new ArrayList<String>();
        final Random r = new Random();
        final int len = alphabet.length;
        for (int i = 0; i < numberOfWords; ++i) {
            final StringBuffer w = new StringBuffer();
            for (int j = 0; j < lengthOfWord; ++j) {
                w.append(alphabet[r.nextInt(len)]);
            }
            words.add(w.toString());
        }
        final String[] w2 = new String[words.size()];
        words.toArray(w2);
        return w2;
    }
    
    public static String createRandowWord(final int lengthOfWord) {
        return createRandomWords(USeqArchive.nonAmbiguousLetters, lengthOfWord, 1)[0];
    }
    
    public ArchiveInfo getArchiveInfo() {
        return this.archiveInfo;
    }
    
    public String getBinaryDataType() {
        return this.binaryDataType;
    }
    
    public HashMap<String, Integer> fetchChromosomesAndLastBase() throws IOException {
        final HashMap<String, DataRange> map = new HashMap<String, DataRange>();
        for (final String chrom : this.chromStrandRegions.keySet()) {
            final DataRange[] dr = this.chromStrandRegions.get(chrom);
            int lastFirstBase = 0;
            DataRange lastDataRange = null;
            for (final DataRange d : dr) {
                if (d.endingBP > lastFirstBase) {
                    lastFirstBase = d.endingBP;
                    lastDataRange = d;
                }
            }
            map.put(chrom, lastDataRange);
        }
        final ZipFile zf = new ZipFile(this.zipFile);
        final HashMap<String, Integer> chromBase = new HashMap<String, Integer>();
        for (final String chrom2 : map.keySet()) {
            final DataRange dr2 = map.get(chrom2);
            final ZipEntry ze = dr2.zipEntry;
            final SliceInfo si = new SliceInfo(ze.getName());
            final DataInputStream dis = new DataInputStream(new BufferedInputStream(zf.getInputStream(ze)));
            final String extension = si.getBinaryType();
            int lastBase = -1;
            if (USeqUtilities.POSITION.matcher(extension).matches()) {
                lastBase = new PositionData(dis, si).fetchLastBase();
            }
            else if (USeqUtilities.POSITION_SCORE.matcher(extension).matches()) {
                lastBase = new PositionScoreData(dis, si).fetchLastBase();
            }
            else if (USeqUtilities.POSITION_TEXT.matcher(extension).matches()) {
                lastBase = new PositionTextData(dis, si).fetchLastBase();
            }
            else if (USeqUtilities.POSITION_SCORE_TEXT.matcher(extension).matches()) {
                lastBase = new PositionScoreTextData(dis, si).fetchLastBase();
            }
            else if (USeqUtilities.REGION.matcher(extension).matches()) {
                lastBase = new RegionData(dis, si).fetchLastBase();
            }
            else if (USeqUtilities.REGION_SCORE.matcher(extension).matches()) {
                lastBase = new RegionScoreData(dis, si).fetchLastBase();
            }
            else if (USeqUtilities.REGION_TEXT.matcher(extension).matches()) {
                lastBase = new RegionTextData(dis, si).fetchLastBase();
            }
            else {
                if (!USeqUtilities.REGION_SCORE_TEXT.matcher(extension).matches()) {
                    throw new IOException("\nFailed to recognize the binary file extension! " + ze.getName());
                }
                lastBase = new RegionScoreTextData(dis, si).fetchLastBase();
            }
            chromBase.put(chrom2, new Integer(lastBase));
        }
        return chromBase;
    }
    
    public File getZipFile() {
        return this.zipFile;
    }
    
    public boolean isStranded() {
        return this.stranded;
    }
    
    static {
        USeqArchive.nonAmbiguousLetters = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "T", "U", "V", "W", "X", "Y", "3", "4", "6", "7", "8", "9" };
    }
    
    private class DataRange implements Comparable<DataRange>
    {
        ZipEntry zipEntry;
        int beginningBP;
        int endingBP;
        
        public DataRange(final ZipEntry zipEntry, final int beginningBP, final int endingBP) {
            this.zipEntry = zipEntry;
            this.beginningBP = beginningBP;
            this.endingBP = endingBP;
        }
        
        public boolean intersects(final int start, final int stop) {
            return stop > this.beginningBP && start < this.endingBP;
        }
        
        @Override
        public int compareTo(final DataRange other) {
            if (this.beginningBP < other.beginningBP) {
                return -1;
            }
            if (this.beginningBP > other.beginningBP) {
                return 1;
            }
            return 0;
        }
    }
}
