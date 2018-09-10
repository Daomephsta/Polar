package leviathan143.polar.common.tileentities;

import java.math.RoundingMode;
import java.util.List;
import java.util.function.BinaryOperator;

import com.google.common.math.DoubleMath;

import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.capabilities.ITappable;
import leviathan143.polar.common.blocks.BlockAnomalyTapper;
import leviathan143.polar.common.entities.anomalies.EntityAnomaly;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityAnomalyTapper extends TileEntity implements ITickable
{
	private EntityAnomaly attachedAnomaly;
	//Cached anomaly tapping handler, because it won't change.
	private ITappable anomalyTapper;
	private int anomalyCheckCountdown = 10;
	
	@Override
	public void update()
	{
		//Decrement anomalyCheckCountdown, wrapping around to 10 when 0 is reached 
		anomalyCheckCountdown = Math.floorMod(anomalyCheckCountdown - 1, 10);
		if(anomalyCheckCountdown == 0 && !validateAnomaly())
		{
			//Detach from the invalid anomaly
			if(attached()) detachFromAnomaly();
			searchForAnomaly();
		}
	}
	
	private boolean validateAnomaly()
	{
		return attached() && attachedAnomaly.isEntityAlive() && 
			world.isBlockLoaded(attachedAnomaly.getPosition());
	}
	
	public void searchForAnomaly()
	{
		IBlockState state = world.getBlockState(getPos());
		EnumFacing facing = state.getValue(BlockAnomalyTapper.FACING);
		//Search 3 blocks "forward" of the "back" of the tapper, and 0.5 blocks to either side 
		AxisAlignedBB anomalySearchArea = new AxisAlignedBB(
			getPos().getX() - 0.5D, 
			getPos().getY() + 1.0D, 
			getPos().getZ() - 0.5D, 
			getPos().getX() + facing.getFrontOffsetX() * 3.0D + 1.5D, 
			getPos().getY() + facing.getFrontOffsetY() * 3.0D, 
			getPos().getZ() + facing.getFrontOffsetZ() * 3.0D + 1.5D);
		List<EntityAnomaly> anomalies = world.getEntitiesWithinAABB(EntityAnomaly.class, anomalySearchArea);
		//Find the closest anomaly in the search area that is of the correct polarity, and attach to it
		anomalies.stream().filter(anomaly -> anomaly.getPolarity() == state.getValue(BlockAnomalyTapper.POLARITY))
			.reduce(BinaryOperator.minBy((a, b) -> 
			DoubleMath.roundToInt(getDistanceSq(a.posX, a.posY, a.posZ) - getDistanceSq(b.posX, b.posY, b.posZ), RoundingMode.UP)))
			.ifPresent(this::attachTo);
	}
	
	private void attachTo(EntityAnomaly anomaly)
	{
		anomaly.open();
		this.attachedAnomaly = anomaly;
		this.anomalyTapper = anomaly.getCapability(PolarAPI.CAPABILITY_TAPPABLE, null);
	}
	
	private void detachFromAnomaly()
	{
		attachedAnomaly.close();
		this.attachedAnomaly = null;
		this.anomalyTapper = null;
	}
	
	private boolean attached()
	{
		return attachedAnomaly != null;
	}
	
	public EntityAnomaly getAttachedAnomaly()
	{
		return attachedAnomaly;
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
