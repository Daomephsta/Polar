package leviathan143.polar.common.handlers;

import leviathan143.polar.common.Polar;
import leviathan143.polar.common.items.ItemJawblade;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class JawbladeHandler
{	
	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getTarget() instanceof EntityWolf)
		{
			ItemStack heldItem = event.getEntityPlayer().getHeldItem(event.getHand());
			if (heldItem.getItem() instanceof ItemJawblade || (heldItem.isEmpty() && event.getEntityPlayer().isSneaking()))
			{
				ItemStack prevMainhandStack = setJawblade((EntityWolf) event.getTarget(), heldItem.copy());
				if (!prevMainhandStack.isEmpty()) event.getEntityPlayer().addItemStackToInventory(prevMainhandStack);
				if (!event.getEntityPlayer().isCreative()) heldItem.shrink(1);
			}
		}
	}
	
	@SubscribeEvent
	public static void onWolfAttack(LivingAttackEvent event)
	{
		Entity trueSource = event.getSource().getTrueSource();
		if (trueSource instanceof EntityWolf)
		{
			EntityWolf wolf = (EntityWolf) trueSource;
			ItemStack jawblade = JawbladeHandler.getJawblade(wolf);
			if (jawblade.isEmpty()) return;
			else 
			{
				jawblade.getItem().hitEntity(jawblade, event.getEntityLiving(), wolf);
				if (jawblade.isEmpty())
                    setJawblade(wolf, ItemStack.EMPTY);
			}
		}
	}
	
	public static ItemStack getJawblade(EntityWolf wolf)
	{
		ItemStack mainhandStack = wolf.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		return mainhandStack.getItem() instanceof ItemJawblade ? mainhandStack : ItemStack.EMPTY; 
	}

	private static ItemStack setJawblade(EntityWolf wolf, ItemStack newJawblade)
	{
		ItemStack mainhandStack = wolf.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND); 
		if (!mainhandStack.isEmpty())
		{
			ItemStack prevMainhandStack = mainhandStack;
			wolf.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, newJawblade);
			return prevMainhandStack;
		}
		else
		{
			wolf.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, newJawblade);
			return ItemStack.EMPTY;
		}
	}
}
