// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import java.util.Collections;
import com.affymetrix.genometryImpl.BioSeq;
import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import java.util.EventObject;

public class NewSymLoadedEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    private final List<? extends SeqSymmetry> new_syms;
    private final BioSeq seq;
    
    public NewSymLoadedEvent(final Object src, final BioSeq seq, final List<? extends SeqSymmetry> syms) {
        super(src);
        this.seq = seq;
        if (syms == null) {
            this.new_syms = Collections.emptyList();
        }
        else {
            this.new_syms = syms;
        }
    }
    
    public List<? extends SeqSymmetry> getNewSyms() {
        return this.new_syms;
    }
    
    public BioSeq getSeq() {
        return this.seq;
    }
}
