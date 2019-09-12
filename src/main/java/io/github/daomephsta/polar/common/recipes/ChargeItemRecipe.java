package io.github.daomephsta.polar.common.recipes;

import io.github.daomephsta.polar.api.IPolarisedItem;
import io.github.daomephsta.polar.api.PolarAPI;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.common.items.ItemRegistry;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ChargeItemRecipe extends SpecialCraftingRecipe
{
	public static final RecipeSerializer<ChargeItemRecipe> SERIALIZER = new SpecialRecipeSerializer<>(ChargeItemRecipe::new);
	
	public ChargeItemRecipe(Identifier id)
	{
		super(id);
	}

	private static final int CHARGE_VALUE = 16;
	
	@Override
	public boolean matches(CraftingInventory inventory, World world)
	{
		boolean redFound = false, 
				blueFound = false; 
		ItemStack chargeableStack = null;
		for (int s = 0; s < inventory.getInvSize(); s++)
		{
			ItemStack stack = inventory.getInvStack(s);
			if (stack.getItem() == ItemRegistry.RED_RESOURCE_BASIC)
				redFound = true;
			else if (stack.getItem() == ItemRegistry.BLUE_RESOURCE_BASIC)
				blueFound = true;
			else if (ComponentProvider.fromItemStack(stack).hasComponent(PolarAPI.CHARGE_STORAGE))
				chargeableStack = stack;
		}
		if (chargeableStack != null)
		{
			if (redFound && blueFound)
				return false;
			Polarity itemPolarity = Polarity.ofStack(chargeableStack);
			return (itemPolarity == Polarity.RED && redFound) || (itemPolarity == Polarity.BLUE && blueFound); 
		}
		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory inventory)
	{
		ItemStack item = null;
		int chargeSources = 0;
		for (int s = 0; s < inventory.getInvSize(); s++)
		{
			ItemStack stack = inventory.getInvStack(s);
			if (stack.getItem() == ItemRegistry.RED_RESOURCE_BASIC || stack.getItem() == ItemRegistry.BLUE_RESOURCE_BASIC)
				chargeSources++;
			else if (ComponentProvider.fromItemStack(stack).hasComponent(PolarAPI.CHARGE_STORAGE))
				item = stack.copy();
		}
		// The item must exist, since matches() must return true for this method to be called
		IPolarChargeStorage chargeable = IPolarChargeStorage.get(item);
		Polarity itemPolarity = item.getItem() instanceof IPolarisedItem 
			? ((IPolarisedItem) item.getItem()).getPolarity(item) 
			: Polarity.NONE;
		int remainder = chargeable.charge(itemPolarity, CHARGE_VALUE * chargeSources, true);
		if (remainder < CHARGE_VALUE * chargeSources)
			chargeable.charge(itemPolarity, CHARGE_VALUE * chargeSources - remainder, false);
		return item;
	}
	
	@Override
	public boolean fits(int width, int height)
	{
		return width * height >= 2; 
	}

	@Override
	public ItemStack getOutput()
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return SERIALIZER;
	}
}