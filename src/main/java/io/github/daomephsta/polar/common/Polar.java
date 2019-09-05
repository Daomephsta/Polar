package io.github.daomephsta.polar.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent;
import io.github.daomephsta.polar.common.core.InternalMethodAccessors;
import io.github.daomephsta.polar.common.handlers.JawbladeHandler;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import io.github.daomephsta.polar.common.handlers.wearables.WearablesHandler;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import io.github.daomephsta.polar.common.network.PacketHandler;
import io.github.daomephsta.polar.common.recipes.EnhancedShapedRecipe;
import io.github.daomephsta.polar.common.recipes.EnhancedShapelessRecipe;
import io.github.daomephsta.polar.common.tileentities.PolarBlockEntityTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class Polar implements ModInitializer
{	
	public static final String MODNAME = "Polar";
	public static final String MODID = "polar";
	public static final String VERSION = "0.0.1";
	
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	
	@Override
	public void onInitialize()
	{
		PolarAPI.initialiseAPI(new InternalMethodAccessors());
		//TriggerRegistry.init();
		BlockRegistry.initialize();
		ItemRegistry.initialize();
		registerComponents();
		PolarBlockEntityTypes.initialize();
		PacketHandler.registerPackets();
		JawbladeHandler.registerEventCallbacks();
		ResidualPolarityHandler.registerEventCallbacks();
		WearablesHandler.registerEventCallbacks();
		registerRecipes();
	}
	
	private static void registerComponents()
	{
		PolarPlayerDataComponent.register();
		PolarChargeStorageComponent.register();
	}
	
	private void registerRecipes()
	{
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "crafting_shaped_enhanced"), EnhancedShapedRecipe.SERIALIZER);
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MODID, "crafting_shapeless_enhanced"), EnhancedShapelessRecipe.SERIALIZER);
		//TODO Reimplement recipes
		//event.getRegistry().registerAll(new RecipeChargeItem().setRegistryName("charge_item"));
	}
}
