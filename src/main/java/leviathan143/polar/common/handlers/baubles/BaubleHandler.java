package leviathan143.polar.common.handlers.baubles;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import leviathan143.polar.common.Polar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class BaubleHandler
{
	public static void preInit()
	{
		
	}
	
	@SubscribeEvent
	public static void handleBlockBreak(BlockEvent.BreakEvent event)
	{
		FallingBlockStabiliserHandler.stabiliseFallingBlocks(event);
		FallingBlockDestroyerHandler.destroyFallingBlocks(event);
	}
	
	static ItemStack findEquippedBauble(EntityPlayer player, IBauble bauble)
	{
		IItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (int s = 0; s < baubles.getSlots(); s++)
		{
			ItemStack stack = baubles.getStackInSlot(s);
			if (stack.getItem() == bauble)
				return stack;
		}
		return ItemStack.EMPTY;
	}
	
	/**
	 * Checks if there is enough Charge to activate {@code chargeable}. Notifies {@code player} 
	 * if the item cannot only be activated once more or can't be activated, due to low charge.
	 * @param polarity The polarity of Charge to check for.
	 * @param cost The Charge cost to activate {@code chargeable}.
	 * @param lowChargeThreshold The Charge level below which charge is considered to be low. 
	 * Must be greater than {@code cost}.
	 * @return 
	 * @return true if the stored Charge in {@code chargeable} of polarity {@code polarity}
	 * is greater than {@code cost}.
	 */
	static boolean checkCharge(EntityPlayer player, ItemStack chargeable, Polarity polarity, int cost, int lowChargeThreshold)
	{
		IPolarChargeStorage chargeStorage = chargeable.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
		if (chargeStorage.discharge(polarity, cost, true) < cost)
		{
			player.sendStatusMessage(new TextComponentTranslation("polar.message.insufficient_charge", cost), true);
			return false;
		}
		else if (chargeStorage.getStoredCharge() <= lowChargeThreshold)
			player.sendStatusMessage(new TextComponentTranslation("polar.message.low_charge", chargeStorage.getStoredCharge() - cost), true);
		return true;
	}
}
