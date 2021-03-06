package io.github.daomephsta.polar.common.handlers.wearables;

import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.callbacks.PlayerBreakBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WearablesHandler
{
    public static void initialise()
    {
        registerEventCallbacks();
    }

    private static void registerEventCallbacks()
    {
        PlayerBreakBlockCallback.EVENT.register(WearablesHandler::handleBlockBreak);
    }

    private static boolean handleBlockBreak(World world, BlockPos pos, PlayerEntity player)
    {
        BlockState state = world.getBlockState(pos);
        boolean result = true;
        result &= FallingBlockStabiliserHandler.stabiliseFallingBlocks(world, player, pos, state);
        result &= FallingBlockDestroyerHandler.destroyFallingBlocks(world, player, pos, state);
        return result;
    }

    static <I extends Item & Trinket> ItemStack findEquippedWearable(PlayerEntity player, I wearable)
    {
        return TrinketsApi.getTrinketComponent(player).stream()
            .flatMap(trinkets -> trinkets.getEquipped(wearable).stream())
            .findFirst()
            .map(Pair::getRight)
            .orElse(ItemStack.EMPTY);
    }

    /**
     * Checks if there is enough Charge to activate {@code chargeable}. Notifies
     * {@code player} if the item cannot only be activated once more or can't be
     * activated, due to low charge.
     * 
     * @param polarity
     *            The polarity of Charge to check for.
     * @param cost
     *            The Charge cost to activate {@code chargeable}.
     * @param lowChargeThreshold
     *            The Charge level below which charge is considered to be low.
     *            Must be greater than {@code cost}.
     * @return true if the stored Charge in {@code chargeable} of polarity
     *         {@code polarity} is greater than {@code cost}.
     */
    static boolean checkCharge(PlayerEntity player, ItemStack chargeable, Polarity polarity, int cost, int lowChargeThreshold)
    {
        IPolarChargeStorage chargeStorage = PolarApi.CHARGE_STORAGE.get(chargeable);
        if (chargeStorage.discharge(polarity, cost, true) < cost)
        {
            player.sendMessage(new TranslatableText("polar.message.insufficient_charge", cost), true);
            return false;
        } 
        else if (chargeStorage.getStoredCharge() <= lowChargeThreshold)
            player.sendMessage(new TranslatableText("polar.message.low_charge", 
                chargeStorage.getStoredCharge() - cost), true);
        return true;
    }
}
