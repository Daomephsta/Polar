package io.github.daomephsta.polar.common.items.blue;

import static io.github.daomephsta.polar.common.config.PolarConfig.POLAR_CONFIG;

import java.util.List;

import dev.emi.trinkets.api.ITrinket;
import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent.SimplePolarChargeStorage;
import io.github.daomephsta.polar.common.handlers.wearables.WearablesHandler;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class FallingBlockDestroyer extends Item implements IPolarisedItem, ITrinket
{
	public FallingBlockDestroyer()
	{
		super(new Item.Settings()
				.maxCount(1)
				.group(PolarAPI.TAB_BLUE));
		SimplePolarChargeStorage.setupFor(this, Polarity.BLUE, POLAR_CONFIG.charge().fallingBlockDestroyerMaxCharge());
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext)
	{
		super.appendTooltip(stack, world, tooltip, tooltipContext);
		IPolarChargeStorage chargeable = IPolarChargeStorage.get(stack);
		tooltip.add(new TranslatableText(Polar.MOD_ID + ".tooltip.charge", chargeable.getStoredCharge(), chargeable.getMaxCharge()));
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

	@Override
	public boolean canWearInSlot(String group, String slot)
	{
		return WearablesHandler.isNecklaceSlot(group, slot);
	}
}
