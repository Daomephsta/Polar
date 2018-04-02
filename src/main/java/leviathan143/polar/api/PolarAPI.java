package leviathan143.polar.api;

import leviathan143.polar.api.internal.DummyAccessors;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/** The main interface between Polar and other mods **/
public class PolarAPI
{
	public static final String PROVIDER_MOD_ID = "polar";
	@CapabilityInject(IPlayerDataPolar.class)
	public static final Capability<IPlayerDataPolar> PLAYER_DATA_POLAR = null;

	private static IInternalMethodAccessors internalAccessors = new DummyAccessors();

	/**
	 * Provides access to internal methods without creating a hard dependency on them.
	 * All the methods of {@link IInternalMethodAccessors} are part of the API.
	 * @return dummy accessors if Polar is not loaded or if it is before preinit. Otherwise the real accessors are returned.
	 */
	public static IInternalMethodAccessors internalAccessors()
	{
		return internalAccessors;
	}

	/** Internal method used by Polar to replace dummy values with the proper values during preinit. 
	 * Other mods should <b>never</b> call this method**/
	public static void initialiseAPI(IInternalMethodAccessors internalAccessors)
	{
		ModContainer activeMod = Loader.instance().activeModContainer();
		if (!activeMod.getModId().equals(PROVIDER_MOD_ID))
			throw new UnsupportedOperationException(String.format(
					"{0} ({1}) has attempted to initialise the Polar API. Other mods should not do this, please report this to the author of {0}",
					activeMod.getName(), activeMod.getModId()));
		PolarAPI.internalAccessors = internalAccessors;
	}
}
