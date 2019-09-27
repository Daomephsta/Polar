package io.github.daomephsta.polar.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.common.advancements.triggers.CriterionRegistry;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent;
import io.github.daomephsta.polar.common.components.PolarPlayerDataComponent;
import io.github.daomephsta.polar.common.config.PolarConfig;
import io.github.daomephsta.polar.common.core.InternalMethodAccessors;
import io.github.daomephsta.polar.common.entities.EntityRegistry;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalySpawningHandler;
import io.github.daomephsta.polar.common.handlers.JawbladeHandler;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import io.github.daomephsta.polar.common.handlers.research.ObserveFallingBlockHandler;
import io.github.daomephsta.polar.common.handlers.wearables.WearablesHandler;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import io.github.daomephsta.polar.common.network.PacketTypes;
import io.github.daomephsta.polar.common.recipes.PolarRecipes;
import io.github.daomephsta.polar.common.tileentities.PolarBlockEntityTypes;
import net.fabricmc.api.ModInitializer;


public class Polar implements ModInitializer
{	
	public static final String MOD_ID = "polar";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Override
	public void onInitialize()
	{
		PolarAPI.initialiseAPI(new InternalMethodAccessors());
		PolarConfig.initialise();
		BlockRegistry.initialize();
		ItemRegistry.initialize();
		EntityRegistry.initialise();
		PolarTags.initialise();
		CompatibilityTags.initialise();
		CriterionRegistry.initialise();
		registerComponents();
		PolarBlockEntityTypes.initialize();
		PacketTypes.initialise();
		AnomalySpawningHandler.registerEventCallbacks();
		JawbladeHandler.registerEventCallbacks();
		ResidualPolarityHandler.registerEventCallbacks();
		WearablesHandler.initialise();
		ObserveFallingBlockHandler.initialise();
		PolarRecipes.initialise();
	}
	
	private static void registerComponents()
	{
		PolarPlayerDataComponent.register();
		PolarChargeStorageComponent.register();
	}
	
	public static void forceClassInit(Class<?> clazz) {}
}
