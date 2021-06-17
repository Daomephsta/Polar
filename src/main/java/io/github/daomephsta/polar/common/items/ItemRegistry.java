package io.github.daomephsta.polar.common.items;

import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.items.blue.FallingBlockDestroyerItem;
import io.github.daomephsta.polar.common.items.red.FallingBlockStabiliserItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.registry.Registry;

public class ItemRegistry 
{
    // Red
    public static final Item RED_RESOURCE_BASIC = register(new Item(new Item.Settings().group(PolarApi.TAB_RED)), "red_resource_basic");
    public static final FallingBlockStabiliserItem FALLING_BLOCK_STABILISER = register(new FallingBlockStabiliserItem(), "falling_block_stabiliser");
    // Blue
    public static final Item BLUE_RESOURCE_BASIC = register(new Item(new Item.Settings().group(PolarApi.TAB_BLUE)), "blue_resource_basic");
    public static final FallingBlockDestroyerItem FALLING_BLOCK_DESTROYER = register(new FallingBlockDestroyerItem(), "falling_block_destroyer");
    //Other
    public static final JawbladeItem STONE_JAWBLADE = register(new JawbladeItem(ToolMaterials.STONE), "stone_jawblade");
    public static final JawbladeItem GOLD_JAWBLADE = register(new JawbladeItem(ToolMaterials.GOLD), "gold_jawblade");
    public static final JawbladeItem IRON_JAWBLADE = register(new JawbladeItem(ToolMaterials.IRON), "iron_jawblade");
    public static final JawbladeItem DIAMOND_JAWBLADE = register(new JawbladeItem(ToolMaterials.DIAMOND), "diamond_jawblade");
    
    private static <T extends Item> T register(T item, String name)
    {
        return Registry.register(Registry.ITEM, Polar.id(name), item);
    }
    
    public static void initialize()
    {
        //Dummy method to force static init
    }
}
