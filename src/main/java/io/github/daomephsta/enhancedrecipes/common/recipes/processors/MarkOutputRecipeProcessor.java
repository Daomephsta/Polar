package io.github.daomephsta.enhancedrecipes.common.recipes.processors;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MarkOutputRecipeProcessor extends RecipeProcessor
{
    public static final RecipeProcessor.Serialiser<?> SERIALISER = new Serialiser();
    private final Ingredient outputMatcher;
    
    private MarkOutputRecipeProcessor(Ingredient outputMatcher)
    {
        this.outputMatcher = outputMatcher;
    }

    @Override
    public TestResult test(CraftingInventory inventory, World world, TestResult predictedOutput)
    {
        return predictedOutput.withPredictedStack(findOutput(inventory));
    }
    
    @Override
    public ItemStack apply(CraftingInventory inventory, ItemStack output)
    {
        return findOutput(inventory);
    }

    private ItemStack findOutput(CraftingInventory inventory)
    {
        ItemStack match = ItemStack.EMPTY;
        for (int s = 0; s < inventory.size(); s++)
        {
            ItemStack stack = inventory.getStack(s);
            if (outputMatcher.test(stack))
            {
                if (match.isEmpty())
                    match = stack;
                else
                    throw new IllegalStateException("More than one output candidate found in the crafting grid");
            }
        }
        return match.copy();
    }

    @Override
    public RecipeProcessor.Serialiser<?> getSerialiser()
    {
        return SERIALISER;
    }

    private static class Serialiser implements RecipeProcessor.Serialiser<MarkOutputRecipeProcessor>
    {
        @Override
        public MarkOutputRecipeProcessor read(Identifier recipeId, JsonObject json)
        {
            if (!json.has("output"))
                throw new JsonSyntaxException("Missing output ingredient");
            return new MarkOutputRecipeProcessor(Ingredient.fromJson(json.get("output")));
        }

        @Override
        public MarkOutputRecipeProcessor read(Identifier recipeId, PacketByteBuf bytes)
        {
            return new MarkOutputRecipeProcessor(Ingredient.fromPacket(bytes));
        }

        @Override
        public void write(PacketByteBuf bytes, MarkOutputRecipeProcessor instance)
        {
            instance.outputMatcher.write(bytes);
        }    
    }
}
