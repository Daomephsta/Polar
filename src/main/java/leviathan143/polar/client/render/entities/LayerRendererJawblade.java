package leviathan143.polar.client.render.entities;

import static net.minecraft.client.renderer.GlStateManager.popMatrix;
import static net.minecraft.client.renderer.GlStateManager.pushMatrix;
import static net.minecraft.client.renderer.GlStateManager.rotate;
import static net.minecraft.client.renderer.GlStateManager.scale;
import static net.minecraft.client.renderer.GlStateManager.translate;

import leviathan143.polar.common.handlers.JawbladeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.ItemStack;

public class LayerRendererJawblade implements LayerRenderer<EntityWolf>
{
	@Override
	public void doRenderLayer(EntityWolf wolf, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ItemStack jawblade = JawbladeHandler.getJawblade(wolf);
		if (jawblade.isEmpty()) return;
		pushMatrix();
		{
			//Scale to model size
			GlStateManager.scale(scale, scale, scale);
			//Translate to head rotation point and rotate into position
			translate(-1.0F, 13.5F, -7.0F);
			//Rotate in the order YXZ, or rotation does not work properly sometimes
			rotate(netHeadYaw, 0.0F, 1.0F, 0.0F);
			rotate(headPitch, 1.0F, 0.0F, 0.0F);
			float rotateZ = (wolf.getInterestedAngle(partialTicks) + wolf.getShakeAngle(partialTicks, 0.0F)) * 180.F / (float) Math.PI;
			rotate(rotateZ, 0.0F, 0.0F, 1.0F);
			pushMatrix();
			{
				double scaleUp = 1.0D / scale;
				scale(scaleUp, scaleUp, scaleUp);
				Minecraft.getMinecraft().getRenderItem().renderItem(jawblade, wolf, TransformType.HEAD, false);
			}
			popMatrix();
		}
		popMatrix();
	}

	@Override
	public boolean shouldCombineTextures()
	{
		return false;
	}
}
