package io.github.daomephsta.polar.common.entities.anomalies;

import java.util.ArrayList;
import java.util.Collection;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;

public class AnomalyIrradiationCrafting
{
	private static final Collection<Recipe> redRecipes = new ArrayList<>(), 
											blueRecipes = new ArrayList<>();
	static
	{
		//Register default recipes
		registerRecipe(Polarity.RED, Ingredient.ofItems(Items.REDSTONE), ItemRegistry.RED_RESOURCE_BASIC);
		registerRecipe(Polarity.RED, Ingredient.ofStacks(new ItemStack(Blocks.REDSTONE_BLOCK)), 
			new ItemStack(ItemRegistry.RED_RESOURCE_BASIC, 9));
		registerRecipe(Polarity.BLUE, Ingredient.ofStacks(new ItemStack(Items.LAPIS_LAZULI)),
			ItemRegistry.BLUE_RESOURCE_BASIC);
		registerRecipe(Polarity.BLUE, Ingredient.ofStacks(new ItemStack(Blocks.LAPIS_BLOCK)),
			new ItemStack(ItemRegistry.BLUE_RESOURCE_BASIC, 9));
	}
	
	/**Convenience method for registering an irradiation recipe with a block output
	 *@see #registerRecipe(Polarity, Ingredient, ItemStack)*/
	public static void registerRecipe(Polarity polarity, Ingredient input, Block output)
	{
		registerRecipe(polarity, input, new ItemStack(output));
	}
	
	/**Convenience method for registering an irradiation recipe with an item output
	 *@see #registerRecipe(Polarity, Ingredient, ItemStack)*/
	public static void registerRecipe(Polarity polarity, Ingredient input, Item output)
	{
		registerRecipe(polarity, input, new ItemStack(output));
	}
	
	/**
	 * Registers an irradiation recipe for the given polarity with the given input and output.
	 * If multiple recipes match a stack, the first recipe found will be used.
	 * @param polarity the anomaly polarity required to craft this recipe. 
	 * Valid values are Polarity.RED and Polarity.BLUE.
	 * @param input the input Ingredient for the recipe
	 * @param output the output stack for the recipe   
	 */
	public static void registerRecipe(Polarity polarity, Ingredient input, ItemStack output)
	{
		switch(polarity)
		{
		case BLUE:
			blueRecipes.add(new Recipe(input, output));
			break;
		case RED:
			redRecipes.add(new Recipe(input, output));
			break;
		default:
			throw new IllegalArgumentException("Recipes can only be registered for red or blue polarities");
		}
	}
	
	/**
	 * Gets the output of irradiation crafting for the given stack and polarity.
	 * If multiple recipes match a stack, the first recipe found will be used.
	 * @param polarity the polarity in use for crafting
	 * @param input the stack to find a matching recipe input for
	 * @return the output of the first matching recipe, or {@link ItemStack#EMPTY} 
	 * if there is no matching recipe. The stack size will be the product of the stack size
	 * of <b>input</b> and the stack size of the output of the matching recipe.
	 */
	public static ItemStack getOutput(Polarity polarity, ItemStack input)
	{
		Collection<Recipe> recipes;
		if (polarity == Polarity.BLUE) recipes = blueRecipes;
		else if (polarity == Polarity.RED) recipes = redRecipes;
		else throw new IllegalArgumentException("Recipes only exist for red or blue polarities");
		
		for (Recipe recipe : recipes)
		{
			if (recipe.matches(input))
			{
				ItemStack output = recipe.getOutput().copy();
				output.setCount(input.getCount() * output.getCount());
				return output;
			}
		}
		return ItemStack.EMPTY;
	}
}

class Recipe
{
	private final Ingredient input;
	private final ItemStack output;
	
	Recipe(Ingredient input, ItemStack output)
	{
		this.input = input;
		this.output = output;
	}
	
	boolean matches(ItemStack stack)
	{
		return input.test(stack);
	}
	
	public ItemStack getOutput()
	{
		return output;
	}
}
