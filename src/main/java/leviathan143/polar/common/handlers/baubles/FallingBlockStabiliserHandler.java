package leviathan143.polar.common.handlers.baubles;

import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import leviathan143.polar.common.blocks.BlockRegistry;
import leviathan143.polar.common.config.PolarConfig;
import leviathan143.polar.common.handlers.ResidualPolarityHandler;
import leviathan143.polar.common.items.ItemRegistry;
import leviathan143.polar.common.tileentities.TileEntityStabilisedBlock;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;

public class FallingBlockStabiliserHandler
{
	static void stabiliseFallingBlocks(BlockEvent.BreakEvent event)
	{
		boolean realPlayer = event.getPlayer() != null && !(event.getPlayer() instanceof FakePlayer);
		if (!realPlayer) 
			return;
		
		IBlockState stateAbove = event.getWorld().getBlockState(event.getPos().up());
		boolean unstableBlock = isUnstableBlock(stateAbove);
		if (!unstableBlock)
			return;
		ItemStack baubleStack = BaubleHandler.findEquippedBauble(event.getPlayer(), ItemRegistry.FALLING_BLOCK_STABILISER);
		if (baubleStack.isEmpty())
			return;
		
		FallingBlockStabiliserHandler.placeStabilisedBlock(event.getPlayer(), baubleStack, event.getWorld(), event.getPos().up(), stateAbove);
	}

	public static boolean isUnstableBlock(IBlockState state)
	{
		boolean unstableBlock = state.getBlock() instanceof BlockFalling;
		return unstableBlock;
	}

	public static boolean placeStabilisedBlock(EntityPlayer player, ItemStack baubleStack, World world, BlockPos pos, IBlockState camo)
	{
		IPolarChargeStorage chargeable = baubleStack.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
		int cost = PolarConfig.charge.graviticStabiliserActivationCost;
		if (BaubleHandler.checkCharge(player, baubleStack, Polarity.RED, cost, PolarConfig.charge.graviticStabiliserActivationCost * 8))
		{
			chargeable.discharge(Polarity.RED, cost, false);
			ResidualPolarityHandler.itemActivated(baubleStack, player);
			world.setBlockState(pos, BlockRegistry.STABILISED_BLOCK.getDefaultState());
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntityStabilisedBlock) ((TileEntityStabilisedBlock) te).setCamoBlockState(camo);
			return true;
		}
		return false;
	}
}
