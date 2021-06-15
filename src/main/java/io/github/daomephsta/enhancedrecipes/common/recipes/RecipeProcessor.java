package io.github.daomephsta.enhancedrecipes.common.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Lifecycle;

import io.github.daomephsta.enhancedrecipes.common.EnhancedRecipes;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

public abstract class RecipeProcessor
{
    public static final Registry<Serialiser<?>> REGISTRY = createRegistry("recipe_processor_serialiser");

    private static <T> Registry<T> createRegistry(String id)
    {
        return FabricRegistryBuilder.from(new SimpleRegistry<T>(
            RegistryKey.ofRegistry(EnhancedRecipes.id(id)), Lifecycle.stable())).buildAndRegister();
    }

    public static RecipeProcessor fromJson(Identifier recipeId, JsonObject json)
    {
        Identifier conditionId = new Identifier(JsonHelper.getString(json, "type"));
        Serialiser<?> serialiser = REGISTRY.get(conditionId);
        if (serialiser == null)
            throw new JsonSyntaxException("Unknown recipe processor type " + conditionId);
        return serialiser.read(recipeId, json);
    }

    public static RecipeProcessor fromBytes(Identifier recipeId, PacketByteBuf bytes)
    {
        Serialiser<?> serialiser = REGISTRY.get(bytes.readIdentifier());
        return serialiser.read(recipeId, bytes);
    }

    @SuppressWarnings("unchecked")
    public static <T extends RecipeProcessor> void toBytes(PacketByteBuf bytes, T processor)
    {
        ((Serialiser<T>) processor.getSerialiser()).write(bytes, processor);
    }

    public TestResult test(CraftingInventory inventory, World world, TestResult predictedOutput) 
    {
        return predictedOutput;
    }

    public ItemStack apply(CraftingInventory inventory, ItemStack output) 
    {
        return output;
    }

    public abstract Serialiser<?> getSerialiser();

    public interface Serialiser<T extends RecipeProcessor>
    {
        public T read(Identifier recipeId, JsonObject json);

        public T read(Identifier recipeId, PacketByteBuf bytes);

        public void write(PacketByteBuf bytes, T instance);
    }
    
    public static class TestResult
    {
        private final ItemStack predictedStack;
        private final boolean matches;
        
        private TestResult(ItemStack predictedStack, boolean matches)
        {
            this.predictedStack = predictedStack;
            this.matches = matches;
        }
        
        public static TestResult fail()
        {
            return new TestResult(ItemStack.EMPTY, false);
        }
        
        static TestResult pass()
        {
            return new TestResult(ItemStack.EMPTY, true);
        }
        
        public TestResult withPredictedStack(ItemStack predictedOutput)
        {
            return new TestResult(predictedOutput, true);
        }
        
        public TestResult withPredictedTag(NbtCompound tag)
        {
            predictedStack.getOrCreateTag().copyFrom(tag);
            return this;
        }
        
        public ItemStack getPredictedStack()
        {
            return predictedStack;
        }

        public boolean matches()
        {
            return matches;
        }
    }
}
