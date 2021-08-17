package io.github.daomephsta.polar.common.research;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.PlayerEntity;

public class InstanceStorage<T>
{
    private Map<PlayerEntity, Collection<T>> client, server;

    public boolean put(PlayerEntity player, T instance)
    {
        return instances(player.world.isClient).computeIfAbsent(player, k -> Sets.newConcurrentHashSet()).add(instance);
    }

    public boolean remove(PlayerEntity player, T instance)
    {
        return get(player).remove(instance);
    }

    public Collection<T> get(PlayerEntity player)
    {
        return instances(player.world.isClient).getOrDefault(player, Collections.emptySet());
    }

    private Map<PlayerEntity, Collection<T>> instances(boolean isClient)
    {
        if (isClient)
        {
            if (client == null)
                client = new ConcurrentHashMap<>();
            return client;
        }
        else
        {
            if (server == null)
                server = new ConcurrentHashMap<>();
            return server;
        }
    }

    @Override
    public String toString()
    {
        var builder = new StringBuilder("InstanceStorage(");
        if (client != null)
        {
            append(builder.append("client="), client);
            if (client != null && server != null)
                builder.append(", ");
        }
        if (server != null)
            append(builder.append("server="), server);
        return builder.append(')').toString();
    }

    private void append(StringBuilder builder, Map<PlayerEntity, Collection<T>> instances)
    {
        builder.append("{");
        int i = 0;
        for (Entry<PlayerEntity, Collection<T>> entry : instances.entrySet())
        {
            if (i > 0) builder.append(", ");
            builder.append(entry.getKey().getEntityName()).append('=').append(entry.getValue());
        }
        builder.append("}");
    }
}