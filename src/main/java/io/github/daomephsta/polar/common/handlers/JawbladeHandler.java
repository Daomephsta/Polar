package io.github.daomephsta.polar.common.handlers;

import io.github.daomephsta.polar.common.callbacks.LivingEntityHurtCallback;
import io.github.daomephsta.polar.common.items.JawbladeItem;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
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
        LivingEntityHurtCallback.EVENT.register(JawbladeHandler::onWolfAttack);
    }
    
    private static ActionResult onEntityInteract(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult)
    {
        ItemStack heldItem = player.getStackInHand(hand);
        if (entity instanceof WolfEntity wolf)
        {
            //Only owners of wolves can give or take jawblades
            boolean canAccess = wolf.isOwner(player);
            boolean validHandState = heldItem.getItem() instanceof JawbladeItem || (heldItem.isEmpty() && player.isSneaking());
            if (validHandState && canAccess)
            {
                ItemStack prevMainhandStack = wolf.getEquippedStack(EquipmentSlot.MAINHAND); 
                wolf.setStackInHand(Hand.MAIN_HAND, heldItem.copy());
                if (!prevMainhandStack.isEmpty()) 
                    player.getInventory().insertStack(prevMainhandStack);
                if (!player.isCreative()) 
                    heldItem.decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
    
    private static boolean onWolfAttack(LivingEntity living, DamageSource source, float amount)
    {
        Entity trueSource = source.getSource();
        if (trueSource instanceof WolfEntity wolf)
        {
            ItemStack jawblade = JawbladeHandler.getJawblade(wolf);
            if (jawblade.isEmpty()) 
                return true;
            else
                jawblade.getItem().postHit(jawblade, living, wolf);
        }
        return true;
    }
    
    public static ItemStack getJawblade(WolfEntity wolf)
    {
        ItemStack mainhandStack = wolf.getEquippedStack(EquipmentSlot.MAINHAND);
        return mainhandStack.getItem() instanceof JawbladeItem ? mainhandStack : ItemStack.EMPTY; 
    }
}
