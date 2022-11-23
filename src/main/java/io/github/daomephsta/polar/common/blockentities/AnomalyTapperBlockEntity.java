package io.github.daomephsta.polar.common.blockentities;

import java.util.function.BinaryOperator;

import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.blocks.AnomalyTapperBlock;
import io.github.daomephsta.polar.common.blocks.BlockEntityHost;
import io.github.daomephsta.polar.common.entities.EntityRegistry;
import io.github.daomephsta.polar.common.entities.anomalies.AnomalyEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AnomalyTapperBlockEntity extends BlockEntity implements BlockEntityHost.Hosted
{
    private AnomalyEntity attachedAnomaly;
    //Cached anomaly charge storage, because it won't change.
    @SuppressWarnings("unused") // Future implementation
    private IPolarChargeStorage anomalyChargeStorage;
    private int anomalyCheckCountdown = 10;

    public AnomalyTapperBlockEntity(BlockPos pos, BlockState state)
    {
        super(PolarBlockEntityTypes.ANOMALY_TAPPER, pos, state);
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
        return attached() && attachedAnomaly.isAlive() && world.isChunkLoaded(
            ChunkSectionPos.getSectionCoord(attachedAnomaly.getBlockPos().getX()),
            ChunkSectionPos.getSectionCoord(attachedAnomaly.getBlockPos().getZ()));
    }

    public void searchForAnomaly()
    {
        Direction facing = getCachedState().get(AnomalyTapperBlock.FACING);
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
                anomaly.getPolarity() == ((AnomalyTapperBlock) getCachedState().getBlock()).getPolarity())
            .stream()
            .reduce(BinaryOperator.minBy(this::closestEntity))
            .ifPresent(this::attachTo);
    }

    private int closestEntity(Entity a, Entity b)
    {
        return Double.compare(
            Vec3d.ofCenter(pos).distanceTo(a.getPos()),
            Vec3d.ofCenter(pos).distanceTo(b.getPos()));
    }

    private void attachTo(AnomalyEntity anomaly)
    {
        anomaly.open();
        this.attachedAnomaly = anomaly;
        this.anomalyChargeStorage = PolarApi.CHARGE_STORAGE.get(anomaly);
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

    public AnomalyEntity getAttachedAnomaly()
    {
        return attachedAnomaly;
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
