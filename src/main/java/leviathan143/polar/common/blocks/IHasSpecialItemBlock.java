package leviathan143.polar.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.item.ItemBlock;

/* This interface declares that the block type that implements it needs a
 * custom ItemBlock or no ItemBlock, and is responsible for creating it */
public interface IHasSpecialItemBlock
{
	/**
	 * This method should return a new instance of ItemBlock or a subclass.
	 * It may also return null if the block should not have an ItemBlock
	 * 
	 * @return the instance of ItemBlock that should be registered for this
	 *         block
	 */
	@Nullable
	public ItemBlock createItemBlock();
}