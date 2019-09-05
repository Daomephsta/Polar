package io.github.daomephsta.polar.common.items.itemblocks;

import io.github.daomephsta.polar.api.CommonWords;
import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.NBTExtensions;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

//TODO Flatten
public class ItemBlockAnomalyTapper extends BlockItem implements IPolarisedItem
{
	public ItemBlockAnomalyTapper(Block block)
	{
		super(block, new Item.Settings());
	}

	public static ItemStack forPolarity(Polarity polarity)
	{
		if (!polarity.isPolarised())
			return ItemStack.EMPTY;

		ItemStack stack = new ItemStack(BlockRegistry.ANOMALY_TAPPER);
		CompoundTag tag = new CompoundTag();
		NBTExtensions.putEnumConstant(tag, CommonWords.POLARITY, polarity);
		stack.setTag(tag);
		return stack;
	}
	
	@Override
	public Polarity getPolarity(ItemStack stack)
	{
		return getPolarityStatic(stack);
	}
	
	public static Polarity getPolarityStatic(ItemStack stack)
	{
		return NBTExtensions.getEnumConstant(stack.getTag(), Polarity.class, CommonWords.POLARITY); 
	}
	
	@Override
	public boolean activatesOn(ActivatesOn trigger)
	{
		return false;
	}

	@Override
	public void appendStacks(ItemGroup itemGroup, DefaultedList<ItemStack> stacks)
	{
		if (!isIn(group)) return;
		if(itemGroup == Polar.TAB_RED) stacks.add(forPolarity(Polarity.RED));
		else if(itemGroup == Polar.TAB_BLUE) stacks.add(forPolarity(Polarity.BLUE));
	}
}
