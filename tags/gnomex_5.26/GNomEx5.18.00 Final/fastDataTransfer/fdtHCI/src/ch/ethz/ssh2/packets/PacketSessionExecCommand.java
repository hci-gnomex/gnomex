package ch.ethz.ssh2.packets;


/**
 * PacketSessionExecCommand.
 * 
 * @author Christian Plattner, plattner@inf.ethz.ch
 * @version $Id: PacketSessionExecCommand.java,v 1.1 2012-10-29 22:29:35 HCI\rcundick Exp $
 */
public class PacketSessionExecCommand
{
	byte[] payload;

	public int recipientChannelID;
	public boolean wantReply;
	public String command;

	public PacketSessionExecCommand(int recipientChannelID, boolean wantReply, String command)
	{
		this.recipientChannelID = recipientChannelID;
		this.wantReply = wantReply;
		this.command = command;
	}
	
	public byte[] getPayload()
	{
		if (payload == null)
		{
			TypesWriter tw = new TypesWriter();
			tw.writeByte(Packets.SSH_MSG_CHANNEL_REQUEST);
			tw.writeUINT32(recipientChannelID);
			tw.writeString("exec");
			tw.writeBoolean(wantReply);
			tw.writeString(command);
			payload = tw.getBytes();
		}
		return payload;
	}
}
