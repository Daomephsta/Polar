package leviathan143.polar.common.recipes;

import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.function.BiPredicate;

import com.google.common.collect.*;
import com.google.gson.*;

import daomephsta.umbra.entity.attributes.AttributeHelper;
import daomephsta.umbra.entity.attributes.AttributeHelper.AttributeModifierOperation;
import daomephsta.umbra.streams.MiscUmbraCollectors;
import leviathan143.polar.common.Polar;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.*;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeAddAttributeModifier extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	private static final String TAG_APPLIED_RECIPES = Polar.MODID + "_applied_recipes";
	private static final Map<String, BiPredicate<EntityPlayer, ItemStack>> mainIngredientPredicates = new HashMap<>();
	static
	{
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
		{
			mainIngredientPredicates.put("equips_to_" + slot.getName(), (player, stack) ->
			{
				if (player != null) return stack.getItem().isValidArmor(stack, slot, player);
				else return EntityLiving.getSlotForItemStack(stack) == slot;
			});
		}
		mainIngredientPredicates.put("is_sword", (player, stack) -> stack.getItem() instanceof ItemSword 
			|| stack.getItem().getToolClasses(stack).contains("sword"));
	}
	//TODO: Replace with tag based ingredient in 1.13?
	private final BiPredicate<EntityPlayer, ItemStack> mainIngredientPredicate;
	private final Multimap<EntityEquipmentSlot, Map.Entry<String, AttributeModifier>> additionalModifiers;
	private final NonNullList<Ingredient> ingredients;
	private final boolean repeatable;
	private final boolean isSimple;
	
	private RecipeAddAttributeModifier(BiPredicate<EntityPlayer, ItemStack> mainIngredientPredicate, Multimap<EntityEquipmentSlot, Entry<String, AttributeModifier>> modifiers, NonNullList<Ingredient> ingredients, boolean repeatable)
	{
		this.mainIngredientPredicate = mainIngredientPredicate;
		this.additionalModifiers = modifiers;
		this.ingredients = ingredients;
		this.repeatable = repeatable;
		this.isSimple = ingredients.stream().allMatch(ingredient -> ingredient.isSimple());
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		ItemStack mainIngredient = null;
		int ingredientCount = 0;
		RecipeItemHelper helper = new RecipeItemHelper();
		List<ItemStack> ingredientStacks = new ArrayList<>();
		for (int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			ItemStack slotStack = inv.getStackInSlot(slot);
			if (!slotStack.isEmpty())
			{
				if (mainIngredientPredicate.test(ForgeHooks.getCraftingPlayer(), slotStack))
				{
					if (mainIngredient == null)
						mainIngredient = slotStack;
					else 
						return false;
				}
				else
				{
					if (isSimple) 
						helper.accountStack(slotStack, 1);
					else 
						ingredientStacks.add(slotStack);
					ingredientCount++;
				}
			}
		}
		
		if (ingredientCount != ingredients.size() || mainIngredient == null) 
			return false;
		
		//Check for repeatability
		if (!repeatable && hasBeenApplied(mainIngredient))
			return false;
		
		if (isSimple) return helper.canCraft(this, null);
		int[] matches = RecipeMatcher.findMatches(ingredientStacks, ingredients);
		return matches != null;
	}
	
	private boolean hasBeenApplied(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			return false;
		if (!nbt.hasKey(TAG_APPLIED_RECIPES))
			return false;
		for (NBTBase nbtBase : nbt.getTagList(TAG_APPLIED_RECIPES, NBT.TAG_STRING))
		{
			String appliedRecipe = ((NBTTagString) nbtBase).getString();
			if (appliedRecipe.equals(getRegistryName().toString()))
				return true;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack mainIngredient = ItemStack.EMPTY;
		for (int slot = 0; slot < inv.getSizeInventory(); slot++)
		{
			ItemStack slotStack = inv.getStackInSlot(slot);
			if (!slotStack.isEmpty() && mainIngredientPredicate.test(ForgeHooks.getCraftingPlayer(), slotStack))
			{
				mainIngredient = slotStack.copy();
				break;
			}
		}
		
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
		{
			Multimap<String, AttributeModifier> baseModifiers = mainIngredient.getItem().getAttributeModifiers(slot, mainIngredient);
			//Add modifiers
			for (Entry<String, AttributeModifier> entry : Iterables.concat(baseModifiers.entries(), additionalModifiers.get(slot)))
			{
				AttributeHelper.addAttributeModifierMerging(mainIngredient, entry.getKey(), entry.getValue(), slot);
			}
		}
		//Prevent repeated application of the modifier
		if (!repeatable)
		{
			if (!mainIngredient.hasTagCompound()) mainIngredient.setTagCompound(new NBTTagCompound());
			NBTTagList appliedRecipes;
			if (mainIngredient.getTagCompound().hasKey(TAG_APPLIED_RECIPES))
				appliedRecipes = mainIngredient.getTagCompound().getTagList(TAG_APPLIED_RECIPES, NBT.TAG_STRING);
			else
			{
				appliedRecipes = new NBTTagList();
				mainIngredient.getTagCompound().setTag(TAG_APPLIED_RECIPES, appliedRecipes);
			}
			appliedRecipes.appendTag(new NBTTagString(this.getRegistryName().toString()));
		}
	
		return mainIngredient;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return width * height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return ingredients;
	}
	
	@Override
	public boolean isDynamic()
	{
		return true;
	}
	
	public static class Factory implements IRecipeFactory
	{
		@Override
		public IRecipe parse(JsonContext context, JsonObject json)
		{
			BiPredicate<EntityPlayer, ItemStack> mainIngredientPredicate = mainIngredientPredicates.get(JsonUtils.getString(json, "validator"));
			//Modifiers
			JsonObject modifiersJSON = JsonUtils.getJsonObject(json, "modifiers");
			Multimap<EntityEquipmentSlot, Map.Entry<String, AttributeModifier>> modifiersMap = MultimapBuilder.enumKeys(EntityEquipmentSlot.class).arrayListValues().build();
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
			{
				if (!modifiersJSON.has(slot.getName())) continue;
				for (JsonElement modifierJSON : JsonUtils.getJsonArray(modifiersJSON, slot.getName()))
				{
					modifiersMap.put(slot, deserialiseAttributeModifier(JsonUtils.getJsonObject(modifierJSON, "modifier")));
				}
			}
			//Ingredients
			NonNullList<Ingredient> ingredients = Streams.stream(JsonUtils.getJsonArray(json, "additional_ingredients"))
				.map(jsonE -> CraftingHelper.getIngredient(jsonE, context))
				.collect(MiscUmbraCollectors.toNonNullList());
			
			return new RecipeAddAttributeModifier(mainIngredientPredicate, modifiersMap, ingredients, JsonUtils.getBoolean(json, "repeatable", false));
		}	
		
		private Map.Entry<String, AttributeModifier> deserialiseAttributeModifier(JsonObject json)
		{
			UUID uuid = UUID.fromString(JsonUtils.getString(json, "uuid"));
			String operationStr = JsonUtils.getString(json, "operation");
			AttributeModifierOperation operation = AttributeModifierOperation.valueOf(operationStr.toUpperCase());
			if (operation == null) throw new JsonSyntaxException("No such AttributeModifierOperation " + operationStr);
			AttributeModifier attributeModifier = AttributeHelper.createModifier(uuid, JsonUtils.getString(json, "name"), JsonUtils.getFloat(json, "amount"), operation);
			
			return new SimpleImmutableEntry<>(JsonUtils.getString(json, "attribute_name"), attributeModifier);
		}
	}
}
