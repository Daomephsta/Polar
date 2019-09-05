package io.github.daomephsta.polar.common.capabilities;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.capabilities.IPolarChargeStorage;

//TODO CHarge storage
public class CapabilityPolarChargeable
{
	public static class SimplePolarChargeable implements IPolarChargeStorage
	{
		private final Polarity polarity;
		private final int maxCharge;
		private int storedCharge;

		public SimplePolarChargeable(Polarity polarity, int maxCharge)
		{
			this(polarity, maxCharge, 0);
		}

		public SimplePolarChargeable(Polarity polarity, int maxCharge, int initialCharge)
		{
			this.polarity = polarity;
			this.maxCharge = maxCharge;
			this.storedCharge = initialCharge;
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
	}
}
