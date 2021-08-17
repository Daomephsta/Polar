package io.github.daomephsta.polar.common.research;

import net.minecraft.entity.player.PlayerEntity;

public abstract class ResearchObjective
{
    private final Runnable onCompletion;
    protected final PlayerEntity player;

    public ResearchObjective(PlayerEntity player, Runnable onCompletion)
    {
        this.player = player;
        this.onCompletion = onCompletion;
    }

    public abstract void startTracking();

    protected final void complete()
    {
        onCompletion.run();
    }

    protected void stopTracking() {}

    public interface Factory
    {
        public ResearchObjective create(PlayerEntity player, Runnable onCompletion);
    }
}
