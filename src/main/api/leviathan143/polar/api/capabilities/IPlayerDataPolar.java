package leviathan143.polar.api.capabilities;

import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.api.factions.FactionRank;

/**An interface that provides access to Polar player data. Designed to be used by other mods.**/
public interface IPlayerDataPolar
{
	public FactionAlignment getFaction();
	
	public void setFaction(FactionAlignment faction);

	public FactionRank getRank();

	public void setRank(FactionRank rank);
}
