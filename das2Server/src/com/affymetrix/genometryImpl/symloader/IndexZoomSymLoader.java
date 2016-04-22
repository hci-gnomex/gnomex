// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symloader;

import java.util.Set;
import java.util.AbstractMap;
import com.affymetrix.genometryImpl.util.SynonymLookup;
import com.affymetrix.genometryImpl.util.BlockCompressedStreamPosition;
import net.sf.samtools.util.BlockCompressedFilePointerUtil;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import com.affymetrix.genometryImpl.operator.Operator;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.GraphIntervalSym;
import com.affymetrix.genometryImpl.operator.DepthOperator;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.net.URI;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.BioSeq;

public abstract class IndexZoomSymLoader extends SymLoader
{
    private static final int BIN_COUNT = 32768;
    private static final int BIN_LENGTH = 16384;
    private BioSeq saveSeq;
    private GraphSym saveSym;
    
    public IndexZoomSymLoader(final URI uri, final String featureName, final AnnotatedSeqGroup group) {
        super(uri, featureName, group);
    }
    
    protected abstract SymLoader getDataFileSymLoader() throws Exception;
    
    protected abstract Iterator<Map<Integer, List<List<Long>>>> getBinIter(final String p0);
    
    protected float getRealAvg(final SimpleSeqSpan span) throws Exception {
        final SymLoader symL = this.getDataFileSymLoader();
        final List<SeqSymmetry> symList = (List<SeqSymmetry>)symL.getRegion(span);
        if (symList.size() == 0) {
            return 0.0f;
        }
        final Operator depthOperator = new DepthOperator(FileTypeCategory.Alignment);
        final GraphIntervalSym sym = (GraphIntervalSym)depthOperator.operate(span.getBioSeq(), symList);
        float total = 0.0f;
        for (int i = 0; i < sym.getPointCount(); ++i) {
            total += sym.getGraphYCoord(i) * sym.getGraphWidthCoord(i);
        }
        return total / span.getLength();
    }
    
    @Override
    public List<? extends SeqSymmetry> getRegion(final SeqSpan overlapSpan) throws Exception {
        this.init();
        final BioSeq seq = overlapSpan.getBioSeq();
        if (!seq.equals(this.saveSeq) || this.saveSym == null) {
            final Iterator<Map<Integer, List<List<Long>>>> binIter = this.getBinIter(seq.toString());
            if (binIter == null) {
                this.saveSym = new GraphSym(new int[0], new int[0], new float[0], this.featureName, seq);
            }
            else {
                final int[] xList = new int[32768];
                for (int i = 0; i < 32768; ++i) {
                    xList[i] = i * 16384;
                }
                final int[] wList = new int[32768];
                Arrays.fill(wList, 16384);
                final float[] yList = new float[32768];
                Arrays.fill(yList, 0.0f);
                float largestY = Float.MIN_VALUE;
                int indexLargest = -1;
                while (binIter.hasNext()) {
                    final Map<Integer, List<List<Long>>> binWrapper = binIter.next();
                    final int binNo = binWrapper.keySet().iterator().next();
                    final int[] region = getRegion(binNo);
                    int yValue = 0;
                    for (final List<Long> chunkWrapper : binWrapper.get(binNo)) {
                        if (chunkWrapper != null) {
                            yValue += (int)(getUncompressedLength(chunkWrapper.get(0), chunkWrapper.get(1)) * 16384L / (region[1] - region[0]));
                        }
                    }
                    if (1 + region[1] - region[0] == 16384 && yValue > 0.0f && (yValue > largestY || indexLargest == -1)) {
                        indexLargest = region[0] / 16384;
                        largestY = yValue;
                    }
                    for (int j = region[0] / 16384; j < (region[1] + 1) / 16384; ++j) {
                        final float[] array = yList;
                        final int n = j;
                        array[n] += yValue;
                    }
                }
                indexLargest = -1;
                if (indexLargest != -1) {
                    try {
                        final float realAvg = this.getRealAvg(new SimpleSeqSpan(indexLargest * 16384, (indexLargest + 1) * 16384, seq));
                        if (realAvg > 0.0f) {
                            final float ratio = realAvg / yList[indexLargest];
                            for (int k = 0; k < yList.length; ++k) {
                                final float[] array2 = yList;
                                final int n2 = k;
                                array2[n2] *= ratio;
                            }
                        }
                    }
                    catch (Exception x) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "fail loading BAM segment " + this.uri, x);
                    }
                }
                this.saveSym = new GraphSym(xList, wList, yList, this.featureName, seq);
            }
            this.saveSeq = seq;
        }
        final List<SeqSymmetry> symList = new ArrayList<SeqSymmetry>();
        symList.add(this.saveSym);
        return symList;
    }
    
    private static long getUncompressedLength(final long chunkStart, final long chunkEnd) {
        final BlockCompressedStreamPosition start = new BlockCompressedStreamPosition(BlockCompressedFilePointerUtil.getBlockAddress(chunkStart), BlockCompressedFilePointerUtil.getBlockOffset(chunkStart));
        final BlockCompressedStreamPosition end = new BlockCompressedStreamPosition(BlockCompressedFilePointerUtil.getBlockAddress(chunkEnd), BlockCompressedFilePointerUtil.getBlockOffset(chunkEnd));
        return Math.max(0L, end.getApproximatePosition() - start.getApproximatePosition());
    }
    
    private static int[] getRegion(final int binno) {
        int counter = 0;
        int idx = -3;
        int[] span = null;
        while (span == null) {
            idx += 3;
            final int base = (int)Math.pow(2.0, idx);
            if (counter + base > binno) {
                final int mod = binno - counter;
                final int lvl = (int)Math.pow(2.0, 29 - idx);
                span = new int[] { lvl * mod, lvl * (mod + 1) - 1 };
            }
            else {
                counter += base;
            }
        }
        return span;
    }
    
    protected Map<String, String> getSynonymMap() {
        final SynonymLookup chromosomeLookup = SynonymLookup.getChromosomeLookup();
        return new AbstractMap<String, String>() {
            @Override
            public Set<Map.Entry<String, String>> entrySet() {
                return null;
            }
            
            @Override
            public String get(final Object key) {
                return chromosomeLookup.getPreferredName((String)key);
            }
        };
    }
    
    public static void main(final String[] args) {
        for (int i = 0; i < 37449; ++i) {
            final int[] r = getRegion(i);
            System.out.println("" + i + ":" + r[0] + "-" + r[1]);
        }
    }
}
