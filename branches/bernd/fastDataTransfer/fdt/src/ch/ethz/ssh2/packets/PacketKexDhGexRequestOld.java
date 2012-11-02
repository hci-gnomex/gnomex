
package ch.ethz.ssh2.packets;

import ch.ethz.ssh2.DHGexParameters;

/**
 * PacketKexDhGexRequestOld.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: PacketKexDhGexRequestOld.java,v 1.1 2012-10-29 22:29:41 HCI\rcundick Exp $
 */
public class PacketKexDhGexRequestOld
{
	byte[] payload;

	int n;

	public PacketKexDhGexRequestOld(DHGexParameters para)
	{
		this.n = para.getPref_group_len();
	}

	public byte[] getPayload()
	{
		if (payload == null)
		{
			TypesWriter tw = new TypesWriter();
			tw.writeByte(Packets.SSH_MSG_KEX_DH_GEX_REQUEST_OLD);
			tw.writeUINT32(n);
			payload = tw.getBytes();
		}
		return payload;
	}
}
