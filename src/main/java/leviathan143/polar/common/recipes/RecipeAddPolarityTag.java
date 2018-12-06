package leviathan143.polar.common.recipes;

import static com.google.common.collect.Streams.stream;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import daomephsta.umbra.nbt.NBTExtensions;
import daomephsta.umbra.streams.NBTCollectors;
import leviathan143.polar.api.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.*;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeAddPolarityTag implements IRecipe
{
	public static final String ACTIVATES_ON = "activates_on";
	private final IForgeRegistryEntry.Impl<IRecipe> registryEntryMetadataDelegate;
	private final IRecipe wrapped;
	private final Polarity polarity;
	private final Set<String> activatesOn;
	
	private RecipeAddPolarityTag(IRecipe wrapped, Polarity polarity, Set<String> activationTypes)
	{
		this.wrapped = wrapped;
		this.polarity = polarity;
		this.activatesOn = activationTypes;
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
		NBTTagCompound nbt = delegateResult.getTagCompound();
		NBTExtensions.setEnumConstant(nbt, CommonWords.POLARITY, polarity);
		nbt.setTag(ACTIVATES_ON, activatesOn.stream().map(NBTTagString::new).collect(NBTCollectors.toNBTList(NBTTagString.class)));
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
			Set<String> activatesOn = stream(JsonUtils.getJsonArray(json, ACTIVATES_ON))
				.map(e -> JsonUtils.getString(e, ACTIVATES_ON))
				.filter(activatesOnStr -> 
				{
					if (IPolarisedItem.ActivatesOn.valueOf(activatesOnStr) == null)
						throw new JsonSyntaxException("No activation trigger " + activatesOnStr + " exists");
					return true;
				})
				.collect(toSet());
			return new RecipeAddPolarityTag(wrapped, polarity, activatesOn);
		}
	}
}
