package io.github.daomephsta.polar.common.recipes;

import static java.util.stream.Collectors.toCollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;
import com.google.gson.JsonObject;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public class EnhancedShapelessRecipe extends ShapelessRecipe
{
	public static final RecipeSerializer<EnhancedShapelessRecipe> SERIALIZER = new Serializer();
	private final Collection<RecipeCondition> conditions;
	private final Collection<RecipeFunction> functions;

	private EnhancedShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> inputs, Collection<RecipeCondition> conditions, Collection<RecipeFunction> functions)
	{
		super(id, group, output, inputs);
		this.conditions = conditions;
		this.functions = functions;
	}

	@Override
	public boolean matches(CraftingInventory inventory, World world)
	{
		return conditions.stream().allMatch(c -> c.test(inventory, world)) && super.method_17730(inventory, world);
	}
	
	@Override
	public ItemStack craft(CraftingInventory inventory)
	{
		ItemStack result = super.method_17729(inventory);
		for (RecipeFunction function : functions)
		{
			result = function.apply(result);
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
			String group = JsonHelper.getString(json, "group");
			DefaultedList<Ingredient> inputs = Streams.stream(JsonHelper.getArray(json, "ingredients"))
					.map(Ingredient::fromJson)
					.collect(toCollection(DefaultedList::of));
			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
			Collection<RecipeCondition> conditions = Streams.stream(JsonHelper.getArray(json, "conditions"))
					.map(e -> RecipeCondition.fromJson(JsonHelper.asObject(e, "condition")))
					.collect(Collectors.toSet());
			Collection<RecipeFunction> functions = Streams.stream(JsonHelper.getArray(json, "functions"))
					.map(e -> RecipeFunction.fromJson(JsonHelper.asObject(e, "function")))
					.collect(Collectors.toSet());
			return new EnhancedShapelessRecipe(id, group, output, inputs, conditions, functions);
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
			ArrayList<RecipeCondition> conditions = new ArrayList<>(bytes.readVarInt());
			for (int c = 0; c < conditions.size(); c++)
			{
				conditions.set(c, RecipeCondition.fromBytes(bytes));
			}
			ArrayList<RecipeFunction> functions = new ArrayList<>(bytes.readVarInt());
			for (int c = 0; c < functions.size(); c++)
			{
				functions.set(c, RecipeFunction.fromBytes(bytes));
			}
			return new EnhancedShapelessRecipe(id, group, output, inputs, conditions, functions);
		}

		@Override
		public void write(PacketByteBuf bytes, EnhancedShapelessRecipe recipe)
		{
			bytes.writeString(recipe.getGroup());
			bytes.writeVarInt(recipe.getPreviewInputs().size());
			for (Ingredient input : recipe.getPreviewInputs())
			{
				input.write(bytes);
			}
			bytes.writeItemStack(recipe.getOutput());
			bytes.writeVarInt(recipe.conditions.size());
			for (RecipeCondition condition : recipe.conditions)
			{
				RecipeCondition.toBytes(bytes, condition);
			}
			bytes.writeVarInt(recipe.functions.size());
			for (RecipeFunction function : recipe.functions)
			{
				RecipeFunction.toBytes(bytes, function);
			}
		}	
	}
}
