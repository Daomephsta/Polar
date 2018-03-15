package leviathan143.polar.common.blocks;

import leviathan143.polar.common.Polar.Constants;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Constants.MODID)
@EventBusSubscriber(modid = Constants.MODID)
public class BlockRegistry
{
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e)
	{
		
	}
}
