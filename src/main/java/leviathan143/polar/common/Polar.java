package leviathan143.polar.common;

import leviathan143.polar.api.PolarAPI;
import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar;
import leviathan143.polar.common.core.InternalMethodAccessors;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid = Polar.MODID, name = Polar.MODNAME, version = Polar.VERSION, dependencies = Polar.DEPENDENCIES)
public class Polar
{	
	public static final String MODNAME = "Polar";
	public static final String MODID = "polar";
	public static final String VERSION = "0.0.1";
	public static final String DEPENDENCIES = "required-before:guideapi";
	public static final String CLIENT_PROXY_PATH = "leviathan143.polar.client.ClientProxy";
	public static final String SERVER_PROXY_PATH = "leviathan143.polar.common.ServerProxy";
	
	@SidedProxy(serverSide=Polar.SERVER_PROXY_PATH, clientSide=Polar.CLIENT_PROXY_PATH)
	public static CommonProxy proxy;
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent event)
	{
		PolarAPI.initialiseAPI(new InternalMethodAccessors());
		CapabilityPlayerDataPolar.register();
	}
}
