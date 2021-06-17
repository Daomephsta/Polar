package io.github.daomephsta.polar.common.entities;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalyEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class EntityRegistry
{    
    public static final EntityType<AnomalyEntity> ANOMALY = register("anomaly",
            FabricEntityTypeBuilder.<AnomalyEntity>create(SpawnGroup.MISC, AnomalyEntity::new)
                .dimensions(EntityDimensions.fixed(1.1F, 1.1F))
                .trackRangeBlocks(128)
                .trackedUpdateRate(Integer.MAX_VALUE)
                .forceTrackedVelocityUpdates(false)
                .build());
    
    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type)
    {
        return Registry.register(Registry.ENTITY_TYPE, Polar.id(name), type);
    }
    
    public static void initialise()
    {
        //Dummy method to force static init
    }
}
