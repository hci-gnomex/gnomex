/*
 * $Id: FileBlockProducer.java,v 1.1 2012-10-29 22:29:40 HCI\rcundick Exp $
 */
package lia.util.net.copy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 
 * This interface, together with {@link FileBlockConsumer} acts as a bridge
 * between the Readers and the Writers in the FDT App
 * 
 * @see BlockingQueue
 * @author ramiro
 */
public interface FileBlockProducer {

    public FileBlock take() throws InterruptedException;
    public FileBlock poll();
    public FileBlock poll(long delay, TimeUnit unit) throws InterruptedException;
    public void transportWorkerDown() throws Exception;
    
}
