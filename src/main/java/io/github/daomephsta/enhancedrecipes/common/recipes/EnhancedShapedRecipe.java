package io.github.daomephsta.enhancedrecipes.common.recipes;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Streams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor.TestResult;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class EnhancedShapedRecipe extends ShapedRecipe
{
    public static final RecipeSerializer<EnhancedShapedRecipe> SERIALIZER = new Serializer();
    private final List<RecipeProcessor> processors;

    private EnhancedShapedRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> inputs, ItemStack output, List<RecipeProcessor> processors)
    {
        super(id, group, width, height, inputs, output);
        this.processors = processors;
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world)
    {
        if (!super.matches(inventory, world))
            return false;
        TestResult result = TestResult.pass();
        for (RecipeProcessor processor : processors)
        {
            result = processor.test(inventory, world, result);
            if (!result.matches())
                return false;
        }
        return true;
    }
    
    @Override
    public ItemStack craft(CraftingInventory inventory)
    {
        ItemStack result = super.craft(inventory);
        for (RecipeProcessor processor : processors)
        {
            result = processor.apply(inventory, result);
        }
        return result;
    }

    public RecipeSerializer<?> getSerializer()
    {
        return SERIALIZER;
    }    
    
    private static class Serializer implements RecipeSerializer<EnhancedShapedRecipe>
    {
        @Override
        public EnhancedShapedRecipe read(Identifier id, JsonObject json)
        {
            String group = JsonHelper.getString(json, "group", "");
            char[][] pattern = Streams.stream(JsonHelper.getArray(json, "pattern"))
                    .map(e -> e.getAsString().toCharArray())
                    .toArray(char[][]::new);
            validatePattern(pattern);
            Char2ObjectMap<Ingredient> key = readKey(JsonHelper.getObject(json, "key"));
            int width = pattern[0].length, 
                height = pattern.length; 
            DefaultedList<Ingredient> inputs = IntStream.range(0, width * height)
                    .mapToObj(i -> 
                    {
                        char c = pattern[i / width][i % width - 1];
                        return c == ' ' ? Ingredient.EMPTY : key.get((c));
                    })
                    .collect(toCollection(DefaultedList::of));
            ItemStack output = json.has("result") 
                    ? ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"))
                    : ItemStack.EMPTY;
            List<RecipeProcessor> processors = Streams.stream(JsonHelper.getArray(json, "processors"))
                    .map(e -> RecipeProcessor.fromJson(id, JsonHelper.asObject(e, "processor")))
                    .collect(Collectors.toList());
            return new EnhancedShapedRecipe(id, group, width, height, inputs, output, processors);
        }
        
        private void validatePattern(char[][] pattern)
        {
            int expectedLength = pattern[0].length;
            for (int r = 0; r < pattern.length; r++)
            {
                char[] row = pattern[r];
                if (row.length > 3)
                    throw new JsonSyntaxException("Row " + r + " has more than 3 keys");
                if (row.length != expectedLength)
                    throw new JsonSyntaxException("Rows have differing key counts. Key Counts: " + 
                            IntStream.range(0, pattern.length).mapToObj(Integer::toString).collect(joining(", ")));
            }
        }
        
        private Char2ObjectMap<Ingredient> readKey(JsonObject key)
        {
            Char2ObjectMap<Ingredient> ret = new Char2ObjectOpenHashMap<>(key.size());
            for (Entry<String, JsonElement> entry : key.entrySet())
            {
                if (entry.getKey().length() != 1)
                    throw new JsonSyntaxException("Keys must be single character");
                if (entry.getKey().equals(" "))
                    throw new JsonSyntaxException("The space character is a reserved key");
                ret.put(entry.getKey().charAt(0), Ingredient.fromJson(entry.getValue()));
            }
            return ret;
        }

        @Override
        public EnhancedShapedRecipe read(Identifier id, PacketByteBuf bytes)
        {
            String group = bytes.readString();
            int width = bytes.readVarInt(),
                height = bytes.readVarInt();
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromPacket(bytes));
            }
            ItemStack output = bytes.readItemStack();
            List<RecipeProcessor> processors = IntStream.range(0, bytes.readVarInt())
                    .mapToObj(i -> RecipeProcessor.fromBytes(id, bytes))
                    .collect(Collectors.toList());
            return new EnhancedShapedRecipe(id, group, width, height, inputs, output, processors);
        }

        @Override
        public void write(PacketByteBuf bytes, EnhancedShapedRecipe recipe)
        {
            bytes.writeString(recipe.getGroup());
            bytes.writeVarInt(recipe.getWidth());
            bytes.writeVarInt(recipe.getHeight());
            for (Ingredient input : recipe.getIngredients())
            {
                input.write(bytes);
            }
            bytes.writeItemStack(recipe.getOutput());
            bytes.writeVarInt(recipe.processors.size());
            for (RecipeProcessor condition : recipe.processors)
            {
                RecipeProcessor.toBytes(bytes, condition);
            }
            bytes.writeVarInt(recipe.processors.size());
            for (RecipeProcessor function : recipe.processors)
            {
                RecipeProcessor.toBytes(bytes, function);
            }
        }    
    }
}
