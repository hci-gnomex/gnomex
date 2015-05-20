/*
 * $Id: AccountableEntity.java,v 1.1 2012-10-29 22:29:37 HCI\rcundick Exp $
 */
package lia.util.net.copy;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * Default implementation for {@link Accountable}
 * @author ramiro
 * 
 */
public abstract class AccountableEntity implements Accountable {

    AtomicLong totalProcessedBytes;
    AtomicLong totalUtilBytes;
    
    public long addAndGetTotalBytes(long delta) {
        return totalProcessedBytes.addAndGet(delta);
    }

    public long addAndGetUtilBytes(long delta) {
        return totalUtilBytes.addAndGet(delta);
    }

    public long getTotalBytes() {
        return totalProcessedBytes.get();
    }

    public long getUtilBytes() {
        return totalUtilBytes.get();
    }

    public AccountableEntity() {
        this(0, 0);
    }
    
    public abstract long getSize();
    
    public AccountableEntity(long initialProcessedBytes, long initialUtilBytes) {
        totalProcessedBytes = new AtomicLong(initialProcessedBytes);
        totalUtilBytes = new AtomicLong(initialUtilBytes);
    }
}
