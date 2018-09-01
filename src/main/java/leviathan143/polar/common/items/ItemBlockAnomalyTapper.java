package leviathan143.polar.common.items;

import leviathan143.polar.api.CommonWords;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.Polar;
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
	
	@Override
	public ItemStack getPolarisedStack(Polarity polarity)
	{
		switch(polarity)
		{
		case BLUE:
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString(CommonWords.POLARITY, Polarity.BLUE.name());
			ItemStack stack = new ItemStack(this);
			stack.setTagCompound(tag);
			return stack;
		}
		case RED:
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString(CommonWords.POLARITY, Polarity.RED.name());
			ItemStack stack = new ItemStack(this);
			stack.setTagCompound(tag);
			return stack;
		}
		default:
			return ItemStack.EMPTY;
		} 
	}
	
	@Override
	public Polarity[] getValidPolarities()
	{
		return Polarity.POLARISED;
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		if (!isInCreativeTab(tab)) return;
		if(tab == Polar.TAB_RED) items.add(getPolarisedStack(Polarity.RED));
		else if(tab == Polar.TAB_BLUE) items.add(getPolarisedStack(Polarity.BLUE));
	}
}
