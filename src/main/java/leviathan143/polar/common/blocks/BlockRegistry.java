package leviathan143.polar.common.blocks;

import leviathan143.polar.client.ModelRegistry;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.blocks.red.BlockStabilised;
import leviathan143.polar.common.items.ItemRegistry;
import leviathan143.polar.common.tileentities.TileEntityAnomalyTapper;
import leviathan143.polar.common.tileentities.TileEntityStabilisedBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Polar.MODID)
@EventBusSubscriber(modid = Polar.MODID)
public class BlockRegistry
{
	//Red
	public static final BlockStabilised STABILISED_BLOCK = null;
	//Blue
	// Other
	public static final BlockAnomalyTapper ANOMALY_TAPPER = null;
	public static final BlockRune RUNE = null;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().registerAll(
			setupBlockSpecialRender(new BlockAnomalyTapper(), "anomaly_tapper"),
			setupBlockSpecialRender(new BlockRune(), "rune"),
			// Red
			setupBlock(new BlockStabilised(), "stabilised_block")
			// Blue
			);
		GameRegistry.registerTileEntity(TileEntityAnomalyTapper.class, new ResourceLocation(Polar.MODID, "anomaly_tapper"));
		GameRegistry.registerTileEntity(TileEntityStabilisedBlock.class, new ResourceLocation(Polar.MODID, "stabilised_block"));
	}
	
	private static Block setupBlock(Block block, String name)
	{
		return setupBlock(block, name, true);
	}
	
	private static Block setupBlockSpecialRender(Block block, String name)
	{
		return setupBlock(block, name, false);
	}
	
	private static Block setupBlock(Block block, String name, boolean standardRender)
	{
		block.setRegistryName(Polar.MODID, name);
		block.setTranslationKey(Polar.MODID + '.' + name);
		
		//Setup itemblock
		ItemBlock itemBlock;
		if(block instanceof IHasSpecialItemBlock) itemBlock = ((IHasSpecialItemBlock) block).createItemBlock();
		else itemBlock = new ItemBlock(block);
		if (itemBlock != null)
		{
			itemBlock.setRegistryName(block.getRegistryName());
			ItemRegistry.queueItemBlock(itemBlock);
			if (standardRender)
				ModelRegistry.enqueue(itemBlock);
		}
		
		return block;
	}
}
