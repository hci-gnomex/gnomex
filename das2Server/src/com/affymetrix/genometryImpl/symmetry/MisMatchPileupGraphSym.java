// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import com.affymetrix.genometryImpl.BioSeq;

public class MisMatchPileupGraphSym extends MisMatchGraphSym
{
    private float min_totalycoord;
    private float max_totalycoord;
    private char[] bases;
    
    public MisMatchPileupGraphSym(final int[] x, final int[] w, final float[] y, final int[] a, final int[] t, final int[] g, final int[] c, final int[] n, final String id, final BioSeq seq, final char[] bases) {
        super(x, w, y, a, t, g, c, n, id, seq);
        this.min_totalycoord = Float.POSITIVE_INFINITY;
        this.max_totalycoord = Float.NEGATIVE_INFINITY;
        this.bases = bases;
    }
    
    public MisMatchPileupGraphSym(final int[] x, final int[] w, final float[] y, final int[] a, final int[] t, final int[] g, final int[] c, final int[] n, final String id, final BioSeq seq) {
        this(x, w, y, a, t, g, c, n, id, seq, null);
    }
    
    @Override
    void setAllResidues(final int[] a, final int[] t, final int[] g, final int[] c, final int[] n) {
        super.setAllResidues(a, t, g, c, n);
        this.setVisibleTotalYRange(this.residuesTot);
        this.bases = null;
    }
    
    @Override
    public float[] getVisibleYRange() {
        return this.getVisibleTotalYRange();
    }
    
    @Override
    protected File index(final String graphName, final int[] a, final int[] t, final int[] g, final int[] c, final int[] n) {
        final File file = super.index(graphName, a, t, g, c, n);
        this.setVisibleTotalYRange(this.residuesTot);
        this.bases = null;
        return file;
    }
    
    @Override
    protected synchronized void readIntoBuffers(final int start) {
        super.readIntoBuffers(start);
        this.setVisibleTotalYRange(this.residuesTot);
        this.bases = null;
    }
    
    @Override
    public float getGraphYCoord(final int i) {
        float totalY = 0.0f;
        for (final float y : this.getAllResiduesY(i)) {
            totalY += y;
        }
        return totalY;
    }
    
    private final float[] getVisibleTotalYRange() {
        final float[] result = new float[2];
        if (this.min_totalycoord == Float.POSITIVE_INFINITY || this.max_totalycoord == Float.NEGATIVE_INFINITY) {
            this.setVisibleTotalYRange(this.residuesTot);
        }
        result[0] = this.min_totalycoord;
        result[1] = this.max_totalycoord;
        return result;
    }
    
    private synchronized void setVisibleTotalYRange(final int[][] resT) {
        this.min_totalycoord = Float.POSITIVE_INFINITY;
        this.max_totalycoord = Float.NEGATIVE_INFINITY;
        for (int i = 0; i < resT[0].length; ++i) {
            float f = 0.0f;
            for (int j = 0; j < 5; ++j) {
                f += resT[j][i];
            }
            if (f < this.min_totalycoord) {
                this.min_totalycoord = f;
            }
            if (f > this.max_totalycoord) {
                this.max_totalycoord = f;
            }
        }
    }
    
    @Override
    public synchronized float[] normalizeGraphYCoords() {
        final List<Float> coords = new ArrayList<Float>();
        float lastY = Float.NaN;
        int lastX = Integer.MAX_VALUE;
        for (int i = 0; i < this.getPointCount(); ++i) {
            final int x = this.getGraphXCoord(i);
            if (this.hasWidth() && x != lastX && lastX != Integer.MAX_VALUE) {
                coords.add(0.0f);
                lastY = 0.0f;
            }
            final float y = this.getGraphYCoord(i);
            if (y != lastY) {
                coords.add(y);
                lastY = y;
            }
            if (this.hasWidth()) {
                lastX = x + this.getGraphWidthCoord(i);
            }
        }
        coords.add(0.0f);
        final float[] tempCoords = new float[coords.size()];
        for (int j = 0; j < coords.size(); ++j) {
            tempCoords[j] = coords.get(j);
        }
        return tempCoords;
    }
    
    public boolean hasReferenceSequence() {
        return this.bases != null;
    }
    
    public char getReferenceBase(final int i) {
        return this.bases[i];
    }
    
    @Override
    public boolean isSpecialGraph() {
        return true;
    }
}
