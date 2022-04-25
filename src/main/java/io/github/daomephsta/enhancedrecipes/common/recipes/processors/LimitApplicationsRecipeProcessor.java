package io.github.daomephsta.enhancedrecipes.common.recipes.processors;

import com.google.gson.JsonObject;

import io.github.daomephsta.enhancedrecipes.common.EnhancedRecipes;
import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import io.github.daomephsta.enhancedrecipes.common.recipes.StackOnlyRecipeProcessor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class LimitApplicationsRecipeProcessor extends StackOnlyRecipeProcessor
{
    public static final RecipeProcessor.Serialiser<?> SERIALISER = new Serialiser();
    private static final String TAG_APPLIED_RECIPES = EnhancedRecipes.MOD_ID + "_applied_recipes";
    private final String recipeId;
    private final int maxApplications;

    private LimitApplicationsRecipeProcessor(Identifier recipeId, int maxApplications)
    {
        this.recipeId = recipeId.toString();
        this.maxApplications = maxApplications;
    }

    @Override
    public boolean test(ItemStack predictedOutput)
    {
        NbtCompound nbt = predictedOutput.getSubNbt(TAG_APPLIED_RECIPES);
        if (nbt == null) return true; // 0 applications
        return nbt.getInt(recipeId) < maxApplications;
    }

    @Override
    public ItemStack apply(ItemStack output)
    {
        NbtCompound appliedRecipes = output.getOrCreateSubNbt(TAG_APPLIED_RECIPES);
        appliedRecipes.putInt(recipeId, appliedRecipes.getInt(recipeId) + 1);
        return output;
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
