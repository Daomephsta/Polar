package leviathan143.polar.client;

import leviathan143.polar.client.render.entities.LayerRendererJawblade;
import leviathan143.polar.client.render.entities.RenderAnomaly;
import leviathan143.polar.common.AbstractProxy;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.entities.anomalies.EntityAnomaly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends AbstractProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityAnomaly.class, rm -> new RenderAnomaly(rm));
		
		ModelLoaderRegistry.registerLoader(ModelStabilisedBlock.ModelStabilisedBlockLoader.INSTANCE);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		Render<Entity> wolfRenderer = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(EntityWolf.class);
		((RenderLivingBase<?>) wolfRenderer).addLayer(new LayerRendererJawblade());
		Polar.LOGGER.debug("Added jawblade render layer to wolf renderer {}", wolfRenderer.getClass());
	}
}
