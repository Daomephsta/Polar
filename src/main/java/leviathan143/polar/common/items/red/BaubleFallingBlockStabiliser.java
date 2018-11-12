package leviathan143.polar.common.items.red;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import leviathan143.polar.api.*;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.capabilities.CapabilityPolarChargeable;
import leviathan143.polar.common.config.PolarConfig;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.*;

public class BaubleFallingBlockStabiliser extends Item implements IBauble, IPolarisedItem
{
	public BaubleFallingBlockStabiliser()
	{
		setMaxStackSize(1);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt)
	{
		return new ICapabilitySerializable<NBTBase>()
		{
			private IPolarChargeStorage chargeable = 
				new CapabilityPolarChargeable.SimplePolarChargeable(Polarity.RED, PolarConfig.charge.graviticStabiliserMaxCharge);

			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing)
			{
				return capability == PolarAPI.CAPABILITY_CHARGEABLE;
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing)
			{
				if(capability == PolarAPI.CAPABILITY_CHARGEABLE) return PolarAPI.CAPABILITY_CHARGEABLE.cast(chargeable);
				return null;
			}

			@Override
			public NBTBase serializeNBT()
			{
				return PolarAPI.CAPABILITY_CHARGEABLE.getStorage().writeNBT(PolarAPI.CAPABILITY_CHARGEABLE, chargeable, null);
			}

			@Override
			public void deserializeNBT(NBTBase nbt)
			{
				PolarAPI.CAPABILITY_CHARGEABLE.getStorage().readNBT(PolarAPI.CAPABILITY_CHARGEABLE, chargeable, null, nbt);
			}
		};
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
}
