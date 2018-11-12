package leviathan143.polar.common.capabilities;

import daomephsta.umbra.capabilities.CapabilityHelper;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityPolarChargeable
{
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IPolarChargeStorage.class,
				CapabilityHelper.fromLambdas(
					(Capability<IPolarChargeStorage> capability, IPolarChargeStorage instance, EnumFacing side, NBTBase nbt) -> 
					{
						NBTTagCompound compoundNBT = (NBTTagCompound) nbt;
						instance.charge(instance.getPolarity(), compoundNBT.getInteger("stored_charge"), false);
					},
					(Capability<IPolarChargeStorage> capability, IPolarChargeStorage instance, EnumFacing side) -> 
					{
						NBTTagCompound nbt = new NBTTagCompound();
						nbt.setInteger("stored_charge", instance.getStoredCharge());
						return nbt;
					}), 
					() -> null);
	}

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
			if(!canCharge() || this.polarity != polarity) return maxAmount;
			int insertedCharge = Math.min(maxCharge - storedCharge, maxAmount);
			if(!simulate) storedCharge += insertedCharge;
			return maxAmount - insertedCharge;
		}
		
		@Override
		public int discharge(Polarity polarity, int maxAmount, boolean simulate)
		{
			if(!canDischarge() || this.polarity != polarity) return 0;
			int extractedCharge = Math.min(storedCharge, maxAmount);
			if(!simulate) storedCharge -= extractedCharge;
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
