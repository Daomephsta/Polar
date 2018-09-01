package leviathan143.polar.api.capabilities;

import leviathan143.polar.api.Polarity;

public interface ITappable
{
	public int extract(Polarity polarity, int amount, boolean simulate);
	
	public boolean canExtract();
	
	public int getStoredCharge();
}
