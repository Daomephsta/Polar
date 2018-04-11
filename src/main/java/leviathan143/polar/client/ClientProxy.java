package leviathan143.polar.client;

import leviathan143.polar.client.render.entities.RenderAnomaly;
import leviathan143.polar.common.CommonProxy;
import leviathan143.polar.common.entities.EntityAnomaly;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy 
{	
	@Override
	public String translate(String key, Object... formatArgs) 
	{
		return I18n.format(key, formatArgs);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityAnomaly.class, rm -> new RenderAnomaly(rm));
	}
}
