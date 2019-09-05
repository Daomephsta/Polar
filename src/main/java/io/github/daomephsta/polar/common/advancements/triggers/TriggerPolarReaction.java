/*TODO Advancements
 * package io.github.daomephsta.polar.common.advancements.triggers;
 * 
 * import com.google.gson.JsonDeserializationContext; import
 * com.google.gson.JsonObject;
 * 
 * import daomephsta.umbra.advancements.TriggerBase; import
 * io.github.daomephsta.polar.common.Polar; import
 * net.minecraft.advancements.ICriterionInstance; import
 * net.minecraft.entity.player.EntityPlayerMP; import
 * net.minecraft.util.JsonUtils; import net.minecraft.util.ResourceLocation;
 * 
 * public class TriggerPolarReaction extends
 * TriggerBase<TriggerPolarReaction.Instance> { private static final
 * ResourceLocation ID = new ResourceLocation(Polar.MODID, "polar_reaction");
 * 
 * public void trigger(EntityPlayerMP player, int reactionStrength) {
 * grantPassedCriteria(player, listener ->
 * listener.getCriterionInstance().test(reactionStrength)); }
 * 
 * @Override public Instance deserializeInstance(JsonObject json,
 * JsonDeserializationContext context) { return new
 * Instance(JsonUtils.getInt(json, "reaction_strength")); }
 * 
 * @Override public ResourceLocation getId() { return ID; }
 * 
 * public static class Instance implements ICriterionInstance { private final
 * int reactionStrength;
 * 
 * private Instance(int reactionStrength) { this.reactionStrength =
 * reactionStrength; }
 * 
 * public boolean test(int reactionStrength) { return this.reactionStrength <=
 * reactionStrength; }
 * 
 * public int getReactionStrength() { return reactionStrength; }
 * 
 * @Override public ResourceLocation getId() { return ID; } } }
 */