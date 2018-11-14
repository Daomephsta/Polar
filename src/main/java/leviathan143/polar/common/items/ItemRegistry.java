package leviathan143.polar.common.items;

import java.util.ArrayDeque;
import java.util.Queue;

import leviathan143.polar.client.ModelRegistry;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.items.blue.BaubleFallingBlockDestroyer;
import leviathan143.polar.common.items.red.BaubleFallingBlockStabiliser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Polar.MODID)
@EventBusSubscriber(modid = Polar.MODID)
public class ItemRegistry 
{
	private static final Queue<ItemBlock> itemBlockQueue = new ArrayDeque<>();
	// Red
	public static final Item RED_RESOURCE_BASIC = null;
	public static final BaubleFallingBlockStabiliser FALLING_BLOCK_STABILISER = null;
	// Blue
	public static final Item BLUE_RESOURCE_BASIC = null;
	public static final BaubleFallingBlockDestroyer FALLING_BLOCK_DESTROYER = null;
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().registerAll(
			// Red
			setupItem(new Item(), "red_resource_basic").setCreativeTab(Polar.TAB_RED),
			setupItem(new BaubleFallingBlockStabiliser().setCreativeTab(Polar.TAB_RED), "falling_block_stabiliser"),
			// Blue
			setupItem(new Item(), "blue_resource_basic").setCreativeTab(Polar.TAB_BLUE),
			setupItem(new BaubleFallingBlockDestroyer().setCreativeTab(Polar.TAB_BLUE), "falling_block_destroyer"));
		while(!itemBlockQueue.isEmpty())
		{
			ItemBlock itemBlock = itemBlockQueue.remove();
			e.getRegistry().register(itemBlock);
		}
	}
	
	private static Item setupItem(Item item, String name)
	{
		return setupItem(item, name, true);
	}
	
	@SuppressWarnings("unused") // Will be used later
	private static Item setupItemSpecialRender(Item item, String name)
	{
		return setupItem(item, name, false);
	}
	
	private static Item setupItem(Item item, String name, boolean standardRender)
	{
		item.setRegistryName(Polar.MODID, name);
		item.setTranslationKey(Polar.MODID + '.' + name);
		if (standardRender)
			ModelRegistry.enqueue(item);
		return item;
	}
	
	public static void queueItemBlock(ItemBlock itemBlock)
	{
		itemBlockQueue.add(itemBlock);
	}
}
