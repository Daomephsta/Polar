package io.github.daomephsta.polar.common.recipes;

import io.github.daomephsta.enhancedrecipes.common.EnhancedRecipes;
import io.github.daomephsta.polar.common.Polar;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PolarRecipes
{
	public static void initialise()
	{
		EnhancedRecipes.initialise();
		registerRecipeSerialiser("crafting_special_charge_item", ChargeItemRecipe.SERIALIZER);
	}

	private static void registerRecipeSerialiser(String name, RecipeSerializer<?> serialiser)
	{
		Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(Polar.MOD_ID, name), serialiser);
	}
}
