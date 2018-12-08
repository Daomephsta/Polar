package leviathan143.polar.common.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import leviathan143.polar.api.capabilities.ISyncableCapability;
import leviathan143.polar.common.Polar;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class CapabilityHandler
{
	private static final Map<Capability<? extends ISyncableCapability>, EnumFacing[]> SYNCABLE_CAPABILITIES = new HashMap<>();
	private static final ResourceLocation POLAR_PLAYER_DATA_KEY = new ResourceLocation(Polar.MODID, "player_data");
	
	static void registerForSyncing(Capability<? extends ISyncableCapability> capability, EnumFacing... sides)
	{
		SYNCABLE_CAPABILITIES.put(capability, sides);
	}
	
	@SubscribeEvent
	public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(POLAR_PLAYER_DATA_KEY, new CapabilityPlayerDataPolar.PlayerDataProvider((EntityPlayer) event.getObject()));
		}
	}
	
	@SubscribeEvent
	public static void syncCapabilities(PlayerLoggedInEvent event)
	{
		for (Entry<Capability<? extends ISyncableCapability>, EnumFacing[]> entry : SYNCABLE_CAPABILITIES.entrySet())
		{
			for (EnumFacing side : entry.getValue())
			{
				ISyncableCapability capability = event.player.getCapability(entry.getKey(), side);
				if (capability != null)
					capability.sync();
			}
		}
	}
}
