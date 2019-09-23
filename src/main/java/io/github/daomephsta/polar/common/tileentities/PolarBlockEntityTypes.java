package io.github.daomephsta.polar.common.tileentities;

import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PolarBlockEntityTypes
{
	public static final BlockEntityType<AnomalyTapperBlockEntity> ANOMALY_TAPPER = 
			register("anomaly_tapper", AnomalyTapperBlockEntity::new, BlockRegistry.RED_ANOMALY_TAPPER, BlockRegistry.BLUE_ANOMALY_TAPPER);
	public static final BlockEntityType<StabilisedBlockBlockEntity> STABILISED_BLOCK = 
			register("stabilised_block", StabilisedBlockBlockEntity::new, BlockRegistry.STABILISED_BLOCK);

	private static <T extends BlockEntity> BlockEntityType<T> register(String name, Supplier<T> constructor, Block... supportedBlocks)
	{
		return Registry.register(Registry.BLOCK_ENTITY, new Identifier(Polar.MOD_ID, name), 
				new BlockEntityType<>(constructor, ImmutableSet.copyOf(supportedBlocks), null) );
	}
	
	public static void initialize()
	{
		//Dummy method to force static init
	}
}
