package leviathan143.polar.client;

import leviathan143.polar.common.CommonProxy;
import leviathan143.polar.common.Polar;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Polar.MODID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy 
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e)
	{
		
	}
	
	@Override
	public String translate(String key, Object... formatArgs) 
	{
		return I18n.format(key, formatArgs);
	}
}
