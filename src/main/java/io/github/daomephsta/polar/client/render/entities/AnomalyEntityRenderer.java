package io.github.daomephsta.polar.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.entities.anomalies.EntityAnomaly;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;

public class AnomalyEntityRenderer extends EntityRenderer<EntityAnomaly>
{
	private static final Identifier 
		TEXTURE_RED = new Identifier(Polar.MOD_ID, "textures/entity/anomaly_red.png"), 
		TEXTURE_BLUE = new Identifier(Polar.MOD_ID, "textures/entity/anomaly_blue.png");
	private final AnomalyModel model = new AnomalyModel();

	public AnomalyEntityRenderer(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher);
	}

	@Override
	public void render(EntityAnomaly entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		GlStateManager.pushMatrix();
		{
			this.bindEntityTexture(entity);
			GlStateManager.translated(x, y, z);
			model.setAngles(entity, 1.0F, 1.0F, entity.age, 1.0F, 1.0F, 0.1F);
			model.render(entity, 1.0F, 1.0F, entity.age, 1.0F, 1.0F, 0.1F);
			GlStateManager.color3f(1.0F, 1.0F, 1.0F);
		}
		GlStateManager.popMatrix();
		super.render(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected Identifier getTexture(EntityAnomaly entity)
	{
		if(entity.getPolarity() == Polarity.RED) return TEXTURE_RED;
		else if(entity.getPolarity() == Polarity.BLUE) return TEXTURE_BLUE;
		else return null;
	}
}
