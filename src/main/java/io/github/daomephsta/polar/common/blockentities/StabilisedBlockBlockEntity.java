package io.github.daomephsta.polar.common.blockentities;

import io.github.daomephsta.polar.common.util.nbt.NbtReader;
import io.github.daomephsta.polar.common.util.nbt.NbtWriter;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class StabilisedBlockBlockEntity extends BlockEntity
{
    private BlockState camoBlockState = Blocks.AIR.getDefaultState();

    public StabilisedBlockBlockEntity(BlockPos pos, BlockState state)
    {
        super(PolarBlockEntityTypes.STABILISED_BLOCK, pos, state);
    }

    public BlockState getCamoBlockState()
    {
        return camoBlockState;
    }

    public void setCamoBlockState(BlockState camoBlockState)
    {
        this.camoBlockState = camoBlockState;
    }

    @Override
    public void readNbt(NbtCompound compound)
    {
        super.writeNbt(compound);
        var reader = NbtReader.create(compound);
        // camo_state NBT isn't available for reading immediately after placement
        if (compound.contains("camo_blockstate"))
            this.camoBlockState = reader.blockState("camo_blockstate");
    }

    @Override
    public void writeNbt(NbtCompound compound)
    {
        super.writeNbt(compound);
        NbtWriter.create(compound)
            .blockState("camo_blockstate", camoBlockState);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket()
    {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt()
    {
        return createNbt();
    }
}
