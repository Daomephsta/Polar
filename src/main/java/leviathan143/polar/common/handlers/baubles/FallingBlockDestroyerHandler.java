package leviathan143.polar.common.handlers.baubles;

import java.util.ArrayDeque;
import java.util.Deque;

import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.api.capabilities.IPolarChargeStorage;
import leviathan143.polar.common.blocks.BlockRegistry;
import leviathan143.polar.common.config.PolarConfig;
import leviathan143.polar.common.handlers.ResidualPolarityHandler;
import leviathan143.polar.common.items.ItemRegistry;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;

public class FallingBlockDestroyerHandler
{	
	static void destroyFallingBlocks(BlockEvent.BreakEvent event)
	{
		boolean realPlayer = event.getPlayer() != null && !(event.getPlayer() instanceof FakePlayer);
		if (!realPlayer) 
			return;
		
		BlockPos posAbove = event.getPos().up();
		IBlockState stateAbove = event.getWorld().getBlockState(posAbove);
		if (!isSoftUnstableBlock(event.getWorld(), stateAbove, posAbove))
			return;
		ItemStack baubleStack = BaubleHandler.findEquippedBauble(event.getPlayer(), ItemRegistry.FALLING_BLOCK_DESTROYER);
		if (baubleStack.isEmpty())
			return;
		
		destroyBlockColumn(event.getPlayer(), baubleStack, event.getWorld(), posAbove);
	}

	private static void destroyBlockColumn(EntityPlayer player, ItemStack baubleStack, World world, BlockPos columnBottomPos)
	{
		Deque<BlockPos> toDestroy = new ArrayDeque<>();
		{
			BlockPos pos = new MutableBlockPos(columnBottomPos);
			IBlockState state = world.getBlockState(pos);
			while (isSoftUnstableBlock(world, state, pos) || state.getBlock() == BlockRegistry.STABILISED_BLOCK)
			{
				toDestroy.push(pos);
				pos = pos.up();
				state = world.getBlockState(pos);
			}
			toDestroy.push(columnBottomPos.down());
		}
		IPolarChargeStorage chargeable = baubleStack.getCapability(PolarAPI.CAPABILITY_CHARGEABLE, null);
		int cost = toDestroy.size() * PolarConfig.charge.percussiveDisintegratorActivationCost;
		if (chargeable.discharge(Polarity.BLUE, cost, true) == cost)
		{
			chargeable.discharge(Polarity.BLUE, cost, false);
			ResidualPolarityHandler.itemActivated(baubleStack, player);
			while (!toDestroy.isEmpty())
			{
				BlockPos pos = toDestroy.pop();
				destroyBlock(world, pos);
			}
		}
		else
			player.sendStatusMessage(new TextComponentTranslation("polar.message.insufficient_charge", cost), true);
	}

	private static void destroyBlock(World world, BlockPos pos)
	{
		if (world instanceof WorldServer)
			((WorldServer) world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX(), pos.getY(), pos.getZ(), 10, 0, 0, 0, 0.0D);
		world.playSound((EntityPlayer)null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 0.1F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
		world.destroyBlock(pos, true);
	}
	
	private static boolean isSoftUnstableBlock(World world, IBlockState state, BlockPos pos)
	{
		boolean unstableBlock = state.getBlock() instanceof BlockFalling;
		boolean softBlock = state.getBlockHardness(world, pos) <= 0.5F;
		return unstableBlock && softBlock;
	}
}
