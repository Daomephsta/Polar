package io.github.daomephsta.polar.common.network;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent.PolarPlayerData;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class SetResidualChargeS2CPacketType implements PacketType
{
	private static final Identifier ID = new Identifier(Polar.MOD_ID, "set_residual_charge");
	
	public Packet<?> toPacket(Polarity polarity)
	{
		PacketByteBuf bytes = new PacketByteBuf(Unpooled.buffer());
		bytes.writeInt(polarity.getIndex());
		return ServerSidePacketRegistry.INSTANCE.toPacket(getId(), bytes);
	}
	
	@Override
	public void accept(PacketContext context, PacketByteBuf bytes)
	{
		Polarity polarity = Polarity.fromIndex(bytes.readInt());
		PolarPlayerData.get(context.getPlayer()).setResidualPolarity(polarity);
	}
	
	@Override
	public Identifier getId()
	{
		return ID;
	}
}
