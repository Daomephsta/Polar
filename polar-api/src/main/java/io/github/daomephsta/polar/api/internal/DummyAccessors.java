package io.github.daomephsta.polar.api.internal;

import io.github.daomephsta.polar.api.IInternalMethodAccessors;
import io.github.daomephsta.polar.api.Polarity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

public class DummyAccessors implements IInternalMethodAccessors
{
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Block output) {}
	
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Item output) {}
	
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, ItemStack output) {}
}
