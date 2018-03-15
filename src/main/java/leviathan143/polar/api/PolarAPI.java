package leviathan143.polar.api;

import leviathan143.polar.common.capabilities.CapabilityPlayerDataPolar;
import net.minecraft.entity.player.EntityPlayer;

/**The main interface between Polar and other mods**/
public class PolarAPI
{
	/**
	 * Gets the player data for a given player. The returned object is read-only.
	 * @param player The player to get player data for.
	 * @return The player data for the given player.
	 */
	public static IReadablePlayerDataPolar getPlayerData(EntityPlayer player)
	{
		return player.getCapability(CapabilityPlayerDataPolar.PLAYER_DATA_POLAR, null);
	}
}
