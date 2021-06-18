
package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ObserveFallingBlockCriterion 
    extends AbstractCriterion<ObserveFallingBlockCriterion.Conditions, ObserveFallingBlockCriterion.Handler>
{
    public ObserveFallingBlockCriterion()
    {
        super(Polar.id("observe_falling_block"), tracker -> new Handler());
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
                container.grant(advancementManager);
        }
    }

    static class Conditions implements CriterionConditions
    {
        private final ObserveFallingBlockCriterion nestOwner;
        
        private Conditions(ObserveFallingBlockCriterion nestOwner)
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
