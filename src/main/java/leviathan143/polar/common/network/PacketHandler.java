package leviathan143.polar.common.network;

import leviathan143.polar.common.Polar;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
	public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(Polar.MODID);
	private static int packetID = 0;

	public static void registerPackets()
	{
		registerPacket(PacketSetResidualCharge.Handler.class, PacketSetResidualCharge.class, Side.CLIENT);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side)
	{
		CHANNEL.registerMessage(messageHandler, requestMessageType, packetID, side);
		packetID++;
	}
}
