package io.github.daomephsta.polar.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class CompatibilityTags
{
    public static final TagKey<Item> MELEE_WEAPONS = itemTagKey("melee_weapons"),
                                  SWORDS = itemTagKey("swords"),
                                  AXES = itemTagKey("axes"),
                                  HEAD_EQUIPMENT = itemTagKey("head_equipment"),
                                  CHEST_EQUIPMENT = itemTagKey("chest_equipment"),
                                  LEGS_EQUIPMENT = itemTagKey("legs_equipment"),
                                  FEET_EQUIPMENT = itemTagKey("feet_equipment");
    public static final TagKey<Block> GRAVITY_AFFECTED = blockTagKey("gravity_affected");

    public static void initialise()
    {
        //Dummy method to force static init
    }

    private static TagKey<Item> itemTagKey(String name)
    {
        return tagKey(Registry.ITEM_KEY, name);
    }

    private static TagKey<Block> blockTagKey(String name)
    {
        return tagKey(Registry.BLOCK_KEY, name);
    }

    private static <T> TagKey<T> tagKey(RegistryKey<Registry<T>> registry, String name)
    {
        return TagKey.of(registry, new Identifier("c", name));
    }
}
