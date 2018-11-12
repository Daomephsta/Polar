package leviathan143.polar.api;

import net.minecraft.item.ItemStack;

public interface IPolarisedItem
{	
	public Polarity getPolarity(ItemStack stack);
}
