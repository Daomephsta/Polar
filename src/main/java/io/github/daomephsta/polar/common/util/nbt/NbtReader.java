package io.github.daomephsta.polar.common.util.nbt;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import io.github.daomephsta.polar.common.util.Consumer3;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

public interface NbtReader
{
    public static NbtReader create(NbtCompound compound)
    {
        return new NbtReaderWriterImpl(compound);
    }

    BlockPos position(String key);
    BlockState blockState(String key);
    <E extends Enum<E>> E enumValue(String key, Class<E> enumClass);
    <E extends Enum<E>> E enumValue(String key, Class<E> enumClass, E fallback);
    <K, V, N extends NbtElement> Map<K, V> map(String key, Consumer3<String, N, BiConsumer<K, V>> mapper);
    <K, V> Map<K, V> map(String key, Function<K, String> keyFn, Function<V, NbtElement> valueFn);
}
