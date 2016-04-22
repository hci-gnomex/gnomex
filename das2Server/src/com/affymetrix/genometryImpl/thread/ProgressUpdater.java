// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.thread;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

public class ProgressUpdater
{
    private static final boolean DEBUG = false;
    private static final int NUM_THREADS = 1;
    private static final boolean DONT_INTERRUPT_IF_RUNNING = false;
    private static final int SECONDS_BETWEEN_UPDATE = 1;
    private ScheduledFuture<?> progressUpdateFuture;
    private final String name;
    private final long startPosition;
    private final long endPosition;
    private final PositionCalculator positionCalculator;
    
    public ProgressUpdater(final String name, final long startPosition, final long endPosition, final PositionCalculator positionCalculator) {
        this.name = name;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.positionCalculator = positionCalculator;
    }
    
    public String getName() {
        return this.name;
    }
    
    public long getStartPosition() {
        return this.startPosition;
    }
    
    public long getEndPosition() {
        return this.endPosition;
    }
    
    public double getProgress() {
        return (this.positionCalculator.getCurrentPosition() - this.getStartPosition()) / (this.getEndPosition() - this.getStartPosition());
    }
    
    public PositionCalculator getPositionCalculator() {
        return this.positionCalculator;
    }
    
    public void start() {
        final CThreadWorker<?, ?> ctw = CThreadHolder.getInstance().getCurrentCThreadWorker();
        if (ctw != null) {
            final ScheduledExecutorService fScheduler = Executors.newScheduledThreadPool(1);
            final Runnable progressUpdateTask = new ProgressUpdateTask(this);
            this.progressUpdateFuture = fScheduler.scheduleWithFixedDelay(progressUpdateTask, 0L, 1L, TimeUnit.SECONDS);
        }
    }
    
    public void kill() {
        if (this.progressUpdateFuture != null) {
            this.progressUpdateFuture.cancel(false);
        }
    }
    
    private static final class ProgressUpdateTask implements Runnable
    {
        private final ProgressUpdater progressUpdater;
        private final CThreadWorker<?, ?> ctw;
        
        private ProgressUpdateTask(final ProgressUpdater progressUpdater) {
            this.progressUpdater = progressUpdater;
            this.ctw = CThreadHolder.getInstance().getCurrentCThreadWorker();
        }
        
        @Override
        public void run() {
            final double progress = this.progressUpdater.getProgress();
            this.ctw.setProgressAsPercent(progress);
        }
    }
}
