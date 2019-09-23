package io.github.daomephsta.polar.common.entities;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.entities.anomalies.EntityAnomaly;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntityRegistry
{	
	public static final EntityType<EntityAnomaly> ANOMALY = register("anomaly",
			FabricEntityTypeBuilder.<EntityAnomaly>create(EntityCategory.MISC, (type, world) -> new EntityAnomaly(world))
				.size(EntityDimensions.fixed(1.1F, 1.1F))
				.trackable(128, Integer.MAX_VALUE, false)
				.build());
	
	private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type)
	{
		return Registry.register(Registry.ENTITY_TYPE, new Identifier(Polar.MOD_ID, name), type);
	}
	
	public static void initialise()
	{
		//Dummy method to force static init
	}
}
