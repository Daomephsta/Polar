package io.github.daomephsta.polar.client.render.entities;

import io.github.daomephsta.polar.common.handlers.JawbladeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

public class FeatureRendererJawblade extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>>
{
    private static final float PIXEL = 1.0F / 16.0F;

    public FeatureRendererJawblade(FeatureRendererContext<WolfEntity, WolfEntityModel<WolfEntity>> context)
    {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, WolfEntity wolf, 
        float limbSwing, float limbDistance, float partialTicks, float lookYaw, float neckYaw, float headPitch)
    {
        ItemStack jawblade = JawbladeHandler.getJawblade(wolf);
        if (jawblade.isEmpty()) return;        
        matrices.push();
        //Translate to head rotation point and rotate into position
        matrices.translate(-1.0 * PIXEL, 13.5 * PIXEL, -7.0 * PIXEL);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(neckYaw));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(headPitch));
        matrices.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(wolf.getBegAnimationProgress(partialTicks) + 
            wolf.getShakeAnimationProgress(partialTicks, 0.0F)));
        MinecraftClient.getInstance().getHeldItemRenderer().renderItem(
            wolf, jawblade, ModelTransformation.Mode.HEAD, false, matrices, vertices, light);
        matrices.pop();
    }
}
