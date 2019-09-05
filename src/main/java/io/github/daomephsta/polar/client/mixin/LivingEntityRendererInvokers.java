package io.github.daomephsta.polar.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererInvokers
{
	@Invoker
	public boolean callAddFeature(FeatureRenderer<?, ?> featureRenderer);
}
