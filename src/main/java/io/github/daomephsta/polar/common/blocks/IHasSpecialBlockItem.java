package io.github.daomephsta.polar.common.blocks;

import net.minecraft.item.BlockItem;

/* This interface declares that the block type that implements it needs a
 * custom BlockItem or no BlockItem, and is responsible for creating it */
public interface IHasSpecialBlockItem
{
    /**
     * This method should return a new instance of BlockItem or a subclass.
     * It may also return null if the block should not have an BlockItem
     * 
     * @return the instance of BlockItem that should be registered for this
     *         block, or null if none should be.
     */
    public BlockItem createBlockItem();
}