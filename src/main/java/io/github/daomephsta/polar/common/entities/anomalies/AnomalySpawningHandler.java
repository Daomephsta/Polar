package io.github.daomephsta.polar.common.entities.anomalies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.Polar;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AnomalySpawningHandler
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static void registerEventCallbacks()
    {
        ServerTickEvents.END_SERVER_TICK.register(AnomalySpawningHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server)
    {
        for (ServerWorld world : server.getWorlds())
        {
            if (Polar.CONFIG.anomalies.isDimensionBlacklisted(world.getRegistryKey().getValue()))
                continue;
            if (world.getTimeOfDay() % 24000 == TimeOfDay.SUNRISE.getTicks() + 1)
                spawnAnomalies(world, Polarity.BLUE);
            else if (world.getTimeOfDay() % 24000 == TimeOfDay.SUNSET.getTicks())
                spawnAnomalies(world, Polarity.RED);
        }
    }

    private static void spawnAnomalies(ServerWorld world, Polarity polarity)
    {
        if (world.getPlayers().isEmpty())
            return;
        //Scales more slowly with increasing numbers of players
        int perPlayerAnomalySpawns = (int) Math.ceil(2 + 0.65 * Math.log(world.getPlayers().size()));
        LOGGER.info("Spawning {} anomalies() per player for {} players", perPlayerAnomalySpawns, world.getPlayers().size());
        for (PlayerEntity player : world.getPlayers())
        {
            for (int i = 0; i < perPlayerAnomalySpawns; i++)
            {
                int r = Polar.CONFIG.anomalies.minRadius() + 
                        world.getRandom().nextInt(Polar.CONFIG.anomalies.maxRadius() - Polar.CONFIG.anomalies.minRadius());
                double theta = world.getRandom().nextDouble() * 2.0D * Math.PI;
                spawnAnomaly(world, player.getX() + r * Math.cos(theta), player.getZ() + r * Math.sin(theta), polarity);
            }
        }
    }
    
    private static void spawnAnomaly(World world, double d, double e, Polarity polarity)
    {    
        //Find all the gaps along the y axis with air in them at the x z coordinates 
        IntList airRanges = new IntArrayList(6);
        BlockPos.Mutable pos = new BlockPos.Mutable(d, 0, e);
        int maxY = Math.min(world.getHeight(), Polar.CONFIG.anomalies.maxSpawnY());
        for(int y = Polar.CONFIG.anomalies.minSpawnY(); y < maxY; y++)
        {
            pos.setY(y);
            if (world.isAir(pos))
            {
                /* If the size is even all ranges are 'closed'. Since an air block
                 * has been found a new range should be started.*/
                if (airRanges.size() % 2 == 0)
                {    
                    airRanges.add(y);
                }
                else if (y == maxY - 1)
                    airRanges.add(maxY - 1);                    
            }
            /* If the size is odd the last range is still open. It should be closed
             * since a non-air block has been found*/
            else if (airRanges.size() % 2 != 0)
                airRanges.add(y - 1);
        }
        //If the last range is still open, close it.
        if (airRanges.size() % 2 != 0)
            airRanges.add(maxY);
        if (airRanges.size() < 2) 
        {
            LOGGER.debug("Failed to spawn anomaly at {}, {} as there were no air gaps", d, e);
            return;
        }
        //Pick a random air filled gap
        int chosenAirRange = world.getRandom().nextInt((int) Math.ceil(airRanges.size() / 2.0F));
        //Pick a random y coordinate inside the gap
        int lowerBound = airRanges.getInt(chosenAirRange * 2);
        int upperBound = airRanges.getInt(chosenAirRange * 2 + 1);
        int y = upperBound != lowerBound 
                ? lowerBound + world.getRandom().nextInt(upperBound - lowerBound)
                : lowerBound;
        
        AnomalyEntity anomaly = new AnomalyEntity(world, polarity);
        anomaly.setPosition(d, y, e);
        world.spawnEntity(anomaly);
        LOGGER.debug("Spawned {} anomaly at {}, {}, {}", polarity.asString(), d, y, e);
    }
}
