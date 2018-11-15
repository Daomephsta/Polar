package leviathan143.polar.common.items.itemblocks;

import daomephsta.umbra.nbt.NBTExtensions;
import leviathan143.polar.api.*;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.blocks.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class ItemBlockAnomalyTapper extends ItemBlock implements IPolarisedItem
{
	public ItemBlockAnomalyTapper(Block block)
	{
		super(block);
	}

	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] {Polar.TAB_RED, Polar.TAB_BLUE};
	}

	public static ItemStack forPolarity(Polarity polarity)
	{
		if (!polarity.isPolarised())
			return ItemStack.EMPTY;

		ItemStack stack = new ItemStack(BlockRegistry.ANOMALY_TAPPER);
		NBTTagCompound tag = new NBTTagCompound();
		NBTExtensions.setEnumConstant(tag, CommonWords.POLARITY, polarity);
		stack.setTagCompound(tag);
		return stack;
	}
	
	@Override
	public Polarity getPolarity(ItemStack stack)
	{
		return getPolarityStatic(stack);
	}
	
	public static Polarity getPolarityStatic(ItemStack stack)
	{
		return NBTExtensions.getEnumConstant(stack.getTagCompound(), Polarity.class, CommonWords.POLARITY); 
	}
	
	@Override
	public boolean activatesOn(ItemStack stack, ActivatesOn trigger)
	{
		return false;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (!isInCreativeTab(tab)) return;
		if(tab == Polar.TAB_RED) items.add(forPolarity(Polarity.RED));
		else if(tab == Polar.TAB_BLUE) items.add(forPolarity(Polarity.BLUE));
	}
}
