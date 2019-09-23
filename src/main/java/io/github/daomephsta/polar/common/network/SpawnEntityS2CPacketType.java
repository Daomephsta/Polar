package io.github.daomephsta.polar.common.network;

import io.github.daomephsta.polar.common.Polar;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class SpawnEntityS2CPacketType implements PacketType
{
	private static final Identifier ID = new Identifier(Polar.MOD_ID, "spawn_entity");

	public Packet<?> toPacket(Entity entity)
	{
		PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
		data.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
		data.writeInt(entity.getEntityId());
		CompoundTag tag = new CompoundTag();
		data.writeCompoundTag(entity.toTag(tag));
		return ServerSidePacketRegistry.INSTANCE.toPacket(getId(), data);
	}

	@Override
	public void accept(PacketContext context, PacketByteBuf data)
	{
		EntityType<?> entityType = Registry.ENTITY_TYPE.get(data.readVarInt());
		int entityId = data.readInt();
		CompoundTag tag = data.readCompoundTag();
		
		context.getTaskQueue().execute(() ->
		{
			ClientWorld world = MinecraftClient.getInstance().world;
			Entity entity = entityType.create(world);
			entity.fromTag(tag);
			world.addEntity(entityId, entity);
		});
	}

	@Override
	public Identifier getId()
	{
		return ID;
	}
}