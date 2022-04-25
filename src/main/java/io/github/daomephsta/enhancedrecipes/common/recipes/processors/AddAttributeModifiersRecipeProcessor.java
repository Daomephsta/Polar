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

import io.github.daomephsta.enhancedrecipes.common.recipes.StackOnlyRecipeProcessor;
import io.github.daomephsta.enhancedrecipes.common.recipes.RecipeProcessor;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class AddAttributeModifiersRecipeProcessor extends StackOnlyRecipeProcessor
{
    public static final RecipeProcessor.Serialiser<?> SERIALISER = new Serialiser();
    private final Multimap<EntityAttribute, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers;

    private AddAttributeModifiersRecipeProcessor(Multimap<EntityAttribute, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers)
    {
        this.modifiers = modifiers;
    }

    @Override
    public ItemStack apply(ItemStack output)
    {
        for (EquipmentSlot slot : EquipmentSlot.values())
        {
            for (Entry<EntityAttribute, EntityAttributeModifier> entry : output.getAttributeModifiers(slot).entries())
            {
                output.addAttributeModifier(entry.getKey(), entry.getValue(), slot);
            }
        }
        for (Entry<EntityAttribute, Entry<EntityAttributeModifier, EquipmentSlot>> entry : modifiers.entries())
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
        public AddAttributeModifiersRecipeProcessor read(Identifier recipeId, JsonObject json)
        {
            Multimap<EntityAttribute, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers =
                ArrayListMultimap.create();
            for (Entry<String, JsonElement> entry : JsonHelper.getObject(json, "modifiers").entrySet())
            {
                EquipmentSlot slot = EquipmentSlot.byName(entry.getKey());
                for (JsonElement element : JsonHelper.asArray(entry.getValue(), slot.getName()))
                {
                    JsonObject modifierJson = JsonHelper.asObject(element, "modifier");
                    UUID uuid = UUID.fromString(JsonHelper.getString(modifierJson, "uuid"));
                    String name = JsonHelper.getString(modifierJson, "name");
                    double amount = JsonHelper.getDouble(modifierJson, "amount");
                    Operation operation = getOperation(modifierJson);
                    EntityAttribute attributeId = Registry.ATTRIBUTE.get(new Identifier(
                        JsonHelper.getString(modifierJson, "attribute_id")));
                    modifiers.put(attributeId,
                        Pair.of(new EntityAttributeModifier(uuid, name, amount, operation), slot));
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

        @Override
        public AddAttributeModifiersRecipeProcessor read(Identifier recipeId, PacketByteBuf bytes)
        {
            int uniqueKeys = bytes.readVarInt();
            Multimap<EntityAttribute, Entry<EntityAttributeModifier, EquipmentSlot>> modifiers =
                ArrayListMultimap.create();
            for (int i = 0; i < uniqueKeys; i++)
            {
                EntityAttribute attributeId = Registry.ATTRIBUTE.get(bytes.readIdentifier());
                int valuesForKey = bytes.readVarInt();
                for (int j = 0; j < valuesForKey; j++)
                {
                    UUID uuid = bytes.readUuid();
                    String name = bytes.readString();
                    double amount = bytes.readDouble();
                    Operation operation = Operation.fromId(bytes.readVarInt());
                    EquipmentSlot slot = EquipmentSlot.values()[bytes.readVarInt()];
                    modifiers.put(attributeId, Pair.of(
                        new EntityAttributeModifier(uuid, name, amount, operation), slot));
                }
            }
            return new AddAttributeModifiersRecipeProcessor(modifiers);
        }

        @Override
        public void write(PacketByteBuf bytes, AddAttributeModifiersRecipeProcessor instance)
        {
            bytes.writeVarInt(instance.modifiers.keySet().size());
            for (EntityAttribute attribute : instance.modifiers.keySet())
            {
                bytes.writeIdentifier(Registry.ATTRIBUTE.getId(attribute));
                Collection<Entry<EntityAttributeModifier, EquipmentSlot>> attributeModifiers =
                    instance.modifiers.get(attribute);
                bytes.writeVarInt(attributeModifiers.size());
                for (Entry<EntityAttributeModifier, EquipmentSlot> entry : attributeModifiers)
                {
                    EntityAttributeModifier modifier = entry.getKey();
                    EquipmentSlot slot = entry.getValue();
                    bytes.writeUuid(modifier.getId());
                    bytes.writeString(modifier.getName());
                    bytes.writeDouble(modifier.getValue());
                    bytes.writeVarInt(modifier.getOperation().getId());
                    bytes.writeVarInt(slot.ordinal());
                }
            }
        }
    }
}
