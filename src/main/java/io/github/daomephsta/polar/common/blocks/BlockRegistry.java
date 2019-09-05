package io.github.daomephsta.polar.common.blocks;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.red.StabilisedBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockRegistry
{
	//Red
	public static final StabilisedBlock STABILISED_BLOCK = registerBlock(new StabilisedBlock(), "stabilised_block");
	//Blue
	// Other
	public static final AnomalyTapperBlock ANOMALY_TAPPER = registerBlock(new AnomalyTapperBlock(), "anomaly_tapper");
	public static final BlockRune RUNE = registerBlock(new BlockRune(), "rune");
	
	private static <T extends Block> T registerBlock(T block, String name)
	{
		Registry.register(Registry.BLOCK, new Identifier(Polar.MODID, name), block);
		BlockItem blockItem = block instanceof IHasSpecialBlockItem
			? ((IHasSpecialBlockItem) block).createBlockItem()
			: new BlockItem(block, new Item.Settings());
		if (blockItem != null)
			Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), blockItem);
		return block;
	}
	
	public static void initialize()
	{
		//Dummy method to force static init
	}
}
