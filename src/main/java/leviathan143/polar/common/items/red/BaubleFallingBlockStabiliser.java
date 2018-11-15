package leviathan143.polar.common.items.red;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import leviathan143.polar.api.*;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.capabilities.CapabilityPolarChargeable.SimplePolarChargeableProvider;
import leviathan143.polar.common.config.PolarConfig;
import leviathan143.polar.common.handlers.ResidualPolarityHandler;
import leviathan143.polar.common.handlers.baubles.FallingBlockStabiliserHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class BaubleFallingBlockStabiliser extends Item implements IBauble, IPolarisedItem
{
	public BaubleFallingBlockStabiliser()
	{
		setMaxStackSize(1);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new SimplePolarChargeableProvider(Polarity.RED, PolarConfig.charge.graviticStabiliserMaxCharge);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack heldItem = player.getHeldItem(hand);
		if (FallingBlockStabiliserHandler.placeStabilisedBlock(player, heldItem, world, pos, world.getBlockState(pos)))
		{
			ResidualPolarityHandler.itemActivated(heldItem, player);
			return EnumActionResult.SUCCESS;
		}
		else return EnumActionResult.FAIL;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		IPolarChargeStorage chargeable = stack.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
		tooltip.add(I18n.format(Polar.MODID + ".tooltip.charge", chargeable.getStoredCharge(), chargeable.getMaxCharge()));
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		IPolarChargeStorage chargeable = stack.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
		return 1.0D - chargeable.getStoredCharge() / (double) chargeable.getMaxCharge();
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack)
	{
		return FactionAlignment.RED.getFactionColour();
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack)
	{
		return BaubleType.TRINKET;
	}
	
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
}
