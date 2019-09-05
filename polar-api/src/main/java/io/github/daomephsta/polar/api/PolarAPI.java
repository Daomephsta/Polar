package io.github.daomephsta.polar.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.daomephsta.polar.api.internal.DummyAccessors;

/** The main interface between Polar and other mods **/
public class PolarAPI
{
	public static final String PROVIDER_MOD_ID = "polar";
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
}
