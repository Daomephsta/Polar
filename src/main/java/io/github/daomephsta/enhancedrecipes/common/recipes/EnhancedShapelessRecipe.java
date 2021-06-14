package io.github.daomephsta.enhancedrecipes.common.recipes;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;
import com.google.gson.JsonObject;

import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor.TestResult;
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

	private EnhancedShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> inputs, List<RecipeProcessor> processors)
	{
		super(id, group, output, inputs);
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
					.map(e -> RecipeProcessor.fromJson(JsonHelper.asObject(e, "processor")))
					.collect(Collectors.toList());
			return new EnhancedShapelessRecipe(id, group, output, inputs, processors);
		}

		@Override
		public EnhancedShapelessRecipe read(Identifier id, PacketByteBuf bytes)
		{
			String group = bytes.readString();
			DefaultedList<Ingredient> inputs = DefaultedList.ofSize(bytes.readVarInt(), Ingredient.EMPTY);
			for (int i = 0; i < inputs.size(); i++)
			{
				inputs.set(i, Ingredient.fromPacket(bytes));
			}
			ItemStack output = bytes.readItemStack();
			ArrayList<RecipeProcessor> processors = new ArrayList<>(bytes.readVarInt());
			for (int c = 0; c < processors.size(); c++)
			{
				processors.set(c, RecipeProcessor.fromBytes(bytes));
			}
			return new EnhancedShapelessRecipe(id, group, output, inputs, processors);
		}

		@Override
		public void write(PacketByteBuf bytes, EnhancedShapelessRecipe recipe)
		{
			bytes.writeString(recipe.getGroup());
			bytes.writeVarInt(recipe.getIngredients().size());
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
