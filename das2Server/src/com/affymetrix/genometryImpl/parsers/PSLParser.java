// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.io.DataInputStream;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;

public final class PSLParser extends AbstractPSLParser implements AnnotationWriter, IndexWriter, Parser
{
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        this.enableSharedQueryTarget(true);
        InputStream is2 = is;
        if (!annotate_seq) {
            is2 = new DataInputStream(is);
        }
        return this.parse(is2, annotate_seq ? uri : nameType, null, group, null, false, annotate_seq, false);
    }
}
