
package io.github.daomephsta.polar.common.advancements.triggers;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.advancement.criterion.Criteria;

public class PolarCriteria
{
    public static final NullaryCriterion PLAYER_ANOMALY_INTERACTION =
        Criteria.register(new NullaryCriterion(Polar.id("player_anomaly_interaction")));
    public static final PolarReactionCriterion POLAR_REACTION =
        Criteria.register(new PolarReactionCriterion());

    public static void initialise()
    {
        // Dummy method to force static init
    }
}
