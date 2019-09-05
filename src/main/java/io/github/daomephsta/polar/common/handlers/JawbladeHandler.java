package io.github.daomephsta.polar.common.handlers;

import io.github.daomephsta.polar.common.items.ItemJawblade;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class JawbladeHandler
{	
	public static void registerEventCallbacks()
	{
		UseEntityCallback.EVENT.register(JawbladeHandler::onEntityInteract);
	}
	
	private static ActionResult onEntityInteract(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult)
	{
		ItemStack heldItem = player.getStackInHand(hand);
		if (entity instanceof WolfEntity)
		{
			//TODO only owners should be able to take jawblades
			if (heldItem.getItem() instanceof ItemJawblade || (heldItem.isEmpty() && player.isSneaking()))
			{
				ItemStack prevMainhandStack = setJawblade((WolfEntity) entity, heldItem.copy());
				if (!prevMainhandStack.isEmpty()) 
					player.inventory.insertStack(prevMainhandStack);
				if (!player.isCreative()) 
					heldItem.decrement(1);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
	
	/*TODO reimplement
	private static void onWolfAttack()
	{
		Entity trueSource = event.getSource().getTrueSource();
		if (trueSource instanceof WolfEntity)
		{
			WolfEntity wolf = (WolfEntity) trueSource;
			ItemStack jawblade = JawbladeHandler.getJawblade(wolf);
			if (jawblade.isEmpty()) return;
			else 
			{
				jawblade.getItem().hitEntity(jawblade, event.getEntityLiving(), wolf);
				if (jawblade.isEmpty())
                    setJawblade(wolf, ItemStack.EMPTY);
			}
		}
	}*/
	
	public static ItemStack getJawblade(WolfEntity wolf)
	{
		ItemStack mainhandStack = wolf.getEquippedStack(EquipmentSlot.MAINHAND);
		return mainhandStack.getItem() instanceof ItemJawblade ? mainhandStack : ItemStack.EMPTY; 
	}

	private static ItemStack setJawblade(WolfEntity wolf, ItemStack newJawblade)
	{
		ItemStack mainhandStack = wolf.getEquippedStack(EquipmentSlot.MAINHAND); 
		if (!mainhandStack.isEmpty())
		{
			ItemStack prevMainhandStack = mainhandStack;
			wolf.setEquippedStack(EquipmentSlot.MAINHAND, newJawblade);
			return prevMainhandStack;
		}
		else
		{
			wolf.setEquippedStack(EquipmentSlot.MAINHAND, newJawblade);
			return ItemStack.EMPTY;
		}
	}
}
