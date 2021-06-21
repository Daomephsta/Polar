package io.github.daomephsta.polar.client.render.entities;

import io.github.daomephsta.polar.client.PolarClientInitializer;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalyEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

public class AnomalyEntityRenderer extends EntityRenderer<AnomalyEntity>
{
    private static final Identifier
        TEXTURE_RED = Polar.id("textures/entity/anomaly_red.png"),
        TEXTURE_BLUE = Polar.id("textures/entity/anomaly_blue.png");
    private final ModelPart base, overlay;

    public AnomalyEntityRenderer(EntityRendererFactory.Context renderContext)
    {
        super(renderContext);
        ModelPart root = renderContext.getPart(PolarClientInitializer.ANOMALY_LAYER);
        this.base = root.getChild("base");
        this.overlay = root.getChild("overlay");
    }

    public static TexturedModelData getTexturedModelData()
    {
        ModelData model = new ModelData();
        ModelPartData part = model.getRoot();
        part.addChild("base", ModelPartBuilder.create()
            .uv(0, 0)
            .cuboid(-5.5F, -5.5F, -5.5F, 11, 11, 11),
            ModelTransform.NONE);
        part.addChild("overlay", ModelPartBuilder.create()
            .uv(0, 22)
            .cuboid(-5.5F, -5.5F, -5.5F, 11, 11, 11, new Dilation(0.001F)),
            ModelTransform.NONE);
        return TexturedModelData.of(model, 44, 44);
    }

    @Override
    public void render(AnomalyEntity entity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider verticesProvider, int light)
    {
        matrixStack.push();
        float speed = 0.05F;
        float pitch = (float) (Math.PI * 0.5D * MathHelper.cos(speed * entity.age) + 0.5D);
        matrixStack.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(pitch));
        float roll = (float) (Math.PI * 0.5D * MathHelper.sin(speed * entity.age) + 0.5F);
        matrixStack.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(roll));
        VertexConsumer vertices = verticesProvider.getBuffer(getLayer(entity));
        base.render(matrixStack, vertices, light, OverlayTexture.DEFAULT_UV);
        //Make runes pulse
        float runeLight = (0.5F + 0.5F * MathHelper.sin(speed * entity.age)) * 15.0F;
        overlay.render(matrixStack, vertices, LightmapTextureManager.pack(0, (int) runeLight),
            OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(entity, yaw, tickDelta, matrixStack, verticesProvider, light);
    }

    private RenderLayer getLayer(AnomalyEntity entity)
    {
        return RenderLayer.getArmorCutoutNoCull(getTexture(entity));
    }

    @Override
    public Identifier getTexture(AnomalyEntity entity)
    {
        return switch (entity.getPolarity())
        {
            case RED -> TEXTURE_RED;
            case BLUE -> TEXTURE_BLUE;
            default -> null;
        };
    }
}
