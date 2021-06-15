package io.github.daomephsta.polar.common;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class PolarTags
{    
    public static void initialise()
    {
        //Dummy method to force static init
    }

    private static Tag<Item> registerItemTag(String name)
    {
        return TagRegistry.item(Polar.id(name));
    }
    
    private static Tag<Block> registerBlockTag(String name)
    {
        return TagRegistry.block(Polar.id("name"));
    }
}
