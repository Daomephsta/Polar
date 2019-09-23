package io.github.daomephsta.enhancedrecipes.common.recipes.processors;

import com.google.gson.JsonObject;

import io.github.daomephsta.enhancedrecipes.common.JsonNbtConverter;
import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public class AddNbtRecipeProcessor extends RecipeProcessor
{
	public static final RecipeProcessor.Serialiser<?> SERIALISER = new Serialiser();
	protected final CompoundTag tag;

	protected AddNbtRecipeProcessor(CompoundTag tag)
	{
		this.tag = tag;
	}

	@Override
	public TestResult test(CraftingInventory inventory, World world, TestResult predictedOutput)
	{
		return predictedOutput.withPredictedTag(tag);
	}
	
	@Override
	public ItemStack apply(CraftingInventory inventory, ItemStack output)
	{
		output.getOrCreateTag().copyFrom(tag);
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
		public AddNbtRecipeProcessor read(String recipeId, JsonObject json)
		{
			return new AddNbtRecipeProcessor(new JsonNbtConverter().toCompoundTag(JsonHelper.getObject(json, "tag")));
		}

		@Override
		public AddNbtRecipeProcessor read(String recipeId, PacketByteBuf bytes)
		{
			return new AddNbtRecipeProcessor(bytes.readCompoundTag());
		}

		@Override
		public void write(PacketByteBuf bytes, AddNbtRecipeProcessor instance)
		{
			bytes.writeCompoundTag(instance.tag);
		}	
	}
}
