package io.github.daomephsta.polar.common;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CompatibilityTags
{
	public static final Tag<Item> MELEE_WEAPONS = registerItemTag("melee_weapons"),
								  SWORDS = registerItemTag("swords"),
								  AXES = registerItemTag("axes"),
								  HEAD_EQUIPMENT = registerItemTag("head_equipment"),
								  CHEST_EQUIPMENT = registerItemTag("chest_equipment"),
								  LEGS_EQUIPMENT = registerItemTag("legs_equipment"),
								  FEET_EQUIPMENT = registerItemTag("feet_equipment");
	public static final Tag<Block> GRAVITY_AFFECTED = registerBlockTag("gravity_affected");
	
	public static void initialise()
	{
		//Dummy method to force static init
	}

	private static Tag<Item> registerItemTag(String name)
	{
		return TagRegistry.item(new Identifier("c", name));
	}
	
	private static Tag<Block> registerBlockTag(String name)
	{
		return TagRegistry.block(new Identifier("c", name));
	}
}
