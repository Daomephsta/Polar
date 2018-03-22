package leviathan143.polar.common.blocks;

import java.util.ArrayList;
import java.util.List;

import leviathan143.polar.client.ISpecialRender;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.items.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Polar.MODID)
@EventBusSubscriber(modid = Polar.MODID)
public class BlockRegistry
{
	private static final List<Block> blocks = new ArrayList<>();
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e)
	{

	}
	
	private static Block setupBlock(Block block, String name)
	{
		block.setRegistryName(Polar.MODID, name);
		block.setUnlocalizedName(Polar.MODID + '.' + name);
		blocks.add(block);
		
		//Setup itemblock
		ItemBlock itemBlock;
		if(block instanceof IHasSpecialItemBlock) itemBlock = ((IHasSpecialItemBlock) block).createItemBlock();
		else itemBlock = new ItemBlock(block);
		if (itemBlock != null)
		{
			itemBlock.setRegistryName(block.getRegistryName());
			ItemRegistry.queueItemBlock(itemBlock);
		}
		
		return block;
	}
	
	public static void registerModels()
	{
		for(Block block : blocks)
		{
			if(block instanceof ISpecialRender) ((ISpecialRender) block).registerRender();
		}
	}
}
