package io.github.daomephsta.polar.common.util.nbt;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

public class NbtTags
{
    public static boolean contains(NbtList list, String target)
    {
        for (NbtElement element : list)
        {
            if (element instanceof NbtString && element.asString().equals(target))
                return true;
        }
        return false;
    }
}
