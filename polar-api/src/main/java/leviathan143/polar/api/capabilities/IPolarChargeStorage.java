package leviathan143.polar.api.capabilities;

import leviathan143.polar.api.Polarity;

public interface IPolarChargeStorage
{
	public default boolean canCharge()
	{
		return getStoredCharge() < getMaxCharge();
	}
	
	public int charge(Polarity polarity, int maxAmount, boolean simulate);
	
	public default boolean canDischarge()
	{
		return getStoredCharge() > 0;
	}
	
	public int discharge(Polarity polarity, int maxAmount, boolean simulate);
	
	public int getStoredCharge();
	
	public Polarity getPolarity();
	
	public int getMaxCharge();
}
