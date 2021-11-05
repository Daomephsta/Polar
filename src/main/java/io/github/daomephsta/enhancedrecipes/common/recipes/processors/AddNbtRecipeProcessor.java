package io.github.daomephsta.enhancedrecipes.common.recipes.processors;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import io.github.daomephsta.enhancedrecipes.common.recipes.StackOnlyRecipeProcessor;
import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class AddNbtRecipeProcessor extends StackOnlyRecipeProcessor
{
    public static final RecipeProcessor.Serialiser<?> SERIALISER = new Serialiser();
    protected final NbtCompound tag;

    protected AddNbtRecipeProcessor(NbtCompound tag)
    {
        this.tag = tag;
    }

    @Override
    public TestResult test(TestResult predictedOutput)
    {
        return predictedOutput.withPredictedTag(tag);
    }

    @Override
    public ItemStack apply(ItemStack output)
    {
        output.getOrCreateNbt().copyFrom(tag);
        return output;
    }

    @Override
    public RecipeProcessor.Serialiser<?> getSerialiser()
    {
        return SERIALISER;
    }

    private static class Serialiser implements RecipeProcessor.Serialiser<AddNbtRecipeProcessor>
    {
        @Override
        public AddNbtRecipeProcessor read(Identifier recipeId, JsonObject json)
        {
            JsonObject tagJson = JsonHelper.getObject(json, "tag");
            return new AddNbtRecipeProcessor((NbtCompound) JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, tagJson));
        }

        @Override
        public AddNbtRecipeProcessor read(Identifier recipeId, PacketByteBuf bytes)
        {
            return new AddNbtRecipeProcessor(bytes.readNbt());
        }

        @Override
        public void write(PacketByteBuf bytes, AddNbtRecipeProcessor instance)
        {
            bytes.writeNbt(instance.tag);
        }
    }
}
