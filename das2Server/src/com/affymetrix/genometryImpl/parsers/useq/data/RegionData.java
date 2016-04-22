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
import java.io.PrintWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class RegionData extends USeqData
{
    private Region[] sortedRegions;

    public RegionData() {
    }

    public RegionData(final Region[] sortedRegions, final SliceInfo sliceInfo) {
        this.sortedRegions = sortedRegions;
        this.sliceInfo = sliceInfo;
    }

    public RegionData(final File binaryFile) throws IOException {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public RegionData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public static void updateSliceInfo(final Region[] sortedRegions, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedRegions[0].getStart());
        sliceInfo.setLastStartPosition(sortedRegions[sortedRegions.length - 1].start);
        sliceInfo.setNumberRecords(sortedRegions.length);
    }

    public int fetchLastBase() {
        int lastBase = -1;
        for (final Region r : this.sortedRegions) {
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
        for (int i = 0; i < this.sortedRegions.length; ++i) {
            out.println(chrom + "\t" + this.sortedRegions[i].start + "\t" + this.sortedRegions[i].stop + "\t" + ".\t0\t" + strand);
        }
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tStart\tStop");
            for (int i = 0; i < this.sortedRegions.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegions[i].start + "\t" + this.sortedRegions[i].stop);
            }
        }
        else {
            out.println("#Chr\tStart\tStop\tStrand");
            for (int i = 0; i < this.sortedRegions.length; ++i) {
                out.println(chrom + "\t" + this.sortedRegions[i].start + "\t" + this.sortedRegions[i].stop + "\t" + strand);
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegions[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegions.length; ++i) {
                final int currentStart = this.sortedRegions[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegions.length; ++i) {
                final int diff2 = this.sortedRegions[i].stop - this.sortedRegions[i].start;
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
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedRegions[0].start);
            int bp2 = this.sortedRegions[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    workingDOS.writeInt(this.sortedRegions[0].stop - this.sortedRegions[0].start);
                    for (int j = 1; j < this.sortedRegions.length; ++j) {
                        final int currentStart2 = this.sortedRegions[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeInt(this.sortedRegions[j].stop - this.sortedRegions[j].start);
                        bp2 = currentStart2;
                    }
                }
                else {
                    workingDOS.writeShort((short)(this.sortedRegions[0].stop - this.sortedRegions[0].start - 32768));
                    for (int j = 1; j < this.sortedRegions.length; ++j) {
                        final int currentStart2 = this.sortedRegions[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        workingDOS.writeShort((short)diff3);
                        workingDOS.writeShort((short)(this.sortedRegions[j].stop - this.sortedRegions[j].start - 32768));
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                workingDOS.writeInt(this.sortedRegions[0].stop - this.sortedRegions[0].start);
                for (int j = 1; j < this.sortedRegions.length; ++j) {
                    final int currentStart2 = this.sortedRegions[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeInt(this.sortedRegions[j].stop - this.sortedRegions[j].start);
                    bp2 = currentStart2;
                }
            }
            else {
                workingDOS.writeShort((short)(this.sortedRegions[0].stop - this.sortedRegions[0].start - 32768));
                for (int j = 1; j < this.sortedRegions.length; ++j) {
                    final int currentStart2 = this.sortedRegions[j].start;
                    final int diff3 = currentStart2 - bp2;
                    workingDOS.writeInt(diff3);
                    workingDOS.writeShort((short)(this.sortedRegions[j].stop - this.sortedRegions[j].start - 32768));
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

    public static RegionData merge(final ArrayList<RegionData> pdAL) {
        final RegionData[] pdArray = new RegionData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedRegions.length;
        }
        final Region[] concatinate = new Region[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final Region[] slice = pdArray[j].sortedRegions;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new RegionData(concatinate, sliceInfo);
    }

    public static RegionData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<RegionData> a = new ArrayList<RegionData>(num);
        for (int i = 0; i < num; ++i) {
            a.add((RegionData)useqDataAL.get(i));
        }
        return merge(a);
    }

    public void write(final ZipOutputStream out, final DataOutputStream dos, final boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedRegions[0].start;
            useShortBeginning = true;
            for (int i = 1; i < this.sortedRegions.length; ++i) {
                final int currentStart = this.sortedRegions[i].start;
                final int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < this.sortedRegions.length; ++i) {
                final int diff2 = this.sortedRegions[i].stop - this.sortedRegions[i].start;
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
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedRegions[0].start);
            int bp2 = this.sortedRegions[0].start;
            if (useShortBeginning) {
                if (!useShortLength) {
                    dos.writeInt(this.sortedRegions[0].stop - this.sortedRegions[0].start);
                    for (int j = 1; j < this.sortedRegions.length; ++j) {
                        final int currentStart2 = this.sortedRegions[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeInt(this.sortedRegions[j].stop - this.sortedRegions[j].start);
                        bp2 = currentStart2;
                    }
                }
                else {
                    dos.writeShort((short)(this.sortedRegions[0].stop - this.sortedRegions[0].start - 32768));
                    for (int j = 1; j < this.sortedRegions.length; ++j) {
                        final int currentStart2 = this.sortedRegions[j].start;
                        final int diff3 = currentStart2 - bp2 - 32768;
                        dos.writeShort((short)diff3);
                        dos.writeShort((short)(this.sortedRegions[j].stop - this.sortedRegions[j].start - 32768));
                        bp2 = currentStart2;
                    }
                }
            }
            else if (!useShortLength) {
                dos.writeInt(this.sortedRegions[0].stop - this.sortedRegions[0].start);
                for (int j = 1; j < this.sortedRegions.length; ++j) {
                    final int currentStart2 = this.sortedRegions[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeInt(this.sortedRegions[j].stop - this.sortedRegions[j].start);
                    bp2 = currentStart2;
                }
            }
            else {
                dos.writeShort((short)(this.sortedRegions[0].stop - this.sortedRegions[0].start - 32768));
                for (int j = 1; j < this.sortedRegions.length; ++j) {
                    final int currentStart2 = this.sortedRegions[j].start;
                    final int diff3 = currentStart2 - bp2;
                    dos.writeInt(diff3);
                    dos.writeShort((short)(this.sortedRegions[j].stop - this.sortedRegions[j].start - 32768));
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
            final int numberRegions = this.sliceInfo.getNumberRecords();
            this.sortedRegions = new Region[numberRegions];
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.REGION_INT_INT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegions[0] = new Region(start, start + dis.readInt());
                for (int i = 1; i < numberRegions; ++i) {
                    start = this.sortedRegions[i - 1].start + dis.readInt();
                    this.sortedRegions[i] = new Region(start, start + dis.readInt());
                }
            }
            else if (USeqUtilities.REGION_INT_SHORT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegions[0] = new Region(start, start + dis.readShort() + 32768);
                for (int i = 1; i < numberRegions; ++i) {
                    start = this.sortedRegions[i - 1].start + dis.readInt();
                    this.sortedRegions[i] = new Region(start, start + dis.readShort() + 32768);
                }
            }
            else if (USeqUtilities.REGION_SHORT_SHORT.matcher(fileType).matches()) {
                int start = dis.readInt();
                this.sortedRegions[0] = new Region(start, start + dis.readShort() + 32768);
                for (int i = 1; i < numberRegions; ++i) {
                    start = this.sortedRegions[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegions[i] = new Region(start, start + dis.readShort() + 32768);
                }
            }
            else {
                if (!USeqUtilities.REGION_SHORT_INT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a Region[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                int start = dis.readInt();
                this.sortedRegions[0] = new Region(start, start + dis.readInt());
                for (int i = 1; i < numberRegions; ++i) {
                    start = this.sortedRegions[i - 1].start + dis.readShort() + 32768;
                    this.sortedRegions[i] = new Region(start, start + dis.readInt());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public Region[] getRegions() {
        return this.sortedRegions;
    }

    public void setRegions(final Region[] sortedRegions) {
        updateSliceInfo(this.sortedRegions = sortedRegions, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<Region> al = new ArrayList<Region>();
        for (int i = 0; i < this.sortedRegions.length; ++i) {
            if (this.sortedRegions[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedRegions[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedRegions = new Region[al.size()]);
        updateSliceInfo(this.sortedRegions, this.sliceInfo);
        return true;
    }
}
