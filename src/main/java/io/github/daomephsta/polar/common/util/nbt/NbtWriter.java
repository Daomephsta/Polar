package io.github.daomephsta.polar.common.util.nbt;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

public interface NbtWriter
{
    public static NbtWriter create(NbtCompound compound)
    {
        return new NbtReaderWriterImpl(compound);
    }

    NbtWriter position(String key, BlockPos pos);
    NbtWriter blockState(String key, BlockState state);
    <E extends Enum<E>> NbtWriter enumValue(String key, E enumVal);
    <K, V> NbtWriter map(String key, Map<K, V> map, Function<K, String> keyFn, Function<V, NbtElement> valueFn);
    NbtWriter compound(String key, Consumer<NbtWriter> subWriter);
}
