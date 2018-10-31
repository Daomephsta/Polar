package leviathan143.polar.common.items.itemblocks;

import daomephsta.umbra.nbt.NBTExtensions;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.blocks.BlockRegistry;
import leviathan143.polar.common.blocks.BlockRune;
import leviathan143.polar.common.blocks.BlockRune.Variant;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class ItemBlockRune extends ItemBlock
{
	public ItemBlockRune(Block block)
	{
		super(block);
	}
	
	public static ItemStack forVariant(BlockRune.Variant variant)
	{
		ItemStack stack = new ItemStack(BlockRegistry.RUNE);
		NBTTagCompound tag = new NBTTagCompound();
		NBTExtensions.setEnumConstant(tag, "variant", variant);
		stack.setTagCompound(tag);
		return stack;
	}
	
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] {Polar.TAB_RED, Polar.TAB_BLUE};
	}
	
	@Override
	public String getTranslationKey(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			return super.getTranslationKey(stack);
		return super.getTranslationKey(stack) + "." + getVariant(stack).getName() ;
	}
	
	public static Variant getVariant(ItemStack itemBlockStack)
	{
		return NBTExtensions.getEnumConstant(itemBlockStack.getTagCompound(), Variant.class, "variant");
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (!isInCreativeTab(tab)) return;
		if(tab == Polar.TAB_RED)
		{
			for (BlockRune.Variant redVariant : BlockRune.Variant.RED) 
			{
				items.add(forVariant(redVariant));
			}
		}
		else if(tab == Polar.TAB_BLUE)
		{
			for (BlockRune.Variant blueVariant : BlockRune.Variant.BLUE) 
			{
				items.add(forVariant(blueVariant));
			}
		}
	}
}
