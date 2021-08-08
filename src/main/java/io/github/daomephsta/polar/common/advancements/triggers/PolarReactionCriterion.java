package io.github.daomephsta.polar.common.advancements.triggers;

import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class PolarReactionCriterion extends AbstractCriterion<PolarReactionCriterion.Conditions>
{
    public PolarReactionCriterion()
    {
        super(Polar.id("polar_reaction"));
    }

    public void handle(ServerPlayerEntity player, int reactionStrength)
    {
        PlayerAdvancementTracker advancements = player.getAdvancementTracker();
        for (ConditionsContainer<Conditions> container : getContainers(advancements))
        {
            if (container.getConditions().reactionStrength <= reactionStrength)
                container.grant(advancements);
        }
    }

    @Override
    public Conditions conditionsFromJson(JsonObject json, AdvancementEntityPredicateDeserializer deserializer)
    {
        return new Conditions(this, JsonHelper.getInt(json, "reaction_strength"));
    }

    record Conditions(PolarReactionCriterion owner, int reactionStrength) implements CriterionConditions
    {
        @Override
        public Identifier getId()
        {
            return owner.getId();
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer serializer)
        {
            JsonObject json = new JsonObject();
            json.addProperty("reaction_strength", reactionStrength);
            return json;
        }
    }
}
