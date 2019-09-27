
package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.advancements.triggers.AbstractCriterion.AbstractHandler;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerAnomalyInteractionCriterion extends AbstractCriterion<PlayerAnomalyInteractionCriterion.Conditions, PlayerAnomalyInteractionCriterion.Handler>			
{
	public PlayerAnomalyInteractionCriterion()
	{
		super(new Identifier(Polar.MOD_ID, "player_anomaly_interaction"), tracker -> new Handler());
	}

	public void handle(ServerPlayerEntity player)
	{
		Handler handler = getHandler(player.getAdvancementManager());
		if (handler != null)
			handler.handle(player.getAdvancementManager());
	}

	@Override
	public PlayerAnomalyInteractionCriterion.Conditions conditionsFromJson(JsonObject json, JsonDeserializationContext context)
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
		private final PlayerAnomalyInteractionCriterion nestOwner;
		
		private Conditions(PlayerAnomalyInteractionCriterion nestOwner)
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
