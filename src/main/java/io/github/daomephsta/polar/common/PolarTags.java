package io.github.daomephsta.polar.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class PolarTags
{
    public static void initialise()
    {
        //Dummy method to force static init
    }

    @SuppressWarnings("unused")
    private static TagKey<Item> registerItemTag(String name)
    {
        return TagKey.of(Registry.ITEM_KEY, Polar.id(name));
    }

    @SuppressWarnings("unused")
    private static TagKey<Block> registerBlockTag(String name)
    {
        return TagKey.of(Registry.BLOCK_KEY, Polar.id("name"));
    }
}
