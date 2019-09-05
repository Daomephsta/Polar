package io.github.daomephsta.polar.common.handlers.wearables;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import net.mcft.copy.wearables.api.IWearablesEntity;
import net.mcft.copy.wearables.api.IWearablesItem;
import net.mcft.copy.wearables.api.IWearablesSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

public class WearablesHandler
{
	public static void registerEventCallbacks()
	{
		
	}
	
// TODO implement block break callback
//	public static void handleBlockBreak(BlockEvent.BreakEvent event)
//	{
//		FallingBlockStabiliserHandler.stabiliseFallingBlocks(event);
//		FallingBlockDestroyerHandler.destroyFallingBlocks(event);
//	}

	static <I extends Item & IWearablesItem> ItemStack findEquippedWearable(PlayerEntity player, I wearable)
	{
		return IWearablesEntity.from(player).getEquippedWearables()
			.filter(slot -> slot.isValid() && slot.get().getItem() == wearable)
			.map(IWearablesSlot::get)
			.findFirst()
			.orElse(ItemStack.EMPTY);
	}

	/**
	 * Checks if there is enough Charge to activate {@code chargeable}. Notifies
	 * {@code player} if the item cannot only be activated once more or can't be
	 * activated, due to low charge.
	 * 
	 * @param polarity
	 *            The polarity of Charge to check for.
	 * @param cost
	 *            The Charge cost to activate {@code chargeable}.
	 * @param lowChargeThreshold
	 *            The Charge level below which charge is considered to be low.
	 *            Must be greater than {@code cost}.
	 * @return
	 * @return true if the stored Charge in {@code chargeable} of polarity
	 *         {@code polarity} is greater than {@code cost}.
	 */
	static boolean checkCharge(PlayerEntity player, ItemStack chargeable, Polarity polarity, int cost, int lowChargeThreshold)
	{
		IPolarChargeStorage chargeStorage = IPolarChargeStorage.get(chargeable);
		if (chargeStorage.discharge(polarity, cost, true) < cost)
		{
			player.addChatMessage(new TranslatableText("polar.message.insufficient_charge", cost), true);
			return false;
		} 
		else if (chargeStorage.getStoredCharge() <= lowChargeThreshold)
			player.addChatMessage(new TranslatableText("polar.message.low_charge", chargeStorage.getStoredCharge() - cost), true);
		return true;
	}
}
