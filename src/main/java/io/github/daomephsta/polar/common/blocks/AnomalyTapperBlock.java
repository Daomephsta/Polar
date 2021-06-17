package io.github.daomephsta.polar.common.blocks;

import java.util.Random;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.blockentities.AnomalyTapperBlockEntity;
import io.github.daomephsta.polar.common.blockentities.PolarBlockEntityTypes;
import io.github.daomephsta.polar.common.items.itemblocks.AnomalyTapperBlockItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AnomalyTapperBlock extends FacingBlock 
    implements IHasSpecialBlockItem, BlockEntityHost<AnomalyTapperBlockEntity>
{
    private final Polarity polarity;
    
    public AnomalyTapperBlock(Polarity polarity)
    {
        super(FabricBlockSettings.of(Material.WOOD)
                .hardness(2.0F)
                .resistance(5.0F)
                .sounds(BlockSoundGroup.WOOD));
        this.polarity = polarity;
        setDefaultState(getDefaultState().with(FACING, Direction.UP));
    }
    
    @Override
    public void randomDisplayTick(BlockState stateIn, World world, BlockPos pos, Random rand)
    {
        AnomalyTapperBlockEntity tapper = (AnomalyTapperBlockEntity) world.getBlockEntity(pos);
        //The BE can be null at this time
        if(tapper != null && tapper.getAttachedAnomaly() != null)
        {
            double travelTime = 25.0D;
            for(int p = 0; p < 8; p++)
            {
                world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 
                    (tapper.getAttachedAnomaly().getX() - pos.getX() - 0.5D) / travelTime, 
                    (tapper.getAttachedAnomaly().getY() - pos.getY()) / travelTime, 
                    (tapper.getAttachedAnomaly().getZ() - pos.getZ() - 0.5D) / travelTime);
            }
        }
    }
    
    @Override
    public BlockEntityTicker<AnomalyTapperBlockEntity> getTickDelegate()
    {
        return AnomalyTapperBlockEntity::tick;
    }
    
    @Override
    public BlockEntityType<AnomalyTapperBlockEntity> getBlockEntityType()
    {
        return PolarBlockEntityTypes.ANOMALY_TAPPER;
    }
    
    @Override
    protected void appendProperties(Builder<Block, BlockState> builder)
    {
          builder.add(FACING);
    }
    
    @Override
    public BlockState getPlacementState(ItemPlacementContext placementContext)
    {
        return getDefaultState().with(FACING, placementContext.getPlayerLookDirection().getOpposite());
    }

    @Override
    public BlockItem createBlockItem()
    {
        return new AnomalyTapperBlockItem(this, getPolarity());
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new AnomalyTapperBlockEntity(blockPos, blockState);
    }

    public Polarity getPolarity()
    {
        return polarity;
    }
}
