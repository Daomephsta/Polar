package io.github.daomephsta.polar.common.components;

import dev.onyxstudios.cca.api.v3.component.ComponentFactory;
import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.config.PolarConfig;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class PolarChargeStorageComponent
{   
	public static void register(ItemComponentFactoryRegistry registry)
	{
        registry.register(ItemRegistry.FALLING_BLOCK_STABILISER, PolarAPI.CHARGE_STORAGE,  
	        Simple.forItem(Polarity.RED, PolarConfig.POLAR_CONFIG.charge().fallingBlockStabiliserMaxCharge(), 0));
        registry.register(ItemRegistry.FALLING_BLOCK_DESTROYER, PolarAPI.CHARGE_STORAGE, 
	        Simple.forItem(Polarity.RED, PolarConfig.POLAR_CONFIG.charge().fallingBlockDestroyerMaxCharge(), 0));
	}
	
    @SuppressWarnings("unchecked")
    public static <A extends ItemComponent & IPolarChargeStorage> ComponentFactory<ItemStack, A> 
        forItem(IPolarChargeStorage delegate)
    {
        return stack -> (A) new ItemAdapter(stack, delegate);
    }
	
	public static class Simple implements IPolarChargeStorage
	{
		private final Polarity polarity;
		private final int maxCharge;
		private int storedCharge;

		public Simple(Polarity polarity, int maxCharge)
		{
			this(polarity, maxCharge, 0);
		}

		public Simple(Polarity polarity, int maxCharge, int initialCharge)
		{
			this.polarity = polarity;
			this.maxCharge = maxCharge;
			this.storedCharge = initialCharge;
		}

        public static <A extends ItemComponent & IPolarChargeStorage> ComponentFactory<ItemStack, A> 
		    forItem(Polarity polarity, int maxCharge, int initialCharge)
        {
            return PolarChargeStorageComponent.forItem(new Simple(polarity, maxCharge, initialCharge));
        }

        @Override
		public int charge(Polarity polarity, int maxAmount, boolean simulate)
		{
			if (!canCharge() || this.polarity != polarity) return maxAmount;
			int insertedCharge = Math.min(maxCharge - storedCharge, maxAmount);
			if (!simulate) storedCharge += insertedCharge;
			return maxAmount - insertedCharge;
		}

		@Override
		public int discharge(Polarity polarity, int maxAmount, boolean simulate)
		{
			if (!canDischarge() || this.polarity != polarity) return 0;
			int extractedCharge = Math.min(storedCharge, maxAmount);
			if (!simulate) storedCharge -= extractedCharge;
			return extractedCharge;
		}

		@Override
		public int getStoredCharge()
		{
			return storedCharge;
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
		
		@Override
		public void readFromNbt(NbtCompound nbt)
		{
			this.storedCharge = nbt.getInt("stored_charge");
		}

		@Override
		public void writeToNbt(NbtCompound nbt)
		{
			nbt.putInt("stored_charge", this.storedCharge);
		}
	}
	
    private static class ItemAdapter extends ItemComponent implements IPolarChargeStorage
    {
        private final IPolarChargeStorage delegate;

        ItemAdapter(ItemStack stack, IPolarChargeStorage delegate)
        {
            super(stack);
            this.delegate = delegate;
            delegate.writeToNbt(getOrCreateRootTag());
        }

        public boolean canCharge()
        {
            return delegate.canCharge();
        }

        public int charge(Polarity polarity, int maxAmount, boolean simulate)
        {
            return delegate.charge(polarity, maxAmount, simulate);
        }

        public boolean canDischarge()
        {
            return delegate.canDischarge();
        }

        public int discharge(Polarity polarity, int maxAmount, boolean simulate)
        {
            return delegate.discharge(polarity, maxAmount, simulate);
        }

        public int getStoredCharge()
        {
            return delegate.getStoredCharge();
        }

        public Polarity getPolarity()
        {
            return delegate.getPolarity();
        }

        public int getMaxCharge()
        {
            return delegate.getMaxCharge();
        }

        @Override
        public void onTagInvalidated()
        {
            super.onTagInvalidated();
            delegate.readFromNbt(getOrCreateRootTag());
        }
    }
}
