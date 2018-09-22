package leviathan143.polar.client.render.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Anomaly - Daomephsta
 * Created using Tabula 7.0.0
 */
public class ModelAnomaly extends ModelBase 
{
    public ModelRenderer anomaly;

    public ModelAnomaly() 
    {
        this.textureWidth = 48;
        this.textureHeight = 24;
        this.anomaly = new ModelRenderer(this, 0, 0);
        this.anomaly.setRotationPoint(0.0F, 16.0F, 0.0F);
        this.anomaly.addBox(-6.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
    	super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        anomaly.render(scale);
    }
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		float speed = 0.05F;
		anomaly.rotateAngleX = (float) (Math.PI * 0.5D * MathHelper.cos(speed * ageInTicks) + 0.5D);
		anomaly.rotateAngleZ = (float) (Math.PI * 0.5D * MathHelper.sin(speed * ageInTicks) + 0.5F);
	}
}
