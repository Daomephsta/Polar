package io.github.daomephsta.polar.common.items.blue;

import java.util.List;

import dev.emi.trinkets.api.TrinketItem;
import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.items.ChargeBar;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class FallingBlockDestroyerItem extends TrinketItem implements IPolarisedItem
{
    private final ChargeBar chargeBar = new ChargeBar(Polarity.BLUE);

    public FallingBlockDestroyerItem()
    {
        super(new Item.Settings()
                .maxCount(1)
                .group(PolarApi.TAB_BLUE));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext)
    {
        super.appendTooltip(stack, world, tooltip, tooltipContext);
        IPolarChargeStorage chargeable = PolarApi.CHARGE_STORAGE.get(stack);
        tooltip.add(Polar.translation("tooltip.charge", chargeable.getStoredCharge(), chargeable.getMaxCharge()));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack)
    {
        return chargeBar.isVisible(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack)
    {
        return chargeBar.getValue(stack);
    }

    @Override
    public int getItemBarColor(ItemStack stack)
    {
        return chargeBar.getColour(stack);
    }

    @Override
    public Polarity getPolarity(ItemStack stack)
    {
        return Polarity.BLUE;
    }

    @Override
    public boolean activatesOn(ActivatesOn trigger)
    {
        return false;
    }
}
