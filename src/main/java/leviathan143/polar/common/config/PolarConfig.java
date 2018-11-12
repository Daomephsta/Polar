package leviathan143.polar.common.config;

import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import leviathan143.polar.common.Polar;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Polar.MODID, category = "")
public class PolarConfig
{
	public static final Anomalies anomalies = new Anomalies();
	public static final Charge charge = new Charge();
	
	public static class Anomalies
	{
		@Name("Minimum Spawn Percentage")
		@Comment("The minimum percentage of chunks to spawn anomalies in")
		public float minChunkPercentage = 0.01F;
		@Name("Maximum Spawn Percentage")
		@Comment("The maximum percentage of chunks to spawn anomalies in")
		public float maxChunkPercentage = 0.02F;
		@Name("Minimum Spawn Y")
		@Comment("The minimum height anomalies can spawn at")
		public int minSpawnY = 0;
		@Name("Maximum Spawn Y")
		@Comment("The maximum height anomalies can spawn at")
		public int maxSpawnY = 72;
		@Name("Minimum Lifetime")
		@Comment("The minimum number of days an anomaly will stay open for, if left untapped")
		public int minLifetime = 3;
		@Name("Maximum Lifetime")
		@Comment("The maximum number of days an anomaly will stay open for, if left untapped")
		public int maxLifetime = 8;
		@Name("Maximum Anomaly Count")
		@Comment("The maximum number of anomalies that can be loaded at once")
		public int maxAnomalyCount = 100;
		@Name("Dimension Blacklist")
		@Comment("Anomalies will not spawn in any dimension that has its integer ID in this list")
		public int[] __dimBlackList = {};
		private IntSet dimBlackList;
		
		public boolean isDimensionBlacklisted(int dimID)
		{
			return dimBlackList.contains(dimID);
		}
	}
	
	public static class Charge
	{
		@RequiresMcRestart
		@Name("Gravitic Stabiliser Maximum Charge")
		@Comment("The maximum charge a Gravitic Stabiliser can hold")
		public int graviticStabiliserMaxCharge = 128;
		@Name("Gravitic Stabiliser Activation Cost")
		@Comment("How much charge it costs to stabilise a block")
		public int graviticStabiliserActivationCost = 2;
	}
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent e)
	{
		if (e.getModID().equals(Polar.MODID))
		{
			ConfigManager.sync(Polar.MODID, Type.INSTANCE);
			finishLoading();
		}
	}

	private static void finishLoading()
	{
		anomalies.dimBlackList = new IntArraySet(anomalies.__dimBlackList);	
	}
	
	public static void preInit()
	{
		finishLoading();
	}
	
	public static void init(){}
	
	public static void postInit() {}
}
