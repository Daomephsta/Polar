package leviathan143.polar.common;

import daomephsta.umbra.mcfunctions.FunctionLoader;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.common.advancements.triggers.TriggerRegistry;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar;
import leviathan143.polar.common.capabilities.CapabilityPolarChargeable;
import leviathan143.polar.common.config.PolarConfig;
import leviathan143.polar.common.core.InternalMethodAccessors;
import leviathan143.polar.common.handlers.baubles.BaubleHandler;
import leviathan143.polar.common.items.ItemRegistry;
import leviathan143.polar.common.recipes.RecipeChargeItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.patchouli.common.item.ItemModBook;

@EventBusSubscriber
@Mod(modid = Polar.MODID, name = Polar.MODNAME, version = Polar.VERSION, dependencies = Polar.DEPENDENCIES)
public class Polar
{	
	public static final String MODNAME = "Polar";
	public static final String MODID = "polar";
	public static final String VERSION = "0.0.1";
	public static final String DEPENDENCIES = "required:patchouli;required-after:baubles;required-after:umbra";
	public static final String CLIENT_PROXY_PATH = "leviathan143.polar.client.ClientProxy";
	public static final String SERVER_PROXY_PATH = "leviathan143.polar.server.ServerProxy";
	
	@SidedProxy(serverSide = Polar.SERVER_PROXY_PATH, clientSide = Polar.CLIENT_PROXY_PATH)
	public static AbstractProxy proxy;
	
	public static final CreativeTabs 
	TAB_RED = new CreativeTabs(MODID + ".red")
	{	
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ItemRegistry.RED_RESOURCE_BASIC);
		}
	},
	TAB_BLUE = new CreativeTabs(MODID + ".blue")
	{	
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ItemRegistry.BLUE_RESOURCE_BASIC);
		}
	},
	TAB_OTHER = new CreativeTabs(MODID + ".other")
	{	
		@Override
		public ItemStack createIcon()
		{
			return ItemModBook.forBook(MODID + ":research_journal");
		}
	};
	
	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
		PolarAPI.initialiseAPI(new InternalMethodAccessors());
		registerCapabilities();
		PolarConfig.preInit();
		TriggerRegistry.init();
		BaubleHandler.preInit();
	}
	
	private static void registerCapabilities()
	{
		CapabilityPlayerDataPolar.register();
		CapabilityPolarChargeable.register();
	}

	@Mod.EventHandler
	public static void init(FMLInitializationEvent event)
	{
		proxy.init(event);
		PolarConfig.init();
	}
	
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		PolarConfig.postInit();
	}
	
	@Mod.EventHandler
	public static void serverStarting(FMLServerStartingEvent event)
	{
		FunctionLoader.loadFunctionsFor(event.getServer(), Polar.MODID);
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry().registerAll(new RecipeChargeItem().setRegistryName("charge_item"));
	}
}
