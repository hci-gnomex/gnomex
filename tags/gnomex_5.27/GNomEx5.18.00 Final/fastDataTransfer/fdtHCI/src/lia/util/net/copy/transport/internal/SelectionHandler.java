/*
 * $Id: SelectionHandler.java,v 1.1 2012-10-29 22:30:17 HCI\rcundick Exp $
 */
package lia.util.net.copy.transport.internal;


/**
 * Every class interested in I/O Events should implement this.
 * It is used by the SelectionManager to notify I/O readiness
 *   
 * @author ramiro
 */
public interface SelectionHandler {
    
    public void handleSelection(FDTSelectionKey fdtSelectionKey);
    public void canceled(FDTSelectionKey fdtSelectionKey);
    
}
