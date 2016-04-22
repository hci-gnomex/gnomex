// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.thread;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import com.affymetrix.genometryImpl.util.ThreadUtils;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.Map;
import java.util.Set;

public class CThreadHolder implements WaitHelperI
{
    private static final boolean DEBUG = false;
    private final Set<CThreadWorker<?, ?>> RUNNING_CTHREADWORKERS;
    private static final CThreadWorker<?, ?> NOOP;
    private final Map<Thread, CThreadWorker<?, ?>> thread2CThreadWorker;
    private static CThreadHolder singleton;
    private final Set<CThreadListener> listeners;
    private CountDownLatch threadLatch;
    
    public static CThreadHolder getInstance() {
        if (CThreadHolder.singleton == null) {
            CThreadHolder.singleton = new CThreadHolder();
        }
        return CThreadHolder.singleton;
    }
    
    private CThreadHolder() {
        this.RUNNING_CTHREADWORKERS = new HashSet<CThreadWorker<?, ?>>();
        this.thread2CThreadWorker = new HashMap<Thread, CThreadWorker<?, ?>>();
        this.listeners = new HashSet<CThreadListener>();
    }
    
    public void cancelAllTasks() {
        for (final CThreadWorker<?, ?> worker : this.getAllCThreadWorkers()) {
            if (worker != null && !worker.isCancelled() && !worker.isDone()) {
                worker.cancelThread(true);
            }
        }
    }
    
    public void execute(final Object obj, final CThreadWorker<?, ?> worker) {
        if (obj == null || worker == null) {
            throw new IllegalArgumentException("None of parameters can be null");
        }
        ThreadUtils.getPrimaryExecutor(obj).execute(worker);
    }
    
    public void addListener(final CThreadListener listener) {
        this.listeners.add(listener);
    }
    
    public CThreadWorker<?, ?> getCurrentCThreadWorker() {
        synchronized (this.thread2CThreadWorker) {
            CThreadWorker<?, ?> currentCThreadWorker = this.thread2CThreadWorker.get(Thread.currentThread());
            if (currentCThreadWorker == null) {
                currentCThreadWorker = CThreadHolder.NOOP;
            }
            return currentCThreadWorker;
        }
    }
    
    public Set<CThreadWorker<?, ?>> getAllCThreadWorkers() {
        synchronized (this.thread2CThreadWorker) {
            return new CopyOnWriteArraySet<CThreadWorker<?, ?>>(this.thread2CThreadWorker.values());
        }
    }
    
    public int getCThreadWorkerCount() {
        synchronized (this.thread2CThreadWorker) {
            return this.thread2CThreadWorker.size();
        }
    }
    
    public void notifyStartThread(final CThreadWorker<?, ?> worker) {
        this.RUNNING_CTHREADWORKERS.add(worker);
        synchronized (this.thread2CThreadWorker) {
            if (this.thread2CThreadWorker.get(Thread.currentThread()) != null) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Thread " + Thread.currentThread() + " already has " + this.thread2CThreadWorker.get(Thread.currentThread()) + " = " + this.thread2CThreadWorker.get(Thread.currentThread()).getMessage() + ", and is starting " + worker + " = " + worker.getMessage());
            }
            this.thread2CThreadWorker.put(Thread.currentThread(), worker);
        }
        this.fireThreadEvent(worker, CThreadEvent.STARTED);
    }
    
    public void notifyBackgroundDone(final CThreadWorker<?, ?> worker) {
        synchronized (this.thread2CThreadWorker) {
            Thread thread = null;
            for (final Thread threadLoop : this.thread2CThreadWorker.keySet()) {
                if (worker == this.thread2CThreadWorker.get(threadLoop)) {
                    thread = threadLoop;
                    break;
                }
            }
            if (thread == null) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "could not find thread for " + worker.getMessage());
            }
            else {
                this.thread2CThreadWorker.remove(thread);
            }
        }
    }
    
    public void notifyEndThread(final CThreadWorker<?, ?> worker) {
        this.fireThreadEvent(worker, CThreadEvent.ENDED);
        synchronized (this.RUNNING_CTHREADWORKERS) {
            this.RUNNING_CTHREADWORKERS.remove(worker);
            if (this.RUNNING_CTHREADWORKERS.size() == 0 && this.threadLatch != null) {
                ThreadUtils.runOnEventQueue(new Runnable() {
                    @Override
                    public void run() {
                        CThreadHolder.this.threadLatch.countDown();
                    }
                });
            }
        }
    }
    
    private void fireThreadEvent(final CThreadWorker<?, ?> worker, final int state) {
        final CThreadEvent event = new CThreadEvent(worker, state);
        for (final CThreadListener listener : this.listeners) {
            listener.heardThreadEvent(event);
        }
    }
    
    @Override
    public Boolean waitForAll() {
        synchronized (this.RUNNING_CTHREADWORKERS) {
            if (this.RUNNING_CTHREADWORKERS.size() == 0) {
                return Boolean.TRUE;
            }
            if (this.threadLatch == null || this.threadLatch.getCount() == 0L) {
                this.threadLatch = new CountDownLatch(1);
            }
        }
        try {
            this.threadLatch.await();
        }
        catch (InterruptedException x) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Script getWaitHelper().run() interrupted", x);
        }
        this.threadLatch = null;
        return Boolean.TRUE;
    }
    
    static {
        NOOP = new CThreadWorker<Void, Void>("noop") {
            @Override
            protected Void runInBackground() {
                return null;
            }
            
            @Override
            protected void finished() {
            }
        };
    }
}
