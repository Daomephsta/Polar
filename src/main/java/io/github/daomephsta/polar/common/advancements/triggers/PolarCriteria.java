
package io.github.daomephsta.polar.common.advancements.triggers;

import io.github.daomephsta.polar.common.Polar;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class PolarCriteria
{
    public static final NullaryCriterion PLAYER_ANOMALY_INTERACTION =
        CriterionRegistry.register(new NullaryCriterion(Polar.id("player_anomaly_interaction")));
    public static final PolarReactionCriterion POLAR_REACTION =
        CriterionRegistry.register(new PolarReactionCriterion());

    public static void initialise()
    {
        // Dummy method to force static init
    }
}
