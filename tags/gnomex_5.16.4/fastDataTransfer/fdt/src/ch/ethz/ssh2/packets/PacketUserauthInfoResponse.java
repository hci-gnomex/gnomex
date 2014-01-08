
package ch.ethz.ssh2.packets;

/**
 * PacketUserauthInfoResponse.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: PacketUserauthInfoResponse.java,v 1.1 2012-10-29 22:29:41 HCI\rcundick Exp $
 */
public class PacketUserauthInfoResponse
{
	byte[] payload;

	String[] responses;

	public PacketUserauthInfoResponse(String[] responses)
	{
		this.responses = responses;
	}

	public byte[] getPayload()
	{
		if (payload == null)
		{
			TypesWriter tw = new TypesWriter();
			tw.writeByte(Packets.SSH_MSG_USERAUTH_INFO_RESPONSE);
			tw.writeUINT32(responses.length);
			for (int i = 0; i < responses.length; i++)
				tw.writeString(responses[i]);

			payload = tw.getBytes();
		}
		return payload;
	}
}
