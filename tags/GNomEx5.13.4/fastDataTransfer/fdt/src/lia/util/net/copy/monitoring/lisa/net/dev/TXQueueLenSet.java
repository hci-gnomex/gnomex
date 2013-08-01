/*
 * $Id: TXQueueLenSet.java,v 1.1 2012-10-29 22:30:18 HCI\rcundick Exp $
 */
package lia.util.net.copy.monitoring.lisa.net.dev;

import java.io.Serializable;

/**
 * Wrapper for setting quelen...
 * @author Ciprian Dobre
 */
public class TXQueueLenSet implements Serializable {

	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1988671591829311032L;
	
	public String ifName;
	
	public int txqueuelen;
	
} // end of class TXQueueLenSet

