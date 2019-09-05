package io.github.daomephsta.polar.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	public static final ItemGroup TAB_RED = FabricItemGroupBuilder.create(new Identifier(PROVIDER_MOD_ID, "red"))
									.icon(() -> new ItemStack(getItem("red_resource_basic")))
									.build(),
								  TAB_BLUE = FabricItemGroupBuilder.create(new Identifier(PROVIDER_MOD_ID, "blue"))
								  	.icon(() -> new ItemStack(getItem("blue_resource_basic")))
								  	.build(),
								  TAB_OTHER = FabricItemGroupBuilder.create(new Identifier(PROVIDER_MOD_ID, "other"))
								  //.icon(() -> ItemModBook.forBook(PROVIDER_MOD_ID + ":research_journal"))
								  .build();
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
		return Registry.ITEM.get(new Identifier(PROVIDER_MOD_ID, name));
	}
	
	public static Block getBlock(String name)
	{
		return Registry.BLOCK.get(new Identifier(PROVIDER_MOD_ID, name));
	}
}
