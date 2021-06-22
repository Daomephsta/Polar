package io.github.daomephsta.polar.common.items;

import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class ChargeBar
{
    private final int colour;

    public ChargeBar(Polarity polarity)
    {
        this.colour = switch (polarity)
        {
            case RED -> MathHelper.packRgb(227, 66, 52);
            case BLUE -> MathHelper.packRgb(0, 128, 255);
            default -> throw new IllegalArgumentException("Unexpected value: " + polarity);
        };
    }

    public boolean isVisible(ItemStack itemStack)
    {
        return true;
    }

    public int getValue(ItemStack stack)
    {
        IPolarChargeStorage chargeable = PolarApi.CHARGE_STORAGE.get(stack);
        // storedCharge units of 1 / damageBarWidth / maxCharge and rounded
        return Math.round(chargeable.getStoredCharge() * 13.0F / chargeable.getMaxCharge());
    }

    public int getColour(ItemStack stack)
    {
        return colour;
    }
}
