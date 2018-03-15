package leviathan143.polar.common.capabilities;

import leviathan143.polar.common.Polar.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class CapabilityAttacher
{
	private static final ResourceLocation POLAR_PLAYER_DATA_KEY = new ResourceLocation(Constants.MODID, "player_data");
	
	@SubscribeEvent
	public static void attachCaps(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof EntityPlayer)
		{
			event.addCapability(POLAR_PLAYER_DATA_KEY, new CapabilityPlayerDataPolar.PlayerDataProvider());
		}
	}
}
