 package io.github.daomephsta.polar.common;

import io.github.daomephsta.polar.api.Polarity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PolarCommonNetworking
{
    public static final Identifier SPAWN_ENTITY = Polar.id("spawn_entity");
    public static final Identifier SET_RESIDUAL_CHARGE = Polar.id("set_residual_charge");

    public static Packet<?> createEntitySpawnPacket(Entity entity)
    {
        PacketByteBuf bytes = new PacketByteBuf(Unpooled.buffer());
        bytes.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
        bytes.writeInt(entity.getId());
        bytes.writeNbt(entity.writeNbt(new NbtCompound()));
        return ServerPlayNetworking.createS2CPacket(SPAWN_ENTITY, bytes);
    }
    
    public static void sendResidualChargePacket(ServerPlayerEntity recipient, Polarity polarity)
    {
        PacketByteBuf bytes = new PacketByteBuf(Unpooled.buffer());
        bytes.writeInt(polarity.getIndex());
        ServerPlayNetworking.send(recipient, SET_RESIDUAL_CHARGE, bytes);
    }
}
