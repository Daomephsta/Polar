package leviathan143.polar.api;

import net.minecraft.item.ItemStack;

/**
 * Interface that declares an item has a polarity.
 * @author Daomephsta
 */
public interface IPolarisedItem
{	
	/**
	 * @param stack The stack this item is in.
	 * @return The polarity of {@code stack}.
	 */
	public Polarity getPolarity(ItemStack stack);
}
