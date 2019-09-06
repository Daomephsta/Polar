package io.github.daomephsta.polar.common.entities.anomalies;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.config.PolarConfig;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;

public class AnomalySpawningHandler
{
	private static final Logger logger = LogManager
			.getLogger(AnomalySpawningHandler.class);

	public static void registerEventCallbacks()
	{
		ServerTickCallback.EVENT.register(AnomalySpawningHandler::onServerTick);
	}

	private static void onServerTick(MinecraftServer server)
	{
		for (ServerWorld world : server.getWorlds())
		{
			if (PolarConfig.POLAR_CONFIG.anomalies.isDimensionBlacklisted(Registry.DIMENSION.getId(world.getDimension().getType())))
				continue;
			if (world.getTimeOfDay() % 24000 == TimeOfDay.SUNRISE.getTicks()
					+ 1)
				spawnAnomalies(world, Polarity.BLUE);
			else if (world.getTimeOfDay() % 24000 == TimeOfDay.SUNSET
					.getTicks())
				spawnAnomalies(world, Polarity.RED);
		}
	}

	private static void spawnAnomalies(ServerWorld world, Polarity polarity)
	{
		for (PlayerEntity player : world.getPlayers())
		{
			
		}
	}
}
