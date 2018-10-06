package leviathan143.polar.common;

import daomephsta.umbra.mcfunctions.FunctionLoader;
import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.common.advancements.triggers.TriggerRegistry;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar;
import leviathan143.polar.common.capabilities.tapping.CapabilityTappable;
import leviathan143.polar.common.config.PolarConfig;
import leviathan143.polar.common.core.InternalMethodAccessors;
import leviathan143.polar.common.items.ItemRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = Polar.MODID, name = Polar.MODNAME, version = Polar.VERSION, dependencies = Polar.DEPENDENCIES)
public class Polar
{	
	public static final String MODNAME = "Polar";
	public static final String MODID = "polar";
	public static final String VERSION = "0.0.1";
	public static final String DEPENDENCIES = "required-before:guideapi";
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
			return new ItemStack(ItemRegistry.RED_IRRADIATED_REDSTONE);
		}
	},
	TAB_BLUE = new CreativeTabs(MODID + ".blue")
	{	
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ItemRegistry.BLUE_IRRADIATED_LAPIS);
		}
	},
	TAB_OTHER = new CreativeTabs(MODID + ".other")
	{	
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(ItemRegistry.RESEARCH_JOURNAL);
		}
	};
	
	@Mod.EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
		PolarAPI.initialiseAPI(new InternalMethodAccessors());
		CapabilityPlayerDataPolar.register();
		CapabilityTappable.register();
		PolarConfig.finishLoading();
		TriggerRegistry.init();
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}
	
	@Mod.EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
	
	@Mod.EventHandler
	public static void serverStarting(FMLServerStartingEvent event)
	{
		FunctionLoader.loadFunctionsFor(event.getServer(), Polar.MODID);
	}
}
