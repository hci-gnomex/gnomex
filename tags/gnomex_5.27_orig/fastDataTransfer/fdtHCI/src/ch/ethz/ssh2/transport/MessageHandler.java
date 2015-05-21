package ch.ethz.ssh2.transport;

import java.io.IOException;

/**
 * MessageHandler.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: MessageHandler.java,v 1.1 2012-10-29 22:29:46 HCI\rcundick Exp $
 */
public interface MessageHandler
{
	public void handleMessage(byte[] msg, int msglen) throws IOException;
}
