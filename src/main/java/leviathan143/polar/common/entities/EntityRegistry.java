package leviathan143.polar.common.entities;

import leviathan143.polar.common.Polar;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Polar.MODID)
@EventBusSubscriber(modid = Polar.MODID)
public class EntityRegistry
{	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> e)
	{
		e.getRegistry().registerAll(
				EntityEntryBuilder.create()
					.entity(EntityAnomaly.class).id("anomaly", 0).name(Polar.MODID + ".anomaly")
					.tracker(128, Integer.MAX_VALUE, false).build());
		
	}
}
