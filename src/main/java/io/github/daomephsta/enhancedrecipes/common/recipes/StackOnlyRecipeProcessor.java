package io.github.daomephsta.enhancedrecipes.common.recipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class StackOnlyRecipeProcessor extends RecipeProcessor
{
    @Override
    public final TestResult test(CraftingInventory inventory, World world, TestResult predictedOutput)
    {
        return test(predictedOutput);
    }
    
    protected abstract TestResult test(TestResult predictedOutput);
    
    @Override
    public final ItemStack apply(CraftingInventory inventory, ItemStack output)
    {
        return apply(output);
    }
    
    protected abstract ItemStack apply(ItemStack output);
}
