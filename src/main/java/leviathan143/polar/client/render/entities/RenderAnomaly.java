package leviathan143.polar.client.render.entities;

import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.entities.EntityAnomaly;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderAnomaly extends Render<EntityAnomaly>
{
	private static final ResourceLocation TEXTURE = new ResourceLocation(Polar.MODID, "textures/entity/anomaly.png");
	private final ModelAnomaly model = new ModelAnomaly();

	public RenderAnomaly(RenderManager renderManager)
	{
		super(renderManager);
	}

	@Override
	public void doRender(EntityAnomaly entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		{
			this.bindEntityTexture(entity);
			if(entity.getPolarity() == Polarity.RED) GlStateManager.color(1.0F, 0.0F, 0.0F);
			else if(entity.getPolarity() == Polarity.BLUE) GlStateManager.color(0.0F, 0.0F, 1.0F);
			GlStateManager.translate(x, y, z);
			model.setRotationAngles(1.0F, 1.0F, entity.ticksExisted, 1.0F, 1.0F, 0.1F, entity);
			model.render(entity, 1.0F, 1.0F, entity.ticksExisted, 1.0F, 1.0F, 0.1F);
			GlStateManager.color(1.0F, 1.0F, 1.0F);
		}
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAnomaly entity)
	{
		return TEXTURE;
	}
}
