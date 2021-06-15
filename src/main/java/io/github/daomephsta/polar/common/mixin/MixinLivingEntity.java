package io.github.daomephsta.polar.common.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.daomephsta.polar.common.callbacks.LivingEntityHurtCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

@Mixin(LivingEntity.class)
public class MixinLivingEntity
{
    @Inject(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;despawnCounter:I"), cancellable = true)
    public void polar_damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info)
    {
        if (!LivingEntityHurtCallback.EVENT.invoker().onLivingHurt((LivingEntity) (Object) this, source, amount))
            info.setReturnValue(false);
    }
}
