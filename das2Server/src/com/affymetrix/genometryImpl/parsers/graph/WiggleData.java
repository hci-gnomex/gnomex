// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers.graph;

import cern.colt.GenericSorting;
import cern.colt.function.IntComparator;
import cern.colt.Swapper;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.GraphIntervalSym;
import java.util.Arrays;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import cern.colt.list.FloatArrayList;
import cern.colt.list.IntArrayList;

public final class WiggleData
{
    private final IntArrayList xData;
    private final FloatArrayList yData;
    private final IntArrayList wData;
    private final String seq_id;
    
    public WiggleData(final String seq_id) {
        this.xData = new IntArrayList();
        this.yData = new FloatArrayList();
        this.wData = new IntArrayList();
        this.seq_id = seq_id;
    }
    
    public GraphSym createGraph(final AnnotatedSeqGroup seq_group, final String graph_id, final String uri) {
        if (this.xData.isEmpty()) {
            return null;
        }
        final int dataSize = this.xData.size();
        final int[] xList = Arrays.copyOf(this.xData.elements(), dataSize);
        this.xData.clear();
        final float[] yList = Arrays.copyOf(this.yData.elements(), dataSize);
        this.yData.clear();
        final int[] wList = Arrays.copyOf(this.wData.elements(), dataSize);
        this.wData.clear();
        sortXYZDataOnX(xList, yList, wList);
        final int largest_x = xList[dataSize - 1] + wList[dataSize - 1];
        final BioSeq seq = seq_group.addSeq(this.seq_id, largest_x, uri);
        return new GraphIntervalSym(xList, wList, yList, graph_id, seq);
    }
    
    public BioSeq getBioSeq() {
        if (this.xData.isEmpty()) {
            return null;
        }
        final int dataSize = this.xData.size();
        final int[] xList = Arrays.copyOf(this.xData.elements(), dataSize);
        this.xData.clear();
        final float[] yList = Arrays.copyOf(this.yData.elements(), dataSize);
        this.yData.clear();
        final int[] wList = Arrays.copyOf(this.wData.elements(), dataSize);
        this.wData.clear();
        sortXYZDataOnX(xList, yList, wList);
        final int largest_x = xList[dataSize - 1] + wList[dataSize - 1];
        return new BioSeq(this.seq_id, null, largest_x);
    }
    
    static void sortXYZDataOnX(final int[] xList, final float[] yList, final int[] wList) {
        final Swapper swapper = (Swapper)new Swapper() {
            public void swap(final int a, final int b) {
                int swapInt = xList[a];
                xList[a] = xList[b];
                xList[b] = swapInt;
                swapInt = wList[a];
                wList[a] = wList[b];
                wList[b] = swapInt;
                final float swapFloat = yList[a];
                yList[a] = yList[b];
                yList[b] = swapFloat;
            }
        };
        final IntComparator comp = (IntComparator)new IntComparator() {
            public int compare(final int a, final int b) {
                return Integer.valueOf(xList[a]).compareTo(Integer.valueOf(xList[b]));
            }
        };
        GenericSorting.quickSort(0, xList.length, comp, swapper);
    }
    
    public void add(final int x, final float y, final int w) {
        this.xData.add(x);
        this.yData.add(y);
        this.wData.add(w);
    }
    
    public boolean isEmpty() {
        return this.xData.isEmpty();
    }
}
