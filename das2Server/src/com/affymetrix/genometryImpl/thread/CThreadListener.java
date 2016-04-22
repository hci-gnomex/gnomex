// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.thread;

import java.util.EventListener;

public interface CThreadListener extends EventListener
{
    void heardThreadEvent(final CThreadEvent p0);
}
