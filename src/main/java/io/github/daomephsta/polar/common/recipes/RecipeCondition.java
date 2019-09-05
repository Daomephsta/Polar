package io.github.daomephsta.polar.common.recipes;

import java.util.function.BiPredicate;

import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

public interface RecipeCondition extends BiPredicate<CraftingInventory, World>
{
	public static final Registry<Serialiser<?>> REGISTRY 
		= Registry.register(Registry.REGISTRIES, new Identifier(Polar.MODID, "recipe_condition_serialiser"), new SimpleRegistry<>());
	
	public static RecipeCondition fromJson(JsonObject json)
	{
		Serialiser<?> serialiser = REGISTRY.get(new Identifier(JsonHelper.getString(json, "type")));
		return serialiser.read(json);
	}
	
	public static RecipeCondition fromBytes(PacketByteBuf bytes)
	{
		Serialiser<?> serialiser = REGISTRY.get(bytes.readIdentifier());
		return serialiser.read(bytes);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends RecipeCondition> void toBytes(PacketByteBuf bytes, T condition)
	{
		((Serialiser<T>) condition.getSerialiser()).write(bytes, condition);
	}
	
	public Serialiser<?> getSerialiser();
	
	public interface Serialiser<T extends RecipeCondition>
	{
		public T read(JsonObject json);
		
		public T read(PacketByteBuf bytes);
		
		public void write(PacketByteBuf bytes, T instance);
	}
}
