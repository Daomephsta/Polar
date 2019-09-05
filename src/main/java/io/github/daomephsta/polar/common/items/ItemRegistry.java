package io.github.daomephsta.polar.common.items;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.items.blue.FallingBlockDestroyer;
import io.github.daomephsta.polar.common.items.red.FallingBlockStabiliser;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry 
{
	// Red
	public static final Item RED_RESOURCE_BASIC = register(new Item(new Item.Settings().group(PolarAPI.TAB_RED)), "red_resource_basic");
	public static final FallingBlockStabiliser FALLING_BLOCK_STABILISER = register(new FallingBlockStabiliser(), "falling_block_stabiliser");
	// Blue
	public static final Item BLUE_RESOURCE_BASIC = register(new Item(new Item.Settings().group(PolarAPI.TAB_BLUE)), "blue_resource_basic");
	public static final FallingBlockDestroyer FALLING_BLOCK_DESTROYER = register(new FallingBlockDestroyer(), "falling_block_destroyer");
	//Other
	public static final ItemJawblade STONE_JAWBLADE = register(new ItemJawblade(ToolMaterials.STONE), "stone_jawblade");
	public static final ItemJawblade GOLD_JAWBLADE = register(new ItemJawblade(ToolMaterials.GOLD), "gold_jawblade");
	public static final ItemJawblade IRON_JAWBLADE = register(new ItemJawblade(ToolMaterials.IRON), "iron_jawblade");
	public static final ItemJawblade DIAMOND_JAWBLADE = register(new ItemJawblade(ToolMaterials.DIAMOND), "diamond_jawblade");
	
	private static <T extends Item> T register(T item, String name)
	{
		return Registry.register(Registry.ITEM, new Identifier(Polar.MODID, name), item);
	}
	
	public static void initialize()
	{
		//Dummy method to force static init
	}
}
