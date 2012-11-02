
package ch.ethz.ssh2;

/**
 * In most cases you probably do not need the information contained in here.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: ConnectionInfo.java,v 1.1 2012-10-29 22:29:37 HCI\rcundick Exp $
 */
public class ConnectionInfo
{
	/**
	 * The used key exchange (KEX) algorithm in the latest key exchange.
	 */
	public String keyExchangeAlgorithm;

	/**
	 * The currently used crypto algorithm for packets from to the client to the
	 * server.
	 */
	public String clientToServerCryptoAlgorithm;
	/**
	 * The currently used crypto algorithm for packets from to the server to the
	 * client.
	 */
	public String serverToClientCryptoAlgorithm;

	/**
	 * The currently used MAC algorithm for packets from to the client to the
	 * server.
	 */
	public String clientToServerMACAlgorithm;
	/**
	 * The currently used MAC algorithm for packets from to the server to the
	 * client.
	 */
	public String serverToClientMACAlgorithm;

	/**
	 * The type of the server host key (currently either "ssh-dss" or
	 * "ssh-rsa").
	 */
	public String serverHostKeyAlgorithm;
	/**
	 * The server host key that was sent during the latest key exchange.
	 */
	public byte[] serverHostKey;

	/**
	 * Number of kex exchanges performed on this connection so far.
	 */
	public int keyExchangeCounter = 0;
}
