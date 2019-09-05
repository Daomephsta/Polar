package io.github.daomephsta.polar.api.capabilities;

import io.github.daomephsta.polar.api.factions.FactionAlignment;
import io.github.daomephsta.polar.api.factions.FactionRank;

/**An interface that provides access to Polar player data. Designed to be used by other mods.**/
public interface IPlayerDataPolar extends ISyncableCapability
{
	public FactionAlignment getFaction();
	
	public void setFaction(FactionAlignment faction);

	public FactionRank getRank();

	public void setRank(FactionRank rank);
}
