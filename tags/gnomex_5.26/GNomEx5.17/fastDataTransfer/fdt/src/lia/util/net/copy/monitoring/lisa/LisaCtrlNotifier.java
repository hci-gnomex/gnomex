/*
 * $Id: LisaCtrlNotifier.java,v 1.1 2012-10-29 22:29:47 HCI\rcundick Exp $
 */ 
package lia.util.net.copy.monitoring.lisa;

/**
 * Receiver of remote commands from LISA/ML modules
 * 
 * @author ramiro
 */
public interface LisaCtrlNotifier {

    public void notifyLisaCtrlMsg(String lisaCtrlMsg);
    
}
