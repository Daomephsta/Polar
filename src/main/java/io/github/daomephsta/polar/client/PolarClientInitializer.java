package io.github.daomephsta.polar.client;

import io.github.daomephsta.polar.client.mixin.LivingEntityRendererInvokers;
import io.github.daomephsta.polar.client.render.entities.AnomalyEntityRenderer;
import io.github.daomephsta.polar.client.render.entities.FeatureRendererJawblade;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.entities.anomalies.EntityAnomaly;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.entity.passive.WolfEntity;

public class PolarClientInitializer implements ClientModInitializer
{	
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(EntityAnomaly.class, (entityRenderDispatcher, context) -> new AnomalyEntityRenderer(entityRenderDispatcher));
		//TODO ModelLoaderRegistry.registerLoader(ModelStabilisedBlock.ModelStabilisedBlockLoader.INSTANCE);
		ModelRegistry.registerModels();
		MinecraftClientInitCallback.EVENT.register(this::onInitialiseMinecraftClient);
	}
	
	private void onInitialiseMinecraftClient(MinecraftClient client)
	{
		EntityRenderer<WolfEntity> wolfRenderer = client.getEntityRenderManager().getRenderer(WolfEntity.class);
		@SuppressWarnings("unchecked")
		LivingEntityRenderer<WolfEntity, WolfEntityModel<WolfEntity>> livingEntityRenderer = (LivingEntityRenderer<WolfEntity, WolfEntityModel<WolfEntity>>) wolfRenderer;
		((LivingEntityRendererInvokers) livingEntityRenderer).callAddFeature(new FeatureRendererJawblade(livingEntityRenderer));
		Polar.LOGGER.debug("Added jawblade feature to wolf renderer {}", wolfRenderer.getClass());
	}
}
