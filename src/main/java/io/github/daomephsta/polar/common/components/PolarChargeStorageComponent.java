package io.github.daomephsta.polar.common.components;

import dev.onyxstudios.cca.api.v3.component.ComponentFactory;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import net.minecraft.item.ItemStack;

public class PolarChargeStorageComponent
{
    public static void register(ItemComponentFactoryRegistry registry)
    {
        //Red
        registry.register(ItemRegistry.FALLING_BLOCK_STABILISER, PolarApi.CHARGE_STORAGE,
            SimpleItem.forItem(Polarity.RED, Polar.CONFIG.charge.fallingBlockStabiliserMaxCharge()));

        //Blue
        registry.register(ItemRegistry.FALLING_BLOCK_DESTROYER, PolarApi.CHARGE_STORAGE,
            SimpleItem.forItem(Polarity.BLUE, Polar.CONFIG.charge.fallingBlockDestroyerMaxCharge()));
    }

    public static class SimpleItem extends ItemComponent implements IPolarChargeStorage
    {
        private static final String STORED_CHARGE = "stored_charge";
        private final Polarity polarity;
        private final int maxCharge;

        public SimpleItem(ItemStack stack, Polarity polarity, int maxCharge)
        {
            super(stack, PolarApi.CHARGE_STORAGE);
            this.polarity = polarity;
            this.maxCharge = maxCharge;
        }

        public static ComponentFactory<ItemStack, SimpleItem> forItem(Polarity polarity, int maxCharge)
        {
            return stack -> new SimpleItem(stack, polarity, maxCharge);
        }

        @Override
        public int charge(Polarity polarity, int maxAmount, boolean simulate)
        {
            if (!canCharge() || this.polarity != polarity) return maxAmount;
            int insertedCharge = Math.min(maxCharge - getInt(STORED_CHARGE), maxAmount);
            if (!simulate)
            {
                putInt(STORED_CHARGE, getInt(STORED_CHARGE) + insertedCharge);
                PolarApi.CHARGE_STORAGE.sync(stack);
            }
            return maxAmount - insertedCharge;
        }

        @Override
        public int discharge(Polarity polarity, int maxAmount, boolean simulate)
        {
            if (!canDischarge() || this.polarity != polarity) return 0;
            int extractedCharge = Math.min(getInt(STORED_CHARGE), maxAmount);
            if (!simulate)
            {
                putInt(STORED_CHARGE, getInt(STORED_CHARGE) - extractedCharge);
                PolarApi.CHARGE_STORAGE.sync(stack);
            }
            return extractedCharge;
        }

        @Override
        public int getStoredCharge()
        {
            return getInt(STORED_CHARGE);
        }

        @Override
        public Polarity getPolarity()
        {
            return polarity;
        }

        @Override
        public int getMaxCharge()
        {
            return maxCharge;
        }
    }
}
