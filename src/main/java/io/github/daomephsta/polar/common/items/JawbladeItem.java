package io.github.daomephsta.polar.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import io.github.daomephsta.polar.api.PolarApi;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class JawbladeItem extends Item
{
    private final ToolMaterial material;
    private final Multimap<EntityAttribute, EntityAttributeModifier> modifiers;

    public JawbladeItem(ToolMaterial material)
    {
        super(new Item.Settings()
                .group(PolarApi.TAB_OTHER)
                .maxCount(1)
                .maxDamage(material.getDurability()));
        this.material = material;
        this.modifiers = ImmutableMultimap.<EntityAttribute, EntityAttributeModifier>builder()
            .put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(
                ATTACK_DAMAGE_MODIFIER_ID, "Jawblade Attack Damage Modifier", material.getAttackDamage(),
                EntityAttributeModifier.Operation.ADDITION))
            .put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(
                ATTACK_SPEED_MODIFIER_ID, "Jawblade Attack Speed Modifier", -2.0D,
                EntityAttributeModifier.Operation.ADDITION))
            .build();
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
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot)
    {
        return slot == EquipmentSlot.MAINHAND ? modifiers : ImmutableMultimap.of();
    }

    @Override
    public boolean canRepair(ItemStack toRepair, ItemStack repair)
    {
        Ingredient repairMaterial = material.getRepairIngredient();
        return !repairMaterial.isEmpty() && repairMaterial.test(repair);
    }

    //TODO Customise applicable enchantments

    @Override
    public int getEnchantability()
    {
        return material.getEnchantability();
    }
}
