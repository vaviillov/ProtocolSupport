package protocolsupport.protocol.packet.middle.clientbound.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.chat.ChatAPI;
import protocolsupport.api.chat.components.BaseComponent;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.packet.middle.ClientBoundMiddlePacket;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.utils.EnumConstantLookups;

public abstract class MiddleTitle extends ClientBoundMiddlePacket {

	public MiddleTitle(ConnectionImpl connection) {
		super(connection);
	}

	protected Action action;
	protected BaseComponent message;
	protected int fadeIn;
	protected int stay;
	protected int fadeOut;

	@Override
	protected void readServerData(ByteBuf serverdata) {
		action = MiscSerializer.readVarIntEnum(serverdata, Action.CONSTANT_LOOKUP);
		switch (action) {
			case SET_TITLE:
			case SET_SUBTITLE:
			case SET_ACTION_BAR: {
				message = ChatAPI.fromJSON(StringSerializer.readVarIntUTF8String(serverdata), true);
				break;
			}
			case SET_TIMES: {
				fadeIn = serverdata.readInt();
				stay = serverdata.readInt();
				fadeOut = serverdata.readInt();
				break;
			}
			case HIDE:
			case RESET: {
				break;
			}
		}
	}

	protected static enum Action {
		SET_TITLE, SET_SUBTITLE, SET_ACTION_BAR, SET_TIMES, HIDE, RESET;
		public static final EnumConstantLookups.EnumConstantLookup<Action> CONSTANT_LOOKUP = new EnumConstantLookups.EnumConstantLookup<>(Action.class);
	}

}
