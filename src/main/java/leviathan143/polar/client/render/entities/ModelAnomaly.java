package leviathan143.polar.client.render.entities;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelAnomaly extends ModelBase
{
	private final ModelRenderer box = new ModelRenderer(this);
	
	public ModelAnomaly()
	{
		box.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		box.setRotationPoint(0.0F, 4.0F, 0.0F);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		box.render(scale);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		box.rotateAngleX = (float) (Math.PI * 0.5D * MathHelper.cos(0.1F * ageInTicks) + 0.5D);
		box.rotateAngleZ = (float) (Math.PI * 0.5D * MathHelper.sin(0.1F * ageInTicks) + 0.5F);
	}
}
