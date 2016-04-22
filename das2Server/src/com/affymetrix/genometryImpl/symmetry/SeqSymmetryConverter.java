// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.symmetry;

import com.affymetrix.genometryImpl.SeqSpan;
import com.affymetrix.genometryImpl.span.SimpleSeqSpan;
import com.affymetrix.genometryImpl.BioSeq;

public final class SeqSymmetryConverter
{
    public static UcscPslSym convertToPslSym(final SeqSymmetry sym, final String type, final BioSeq targetseq) {
        final int child_count = sym.getChildCount();
        final SeqSpan tspan = sym.getSpan(targetseq);
        final boolean forward = tspan.isForward();
        String qname = null;
        if (sym instanceof SymWithProps) {
            final SymWithProps psym = (SymWithProps)sym;
            qname = (String)psym.getProperty("group");
        }
        if (qname == null) {
            qname = sym.getID();
        }
        if (qname == null) {
            qname = "unknown";
        }
        int curlength = 0;
        final int[] blockSizes = new int[child_count];
        final int[] qmins = new int[child_count];
        final int[] tmins = new int[child_count];
        for (int i = 0; i < child_count; ++i) {
            final SeqSymmetry child = sym.getChild(i);
            final SeqSpan child_tspan = child.getSpan(targetseq);
            blockSizes[i] = child_tspan.getLength();
            tmins[i] = child_tspan.getMin();
            qmins[i] = curlength;
            curlength += child_tspan.getLength();
        }
        final BioSeq queryseq = new BioSeq(qname, qname, curlength);
        final SeqSpan qspan = new SimpleSeqSpan(0, curlength, queryseq);
        final UcscPslSym pslsym = new UcscPslSym(type, -1, -1, -1, -1, -1, -1, -1, -1, forward, queryseq, qspan.getMin(), qspan.getMax(), targetseq, tspan.getMin(), tspan.getMax(), child_count, blockSizes, qmins, tmins);
        return pslsym;
    }
    
    public static UcscPslSym convertToPslSym(final SeqSymmetry sym, final String type, final BioSeq queryseq, final BioSeq targetseq) {
        final int child_count = sym.getChildCount();
        final SeqSpan qspan = sym.getSpan(queryseq);
        final SeqSpan tspan = sym.getSpan(targetseq);
        final boolean forward = !(tspan.isForward() ^ qspan.isForward());
        final int[] blockSizes = new int[child_count];
        final int[] tmins = new int[child_count];
        final int[] qmins = new int[child_count];
        for (int i = 0; i < child_count; ++i) {
            final SeqSymmetry child = sym.getChild(i);
            final SeqSpan child_qspan = child.getSpan(queryseq);
            final SeqSpan child_tspan = child.getSpan(targetseq);
            blockSizes[i] = child_tspan.getLength();
            tmins[i] = child_tspan.getMin();
            if (child_qspan != null) {
                qmins[i] = child_qspan.getMin();
            }
        }
        final UcscPslSym pslsym = new UcscPslSym(type, -1, -1, -1, -1, -1, -1, -1, -1, forward, queryseq, qspan.getMin(), qspan.getMax(), targetseq, tspan.getMin(), tspan.getMax(), child_count, blockSizes, qmins, tmins);
        return pslsym;
    }
}
