package leviathan143.polar.api;

import net.minecraft.item.ItemStack;

/**
 * Interface that declares an item has a polarity.
 * @author Daomephsta
 */
public interface IPolarisedItem
{	
	public static enum ActivatesOn
	{
		WEARER_ATTACKED,
		WEARER_ATTACK,
		ITEM_RIGHT_CLICK,
		BLOCK_LEFT_CLICK,
		BLOCK_RIGHT_CLICK,
		ENTITY_RIGHT_CLICK;
	}

	/**
	 * @param stack The stack this item is in.
	 * @return The polarity of {@code stack}.
	 */
	public Polarity getPolarity(ItemStack stack);
	
	/**
	 * <b>Note:</b> This is only for checking if the item will always activate
	 * under certain circumstances.
	 * @param trigger The activation trigger to check for.
	 * @return true if this item activates when {@code trigger} occurs.
	 */
	public boolean activatesOn(ActivatesOn trigger);
}
