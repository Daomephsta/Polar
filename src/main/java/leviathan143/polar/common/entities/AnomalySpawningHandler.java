package leviathan143.polar.common.entities;

import java.util.*;

import com.google.common.collect.Iterables;

import leviathan143.polar.common.Polar;
import leviathan143.polar.common.config.PolarConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class AnomalySpawningHandler
{
	
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent e)
	{
		if (e.phase == Phase.END) return;
		for (int loadedDimID : DimensionManager.getIDs())
		{
			if (PolarConfig.anomalies.isDimensionBlacklisted(loadedDimID))
				continue;
			
			WorldServer world = DimensionManager.getWorld(loadedDimID);
			if(world.getWorldTime() != PolarConfig.anomalies.spawnTime) continue; 
			
			Collection<EntityAnomaly> anomalies = getAnomaliesInLoadedChunks(world);
			//Don't spawn anomalies if the cap has been reached
			if (anomalies.size() == PolarConfig.anomalies.maxAnomalyCount) 
				continue;
			//If there are more anomalies than the cap, cull them.
			else if (anomalies.size() > PolarConfig.anomalies.maxAnomalyCount)
			{
				cullAnomalies(anomalies);
				continue;
			}
				
			Collection<Chunk> loadedChunks = world.getChunkProvider().getLoadedChunks();
			//The percentage of loaded chunks to spawn an anomaly in
			float genPercentage = PolarConfig.anomalies.minGenPercentage + world.rand.nextFloat() * (PolarConfig.anomalies.maxGenPercentage - PolarConfig.anomalies.minGenPercentage);
			//The number of loaded chunks to spawn an anomaly in
			int spawnChunkCount = (int) Math.min(Math.ceil(genPercentage * loadedChunks.size()), PolarConfig.anomalies.maxAnomalyCount - anomalies.size());
			for (Chunk randomChunk : pickRandomChunks(world.rand, loadedChunks, spawnChunkCount))
			{
				spawnAnomalyInChunk(world, randomChunk);
			}
		}
	}
	
	private static Collection<EntityAnomaly> getAnomaliesInLoadedChunks(WorldServer worldServer)
	{
		Collection<EntityAnomaly> anomalies = new ArrayList<>(); 
		for(Chunk loadedChunk : worldServer.getChunkProvider().getLoadedChunks())
		{
			for(ClassInheritanceMultiMap<Entity> entityList : loadedChunk.getEntityLists())
			{
				Iterables.addAll(anomalies, entityList.getByClass(EntityAnomaly.class));
			}
		}
		return anomalies;
	}
	
	private static void cullAnomalies(Collection<EntityAnomaly> anomalies)
	{
		int cullCount = anomalies.size() - PolarConfig.anomalies.maxAnomalyCount;
	}
	
	private static Iterable<Chunk> pickRandomChunks(Random rand, Collection<Chunk> loadedChunks, int pickSize)
	{
		Set<Chunk> randomChunks = new HashSet<>();
		for (int p = 0; p < pickSize; p++)
		{
			randomChunks.add(Iterables.get(loadedChunks, rand.nextInt(loadedChunks.size())));
		}
		return randomChunks;
	}
	
	private static void spawnAnomalyInChunk(World world, Chunk chunk)
	{
		
	}
}
