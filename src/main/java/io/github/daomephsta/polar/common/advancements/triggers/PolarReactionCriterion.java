
package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.advancements.triggers.AbstractCriterion.AbstractHandler;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;


public class PolarReactionCriterion extends AbstractCriterion<PolarReactionCriterion.Conditions, PolarReactionCriterion.Handler>
{
	public PolarReactionCriterion()
	{
		super(new Identifier(Polar.MOD_ID, "polar_reaction"), tracker -> new Handler());
	}

	public void handle(ServerPlayerEntity player, int reactionStrength)
	{
		Handler handler = getHandler(player.getAdvancementManager());
		if (handler != null)
			handler.handle(player.getAdvancementManager(), reactionStrength);
	}

	@Override
	public Conditions conditionsFromJson(JsonObject json, JsonDeserializationContext context)
	{
		return new Conditions(this, JsonHelper.getInt(json, "reaction_strength"));
	}

	static class Handler extends AbstractHandler<Conditions> 
	{
		private void handle(PlayerAdvancementTracker advancementManager, int reactionStrength)
		{
			for (ConditionsContainer<Conditions> container : getContainers())
			{
				if (container.getConditions().test(reactionStrength))
					container.apply(advancementManager);
			}
		}
	}

	class Conditions implements CriterionConditions
	{
		private final PolarReactionCriterion nestOwner;
		private final int reactionStrength;

		private Conditions(PolarReactionCriterion nestOwner, int reactionStrength)
		{
			this.nestOwner = nestOwner;
			this.reactionStrength = reactionStrength;
		}

		public boolean test(int reactionStrength)
		{
			return this.reactionStrength <= reactionStrength;
		}

		public int getReactionStrength()
		{
			return reactionStrength;
		}

		@Override
		public Identifier getId()
		{
			return nestOwner.getId();
		}
	}
}
