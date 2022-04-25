package io.github.daomephsta.enhancedrecipes.common.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.daomephsta.enhancedrecipes.common.EnhancedRecipes;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public abstract class RecipeProcessor
{
    public static final Registry<Serialiser<?>> REGISTRY =
        createRegistry(Serialiser.class, "recipe_processor_serialiser");

    @SuppressWarnings("unchecked")
    private static <T, R extends Registry<? extends T>> R createRegistry(Class<T> clazz, String id)
    {
        return (R) FabricRegistryBuilder.createSimple(clazz, EnhancedRecipes.id(id)).buildAndRegister();
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

    public boolean test(CraftingInventory inventory, World world, ItemStack predictedOutput)
    {
        return true;
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
}
