package leviathan143.polar.common.entities;

import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.ITappable;
import leviathan143.polar.common.config.PolarConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class AnomalyTappingHandler implements ITappable, INBTSerializable<NBTTagCompound>
{
	private EntityAnomaly anomaly;
	private int charge;
	
	public AnomalyTappingHandler(EntityAnomaly anomaly, int initialCharge)
	{
		this.anomaly = anomaly;
		this.charge = initialCharge;
	}

	@Override
	public int extract(Polarity polarity, int maxAmount, boolean simulate)
	{
		if(!canExtract() || anomaly.getPolarity() != polarity) return 0;
		int extractedCharge = Math.min(charge, maxAmount);
		if(!simulate) charge -= extractedCharge;
		return extractedCharge;
	}

	@Override
	public boolean canExtract()
	{
		return true;
	}

	@Override
	public int getStoredCharge()
	{
		return charge;
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("charge", charge);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		charge = nbt.getInteger("charge");
	}
}
