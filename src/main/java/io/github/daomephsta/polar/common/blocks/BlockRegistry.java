package io.github.daomephsta.polar.common.blocks;

import java.util.function.Function;

import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.red.StabilisedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class BlockRegistry
{
	//Red
	public static final StabilisedBlock STABILISED_BLOCK = registerBlock(new StabilisedBlock(), "stabilised_block");
	public static final AnomalyTapperBlock RED_ANOMALY_TAPPER = registerBlock(new AnomalyTapperBlock(Polarity.RED), "red_anomaly_tapper");
	//Blue
	public static final AnomalyTapperBlock BLUE_ANOMALY_TAPPER = registerBlock(new AnomalyTapperBlock(Polarity.BLUE), "blue_anomaly_tapper");
	// Other
	public static final Block RAL_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_BLUE)), "ral_rune"),
							  SAI_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_BLUE)), "sai_rune"),
							  DEL_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_BLUE)), "del_rune"),
							  PER_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_BLUE)), "per_rune"),
							  SEI_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_BLUE)), "sei_rune"),
							  NEI_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_BLUE)), "nei_rune"),
							  QER_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_RED)), "qer_rune"),
							  DER_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_RED)), "der_rune"),
							  NIM_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_RED)), "nim_rune"),
							  MIR_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_RED)), "mir_rune"),
							  JO_RUNE  = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_RED)), "jo_rune"),
							  TIR_RUNE = registerBlock(new Block(Block.Settings.of(Material.STONE)), 
									  block -> new BlockItem(block, new Item.Settings().group(PolarApi.TAB_RED)), "tir_rune");
	
	private static <T extends Block> T registerBlock(T block, String name)
	{
		Function<Block, BlockItem> blockItemCreator = block instanceof IHasSpecialBlockItem
			? b -> ((IHasSpecialBlockItem) b).createBlockItem()
			: b -> new BlockItem(b, new Item.Settings());
		return registerBlock(block, blockItemCreator, name);
	}
	
	private static <T extends Block> T registerBlock(T block, Function<Block, BlockItem> blockItemCreator, String name)
	{
		Registry.register(Registry.BLOCK, Polar.id(name), block);
		BlockItem blockItem = blockItemCreator.apply(block);
		if (blockItem != null)
			Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), blockItem);
		return block;
	}
	
	public static void initialize()
	{
		//Dummy method to force static init
	}
}
