package io.github.daomephsta.polar.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.daomephsta.polar.api.components.IPolarChargeStorage;
import io.github.daomephsta.polar.api.components.IPolarPlayerData;
import io.github.daomephsta.polar.api.internal.DummyAccessors;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/** The main interface between Polar and other mods **/
public class PolarAPI
{
	public static final String PROVIDER_MOD_ID = "polar";
	public static final ItemGroup TAB_RED = FabricItemGroupBuilder.create(id("red"))
									.icon(() -> new ItemStack(getItem("red_resource_basic")))
									.build(),
								  TAB_BLUE = FabricItemGroupBuilder.create(id("blue"))
								  	.icon(() -> new ItemStack(getItem("blue_resource_basic")))
								  	.build(),
								  TAB_OTHER = FabricItemGroupBuilder.create(id("other"))
								  //.icon(() -> ItemModBook.forBook(PROVIDER_MOD_ID + ":research_journal"))
								  .build();
	public static final ComponentKey<IPolarPlayerData> PLAYER_DATA = 
	    ComponentRegistry.getOrCreate(id("player_data"), IPolarPlayerData.class);
	public static final ComponentKey<IPolarChargeStorage> CHARGE_STORAGE = 
	    ComponentRegistry.getOrCreate(id("charge_storage"), IPolarChargeStorage.class);
	
	private static final Logger LOGGER = LogManager.getLogger();
	

	private static IInternalMethodAccessors internalAccessors = new DummyAccessors();

	/**
	 * Provides access to internal methods without creating a hard dependency on Polar.
	 * All the methods of {@link IInternalMethodAccessors} are part of the API.
	 * @return dummy accessors if Polar is not loaded or if it is not yet initialised. Otherwise the real accessors are returned.
	 */
	public static IInternalMethodAccessors internalAccessors()
	{
		return internalAccessors;
	}

	/** 
	 * Internal method used by Polar to replace dummy values with the proper values when Polar initialises. 
	 * Other mods should <b>never</b> call this method.
	 **/
	public static void initialiseAPI(IInternalMethodAccessors internalAccessors)
	{
		LOGGER.info("[Polar API] Set internal accessors to {}", internalAccessors);
		PolarAPI.internalAccessors = internalAccessors;
	}
	
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
