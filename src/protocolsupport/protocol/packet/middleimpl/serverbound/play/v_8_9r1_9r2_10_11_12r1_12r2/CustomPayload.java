package protocolsupport.protocol.packet.middleimpl.serverbound.play.v_8_9r1_9r2_10_11_12r1_12r2;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.packet.middle.ServerBoundMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.storage.netcache.CustomPayloadChannelsCache;
import protocolsupport.protocol.typeremapper.legacy.LegacyCustomPayloadChannelName;
import protocolsupport.protocol.typeremapper.legacy.LegacyCustomPayloadData;

public class CustomPayload extends ServerBoundMiddlePacket {

	protected final CustomPayloadChannelsCache channelsCache = cache.getChannelsCache();

	public CustomPayload(ConnectionImpl connection) {
		super(connection);
	}

	protected String tag;
	protected ByteBuf data;

	@Override
	protected void readClientData(ByteBuf clientdata) {
		tag = StringSerializer.readVarIntUTF8String(clientdata, 20);
		data = MiscSerializer.readAllBytesSlice(clientdata, Short.MAX_VALUE);
	}

	@Override
	protected void writeToServer() {
		switch (tag) {
			case LegacyCustomPayloadChannelName.LEGACY_REGISTER: {
				LegacyCustomPayloadData.transformAndWriteRegisterUnregister(codec, channelsCache, tag, data, true);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_UNREGISTER: {
				LegacyCustomPayloadData.transformAndWriteRegisterUnregister(codec, channelsCache, tag, data, false);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_BOOK_EDIT: {
				LegacyCustomPayloadData.transformAndWriteBookEdit(codec, version, data);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_BOOK_SIGN: {
				LegacyCustomPayloadData.transformAndWriteBookSign(codec, version, data);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_SET_BEACON: {
				LegacyCustomPayloadData.transformAndWriteSetBeaconEffect(codec, data);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_NAME_ITEM: {
				LegacyCustomPayloadData.transformAndWriteNameItemSString(codec, data);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_TRADE_SELECT: {
				LegacyCustomPayloadData.transformAndWriteTradeSelect(codec, data);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_PICK_ITEM: {
				LegacyCustomPayloadData.transformAndWritePickItem(codec, data);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_STRUCTURE_BLOCK: {
				LegacyCustomPayloadData.transformAndWriteStructureBlock(codec, data);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_COMMAND_RIGHT_NAME:
			case LegacyCustomPayloadChannelName.LEGACY_COMMAND_TYPO_NAME: {
				LegacyCustomPayloadData.transformAndWriteAdvancedCommandBlockEdit(codec, data, true);
				break;
			}
			case LegacyCustomPayloadChannelName.LEGACY_COMMAND_BLOCK_NAME: {
				LegacyCustomPayloadData.transformAndWriteAutoCommandBlockEdit(codec, data);
				break;
			}
			default: {
				LegacyCustomPayloadData.transformAndWriteCustomPayload(codec, tag, data);
				break;
			}
		}
	}

}
