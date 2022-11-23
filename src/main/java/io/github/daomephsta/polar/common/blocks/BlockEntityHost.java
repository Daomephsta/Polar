package io.github.daomephsta.polar.common.blocks;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.world.World;

/** Subinterface of BlockEntityProvider that reduces type checking and conversion boilerplate related to tickers.<br>
 *  This interface should be implemented by the {@code Block}.<br>
 *  The {@code BlockEntity} must implement {@link BlockEntityHost.Hosted}.
 *  @param <T> class of the block entity hosted by this block
 *  @author Daomephsta
 */
public interface BlockEntityHost<T extends BlockEntity & BlockEntityHost.Hosted> extends BlockEntityProvider
{
    @Override
    public default <U extends BlockEntity> BlockEntityTicker<U> getTicker(
        World world, BlockState state, BlockEntityType<U> actualType)
    {
        if (actualType == getBlockEntityType())
            return (a, b, c, blockEntity) -> ((Hosted) blockEntity).tick();
        return null;
    }

    public BlockEntityType<T> getBlockEntityType();

    /**
     * Implement this interface on any {@code BlockEntity} hosted by a
     * {@code Block} that implements {@code BlockEntityHost}
     * @author Daomephsta
     */
    public interface Hosted
    {
        public void tick();
    }
}
