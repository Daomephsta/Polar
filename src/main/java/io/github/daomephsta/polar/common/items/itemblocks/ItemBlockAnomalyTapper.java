package io.github.daomephsta.polar.common.items.itemblocks;

import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.Polarity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

//TODO Flatten
public class ItemBlockAnomalyTapper extends BlockItem implements IPolarisedItem
{
	private final Polarity polarity;
	
	public ItemBlockAnomalyTapper(Block block, Polarity polarity)
	{
		super(block, new Item.Settings().group(polarity.getItemGroup()));
		this.polarity = polarity;
	}
	
	@Override
	public Polarity getPolarity(ItemStack stack)
	{
		assert stack.getItem() == this;
		return polarity;
	}
	
	@Override
	public boolean activatesOn(ActivatesOn trigger)
	{
		return false;
	}
}
