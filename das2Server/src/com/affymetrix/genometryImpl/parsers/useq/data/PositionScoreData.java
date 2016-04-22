//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.useq.data;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class PositionScoreData extends USeqData implements Comparable<PositionScoreData>
{
    private PositionScore[] sortedPositionScores;
    private int[] basePositions;
    private float[] scores;

    public PositionScoreData() {
    }

    public PositionScoreData(final PositionScore[] sortedPositionScores, final SliceInfo sliceInfo) {
        this.sortedPositionScores = sortedPositionScores;
        this.sliceInfo = sliceInfo;
    }

    public PositionScoreData(final File binaryFile) throws IOException {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public PositionScoreData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public PositionScoreData(final int[] positions, final float[] scoreArray, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.sortedPositionScores = new PositionScore[this.basePositions.length];
        for (int i = 0; i < this.sortedPositionScores.length; ++i) {
            this.sortedPositionScores[i] = new PositionScore(positions[i], scoreArray[i]);
        }
    }

    public void sliceWritePositionScoreData(final int rowChunkSize, final File saveDirectory, final ArrayList<File> files2Zip) {
        int beginningIndex = 0;
        int endIndex = 0;
        final int numberPositions = this.sortedPositionScores.length;
        do {
            PositionScore[] slice;
            if (rowChunkSize == -1) {
                beginningIndex = 0;
                endIndex = numberPositions;
                slice = this.sortedPositionScores;
            }
            else {
                beginningIndex = endIndex;
                endIndex = beginningIndex + rowChunkSize;
                if (endIndex > numberPositions) {
                    endIndex = numberPositions;
                }
                else {
                    final int endBP = this.sortedPositionScores[endIndex - 1].getPosition();
                    for (int i = endIndex; i < numberPositions; ++i) {
                        if (this.sortedPositionScores[i].getPosition() != endBP) {
                            break;
                        }
                        ++endIndex;
                    }
                }
                final int num = endIndex - beginningIndex;
                slice = new PositionScore[num];
                System.arraycopy(this.sortedPositionScores, beginningIndex, slice, 0, num);
            }
            updateSliceInfo(slice, this.sliceInfo);
            final PositionScoreData pd = new PositionScoreData(slice, this.sliceInfo);
            final File savedFile = pd.write(saveDirectory, true);
            files2Zip.add(savedFile);
        } while (endIndex != numberPositions);
    }

    public static void updateSliceInfo(final PositionScore[] sortedPositionScores, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedPositionScores[0].position);
        sliceInfo.setLastStartPosition(sortedPositionScores[sortedPositionScores.length - 1].position);
        sliceInfo.setNumberRecords(sortedPositionScores.length);
    }

    @Override
    public int compareTo(final PositionScoreData other) {
        if (this.sortedPositionScores[0].position < other.sortedPositionScores[0].position) {
            return -1;
        }
        if (this.sortedPositionScores[0].position > other.sortedPositionScores[0].position) {
            return 1;
        }
        return 0;
    }

    public int[] getBasePositions() {
        if (this.basePositions == null) {
            this.basePositions = new int[this.sortedPositionScores.length];
            this.scores = new float[this.sortedPositionScores.length];
            for (int i = 0; i < this.basePositions.length; ++i) {
                this.basePositions[i] = this.sortedPositionScores[i].position;
                this.scores[i] = this.sortedPositionScores[i].score;
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

    public static PositionScoreData merge(final ArrayList<PositionScoreData> pdAL) {
        final PositionScoreData[] pdArray = new PositionScoreData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedPositionScores.length;
        }
        final PositionScore[] concatinate = new PositionScore[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final PositionScore[] slice = pdArray[j].sortedPositionScores;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new PositionScoreData(concatinate, sliceInfo);
    }

    public static PositionScoreData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<PositionScoreData> a = new ArrayList<PositionScoreData>(num);
        for (int i = 0; i < num; ++i) {
            a.add((PositionScoreData)useqDataAL.get(i));
        }
        return merge(a);
    }

    public int fetchLastBase() {
        return this.sortedPositionScores[this.sortedPositionScores.length - 1].position;
    }

    public void writeBed(final PrintWriter out, final boolean fixBedScores) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        for (int i = 0; i < this.sortedPositionScores.length; ++i) {
            if (fixBedScores) {
                final int score = USeqUtilities.fixBedScore(this.sortedPositionScores[i].score);
                out.println(chrom + "\t" + this.sortedPositionScores[i].position + "\t" + (this.sortedPositionScores[i].position + 1) + "\t" + ".\t" + score + "\t" + strand);
            }
            else {
                out.println(chrom + "\t" + this.sortedPositionScores[i].position + "\t" + (this.sortedPositionScores[i].position + 1) + "\t" + ".\t" + this.sortedPositionScores[i].score + "\t" + strand);
            }
        }
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tPosition\tScore");
            for (int i = 0; i < this.sortedPositionScores.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositionScores[i].position + "\t" + this.sortedPositionScores[i].score);
            }
        }
        else {
            out.println("#Chr\tPosition\tScore\tStrand");
            for (int i = 0; i < this.sortedPositionScores.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositionScores[i].position + "\t" + this.sortedPositionScores[i].score + "\t" + strand);
            }
        }
    }

    public void writePositionScore(final PrintWriter out) {
        int prior = -1;
        for (int i = 0; i < this.sortedPositionScores.length; ++i) {
            if (prior != this.sortedPositionScores[i].position) {
                out.println(this.sortedPositionScores[i].position + 1 + "\t" + this.sortedPositionScores[i].score);
                prior = this.sortedPositionScores[i].position;
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedPositionScores[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositionScores.length; ++i) {
                final int currentStart = this.sortedPositionScores[i].position;
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
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedPositionScores[0].position);
            workingDOS.writeFloat(this.sortedPositionScores[0].score);
            if (useShort) {
                int bp2 = this.sortedPositionScores[0].position;
                for (int j = 1; j < this.sortedPositionScores.length; ++j) {
                    final int currentStart2 = this.sortedPositionScores[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    workingDOS.writeShort((short)diff2);
                    workingDOS.writeFloat(this.sortedPositionScores[j].score);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositionScores[0].position;
                for (int j = 1; j < this.sortedPositionScores.length; ++j) {
                    final int currentStart2 = this.sortedPositionScores[j].position;
                    final int diff2 = currentStart2 - bp2;
                    workingDOS.writeInt(diff2);
                    workingDOS.writeFloat(this.sortedPositionScores[j].score);
                    bp2 = currentStart2;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
            int bp = this.sortedPositionScores[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositionScores.length; ++i) {
                final int currentStart = this.sortedPositionScores[i].position;
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
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedPositionScores[0].position);
            dos.writeFloat(this.sortedPositionScores[0].score);
            if (useShort) {
                int bp2 = this.sortedPositionScores[0].position;
                for (int j = 1; j < this.sortedPositionScores.length; ++j) {
                    final int currentStart2 = this.sortedPositionScores[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    dos.writeShort((short)diff2);
                    dos.writeFloat(this.sortedPositionScores[j].score);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositionScores[0].position;
                for (int j = 1; j < this.sortedPositionScores.length; ++j) {
                    final int currentStart2 = this.sortedPositionScores[j].position;
                    final int diff2 = currentStart2 - bp2;
                    dos.writeInt(diff2);
                    dos.writeFloat(this.sortedPositionScores[j].score);
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
            final int numberPositions = this.sliceInfo.getNumberRecords();
            (this.sortedPositionScores = new PositionScore[numberPositions])[0] = new PositionScore(dis.readInt(), dis.readFloat());
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.POSITION_SCORE_INT_FLOAT.matcher(fileType).matches()) {
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositionScores[i] = new PositionScore(this.sortedPositionScores[i - 1].position + dis.readInt(), dis.readFloat());
                }
            }
            else {
                if (!USeqUtilities.POSITION_SCORE_SHORT_FLOAT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a PositionScore[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositionScores[i] = new PositionScore(this.sortedPositionScores[i - 1].position + dis.readShort() + 32768, dis.readFloat());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public PositionScore[] getPositionScores() {
        return this.sortedPositionScores;
    }

    public void setPositionScores(final PositionScore[] sortedPositionScores) {
        updateSliceInfo(this.sortedPositionScores = sortedPositionScores, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<PositionScore> al = new ArrayList<PositionScore>();
        for (int i = 0; i < this.sortedPositionScores.length; ++i) {
            if (this.sortedPositionScores[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedPositionScores[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedPositionScores = new PositionScore[al.size()]);
        updateSliceInfo(this.sortedPositionScores, this.sliceInfo);
        return true;
    }
}
