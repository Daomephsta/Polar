package io.github.daomephsta.enhancedrecipes.common.recipes.processors;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public class AddAttributeModifiersRecipeProcessor extends RecipeProcessor
{
	public static final RecipeProcessor.Serialiser<?> SERIALISER = new Serialiser();
	private final Multimap<String, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers;
	
	private AddAttributeModifiersRecipeProcessor(Multimap<String, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers)
	{
		this.modifiers = modifiers;
	}

	@Override
	public TestResult test(CraftingInventory inventory, World world, TestResult predictedOutput)
	{
		return predictedOutput;
	}
	
	@Override
	public ItemStack apply(CraftingInventory inventory, ItemStack output)
	{
		for (EquipmentSlot slot : EquipmentSlot.values())
		{
			for (Entry<String, EntityAttributeModifier> entry : output.getAttributeModifiers(slot).entries())
			{
				output.addAttributeModifier(entry.getKey(), entry.getValue(), slot);
			}
		}
		for (Entry<String, Entry<EntityAttributeModifier, EquipmentSlot>> entry : modifiers.entries())
		{
			output.addAttributeModifier(entry.getKey(), entry.getValue().getKey(), entry.getValue().getValue());
		}
		return output;
	}

	@Override
	public RecipeProcessor.Serialiser<?> getSerialiser()
	{
		return SERIALISER;
	}

	private static class Serialiser implements RecipeProcessor.Serialiser<AddAttributeModifiersRecipeProcessor>
	{
		@Override
		public AddAttributeModifiersRecipeProcessor read(String recipeId, JsonObject json)
		{
			Multimap<String, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers = ArrayListMultimap.create();
			for (Entry<String, JsonElement> entry : JsonHelper.getObject(json, "modifiers").entrySet())
			{
				EquipmentSlot slot = EquipmentSlot.byName(entry.getKey());
				for (JsonElement element : JsonHelper.asArray(entry.getValue(), slot.getName()))
				{
					JsonObject modifierJson = JsonHelper.asObject(element, "modifier");
					UUID uuid = UUID.fromString(JsonHelper.getString(modifierJson, "uuid"));
					String name = JsonHelper.getString(modifierJson, "name");
					double amount = getDouble(modifierJson, "amount");
					Operation operation = getOperation(modifierJson);
					String attributeId = JsonHelper.getString(modifierJson, "attribute_id");
					modifiers.put(attributeId, Pair.of(new EntityAttributeModifier(uuid, name, amount, operation), slot));
				}
			}
			return new AddAttributeModifiersRecipeProcessor(modifiers);
		}

		private Operation getOperation(JsonObject modifierJson)
		{
			try
			{
				return Operation.valueOf(JsonHelper.getString(modifierJson, "operation"));
			}
			catch (IllegalArgumentException e)
			{
				throw new JsonSyntaxException("Operation must be one of " + Arrays.toString(Operation.values()));
			}
		}

		private double getDouble(JsonObject json, String key)
		{
			if (json.has(key))
			{
				JsonElement element = json.get(key); 
				if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
					return element.getAsJsonPrimitive().getAsDouble();
				else
					throw new JsonSyntaxException("Expected " + key + " to be a double, was " + JsonHelper.getType(element));
			}
			else
				throw new JsonSyntaxException("Missing " + key + ", expected to find a double");
		}

		@Override
		public AddAttributeModifiersRecipeProcessor read(String recipeId, PacketByteBuf bytes)
		{
			int uniqueKeys = bytes.readVarInt();
			Multimap<String, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers = ArrayListMultimap.create();
			for (int i = 0; i < uniqueKeys; i++)
			{
				String attributeId = bytes.readString();
				int valuesForKey = bytes.readVarInt();
				for (int j = 0; j < valuesForKey; j++)
				{
					UUID uuid = bytes.readUuid();
					String name = bytes.readString();
					double amount = bytes.readDouble();
					Operation operation = Operation.fromId(bytes.readVarInt());
					EquipmentSlot slot = EquipmentSlot.values()[bytes.readVarInt()];
					modifiers.put(attributeId, Pair.of(new EntityAttributeModifier(uuid, name, amount, operation), slot));
				}
			}
			return new AddAttributeModifiersRecipeProcessor(modifiers);
		}

		@Override
		public void write(PacketByteBuf bytes, AddAttributeModifiersRecipeProcessor instance)
		{
			bytes.writeVarInt(instance.modifiers.keySet().size());
			for (String attributeId : instance.modifiers.keySet())
			{
				bytes.writeString(attributeId);
				Collection<Entry<EntityAttributeModifier, EquipmentSlot>> attributeModifiers = instance.modifiers.get(attributeId);
				bytes.writeVarInt(attributeModifiers.size());
				for (Entry<EntityAttributeModifier, EquipmentSlot> entry : attributeModifiers)
				{
					EntityAttributeModifier modifier = entry.getKey();
					EquipmentSlot slot = entry.getValue();
					bytes.writeUuid(modifier.getId());
					bytes.writeString(modifier.getName());
					bytes.writeDouble(modifier.getAmount());
					bytes.writeVarInt(modifier.getOperation().getId());
					bytes.writeVarInt(slot.ordinal());
				}
			}
		}	
	}
}
