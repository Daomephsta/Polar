package leviathan143.polar.common.core;

import leviathan143.polar.api.IInternalMethodAccessors;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.entities.anomalies.AnomalyIrradiationCrafting;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class InternalMethodAccessors implements IInternalMethodAccessors
{
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Block output) 
	{
		AnomalyIrradiationCrafting.registerRecipe(polarity, input, output);
	}
	
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Item output) 
	{
		AnomalyIrradiationCrafting.registerRecipe(polarity, input, output);
	}
	
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, ItemStack output) 
	{
		AnomalyIrradiationCrafting.registerRecipe(polarity, input, output);
	}
}
