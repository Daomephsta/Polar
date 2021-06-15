package io.github.daomephsta.polar.client;

import io.github.daomephsta.polar.client.render.entities.AnomalyEntityRenderer;
import io.github.daomephsta.polar.client.render.entities.FeatureRendererJawblade;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.entities.EntityRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendereregistry.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.WolfEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public class PolarClientInitializer implements ClientModInitializer
{    
    @Override
    public void onInitializeClient()
    {
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.ANOMALY, AnomalyEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.STABILISED_BLOCK, RenderLayer.getTranslucent());
        ModelRegistry.registerModels();
        PolarClientNetworking.initialise();
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(this::registerFeatureRenderers);
    }
    
    private void registerFeatureRenderers(EntityType<? extends LivingEntity> entityType, 
        LivingEntityRenderer<?, ?> entityRenderer, RegistrationHelper registrationHelper, Context context)
    {
        if (entityType == EntityType.WOLF)
        {
            registrationHelper.register(new FeatureRendererJawblade((WolfEntityRenderer) entityRenderer));
            Polar.LOGGER.debug("Added jawblade feature to wolf renderer");
        }
    }
}
