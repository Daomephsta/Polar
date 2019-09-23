package io.github.daomephsta.polar.common.components;

import static io.github.daomephsta.polar.common.config.PolarConfig.POLAR_CONFIG;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import nerdhub.cardinal.components.api.component.extension.CloneableComponent;
import nerdhub.cardinal.components.api.event.EntityComponentCallback;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;

public class PolarChargeStorageComponent
{
	public static void register()
	{
		ItemComponentCallback.event(ItemRegistry.FALLING_BLOCK_DESTROYER)
			.register((stack, components) -> components.put(PolarAPI.CHARGE_STORAGE, new SimplePolarChargeStorage(Polarity.BLUE, POLAR_CONFIG.charge().fallingBlockDestroyerMaxCharge())));
		ItemComponentCallback.event(ItemRegistry.FALLING_BLOCK_STABILISER)
			.register(addChargeStorage(Polarity.RED, POLAR_CONFIG.charge().fallingBlockStabiliserMaxCharge()));
	}
	
	private static ItemComponentCallback addChargeStorage(Polarity polarity, int maxCharge)
	{
		return (stack, components) -> components.put(PolarAPI.CHARGE_STORAGE, new SimplePolarChargeStorage(polarity, maxCharge));
	}
	
	public static class SimplePolarChargeStorage implements IPolarChargeStorage
	{
		private final Polarity polarity;
		private final int maxCharge;
		private int storedCharge;

		public SimplePolarChargeStorage(Polarity polarity, int maxCharge)
		{
			this(polarity, maxCharge, 0);
		}

		public SimplePolarChargeStorage(Polarity polarity, int maxCharge, int initialCharge)
		{
			this.polarity = polarity;
			this.maxCharge = maxCharge;
			this.storedCharge = initialCharge;
		}
		
		public static void setupFor(Item item, Polarity polarity, int maxCharge)
		{
			ItemComponentCallback.event(item).register(
					(stack, components) -> components.put(PolarAPI.CHARGE_STORAGE, new SimplePolarChargeStorage(polarity, maxCharge)));
		}
		
		public static <E extends Entity> void setupFor(Class<E> entityClass, Polarity polarity, int maxCharge)
		{
			EntityComponentCallback.event(entityClass).register(
					(entity, components) -> components.put(PolarAPI.CHARGE_STORAGE, new SimplePolarChargeStorage(polarity, maxCharge)));
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
		public CloneableComponent newInstance()
		{
			return new SimplePolarChargeStorage(polarity, maxCharge, storedCharge);
		}

		@Override
		public void fromTag(CompoundTag tag)
		{
			this.storedCharge = tag.getInt("stored_charge");
		}

		@Override
		public CompoundTag toTag(CompoundTag tag)
		{
			tag.putInt("stored_charge", this.storedCharge);
			return tag;
		}
	}
}
