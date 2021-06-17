package io.github.daomephsta.polar.client.render.entities;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalyEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AnomalyEntityRenderer extends EntityRenderer<AnomalyEntity>
{
    private static final Identifier 
        TEXTURE_RED = Polar.id("textures/entity/anomaly_red.png"), 
        TEXTURE_BLUE = Polar.id("textures/entity/anomaly_blue.png");
    private final AnomalyModel model = new AnomalyModel();

    public AnomalyEntityRenderer(EntityRendererFactory.Context renderContext)
    {
        super(renderContext);
    }
    
    @Override
    public void render(AnomalyEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
    {
//        GlStateManager.pushMatrix();
//        {
//            this.bindEntityTexture(entity);
//            GlStateManager.translated(x, y, z);
//            model.setAngles(entity, 1.0F, 1.0F, entity.age, 1.0F, 1.0F, 0.1F);
//            model.render(entity, 1.0F, 1.0F, entity.age, 1.0F, 1.0F, 0.1F);
//            GlStateManager.color3f(1.0F, 1.0F, 1.0F);
//        }
//        GlStateManager.popMatrix();
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
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
