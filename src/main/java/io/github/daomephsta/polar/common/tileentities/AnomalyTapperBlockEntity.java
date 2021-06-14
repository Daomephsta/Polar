package io.github.daomephsta.polar.common.tileentities;

import java.util.function.BinaryOperator;

import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.blocks.AnomalyTapperBlock;
import io.github.daomephsta.polar.common.entities.EntityRegistry;
import io.github.daomephsta.polar.common.entities.anomalies.EntityAnomaly;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AnomalyTapperBlockEntity extends BlockEntity
{
	private EntityAnomaly attachedAnomaly;
	//Cached anomaly charge storage, because it won't change.
	private IPolarChargeStorage	anomalyChargeStorage;
	private int anomalyCheckCountdown = 10;
	
	public AnomalyTapperBlockEntity(BlockPos pos, BlockState state)
	{
		super(PolarBlockEntityTypes.ANOMALY_TAPPER, pos, state);
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, AnomalyTapperBlockEntity self)
	{
		//Decrement anomalyCheckCountdown, wrapping around to 10 when 0 is reached 
		self.anomalyCheckCountdown = Math.floorMod(self.anomalyCheckCountdown - 1, 10);
		if(self.anomalyCheckCountdown == 0 && !self.validateAnomaly())
		{
			//Detach from the invalid anomaly
			if(self.attached()) self.detachFromAnomaly();
			self.searchForAnomaly(world, pos, state);
		}
	}
	
	private boolean validateAnomaly()
	{
		return attached() && attachedAnomaly.isAlive() && 
			world.isChunkLoaded(attachedAnomaly.getBlockPos());
	}
	
	public void searchForAnomaly(World world, BlockPos pos, BlockState state)
	{
		Direction facing = state.get(AnomalyTapperBlock.FACING);
		//Search 3 blocks "forward" of the "back" of the tapper, and 0.5 blocks to either side 
		Box anomalySearchArea = new Box(
			pos.getX() - 0.5D, 
			pos.getY() + 1.0D, 
			pos.getZ() - 0.5D, 
			pos.getX() + facing.getOffsetX() * 3.0D + 1.5D, 
			pos.getY() + facing.getOffsetY() * 3.0D, 
			pos.getZ() + facing.getOffsetZ() * 3.0D + 1.5D);
		//Find the closest anomaly in the search area that is of the correct polarity, and attach to it
		world.getEntitiesByType(EntityRegistry.ANOMALY, anomalySearchArea, anomaly -> 
		        anomaly.getPolarity() == ((AnomalyTapperBlock) state.getBlock()).getPolarity())
		    .stream()
			.reduce(BinaryOperator.minBy(this::closestEntity))
			.ifPresent(this::attachTo);
	}
	
	private int closestEntity(Entity a, Entity b)
	{
	    return Double.compare(
	        pos.getSquaredDistance(a.getPos(), true /*center*/), 
	        pos.getSquaredDistance(b.getPos(), true /*center*/));
	}
	
	private void attachTo(EntityAnomaly anomaly)
	{
		anomaly.open();
		this.attachedAnomaly = anomaly;
		this.anomalyChargeStorage = PolarAPI.CHARGE_STORAGE.get(anomaly);
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
		return new BlockEntityUpdateS2CPacket(getPos(), 0, toInitialChunkDataNbt());
	}
	
	@Override
	public NbtCompound toInitialChunkDataNbt()
	{
		return writeNbt(new NbtCompound());
	}
}
