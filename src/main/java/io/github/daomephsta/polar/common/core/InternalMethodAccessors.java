package io.github.daomephsta.polar.common.core;

import io.github.daomephsta.polar.api.IInternalMethodAccessors;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalyIrradiationCrafting;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

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
