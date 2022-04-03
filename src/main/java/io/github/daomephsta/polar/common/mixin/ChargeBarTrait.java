package io.github.daomephsta.polar.common.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.items.blue.FallingBlockDestroyerItem;
import io.github.daomephsta.polar.common.items.red.FallingBlockStabiliserItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Mixin({FallingBlockDestroyerItem.class, FallingBlockStabiliserItem.class})
public abstract class ChargeBarTrait extends Item implements IPolarisedItem
{
    public ChargeBarTrait(Settings settings)
    {
        super(settings);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack)
    {
        return true;
    }

    @Override
    public int getItemBarStep(ItemStack stack)
    {
        IPolarChargeStorage chargeable = PolarApi.CHARGE_STORAGE.get(stack);
        // storedCharge units of 1 / damageBarWidth / maxCharge and rounded
        return Math.round(chargeable.getStoredCharge() * 13.0F / chargeable.getMaxCharge());
    }

    @Override
    public int getItemBarColor(ItemStack stack)
    {
        return switch (getPolarity(stack))
        {
            case RED -> MathHelper.packRgb(227, 66, 52);
            case BLUE -> MathHelper.packRgb(0, 128, 255);
            default -> super.getItemBarColor(stack);
        };
    }
}