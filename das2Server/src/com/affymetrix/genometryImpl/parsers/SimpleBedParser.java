// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.parsers;

import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import com.affymetrix.genometryImpl.util.SeqUtils;
import com.affymetrix.genometryImpl.SeqSpan;
import java.util.ArrayList;
import java.io.OutputStream;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.Collection;

public final class SimpleBedParser implements AnnotationWriter
{
    @Override
    public String getMimeType() {
        return "text/plain";
    }
    
    @Override
    public boolean writeAnnotations(final Collection<? extends SeqSymmetry> syms, final BioSeq seq, final String type, final OutputStream outstream) throws IOException {
        final List<SeqSpan> spanlist = new ArrayList<SeqSpan>(syms.size());
        for (final SeqSymmetry sym : syms) {
            SeqUtils.collectLeafSpans(sym, seq, spanlist);
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
        boolean success;
        try {
            final Writer bw = new BufferedWriter(new OutputStreamWriter(outstream));
            for (final SeqSpan span : spanlist) {
                bw.write(span.getBioSeq().getID());
                bw.write(9);
                bw.write(Integer.toString(span.getMin()));
                bw.write(9);
                bw.write(Integer.toString(span.getMax()));
                bw.write(10);
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            bw.flush();
            success = true;
        }
        catch (Exception ex) {
            success = false;
            final IOException ioe = new IOException(ex.getMessage());
            ioe.initCause(ex);
            throw ioe;
        }
        return success;
    }
}
