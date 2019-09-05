package io.github.daomephsta.polar.client.render.entities;



import com.mojang.blaze3d.platform.GlStateManager;

import io.github.daomephsta.polar.common.handlers.JawbladeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;

public class FeatureRendererJawblade extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>>
{
	public FeatureRendererJawblade(FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> featureRendererContext_1)
	{
		super(featureRendererContext_1);
	}

	@Override
	public void render(WolfEntity wolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ItemStack jawblade = JawbladeHandler.getJawblade(wolf);
		if (jawblade.isEmpty()) return;
		GlStateManager.pushMatrix();
		{
			//Scale to model size
			GlStateManager.scaled(scale, scale, scale);
			//Translate to head rotation point and rotate into position
			GlStateManager.translatef(-1.0F, 13.5F, -7.0F);
			//Rotate in the order YXZ, or rotation does not work properly sometimes
			GlStateManager.rotatef(netHeadYaw, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotatef(headPitch, 1.0F, 0.0F, 0.0F);
			float rotateZ = (wolf.getBegAnimationProgress(partialTicks) + wolf.getShakeAnimationProgress(partialTicks, 0.0F)) * 180.F / (float) Math.PI;
			GlStateManager.rotatef(rotateZ, 0.0F, 0.0F, 1.0F);
			GlStateManager.pushMatrix();
			{
				double scaleUp = 1.0D / scale;
				GlStateManager.scaled(scaleUp, scaleUp, scaleUp);
				MinecraftClient.getInstance().getItemRenderer().renderHeldItem(jawblade, wolf, ModelTransformation.Type.HEAD, false);
			}
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	@Override
	public boolean hasHurtOverlay()
	{
		return false;
	}
}
