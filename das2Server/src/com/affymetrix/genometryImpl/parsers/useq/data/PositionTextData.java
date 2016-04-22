//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.useq.data;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Arrays;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import com.affymetrix.genometryImpl.parsers.useq.apps.Text2USeq;
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class PositionTextData extends USeqData
{
    private PositionText[] sortedPositionTexts;

    public PositionTextData() {
    }

    public PositionTextData(final PositionText[] sortedPositionTexts, final SliceInfo sliceInfo) {
        this.sortedPositionTexts = sortedPositionTexts;
        this.sliceInfo = sliceInfo;
    }

    public PositionTextData(final File binaryFile) throws Exception {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public PositionTextData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public static void updateSliceInfo(final PositionText[] sortedPositionTexts, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedPositionTexts[0].position);
        sliceInfo.setLastStartPosition(sortedPositionTexts[sortedPositionTexts.length - 1].position);
        sliceInfo.setNumberRecords(sortedPositionTexts.length);
    }

    public int fetchLastBase() {
        return this.sortedPositionTexts[this.sortedPositionTexts.length - 1].position;
    }

    public void writeBed(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        for (int i = 0; i < this.sortedPositionTexts.length; ++i) {
            final String[] tokens = Text2USeq.PATTERN_TAB.split(this.sortedPositionTexts[i].text);
            if (tokens.length == 7) {
                out.println(chrom + "\t" + this.sortedPositionTexts[i].position + "\t" + (this.sortedPositionTexts[i].position + 1) + "\t" + tokens[0] + "\t0\t" + strand + "\t" + tokens[1] + "\t" + tokens[2] + "\t" + tokens[3] + "\t" + tokens[4] + "\t" + tokens[5] + "\t" + tokens[6]);
            }
            else {
                out.println(chrom + "\t" + this.sortedPositionTexts[i].position + "\t" + (this.sortedPositionTexts[i].position + 1) + "\t" + this.sortedPositionTexts[i].text + "\t0\t" + strand);
            }
        }
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tPosition\tText(s)");
            for (int i = 0; i < this.sortedPositionTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositionTexts[i].position + "\t" + this.sortedPositionTexts[i].text);
            }
        }
        else {
            out.println("#Chr\tPosition\tText(s)\tStrand");
            for (int i = 0; i < this.sortedPositionTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositionTexts[i].position + "\t" + this.sortedPositionTexts[i].text + "\t" + strand);
            }
        }
    }

    public void writePositionScore(final PrintWriter out) {
        int prior = -1;
        for (int i = 0; i < this.sortedPositionTexts.length; ++i) {
            if (prior != this.sortedPositionTexts[i].position) {
                out.println(this.sortedPositionTexts[i].position + 1 + "\t0");
                prior = this.sortedPositionTexts[i].position;
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedPositionTexts[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositionTexts.length; ++i) {
                final int currentStart = this.sortedPositionTexts[i].position;
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
            fileType = "st";
        }
        else {
            fileType = "it";
        }
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedPositionTexts[0].position);
            workingDOS.writeUTF(this.sortedPositionTexts[0].text);
            if (useShort) {
                int bp2 = this.sortedPositionTexts[0].position;
                for (int j = 1; j < this.sortedPositionTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionTexts[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    workingDOS.writeShort((short)diff2);
                    workingDOS.writeUTF(this.sortedPositionTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositionTexts[0].position;
                for (int j = 1; j < this.sortedPositionTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionTexts[j].position;
                    final int diff2 = currentStart2 - bp2;
                    workingDOS.writeInt(diff2);
                    workingDOS.writeUTF(this.sortedPositionTexts[j].text);
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

    public static PositionTextData merge(final ArrayList<PositionTextData> pdAL) {
        final PositionTextData[] pdArray = new PositionTextData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedPositionTexts.length;
        }
        final PositionText[] concatinate = new PositionText[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final PositionText[] slice = pdArray[j].sortedPositionTexts;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new PositionTextData(concatinate, sliceInfo);
    }

    public static PositionTextData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<PositionTextData> a = new ArrayList<PositionTextData>(num);
        for (int i = 0; i < num; ++i) {
            a.add((PositionTextData)useqDataAL.get(i));
        }
        return merge(a);
    }

    public void write(final ZipOutputStream out, final DataOutputStream dos, final boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedPositionTexts[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositionTexts.length; ++i) {
                final int currentStart = this.sortedPositionTexts[i].position;
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
            fileType = "st";
        }
        else {
            fileType = "it";
        }
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedPositionTexts[0].position);
            dos.writeUTF(this.sortedPositionTexts[0].text);
            if (useShort) {
                int bp2 = this.sortedPositionTexts[0].position;
                for (int j = 1; j < this.sortedPositionTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionTexts[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    dos.writeShort((short)diff2);
                    dos.writeUTF(this.sortedPositionTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositionTexts[0].position;
                for (int j = 1; j < this.sortedPositionTexts.length; ++j) {
                    final int currentStart2 = this.sortedPositionTexts[j].position;
                    final int diff2 = currentStart2 - bp2;
                    dos.writeInt(diff2);
                    dos.writeUTF(this.sortedPositionTexts[j].text);
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
            (this.sortedPositionTexts = new PositionText[numberPositions])[0] = new PositionText(dis.readInt(), dis.readUTF());
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.POSITION_TEXT_INT_TEXT.matcher(fileType).matches()) {
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositionTexts[i] = new PositionText(this.sortedPositionTexts[i - 1].position + dis.readInt(), dis.readUTF());
                }
            }
            else {
                if (!USeqUtilities.POSITION_TEXT_SHORT_TEXT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a PositionText[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositionTexts[i] = new PositionText(this.sortedPositionTexts[i - 1].position + dis.readShort() + 32768, dis.readUTF());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public PositionText[] getPositionTexts() {
        return this.sortedPositionTexts;
    }

    public void setPositionTexts(final PositionText[] sortedPositionTexts) {
        updateSliceInfo(this.sortedPositionTexts = sortedPositionTexts, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<PositionText> al = new ArrayList<PositionText>();
        for (int i = 0; i < this.sortedPositionTexts.length; ++i) {
            if (this.sortedPositionTexts[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedPositionTexts[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedPositionTexts = new PositionText[al.size()]);
        updateSliceInfo(this.sortedPositionTexts, this.sliceInfo);
        return true;
    }
}
