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
import com.affymetrix.genometryImpl.parsers.useq.apps.Text2USeq;
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class RegionScoreTextData extends USeqData
{
    private RegionScoreText[] sortedRegionScoreTexts;

    public RegionScoreTextData() {
    }

    public RegionScoreTextData(final RegionScoreText[] sortedRegionScoreTexts, final SliceInfo sliceInfo) {
        this.sortedRegionScoreTexts = sortedRegionScoreTexts;
        this.sliceInfo = sliceInfo;
    }

    public RegionScoreTextData(final File binaryFile) throws IOException {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public RegionScoreTextData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public static void updateSliceInfo(final RegionScoreText[] sortedRegionScoreTexts, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedRegionScoreTexts[0].getStart());
        sliceInfo.setLastStartPosition(sortedRegionScoreTexts[sortedRegionScoreTexts.length - 1].start);
        sliceInfo.setNumberRecords(sortedRegionScoreTexts.length);
    }

    public int fetchLastBase() {
        int lastBase = -1;
        for (final RegionScoreText r : this.sortedRegionScoreTexts) {
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
        for (int i = 0; i < this.sortedRegionScoreTexts.length; ++i) {
            final String[] tokens = Text2USeq.PATTERN_TAB.split(this.sortedRegionScoreTexts[i].text);
            if (fixScore) {
                final int score = USeqUtilities.fixBedScore(this.sortedRegionScoreTexts[i].score);
                if (tokens.length == 7) {
                    out.println(chrom + "\t" + this.sortedRegionScoreTexts[i].start + "\t" + this.sortedRegionScoreTexts[i].stop + "\t" + tokens[0] + "\t" + score + "\t" + strand + "\t" + tokens[1] + "\t" + tokens[2] + "\t" + tokens[3] + "\t" + tokens[4] + "\t" + tokens[5] + "\t" + tokens[6]);
                }
                else {
                    out.println(chrom + "\t" + this.sortedRegionScoreTexts[i].start + "\t" + this.sortedRegionScoreTexts[i].stop + "\t" + this.sortedRegionScoreTexts[i].text + "\t" + score + "\t" + strand);
                }
            }
            else if (tokens.length == 7) {
                out.println(chrom + "\t" + this.sortedRegionScoreTexts[i].start + "\t" + this.sortedRegionScoreTexts[i].stop + "\t" + tokens[0] + "\t" + this.sortedRegionScoreTexts[i].score + "\t" + strand + "\t" + tokens[1] + "\t" + tokens[2] + "\t" + tokens[3] + "\t" + tokens[4] + "\t" + tokens[5] + "\t" + tokens[6]);
            }
            else {
                out.println(chrom + "\t" + this.sortedRegionScoreTexts[i].start + "\t" + this.sortedRegionScoreTexts[i].stop + "\t" + this.sortedRegionScoreTexts[i].text + "\t" + this.sortedRegionScoreTexts[i].score + "\t" + strand);
            }
        }
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tStart\tStop\tScore\t(Text(s)");
            for (int i = 0; i < this.sortedRegionScoreTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegionScoreTexts[i].start + "\t" + this.sortedRegionScoreTexts[i].stop + "\t" + this.sortedRegionScoreTexts[i].score + "\t" + this.sortedRegionScoreTexts[i].text);
            }
        }
        else {
            out.println("#Chr\tStart\tStop\tScore\tText(s)\tStrand");
            for (int i = 0; i < this.sortedRegionScoreTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegionScoreTexts[i].start + "\t" + this.sortedRegionScoreTexts[i].stop + "\t" + this.sortedRegionScoreTexts[i].score + "\t" + this.sortedRegionScoreTexts[i].text + "\t" + strand);
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegionScoreTexts[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegionScoreTexts.length; ++i) {
                final int currentStart = this.sortedRegionScoreTexts[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegionScoreTexts.length; ++i) {
                final int diff2 = this.sortedRegionScoreTexts[i].stop - this.sortedRegionScoreTexts[i].start;
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
        fileType = fileType + "f" + "t";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedRegionScoreTexts[0].start);
            int bp2 = this.sortedRegionScoreTexts[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    workingDOS.writeInt(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start);
                    workingDOS.writeFloat(this.sortedRegionScoreTexts[0].score);
                    workingDOS.writeUTF(this.sortedRegionScoreTexts[0].text);
                    for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeInt(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start);
                        workingDOS.writeFloat(this.sortedRegionScoreTexts[j].score);
                        workingDOS.writeUTF(this.sortedRegionScoreTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
                else {
                    workingDOS.writeShort((short)(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start - 32768));
                    workingDOS.writeFloat(this.sortedRegionScoreTexts[0].score);
                    workingDOS.writeUTF(this.sortedRegionScoreTexts[0].text);
                    for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeShort((short)(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start - 32768));
                        workingDOS.writeFloat(this.sortedRegionScoreTexts[j].score);
                        workingDOS.writeUTF(this.sortedRegionScoreTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                workingDOS.writeInt(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start);
                workingDOS.writeFloat(this.sortedRegionScoreTexts[0].score);
                workingDOS.writeUTF(this.sortedRegionScoreTexts[0].text);
                for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeInt(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start);
                    workingDOS.writeFloat(this.sortedRegionScoreTexts[j].score);
                    workingDOS.writeUTF(this.sortedRegionScoreTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                workingDOS.writeShort((short)(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start - 32768));
                workingDOS.writeFloat(this.sortedRegionScoreTexts[0].score);
                workingDOS.writeUTF(this.sortedRegionScoreTexts[0].text);
                for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeShort((short)(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start - 32768));
                    workingDOS.writeFloat(this.sortedRegionScoreTexts[j].score);
                    workingDOS.writeUTF(this.sortedRegionScoreTexts[j].text);
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

    public static RegionScoreTextData merge(final ArrayList<RegionScoreTextData> pdAL) {
        final RegionScoreTextData[] pdArray = new RegionScoreTextData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedRegionScoreTexts.length;
        }
        final RegionScoreText[] concatinate = new RegionScoreText[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final RegionScoreText[] slice = pdArray[j].sortedRegionScoreTexts;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new RegionScoreTextData(concatinate, sliceInfo);
    }

    public static RegionScoreTextData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<RegionScoreTextData> a = new ArrayList<RegionScoreTextData>(num);
        for (int i = 0; i < num; ++i) {
            a.add((RegionScoreTextData)useqDataAL.get(i));
        }
        return merge(a);
    }

    public void write(final ZipOutputStream out, final DataOutputStream dos, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegionScoreTexts[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegionScoreTexts.length; ++i) {
                final int currentStart = this.sortedRegionScoreTexts[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegionScoreTexts.length; ++i) {
                final int diff2 = this.sortedRegionScoreTexts[i].stop - this.sortedRegionScoreTexts[i].start;
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
        fileType = fileType + "f" + "t";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedRegionScoreTexts[0].start);
            int bp2 = this.sortedRegionScoreTexts[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    dos.writeInt(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start);
                    dos.writeFloat(this.sortedRegionScoreTexts[0].score);
                    dos.writeUTF(this.sortedRegionScoreTexts[0].text);
                    for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeInt(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start);
                        dos.writeFloat(this.sortedRegionScoreTexts[j].score);
                        dos.writeUTF(this.sortedRegionScoreTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
                else {
                    dos.writeShort((short)(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start - 32768));
                    dos.writeFloat(this.sortedRegionScoreTexts[0].score);
                    dos.writeUTF(this.sortedRegionScoreTexts[0].text);
                    for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeShort((short)(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start - 32768));
                        dos.writeFloat(this.sortedRegionScoreTexts[j].score);
                        dos.writeUTF(this.sortedRegionScoreTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                dos.writeInt(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start);
                dos.writeFloat(this.sortedRegionScoreTexts[0].score);
                dos.writeUTF(this.sortedRegionScoreTexts[0].text);
                for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeInt(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start);
                    dos.writeFloat(this.sortedRegionScoreTexts[j].score);
                    dos.writeUTF(this.sortedRegionScoreTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                dos.writeShort((short)(this.sortedRegionScoreTexts[0].stop - this.sortedRegionScoreTexts[0].start - 32768));
                dos.writeFloat(this.sortedRegionScoreTexts[0].score);
                dos.writeUTF(this.sortedRegionScoreTexts[0].text);
                for (int j = 1; j < this.sortedRegionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionScoreTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeShort((short)(this.sortedRegionScoreTexts[j].stop - this.sortedRegionScoreTexts[j].start - 32768));
                    dos.writeFloat(this.sortedRegionScoreTexts[j].score);
                    dos.writeUTF(this.sortedRegionScoreTexts[j].text);
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
            final int numberRegionScoreTexts = this.sliceInfo.getNumberRecords();
            this.sortedRegionScoreTexts = new RegionScoreText[numberRegionScoreTexts];
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.REGION_SCORE_TEXT_INT_INT_FLOAT_TEXT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionScoreTexts[0] = new RegionScoreText(start, start + dis.readInt(), dis.readFloat(), dis.readUTF());
                for (int i = 1; i < numberRegionScoreTexts; ++i) {
                    start = this.sortedRegionScoreTexts[i - 1].start + dis.readInt();
                    this.sortedRegionScoreTexts[i] = new RegionScoreText(start, start + dis.readInt(), dis.readFloat(), dis.readUTF());
                }
            }
            else if (USeqUtilities.REGION_SCORE_TEXT_INT_SHORT_FLOAT_TEXT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionScoreTexts[0] = new RegionScoreText(start, start + dis.readShort() + 32768, dis.readFloat(), dis.readUTF());
                for (int i = 1; i < numberRegionScoreTexts; ++i) {
                    start = this.sortedRegionScoreTexts[i - 1].start + dis.readInt();
                    this.sortedRegionScoreTexts[i] = new RegionScoreText(start, start + dis.readShort() + 32768, dis.readFloat(), dis.readUTF());
                }
            }
            else if (USeqUtilities.REGION_SCORE_TEXT_SHORT_SHORT_FLOAT_TEXT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionScoreTexts[0] = new RegionScoreText(start, start + dis.readShort() + 32768, dis.readFloat(), dis.readUTF());
                for (int i = 1; i < numberRegionScoreTexts; ++i) {
                    start = this.sortedRegionScoreTexts[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegionScoreTexts[i] = new RegionScoreText(start, start + dis.readShort() + 32768, dis.readFloat(), dis.readUTF());
                }
            }
            else {
                if (!USeqUtilities.REGION_SCORE_TEXT_SHORT_INT_FLOAT_TEXT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a RegionScoreText[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                int start = dis.readInt();
                this.sortedRegionScoreTexts[0] = new RegionScoreText(start, start + dis.readInt(), dis.readFloat(), dis.readUTF());
                for (int i = 1; i < numberRegionScoreTexts; ++i) {
                    start = this.sortedRegionScoreTexts[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegionScoreTexts[i] = new RegionScoreText(start, start + dis.readInt(), dis.readFloat(), dis.readUTF());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public RegionScoreText[] getRegionScoreTexts() {
        return this.sortedRegionScoreTexts;
    }

    public void setRegionScoreTexts(final RegionScoreText[] sortedRegionScoreTexts) {
        updateSliceInfo(this.sortedRegionScoreTexts = sortedRegionScoreTexts, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<RegionScoreText> al = new ArrayList<RegionScoreText>();
        for (int i = 0; i < this.sortedRegionScoreTexts.length; ++i) {
            if (this.sortedRegionScoreTexts[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedRegionScoreTexts[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedRegionScoreTexts = new RegionScoreText[al.size()]);
        updateSliceInfo(this.sortedRegionScoreTexts, this.sliceInfo);
        return true;
    }
}
