package leviathan143.polar.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/** An abstraction over internal method calls, so that they do not require a hard
 * dependency on Polar
 * @author Daomephsta
 */
public interface IInternalMethodAccessors
{
	/**
	 * Convenience method for registering an irradiation recipe with a block output
	 * @see #registerRecipe(Polarity, Ingredient, ItemStack)
	 **/
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Block output);
	
	/**
	 * Convenience method for registering an irradiation recipe with an item output
	 * @see #registerRecipe(Polarity, Ingredient, ItemStack)
	 **/
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Item output);
	
	/**
	 * Registers an irradiation recipe for the given polarity with the given input and output.
	 * If multiple recipes match a stack, the first recipe found will be used.
	 * @param polarity the anomaly polarity required to craft this recipe. 
	 * Valid values are {@code Polarity.RED} and {@code Polarity.BLUE}.
	 * @param input the input Ingredient for the recipe
	 * @param output the output stack for the recipe   
	 **/
	public void registerIrradiationRecipe(Polarity polarity, Ingredient input, ItemStack output);
}
