package io.github.daomephsta.polar.common.items;

import com.google.common.collect.Multimap;

import io.github.daomephsta.polar.common.Polar;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class ItemJawblade extends Item
{	
	private final ToolMaterial material;

	public ItemJawblade(ToolMaterial material)
	{
		super(new Item.Settings()
				.group(Polar.TAB_OTHER)
				.maxCount(1)
				.maxDamage(material.getDurability()));
		this.material = material;
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
	{
		stack.damage(1, attacker, living -> living.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		int fireAspectLevel = EnchantmentHelper.getFireAspect(attacker);
		if (fireAspectLevel > 0) target.setOnFireFor(fireAspectLevel * 4);
		return true;
	}
	
	@Override
	public Multimap<String, EntityAttributeModifier> getModifiers(EquipmentSlot slot)
	{
		Multimap<String, EntityAttributeModifier> modifiers = super.getModifiers(slot);
		if (slot == EquipmentSlot.MAINHAND)
		{
			modifiers.put(EntityAttributes.ATTACK_DAMAGE.getId(), new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_UUID, "Jawblade Attack Damage Modifier", material.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
			modifiers.put(EntityAttributes.ATTACK_SPEED.getId(), new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_UUID, "Jawblade Attack Speed Modifier", -2.0D, EntityAttributeModifier.Operation.ADDITION));
		}
		return modifiers;
	}
	
	@Override
	public boolean canRepair(ItemStack toRepair, ItemStack repair)
	{
		Ingredient repairMaterial = material.getRepairIngredient();
		return (!repairMaterial.isEmpty() && repairMaterial.test(repair)) 
			? true
			: super.canRepair(toRepair, repair);
	}
	
	//TODO Customise applicable enchantments
	
	@Override
	public int getEnchantability()
	{
		return material.getEnchantability(); 
	}
}
