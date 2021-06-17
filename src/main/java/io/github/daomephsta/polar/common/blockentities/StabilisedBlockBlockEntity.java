package io.github.daomephsta.polar.common.blockentities;

import io.github.daomephsta.polar.common.NBTExtensions;
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
        super.readNbt(compound);
        // No camostate Nbt available for reading immediately after placement
        if (compound.contains("camo_blockstate"))
            this.camoBlockState = NBTExtensions.getBlockState(compound, "camo_blockstate");
    }
    
    @Override
    public NbtCompound writeNbt(NbtCompound compound)
    {
        super.writeNbt(compound);
        NBTExtensions.putBlockState(compound, "camo_blockstate", camoBlockState);
        return compound;
    }
    
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket()
    {
        return new BlockEntityUpdateS2CPacket(getPos(), 0, toInitialChunkDataNbt());
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt()
    {
        return writeNbt(new NbtCompound());
    }
}
