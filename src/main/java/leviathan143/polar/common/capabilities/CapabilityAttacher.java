package leviathan143.polar.common.capabilities;

import leviathan143.polar.common.Polar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class CapabilityAttacher
{
	private static final ResourceLocation POLAR_PLAYER_DATA_KEY = new ResourceLocation(Polar.MODID, "player_data");
	private static final ResourceLocation WORLD_TASK_SCHEDULER_KEY = new ResourceLocation(Polar.MODID, "world_task_scheduler");
	
	@SubscribeEvent
	public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(POLAR_PLAYER_DATA_KEY, new CapabilityPlayerDataPolar.PlayerDataProvider());
		}
	}
	
	@SubscribeEvent
	public static void attachWorldCaps(AttachCapabilitiesEvent<World> event)
	{
		event.addCapability(WORLD_TASK_SCHEDULER_KEY, new WorldTaskScheduler.WorldTaskSchedulerProvider(event.getObject()));
	}
}
