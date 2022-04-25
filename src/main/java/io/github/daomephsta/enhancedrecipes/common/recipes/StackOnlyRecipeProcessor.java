package io.github.daomephsta.enhancedrecipes.common.recipes;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class StackOnlyRecipeProcessor extends RecipeProcessor
{
    @Override
    public final boolean test(CraftingInventory inventory, World world, ItemStack predictedOutput)
    {
        return test(predictedOutput);
    }

    protected boolean test(ItemStack predictedOutput)
    {
        return true;
    }

    @Override
    public final ItemStack apply(CraftingInventory inventory, ItemStack output)
    {
        return apply(output);
    }

    protected abstract ItemStack apply(ItemStack output);
}
