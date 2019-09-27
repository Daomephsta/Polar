
package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.advancements.triggers.AbstractCriterion.AbstractHandler;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ObserveFallingBlockCriterion extends AbstractCriterion<ObserveFallingBlockCriterion.Conditions, ObserveFallingBlockCriterion.Handler>
{
	public ObserveFallingBlockCriterion()
	{
		super(new Identifier(Polar.MOD_ID, "observe_falling_block"), tracker -> new Handler());
	}


	public void handle(ServerPlayerEntity player)
	{
		Handler handler = getHandler(player.getAdvancementManager());
		if (handler != null)
			handler.handle(player.getAdvancementManager());
	}

	@Override
	public ObserveFallingBlockCriterion.Conditions conditionsFromJson(JsonObject json, JsonDeserializationContext context)
	{
		return new Conditions(this);
	}

	static class Handler extends AbstractHandler<Conditions> 
	{
		private void handle(PlayerAdvancementTracker advancementManager)
		{
			for (ConditionsContainer<Conditions> container : getContainers())
			{
				container.apply(advancementManager);
			}
		}
	}

	class Conditions implements CriterionConditions
	{
		private final ObserveFallingBlockCriterion nestOwner;
		
		private Conditions(ObserveFallingBlockCriterion nestOwner)
		{
			this.nestOwner = nestOwner;
		}

		@Override
		public Identifier getId()
		{
			return nestOwner.getId();
		}
	}
}
