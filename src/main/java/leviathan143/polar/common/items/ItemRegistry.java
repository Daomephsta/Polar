package leviathan143.polar.common.items;

import java.util.*;

import leviathan143.polar.api.Polarity;
import leviathan143.polar.client.ISpecialRender;
import leviathan143.polar.common.Polar;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(Polar.MODID)
@EventBusSubscriber(modid = Polar.MODID)
public class ItemRegistry 
{
	private static final List<Item> items = new ArrayList<>();
	private static final Queue<ItemBlock> itemBlockQueue = new ArrayDeque<>();
	
	public static final Item RESEARCH_JOURNAL = null;
	public static final Item RED_IRRADIATED_REDSTONE = null;
	public static final Item BLUE_IRRADIATED_LAPIS = null;
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
		e.getRegistry().registerAll(
			setupItem(new IrradiatedItem(Items.REDSTONE, Polarity.RED), "red_irradiated_redstone"),
			setupItem(new IrradiatedItem(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), Polarity.BLUE), 
				"blue_irradiated_lapis"));
		while(!itemBlockQueue.isEmpty())
		{
			ItemBlock itemBlock = itemBlockQueue.remove();
			e.getRegistry().register(itemBlock);
		}
	}
	
	private static Item setupItem(Item item, String name)
	{
		item.setRegistryName(Polar.MODID, name);
		item.setTranslationKey(Polar.MODID + '.' + name);
		items.add(item);
		return item;
	}
	
	public static void registerModels()
	{
		for(Item item : items)
		{
			if(item instanceof ISpecialRender) ((ISpecialRender) item).registerRender();
			else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
	
	public static void queueItemBlock(ItemBlock itemBlock)
	{
		itemBlockQueue.add(itemBlock);
	}
}
