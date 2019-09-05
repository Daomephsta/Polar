package io.github.daomephsta.polar.client;

public class ModelRegistry
{	
	public static void registerModels()
	{
		registerSpecialItemModels();
		registerSpecialBlockModels();
	}
	
	private static void registerSpecialBlockModels()
	{
		/*TODO Reimplement
		ModelLoadingRegistry.INSTANCE.registerAppender((resourceManager, out) -> 
		{
			//Anomaly Tapper: Create and register blockstate variant strings
			out.accept(new ModelIdentifier(Registry.BLOCK.getId(BlockRegistry.ANOMALY_TAPPER), 
					String.format("%s=%s,%s=%s", BlockAnomalyTapper.FACING.getName(), Direction.UP.getName(), 
					BlockAnomalyTapper.POLARITY.getName(), Polarity.RED.asString())));
			out.accept(new ModelIdentifier(Registry.BLOCK.getId(BlockRegistry.ANOMALY_TAPPER), 
					String.format("%s=%s,%s=%s", BlockAnomalyTapper.FACING.getName(), Direction.UP.getName(), 
					BlockAnomalyTapper.POLARITY.getName(), Polarity.BLUE.asString())));
			 
			//Rune Block: Create and register blockstate variant strings
			Arrays.stream(Variant.values())
				.map(v -> new ModelIdentifier(Registry.BLOCK.getId(BlockRegistry.RUNE), BlockRune.VARIANT.getName() + "=" + v.asString()))
				.forEach(out::accept);
		});*/
	}

	private static void registerSpecialItemModels() {}
}
