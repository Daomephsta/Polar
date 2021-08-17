package io.github.daomephsta.polar.common;

import com.google.common.collect.Iterables;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.math.BlockPos;

public class NBTExtensions
{
    public static <E extends Enum<E>> E getEnumConstant(NbtCompound nbt, Class<E> enumClass, String key, E fallback)
    {
        return nbt.contains(key) 
            ? Enum.valueOf(enumClass, nbt.getString(key))
            : fallback;
    }
    
    public static <E extends Enum<E>> E getEnumConstant(NbtCompound nbt, Class<E> enumClass, String key)
    {
        return Enum.valueOf(enumClass, nbt.getString(key));
    }
    
    public static <E extends Enum<E>> void putEnumConstant(NbtCompound nbt, String key, E enumConstant)
    {
        nbt.putString(key, enumConstant.name());
    }
    
    public static BlockState getBlockState(NbtCompound nbt, String key)
    {   
        DataResult<Pair<BlockState, NbtElement>> state = BlockState.CODEC.decode(NbtOps.INSTANCE, nbt.get(key));
        return state.result().orElseThrow().getFirst();
    }
    
    public static void putBlockState(NbtCompound nbt, String key, BlockState state)
    {
        BlockState.CODEC.encodeStart(NbtOps.INSTANCE, state).result()
            .ifPresent(stateNbt -> nbt.put(key, stateNbt));
    }
    
    public static BlockPos getPosition(NbtCompound nbt, String key)
    {
        NbtCompound posNbt = nbt.getCompound(key);
        int x = posNbt.getInt("x"),
            y = posNbt.getInt("y"),
            z = posNbt.getInt("z");
        return new BlockPos(x, y, z);   
    }
    
    public static void putPosition(NbtCompound nbt, String key, BlockPos pos)
    {
        NbtCompound posNbt = new NbtCompound();
        posNbt.putInt("x", pos.getX());
        posNbt.putInt("y", pos.getY());
        posNbt.putInt("z", pos.getZ());
        nbt.put(key, posNbt);
    }
    
    public static boolean contains(NbtList list, String target)
    {
        return Iterables.any(list, nbt -> nbt instanceof NbtString && nbt.asString().equals(target));
    }
}
