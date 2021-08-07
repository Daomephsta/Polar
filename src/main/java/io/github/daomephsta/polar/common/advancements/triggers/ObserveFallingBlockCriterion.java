package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ObserveFallingBlockCriterion extends AbstractCriterion<ObserveFallingBlockCriterion.Conditions>
{
    public ObserveFallingBlockCriterion()
    {
        super(Polar.id("observe_falling_block"));
    }

    public void handle(ServerPlayerEntity player)
    {
        PlayerAdvancementTracker advancements = player.getAdvancementTracker();
        for (ConditionsContainer<Conditions> container : getHandler(advancements))
            container.grant(advancements);
    }

    @Override
    public Conditions conditionsFromJson(JsonObject json, AdvancementEntityPredicateDeserializer deserializer)
    {
        return new Conditions(this);
    }

    record Conditions(ObserveFallingBlockCriterion owner) implements CriterionConditions
    {
        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer advancementEntityPredicateSerializer)
        {
            return new JsonObject();
        }

        @Override
        public Identifier getId()
        {
            return owner.getId();
        }
    }
}
