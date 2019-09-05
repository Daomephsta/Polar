package io.github.daomephsta.polar.common.items.itemblocks;

import io.github.daomephsta.polar.common.NBTExtensions;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.blocks.BlockRune;
import io.github.daomephsta.polar.common.blocks.BlockRune.Variant;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

public class ItemBlockRune extends BlockItem
{
	public ItemBlockRune(Block block)
	{
		super(block, new Item.Settings());
	}
	
	public static ItemStack forVariant(BlockRune.Variant variant)
	{
		ItemStack stack = new ItemStack(BlockRegistry.RUNE);
		CompoundTag tag = new CompoundTag();
		NBTExtensions.putEnumConstant(tag, "variant", variant);
		stack.setTag(tag);
		return stack;
	}
	
	@Override
	public String getTranslationKey(ItemStack stack)
	{
		if (!stack.hasTag())
			return super.getTranslationKey(stack);
		return super.getTranslationKey(stack) + "." + getVariant(stack).asString();
	}
	
	public static Variant getVariant(ItemStack itemBlockStack)
	{
		return NBTExtensions.getEnumConstant(itemBlockStack.getTag(), Variant.class, "variant");
	}
	
	@Override
	public void appendStacks(ItemGroup itemGroup, DefaultedList<ItemStack> stacks)
	{
		if (!isIn(itemGroup)) return;
		if(itemGroup == Polar.TAB_RED)
		{
			for (BlockRune.Variant redVariant : BlockRune.Variant.RED) 
			{
				stacks.add(forVariant(redVariant));
			}
		}
		else if(itemGroup == Polar.TAB_BLUE)
		{
			for (BlockRune.Variant blueVariant : BlockRune.Variant.BLUE) 
			{
				stacks.add(forVariant(blueVariant));
			}
		}
	}
}
