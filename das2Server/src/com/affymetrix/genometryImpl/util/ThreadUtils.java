// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.util.HashMap;
import javax.swing.SwingUtilities;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import java.util.Map;

public final class ThreadUtils
{
    static Map<Object, Executor> obj2exec;
    
    public static synchronized Executor getPrimaryExecutor(final Object key) {
        Executor exec = ThreadUtils.obj2exec.get(key);
        if (exec == null) {
            exec = Executors.newSingleThreadExecutor();
            ThreadUtils.obj2exec.put(key, exec);
        }
        return exec;
    }
    
    public static void runOnEventQueue(final Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        }
        else {
            SwingUtilities.invokeLater(r);
        }
    }
    
    static {
        ThreadUtils.obj2exec = new HashMap<Object, Executor>();
    }
}
