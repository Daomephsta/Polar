package io.github.daomephsta.polar.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CompatibilityTags
{
    public static final TagKey<Item> MELEE_WEAPONS = registerItemTag("melee_weapons"),
                                  SWORDS = registerItemTag("swords"),
                                  AXES = registerItemTag("axes"),
                                  HEAD_EQUIPMENT = registerItemTag("head_equipment"),
                                  CHEST_EQUIPMENT = registerItemTag("chest_equipment"),
                                  LEGS_EQUIPMENT = registerItemTag("legs_equipment"),
                                  FEET_EQUIPMENT = registerItemTag("feet_equipment");
    public static final TagKey<Block> GRAVITY_AFFECTED = registerBlockTag("gravity_affected");

    public static void initialise()
    {
        //Dummy method to force static init
    }

    private static TagKey<Item> registerItemTag(String name)
    {
        return TagKey.of(Registry.ITEM_KEY, new Identifier("c", name));
    }

    private static TagKey<Block> registerBlockTag(String name)
    {
        return TagKey.of(Registry.BLOCK_KEY, new Identifier("c", name));
    }
}
