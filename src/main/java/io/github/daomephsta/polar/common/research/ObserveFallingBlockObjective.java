package io.github.daomephsta.polar.common.research;

import java.util.HashSet;
import java.util.Set;

import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class ObserveFallingBlockObjective extends ResearchObjective
{
    private static final InstanceStorage<ObserveFallingBlockObjective> INSTANCES = new InstanceStorage<>();
    static
    {
        EntityTrackingEvents.START_TRACKING.register(ObserveFallingBlockObjective::onStartTracking);
        EntityTrackingEvents.STOP_TRACKING.register(ObserveFallingBlockObjective::onStopTracking);
    }
    private final Set<FallingBlockEntity> fallingBlocks = new HashSet<>();
    private final int distance;

    public ObserveFallingBlockObjective(PlayerEntity player, Runnable onCompletion, int distance)
    {
        super(player, onCompletion);
        this.distance = distance;
    }

    private static void onStartTracking(Entity entity, ServerPlayerEntity player)
    {
        if (entity instanceof FallingBlockEntity fallingBlock)
        {
            for (ObserveFallingBlockObjective objective : INSTANCES.get(player))
                objective.fallingBlocks.add(fallingBlock);
        }
    }

    private static void onStopTracking(Entity entity, ServerPlayerEntity player)
    {
        if (entity instanceof FallingBlockEntity fallingBlock)
        {
            for (var objective : INSTANCES.get(player))
            {
                if (objective.fallingBlocks.remove(fallingBlock) &&
                    fallingBlock.isOnGround() && objective.isHeightAdequate(fallingBlock))
                {
                    objective.complete();
                }
            }
        }
    }

    private boolean isHeightAdequate(FallingBlockEntity fallingBlock)
    {
        return !fallingBlock.getFallingBlockPos().isWithinDistance(fallingBlock.getBlockPos(), distance);
    }

    @Override
    public void startTracking()
    {
        INSTANCES.put(player, this);
    }

    @Override
    protected void stopTracking()
    {
        INSTANCES.remove(player, this);
    }
}
