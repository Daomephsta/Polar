package leviathan143.polar.common.recipes;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import daomephsta.umbra.nbt.NBTExtensions;
import leviathan143.polar.api.CommonWords;
import leviathan143.polar.api.Polarity;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.*;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeAddPolarityTag implements IRecipe
{
	private final IForgeRegistryEntry.Impl<IRecipe> registryEntryMetadataDelegate;
	private final IRecipe wrapped;
	private final Polarity polarity;
	
	private RecipeAddPolarityTag(IRecipe wrapped, Polarity polarity)
	{
		this.wrapped = wrapped;
		this.polarity = polarity;
		this.registryEntryMetadataDelegate = new IForgeRegistryEntry.Impl<>();
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		return wrapped.matches(inv, worldIn);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack delegateResult = wrapped.getCraftingResult(inv);
		if (!delegateResult.hasTagCompound()) delegateResult.setTagCompound(new NBTTagCompound());
		NBTExtensions.setEnumConstant(delegateResult.getTagCompound(), CommonWords.POLARITY, polarity);
		return delegateResult;
	}

	@Override
	public boolean canFit(int width, int height)
	{
		return wrapped.canFit(width, height);
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return wrapped.getRecipeOutput();
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
		return wrapped.getRemainingItems(inv);
	}

	@Override
	public NonNullList<Ingredient> getIngredients()
	{
		return wrapped.getIngredients();
	}

	@Override
	public boolean isDynamic()
	{
		return wrapped.isDynamic();
	}

	@Override
	public String getGroup()
	{
		return wrapped.getGroup();
	}
	
	@Override
	public ResourceLocation getRegistryName()
	{
		return registryEntryMetadataDelegate.getRegistryName();
	}
	
	@Override
	public IRecipe setRegistryName(ResourceLocation name)
	{
		wrapped.setRegistryName(name);
		registryEntryMetadataDelegate.setRegistryName(name);
		return this;
	}
	
	@Override
	public Class<IRecipe> getRegistryType()
	{
		return registryEntryMetadataDelegate.getRegistryType();
	}
	
	public static class Factory implements IRecipeFactory
	{
		@Override
		public IRecipe parse(JsonContext context, JsonObject json)
		{
			IRecipe wrapped = CraftingHelper.getRecipe(JsonUtils.getJsonObject(json, "wrapped"), context);
			String polarityStr = JsonUtils.getString(json, "polarity");
			Polarity polarity = Polarity.valueOf(polarityStr.toUpperCase());
			if (polarity == null)
				throw new JsonSyntaxException("No polarity " + polarityStr + " exists");
			return new RecipeAddPolarityTag(wrapped, polarity);
		}
	}
}
