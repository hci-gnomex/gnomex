/*
 * $Id: FileBlockConsumer.java,v 1.1 2012-10-29 22:29:54 HCI\rcundick Exp $
 */
package lia.util.net.copy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * This interface, together with {@link FileBlockProducer} acts as a bridge
 * between the Readers and the Writers in the FDT App
 * 
 * @see BlockingQueue
 * @author ramiro
 * 
 */
public interface FileBlockConsumer {
    
    public boolean offer(final FileBlock fileBlock, long delay, TimeUnit unit) throws InterruptedException;
    public void put(FileBlock fileBlock) throws InterruptedException;
    
}
