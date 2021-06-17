package io.github.daomephsta.polar.common.blocks.red;

import io.github.daomephsta.polar.common.blockentities.StabilisedBlockBlockEntity;
import io.github.daomephsta.polar.common.blocks.IHasSpecialBlockItem;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StabilisedBlock extends Block implements IHasSpecialBlockItem, BlockEntityProvider
{
    public StabilisedBlock()
    {
        super(FabricBlockSettings.of(Material.AGGREGATE).hardness(0.3F).dynamicBounds().nonOpaque());
    }

    public BlockState stabilise(BlockState camouflague)
    {
        return this.getDefaultState();
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
    public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos pos, ShapeContext context)
    {
        BlockEntity blockEntity = blockView.getBlockEntity(pos);
        if (blockEntity  instanceof StabilisedBlockBlockEntity)
        {
            return ((StabilisedBlockBlockEntity) blockEntity).getCamoBlockState()
                .getOutlineShape(blockView, pos, context);
        }
        return super.getOutlineShape(state, blockView, pos, context);
    }
    
    @Override
    public BlockItem createBlockItem()
    {
        return null;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
    {
        return new StabilisedBlockBlockEntity(pos, state);
    }
}
