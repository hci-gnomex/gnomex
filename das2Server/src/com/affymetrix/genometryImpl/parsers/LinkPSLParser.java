// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import com.affymetrix.genometryImpl.AnnotatedSeqGroup;
import java.io.InputStream;

public final class LinkPSLParser extends AbstractPSLParser
{
    @Override
    public List<? extends SeqSymmetry> parse(final InputStream is, final AnnotatedSeqGroup group, final String nameType, final String uri, final boolean annotate_seq) throws Exception {
        this.setIsLinkPsl(true);
        this.enableSharedQueryTarget(true);
        return this.parse(is, annotate_seq ? uri : nameType, null, group, null, false, annotate_seq, false);
    }
}
