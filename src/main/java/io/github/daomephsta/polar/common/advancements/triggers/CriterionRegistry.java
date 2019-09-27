
package io.github.daomephsta.polar.common.advancements.triggers;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.mixin.CriterionsAccess;
import net.minecraft.advancement.criterion.Criterions;

public class CriterionRegistry
{
	static
	{
		//Prevent mixin failure, see https://github.com/SpongePowered/Mixin/issues/342
		Polar.forceClassInit(Criterions.class);
	}
	public static final PlayerAnomalyInteractionCriterion PLAYER_ANOMALY_INTERACTION = CriterionsAccess
			.invokeRegister(new PlayerAnomalyInteractionCriterion());
	public static final PolarReactionCriterion POLAR_REACTION = CriterionsAccess
			.invokeRegister(new PolarReactionCriterion());
	public static final ObserveFallingBlockCriterion OBSERVE_FALLING_BLOCK = CriterionsAccess
			.invokeRegister(new ObserveFallingBlockCriterion());
 
	public static void initialise() 
	{
		// Dummy method to force static init
	}
}
