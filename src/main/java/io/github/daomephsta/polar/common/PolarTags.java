package io.github.daomephsta.polar.common;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class PolarTags
{	
	public static void initialise()
	{
		//Dummy method to force static init
	}

	private static Tag<Item> registerItemTag(String name)
	{
		return TagRegistry.item(new Identifier(Polar.MOD_ID, name));
	}
	
	private static Tag<Block> registerBlockTag(String name)
	{
		return TagRegistry.block(new Identifier(Polar.MOD_ID, "name"));
	}
}
