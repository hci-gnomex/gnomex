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

public abstract class AbstractFloatTransformer implements Operator
{
    public abstract float transform(final float p0);

    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (symList.size() != 1 || !(symList.get(0) instanceof GraphSym)) {
            return null;
        }
        final GraphSym sourceSym = (GraphSym) symList.get(0);
        final int[] x = new int[sourceSym.getGraphXCoords().length];
        System.arraycopy(sourceSym.getGraphXCoords(), 0, x, 0, sourceSym.getGraphXCoords().length);
        final float[] sourceY = sourceSym.getGraphYCoords();
        final float[] y = new float[sourceY.length];
        for (int i = 0; i < sourceY.length; ++i) {
            y[i] = this.transform(sourceY[i]);
        }
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
    public boolean setParameters(final Map<String, Object> parms) {
        return parms.isEmpty();
    }

    @Override
    public boolean supportsTwoTrack() {
        return false;
    }

    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Graph;
    }

    public String getParamPrompt() {
        return null;
    }

    public static boolean isGraphTransform(final Operator operator) {
        for (final FileTypeCategory category : FileTypeCategory.values()) {
            if (category == FileTypeCategory.Graph) {
                if (operator.getOperandCountMin(category) != 1) {
                    return false;
                }
            }
            else if (operator.getOperandCountMax(category) > 0) {
                return false;
            }
        }
        return true;
    }
}
