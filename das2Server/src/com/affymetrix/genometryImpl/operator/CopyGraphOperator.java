//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.symmetry.GraphSym;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public final class CopyGraphOperator implements Operator, ICopy
{
    @Override
    public String getName() {
        return "copygraph";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (symList.size() != 1 || !(symList.get(0) instanceof GraphSym)) {
            return null;
        }
        final GraphSym sourceSym = (GraphSym)symList.get(0);
        final int[] x = new int[sourceSym.getGraphXCoords().length];
        System.arraycopy(sourceSym.getGraphXCoords(), 0, x, 0, sourceSym.getGraphXCoords().length);
        final float[] y = new float[sourceSym.getGraphYCoords().length];
        System.arraycopy(sourceSym.getGraphYCoords(), 0, y, 0, sourceSym.getGraphYCoords().length);
        final String id = sourceSym.getID();
        final BioSeq seq = sourceSym.getGraphSeq();
        GraphSym graphSym;
        if (sourceSym.hasWidth()) {
            final int[] w = new int[sourceSym.getGraphWidthCoords().length];
            System.arraycopy(sourceSym.getGraphWidthCoords(), 0, w, 0, sourceSym.getGraphWidthCoords().length);
            graphSym = new GraphSym(x, w, y, id, seq);
        }
        else {
            graphSym = new GraphSym(x, y, id, seq);
        }
        return graphSym;
    }

    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Graph) ? 1 : 0;
    }

    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Graph) ? 1 : 0;
    }

    @Override
    public Map<String, Class<?>> getParameters() {
        return null;
    }

    @Override
    public boolean setParameters(final Map<String, Object> obj) {
        return false;
    }

    @Override
    public boolean supportsTwoTrack() {
        return false;
    }

    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Graph;
    }
}
