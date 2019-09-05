package io.github.daomephsta.polar.common.items.blue;

import java.util.List;

import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.capabilities.IPolarChargeStorage;
import io.github.daomephsta.polar.common.Polar;
import net.mcft.copy.wearables.api.IWearablesItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class FallingBlockDestroyer extends Item implements IPolarisedItem, IWearablesItem
{
	public FallingBlockDestroyer()
	{
		super(new Item.Settings()
				.maxCount(1)
				.group(PolarAPI.TAB_BLUE));
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext)
	{
		super.appendTooltip(stack, world, tooltip, tooltipContext);
		//TODO Charge
		IPolarChargeStorage chargeable = port.Dummy.CHARGE_STORAGE;
		tooltip.add(new TranslatableText(Polar.MODID + ".tooltip.charge", chargeable.getStoredCharge(), chargeable.getMaxCharge()));
	}
	
	//TODO Implement coloured charge bars
	
	@Override
	public Polarity getPolarity(ItemStack stack)
	{
		return Polarity.BLUE;
	}
	
	@Override
	public boolean activatesOn(ActivatesOn trigger)
	{
		return false;
	}
}
