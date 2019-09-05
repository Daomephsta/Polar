package io.github.daomephsta.polar.common.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.github.daomephsta.polar.api.CommonWords;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.items.itemblocks.ItemBlockAnomalyTapper;
import io.github.daomephsta.polar.common.tileentities.AnomalyTapperBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext.Builder;

//TODO Separate blocks for each polarity
public class AnomalyTapperBlock extends FacingBlock implements IHasSpecialBlockItem
{
	public static final Property<Polarity> POLARITY = EnumProperty.of(CommonWords.POLARITY, Polarity.class, Polarity.POLARISED);
	
	public AnomalyTapperBlock()
	{
		super(FabricBlockSettings.of(Material.WOOD)
				.hardness(2.0F)
				.resistance(5.0F)
				.sounds(BlockSoundGroup.WOOD)
				.build());
		
		setDefaultState(stateFactory.getDefaultState()
			.with(POLARITY, Polarity.RED)
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
	      stateFactoryBuilder.add(FACING, POLARITY);
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
	public ItemStack getPickStack(BlockView blockView, BlockPos pos, BlockState state)
	{
		return ItemBlockAnomalyTapper.forPolarity(state.get(POLARITY));
	}
	
	//TODO Reimplement as loot table
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, Builder lootContextBuilder)
	{
		return Collections.singletonList(ItemBlockAnomalyTapper.forPolarity(state.get(POLARITY)));
	}
	
	@Override
	public boolean hasBlockEntity()
	{
		return true;
	}

	@Override
	public BlockItem createBlockItem()
	{
		return new ItemBlockAnomalyTapper(this);
	}
	
	private Polarity getPolarity(ItemStack itemBlockStack)
	{
		return Polarity.valueOf(itemBlockStack.getTag().getString(CommonWords.POLARITY));
	}
}
