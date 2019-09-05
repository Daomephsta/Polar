/*TODO Advancements
 * package io.github.daomephsta.polar.common.advancements.triggers;
 * 
 * import static com.google.common.base.Predicates.and;
 * 
 * import java.lang.reflect.Field;
 * 
 * import com.google.common.base.Predicate;
 * 
 * import daomephsta.umbra.advancements.TriggerBase; import
 * daomephsta.umbra.reflection.SRGReflectionHelper; import
 * net.minecraft.advancement.criterion.Criterion; import
 * net.minecraft.advancement.criterion.CriterionConditions; import
 * net.minecraft.advancements.Advancement; import
 * net.minecraft.advancements.ICriterionInstance; import
 * net.minecraft.entity.player.EntityPlayerMP;
 * 
 * public abstract class TriggerResearchBase<T extends CriterionConditions>
 * implements Criterion<T> { private static final Field Listener_advancement =
 * SRGReflectionHelper.findField(Listener.class, "field_192161_b");
 * 
 * @Override protected void grantPassedCriteria(EntityPlayerMP player,
 * Predicate<Listener<T>> conditions) { Predicate<Listener<T>> hasParent = l ->
 * player.getAdvancements().getProgress(getAdvancement(l).getParent()).isDone();
 * super.grantPassedCriteria(player, and(hasParent, conditions)); }
 * 
 * private Advancement getAdvancement(Listener<T> listener) { try { return
 * (Advancement) Listener_advancement.get(listener); } catch
 * (IllegalArgumentException | IllegalAccessException e) { throw new
 * RuntimeException("Could not access listener advancement", e); } } }
 */