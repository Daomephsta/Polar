package io.github.daomephsta.polar.common.items.red;

import static io.github.daomephsta.polar.common.config.PolarConfig.POLAR_CONFIG;

import java.util.List;

import dev.emi.trinkets.api.ITrinket;
import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.components.PolarChargeStorageComponent.SimplePolarChargeStorage;
import io.github.daomephsta.polar.common.handlers.ResidualPolarityHandler;
import io.github.daomephsta.polar.common.handlers.wearables.FallingBlockStabiliserHandler;
import io.github.daomephsta.polar.common.handlers.wearables.WearablesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class FallingBlockStabiliser extends Item implements IPolarisedItem, ITrinket
{
	public FallingBlockStabiliser()
	{
		super(new Item.Settings()
				.group(PolarAPI.TAB_RED));
		SimplePolarChargeStorage.setupFor(this, Polarity.RED, POLAR_CONFIG.charge().fallingBlockStabiliserMaxCharge());
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext itemUsageContext)
	{
		ItemStack heldItem = itemUsageContext.getPlayer().getStackInHand(itemUsageContext.getHand());
		BlockState blockState = itemUsageContext.getWorld().getBlockState(itemUsageContext.getBlockPos());
		if (FallingBlockStabiliserHandler.isUnstableBlock(blockState) 
			&& FallingBlockStabiliserHandler.placeStabilisedBlock(itemUsageContext.getPlayer(), heldItem, itemUsageContext.getWorld(), itemUsageContext.getBlockPos(), blockState))
		{
			ResidualPolarityHandler.itemActivated(heldItem, itemUsageContext.getPlayer());
			return ActionResult.SUCCESS;
		}
		else return ActionResult.FAIL;
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
		return Polarity.RED;
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
