package leviathan143.polar.client;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Functions;

import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.blocks.*;
import leviathan143.polar.common.blocks.BlockRune.Variant;
import leviathan143.polar.common.items.itemblocks.ItemBlockAnomalyTapper;
import leviathan143.polar.common.items.itemblocks.ItemBlockRune;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = Polar.MODID, value = Side.CLIENT)
public class ModelRegistry
{
	private static final Queue<Item> modelRegistrationQueue = new ArrayDeque<>();
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e)
	{
		while (!modelRegistrationQueue.isEmpty())
		{
			Item item = modelRegistrationQueue.remove();
			ModelLoader.setCustomModelResourceLocation(item, 0, 
				new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
		registerSpecialItemModels();
		registerSpecialBlockModels();
	}
	
	private static void registerSpecialBlockModels()
	{
		//Anomaly Tapper: Create and register blockstate variant strings
		ModelResourceLocation 
			redTapper = new ModelResourceLocation(BlockRegistry.ANOMALY_TAPPER.getRegistryName(), 
				String.format("%s=%s,%s=%s", BlockAnomalyTapper.FACING.getName(), EnumFacing.UP.getName(), 
				BlockAnomalyTapper.POLARITY.getName(), Polarity.RED.getName())),
			blueTapper = new ModelResourceLocation(BlockRegistry.ANOMALY_TAPPER.getRegistryName(), 
				String.format("%s=%s,%s=%s", BlockAnomalyTapper.FACING.getName(), EnumFacing.UP.getName(), 
				BlockAnomalyTapper.POLARITY.getName(), Polarity.BLUE.getName()));
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(BlockRegistry.ANOMALY_TAPPER), stack -> ItemBlockAnomalyTapper.getPolarityStatic(stack) == Polarity.RED ? redTapper : blueTapper);
		ModelLoader.registerItemVariants(Item.getItemFromBlock(BlockRegistry.ANOMALY_TAPPER), redTapper, blueTapper);
		
		//Rune Block: Create and register blockstate variant strings
		Map<Variant, ModelResourceLocation> variantLocations = Arrays.stream(Variant.values())
			.collect(Collectors.toMap(Functions.identity(), 
				v -> new ModelResourceLocation(BlockRegistry.RUNE.getRegistryName(), BlockRune.VARIANT.getName() + "=" + v.getName())));
		ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(BlockRegistry.RUNE), stack -> variantLocations.get(ItemBlockRune.getVariant(stack)));
		ModelLoader.registerItemVariants(Item.getItemFromBlock(BlockRegistry.RUNE), variantLocations.values().toArray(new ModelResourceLocation[0]));
	}

	private static void registerSpecialItemModels() {}

	public static void enqueue(Item item)
	{
		modelRegistrationQueue.add(item);
	}
}
