package io.github.daomephsta.polar.common.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.Identifier;

public class PolarConfig
{
	//@Setting(ignore = true)
	public static final PolarConfig POLAR_CONFIG = new PolarConfig();
	//@Setting(ignore = true)
	private static final Logger LOGGER = LogManager.getLogger();
	//@Setting(ignore = true)
	private static final Path PATH = Paths.get("config/polar.json5");
	private Anomalies anomalies = new Anomalies();
	private Charge charge = new Charge();
	
	public Anomalies anomalies()
	{
		return anomalies;
	}
	
	public Charge charge()
	{
		return charge;
	}
	
	public static class Anomalies
	{
		//@Constrain.Min(0)
		//@Comment("The minimum radius anomalies ")
		private int minRadius = 32;
		//@Constrain.Min(0)
		//@Comment("The maximum percentage of chunks to spawn anomalies in")
		private int maxRadius = 64;
		//@Constrain.Min(0)
		//@Comment("The minimum height anomalies can spawn at")
		private int minSpawnY = 0;
		//@Constrain.Min(0)
		//@Comment("The maximum height anomalies can spawn at")
		private int maxSpawnY = 72;
		//@Constrain.Min(1)
		//@Comment("The minimum number of days an anomaly will stay open for, if left untapped")
		private int minLifetime = 3;
		//@Constrain.Min(1)
		//@Comment("The maximum number of days an anomaly will stay open for, if left untapped")
		private int maxLifetime = 8;
		//@Constrain.Min(1)
		//@Comment("The maximum number of anomalies that can be loaded at once")
		private int maxAnomalyCount = 100;
		//@Comment("Anomalies will not spawn in any dimension that has its identifier in this list")
		private Collection<Identifier> dimBlackList = new HashSet<>();
		
		public int minRadius()
		{
			return minRadius;
		}
		
		//@Listener("minRadius")
		private void minRadiusValidator(int oldValue, int newValue)
		{
			if (minRadius >= maxRadius)
			{
				this.minRadius = oldValue;
				LOGGER.warn("anomalies.minRadius must be less than anomalies.maxRadius. Value reset to {}", oldValue);
			}
		}

		public int maxRadius()
		{
			return maxRadius;
		}

		public int minSpawnY()
		{
			return minSpawnY;
		}

		public int maxSpawnY()
		{
			return maxSpawnY;
		}

		public int minLifetime()
		{
			return minLifetime;
		}

		public int maxLifetime()
		{
			return maxLifetime;
		}

		public int maxAnomalyCount()
		{
			return maxAnomalyCount;
		}
		
		public boolean isDimensionBlacklisted(Identifier identifier)
		{
			return dimBlackList.contains(identifier);
		}
	}
	
	public static class Charge
	{
		//@Constrain.Min(1)
		//@Comment("The maximum charge a Gravitic Stabiliser can hold")
		private int fallingBlockStabiliserMaxCharge = 128;
		//@Constrain.Min(0)
		//@Comment("How much charge it costs to stabilise a block")
		private int fallingBlockStabiliserActivationCost = 2;
		
		//@Constrain.Min(1)
		//@Comment("The maximum charge a Percussive Disintegrator can hold")
		private int fallingBlockDestroyerMaxCharge = 256;
		//@Constrain.Min(0)
		//@Comment("How much charge it costs to destroy a block")
		private int fallingBlockDestroyerActivationCost = 2;
		
		public int fallingBlockStabiliserMaxCharge()
		{
			return fallingBlockStabiliserMaxCharge;
		}
		
		public int fallingBlockStabiliserActivationCost()
		{
			return fallingBlockStabiliserActivationCost;
		}
		
		public int fallingBlockDestroyerMaxCharge()
		{
			return fallingBlockDestroyerMaxCharge;
		}
		
		public int fallingBlockDestroyerActivationCost()
		{
			return fallingBlockDestroyerActivationCost;
		}
	}
	
	public static void initialise()
	{
		/*try
		{
			ConfigNode root = new ConfigNode();
			AnnotatedSettings.applyToNode(root, POLAR_CONFIG);
			if (Files.exists(PATH))
				new JanksonSettings().deserialize(root, Files.newInputStream(PATH));
			else
				new JanksonSettings().serialize(root, Files.newOutputStream(PATH), false);
		} 
		catch (IOException | FiberException e)
		{
			e.printStackTrace();
		}*/
	}
}
