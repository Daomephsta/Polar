package io.github.daomephsta.polar.common;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class PolarTags
{
    public static void initialise()
    {
        //Dummy method to force static init
    }


    @SuppressWarnings("unused")
    private static Tag<Item> registerItemTag(String name)
    {
        return TagFactory.ITEM.create(Polar.id(name));
    }

    @SuppressWarnings("unused")
    private static Tag<Block> registerBlockTag(String name)
    {
        return TagFactory.BLOCK.create(Polar.id("name"));
    }
}
