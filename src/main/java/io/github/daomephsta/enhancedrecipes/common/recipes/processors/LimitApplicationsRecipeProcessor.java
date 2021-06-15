package io.github.daomephsta.enhancedrecipes.common.recipes.processors;

import com.google.gson.JsonObject;

import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import io.github.daomephsta.polar.common.Polar;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

public class LimitApplicationsRecipeProcessor extends RecipeProcessor
{
    public static final RecipeProcessor.Serialiser<?> SERIALISER = new Serialiser();
    private static final String TAG_APPLIED_RECIPES = Polar.MOD_ID + "_applied_recipes";
    private final String recipeId;
    private final int maxApplications;

    private LimitApplicationsRecipeProcessor(Identifier recipeId, int maxApplications)
    {
        this.recipeId = recipeId.toString();
        this.maxApplications = maxApplications;
    }

    @Override
    public TestResult test(CraftingInventory inventory, World world, TestResult predictedOutput)
    {
        if (getApplicationCount(predictedOutput.getPredictedStack()) == maxApplications)
            return TestResult.fail();
        incrementApplicationCount(predictedOutput.getPredictedStack());
        return predictedOutput;
    }
    
    @Override
    public ItemStack apply(CraftingInventory inventory, ItemStack output)
    {
        incrementApplicationCount(output);
        return output;
    }
    
    private int getApplicationCount(ItemStack stack)
    {
        NbtCompound nbt = stack.getOrCreateTag();
        if (!nbt.contains(TAG_APPLIED_RECIPES))
            return 0;
        return nbt.getCompound(TAG_APPLIED_RECIPES).getInt(recipeId);
    }
    
    private void incrementApplicationCount(ItemStack stack)
    {
        NbtCompound appliedRecipes = stack.getOrCreateSubTag(TAG_APPLIED_RECIPES);
        appliedRecipes.putInt(recipeId, appliedRecipes.getInt(recipeId) + 1);
    }

    @Override
    public RecipeProcessor.Serialiser<?> getSerialiser()
    {
        return SERIALISER;
    }

    private static class Serialiser implements RecipeProcessor.Serialiser<LimitApplicationsRecipeProcessor>
    {
        @Override
        public LimitApplicationsRecipeProcessor read(Identifier recipeId, JsonObject json)
        {
            return new LimitApplicationsRecipeProcessor(recipeId, JsonHelper.getInt(json, "max_applications"));
        }

        @Override
        public LimitApplicationsRecipeProcessor read(Identifier recipeId, PacketByteBuf bytes)
        {
            return new LimitApplicationsRecipeProcessor(recipeId, bytes.readInt());
        }

        @Override
        public void write(PacketByteBuf bytes, LimitApplicationsRecipeProcessor instance)
        {
            bytes.writeInt(instance.maxApplications);
        }    
    }
}
