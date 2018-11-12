package leviathan143.polar.common.tileentities;

import daomephsta.umbra.nbt.NBTExtensions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityStabilisedBlock extends TileEntity
{
	private IBlockState camoBlockState;
	
	public IBlockState getCamoBlockState()
	{
		return camoBlockState;
	}

	public void setCamoBlockState(IBlockState camoBlockState)
	{
		this.camoBlockState = camoBlockState;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		// No camostate tag available for reading immediately after placement
		if (compound.hasKey("camo_blockstate"))
			this.camoBlockState = NBTExtensions.getBlockState(compound, "camo_blockstate");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		NBTExtensions.setBlockState(compound, "camo_blockstate", camoBlockState);
		return compound;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
}
