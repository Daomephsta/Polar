package io.github.daomephsta.polar.common;

import java.util.Map.Entry;

import com.google.common.collect.Iterables;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class NBTExtensions
{
	public static <E extends Enum<E>> E getEnumConstant(CompoundTag nbt, Class<E> enumClass, String key, E fallback)
	{
		return nbt.containsKey(key) 
			? Enum.valueOf(enumClass, nbt.getString(key))
			: fallback;
	}
	
	public static <E extends Enum<E>> E getEnumConstant(CompoundTag nbt, Class<E> enumClass, String key)
	{
		return Enum.valueOf(enumClass, nbt.getString(key));
	}
	
	public static <E extends Enum<E>> void putEnumConstant(CompoundTag nbt, String key, E enumConstant)
	{
		nbt.putString(key, enumConstant.name());
	}
	
	public static BlockState getBlockState(CompoundTag nbt, String key)
	{
		CompoundTag stateTag = nbt.getCompound(key);
		Block block = Registry.BLOCK.get(new Identifier(stateTag.getString("block")));
		CompoundTag propertiesTag = stateTag.getCompound("properties");
		StateFactory<Block, BlockState> stateContainer = block.getStateFactory();
		BlockState state = block.getDefaultState();
		for (String propName : propertiesTag.getKeys())
		{
			Property<?> property = stateContainer.getProperty(propName);
			state = deserialisePropertyValue(state, property, propertiesTag.getString(propName));
		}
		return state;
	}
	
	private static <T extends Comparable<T>> BlockState deserialisePropertyValue(BlockState state, Property<T> property, String serValue)
	{
		return state.with(property, property.getValue(serValue).get());
	}
	
	public static void putBlockState(CompoundTag nbt, String key, BlockState state)
	{
		CompoundTag stateTag = new CompoundTag();
		stateTag.putString("block", Registry.BLOCK.getId(state.getBlock()).toString());
		CompoundTag propertiesTag = new CompoundTag();
		for (Entry<Property<?>, Comparable<?>> entry : state.getEntries().entrySet())
		{
			Property<?> property = entry.getKey();
			propertiesTag.putString(property.getName(), serialisePropertyValue(property, entry.getValue()));
		}
		stateTag.put("properties", propertiesTag);
		
		nbt.put(key, stateTag);
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> String serialisePropertyValue(Property<T> property, Comparable<?> value)
	{
		return property.getName((T) value);
	}
	
	public static BlockPos getPosition(CompoundTag nbt, String key)
	{
		CompoundTag posTag = nbt.getCompound(key);
		int x = posTag.getInt("x"),
			y = posTag.getInt("y"),
			z = posTag.getInt("z");
		return new BlockPos(x, y, z);
	}
	
	public static void putPosition(CompoundTag nbt, String key, BlockPos pos)
	{
		CompoundTag posTag = new CompoundTag();
		posTag.putInt("x", pos.getX());
		posTag.putInt("y", pos.getY());
		posTag.putInt("z", pos.getZ());
		nbt.putIntArray(key, new int[] {});
	}
	
	public static boolean contains(ListTag list, String target)
	{
		return Iterables.any(list, nbt -> nbt instanceof StringTag && ((StringTag) nbt).asString().equals(target));
	}
}
