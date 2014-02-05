
package ch.ethz.ssh2.packets;

/**
 * PacketGlobalCancelForwardRequest.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: PacketGlobalCancelForwardRequest.java,v 1.1 2012-10-29 22:29:35 HCI\rcundick Exp $
 */
public class PacketGlobalCancelForwardRequest
{
	byte[] payload;

	public boolean wantReply;
	public String bindAddress;
	public int bindPort;

	public PacketGlobalCancelForwardRequest(boolean wantReply, String bindAddress, int bindPort)
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
			
			tw.writeString("cancel-tcpip-forward");
			tw.writeBoolean(wantReply);
			tw.writeString(bindAddress);
			tw.writeUINT32(bindPort);

			payload = tw.getBytes();
		}
		return payload;
	}
}
