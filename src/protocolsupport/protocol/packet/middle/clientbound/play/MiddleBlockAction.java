package protocolsupport.protocol.packet.middle.clientbound.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public abstract class MiddleBlockAction extends MiddleBlock {

	public MiddleBlockAction(ConnectionImpl connection) {
		super(connection);
	}

	protected int actionId;
	protected int actionParam;
	protected int blockId;

	@Override
	protected void readServerData(ByteBuf serverdata) {
		super.readServerData(serverdata);
		actionId = serverdata.readUnsignedByte();
		actionParam = serverdata.readUnsignedByte();
		blockId = VarNumberSerializer.readVarInt(serverdata);
	}

}
