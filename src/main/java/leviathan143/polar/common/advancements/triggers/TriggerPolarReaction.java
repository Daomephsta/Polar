package leviathan143.polar.common.advancements.triggers;

import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import leviathan143.polar.common.Polar;
import net.minecraft.advancements.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class TriggerPolarReaction implements ICriterionTrigger<TriggerPolarReaction.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Polar.MODID, "polar_reaction");
	private final Multimap<PlayerAdvancements, Listener<Instance>> listeners = MultimapBuilder.hashKeys().hashSetValues().build();

	public void trigger(EntityPlayerMP player, int reactionStrength)
	{
		Collection<Listener<Instance>> playerListeners = listeners.get(player.getAdvancements());
		/* Avoid CME by adding criterions that have passed to a list, then iterating over that list
		 * and granting them. In this case all criterions pass, so the passed list is initialised with
		 * the criterion list*/
		Collection<Listener<Instance>> passedCriteria = playerListeners.stream()
			.filter(listener -> listener.getCriterionInstance().test(reactionStrength))
			.collect(Collectors.toList());
		for (Listener<Instance> passedCriterion : passedCriteria)
		{
			passedCriterion.grantCriterion(player.getAdvancements());
		}
	}
	
	@Override
	public void addListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener)
	{
		listeners.put(playerAdvancements, listener);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancements, Listener<Instance> listener)
	{
		listeners.remove(playerAdvancements, listener);
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancements)
	{
		listeners.removeAll(playerAdvancements);
	}

	@Override
	public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
	{
		return new Instance(JsonUtils.getInt(json, "reaction_strength"));
	}
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	public class Instance implements ICriterionInstance
	{
		private final int reactionStrength;
		
		private Instance(int reactionStrength)
		{
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
		public ResourceLocation getId()
		{
			return ID;
		}
	}
}
