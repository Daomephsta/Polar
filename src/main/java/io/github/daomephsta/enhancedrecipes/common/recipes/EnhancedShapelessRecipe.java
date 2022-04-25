package io.github.daomephsta.enhancedrecipes.common.recipes;

import static java.util.stream.Collectors.toCollection;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Streams;
import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class EnhancedShapelessRecipe extends ShapelessRecipe
{
    public static final RecipeSerializer<EnhancedShapelessRecipe> SERIALIZER = new Serializer();
    private final List<RecipeProcessor> processors;
    private final boolean excludeFromRecipeBook;

    private EnhancedShapelessRecipe(Identifier id, String group, ItemStack output,
        DefaultedList<Ingredient> inputs, List<RecipeProcessor> processors, boolean excludeFromRecipeBook)
    {
        super(id, group, output, inputs);
        this.processors = processors;
        this.excludeFromRecipeBook = excludeFromRecipeBook;
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world)
    {
        if (!super.matches(inventory, world))
            return false;
        ItemStack result = ItemStack.EMPTY;
        for (RecipeProcessor processor : processors)
        {
            if (!processor.test(inventory, world, result))
                return false;
            result = processor.apply(inventory, result);
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory)
    {
        ItemStack result = super.craft(inventory);
        for (RecipeProcessor processor : processors)
            result = processor.apply(inventory, result);
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }

    @Override
    public boolean isIgnoredInRecipeBook()
    {
        return excludeFromRecipeBook;
    }

    private static class Serializer implements RecipeSerializer<EnhancedShapelessRecipe>
    {
        @Override
        public EnhancedShapelessRecipe read(Identifier id, JsonObject json)
        {
            String group = JsonHelper.getString(json, "group", "");
            DefaultedList<Ingredient> inputs = Streams.stream(JsonHelper.getArray(json, "ingredients"))
                    .map(Ingredient::fromJson)
                    .collect(toCollection(DefaultedList::of));
            ItemStack output = json.has("result")
                    ? ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"))
                    : ItemStack.EMPTY;
            List<RecipeProcessor> processors = Streams.stream(JsonHelper.getArray(json, "processors"))
                    .map(e -> RecipeProcessor.fromJson(id, JsonHelper.asObject(e, "processor")))
                    .collect(Collectors.toList());
            boolean excludeFromRecipeBook = false;
            for (var processor : processors)
            {
                if (processor instanceof StackOnlyRecipeProcessor pure)
                    output = pure.apply(output);
                else
                    excludeFromRecipeBook = true;
            }
            return new EnhancedShapelessRecipe(id, group, output, inputs, processors, excludeFromRecipeBook);
        }

        @Override
        public EnhancedShapelessRecipe read(Identifier id, PacketByteBuf bytes)
        {
            String group = bytes.readString();
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(bytes.readVarInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++)
                inputs.set(i, Ingredient.fromPacket(bytes));
            ItemStack output = bytes.readItemStack();
            List<RecipeProcessor> processors = IntStream.range(0, bytes.readVarInt())
                    .mapToObj(i -> RecipeProcessor.fromBytes(id, bytes))
                    .collect(Collectors.toList());
            boolean excludeFromRecipeBook = bytes.readBoolean();
            return new EnhancedShapelessRecipe(id, group, output, inputs, processors, excludeFromRecipeBook);
        }

        @Override
        public void write(PacketByteBuf bytes, EnhancedShapelessRecipe recipe)
        {
            bytes.writeString(recipe.getGroup());
            bytes.writeVarInt(recipe.getIngredients().size());
            for (Ingredient input : recipe.getIngredients())
                input.write(bytes);
            bytes.writeItemStack(recipe.getOutput());
            bytes.writeVarInt(recipe.processors.size());
            for (var processor : recipe.processors)
                RecipeProcessor.toBytes(bytes, processor);
            bytes.writeBoolean(recipe.excludeFromRecipeBook);
        }
    }
}
