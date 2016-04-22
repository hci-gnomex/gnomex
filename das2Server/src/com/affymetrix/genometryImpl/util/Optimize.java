// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import com.affymetrix.genometryImpl.symmetry.IntervalSearchSym;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import com.affymetrix.genometryImpl.symmetry.TypeContainerAnnot;
import java.util.Iterator;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;

public abstract class Optimize
{
    private static final boolean DEBUG = false;
    
    public static final void genome(final AnnotatedSeqGroup genome) {
        for (final BioSeq aseq : genome.getSeqList()) {
            Seq(aseq);
        }
    }
    
    private static final void Seq(final BioSeq aseq) {
        final int annot_count = aseq.getAnnotationCount();
        for (int i = annot_count - 1; i >= 0; --i) {
            final SeqSymmetry annot = aseq.getAnnotation(i);
            if (annot instanceof TypeContainerAnnot) {
                final TypeContainerAnnot container = (TypeContainerAnnot)annot;
                typeContainer(container, aseq);
            }
            else {
                System.out.println("problem in optimizeSeq(), found top-level sym that is not a TypeContainerAnnot: " + annot);
            }
        }
    }
    
    private static final void typeContainer(final TypeContainerAnnot container, final BioSeq aseq) {
        final String annot_type = container.getType();
        final int child_count = container.getChildCount();
        final ArrayList<SeqSymmetry> temp_annots = new ArrayList<SeqSymmetry>(child_count);
        for (int i = child_count - 1; i >= 0; --i) {
            final SeqSymmetry child = container.getChild(i);
            if (child instanceof IntervalSearchSym) {
                final IntervalSearchSym search_sym = (IntervalSearchSym)child;
                if (!search_sym.getOptimizedForSearch()) {
                    search_sym.initForSearching(aseq);
                }
            }
            else {
                temp_annots.add(child);
                container.removeChild(child);
            }
        }
        final int temp_count = temp_annots.size();
        for (int j = temp_count - 1; j >= 0; --j) {
            final SeqSymmetry annot_sym = temp_annots.get(j);
            final IntervalSearchSym search_sym2 = new IntervalSearchSym(aseq, annot_sym);
            search_sym2.setProperty("method", annot_type);
            search_sym2.initForSearching(aseq);
            container.addChild(search_sym2);
        }
    }
}
