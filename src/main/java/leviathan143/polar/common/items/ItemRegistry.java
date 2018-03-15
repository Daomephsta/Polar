package leviathan143.polar.common.items;

import javax.annotation.Nullable;

import leviathan143.polar.common.Polar.Constants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Constants.MODID)
@EventBusSubscriber(modid = Constants.MODID)
public class ItemRegistry 
{
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
	}

	/* This interface declares that the block type that implements it needs a
	 * custom ItemBlock or no ItemBlock, and is responsible for creating it */
	public static interface ISpecialItemBlock
	{
		/**
		 * This method should return a new instance of ItemBlock or a subclass.
		 * It may also return null if the block should not have an ItemBlock
		 * 
		 * @return the instance of ItemBlock that should be registered for this
		 *         block
		 */
		@Nullable
		public ItemBlock createItemBlock();
	}
}
