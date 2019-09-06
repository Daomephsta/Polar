package io.github.daomephsta.polar.common.network;

import net.fabricmc.fabric.api.network.PacketConsumer;
import net.minecraft.util.Identifier;

public interface PacketType extends PacketConsumer
{
	public Identifier getId();
}
