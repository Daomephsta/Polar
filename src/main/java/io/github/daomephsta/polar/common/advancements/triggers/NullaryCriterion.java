package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonObject;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NullaryCriterion extends AbstractCriterion<NullaryCriterion.Conditions>
{
    public NullaryCriterion(Identifier id)
    {
        super(id);
    }

    public void handle(ServerPlayerEntity player)
    {
        PlayerAdvancementTracker advancements = player.getAdvancementTracker();
        for (var container : getContainers(advancements))
            container.grant(advancements);
    }

    @Override
    public Conditions conditionsFromJson(JsonObject json, AdvancementEntityPredicateDeserializer deserializer)
    {
        return new Conditions(this);
    }

    record Conditions(NullaryCriterion owner) implements CriterionConditions
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
