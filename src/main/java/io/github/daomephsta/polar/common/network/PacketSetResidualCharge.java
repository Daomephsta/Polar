package io.github.daomephsta.polar.common.network;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent.PolarPlayerData;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class PacketSetResidualCharge
{
	private static final Identifier ID = new Identifier(Polar.MODID, "set_residual_charge");
	
	static void register()
	{
		ClientSidePacketRegistry.INSTANCE.register(ID, PacketSetResidualCharge::process);
	}
	
	private static void process(PacketContext context, PacketByteBuf bytes)
	{
		Polarity polarity = Polarity.fromIndex(bytes.readInt());
		PolarPlayerData.get(context.getPlayer()).setResidualPolarity(polarity);
	}
	
	public static void sendToPlayer(PlayerEntity player, Polarity polarity)
	{
		PacketByteBuf bytes = new PacketByteBuf(Unpooled.buffer());
		ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ID, bytes );
	}
}
