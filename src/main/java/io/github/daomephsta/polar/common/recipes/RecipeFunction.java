package io.github.daomephsta.polar.common.recipes;

import java.util.function.UnaryOperator;

import com.google.gson.JsonObject;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public interface RecipeFunction extends UnaryOperator<ItemStack>
{
	public static final Registry<RecipeFunction.Serialiser<?>> REGISTRY 
		= Registry.register(Registry.REGISTRIES, new Identifier(Polar.MODID, "recipe_function_serialiser"), new SimpleRegistry<>());
	
	public static RecipeFunction fromJson(JsonObject json)
	{
		Serialiser<?> serialiser = REGISTRY.get(new Identifier(JsonHelper.getString(json, "type")));
		return serialiser.read(json);
	}
	
	public static RecipeFunction fromBytes(PacketByteBuf bytes)
	{
		Serialiser<?> serialiser = REGISTRY.get(bytes.readIdentifier());
		return serialiser.read(bytes);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends RecipeFunction> void toBytes(PacketByteBuf bytes, T condition)
	{
		((Serialiser<T>) condition.getSerialiser()).write(bytes, condition);
	}
	
	public Serialiser<?> getSerialiser();
	
	public interface Serialiser<T extends RecipeFunction>
	{
		public T read(JsonObject json);
		
		public T read(PacketByteBuf bytes);
		
		public void write(PacketByteBuf bytes, T instance);
	}
}
