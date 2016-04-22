// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import com.affymetrix.genometryImpl.symmetry.SeqSymmetry;
import java.util.List;
import javax.swing.JPopupMenu;

public interface ContextualPopupListener
{
    void popupNotify(final JPopupMenu p0, final List<SeqSymmetry> p1, final SeqSymmetry p2);
}
