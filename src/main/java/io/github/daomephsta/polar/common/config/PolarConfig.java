package io.github.daomephsta.polar.common.config;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.util.Identifier;

//TODO Config
public class PolarConfig
{
	public static final Anomalies anomalies = new Anomalies();
	public static final Charge charge = new Charge();
	
	public static class Anomalies
	{
		//@Name("Minimum Spawn Percentage")
		//@Comment("The minimum percentage of chunks to spawn anomalies in")
		public float minChunkPercentage = 0.01F;
		//@Name("Maximum Spawn Percentage")
		//@Comment("The maximum percentage of chunks to spawn anomalies in")
		public float maxChunkPercentage = 0.02F;
		//@Name("Minimum Spawn Y")
		//@Comment("The minimum height anomalies can spawn at")
		public int minSpawnY = 0;
		//@Name("Maximum Spawn Y")
		//@Comment("The maximum height anomalies can spawn at")
		public int maxSpawnY = 72;
		//@Name("Minimum Lifetime")
		//@Comment("The minimum number of days an anomaly will stay open for, if left untapped")
		public int minLifetime = 3;
		//@Name("Maximum Lifetime")
		//@Comment("The maximum number of days an anomaly will stay open for, if left untapped")
		public int maxLifetime = 8;
		//@Name("Maximum Anomaly Count")
		//@Comment("The maximum number of anomalies that can be loaded at once")
		public int maxAnomalyCount = 100;
		//@Name("Dimension Blacklist")
		//@Comment("Anomalies will not spawn in any dimension that has its integer ID in this list")
		public int[] __dimBlackList = {};
		private IntSet dimBlackList;
		
		public boolean isDimensionBlacklisted(Identifier identifier)
		{
			return dimBlackList.contains(identifier);
		}
	}
	
	public static class Charge
	{
		//@RequiresMcRestart
		//@Name("Gravitic Stabiliser Maximum Charge")
		//@Comment("The maximum charge a Gravitic Stabiliser can hold")
		public int graviticStabiliserMaxCharge = 128;
		//@Name("Gravitic Stabiliser Activation Cost")
		//@Comment("How much charge it costs to stabilise a block")
		public int graviticStabiliserActivationCost = 2;
		//@RequiresMcRestart
		//@Name("Percussive Disintegrator Maximum Charge")
		//@Comment("The maximum charge a Percussive Disintegrator can hold")
		public int percussiveDisintegratorMaxCharge = 256;
		//@Name("Percussive Disintegrator Activation Cost")
		//@Comment("How much charge it costs to destroy a block")
		public int percussiveDisintegratorActivationCost = 2;
	}
	
	/*
	 * @SubscribeEvent public static void onConfigChanged(ConfigChangedEvent e)
	 * { if (e.getModID().equals(Polar.MODID)) { ConfigManager.sync(Polar.MODID,
	 * Type.INSTANCE); finishLoading(); } }
	 */
}
