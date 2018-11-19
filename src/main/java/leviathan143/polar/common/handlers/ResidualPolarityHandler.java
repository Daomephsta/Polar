package leviathan143.polar.common.handlers;

import daomephsta.umbra.nbt.NBTExtensions;
import leviathan143.polar.api.IPolarisedItem;
import leviathan143.polar.api.Polarity;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.advancements.triggers.TriggerRegistry;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar.PlayerDataPolar;
import leviathan143.polar.common.recipes.RecipeAddPolarityTag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Polar.MODID)
public class ResidualPolarityHandler
{
	@SubscribeEvent
	public static void handleArmour(LivingAttackEvent event)
	{
		/*Ignore unblockable damage sources because they ignore armour, so are not considered to
		 * "activate" it; and to avoid stack overflow*/
		if (event.getEntityLiving() instanceof EntityPlayer && !event.getSource().isUnblockable())
		{
			for (ItemStack armour : event.getEntityLiving().getArmorInventoryList())
			{
				if (Polarity.isStackPolarised(armour) && activatesOn(armour, IPolarisedItem.ActivatesOn.WEARER_ATTACKED))
				{
					itemActivated(armour, (EntityPlayer) event.getEntityLiving());
				}
			}
		}
	}

	@SubscribeEvent
	public static void handleAttackEntity(AttackEntityEvent event)
	{
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		if (Polarity.isStackPolarised(stack) 
			&& activatesOn(stack, IPolarisedItem.ActivatesOn.WEARER_ATTACK))
		{
			itemActivated(stack, event.getEntityPlayer());
		}
	}

	@SubscribeEvent
	public static void handleAttackBlock(PlayerInteractEvent.LeftClickBlock event)
	{
		if (Polarity.isStackPolarised(event.getItemStack()) 
			&& activatesOn(event.getItemStack(), IPolarisedItem.ActivatesOn.BLOCK_LEFT_CLICK))
		{
			itemActivated(event.getItemStack(), event.getEntityPlayer());
		}
	}
	
	@SubscribeEvent
	public static void handleUseItem(PlayerInteractEvent.RightClickItem event)
	{
		if (Polarity.isStackPolarised(event.getItemStack()) 
			&& activatesOn(event.getItemStack(), IPolarisedItem.ActivatesOn.ITEM_RIGHT_CLICK))
		{
			itemActivated(event.getItemStack(), event.getEntityPlayer());
		}
	}
	
	@SubscribeEvent
	public static void handleUseItemOnBlock(PlayerInteractEvent.RightClickBlock event)
	{
		if (Polarity.isStackPolarised(event.getItemStack()) 
			&& activatesOn(event.getItemStack(), IPolarisedItem.ActivatesOn.BLOCK_RIGHT_CLICK))
		{
			itemActivated(event.getItemStack(), event.getEntityPlayer());
		}
	}
	
	@SubscribeEvent
	public static void handleUseItemOnEntity(PlayerInteractEvent.EntityInteract event)
	{
		if (Polarity.isStackPolarised(event.getItemStack()) 
			&& activatesOn(event.getItemStack(), IPolarisedItem.ActivatesOn.ENTITY_RIGHT_CLICK))
		{
				itemActivated(event.getItemStack(), event.getEntityPlayer());
		}
	}
	
	/**
	 * @param stack ItemStack to check the activation types of.
	 * @param trigger The activation trigger to check for.
	 * @return true if {@code stack} activates when {@code trigger} occurs.
	 */
	private static boolean activatesOn(ItemStack stack, IPolarisedItem.ActivatesOn trigger)
	{
		if (stack.getItem() instanceof IPolarisedItem)
			return ((IPolarisedItem) stack.getItem()).activatesOn(trigger);
		if (!stack.hasTagCompound()) return false;
		if (!stack.getTagCompound().hasKey(RecipeAddPolarityTag.ACTIVATES_ON))
			return false;
		return NBTExtensions
			.contains(stack.getTagCompound().getTagList(RecipeAddPolarityTag.ACTIVATES_ON, NBT.TAG_STRING), trigger.name()); 
	}
	
	public static void itemActivated(ItemStack stack, EntityPlayer player)
	{
		PlayerDataPolar playerData = PlayerDataPolar.get(player);
		Polarity residualCharge = playerData.getResidualPolarity();
		Polarity itemPolarity = Polarity.ofStack(stack);
		// Ifs are nested to avoid shocking if a residual charge is not left, regardless of polarities
		if (residualCharge == Polarity.NONE)
		{
			/* 1 in 10 activations should leave a residual charge. This is determined server side and 
			 * synced with a packet as sequences from Randoms are not the same between server and client,
			 * though the seeds are.*/
			if (!player.world.isRemote && player.world.rand.nextFloat() <= 0.9F)
					playerData.setResidualPolarity(itemPolarity);
		}
		else if (residualCharge != itemPolarity)
		{
			// Damage source must be unblockable to avoid stack overflow
			player.attackEntityFrom(DamageSource.MAGIC, 1.0F);
			playerData.setResidualPolarity(Polarity.NONE);
			if (player instanceof EntityPlayerMP)
				TriggerRegistry.POLAR_REACTION.trigger((EntityPlayerMP) player, 0);
		}
	}
}
