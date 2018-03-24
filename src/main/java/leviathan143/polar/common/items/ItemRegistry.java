package leviathan143.polar.common.items;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import leviathan143.polar.client.ISpecialRender;
import leviathan143.polar.common.Polar;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e)
	{
		while(!itemBlockQueue.isEmpty())
		{
			ItemBlock itemBlock = itemBlockQueue.remove();
			e.getRegistry().register(itemBlock);
		}
	}
	
	private static Item setupItem(Item item, String name)
	{
		item.setRegistryName(Polar.MODID, name);
		item.setUnlocalizedName(Polar.MODID + '.' + name);
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
