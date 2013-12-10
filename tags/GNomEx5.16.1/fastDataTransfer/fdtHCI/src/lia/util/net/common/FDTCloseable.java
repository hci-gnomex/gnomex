/*
 * $Id: FDTCloseable.java,v 1.1 2012-10-29 22:29:44 HCI\rcundick Exp $
 */
package lia.util.net.common;


/**
 * An extended version of {@link java.io.Closeable} with the possibility to specify 
 * an eventual message and/or exception
 * 
 * The close() methods return true if they have been already called 
 * 
 * @author ramiro
 * 
 */
public interface FDTCloseable {

    public boolean close(String downMessage, Throwable downCause);
    public boolean isClosed();
    
}
