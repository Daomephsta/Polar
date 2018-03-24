package leviathan143.polar.api.factions;

import leviathan143.polar.common.Polar;

/**
 * Represents the rank of a player or NPC entity within a faction. Unaligned players always have the rank NONE. 
 * Each faction has unique names for each rank, though the ranks are the same internally.
**/
public enum FactionRank 
{
	NONE,
	INITIATE,
	APPRENTICE,
	JOURNEYMAN,
	MASTER;
	
	/**
	 * @param alignment The faction to localise the rank for
	 * @return A string key to be used to localise the name of the rank for a particular faction
	 **/
	public String getLangKey(FactionAlignment alignment)
	{
		if(alignment == FactionAlignment.UNALIGNED) return "";
		return Polar.MODID + ".rank." + alignment.name() + "_" + this.name();
	}
}
