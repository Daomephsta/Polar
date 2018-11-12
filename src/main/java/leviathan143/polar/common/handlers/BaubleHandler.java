package leviathan143.polar.common.handlers;

import baubles.api.BaublesApi;
import baubles.api.IBauble;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.blocks.BlockRegistry;
import leviathan143.polar.common.config.PolarConfig;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class BaubleHandler
{
	@SubscribeEvent
	public static void handleBlockBreak(BlockEvent.BreakEvent event)
	{
		stabiliseFallingBlocks(event);
	}
	
	private static void stabiliseFallingBlocks(BlockEvent.BreakEvent event)
	{
		boolean realPlayer = event.getPlayer() != null && !(event.getPlayer() instanceof FakePlayer);
		if (!realPlayer) 
			return;
		
		IBlockState stateAbove = event.getWorld().getBlockState(event.getPos().up());
		boolean unstableBlock = stateAbove.getBlock() instanceof BlockFalling;
		if (!unstableBlock)
			return;
		ItemStack baubleStack = findEquippedBauble(event.getPlayer(), ItemRegistry.FALLING_BLOCK_STABILISER);
		if (baubleStack.isEmpty())
			return;
		
		placeStabilisedBlock(event.getPlayer(), baubleStack, event.getWorld(), event.getPos().up(), stateAbove);
	}
	
	private static void placeStabilisedBlock(EntityPlayer player, ItemStack baubleStack, World world, BlockPos pos, IBlockState camo)
	{
		IPolarChargeStorage chargeable = baubleStack.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
		int cost = PolarConfig.charge.graviticStabiliserActivationCost;
		if (chargeable.discharge(Polarity.RED, cost, true) == cost)
		{
			chargeable.discharge(Polarity.RED, cost, false);
			ResidualPolarityHandler.itemActivated(baubleStack, player);
			world.setBlockState(pos, BlockRegistry.STABILISED_BLOCK.getDefaultState());
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TileEntityStabilisedBlock) ((TileEntityStabilisedBlock) te).setCamoBlockState(camo);
		}
	}
	
	private static ItemStack findEquippedBauble(EntityPlayer player, IBauble bauble)
	{
		IItemHandler baubles = BaublesApi.getBaublesHandler(player);
		for (int s = 0; s < baubles.getSlots(); s++)
		{
			ItemStack stack = baubles.getStackInSlot(s);
			if (stack.getItem() == bauble)
				return stack;
		}
		return ItemStack.EMPTY;
	}
}
