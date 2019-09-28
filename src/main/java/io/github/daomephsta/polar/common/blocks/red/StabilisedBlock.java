package io.github.daomephsta.polar.common.blocks.red;

import io.github.daomephsta.polar.common.blocks.IHasSpecialBlockItem;
import io.github.daomephsta.polar.common.tileentities.StabilisedBlockBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StabilisedBlock extends Block implements IHasSpecialBlockItem, BlockEntityProvider
{
	private static final Property<Boolean> OPAQUE = BooleanProperty.of("opaque");

	public StabilisedBlock()
	{
		super(FabricBlockSettings.of(Material.SAND)
				.hardness(0.3F)
				.dynamicBounds()
				.build());
		setDefaultState(getStateFactory().getDefaultState().with(OPAQUE, true));
	}

	public BlockState stabilise(BlockState camouflague)
	{
		return this.getDefaultState().with(OPAQUE, camouflague.isOpaque());
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool)
	{
		if (blockEntity instanceof StabilisedBlockBlockEntity)
		{
			//Turn into camo when broken
			BlockState camoState = ((StabilisedBlockBlockEntity) blockEntity).getCamoBlockState();
			world.setBlockState(pos, camoState);
		}
		super.afterBreak(world, player, pos, state, blockEntity, tool);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> stateFactoryBuilder)
	{
		stateFactoryBuilder.add(OPAQUE);
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean isOpaque(BlockState state)
	{
		return state.get(OPAQUE);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos pos, EntityContext entityContext)
	{
		BlockEntity blockEntity = blockView.getBlockEntity(pos);
		if (blockEntity  instanceof StabilisedBlockBlockEntity)
			return ((StabilisedBlockBlockEntity) blockEntity).getCamoBlockState().getOutlineShape(blockView, pos, entityContext);
		return super.getOutlineShape(state, blockView, pos, entityContext);
	}
	
	@Override
	public BlockItem createBlockItem()
	{
		return null;
	}

	@Override
	public boolean hasBlockEntity()
	{
		return true;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view)
	{
		return new StabilisedBlockBlockEntity();
	}
}
