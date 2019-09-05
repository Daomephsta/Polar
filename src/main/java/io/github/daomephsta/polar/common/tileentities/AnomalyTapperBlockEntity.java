package io.github.daomephsta.polar.common.tileentities;

import java.math.RoundingMode;
import java.util.List;
import java.util.function.BinaryOperator;

import com.google.common.math.DoubleMath;

import io.github.daomephsta.polar.api.capabilities.IPolarChargeStorage;
import io.github.daomephsta.polar.common.blocks.AnomalyTapperBlock;
import io.github.daomephsta.polar.common.entities.anomalies.EntityAnomaly;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class AnomalyTapperBlockEntity extends BlockEntity implements Tickable
{
	private EntityAnomaly attachedAnomaly;
	//Cached anomaly charge storage, because it won't change.
	private IPolarChargeStorage	anomalyChargeStorage = port.Dummy.CHARGE_STORAGE;
	private int anomalyCheckCountdown = 10;
	
	public AnomalyTapperBlockEntity()
	{
		super(PolarBlockEntityTypes.ANOMALY_TAPPER);
	}
	
	@Override
	public void tick()
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
		return attached() && attachedAnomaly.isAlive() && 
			world.isBlockLoaded(attachedAnomaly.getBlockPos());
	}
	
	public void searchForAnomaly()
	{
		BlockState state = world.getBlockState(getPos());
		Direction facing = state.get(AnomalyTapperBlock.FACING);
		//Search 3 blocks "forward" of the "back" of the tapper, and 0.5 blocks to either side 
		Box anomalySearchArea = new Box(
			getPos().getX() - 0.5D, 
			getPos().getY() + 1.0D, 
			getPos().getZ() - 0.5D, 
			getPos().getX() + facing.getOffsetX() * 3.0D + 1.5D, 
			getPos().getY() + facing.getOffsetY() * 3.0D, 
			getPos().getZ() + facing.getOffsetZ() * 3.0D + 1.5D);
		List<EntityAnomaly> anomalies = world.getEntities(EntityAnomaly.class, anomalySearchArea);
		//Find the closest anomaly in the search area that is of the correct polarity, and attach to it
		anomalies.stream().filter(anomaly -> anomaly.getPolarity() == ((AnomalyTapperBlock) state.getBlock()).getPolarity())
			.reduce(BinaryOperator.minBy((a, b) -> 
			DoubleMath.roundToInt(getSquaredDistance(a.x, a.y, a.z) - getSquaredDistance(b.x, b.y, b.z), RoundingMode.UP)))
			.ifPresent(this::attachTo);
	}
	
	private void attachTo(EntityAnomaly anomaly)
	{
		anomaly.open();
		this.attachedAnomaly = anomaly;
		//TODO Charge storage
		//this.anomalyChargeStorage = anomaly.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
	}
	
	private void detachFromAnomaly()
	{
		attachedAnomaly.close();
		this.attachedAnomaly = null;
		this.anomalyChargeStorage = null;
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
