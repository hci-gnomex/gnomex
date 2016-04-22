// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.thread;

import java.util.EventObject;

public class CThreadEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    public static int STARTED;
    public static int ENDED;
    private final int state;
    
    public CThreadEvent(final CThreadWorker<?, ?> worker, final int state) {
        super(worker);
        if (state != CThreadEvent.STARTED && state != CThreadEvent.ENDED) {
            throw new IllegalArgumentException("Invalid Statusbar Message");
        }
        this.state = state;
    }
    
    public int getState() {
        return this.state;
    }
    
    static {
        CThreadEvent.STARTED = 0;
        CThreadEvent.ENDED = 1;
    }
}
