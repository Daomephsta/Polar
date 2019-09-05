package io.github.daomephsta.polar.common.blocks;

import java.util.Random;

import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.items.itemblocks.ItemBlockAnomalyTapper;
import io.github.daomephsta.polar.common.tileentities.AnomalyTapperBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class AnomalyTapperBlock extends FacingBlock implements IHasSpecialBlockItem
{
	private final Polarity polarity;
	
	public AnomalyTapperBlock(Polarity polarity)
	{
		super(FabricBlockSettings.of(Material.WOOD)
				.hardness(2.0F)
				.resistance(5.0F)
				.sounds(BlockSoundGroup.WOOD)
				.build());
		this.polarity = polarity;
		setDefaultState(stateFactory.getDefaultState()
			.with(FACING, Direction.UP));
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
					(tapper.getAttachedAnomaly().x - pos.getX() - 0.5D) / travelTime, 
					(tapper.getAttachedAnomaly().y - pos.getY()) / travelTime, 
					(tapper.getAttachedAnomaly().z - pos.getZ() - 0.5D) / travelTime);
			}
		}
	}
	
	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactoryBuilder) 
	{
	      stateFactoryBuilder.add(FACING);
	}
	
	/*TODO Tapper Placement
	 * @Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState()
			.withProperty(FACING, facing)
			.withProperty(POLARITY, getPolarity(placer.getHeldItem(hand)));
	}*/
	
	@Override
	public boolean hasBlockEntity()
	{
		return true;
	}

	@Override
	public BlockItem createBlockItem()
	{
		return new ItemBlockAnomalyTapper(this, getPolarity());
	}

	public Polarity getPolarity()
	{
		return polarity;
	}
}
