//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.useq.data;

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
import java.io.IOException;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class RegionTextData extends USeqData
{
    private RegionText[] sortedRegionTexts;

    public RegionTextData() {
    }

    public RegionTextData(final RegionText[] sortedRegionTexts, final SliceInfo sliceInfo) {
        this.sortedRegionTexts = sortedRegionTexts;
        this.sliceInfo = sliceInfo;
    }

    public RegionTextData(final File binaryFile) throws IOException {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public RegionTextData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public static void updateSliceInfo(final RegionText[] sortedRegionTexts, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedRegionTexts[0].getStart());
        sliceInfo.setLastStartPosition(sortedRegionTexts[sortedRegionTexts.length - 1].start);
        sliceInfo.setNumberRecords(sortedRegionTexts.length);
    }

    public int fetchLastBase() {
        int lastBase = -1;
        for (final RegionText r : this.sortedRegionTexts) {
            final int end = r.getStop();
            if (end > lastBase) {
                lastBase = end;
            }
        }
        return lastBase;
    }

    public void writeBed(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        for (int i = 0; i < this.sortedRegionTexts.length; ++i) {
            final String[] tokens = Text2USeq.PATTERN_TAB.split(this.sortedRegionTexts[i].text);
            if (tokens.length == 7) {
                out.println(chrom + "\t" + this.sortedRegionTexts[i].start + "\t" + this.sortedRegionTexts[i].stop + "\t" + tokens[0] + "\t0\t" + strand + "\t" + tokens[1] + "\t" + tokens[2] + "\t" + tokens[3] + "\t" + tokens[4] + "\t" + tokens[5] + "\t" + tokens[6]);
            }
            else {
                out.println(chrom + "\t" + this.sortedRegionTexts[i].start + "\t" + this.sortedRegionTexts[i].stop + "\t" + this.sortedRegionTexts[i].text + "\t0\t" + strand);
            }
        }
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tStart\tStop\tText(s)");
            for (int i = 0; i < this.sortedRegionTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegionTexts[i].start + "\t" + this.sortedRegionTexts[i].stop + "\t" + this.sortedRegionTexts[i].text);
            }
        }
        else {
            out.println("#Chr\tStart\tStop\tText(s)\tStrand");
            for (int i = 0; i < this.sortedRegionTexts.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegionTexts[i].start + "\t" + this.sortedRegionTexts[i].stop + "\t" + this.sortedRegionTexts[i].text + "\t" + strand);
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegionTexts[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegionTexts.length; ++i) {
                final int currentStart = this.sortedRegionTexts[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegionTexts.length; ++i) {
                final int diff2 = this.sortedRegionTexts[i].stop - this.sortedRegionTexts[i].start;
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
        fileType += "t";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedRegionTexts[0].start);
            int bp2 = this.sortedRegionTexts[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    workingDOS.writeInt(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start);
                    workingDOS.writeUTF(this.sortedRegionTexts[0].text);
                    for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeInt(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start);
                        workingDOS.writeUTF(this.sortedRegionTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
                else {
                    workingDOS.writeShort((short)(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start - 32768));
                    workingDOS.writeUTF(this.sortedRegionTexts[0].text);
                    for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeShort((short)(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start - 32768));
                        workingDOS.writeUTF(this.sortedRegionTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                workingDOS.writeInt(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start);
                workingDOS.writeUTF(this.sortedRegionTexts[0].text);
                for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeInt(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start);
                    workingDOS.writeUTF(this.sortedRegionTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                workingDOS.writeShort((short)(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start - 32768));
                workingDOS.writeUTF(this.sortedRegionTexts[0].text);
                for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeShort((short)(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start - 32768));
                    workingDOS.writeUTF(this.sortedRegionTexts[j].text);
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

    public static RegionTextData merge(final ArrayList<RegionTextData> pdAL) {
        final RegionTextData[] pdArray = new RegionTextData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedRegionTexts.length;
        }
        final RegionText[] concatinate = new RegionText[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final RegionText[] slice = pdArray[j].sortedRegionTexts;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new RegionTextData(concatinate, sliceInfo);
    }

    public static RegionTextData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<RegionTextData> a = new ArrayList<RegionTextData>(num);
        for (int i = 0; i < num; ++i) {
            a.add((RegionTextData)useqDataAL.get(i));
        }
        return merge(a);
    }

    public void write(final ZipOutputStream out, final DataOutputStream dos, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegionTexts[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegionTexts.length; ++i) {
                final int currentStart = this.sortedRegionTexts[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegionTexts.length; ++i) {
                final int diff2 = this.sortedRegionTexts[i].stop - this.sortedRegionTexts[i].start;
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
        fileType += "t";
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedRegionTexts[0].start);
            int bp2 = this.sortedRegionTexts[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    dos.writeInt(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start);
                    dos.writeUTF(this.sortedRegionTexts[0].text);
                    for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeInt(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start);
                        dos.writeUTF(this.sortedRegionTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
                else {
                    dos.writeShort((short)(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start - 32768));
                    dos.writeUTF(this.sortedRegionTexts[0].text);
                    for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                        final int currentStart2 = this.sortedRegionTexts[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeShort((short)(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start - 32768));
                        dos.writeUTF(this.sortedRegionTexts[j].text);
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                dos.writeInt(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start);
                dos.writeUTF(this.sortedRegionTexts[0].text);
                for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeInt(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start);
                    dos.writeUTF(this.sortedRegionTexts[j].text);
                    bp2 = currentStart2;
                }
            }
            else {
                dos.writeShort((short)(this.sortedRegionTexts[0].stop - this.sortedRegionTexts[0].start - 32768));
                dos.writeUTF(this.sortedRegionTexts[0].text);
                for (int j = 1; j < this.sortedRegionTexts.length; ++j) {
                    final int currentStart2 = this.sortedRegionTexts[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeShort((short)(this.sortedRegionTexts[j].stop - this.sortedRegionTexts[j].start - 32768));
                    dos.writeUTF(this.sortedRegionTexts[j].text);
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
            final int numberRegionTexts = this.sliceInfo.getNumberRecords();
            this.sortedRegionTexts = new RegionText[numberRegionTexts];
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.REGION_TEXT_INT_INT_TEXT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionTexts[0] = new RegionText(start, start + dis.readInt(), dis.readUTF());
                for (int i = 1; i < numberRegionTexts; ++i) {
                    start = this.sortedRegionTexts[i - 1].start + dis.readInt();
                    this.sortedRegionTexts[i] = new RegionText(start, start + dis.readInt(), dis.readUTF());
                }
            }
            else if (USeqUtilities.REGION_TEXT_INT_SHORT_TEXT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionTexts[0] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());
                for (int i = 1; i < numberRegionTexts; ++i) {
                    start = this.sortedRegionTexts[i - 1].start + dis.readInt();
                    this.sortedRegionTexts[i] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());
                }
            }
            else if (USeqUtilities.REGION_TEXT_SHORT_SHORT_TEXT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegionTexts[0] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());
                for (int i = 1; i < numberRegionTexts; ++i) {
                    start = this.sortedRegionTexts[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegionTexts[i] = new RegionText(start, start + dis.readShort() + 32768, dis.readUTF());
                }
            }
            else {
                if (!USeqUtilities.REGION_TEXT_SHORT_INT_TEXT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a RegionText[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                int start = dis.readInt();
                this.sortedRegionTexts[0] = new RegionText(start, start + dis.readInt(), dis.readUTF());
                for (int i = 1; i < numberRegionTexts; ++i) {
                    start = this.sortedRegionTexts[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegionTexts[i] = new RegionText(start, start + dis.readInt(), dis.readUTF());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public RegionText[] getRegionTexts() {
        return this.sortedRegionTexts;
    }

    public void setRegionTexts(final RegionText[] sortedRegionTexts) {
        updateSliceInfo(this.sortedRegionTexts = sortedRegionTexts, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<RegionText> al = new ArrayList<RegionText>();
        for (int i = 0; i < this.sortedRegionTexts.length; ++i) {
            if (this.sortedRegionTexts[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedRegionTexts[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedRegionTexts = new RegionText[al.size()]);
        updateSliceInfo(this.sortedRegionTexts, this.sliceInfo);
        return true;
    }
}
