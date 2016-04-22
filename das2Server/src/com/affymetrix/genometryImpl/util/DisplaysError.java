// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.logging.Level;
import com.affymetrix.genometryImpl.event.GenericAction;
import java.util.List;

public interface DisplaysError
{
    void showError(final String p0, final String p1, final List<GenericAction> p2, final Level p3);
}
