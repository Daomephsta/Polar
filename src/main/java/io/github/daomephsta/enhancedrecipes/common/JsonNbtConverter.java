package io.github.daomephsta.enhancedrecipes.common;

import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.AbstractListTag;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class JsonNbtConverter
{
	private final Strategy conversionStrategy;
	
	public JsonNbtConverter(Strategy conversionStrategy)
	{
		this.conversionStrategy = conversionStrategy;
	}
	
	public JsonNbtConverter()
	{
		this(STRICT);
	}

	public Tag toTag(JsonElement json)
	{
		if (json.isJsonObject())
			return toCompoundTag(json.getAsJsonObject());
		else if (json.isJsonArray())
			return toListTag(json.getAsJsonArray());
		else if(json.isJsonPrimitive())
		{
			JsonPrimitive primitive = (JsonPrimitive) json;
			if (primitive.isString())
				return new StringTag(primitive.getAsString());
			else if (primitive.isNumber())
			{
				Number number = primitive.getAsNumber();
				if (number instanceof Byte)
					return new ByteTag(number.byteValue());
				else if (number instanceof Integer)
					return new IntTag(number.intValue());
				else if (number instanceof Long)
					return new LongTag(number.longValue());
				else if (number instanceof Float)
					return new FloatTag(number.floatValue());
				else if (number instanceof Double)
					return new DoubleTag(number.doubleValue());
				else 
					return conversionStrategy.convertNonstandardNumber(number);
			}
			else if (primitive.isBoolean())
				return conversionStrategy.convertBoolean(primitive.getAsBoolean());
		}
		throw new JsonSyntaxException("Expected a JSON object, array, or primitive; received " + json.getClass().getSimpleName() + " " + json);
	}
	
	public CompoundTag toCompoundTag(JsonObject json)
	{
		CompoundTag tag = new CompoundTag();
		for (Entry<String, JsonElement> entry : json.entrySet())
		{
			tag.put(entry.getKey(), toTag(entry.getValue()));
		}
		return tag;
	}
	
	public AbstractListTag<? extends Tag> toListTag(JsonArray json)
	{
		if (Streams.stream(json).allMatch(e -> e.isJsonPrimitive() && e.getAsJsonPrimitive().getAsNumber() instanceof Byte))
		{
			byte[] bytes = new byte[json.size()];
			return new ByteArrayTag(bytes);
		}
		else if (Streams.stream(json).allMatch(e -> e.isJsonPrimitive() && e.getAsJsonPrimitive().getAsNumber() instanceof Integer))
		{
			int[] ints = new int[json.size()];
			return new IntArrayTag(ints);
		}
		else if (Streams.stream(json).allMatch(e -> e.isJsonPrimitive() && e.getAsJsonPrimitive().getAsNumber() instanceof Long))
		{
			long[] longs = new long[json.size()];
			return new LongArrayTag(longs);
		}
		else
			return Streams.stream(json).map(this::toTag).collect(Collectors.toCollection(ListTag::new));
	}
	
	public JsonElement toJsonElement(Tag tag)
	{
		switch (tag.getType())
		{
			case NbtType.BYTE:
				return new JsonPrimitive(((ByteTag) tag).getByte());
			case NbtType.SHORT:
				return new JsonPrimitive(((ShortTag) tag).getShort());
			case NbtType.INT:
				return new JsonPrimitive(((IntTag) tag).getInt());
			case NbtType.LONG:
				return new JsonPrimitive(((LongTag) tag).getLong());
			case NbtType.FLOAT:
				return new JsonPrimitive(((FloatTag) tag).getFloat());
			case NbtType.DOUBLE:
				return new JsonPrimitive(((DoubleTag) tag).getDouble());
			case NbtType.BYTE_ARRAY:
			case NbtType.INT_ARRAY:
			case NbtType.LONG_ARRAY:
			case NbtType.LIST:
				return toJsonArray((AbstractListTag<?>) tag);
			case NbtType.STRING:
				return new JsonPrimitive(tag.asString());
			case NbtType.COMPOUND:
				return toJsonObject((CompoundTag) tag);
			default:
				throw new IllegalArgumentException("Unsupported tag type " + tag.getType() + " (" + tag.getClass().getSimpleName() + ")");
		}
	}

	public JsonObject toJsonObject(CompoundTag tag)
	{
		JsonObject json = new JsonObject();
		for (String key : tag.getKeys())
		{
			json.add(key, toJsonElement(tag.getTag(key)));
		}
		return json;
	}
	
	private JsonElement toJsonArray(AbstractListTag<?> tag)
	{
		JsonArray array = new JsonArray();
		for (Tag element : tag)
		{
			array.add(toJsonElement(element));
		}
		return array;
	}
	
	public static final Strategy STRICT = new Strategy()
	{
		@Override
		public Tag convertNonstandardNumber(Number number) throws JsonSyntaxException
		{
			throw new JsonSyntaxException("Cannot convert " + number);
		}
		
		@Override
		public Tag convertBoolean(Boolean bool) throws JsonSyntaxException
		{
			throw new JsonSyntaxException("Cannot convert " + bool);
		}
	};

	public interface Strategy
	{
		public Tag convertBoolean(Boolean bool) throws JsonSyntaxException;

		public Tag convertNonstandardNumber(Number number) throws JsonSyntaxException;
	}
}
