//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.useq.data;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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

public class PositionScoreTextData extends USeqData
{
    private PositionScoreText[] sortedPositionScoreTexts;
    private int[] basePositions;
    private float[] scores;

    public PositionScoreTextData() {
    }

    public PositionScoreTextData(final PositionScoreText[] sortedPositionScoreTexts, final SliceInfo sliceInfo) {
        this.sortedPositionScoreTexts = sortedPositionScoreTexts;
        this.sliceInfo = sliceInfo;
    }

    public PositionScoreTextData(final File binaryFile) throws IOException {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public PositionScoreTextData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public static void updateSliceInfo(final PositionScoreText[] sortedPositionScoreTexts, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedPositionScoreTexts[0].position);
        sliceInfo.setLastStartPosition(sortedPositionScoreTexts[sortedPositionScoreTexts.length - 1].position);
        sliceInfo.setNumberRecords(sortedPositionScoreTexts.length);
    }

    public int fetchLastBase() {
        return this.sortedPositionScoreTexts[this.sortedPositionScoreTexts.length - 1].position;
    }

    public void writeBed(final PrintWriter out, final boolean fixScore) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        for (int i = 0; i < this.sortedPositionScoreTexts.length; ++i) {
            final String[] tokens = Text2USeq.PATTERN_TAB.split(this.sortedPositionScoreTexts[i].text);
            if (fixScore) {
                final int score = USeqUtilities.fixBedScore(this.sortedPositionScoreTexts[i].score);
                if (tokens.length == 7) {
                    out.println(chrom + "\t" + this.sortedPositionScoreTexts[i].position + "\t" + (this.sortedPositionScoreTexts[i].position + 1) + "\t" + tokens[0] + "\t" + score + "\t" + strand + "\t" + tokens[1] + "\t" + tokens[2] + "\t" + tokens[3] + "\t" + tokens[4] + "\t" + tokens[5] + "\t" + tokens[6]);
                }
                else {
                    out.println(chrom + "\t" + this.sortedPositionScoreTexts[i].position + "\t" + (this.sortedPositionScoreTexts[i].position + 1) + "\t" + this.sortedPositionScoreTexts[i].text + "\t" + score + "\t" + strand);
                }
            }
            else if (tokens.length == 7) {
                out.println(chrom + "\t" + this.sortedPositionScoreTexts[i].position + "\t" + (this.sortedPositionScoreTexts[i].position + 1) + "\t" + tokens[0] + "\t" + this.sortedPositionScoreTexts[i].score + "\t" + strand + "\t" + tokens[1] + "\t" + tokens[2] + "\t" + tokens[3] + "\t" + tokens[4] + "\t" + tokens[5] + "\t" + tokens[6]);
            }
            else {
                out.println(chrom + "\t" + this.sortedPositionScoreTexts[i].position + "\t" + (this.sortedPositionScoreTexts[i].position + 1) + "\t" + this.sortedPositionScoreTexts[i].text + "\t" + this.sortedPositionScoreTexts[i].score + "\t" + strand);
            }
        }
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tPosition\tScore\tText(s)");
            for (int i = 0; i < this.sortedPositionScoreTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositionScoreTexts[i].position + "\t" + this.sortedPositionScoreTexts[i].score + "\t" + this.sortedPositionScoreTexts[i].text);
            }
        }
        else {
            out.println("#Chr\tPosition\tScore\tText(s)\tStrand");
            for (int i = 0; i < this.sortedPositionScoreTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositionScoreTexts[i].position + "\t" + this.sortedPositionScoreTexts[i].score + "\t" + this.sortedPositionScoreTexts[i].text + "\t" + strand);
            }
        }
    }

    public void writePositionScore(final PrintWriter out) {
        int prior = -1;
        for (int i = 0; i < this.sortedPositionScoreTexts.length; ++i) {
            if (prior != this.sortedPositionScoreTexts[i].position) {
                out.println(this.sortedPositionScoreTexts[i].position + 1 + "\t" + this.sortedPositionScoreTexts[i].score);
                prior = this.sortedPositionScoreTexts[i].position;
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedPositionScoreTexts[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositionScoreTexts.length; ++i) {
                final int currentStart = this.sortedPositionScoreTexts[i].position;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShort = false;
                    break;
                }
                bp = currentStart;
            }
        }
        String fileType;
        if (useShort) {
            fileType = "sf";
        }
        else {
            fileType = "if";
        }
        fileType += "t";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedPositionScoreTexts[0].position);
            workingDOS.writeFloat(this.sortedPositionScoreTexts[0].score);
            workingDOS.writeUTF(this.sortedPositionScoreTexts[0].text);
            if (useShort) {
                int bp2 = this.sortedPositionScoreTexts[0].position;
                for (int j = 1; j < this.sortedPositionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionScoreTexts[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    workingDOS.writeShort((short)diff2);
                    workingDOS.writeFloat(this.sortedPositionScoreTexts[j].score);
                    workingDOS.writeUTF(this.sortedPositionScoreTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositionScoreTexts[0].position;
                for (int j = 1; j < this.sortedPositionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionScoreTexts[j].position;
                    final int diff2 = currentStart2 - bp2;
                    workingDOS.writeInt(diff2);
                    workingDOS.writeFloat(this.sortedPositionScoreTexts[j].score);
                    workingDOS.writeUTF(this.sortedPositionScoreTexts[j].text);
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

    public void write(final ZipOutputStream out, final DataOutputStream dos, final boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedPositionScoreTexts[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositionScoreTexts.length; ++i) {
                final int currentStart = this.sortedPositionScoreTexts[i].position;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShort = false;
                    break;
                }
                bp = currentStart;
            }
        }
        String fileType;
        if (useShort) {
            fileType = "sf";
        }
        else {
            fileType = "if";
        }
        fileType += "t";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedPositionScoreTexts[0].position);
            dos.writeFloat(this.sortedPositionScoreTexts[0].score);
            dos.writeUTF(this.sortedPositionScoreTexts[0].text);
            if (useShort) {
                int bp2 = this.sortedPositionScoreTexts[0].position;
                for (int j = 1; j < this.sortedPositionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionScoreTexts[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    dos.writeShort((short)diff2);
                    dos.writeFloat(this.sortedPositionScoreTexts[j].score);
                    dos.writeUTF(this.sortedPositionScoreTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositionScoreTexts[0].position;
                for (int j = 1; j < this.sortedPositionScoreTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionScoreTexts[j].position;
                    final int diff2 = currentStart2 - bp2;
                    dos.writeInt(diff2);
                    dos.writeFloat(this.sortedPositionScoreTexts[j].score);
                    dos.writeUTF(this.sortedPositionScoreTexts[j].text);
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

    public static PositionScoreTextData merge(final ArrayList<PositionScoreTextData> pdAL) {
        final PositionScoreTextData[] pdArray = new PositionScoreTextData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedPositionScoreTexts.length;
        }
        final PositionScoreText[] concatinate = new PositionScoreText[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final PositionScoreText[] slice = pdArray[j].sortedPositionScoreTexts;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new PositionScoreTextData(concatinate, sliceInfo);
    }

    public static PositionScoreTextData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<PositionScoreTextData> a = new ArrayList<PositionScoreTextData>(num);
        for (int i = 0; i < num; ++i) {
            a.add((PositionScoreTextData)useqDataAL.get(i));
        }
        return merge(a);
    }

    @Override
    public void read(final DataInputStream dis) {
        try {
            this.header = dis.readUTF();
            final int numberPositions = this.sliceInfo.getNumberRecords();
            (this.sortedPositionScoreTexts = new PositionScoreText[numberPositions])[0] = new PositionScoreText(dis.readInt(), dis.readFloat(), dis.readUTF());
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.POSITION_SCORE_TEXT_INT_FLOAT_TEXT.matcher(fileType).matches()) {
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositionScoreTexts[i] = new PositionScoreText(this.sortedPositionScoreTexts[i - 1].position + dis.readInt(), dis.readFloat(), dis.readUTF());
                }
            }
            else {
                if (!USeqUtilities.POSITION_SCORE_TEXT_SHORT_FLOAT_TEXT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a PositionScoreText[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositionScoreTexts[i] = new PositionScoreText(this.sortedPositionScoreTexts[i - 1].position + dis.readShort() + 32768, dis.readFloat(), dis.readUTF());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public PositionScoreText[] getPositionScoreTexts() {
        return this.sortedPositionScoreTexts;
    }

    public void setPositionScoreTexts(final PositionScoreText[] sortedPositionScoreTexts) {
        updateSliceInfo(this.sortedPositionScoreTexts = sortedPositionScoreTexts, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<PositionScoreText> al = new ArrayList<PositionScoreText>();
        for (int i = 0; i < this.sortedPositionScoreTexts.length; ++i) {
            if (this.sortedPositionScoreTexts[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedPositionScoreTexts[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedPositionScoreTexts = new PositionScoreText[al.size()]);
        updateSliceInfo(this.sortedPositionScoreTexts, this.sliceInfo);
        return true;
    }

    public int[] getBasePositions() {
        if (this.basePositions == null) {
            this.basePositions = new int[this.sortedPositionScoreTexts.length];
            this.scores = new float[this.sortedPositionScoreTexts.length];
            for (int i = 0; i < this.basePositions.length; ++i) {
                this.basePositions[i] = this.sortedPositionScoreTexts[i].position;
                this.scores[i] = this.sortedPositionScoreTexts[i].score;
            }
        }
        return this.basePositions;
    }

    public float[] getBaseScores() {
        if (this.scores == null) {
            this.getBasePositions();
        }
        return this.scores;
    }
}
