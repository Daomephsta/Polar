 package io.github.daomephsta.polar.common.network;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;

public class PacketTypes
{
	public static final SpawnEntityS2CPacketType SPAWN_ENTITY = registerS2CPacket(new SpawnEntityS2CPacketType());
	public static final SetResidualChargeS2CPacketType SET_RESIDUAL_CHARGE = registerS2CPacket(new SetResidualChargeS2CPacketType());
	
	public static void initialise()
	{
		//Dummy method to force static init
	}

	private static <P extends PacketType> P registerS2CPacket(P packetType)
	{
		ClientSidePacketRegistry.INSTANCE.register(packetType.getId(), packetType);
		return packetType;
	}
	
	private static <P extends PacketType> P registerC2SPacket(P packetType)
	{
		ServerSidePacketRegistry.INSTANCE.register(packetType.getId(), packetType);
		return packetType;
	}
}
