package io.github.daomephsta.polar.common.handlers.wearables;

import java.util.ArrayDeque;
import java.util.Deque;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.capabilities.IPolarChargeStorage;
import io.github.daomephsta.polar.common.CompatibilityTags;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.config.PolarConfig;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FallingBlockDestroyerHandler
{	
// TODO implement block break callback
//	static void destroyFallingBlocks(BlockEvent.BreakEvent event)
//	{
//		boolean realPlayer = event.getPlayer() != null
//				&& !(event.getPlayer() instanceof FakePlayer);
//		if (!realPlayer)
//			return;
//
//		BlockPos posAbove = event.getPos().up();
//		BlockState stateAbove = event.getWorld().getBlockState(posAbove);
//		if (!isSoftUnstableBlock(event.getWorld(), stateAbove, posAbove))
//			return;
//		ItemStack baubleStack = WearablesHandler.findEquippedWearable(
//				event.getPlayer(), ItemRegistry.FALLING_BLOCK_DESTROYER);
//		if (baubleStack.isEmpty())
//			return;
//
//		destroyBlockColumn(event.getPlayer(), baubleStack, event.getWorld(),
//				posAbove);
//	}

	private static void destroyBlockColumn(PlayerEntity player,
			ItemStack baubleStack, World world, BlockPos columnBottomPos)
	{
		Deque<BlockPos> toDestroy = new ArrayDeque<>();
		{
			BlockPos pos = new BlockPos.Mutable(columnBottomPos);
			BlockState state = world.getBlockState(pos);
			while (isSoftUnstableBlock(world, state, pos)
					|| state.getBlock() == BlockRegistry.STABILISED_BLOCK)
			{
				toDestroy.push(pos);
				pos = pos.up();
				state = world.getBlockState(pos);
			}
			toDestroy.push(columnBottomPos.down());
		}
		
		//TODO Chargeables
		IPolarChargeStorage chargeable = port.Dummy.CHARGE_STORAGE;
		int cost = toDestroy.size()
				* PolarConfig.charge.percussiveDisintegratorActivationCost;
		if (WearablesHandler.checkCharge(player, baubleStack, Polarity.BLUE, cost,
				PolarConfig.charge.percussiveDisintegratorActivationCost * 8))
		{
			chargeable.discharge(Polarity.BLUE, cost, false);
			while (!toDestroy.isEmpty())
			{
				BlockPos pos = toDestroy.pop();
				destroyBlock(world, pos);
				ResidualPolarityHandler.itemActivated(baubleStack, player);
			}
		}

	}

	private static void destroyBlock(World world, BlockPos pos)
	{
		if (world instanceof ServerWorld)
			((ServerWorld) world).spawnParticles(
					ParticleTypes.EXPLOSION, pos.getX(), pos.getY(),
					pos.getZ(), 10, 0, 0, 0, 0.0D);
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.1F,
				(1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat())
						* 0.2F) * 0.7F);
		world.breakBlock(pos, true);
	}

	private static boolean isSoftUnstableBlock(World world, BlockState state,
			BlockPos pos)
	{
		boolean unstableBlock = CompatibilityTags.GRAVITY_AFFECTED.contains(state.getBlock());
		boolean softBlock = state.getHardness(world, pos) <= 0.6F;
		return unstableBlock && softBlock;
	}
}
