//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.useq.data;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class RegionScoreData extends USeqData
{
    private RegionScore[] sortedRegionScores;

    public RegionScoreData() {
    }

    public RegionScoreData(final RegionScore[] sortedRegionScores, final SliceInfo sliceInfo) {
        this.sortedRegionScores = sortedRegionScores;
        this.sliceInfo = sliceInfo;
    }

    public RegionScoreData(final File binaryFile) throws IOException {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public RegionScoreData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public static void updateSliceInfo(final RegionScore[] sortedRegionScores, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedRegionScores[0].getStart());
        sliceInfo.setLastStartPosition(sortedRegionScores[sortedRegionScores.length - 1].start);
        sliceInfo.setNumberRecords(sortedRegionScores.length);
    }

    public int fetchLastBase() {
        int lastBase = -1;
        for (final RegionScore r : this.sortedRegionScores) {
            final int end = r.getStop();
            if (end > lastBase) {
                lastBase = end;
            }
        }
        return lastBase;
    }

    public void writeBed(final PrintWriter out, final boolean fixScore) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        for (int i = 0; i < this.sortedRegionScores.length; ++i) {
            if (fixScore) {
                final int score = USeqUtilities.fixBedScore(this.sortedRegionScores[i].score);
                out.println(chrom + "\t" + this.sortedRegionScores[i].start + "\t" + this.sortedRegionScores[i].stop + "\t" + ".\t" + score + "\t" + strand);
            }
            else {
                out.println(chrom + "\t" + this.sortedRegionScores[i].start + "\t" + this.sortedRegionScores[i].stop + "\t" + ".\t" + this.sortedRegionScores[i].score + "\t" + strand);
            }
        }
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tStart\tStop\tScore");
            for (int i = 0; i < this.sortedRegionScores.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegionScores[i].start + "\t" + this.sortedRegionScores[i].stop + "\t" + this.sortedRegionScores[i].score);
            }
        }
        else {
            out.println("#Chr\tStart\tStop\tScore\tStrand");
            for (int i = 0; i < this.sortedRegionScores.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegionScores[i].start + "\t" + this.sortedRegionScores[i].stop + "\t" + this.sortedRegionScores[i].score + "\t" + strand);
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegionScores[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegionScores.length; ++i) {
                final int currentStart = this.sortedRegionScores[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegionScores.length; ++i) {
                final int diff2 = this.sortedRegionScores[i].stop - this.sortedRegionScores[i].start;
                if (diff2 > 65536) {
                    useShortLength = false;
                    break;
                }
            }
        }
        String fileType;
        if (useShortBeginning) {
            fileType = "s";
        }
        else {
            fileType = "i";
        }
        if (useShortLength) {
            fileType += "s";
        }
        else {
            fileType += "i";
        }
        fileType += "f";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedRegionScores[0].start);
            int bp2 = this.sortedRegionScores[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    workingDOS.writeInt(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start);
                    workingDOS.writeFloat(this.sortedRegionScores[0].score);
                    for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                        final int currentStart2 = this.sortedRegionScores[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeInt(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start);
                        workingDOS.writeFloat(this.sortedRegionScores[j].score);
                        bp2 = currentStart2;
                    }
                }
                else {
                    workingDOS.writeShort((short)(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start - 32768));
                    workingDOS.writeFloat(this.sortedRegionScores[0].score);
                    for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                        final int currentStart2 = this.sortedRegionScores[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeShort((short)(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start - 32768));
                        workingDOS.writeFloat(this.sortedRegionScores[j].score);
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                workingDOS.writeInt(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start);
                workingDOS.writeFloat(this.sortedRegionScores[0].score);
                for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                    final int currentStart2 = this.sortedRegionScores[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeInt(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start);
                    workingDOS.writeFloat(this.sortedRegionScores[j].score);
                    bp2 = currentStart2;
                }
            }
            else {
                workingDOS.writeShort((short)(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start - 32768));
                workingDOS.writeFloat(this.sortedRegionScores[0].score);
                for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                    final int currentStart2 = this.sortedRegionScores[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeShort((short)(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start - 32768));
                    workingDOS.writeFloat(this.sortedRegionScores[j].score);
                    bp2 = currentStart2;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.binaryFile = null;
        }
        finally {
            USeqUtilities.safeClose(workingDOS);
            USeqUtilities.safeClose(workingFOS);
        }
        return this.binaryFile;
    }

    public static RegionScoreData merge(final ArrayList<RegionScoreData> pdAL) {
        final RegionScoreData[] pdArray = new RegionScoreData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedRegionScores.length;
        }
        final RegionScore[] concatinate = new RegionScore[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final RegionScore[] slice = pdArray[j].sortedRegionScores;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new RegionScoreData(concatinate, sliceInfo);
    }

    public static RegionScoreData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<RegionScoreData> a = new ArrayList<RegionScoreData>(num);
        for (int i = 0; i < num; ++i) {
            a.add((RegionScoreData)useqDataAL.get(i));
        }
        return merge(a);
    }

    public void write(final ZipOutputStream out, final DataOutputStream dos, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegionScores[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegionScores.length; ++i) {
                final int currentStart = this.sortedRegionScores[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegionScores.length; ++i) {
                final int diff2 = this.sortedRegionScores[i].stop - this.sortedRegionScores[i].start;
                if (diff2 > 65536) {
                    useShortLength = false;
                    break;
                }
            }
        }
        String fileType;
        if (useShortBeginning) {
            fileType = "s";
        }
        else {
            fileType = "i";
        }
        if (useShortLength) {
            fileType += "s";
        }
        else {
            fileType += "i";
        }
        fileType += "f";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedRegionScores[0].start);
            int bp2 = this.sortedRegionScores[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    dos.writeInt(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start);
                    dos.writeFloat(this.sortedRegionScores[0].score);
                    for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                        final int currentStart2 = this.sortedRegionScores[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeInt(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start);
                        dos.writeFloat(this.sortedRegionScores[j].score);
                        bp2 = currentStart2;
                    }
                }
                else {
                    dos.writeShort((short)(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start - 32768));
                    dos.writeFloat(this.sortedRegionScores[0].score);
                    for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                        final int currentStart2 = this.sortedRegionScores[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeShort((short)(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start - 32768));
                        dos.writeFloat(this.sortedRegionScores[j].score);
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                dos.writeInt(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start);
                dos.writeFloat(this.sortedRegionScores[0].score);
                for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                    final int currentStart2 = this.sortedRegionScores[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeInt(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start);
                    dos.writeFloat(this.sortedRegionScores[j].score);
                    bp2 = currentStart2;
                }
            }
            else {
                dos.writeShort((short)(this.sortedRegionScores[0].stop - this.sortedRegionScores[0].start - 32768));
                dos.writeFloat(this.sortedRegionScores[0].score);
                for (int j = 1; j < this.sortedRegionScores.length; ++j) {
                    final int currentStart2 = this.sortedRegionScores[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeShort((short)(this.sortedRegionScores[j].stop - this.sortedRegionScores[j].start - 32768));
                    dos.writeFloat(this.sortedRegionScores[j].score);
                    bp2 = currentStart2;
                }
            }
            out.closeEntry();
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(dos);
        }
    }

    @Override
    public void read(final DataInputStream dis) {
        try {
            this.header = dis.readUTF();
            final int numberRegionScores = this.sliceInfo.getNumberRecords();
            this.sortedRegionScores = new RegionScore[numberRegionScores];
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.REGION_SCORE_INT_INT_FLOAT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionScores[0] = new RegionScore(start, start + dis.readInt(), dis.readFloat());
                for (int i = 1; i < numberRegionScores; ++i) {
                    start = this.sortedRegionScores[i - 1].start + dis.readInt();
                    this.sortedRegionScores[i] = new RegionScore(start, start + dis.readInt(), dis.readFloat());
                }
            }
            else if (USeqUtilities.REGION_SCORE_INT_SHORT_FLOAT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionScores[0] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());
                for (int i = 1; i < numberRegionScores; ++i) {
                    start = this.sortedRegionScores[i - 1].start + dis.readInt();
                    this.sortedRegionScores[i] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());
                }
            }
            else if (USeqUtilities.REGION_SCORE_SHORT_SHORT_FLOAT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionScores[0] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());
                for (int i = 1; i < numberRegionScores; ++i) {
                    start = this.sortedRegionScores[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegionScores[i] = new RegionScore(start, start + dis.readShort() + 32768, dis.readFloat());
                }
            }
            else {
                if (!USeqUtilities.REGION_SCORE_SHORT_INT_FLOAT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a RegionScore[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                int start = dis.readInt();
                this.sortedRegionScores[0] = new RegionScore(start, start + dis.readInt(), dis.readFloat());
                for (int i = 1; i < numberRegionScores; ++i) {
                    start = this.sortedRegionScores[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegionScores[i] = new RegionScore(start, start + dis.readInt(), dis.readFloat());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public RegionScore[] getRegionScores() {
        return this.sortedRegionScores;
    }

    public void setRegionScores(final RegionScore[] sortedRegionScores) {
        updateSliceInfo(this.sortedRegionScores = sortedRegionScores, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<RegionScore> al = new ArrayList<RegionScore>();
        for (int i = 0; i < this.sortedRegionScores.length; ++i) {
            if (this.sortedRegionScores[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedRegionScores[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedRegionScores = new RegionScore[al.size()]);
        updateSliceInfo(this.sortedRegionScores, this.sliceInfo);
        return true;
    }
}
