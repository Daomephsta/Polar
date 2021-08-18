package io.github.daomephsta.polar.common.util.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;

import io.github.daomephsta.polar.common.util.Consumer3;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;

class NbtReaderWriterImpl implements NbtReader, NbtWriter
{
    private final NbtCompound compound;

    NbtReaderWriterImpl(NbtCompound compound)
    {
        this.compound = compound;
    }

    @Override
    public NbtWriter compound(String key, Consumer<NbtWriter> subWriter)
    {
        var subCompound = new NbtCompound();
        subWriter.accept(NbtWriter.create(subCompound));
        compound.put(key, subCompound);
        return this;
    }

    @Override
    public <K, V> Map<K, V> map(String key, Function<K, String> keyFn, Function<V, NbtElement> valueFn)
    {
        var map = new HashMap<K, V>();
        var subCompound = compound.getCompound(key);
        for (var entry : map.entrySet())
            subCompound.put(keyFn.apply(entry.getKey()), valueFn.apply(entry.getValue()));
        compound.put(key, subCompound);
        return map;
    }

    @Override
    public <K, V, N extends NbtElement> Map<K, V> map(String key, Consumer3<String, N, BiConsumer<K, V>> mapper)
    {
        var map = new HashMap<K, V>();
        var subCompound = compound.getCompound(key);
        for (String subCompoundKey : subCompound.getKeys())
        {
            @SuppressWarnings("unchecked")
            N value = (N) subCompound.get(subCompoundKey);
            mapper.accept(key, value, map::put);
        }
        return map;
    }

    @Override
    public <K, V> NbtWriter map(String key, Map<K, V> map,
        Function<K, String> keyFn, Function<V, NbtElement> valueFn)
    {
        var subCompound = new NbtCompound();
        for (var entry : map.entrySet())
            subCompound.put(keyFn.apply(entry.getKey()), valueFn.apply(entry.getValue()));
        compound.put(key, subCompound);
        return this;
    }

    @Override
    public <E extends Enum<E>> E enumValue(String key, Class<E> enumClass, E fallback)
    {
        return compound.contains(key)
            ? Enum.valueOf(enumClass, compound.getString(key))
            : fallback;
    }

    @Override
    public <E extends Enum<E>> E enumValue(String key, Class<E> enumClass)
    {
        return Enum.valueOf(enumClass, compound.getString(key));
    }

    @Override
    public <E extends Enum<E>> NbtWriter enumValue(String key, E enumVal)
    {
        compound.putString(key, enumVal.name());
        return this;
    }

    @Override
    public BlockState blockState(String key)
    {
        DataResult<Pair<BlockState, NbtElement>> state =
            BlockState.CODEC.decode(NbtOps.INSTANCE, compound.get(key));
        return state.result().orElseThrow().getFirst();
    }

    @Override
    public NbtWriter blockState(String key, BlockState state)
    {
        BlockState.CODEC.encodeStart(NbtOps.INSTANCE, state).result()
            .ifPresent(stateNbt -> compound.put(key, stateNbt));
        return this;
    }

    @Override
    public BlockPos position(String key)
    {
        NbtCompound posNbt = compound.getCompound(key);
        int x = posNbt.getInt("x"),
            y = posNbt.getInt("y"),
            z = posNbt.getInt("z");
        return new BlockPos(x, y, z);
    }

    @Override
    public NbtWriter position(String key, BlockPos pos)
    {
        NbtCompound posNbt = new NbtCompound();
        posNbt.putInt("x", pos.getX());
        posNbt.putInt("y", pos.getY());
        posNbt.putInt("z", pos.getZ());
        compound.put(key, posNbt);
        return this;
    }
}
