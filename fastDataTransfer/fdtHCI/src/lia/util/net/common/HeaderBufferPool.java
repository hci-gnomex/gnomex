/*
 * $Id: HeaderBufferPool.java,v 1.1 2012-10-29 22:29:45 HCI\rcundick Exp $
 */
package lia.util.net.common;
import gui.Log;
import java.util.logging.Level;

/**
 * 
 * This class is only for the love of fast development. 
 * TODO - We should have only one buffer pool in entire FDT Application;
 * when this will happen this class will disappear ....
 *  
 * @author ramiro
 */
public class HeaderBufferPool extends AbstractBPool {

    private static final Log logger = Log.getLoggerInstance();
    ;
    //the list of ByteBuffer-s
    private static HeaderBufferPool _theInstance;
    //used for double checked locking
    private static volatile boolean initialized = false;

    private HeaderBufferPool(int bufferSize, int maxPollIter, boolean trackAllocations) {
        super(bufferSize, maxPollIter, trackAllocations, false);
    }


    public static final HeaderBufferPool getInstance() {
        //double checked locking
        if (!initialized) {
            synchronized (DirectByteBufferPool.class) {
                while (!initialized) {
                    try {
                        DirectByteBufferPool.class.wait();
                    } catch (Throwable t) {
                        logger.log(Level.WARNING, " Got exception waiting for initialization ", t);
                    }
                }
            }
        }

        return _theInstance;
    }

    public static final boolean initInstance() {

        synchronized (HeaderBufferPool.class) {
            if (!initialized) {
                
                _theInstance = new HeaderBufferPool(Config.HEADER_SIZE, 0, Config.TRACK_ALLOCATIONS);

                initialized = true;

                HeaderBufferPool.class.notifyAll();

                return true;
            }

        }

        return false;
    }

}
