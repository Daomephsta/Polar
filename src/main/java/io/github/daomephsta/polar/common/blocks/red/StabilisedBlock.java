package io.github.daomephsta.polar.common.blocks.red;

import io.github.daomephsta.polar.common.blocks.IHasSpecialBlockItem;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;

public class StabilisedBlock extends Block implements IHasSpecialBlockItem
{
	public StabilisedBlock()
	{
		super(FabricBlockSettings.of(Material.SAND)
				.hardness(0.3F)
				.build());
	}
	
	/*TODO Stablised Block
	 * @Override public void breakBlock(World world, BlockPos pos, IBlockState
	 * state) { TileEntity te = world.getTileEntity(pos); if (te instanceof
	 * TileEntityStabilisedBlock) { //Turn into camo when broken IBlockState
	 * camoState = ((TileEntityStabilisedBlock) te).getCamoBlockState();
	 * world.setBlockState(pos, camoState); } super.breakBlock(world, pos,
	 * state); }
	 * 
	 * @Override protected BlockStateContainer createBlockState() { return new
	 * BlockStateContainer.Builder(this).add(BLOCK_ACCESS, POSITION,
	 * CAMO_STATE).build(); }
	 * 
	 * @Override public boolean canRenderInLayer(IBlockState state,
	 * BlockRenderLayer layer) { IExtendedBlockState extState =
	 * (IExtendedBlockState) state; IBlockState camoState =
	 * extState.getValue(CAMO_STATE); if(camoState != null) return
	 * camoState.getBlock().canRenderInLayer(camoState, layer) || layer ==
	 * BlockRenderLayer.TRANSLUCENT; else return true; }
	 * 
	 * @Override public boolean isOpaqueCube(IBlockState state) {
	 * IExtendedBlockState extState = (IExtendedBlockState) state; IBlockState
	 * camoState = extState.getValue(CAMO_STATE); if(camoState != null) return
	 * camoState.isOpaqueCube(); return false; }
	 * 
	 * @Override
	 * 
	 * @SuppressWarnings("deprecation") // Non-deprecated overload would be
	 * difficult to call safely public SoundType getSoundGroup(IBlockState
	 * state, World world, BlockPos pos, Entity entity) { TileEntity te =
	 * world.getTileEntity(pos); if (te instanceof TileEntityStabilisedBlock) {
	 * //Turn into camo when broken BlockState camoState =
	 * ((TileEntityStabilisedBlock) te).getCamoBlockState(); return
	 * camoState.getBlock().getSoundGroup(state); } return
	 * super.getSoundType(state, world, pos, entity); }
	 */
	
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
}
