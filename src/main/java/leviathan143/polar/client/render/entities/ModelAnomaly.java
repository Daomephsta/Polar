package leviathan143.polar.client.render.entities;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Anomaly - Daomephsta
 * Created using Tabula 7.0.0
 */
public class ModelAnomaly extends ModelBase 
{
	private static final FloatBuffer STANDARD_LIGHTING_BUF = GLAllocation.createDirectFloatBuffer(16);
	private static final FloatBuffer GLOW_BUF = GLAllocation.createDirectFloatBuffer(4); 
    private ModelRenderer 
    	anomaly,
    	overlay;

    public ModelAnomaly() 
    {
    	this.textureWidth = 44;
        this.textureHeight = 44;
        this.overlay = new ModelRenderer(this, 0, 22);
        this.overlay.setRotationPoint(0.0F, 5.5F, 0.0F);
        this.overlay.addBox(-5.5F, -5.5F, -5.5F, 11, 11, 11, 0.0F);
        this.anomaly = new ModelRenderer(this, 0, 0);
        this.anomaly.setRotationPoint(0.0F, 5.5F, 0.0F);
        this.anomaly.addBox(-5.5F, -5.5F, -5.5F, 11, 11, 11, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
    	double overlayOffsetScale = 1.001D;
    	super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    	GlStateManager.pushMatrix();
    	{
    		//Make runes pulse
    		float speed = 0.05F;
    		float light = -(MathHelper.sin(speed * ageInTicks + MathHelper.cos(speed * ageInTicks)));
    		//Store the original light model
    		GlStateManager.getFloat(GL11.GL_LIGHT_MODEL_AMBIENT, STANDARD_LIGHTING_BUF);
    		GlStateManager.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, setFloatBuffer(light, light, light, light));
        	GlStateManager.translate(overlay.offsetX, overlay.offsetY, overlay.offsetZ);
        	GlStateManager.translate(overlay.rotationPointX * scale, overlay.rotationPointY * scale, overlay.rotationPointZ * scale);
        	GlStateManager.scale(overlayOffsetScale, overlayOffsetScale, overlayOffsetScale);
        	GlStateManager.translate(-overlay.offsetX, -overlay.offsetY, -overlay.offsetZ);
        	GlStateManager.translate(-overlay.rotationPointX * scale, -overlay.rotationPointY * scale, -overlay.rotationPointZ * scale);
        	overlay.render(scale);
        	//Reset the light model
        	GlStateManager.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, STANDARD_LIGHTING_BUF);
        	GlStateManager.disableLighting();
            anomaly.render(scale);
        	GlStateManager.enableLighting();
    	}
        GlStateManager.popMatrix();
    }
    
    private FloatBuffer setFloatBuffer(float a, float b, float c, float d)
    {
    	GLOW_BUF.clear();
    	GLOW_BUF.put(a).put(b).put(c).put(d);
    	GLOW_BUF.flip();
    	return GLOW_BUF;
    }
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		float speed = 0.05F;
		
		float rotateAngleX = (float) (Math.PI * 0.5D * MathHelper.cos(speed * ageInTicks) + 0.5D);
		anomaly.rotateAngleX = overlay.rotateAngleX = rotateAngleX; 
		float rotateAngleZ = (float) (Math.PI * 0.5D * MathHelper.sin(speed * ageInTicks) + 0.5F);
		anomaly.rotateAngleZ = overlay.rotateAngleZ = rotateAngleZ;
	}
}
