//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.parsers.gchp;

import java.util.Arrays;
import cern.colt.list.ByteArrayList;
import cern.colt.list.ShortArrayList;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.BioSeq;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

final class AffySingleChromData
{
    private final int start;
    private final int rowCount;
    String displayName;
    private List<AffyChpColumnData> columns;
    private final AffyGenericChpFile chpFile;
    AffyDataSet dataSet;

    public AffySingleChromData(final AffyGenericChpFile chpFile, final AffyDataSet dataSet, final int chromNum, final String chromDisplayName, final int start, final int count, final List<AffyChpColumnData> columns) {
        this.columns = new ArrayList<AffyChpColumnData>();
        this.chpFile = chpFile;
        this.dataSet = dataSet;
        this.displayName = chromDisplayName;
        this.start = start;
        this.rowCount = count;
        this.columns = columns;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [displayName=" + this.displayName + ", start=" + this.start + ", count=" + this.rowCount + ", columns=" + this.columns.size() + "]";
    }

    private void parse(final DataInputStream dis) throws IOException {
        Logger.getLogger(AffySingleChromData.class.getName()).log(Level.FINE, "Parsing chromData: {0}, {1}", new Object[] { this.displayName, this.rowCount });
        for (int row = 0; row < this.rowCount; ++row) {
            for (final AffyChpColumnData col : this.columns) {
                col.addData(dis);
            }
        }
    }

    private void skip(final DataInputStream dis) throws IOException {
        final int rowSize = totalRowSize(this.columns);
        long skipped;
        for (long skipSize = this.rowCount * rowSize; skipSize > 0L; skipSize -= skipped) {
            skipped = dis.skip(skipSize);
        }
    }

    private static int totalRowSize(final List<AffyChpColumnData> columns) {
        int rowSize = 0;
        for (final AffyChpColumnData col : columns) {
            rowSize += col.getByteLength();
        }
        return rowSize;
    }

    void parseOrSkip(final DataInputStream dis) throws IOException {
        if (this.chpFile.getLoadPolicy().shouldLoadChrom(this.displayName)) {
            this.parse(dis);
        }
        else {
            this.skip(dis);
        }
    }

    public List<SeqSymmetry> makeGraphs(final BioSeq seq) throws IOException {
        final List<SeqSymmetry> results = new ArrayList<SeqSymmetry>(this.columns.size());
        final ArrayList probeSetNames = (ArrayList)this.columns.get(0).getData();
        probeSetNames.trimToSize();
        final IntArrayList positions = (IntArrayList)this.columns.get(2).getData();
        positions.trimToSize();
        if (positions.size() <= -1) {
            return results;
        }
        for (final AffyChpColumnData colData : this.columns.subList(3, this.columns.size())) {
            final String graphId = colData.name;
            if (colData.getData() instanceof FloatArrayList) {
                final List<Object> trimmedXandY = this.trimNaN(positions, (FloatArrayList)colData.getData());
                final IntArrayList xlist = (IntArrayList) trimmedXandY.get(0);
                final FloatArrayList flist = (FloatArrayList) trimmedXandY.get(1);
                xlist.trimToSize();
                flist.trimToSize();
                final GraphSym gsym = new GraphSym(xlist.elements(), flist.elements(), graphId, seq);
                results.add(gsym);
            }
            else if (colData.getData() instanceof IntArrayList) {
                final IntArrayList ilist = (IntArrayList)colData.getData();
                ilist.trimToSize();
                final float[] y = new float[ilist.size()];
                for (int i = 0; i < ilist.size(); ++i) {
                    y[i] = ilist.get(i);
                }
                final GraphSym gsym2 = new GraphSym(positions.elements(), y, graphId, seq);
                results.add(gsym2);
            }
            else if (colData.getData() instanceof ShortArrayList) {
                GraphSym gsym3;
                if (colData.name.startsWith("CNState")) {
                    final List<Object> trimmedXandY2 = this.trim255(positions, (ShortArrayList)colData.getData());
                    final IntArrayList xlist2 = (IntArrayList) trimmedXandY2.get(0);
                    final ShortArrayList ilist2 = (ShortArrayList) trimmedXandY2.get(1);
                    xlist2.trimToSize();
                    ilist2.trimToSize();
                    final float[] y = new float[ilist2.size()];
                    for (int j = 0; j < ilist2.size(); ++j) {
                        y[j] = ilist2.get(j);
                    }
                    gsym3 = new GraphSym(xlist2.elements(), y, graphId, seq);
                }
                else {
                    final ShortArrayList ilist3 = (ShortArrayList)colData.getData();
                    ilist3.trimToSize();
                    final float[] y = new float[ilist3.size()];
                    for (int k = 0; k < ilist3.size(); ++k) {
                        y[k] = ilist3.get(k);
                    }
                    gsym3 = new GraphSym(positions.elements(), y, graphId, seq);
                }
                results.add(gsym3);
            }
            else if (colData.getData() instanceof ByteArrayList) {
                final ByteArrayList ilist4 = (ByteArrayList)colData.getData();
                ilist4.trimToSize();
                final float[] y = new float[ilist4.size()];
                for (int i = 0; i < ilist4.size(); ++i) {
                    y[i] = ilist4.get(i);
                }
                final GraphSym gsym2 = new GraphSym(positions.elements(), y, graphId, seq);
                results.add(gsym2);
            }
            else {
                Logger.getLogger(AffySingleChromData.class.getName()).log(Level.SEVERE, "Don''t know how to make a graph for data of type: {0}", colData.type);
            }
        }
        return results;
    }

    private List<Object> trimNaN(final IntArrayList x, final FloatArrayList y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Lists must be the same size " + x.size() + " != " + y.size());
        }
        boolean had_bad_values = false;
        final IntArrayList x_out = new IntArrayList(x.size());
        final FloatArrayList y_out = new FloatArrayList(y.size());
        for (int i = 0; i < x.size(); ++i) {
            final float f = y.get(i);
            if (Float.isNaN(f) || Float.isInfinite(f)) {
                had_bad_values = true;
            }
            else {
                x_out.add(x.get(i));
                y_out.add(f);
            }
        }
        if (had_bad_values) {
            return Arrays.<Object>asList(x_out, y_out);
        }
        return Arrays.<Object>asList(x, y);
    }

    private List<Object> trim255(final IntArrayList x, final ShortArrayList y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Lists must be the same size " + x.size() + " != " + y.size());
        }
        boolean had_bad_values = false;
        final IntArrayList x_out = new IntArrayList(x.size());
        final ShortArrayList y_out = new ShortArrayList(y.size());
        for (int i = 0; i < x.size(); ++i) {
            final short f = y.get(i);
            if (f == 255) {
                had_bad_values = true;
            }
            else {
                x_out.add(x.get(i));
                y_out.add(f);
            }
        }
        if (had_bad_values) {
            return Arrays.<Object>asList(x_out, y_out);
        }
        return Arrays.<Object>asList(x, y);
    }
}
