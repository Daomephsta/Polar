package io.github.daomephsta.polar.api.components;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import nerdhub.cardinal.components.api.component.extension.CloneableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface IPolarChargeStorage extends CloneableComponent
{
	public static IPolarChargeStorage get(ItemStack stack)
	{
		return PolarAPI.CHARGE_STORAGE.get(ComponentProvider.fromItemStack(stack));
	}

	public static IPolarChargeStorage get(Entity entity)
	{
		return PolarAPI.CHARGE_STORAGE.get(ComponentProvider.fromEntity(entity));
	}
	
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
