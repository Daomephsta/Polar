package io.github.daomephsta.polar.client;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.PolarCommonNetworking;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent.PolarPlayerData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class PolarClientNetworking
{
    public static void initialise()
    {
        ClientPlayNetworking.registerGlobalReceiver(PolarCommonNetworking.SPAWN_ENTITY, 
            PolarClientNetworking::handleSpawnPacket);
        ClientPlayNetworking.registerGlobalReceiver(PolarCommonNetworking.SET_RESIDUAL_CHARGE, 
            PolarClientNetworking::handleResidualChargePacket);
    }
    
    private static void handleSpawnPacket(MinecraftClient client, ClientPlayNetworkHandler handler, 
        PacketByteBuf bytes, PacketSender responseSender)
    {
        EntityType<?> entityType = Registry.ENTITY_TYPE.get(bytes.readVarInt());
        int entityId = bytes.readInt();
        NbtCompound nbt = bytes.readNbt();
        
        client.execute(() ->
        {
            ClientWorld world = MinecraftClient.getInstance().world;
            Entity entity = entityType.create(world);
            entity.readNbt(nbt);
            world.addEntity(entityId, entity);
        });
    }
    
    private static void handleResidualChargePacket(MinecraftClient client, ClientPlayNetworkHandler handler, 
        PacketByteBuf bytes, PacketSender responseSender)
    {
        Polarity polarity = Polarity.fromIndex(bytes.readInt());
        client.execute(() ->
        {
            PolarPlayerData.get(client.player).setResidualPolarity(polarity);
        });
    }
}
