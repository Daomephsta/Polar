package io.github.daomephsta.polar.common.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**TODO PR to cotton-player-events or fabric-api
 * Callback for fully breaking a block. 
 */
public interface PlayerBreakBlockCallback
{
	public static final Event<PlayerBreakBlockCallback> EVENT = EventFactory.createArrayBacked(PlayerBreakBlockCallback.class, 
			(callbacks) -> (world, pos, player) ->
			{
				for (PlayerBreakBlockCallback callback : callbacks)
				{
					if (!callback.preBreakBlock(world, pos, player))
						return false;
				}
				return true;
			});
	
	/**
	 * Called before a block is broken
	 * @param world the world the block is in  
	 * @param pos the position of the block
	 * @param playerEntity the player that broke the block
	 * @return true to continue with processing as normal, false to cancel further processing.
	 */
	public boolean preBreakBlock(World world, BlockPos pos, PlayerEntity playerEntity);
}