package protocolsupport.protocol.packet.middleimpl.serverbound.handshake.v_5;

import org.bukkit.Bukkit;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.packet.PacketType;
import protocolsupport.protocol.packet.middle.ServerBoundMiddlePacket;
import protocolsupport.protocol.packet.middleimpl.ServerBoundPacketData;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;

public class Ping extends ServerBoundMiddlePacket {

	public Ping(ConnectionImpl connection) {
		super(connection);
	}

	@Override
	protected void readClientData(ByteBuf clientdata) {
		clientdata.readUnsignedByte();
	}

	@Override
	protected void writeToServer() {
		ServerBoundPacketData setprotocol = ServerBoundPacketData.create(PacketType.SERVERBOUND_HANDSHAKE_START);
		VarNumberSerializer.writeVarInt(setprotocol, ProtocolVersionsHelper.LATEST_PC.getId());
		StringSerializer.writeVarIntUTF8String(setprotocol, "");
		setprotocol.writeShort(Bukkit.getPort());
		VarNumberSerializer.writeVarInt(setprotocol, 1);
		codec.read(setprotocol);

		codec.read(ServerBoundPacketData.create(PacketType.SERVERBOUND_STATUS_REQUEST));
	}

}
