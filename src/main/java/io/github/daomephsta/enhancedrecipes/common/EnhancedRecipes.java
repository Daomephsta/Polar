package io.github.daomephsta.enhancedrecipes.common;

import io.github.daomephsta.enhancedrecipes.common.recipes.EnhancedShapedRecipe;
import io.github.daomephsta.enhancedrecipes.common.recipes.EnhancedShapelessRecipe;
import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import io.github.daomephsta.enhancedrecipes.common.recipes.processors.AddAttributeModifiersRecipeProcessor;
import io.github.daomephsta.enhancedrecipes.common.recipes.processors.AddNbtRecipeProcessor;
import io.github.daomephsta.enhancedrecipes.common.recipes.processors.LimitApplicationsRecipeProcessor;
import io.github.daomephsta.enhancedrecipes.common.recipes.processors.MarkOutputRecipeProcessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnhancedRecipes implements ModInitializer
{
    public static final String MOD_ID = "enhanced_recipes";

    @Override
    public void onInitialize()
    {
        initialise();
    }

    public static void initialise()
    {
        registerRecipeSerialiser("crafting_shaped", EnhancedShapedRecipe.SERIALIZER);
        registerRecipeSerialiser("crafting_shapeless", EnhancedShapelessRecipe.SERIALIZER);
        
        registerRecipeProcessorSerialiser("mark_output", MarkOutputRecipeProcessor.SERIALISER);
        registerRecipeProcessorSerialiser("add_nbt", AddNbtRecipeProcessor.SERIALISER);
        registerRecipeProcessorSerialiser("add_attribute_modifiers", AddAttributeModifiersRecipeProcessor.SERIALISER);
        registerRecipeProcessorSerialiser("limit_applications", LimitApplicationsRecipeProcessor.SERIALISER);
    }

    private static void registerRecipeSerialiser(String name, RecipeSerializer<?> serialiser)
    {
        Registry.register(Registry.RECIPE_SERIALIZER, id(name), serialiser);
    }
    
    private static void registerRecipeProcessorSerialiser(String name, RecipeProcessor.Serialiser<?> serialiser)
    {
        Registry.register(RecipeProcessor.REGISTRY, id(name), serialiser);
    }

    public static Identifier id(String name)
    {
        return new Identifier(MOD_ID, name);
    }
}
