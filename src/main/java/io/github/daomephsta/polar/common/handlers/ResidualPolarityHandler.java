package io.github.daomephsta.polar.common.handlers;

import io.github.daomephsta.polar.api.CommonWords;
import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.NBTExtensions;
import io.github.daomephsta.polar.common.capabilities.CapabilityPlayerDataPolar.PlayerDataPolar;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ResidualPolarityHandler
{
	public static void registerEventCallbacks()
	{
		AttackEntityCallback.EVENT.register(ResidualPolarityHandler::handleAttackEntity);
		AttackBlockCallback.EVENT.register(ResidualPolarityHandler::handleAttackBlock);
		UseItemCallback.EVENT.register(ResidualPolarityHandler::handleUseItem);
		UseBlockCallback.EVENT.register(ResidualPolarityHandler::handleUseItemOnBlock);
		UseEntityCallback.EVENT.register(ResidualPolarityHandler::handleUseItemOnEntity);
	}
	
//	TODO handle armour residual charge
//	private static void handleArmour() 
//	{
//		/*Ignore unblockable damage sources because they ignore armour, so are not
//  		considered to "activate" it; and to avoid stack overflow*/ 
//		if (event.getEntityLiving() instanceof PlayerEntity && !event.getSource().isUnblockable()) 
//		{ 
//			for (ItemStack armour : event.getEntityLiving().getArmorInventoryList()) 
//			{ 
//				if (Polarity.isStackPolarised(armour) && activatesOn(armour, IPolarisedItem.ActivatesOn.WEARER_ATTACKED)) 
//				{ 
//					itemActivated(armour, (PlayerEntity) event.getEntityLiving()); 
//				} 
//			} 
//		} 
//	}

	private static ActionResult handleAttackEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult)
	{
		ItemStack stack = player.getMainHandStack();
		if (Polarity.isStackPolarised(stack) && activatesOn(stack, IPolarisedItem.ActivatesOn.WEARER_ATTACK))
		{
			itemActivated(stack, player);
		}
		return ActionResult.PASS;
	}
	
	private static ActionResult handleAttackBlock(PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction)
	{
		ItemStack stack = player.getMainHandStack();
		if (Polarity.isStackPolarised(stack) && activatesOn(stack, IPolarisedItem.ActivatesOn.BLOCK_LEFT_CLICK))
		{
			itemActivated(stack, player);
		}
		return ActionResult.PASS;
	}
	
	private static ActionResult handleUseItem(PlayerEntity player, World world, Hand hand)
	{
		ItemStack stack = player.getMainHandStack();
		if (Polarity.isStackPolarised(stack) && activatesOn(stack, IPolarisedItem.ActivatesOn.ITEM_RIGHT_CLICK))
		{
			itemActivated(stack, player);
		}
		return ActionResult.PASS;
	}
	
	private static ActionResult handleUseItemOnBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult)
	{
		ItemStack stack = player.getMainHandStack();
		if (Polarity.isStackPolarised(stack) && activatesOn(stack, IPolarisedItem.ActivatesOn.BLOCK_RIGHT_CLICK))
		{
			itemActivated(stack, player);
		}
		return ActionResult.PASS;
	}
	
	private static ActionResult handleUseItemOnEntity(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult)
	{
		ItemStack stack = player.getMainHandStack();
		if (Polarity.isStackPolarised(stack) && activatesOn(stack, IPolarisedItem.ActivatesOn.ENTITY_RIGHT_CLICK))
		{
			itemActivated(stack, player);
		}
		return ActionResult.PASS;
	}

	/**
	 * @param stack
	 *            ItemStack to check the activation types of.
	 * @param trigger
	 *            The activation trigger to check for.
	 * @return true if {@code stack} activates when {@code trigger} occurs.
	 */
	private static boolean activatesOn(ItemStack stack,
			IPolarisedItem.ActivatesOn trigger)
	{
		if (stack.getItem() instanceof IPolarisedItem)
			return ((IPolarisedItem) stack.getItem()).activatesOn(trigger);
		if (!stack.hasTag())
			return false;
		if (!stack.getTag().containsKey(CommonWords.ACTIVATES_ON))
			return false;
		ListTag activatesOn = stack.getTag().getList(CommonWords.ACTIVATES_ON, NbtType.STRING);
		return NBTExtensions.contains(activatesOn, trigger.name());
	}

	public static void itemActivated(ItemStack stack, PlayerEntity player) 
	{ 
		PlayerDataPolar playerData = PlayerDataPolar.get(player);
		Polarity residualCharge = playerData.getResidualPolarity(),
				 itemPolarity = Polarity.ofStack(stack); 
		// Ifs are nested to avoid shocking if a residual charge is not left, regardless of polarities
		if (residualCharge == Polarity.NONE) 
		{ 

			/* 1 in 10 activations should leave a residual charge. This is determined server side and synced
			 * with a packet as sequences from Randoms are not the same between server and client, though the seeds are.*/ 
			if (!player.world.isClient && player.world.getRandom().nextFloat() <= 0.9F)
				playerData.setResidualPolarity(itemPolarity); 
		} 
		else if (residualCharge != itemPolarity) 
		{ 
			// Damage source must be unblockable to avoid stack overflow
			player.damage(DamageSource.MAGIC, 1.0F);
			playerData.setResidualPolarity(Polarity.NONE);
			/*
		  TriggerRegistry.POLAR_REACTION.trigger((PlayerEntityMP) player, 0);*/ 
		}
	}
}
