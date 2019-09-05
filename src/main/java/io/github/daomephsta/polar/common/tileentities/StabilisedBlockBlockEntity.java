package io.github.daomephsta.polar.common.tileentities;

import io.github.daomephsta.polar.common.NBTExtensions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.nbt.CompoundTag;

public class StabilisedBlockBlockEntity extends BlockEntity
{
	private BlockState camoBlockState = Blocks.AIR.getDefaultState();
	
	public StabilisedBlockBlockEntity()
	{
		super(PolarBlockEntityTypes.STABILISED_BLOCK);
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
	public void fromTag(CompoundTag compound)
	{
		super.fromTag(compound);
		// No camostate tag available for reading immediately after placement
		if (compound.containsKey("camo_blockstate"))
			this.camoBlockState = NBTExtensions.getBlockState(compound, "camo_blockstate");
	}
	
	@Override
	public CompoundTag toTag(CompoundTag compound)
	{
		super.toTag(compound);
		NBTExtensions.putBlockState(compound, "camo_blockstate", camoBlockState);
		return compound;
	}
	
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket()
	{
		return new BlockEntityUpdateS2CPacket(getPos(), 0, toInitialChunkDataTag());
	}
	
	@Override
	public CompoundTag toInitialChunkDataTag()
	{
		return toTag(new CompoundTag());
	}
}
