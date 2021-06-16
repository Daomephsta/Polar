package io.github.daomephsta.polar.common.handlers.wearables;

import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.CompatibilityTags;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import io.github.daomephsta.polar.common.tileentities.StabilisedBlockBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FallingBlockStabiliserHandler
{
    static boolean stabiliseFallingBlocks(World world, PlayerEntity player, BlockPos pos, BlockState state)
    {
        BlockState stateAbove = world.getBlockState(pos.up());
        boolean unstableBlock = isUnstableBlock(stateAbove);
        if (!unstableBlock)
            return true;
        ItemStack wearableStack = WearablesHandler.findEquippedWearable(player, ItemRegistry.FALLING_BLOCK_STABILISER);
        if (wearableStack.isEmpty())
            return true;
        FallingBlockStabiliserHandler.placeStabilisedBlock(player, wearableStack, world, pos.up(), stateAbove);
        return true;
    }

    public static boolean isUnstableBlock(BlockState state)
    {
        return CompatibilityTags.GRAVITY_AFFECTED.contains(state.getBlock());
    }

    public static boolean placeStabilisedBlock(PlayerEntity player, ItemStack wearableStack, World world, BlockPos pos, BlockState camo)
    {
        IPolarChargeStorage chargeable = PolarApi.CHARGE_STORAGE.get(wearableStack);
        int cost = Polar.CONFIG.charge.fallingBlockStabiliserActivationCost();
        if (WearablesHandler.checkCharge(player, wearableStack, Polarity.RED, cost, Polar.CONFIG.charge.fallingBlockStabiliserActivationCost() * 8))
        {
            chargeable.discharge(Polarity.RED, cost, false);
            ResidualPolarityHandler.itemActivated(wearableStack, player);
            world.setBlockState(pos, BlockRegistry.STABILISED_BLOCK.stabilise(camo));
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof StabilisedBlockBlockEntity)
                ((StabilisedBlockBlockEntity) be).setCamoBlockState(camo);
            return true;
        }
        return false;
    }
}
