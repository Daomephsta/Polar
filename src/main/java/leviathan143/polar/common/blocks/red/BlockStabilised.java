package leviathan143.polar.common.blocks.red;

import daomephsta.umbra.shennanigans.IWrapperBlock;
import leviathan143.polar.common.blocks.IHasSpecialItemBlock;
import leviathan143.polar.common.tileentities.TileEntityStabilisedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class BlockStabilised extends Block implements IWrapperBlock, IHasSpecialItemBlock
{
	public static final IUnlistedProperty<IBlockAccess> BLOCK_ACCESS = new SimpleUnlistedProperty<>("block_access", IBlockAccess.class);
	public static final IUnlistedProperty<BlockPos> POSITION = new SimpleUnlistedProperty<>("position", BlockPos.class);
	public static final IUnlistedProperty<IBlockState> CAMO_STATE = new SimpleUnlistedProperty<>("camo_state", IBlockState.class);
	
	public BlockStabilised()
	{
		super(Material.GROUND);
		setHardness(0.3F);
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityStabilisedBlock)
		{
			//Turn into camo when broken
			IBlockState camoState = ((TileEntityStabilisedBlock) te).getCamoBlockState();
			world.setBlockState(pos, camoState);
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IExtendedBlockState extState = ((IExtendedBlockState) state)
			.withProperty(BLOCK_ACCESS, world)
			.withProperty(POSITION, pos);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityStabilisedBlock)
		{
			extState = extState.withProperty(CAMO_STATE, ((TileEntityStabilisedBlock) te).getCamoBlockState());
		}
		return extState;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer.Builder(this).add(BLOCK_ACCESS, POSITION, CAMO_STATE).build();
	}

	@Override
	public IBlockState getWrappedState(IBlockState wrapperState, IBlockAccess blockAccess, BlockPos pos)
	{
		IExtendedBlockState extState = (IExtendedBlockState) wrapperState;
		return extState.getValue(CAMO_STATE);
	}

	@Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) 
	{
		IExtendedBlockState extState = (IExtendedBlockState) state;
		IBlockState camoState = extState.getValue(CAMO_STATE);
		if(camoState != null)
			return camoState.getBlock().canRenderInLayer(camoState, layer) || layer == BlockRenderLayer.CUTOUT;
		else return true;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		IExtendedBlockState extState = (IExtendedBlockState) state;
		IBlockState camoState = extState.getValue(CAMO_STATE);
		if(camoState != null)
			return camoState.isOpaqueCube();
		return false;
	}
	
	@Override
	@SuppressWarnings("deprecation") // Non-deprecated overload would be difficult to call safely
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
	{
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileEntityStabilisedBlock)
		{
			//Turn into camo when broken
			IBlockState camoState = ((TileEntityStabilisedBlock) te).getCamoBlockState();
			return camoState.getBlock().getSoundType();
		}
		return super.getSoundType(state, world, pos, entity);
	}
	
	@Override
	public ItemBlock createItemBlock()
	{
		return null;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityStabilisedBlock();
	}
}
