package leviathan143.polar.api.internal;

import leviathan143.polar.api.IInternalMethodAccessors;
import leviathan143.polar.api.Polarity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class DummyAccessors implements IInternalMethodAccessors
{
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Block output) {}
	
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Item output) {}
	
	@Override
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, ItemStack output) {}
}
