package leviathan143.polar.common.items;

import leviathan143.polar.api.Polarity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IPolarisedItem
{
	public ItemStack getPolarisedStack(Polarity polarity);
	
	public Polarity[] getValidPolarities();
	
	public Polarity getPolarity(ItemStack stack);
	
	public static ItemStack getPolarisedStack(Block polarisedBlock, Polarity polarity)
	{
		return getPolarisedStack(Item.getItemFromBlock(polarisedBlock), polarity);
	}
	
	public static ItemStack getPolarisedStack(Item polarisedItem, Polarity polarity)
	{
		if (polarisedItem instanceof IPolarisedItem == false) throw new IllegalArgumentException(polarisedItem + " is not an instance of IPolarisedItem");
		return ((IPolarisedItem) polarisedItem).getPolarisedStack(polarity);
	}
}
