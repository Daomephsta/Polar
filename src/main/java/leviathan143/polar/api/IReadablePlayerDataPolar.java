package leviathan143.polar.api;

import leviathan143.polar.api.factions.FactionAlignment;
import leviathan143.polar.api.factions.FactionRank;

/**An interface that provides readonly access to Polar player data. Designed to be used by other mods.**/
public interface IReadablePlayerDataPolar
{
	public FactionAlignment getFaction();

	public FactionRank getRank();
}
