package leviathan143.polar.common.items;

import java.util.ArrayDeque;
import java.util.Queue;

import leviathan143.polar.api.Polarity;
import leviathan143.polar.client.ModelRegistry;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.items.red.BaubleFallingBlockStabiliser;
import net.minecraft.init.Items;
import net.minecraft.item.*;
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
	public static final Item RED_IRRADIATED_REDSTONE = null;
	public static final BaubleFallingBlockStabiliser FALLING_BLOCK_STABILISER = null;
	// Blue
	public static final Item BLUE_IRRADIATED_LAPIS = null;
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().registerAll(
			// Red
			setupItem(new IrradiatedItem(Items.REDSTONE, Polarity.RED), "red_irradiated_redstone").setCreativeTab(Polar.TAB_RED),
			setupItem(new BaubleFallingBlockStabiliser().setCreativeTab(Polar.TAB_RED), "falling_block_stabiliser"),
			// Blue
			setupItem(new IrradiatedItem(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), Polarity.BLUE), 
				"blue_irradiated_lapis").setCreativeTab(Polar.TAB_BLUE));
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
