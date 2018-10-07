package leviathan143.polar.common.entities.anomalies;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Iterables;

import daomephsta.umbra.TimeOfDay;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.config.PolarConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
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
	private static final Logger logger = LogManager.getLogger(AnomalySpawningHandler.class.getSimpleName());
	
	@SubscribeEvent
	public static void onServerTick(ServerTickEvent e)
	{
		if (e.phase == Phase.END) return;
		for (int loadedDimID : DimensionManager.getIDs())
		{
			if (PolarConfig.anomalies.isDimensionBlacklisted(loadedDimID))
				continue;
			
			WorldServer world = DimensionManager.getWorld(loadedDimID);
			if(world.getWorldTime() % 24000 == TimeOfDay.SUNRISE.getTicks() + 1)
				spawnAnomalies(world, Polarity.BLUE);
			else if(world.getWorldTime() % 24000 == TimeOfDay.SUNSET.getTicks())
				spawnAnomalies(world, Polarity.RED);
		}
	}
	
	private static void spawnAnomalies(WorldServer world, Polarity polarity)
	{
		List<EntityAnomaly> anomalies = getAnomaliesOfPolarityInLoadedChunks(world, polarity);
		//The cap is allocated half to red anomalies, and half to blue
		//Don't spawn anomalies if the cap has been reached
		if (anomalies.size() == PolarConfig.anomalies.maxAnomalyCount / 2.0F) 
			return;
		//If there are more anomalies than the cap, cull them.
		else if (anomalies.size() > PolarConfig.anomalies.maxAnomalyCount / 2.0F)
		{
			cullAnomalies(anomalies);
			return;
		}
			
		Collection<Chunk> loadedChunks = world.getChunkProvider().getLoadedChunks();
		//The percentage of loaded chunks to spawn an anomaly in
		float spawnChunkPercentage = PolarConfig.anomalies.minChunkPercentage + world.rand.nextFloat() 
			* (PolarConfig.anomalies.maxChunkPercentage - PolarConfig.anomalies.minChunkPercentage);
		//The number of loaded chunks to spawn an anomaly in
		int spawnChunkCount = (int) Math.min(Math.ceil(spawnChunkPercentage * loadedChunks.size()),
			PolarConfig.anomalies.maxAnomalyCount - anomalies.size());
		logger.debug("{} chunks loaded, {} anomalies in loaded chunks (Max {}). Attempting to spawn {} anomalies in {} percent of "
			+ "loaded chunks, actually spawning {} anomalies",
			loadedChunks.size(), anomalies.size(), PolarConfig.anomalies.maxAnomalyCount, polarity.getName(), spawnChunkPercentage * 10.0F, 
			spawnChunkCount);
		for (Chunk randomChunk : pickRandomChunks(world.rand, loadedChunks, spawnChunkCount))
		{
			spawnAnomalyInChunk(randomChunk, polarity);
		}
	}
	
	private static List<EntityAnomaly> getAnomaliesOfPolarityInLoadedChunks(WorldServer worldServer, Polarity polarity)
	{
		List<EntityAnomaly> anomalies = new ArrayList<>(); 
		for(Chunk loadedChunk : worldServer.getChunkProvider().getLoadedChunks())
		{
			for(ClassInheritanceMultiMap<Entity> entityList : loadedChunk.getEntityLists())
			{
				for (EntityAnomaly anomaly : entityList.getByClass(EntityAnomaly.class))
				{
					if (anomaly.getPolarity() == polarity) anomalies.add(anomaly);
				}
			}
		}
		return anomalies;
	}
	
	private static void cullAnomalies(List<EntityAnomaly> anomalies)
	{
		int cullCount = anomalies.size() - PolarConfig.anomalies.maxAnomalyCount;
		for(int c = 0; c < cullCount; c++)
		{
			anomalies.get(c).setDead();
		}
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
	
	private static void spawnAnomalyInChunk(Chunk chunk, Polarity polarity)
	{
		World world = chunk.getWorld();
		int x = chunk.x * 16 + world.rand.nextInt(16);
		int z = chunk.z * 16 + world.rand.nextInt(16);
		
		//Find all the gaps along the y axis with air in them at the x z coordinates 
		IntList airRanges = new IntArrayList(6);
		MutableBlockPos pos = new MutableBlockPos(x, 0, z);
		int maxY = Math.min(world.getActualHeight(), PolarConfig.anomalies.maxSpawnY);
		for(int h = PolarConfig.anomalies.minSpawnY; h < maxY; h++)
		{
			pos.setY(h);
			if (world.isAirBlock(pos))
			{
				/* If the size is even all ranges are 'closed'. Since an air block
				 * has been found a new range should be started.*/
				if (airRanges.size() % 2 == 0)
				{	
					airRanges.add(h);
				}
				else if (h == maxY - 1)
					airRanges.add(maxY - 1);					
			}
			/* If the size is odd the last range is still open. It should be closed
			 * since a non-air block has been found*/
			else if (airRanges.size() % 2 != 0)
				airRanges.add(h - 1);
		}
		//Pick a random air filled gap
		int chosenAirRange = world.rand.nextInt(airRanges.size() / 2);
		//Pick a random y coordinate inside the gap
		int lowerBound = airRanges.getInt(chosenAirRange * 2);
		int upperBound = airRanges.getInt(chosenAirRange * 2 + 1);
		int y = upperBound != lowerBound 
				? lowerBound + world.rand.nextInt(upperBound - lowerBound)
				: lowerBound;
		
		EntityAnomaly anomaly = new EntityAnomaly(world, polarity);
		anomaly.setPosition(x, y, z);
		world.spawnEntity(anomaly);
		logger.debug("Spawned {} anomaly at {}, {}, {}", polarity.getName(), x, y, z);
	}
}
