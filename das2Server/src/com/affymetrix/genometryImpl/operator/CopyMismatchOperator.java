//
// Decompiled by Procyon v0.5.30
//

package com.affymetrix.genometryImpl.operator;

import java.util.Map;
import com.affymetrix.genometryImpl.parsers.FileTypeCategory;
import com.affymetrix.genometryImpl.symmetry.MisMatchPileupGraphSym;
import com.affymetrix.genometryImpl.symmetry.MisMatchGraphSym;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.GenometryConstants;

public class CopyMismatchOperator implements Operator, ICopy
{
    @Override
    public String getName() {
        return "copymismatch";
    }

    @Override
    public String getDisplay() {
        return GenometryConstants.BUNDLE.getString("operator_" + this.getName());
    }

    @Override
    public SeqSymmetry operate(final BioSeq aseq, final List<SeqSymmetry> symList) {
        if (symList.size() != 1 || !(symList.get(0) instanceof MisMatchGraphSym)) {
            return null;
        }
        final MisMatchGraphSym sourceSym = (MisMatchGraphSym)symList.get(0);
        MisMatchGraphSym graphSym = null;
        final int[] x = new int[sourceSym.getGraphXCoords().length];
        System.arraycopy(sourceSym.getGraphXCoords(), 0, x, 0, sourceSym.getGraphXCoords().length);
        final int[] w = new int[sourceSym.getGraphWidthCoords().length];
        System.arraycopy(sourceSym.getGraphWidthCoords(), 0, w, 0, sourceSym.getGraphWidthCoords().length);
        final float[] y = new float[sourceSym.getGraphYCoords().length];
        System.arraycopy(sourceSym.getGraphYCoords(), 0, y, 0, sourceSym.getGraphYCoords().length);
        final int[][] residues = sourceSym.getAllResidues();
        final int[] a = new int[residues[0].length];
        System.arraycopy(residues[0], 0, a, 0, residues[0].length);
        final int[] t = new int[residues[1].length];
        System.arraycopy(residues[1], 0, t, 0, residues[0].length);
        final int[] g = new int[residues[2].length];
        System.arraycopy(residues[2], 0, g, 0, residues[0].length);
        final int[] c = new int[residues[3].length];
        System.arraycopy(residues[3], 0, c, 0, residues[0].length);
        final int[] n = new int[residues[4].length];
        System.arraycopy(residues[4], 0, n, 0, residues[0].length);
        final String id = sourceSym.getID();
        final BioSeq seq = sourceSym.getGraphSeq();
        if (sourceSym instanceof MisMatchPileupGraphSym) {
            graphSym = new MisMatchPileupGraphSym(x, w, y, a, t, g, c, n, id, seq);
        }
        else {
            graphSym = new MisMatchGraphSym(x, w, y, a, t, g, c, n, id, seq);
        }
        return graphSym;
    }

    @Override
    public int getOperandCountMin(final FileTypeCategory category) {
        return (category == FileTypeCategory.Mismatch) ? 1 : 0;
    }

    @Override
    public int getOperandCountMax(final FileTypeCategory category) {
        return (category == FileTypeCategory.Mismatch) ? 1 : 0;
    }

    @Override
    public Map<String, Class<?>> getParameters() {
        return null;
    }

    @Override
    public boolean setParameters(final Map<String, Object> parms) {
        return true;
    }

    @Override
    public boolean supportsTwoTrack() {
        return false;
    }

    @Override
    public FileTypeCategory getOutputCategory() {
        return FileTypeCategory.Mismatch;
    }
}
