package leviathan143.polar.common.advancements.triggers;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import leviathan143.polar.common.Polar;
import net.minecraft.advancements.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class TriggerPlayerAnomalyInteraction implements ICriterionTrigger<TriggerPlayerAnomalyInteraction.Instance>
{
	private static final ResourceLocation ID = new ResourceLocation(Polar.MODID, "player_anomaly_interaction");
	private final Multimap<PlayerAdvancements, Listener<Instance>> listeners = MultimapBuilder.hashKeys().hashSetValues().build();

	public void trigger(EntityPlayerMP player)
	{
		//No special criteria per listener
		Collection<Listener<Instance>> playerListeners = listeners.get(player.getAdvancements());
		//Listeners are removed when their criterion is granted, so check before
		boolean hasListeners = !playerListeners.isEmpty();
		/* Avoid CME by adding criterions that have passed to a list, then iterating over that list
		 * and granting them. In this case all criterions pass, so the passed list is initialised with
		 * the criterion list*/
		Collection<Listener<Instance>> passedCriteria = new ArrayList<>(playerListeners);
		for (Listener<Instance> passedCriterion : passedCriteria)
		{
			passedCriterion.grantCriterion(player.getAdvancements());
		}
		if (hasListeners)
			player.sendMessage(new TextComponentTranslation(Polar.MODID + ".message.research_journal_prompt"));
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
		return new Instance();
	}
	
	@Override
	public ResourceLocation getId()
	{
		return ID;
	}

	public class Instance implements ICriterionInstance
	{
		@Override
		public ResourceLocation getId()
		{
			return ID;
		}
	}
}
