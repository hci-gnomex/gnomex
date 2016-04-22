//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.useq.data;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.affymetrix.genometryImpl.parsers.useq.USeqUtilities;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.File;
import com.affymetrix.genometryImpl.parsers.useq.SliceInfo;

public class PositionData extends USeqData implements Comparable<PositionData>
{
    private Position[] sortedPositions;
    private int[] basePositions;

    public PositionData() {
    }

    public PositionData(final Position[] sortedPositions, final SliceInfo sliceInfo) {
        this.sortedPositions = sortedPositions;
        this.sliceInfo = sliceInfo;
    }

    public PositionData(final File binaryFile) throws IOException {
        this.sliceInfo = new SliceInfo(binaryFile.getName());
        this.read(binaryFile);
    }

    public PositionData(final DataInputStream dis, final SliceInfo sliceInfo) {
        this.sliceInfo = sliceInfo;
        this.read(dis);
    }

    public static void updateSliceInfo(final Position[] sortedPositions, final SliceInfo sliceInfo) {
        sliceInfo.setFirstStartPosition(sortedPositions[0].position);
        sliceInfo.setLastStartPosition(sortedPositions[sortedPositions.length - 1].position);
        sliceInfo.setNumberRecords(sortedPositions.length);
    }

    public int[] getBasePositions() {
        if (this.basePositions == null) {
            this.basePositions = new int[this.sortedPositions.length];
            for (int i = 0; i < this.basePositions.length; ++i) {
                this.basePositions[i] = this.sortedPositions[i].position;
            }
        }
        return this.basePositions;
    }

    public static PositionData merge(final ArrayList<PositionData> pdAL) {
        final PositionData[] pdArray = new PositionData[pdAL.size()];
        pdAL.toArray(pdArray);
        Arrays.sort(pdArray);
        int num = 0;
        for (int i = 0; i < pdArray.length; ++i) {
            num += pdArray[i].sortedPositions.length;
        }
        final Position[] concatinate = new Position[num];
        int index = 0;
        for (int j = 0; j < pdArray.length; ++j) {
            final Position[] slice = pdArray[j].sortedPositions;
            System.arraycopy(slice, 0, concatinate, index, slice.length);
            index += slice.length;
        }
        final SliceInfo sliceInfo = pdArray[0].sliceInfo;
        updateSliceInfo(concatinate, sliceInfo);
        return new PositionData(concatinate, sliceInfo);
    }

	public static PositionData mergeUSeqData(ArrayList<USeqData> useqDataAL) {
		int num = useqDataAL.size();
		//convert ArrayList
		ArrayList<PositionData> a = new ArrayList<PositionData>(num);
		for (int i=0; i< num; i++) {
			a.add((PositionData) useqDataAL.get(i));
		}
		return merge (a);
	}

/*
    public static PositionData mergeUSeqData(final ArrayList<USeqData> useqDataAL) {
        final int num = useqDataAL.size();
        final ArrayList<PositionData> a = new ArrayList<PositionData>(num);
        for (int i = 0; i < num; ++i) {
            a.add(useqDataAL.get(i));
        }
        return merge(a);
    }
*/
    @Override
    public int compareTo(final PositionData other) {
        if (this.sortedPositions[0].position < other.sortedPositions[0].position) {
            return -1;
        }
        if (this.sortedPositions[0].position > other.sortedPositions[0].position) {
            return 1;
        }
        return 0;
    }

    public void writeBed(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        for (int i = 0; i < this.sortedPositions.length; ++i) {
            out.println(chrom + "\t" + this.sortedPositions[i].position + "\t" + (this.sortedPositions[i].position + 1) + "\t" + ".\t0\t" + strand);
        }
    }

    public int fetchLastBase() {
        return this.sortedPositions[this.sortedPositions.length - 1].position;
    }

    public void writeNative(final PrintWriter out) {
        final String chrom = this.sliceInfo.getChromosome();
        final String strand = this.sliceInfo.getStrand();
        if (strand.equals(".")) {
            out.println("#Chr\tPosition");
            for (int i = 0; i < this.sortedPositions.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositions[i].position);
            }
        }
        else {
            out.println("#Chr\tPosition\tStrand");
            for (int i = 0; i < this.sortedPositions.length; ++i) {
                out.println(chrom + "\t" + this.sortedPositions[i].position + "\t" + strand);
            }
        }
    }

    public void writePositionScore(final PrintWriter out) {
        int priorPosition = -1;
        for (int i = 0; i < this.sortedPositions.length; ++i) {
            if (priorPosition != this.sortedPositions[i].position) {
                out.println(this.sortedPositions[i].position + 1 + "\t0");
                priorPosition = this.sortedPositions[i].position;
            }
        }
    }

    public File write(final File saveDirectory, final boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = this.sortedPositions[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositions.length; ++i) {
                final int currentStart = this.sortedPositions[i].position;
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
            fileType = "s";
        }
        else {
            fileType = "i";
        }
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = new File(saveDirectory, this.sliceInfo.getSliceName());
        FileOutputStream workingFOS = null;
        DataOutputStream workingDOS = null;
        try {
            workingFOS = new FileOutputStream(this.binaryFile);
            workingDOS = new DataOutputStream(new BufferedOutputStream(workingFOS));
            workingDOS.writeUTF(this.header);
            workingDOS.writeInt(this.sortedPositions[0].position);
            if (useShort) {
                int bp2 = this.sortedPositions[0].position;
                for (int j = 1; j < this.sortedPositions.length; ++j) {
                    final int currentStart2 = this.sortedPositions[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    workingDOS.writeShort((short)diff2);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositions[0].position;
                for (int j = 1; j < this.sortedPositions.length; ++j) {
                    final int currentStart2 = this.sortedPositions[j].position;
                    final int diff2 = currentStart2 - bp2;
                    workingDOS.writeInt(diff2);
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
            int bp = this.sortedPositions[0].position;
            useShort = true;
            for (int i = 1; i < this.sortedPositions.length; ++i) {
                final int currentStart = this.sortedPositions[i].position;
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
            fileType = "s";
        }
        else {
            fileType = "i";
        }
        this.sliceInfo.setBinaryType(fileType);
        this.binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(this.sliceInfo.getSliceName()));
            dos.writeUTF(this.header);
            dos.writeInt(this.sortedPositions[0].position);
            if (useShort) {
                int bp2 = this.sortedPositions[0].position;
                for (int j = 1; j < this.sortedPositions.length; ++j) {
                    final int currentStart2 = this.sortedPositions[j].position;
                    final int diff2 = currentStart2 - bp2 - 32768;
                    dos.writeShort((short)diff2);
                    bp2 = currentStart2;
                }
            }
            else {
                int bp2 = this.sortedPositions[0].position;
                for (int j = 1; j < this.sortedPositions.length; ++j) {
                    final int currentStart2 = this.sortedPositions[j].position;
                    final int diff2 = currentStart2 - bp2;
                    dos.writeInt(diff2);
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
            (this.sortedPositions = new Position[numberPositions])[0] = new Position(dis.readInt());
            final String fileType = this.sliceInfo.getBinaryType();
            if (USeqUtilities.POSITION_INT.matcher(fileType).matches()) {
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositions[i] = new Position(this.sortedPositions[i - 1].getPosition() + dis.readInt());
                }
            }
            else {
                if (!USeqUtilities.POSITION_SHORT.matcher(fileType).matches()) {
                    throw new IOException("Incorrect file type for creating a Position[] -> '" + fileType + "' in " + this.binaryFile + "\n");
                }
                for (int i = 1; i < numberPositions; ++i) {
                    this.sortedPositions[i] = new Position(this.sortedPositions[i - 1].getPosition() + dis.readShort() + 32768);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(dis);
        }
    }

    public Position[] getPositions() {
        return this.sortedPositions;
    }

    public void setPositions(final Position[] sortedPositions) {
        updateSliceInfo(this.sortedPositions = sortedPositions, this.sliceInfo);
    }

    public boolean trim(final int beginningBP, final int endingBP) {
        final ArrayList<Position> al = new ArrayList<Position>();
        for (int i = 0; i < this.sortedPositions.length; ++i) {
            if (this.sortedPositions[i].isContainedBy(beginningBP, endingBP)) {
                al.add(this.sortedPositions[i]);
            }
        }
        if (al.size() == 0) {
            return false;
        }
        al.toArray(this.sortedPositions = new Position[al.size()]);
        updateSliceInfo(this.sortedPositions, this.sliceInfo);
        return true;
    }
}
