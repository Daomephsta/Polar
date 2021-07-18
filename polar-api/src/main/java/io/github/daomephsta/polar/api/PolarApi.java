package io.github.daomephsta.polar.api;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.daomephsta.inscribe.api.InscribeApi;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.api.components.IPolarPlayerData;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/** The main interface between Polar and other mods **/
public interface PolarApi
{
    /**ID of the mod providing the Polar API*/
    public static final String PROVIDER_MOD_ID = "polar";
    public static final ItemGroup 
        TAB_RED = FabricItemGroupBuilder.create(id("red"))
            .icon(() -> new ItemStack(getItem("red_resource_basic")))
            .build(),
        TAB_BLUE = FabricItemGroupBuilder.create(id("blue"))
            .icon(() -> new ItemStack(getItem("blue_resource_basic")))
            .build(),
        TAB_OTHER = FabricItemGroupBuilder.create(id("other"))
            .icon(() -> InscribeApi.stackOfGuide(id("research_journal")))
            .build();

    public static final ComponentKey<IPolarPlayerData> PLAYER_DATA = 
        ComponentRegistry.getOrCreate(id("player_data"), IPolarPlayerData.class);
    public static final ComponentKey<IPolarChargeStorage> CHARGE_STORAGE = 
        ComponentRegistry.getOrCreate(id("charge_storage"), IPolarChargeStorage.class);
    
    /**
     * Convenience method for registering an irradiation recipe with a block output
     * @see #registerRecipe(Polarity, Ingredient, ItemStack)
     **/
    public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Block output);
    
    /**
     * Convenience method for registering an irradiation recipe with an item output
     * @see #registerRecipe(Polarity, Ingredient, ItemStack)
     **/
    public void registerIrradiationRecipe(Polarity polarity, Ingredient input, Item output);
    
    /**
     * Registers an irradiation recipe for the given polarity with the given input and output.
     * If multiple recipes match a stack, the first recipe found will be used.
     * @param polarity the anomaly polarity required to craft this recipe. 
     * Valid values are {@code Polarity.RED} and {@code Polarity.BLUE}.
     * @param input the input Ingredient for the recipe
     * @param output the output stack for the recipe   
     **/
    public void registerIrradiationRecipe(Polarity polarity, Ingredient input, ItemStack output);
    
    public static Item getItem(String name)
    {
        return Registry.ITEM.get(id(name));
    }
    
    public static Block getBlock(String name)
    {
        return Registry.BLOCK.get(id(name));
    }

    public static Identifier id(String name)
    {
        return new Identifier(PROVIDER_MOD_ID, name);
    }
}