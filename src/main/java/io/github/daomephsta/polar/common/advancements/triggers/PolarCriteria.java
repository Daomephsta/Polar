
package io.github.daomephsta.polar.common.advancements.triggers;

import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class PolarCriteria
{
    public static final PlayerAnomalyInteractionCriterion PLAYER_ANOMALY_INTERACTION = 
        CriterionRegistry.register(new PlayerAnomalyInteractionCriterion());
    public static final PolarReactionCriterion POLAR_REACTION = 
        CriterionRegistry.register(new PolarReactionCriterion());
    public static final ObserveFallingBlockCriterion OBSERVE_FALLING_BLOCK = 
        CriterionRegistry.register(new ObserveFallingBlockCriterion());
 
    public static void initialise() 
    {
        // Dummy method to force static init
    }
}
