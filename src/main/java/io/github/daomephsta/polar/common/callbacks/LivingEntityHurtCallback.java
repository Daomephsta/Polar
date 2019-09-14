package io.github.daomephsta.polar.common.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface LivingEntityHurtCallback
{
	public static final Event<LivingEntityHurtCallback> EVENT = EventFactory.createArrayBacked(LivingEntityHurtCallback.class, 
			(callbacks) -> (living, source, amount) ->
			{
				for (LivingEntityHurtCallback callback : callbacks)
				{
					if (!callback.onLivingHurt(living, source, amount))
						return false;
				}
				return true;
			});
	
	/**
	 * Called when damage is done to a living entity.
	 * @param living the hurt entity
	 * @param source the source of the damage
	 * @param amount the amount of damage in half hearts
	 * @return false to cancel the damage
	 */
	public boolean onLivingHurt(LivingEntity living, DamageSource source, float amount);
}
