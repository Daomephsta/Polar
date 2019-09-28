package io.github.daomephsta.polar.client;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

public class ModelRegistry
{	
	public static void registerModels()
	{
		registerSpecialItemModels();
		registerSpecialBlockModels();
	}
	
	private static void registerSpecialBlockModels()
	{
		ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> StabilisedBlockModel.VariantProvider.INSTANCE);
	}

	private static void registerSpecialItemModels() {}
}
