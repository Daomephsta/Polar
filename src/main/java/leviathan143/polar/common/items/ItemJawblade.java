package leviathan143.polar.common.items;

import com.google.common.collect.Multimap;

import daomephsta.umbra.entity.attributes.AttributeHelper;
import daomephsta.umbra.entity.attributes.AttributeHelper.AttributeModifierOperation;
import net.minecraft.enchantment.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ItemJawblade extends Item
{	
	private final ToolMaterial material;

	public ItemJawblade(ToolMaterial material)
	{
		this.material = material;
		setMaxStackSize(1);
		setMaxDamage(material.getMaxUses());
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);
		int fireAspectLevel = EnchantmentHelper.getFireAspectModifier(attacker);
		if (fireAspectLevel > 0) target.setFire(fireAspectLevel * 4);
		return true;
	}
	
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
		Multimap<String, AttributeModifier> attributeModifiers = super.getAttributeModifiers(slot, stack);
		if (slot == EntityEquipmentSlot.MAINHAND)
		{
			attributeModifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), 
				AttributeHelper.createModifier(ATTACK_DAMAGE_MODIFIER, "Jawblade Attack Damage Modifier", material.getAttackDamage(), 
				AttributeModifierOperation.ADDITIVE));
			attributeModifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), 
				AttributeHelper.createModifier(ATTACK_SPEED_MODIFIER, "Jawblade Attack Speed Modifier", -2.0D, 
				AttributeModifierOperation.ADDITIVE));
		}
		return attributeModifiers;
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		ItemStack repairMaterial = material.getRepairItemStack();
		return (!repairMaterial.isEmpty() && OreDictionary.itemMatches(repairMaterial, repair, false)) 
			? true
			: super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return enchantment.type == EnumEnchantmentType.WEAPON && enchantment != Enchantments.SWEEPING;
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack)
	{
		return material.getEnchantability(); 
	}
}
