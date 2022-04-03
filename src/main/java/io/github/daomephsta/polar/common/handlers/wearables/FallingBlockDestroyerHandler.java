package io.github.daomephsta.polar.common.handlers.wearables;

import java.util.ArrayDeque;
import java.util.Deque;

import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.CompatibilityTags;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FallingBlockDestroyerHandler
{
    static boolean destroyFallingBlocks(World world, PlayerEntity player, BlockPos pos, BlockState state)
    {
        BlockState stateAbove = world.getBlockState(pos.up());
        if (!isSoftUnstableBlock(world, stateAbove, pos.up()))
            return true;
        ItemStack baubleStack = WearablesHandler.findEquippedWearable(player, ItemRegistry.FALLING_BLOCK_DESTROYER);
        if (baubleStack.isEmpty())
            return true;
        destroyBlockColumn(player, baubleStack, world, pos.up(2));
        return true;
    }

    private static void destroyBlockColumn(PlayerEntity player, ItemStack wearableStack, World world, BlockPos columnBottomPos)
    {
        Deque<BlockPos> toDestroy = new ArrayDeque<>();
        {
            BlockPos pos = columnBottomPos.mutableCopy();
            BlockState state = world.getBlockState(pos);
            while (isSoftUnstableBlock(world, state, pos) || state.getBlock() == BlockRegistry.STABILISED_BLOCK)
            {
                toDestroy.push(pos);
                pos = pos.up();
                state = world.getBlockState(pos);
            }
            toDestroy.push(columnBottomPos.down());
        }

        IPolarChargeStorage chargeable = PolarApi.CHARGE_STORAGE.get(wearableStack);
        int cost = toDestroy.size() * Polar.CONFIG.charge.fallingBlockDestroyerActivationCost();
        if (WearablesHandler.checkCharge(player, wearableStack, Polarity.BLUE, cost, Polar.CONFIG.charge.fallingBlockDestroyerActivationCost() * 8))
        {
            chargeable.discharge(Polarity.BLUE, cost, false);
            while (!toDestroy.isEmpty())
            {
                BlockPos pos = toDestroy.pop();
                destroyBlock(world, pos);
                ResidualPolarityHandler.itemActivated(wearableStack, player);
            }
        }
    }

    private static void destroyBlock(World world, BlockPos pos)
    {
        if (world.getRandom().nextBoolean())
        {
            double x = pos.getX() + world.getRandom().nextDouble();
            double z = pos.getZ() + world.getRandom().nextDouble();
            world.addParticle(ParticleTypes.EXPLOSION, x, pos.getY(), z, 0.0D, 0.0D, 0.0D);
        }

        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.1F,
                (1.0F + (world.getRandom().nextFloat() - world.getRandom().nextFloat())
                        * 0.2F) * 0.7F);
        world.breakBlock(pos, true);
    }

    private static boolean isSoftUnstableBlock(World world, BlockState state, BlockPos pos)
    {
        boolean unstableBlock = state.isIn(CompatibilityTags.GRAVITY_AFFECTED);
        boolean softBlock = state.getHardness(world, pos) <= 0.6F;
        return unstableBlock && softBlock;
    }
}
