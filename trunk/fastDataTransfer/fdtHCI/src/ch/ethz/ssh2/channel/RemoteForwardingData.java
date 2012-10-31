
package ch.ethz.ssh2.channel;

/**
 * RemoteForwardingData. Data about a requested remote forwarding.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: RemoteForwardingData.java,v 1.1 2012-10-29 22:29:58 HCI\rcundick Exp $
 */
public class RemoteForwardingData
{
	public String bindAddress;
	public int bindPort;

	String targetAddress;
	int targetPort;
}
