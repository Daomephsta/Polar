package io.github.daomephsta.polar.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.Criterions;

@Mixin(Criterions.class)
public interface CriterionsAccess
{
	@Invoker
	public static <T extends Criterion<?>> T invokeRegister(T criterion)
	{
		throw new IllegalStateException("Dummy method body should not be invoked. Critical mixin failure.");
	}
}
