
package ch.ethz.ssh2.packets;

/**
 * PacketGlobalForwardRequest.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: PacketGlobalForwardRequest.java,v 1.1 2012-10-29 22:29:36 HCI\rcundick Exp $
 */
public class PacketGlobalForwardRequest
{
	byte[] payload;

	public boolean wantReply;
	public String bindAddress;
	public int bindPort;

	public PacketGlobalForwardRequest(boolean wantReply, String bindAddress, int bindPort)
	{
		this.wantReply = wantReply;
		this.bindAddress = bindAddress;
		this.bindPort = bindPort;
	}

	public byte[] getPayload()
	{
		if (payload == null)
		{
			TypesWriter tw = new TypesWriter();
			tw.writeByte(Packets.SSH_MSG_GLOBAL_REQUEST);
			
			tw.writeString("tcpip-forward");
			tw.writeBoolean(wantReply);
			tw.writeString(bindAddress);
			tw.writeUINT32(bindPort);

			payload = tw.getBytes();
		}
		return payload;
	}
}
