package io.github.daomephsta.polar.api.capabilities;

import io.github.daomephsta.polar.api.Polarity;

public interface IPolarChargeStorage
{
	public default boolean canCharge()
	{
		return getStoredCharge() < getMaxCharge();
	}
	
	/**
	 * Inserts charge into storage, up to a maximum amount.
	 * This will always return {@code maxAmount} if {@link #canCharge()} returns false.
	 * @param polarity The polarity of charge to insert
	 * @param maxAmount The maximum amount of charge to insert.
	 * @param simulate If true, the extraction is simulated, there 
	 * is no change in stored charge.
	 * @return Any remaining charge that could not be inserted.
	 */
	public int charge(Polarity polarity, int maxAmount, boolean simulate);
	
	public default boolean canDischarge()
	{
		return getStoredCharge() > 0;
	}
	
	/**
	 * Extracts charge from storage, up to a maximum amount.
	 * This will always return 0 if {@link #canDischarge()} returns false.
	 * @param polarity The polarity of charge to extract.
	 * @param maxAmount The maximum amount of charge to extract.
	 * @param simulate If true, the extraction is simulated, there 
	 * is no change in stored charge.
	 * @return The actual amount of charge extracted.
	 */
	public int discharge(Polarity polarity, int maxAmount, boolean simulate);
	
	public int getStoredCharge();
	
	public Polarity getPolarity();
	
	public int getMaxCharge();
}
