package io.github.daomephsta.enhancedrecipes.common.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

public abstract class RecipeProcessor
{
	public static final Registry<Serialiser<?>> REGISTRY 
		= Registry.register(Registry.REGISTRIES, new Identifier(Polar.MODID, "recipe_processor_serialiser"), new SimpleRegistry<>());

	public static RecipeProcessor fromJson(JsonObject json)
	{
		Identifier conditionId = new Identifier(JsonHelper.getString(json, "type"));
		Serialiser<?> serialiser = REGISTRY.get(conditionId);
		if (serialiser == null)
			throw new JsonSyntaxException("Unknown recipe processor type " + conditionId);
		return serialiser.read(null, json);
	}

	public static RecipeProcessor fromBytes(PacketByteBuf bytes)
	{
		Serialiser<?> serialiser = REGISTRY.get(bytes.readIdentifier());
		return serialiser.read(null, bytes);
	}

	@SuppressWarnings("unchecked")
	public static <T extends RecipeProcessor> void toBytes(PacketByteBuf bytes, T condition)
	{
		((Serialiser<T>) condition.getSerialiser()).write(bytes, condition);
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
		public T read(String recipeId, JsonObject json);

		public T read(String recipeId, PacketByteBuf bytes);

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
		
		public TestResult withPredictedTag(CompoundTag tag)
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
