package leviathan143.polar.common.items;

import leviathan143.polar.api.Polarity;
import net.minecraft.item.ItemStack;

public interface IPolarisedItem
{	
	public Polarity getPolarity(ItemStack stack);
}
