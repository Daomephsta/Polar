 package io.github.daomephsta.polar.common;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent.PolarPlayerData;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PolarCommonNetworking
{
    public static final Identifier SPAWN_ENTITY = Polar.id("spawn_entity"),
                                   SET_RESIDUAL_CHARGE = Polar.id("set_residual_charge"),
                                   RESEARCH = Polar.id("research");

    public static void initialise()
    {
        ServerPlayNetworking.registerGlobalReceiver(PolarCommonNetworking.RESEARCH,
            PolarCommonNetworking::handleResearchPacket);
    }

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

    public enum S2CResearchPacketAction
    {
        COMPLETE, FORGET;
    }

    public static void sendResearchPacket(ServerPlayerEntity recipient, Identifier research, S2CResearchPacketAction action)
    {
        PacketByteBuf bytes = new PacketByteBuf(Unpooled.buffer());
        bytes.writeIdentifier(research);
        bytes.writeEnumConstant(action);
        ServerPlayNetworking.send(recipient, PolarCommonNetworking.RESEARCH, bytes);
    }

    private static void handleResearchPacket(MinecraftServer server, ServerPlayerEntity player,
        ServerPlayNetworkHandler handler, PacketByteBuf bytes, PacketSender responseSender)
    {
        Identifier research = bytes.readIdentifier();
        server.execute(() ->
        {
            PolarPlayerData.get(player).startResearch(research);
        });
    }
}
