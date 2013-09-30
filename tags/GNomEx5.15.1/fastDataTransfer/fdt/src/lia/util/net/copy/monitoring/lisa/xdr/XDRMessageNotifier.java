/*
 * $Id: XDRMessageNotifier.java,v 1.1 2012-10-29 22:29:45 HCI\rcundick Exp $
 */
package lia.util.net.copy.monitoring.lisa.xdr;

/**
 * 
 * @author Adrian Muraru
 */
public interface XDRMessageNotifier {
    public void notifyXDRMessage(XDRMessage message, XDRGenericComm comm);
    public void notifyXDRCommClosed(XDRGenericComm comm);
}
