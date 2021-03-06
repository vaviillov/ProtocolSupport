package protocolsupport.protocol.packet.middle.clientbound.play;

import io.netty.buffer.ByteBuf;
import protocolsupport.api.tab.TabAPI;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.packet.middle.ClientBoundMiddlePacket;
import protocolsupport.protocol.serializer.StringSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.storage.netcache.AttributesCache;
import protocolsupport.protocol.storage.netcache.NetworkEntityCache;
import protocolsupport.protocol.storage.netcache.window.WindowCache;
import protocolsupport.protocol.typeremapper.window.AbstractWindowsRemapper;
import protocolsupport.protocol.typeremapper.window.WindowsRemappersRegistry;
import protocolsupport.protocol.types.Environment;
import protocolsupport.protocol.types.GameMode;
import protocolsupport.protocol.types.WindowType;
import protocolsupport.protocol.types.networkentity.NetworkEntity;

public abstract class MiddleStartGame extends ClientBoundMiddlePacket {

	protected final AttributesCache clientCache = cache.getAttributesCache();
	protected final NetworkEntityCache entityCache = cache.getEntityCache();
	protected final WindowCache windowCache = cache.getWindowCache();

	protected final AbstractWindowsRemapper windowRemapper = WindowsRemappersRegistry.get(version);

	public MiddleStartGame(ConnectionImpl connection) {
		super(connection);
	}

	protected NetworkEntity player;
	protected GameMode gamemode;
	protected boolean hardcore;
	protected Environment dimension;
	protected long hashedSeed;
	protected int maxplayers;
	protected String leveltype;
	protected int renderDistance;
	protected boolean reducedDebugInfo;
	protected boolean respawnScreenEnabled;

	@Override
	protected void readServerData(ByteBuf serverdata) {
		player = NetworkEntity.createPlayer(serverdata.readInt());
		int gmdata = serverdata.readByte();
		gamemode = GameMode.getById(gmdata & 0xFFFFFFF7);
		hardcore = (gmdata & 0x8) == 0x8;
		dimension = Environment.getById(serverdata.readInt());
		hashedSeed = serverdata.readLong();
		serverdata.readByte();
		maxplayers = TabAPI.getMaxTabSize();
		leveltype = StringSerializer.readVarIntUTF8String(serverdata);
		renderDistance = VarNumberSerializer.readVarInt(serverdata);
		reducedDebugInfo = serverdata.readBoolean();
		respawnScreenEnabled = serverdata.readBoolean();
	}

	@Override
	protected void handleReadData() {
		clientCache.setCurrentDimension(dimension);
		clientCache.setRespawnScreenEnabled(respawnScreenEnabled);
		entityCache.clearEntities();
		entityCache.setSelf(player);
		windowCache.setPlayerWindow(windowRemapper.get(WindowType.PLAYER, 0));
	}

}
