package io.github.daomephsta.polar.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class PolarTags
{
    public static void initialise()
    {
        //Dummy method to force static init
    }

    @SuppressWarnings("unused")
    private static TagKey<Item> itemTagKey(String name)
    {
        return tagKey(Registry.ITEM_KEY, name);
    }

    @SuppressWarnings("unused")
    private static TagKey<Block> blockTagKey(String name)
    {
        return tagKey(Registry.BLOCK_KEY, name);
    }

    private static <T> TagKey<T> tagKey(RegistryKey<Registry<T>> registry, String name)
    {
        return TagKey.of(registry, Polar.id(name));
    }
}
