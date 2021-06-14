package io.github.daomephsta.polar.common.tileentities;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.blocks.BlockRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder.Factory;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PolarBlockEntityTypes
{
	public static final BlockEntityType<AnomalyTapperBlockEntity> ANOMALY_TAPPER = 
			register("anomaly_tapper", AnomalyTapperBlockEntity::new, 
			    BlockRegistry.RED_ANOMALY_TAPPER, BlockRegistry.BLUE_ANOMALY_TAPPER);
	public static final BlockEntityType<StabilisedBlockBlockEntity> STABILISED_BLOCK = 
			register("stabilised_block", StabilisedBlockBlockEntity::new, BlockRegistry.STABILISED_BLOCK);

	private static <T extends BlockEntity> BlockEntityType<T> register(
	    String name, Factory<T> constructor, Block... supportedBlocks)
	{
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(Polar.MOD_ID, name), 
				FabricBlockEntityTypeBuilder.create(constructor, supportedBlocks).build());
	}
	
	public static void initialize()
	{
		//Dummy method to force static init
	}
}
