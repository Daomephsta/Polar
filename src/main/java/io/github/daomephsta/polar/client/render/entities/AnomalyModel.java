package io.github.daomephsta.polar.client.render.entities;

import io.github.daomephsta.polar.common.entities.anomalies.EntityAnomaly;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Anomaly - Daomephsta
 * Created using Tabula 7.0.0
 */
//TODO
public class AnomalyModel extends EntityModel<EntityAnomaly> 
{
//    private static final FloatBuffer STANDARD_LIGHTING_BUF = GlAllocationUtils.allocateFloatBuffer(16);
//    private static final FloatBuffer GLOW_BUF = GlAllocationUtils.allocateFloatBuffer(4); 
//    private ModelPart 
//        base,
//        overlay;

    public AnomalyModel() 
    {
//        this.textureWidth = 44;
//        this.textureHeight = 44;
        
//        Cuboid c = new Cuboid(i, j, f, g, h, k, l, m, n, o, p, bl, q, r);
//        this.overlay = new ModelPart(Arrays.asList(c), Collections.emptyMap());
//        this.overlay.setPivot(0.0F, 5.5F, 0.0F);
//        
//        Cuboid d = new Cuboid(i, j, f, g, h, k, l, m, n, o, p, bl, q, r);
//        this.base = new ModelPart(Arrays.asList(d), Collections.emptyMap());
//        this.base.setPivot(0.0F, 5.5F, 0.0F);
//        
//        this.overlay = new ModelPart(this, 0, 22);
//        this.overlay.setPivot(0.0F, 5.5F, 0.0F);
//        this.overlay.addBox(-5.5F, -5.5F, -5.5F, 11, 11, 11, 0.001F);
//        this.base = new ModelPart(this, 0, 0);
//        this.base.setPivot(0.0F, 5.5F, 0.0F);
//        this.base.addBox(-5.5F, -5.5F, -5.5F, 11, 11, 11, 0.0F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int i, int j, float f, 
        float g, float h, float k)
    {
//        double overlayOffsetScale = 1.001D;
//        super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//        GlStateManager.pushMatrix();
//        {
//            //Make runes pulse
//            float speed = 0.05F;
//            float light = -(MathHelper.sin(speed * ageInTicks + MathHelper.cos(speed * ageInTicks)));
//            //Store the original light model
//            GlStateManager.getMatrix(GL11.GL_LIGHT_MODEL_AMBIENT, STANDARD_LIGHTING_BUF);
//            GlStateManager.lightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setFloatBuffer(light, light, light, light));
//            GlStateManager.translatef(overlay.x, overlay.y, overlay.z);
//            GlStateManager.translatef(overlay.rotationPointX * scale, overlay.rotationPointY * scale, overlay.rotationPointZ * scale);
//            GlStateManager.scaled(overlayOffsetScale, overlayOffsetScale, overlayOffsetScale);
//            GlStateManager.translated(-overlay.x, -overlay.y, -overlay.z);
//            GlStateManager.translatef(-overlay.rotationPointX * scale, -overlay.rotationPointY * scale, -overlay.rotationPointZ * scale);
//            overlay.render(scale);
//            //Reset the light model
//            GlStateManager.lightModel(GL11.GL_LIGHT_MODEL_AMBIENT, STANDARD_LIGHTING_BUF);
//            GlStateManager.disableLighting();
//            base.render(scale);
//            GlStateManager.enableLighting();
//        }
//        GlStateManager.popMatrix();
    }
    
//    private FloatBuffer setFloatBuffer(float a, float b, float c, float d)
//    {
//        GLOW_BUF.clear();
//        GLOW_BUF.put(a).put(b).put(c).put(d);
//        GLOW_BUF.flip();
//        return GLOW_BUF;
//    }
    
    @Override
    public void setAngles(EntityAnomaly anomaly, float f, float g, float h, float i, float j)
    {
//        float speed = 0.05F;
//        base.pitch = overlay.pitch = (float) (Math.PI * 0.5D * MathHelper.cos(speed * ageInTicks) + 0.5D); 
//        base.roll = overlay.roll = (float) (Math.PI * 0.5D * MathHelper.sin(speed * ageInTicks) + 0.5F);
    }
}
