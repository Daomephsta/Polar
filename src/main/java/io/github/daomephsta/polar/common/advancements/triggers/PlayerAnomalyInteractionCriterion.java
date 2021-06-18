
package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerAnomalyInteractionCriterion 
    extends AbstractCriterion<PlayerAnomalyInteractionCriterion.Conditions, PlayerAnomalyInteractionCriterion.Handler>            
{
    public PlayerAnomalyInteractionCriterion()
    {
        super(Polar.id("player_anomaly_interaction"), tracker -> new Handler());
    }

    public void handle(ServerPlayerEntity player)
    {
        Handler handler = getHandler(player.getAdvancementTracker());
        if (handler != null)
            handler.handle(player.getAdvancementTracker());
    }

    @Override
    public Conditions conditionsFromJson(JsonObject json, AdvancementEntityPredicateDeserializer deserializer)
    {
        return new Conditions(this);
    }

    static class Handler extends AbstractCriterion.AbstractHandler<Conditions> 
    {
        private void handle(PlayerAdvancementTracker advancementManager)
        {
            for (ConditionsContainer<Conditions> container : getContainers())
            {
                container.grant(advancementManager);
            }
        }
    }

    static class Conditions implements CriterionConditions
    {
        private final PlayerAnomalyInteractionCriterion nestOwner;
        
        private Conditions(PlayerAnomalyInteractionCriterion nestOwner)
        {
            this.nestOwner = nestOwner;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer advancementEntityPredicateSerializer)
        {
            return new JsonObject();
        }

        @Override
        public Identifier getId()
        {
            return nestOwner.getId();
        }
    }
}
