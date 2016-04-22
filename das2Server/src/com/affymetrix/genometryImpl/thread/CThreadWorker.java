// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.thread;

import javax.swing.SwingWorker;

public abstract class CThreadWorker<T, V> extends SwingWorker<T, V>
{
    private static final boolean DEBUG = false;
    private final String message;
    private final int priority;
    private ProgressUpdater progressUpdater;
    
    public CThreadWorker(final String msg) {
        this(msg, 5);
    }
    
    public CThreadWorker(final String msg, final int priority) {
        if (msg == null || msg.length() == 0) {
            throw new IllegalArgumentException("Invalid Statusbar Message");
        }
        this.message = msg;
        if (priority < 1 || priority > 10) {
            throw new IllegalArgumentException("Invalid Thread priority " + priority);
        }
        this.priority = priority;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public ProgressUpdater getProgressUpdater() {
        return this.progressUpdater;
    }
    
    public void setProgressUpdater(final ProgressUpdater progressUpdater) {
        if (this.progressUpdater != null) {
            return;
        }
        (this.progressUpdater = progressUpdater).start();
    }
    
    public final void done() {
        this.finished();
        CThreadHolder.getInstance().notifyEndThread(this);
    }
    
    public void setProgressAsPercent(double percent) {
        if (percent > 1.0) {
            percent = 1.0;
        }
        if (percent < 0.0) {
            percent = 0.0;
        }
        this.setProgress((int)(percent * 100.0));
    }
    
    @Override
    protected final T doInBackground() throws Exception {
        CThreadHolder.getInstance().notifyStartThread(this);
        T t;
        try {
            t = this.runInBackground();
        }
        catch (Exception x) {
            throw x;
        }
        finally {
            if (this.progressUpdater != null) {
                this.progressUpdater.kill();
            }
        }
        CThreadHolder.getInstance().notifyBackgroundDone(this);
        return t;
    }
    
    protected abstract T runInBackground();
    
    protected abstract void finished();
    
    protected boolean showCancelConfirmation() {
        return true;
    }
    
    public void cancelThread(final boolean b) {
        if (!this.showCancelConfirmation()) {
            return;
        }
        this.cancel(b);
    }
}
